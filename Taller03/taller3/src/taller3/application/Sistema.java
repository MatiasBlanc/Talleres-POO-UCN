package taller3.application;

import java.util.ArrayList;
import java.util.LinkedList;

import taller3.domain.habilidades.Furioso;
import taller3.domain.habilidades.Habilidad;
import taller3.domain.habilidades.Mordedor;
import taller3.domain.habilidades.Regenerable;
import taller3.domain.habilidades.Volador;
import taller3.domain.minijuegos.CarreraCementerio;
import taller3.domain.minijuegos.CazaCalabazas;
import taller3.domain.minijuegos.LanzamientoLapidas;
import taller3.domain.minijuegos.Minijuego;
import taller3.domain.minijuegos.SobrevivirAmanecer;
import taller3.domain.monstruos.HombreLobo;
import taller3.domain.monstruos.Momia;
import taller3.domain.monstruos.Monstruo;
import taller3.domain.monstruos.Vampiro;
import taller3.domain.monstruos.Zombi;
import taller3.domain.sesion.EntradaHistorial;
import taller3.domain.sesion.Jugador;

public class Sistema {
    private final ArrayList<Monstruo> monstruos = new ArrayList<>();
    private final ArrayList<Jugador> jugadores = new ArrayList<>();
    private final ArrayList<Minijuego> minijuegos = new ArrayList<>();
    private final ArrayList<Boolean> minijuegoJugado = new ArrayList<>();
    private final ArrayList<Integer> puntajesMinijuego = new ArrayList<>();
    private final ArrayList<Monstruo> monstruosUsados = new ArrayList<>();
    private final LinkedList<EntradaHistorial> historial = new LinkedList<>();
    private final ArrayList<Habilidad> habilidadesEnCurso = new ArrayList<>();

    private String nombreSesion = "";
    private Jugador jugadorActual;
    private boolean jugadorExistia;
    private boolean sesionActiva;
    private int puntajeSesion;
    private int minijuegoEnCurso = -1;
    private int monstruoEnCurso = -1;
    private double potencialEnCurso;

    public Sistema() {
        minijuegos.add(new CarreraCementerio());
        minijuegos.add(new CazaCalabazas());
        minijuegos.add(new LanzamientoLapidas());
        minijuegos.add(new SobrevivirAmanecer());
        for (int i = 0; i < minijuegos.size(); i++) {
            minijuegoJugado.add(false);
            puntajesMinijuego.add(0);
        }
    }

    public String registrarMonstruo(String[] partes) {
        if (partes == null || partes.length == 0) {
            return "cantidad incorrecta de campos";
        }

        String[] campos = new String[partes.length];
        boolean hayContenido = false;
        for (int i = 0; i < partes.length; i++) {
            campos[i] = partes[i] == null ? "" : partes[i].trim();
            if (!campos[i].isEmpty()) {
                hayContenido = true;
            }
        }
        if (!hayContenido) {
            return campos.length <= 1 ? "línea vacía" : "tipo desconocido";
        }

        String tipo = campos[0];
        int esperados;
        switch (tipo) {
            case "Zombi":
            case "Momia":
                esperados = 6;
                break;
            case "Vampiro":
            case "HombreLobo":
                esperados = 7;
                break;
            default:
                return "tipo desconocido";
        }
        if (campos.length != esperados) {
            return "cantidad incorrecta de campos";
        }

        int[] valores = new int[campos.length - 1];
        for (int i = 1; i < campos.length; i++) {
            try {
                valores[i - 1] = Integer.parseInt(campos[i]);
            } catch (NumberFormatException e) {
                return "valor no numérico";
            }
        }

        int vida = valores[0];
        int velocidad = valores[1];
        int fuerza = valores[2];
        int resistencia = valores[3];
        if (!enRango(vida, 1, 200) || !enRango(velocidad, 1, 100)
                || !enRango(fuerza, 1, 100) || !enRango(resistencia, 1, 100)) {
            return "valor fuera de rango";
        }

        Monstruo nuevo;
        switch (tipo) {
            case "Zombi":
                if (!enRango(valores[4], 1, 100)) {
                    return "valor fuera de rango";
                }
                nuevo = new Zombi(vida, velocidad, fuerza, resistencia, valores[4]);
                break;
            case "Vampiro":
                if (!enRango(valores[4], 1, 100) || !enRango(valores[5], 1, 100)) {
                    return "valor fuera de rango";
                }
                nuevo = new Vampiro(vida, velocidad, fuerza, resistencia, valores[4], valores[5]);
                break;
            case "Momia":
                if (!enRango(valores[4], 1, 300)) {
                    return "valor fuera de rango";
                }
                nuevo = new Momia(vida, velocidad, fuerza, resistencia, valores[4]);
                break;
            case "HombreLobo":
                if (!enRango(valores[4], 1, 100) || !enRango(valores[5], 1, 4)) {
                    return "valor fuera de rango";
                }
                nuevo = new HombreLobo(vida, velocidad, fuerza, resistencia, valores[4], valores[5]);
                break;
            default:
                return "tipo desconocido";
        }

        monstruos.add(nuevo);
        return null;
    }

    public String registrarJugador(String[] partes) {
        if (partes == null || partes.length != 2) {
            return "cantidad incorrecta de campos";
        }

        String nombre = partes[0] == null ? "" : partes[0].trim();
        String puntajeTexto = partes[1] == null ? "" : partes[1].trim();
        if (nombre.isEmpty()) {
            return "nombre vacío";
        }
        if (nombre.indexOf(';') >= 0) {
            return "nombre inválido";
        }

        int puntaje;
        try {
            puntaje = Integer.parseInt(puntajeTexto);
        } catch (NumberFormatException e) {
            return "valor no numérico";
        }
        if (puntaje < 0) {
            return "valor fuera de rango";
        }
        if (buscarJugador(nombre) != null) {
            return "jugador duplicado";
        }

        jugadores.add(new Jugador(nombre, puntaje));
        return null;
    }

    public int cantidadMonstruos() {
        return monstruos.size();
    }

    public int cantidadJugadores() {
        return jugadores.size();
    }

    public String validarNombre(String nombre) {
        String limpio = nombre == null ? "" : nombre.trim();
        if (limpio.isEmpty()) {
            return "[!] El nombre no puede estar vacío.";
        }
        if (limpio.indexOf(';') >= 0) {
            return "[!] El nombre no puede contener el carácter ';'.";
        }
        return null;
    }

    public boolean iniciarSesion(String nombre) {
        limpiarSesion();
        nombreSesion = nombre == null ? "" : nombre.trim();
        jugadorActual = buscarJugador(nombreSesion);
        jugadorExistia = jugadorActual != null;
        sesionActiva = true;
        return jugadorExistia;
    }

    public String getNombreSesion() {
        return nombreSesion;
    }

    public boolean isJugadorConocido() {
        return jugadorExistia;
    }

    public int getRecordActual() {
        if (jugadorActual == null) {
            return 0;
        }
        return jugadorActual.getPuntaje();
    }

    public int cantidadMinijuegos() {
        return minijuegos.size();
    }

    public String getNombreMinijuego(int indice) {
        Minijuego minijuego = minijuegoSeguro(indice);
        return minijuego == null ? "" : minijuego.getNombre();
    }

    public boolean isMinijuegoJugado(int indice) {
        if (indice >= 0 && indice < minijuegoJugado.size()) {
            return minijuegoJugado.get(indice);
        }
        return false;
    }

    public int getPuntajeMinijuego(int indice) {
        if (indice >= 0 && indice < puntajesMinijuego.size()) {
            return puntajesMinijuego.get(indice);
        }
        return 0;
    }

    public String seleccionarMinijuego(int indice) {
        if (minijuegoSeguro(indice) == null) {
            return "[!] Opción fuera de rango. Intente nuevamente.";
        }
        if (isMinijuegoJugado(indice)) {
            return "[!] Ya jugaste " + getNombreMinijuego(indice) + " en esta sesión.";
        }
        minijuegoEnCurso = indice;
        monstruoEnCurso = -1;
        habilidadesEnCurso.clear();
        return null;
    }

    public String getNombreMinijuegoEnCurso() {
        return getNombreMinijuego(minijuegoEnCurso);
    }

    public String getDescripcionMinijuegoEnCurso() {
        Minijuego minijuego = minijuegoSeguro(minijuegoEnCurso);
        return minijuego == null ? "" : minijuego.getDescripcion();
    }

    public void cancelarSeleccionMinijuego() {
        minijuegoEnCurso = -1;
        cancelarSeleccionMonstruo();
    }

    public int cantidadMonstruosDisponibles() {
        int disponibles = 0;
        for (int i = 0; i < monstruos.size(); i++) {
            if (!estaUsado(monstruos.get(i))) {
                disponibles++;
            }
        }
        return disponibles;
    }

    public String describirMonstruoDisponible(int indiceDisponible) {
        Monstruo monstruo = monstruoDisponible(indiceDisponible);
        return monstruo == null ? "" : monstruo.describir();
    }

    public String seleccionarMonstruoDisponible(int indiceDisponible) {
        if (minijuegoSeguro(minijuegoEnCurso) == null) {
            return "[!] Primero debes elegir un minijuego.";
        }
        int indiceCatalogo = indiceCatalogoDisponible(indiceDisponible);
        if (indiceCatalogo < 0) {
            return "[!] Opción fuera de rango. Intente nuevamente.";
        }

        Monstruo monstruo = monstruoSeguro(indiceCatalogo);
        if (monstruo == null || estaUsado(monstruo)) {
            return "[!] Ese monstruo ya fue usado en esta sesión.";
        }

        monstruoEnCurso = indiceCatalogo;
        cargarHabilidades(monstruo);
        potencialEnCurso = minijuegos.get(minijuegoEnCurso).calcularPotencial(monstruo);
        return null;
    }

    public void cancelarSeleccionMonstruo() {
        monstruoEnCurso = -1;
        potencialEnCurso = 0;
        habilidadesEnCurso.clear();
    }

    public double getPotencialEnCurso() {
        return potencialEnCurso;
    }

    public int cantidadHabilidadesEnCurso() {
        return habilidadesEnCurso.size();
    }

    public String getNombreHabilidadEnCurso(int indice) {
        if (indice >= 0 && indice < habilidadesEnCurso.size()) {
            return habilidadesEnCurso.get(indice).getEtiqueta();
        }
        return "";
    }

    public ResultadoJugada confirmarJugada(int indiceHabilidad) {
        Minijuego minijuego = minijuegoSeguro(minijuegoEnCurso);
        Monstruo monstruo = monstruoSeguro(monstruoEnCurso);
        if (minijuego == null || monstruo == null) {
            return ResultadoJugada.error("[!] No hay una jugada en curso.");
        }
        if (isMinijuegoJugado(minijuegoEnCurso) || estaUsado(monstruo)) {
            return ResultadoJugada.error("[!] Esa jugada ya fue realizada en esta sesión.");
        }

        Habilidad habilidad = Habilidad.NINGUNA;
        String mensajeHabilidad = "";
        boolean usoHabilidad = false;
        if (indiceHabilidad != 0) {
            if (indiceHabilidad < 1 || indiceHabilidad > habilidadesEnCurso.size()) {
                return ResultadoJugada.error("[!] Opción fuera de rango. Intente nuevamente.");
            }
            habilidad = habilidadesEnCurso.get(indiceHabilidad - 1);
            mensajeHabilidad = ejecutarHabilidad(monstruo, habilidad);
            usoHabilidad = true;
        }

        boolean efectiva = usoHabilidad && habilidad == minijuego.getHabilidadPremiada();
        double multiplicador = efectiva ? 1.5 : 1.0;
        int puntaje = (int) Math.round(potencialEnCurso * 10.0 * multiplicador);

        monstruosUsados.add(monstruo);
        minijuegoJugado.set(minijuegoEnCurso, true);
        puntajesMinijuego.set(minijuegoEnCurso, puntaje);
        historial.add(new EntradaHistorial(
                minijuego.getNombre(),
                monstruo.getTipo(),
                habilidad.getEtiqueta(),
                puntaje));
        puntajeSesion += puntaje;
        cancelarSeleccionMinijuego();
        return ResultadoJugada.ok(mensajeHabilidad, usoHabilidad, efectiva, puntaje);
    }

    public String describirCatalogo(int indice) {
        Monstruo monstruo = monstruoSeguro(indice);
        if (monstruo == null) {
            return "";
        }
        String estado = estaUsado(monstruo) ? "USADO" : "DISPONIBLE";
        return monstruo.describir() + " | Habilidades: " + describirHabilidades(monstruo) + " | " + estado;
    }

    public int cantidadHistorial() {
        return historial.size();
    }

    public String getHistorialMinijuego(int indice) {
        EntradaHistorial entrada = entradaSegura(indice);
        return entrada == null ? "" : entrada.getMinijuego();
    }

    public String getHistorialMonstruo(int indice) {
        EntradaHistorial entrada = entradaSegura(indice);
        return entrada == null ? "" : entrada.getMonstruo();
    }

    public String getHistorialHabilidad(int indice) {
        EntradaHistorial entrada = entradaSegura(indice);
        return entrada == null ? "" : entrada.getHabilidad();
    }

    public int getHistorialPuntaje(int indice) {
        EntradaHistorial entrada = entradaSegura(indice);
        return entrada == null ? 0 : entrada.getPuntaje();
    }

    public int getPuntajeAcumulado() {
        return puntajeSesion;
    }

    public int cantidadRanking() {
        return jugadores.size();
    }

    public String getNombreRanking(int indice) {
        Jugador jugador = jugadorRanking(indice);
        return jugador == null ? "" : jugador.getNombre();
    }

    public int getPuntajeRanking(int indice) {
        Jugador jugador = jugadorRanking(indice);
        return jugador == null ? 0 : jugador.getPuntaje();
    }

    public ResultadoSesion terminarSesion() {
        if (!sesionActiva) {
            return new ResultadoSesion(0, false, false, false, 0, false);
        }

        int total = puntajeSesion;
        boolean jugo = !historial.isEmpty();
        boolean eraNuevo = !jugadorExistia;
        boolean supero = false;
        int anterior = jugadorExistia && jugadorActual != null ? jugadorActual.getPuntaje() : 0;
        boolean guardar = false;

        if (jugo) {
            guardar = true;
            if (!jugadorExistia) {
                Jugador nuevo = new Jugador(nombreSesion, total);
                jugadores.add(nuevo);
                jugadorActual = nuevo;
                jugadorExistia = true;
            } else if (jugadorActual != null && total > jugadorActual.getPuntaje()) {
                supero = true;
                jugadorActual.setPuntaje(total);
            }
        }

        sesionActiva = false;
        limpiarSesion();
        return new ResultadoSesion(total, jugo, eraNuevo, supero, anterior, guardar);
    }

    public String lineaRegistro(int indice) {
        if (indice >= 0 && indice < jugadores.size()) {
            Jugador jugador = jugadores.get(indice);
            return jugador.getNombre() + ";" + jugador.getPuntaje();
        }
        return "";
    }

    private void limpiarSesion() {
        puntajeSesion = 0;
        historial.clear();
        monstruosUsados.clear();
        for (int i = 0; i < minijuegoJugado.size(); i++) {
            minijuegoJugado.set(i, false);
            puntajesMinijuego.set(i, 0);
        }
        cancelarSeleccionMinijuego();
    }

    private void cargarHabilidades(Monstruo monstruo) {
        habilidadesEnCurso.clear();
        if (monstruo instanceof Mordedor) {
            habilidadesEnCurso.add(Habilidad.MORDER);
        }
        if (monstruo instanceof Volador) {
            habilidadesEnCurso.add(Habilidad.VOLAR);
        }
        if (monstruo instanceof Regenerable) {
            habilidadesEnCurso.add(Habilidad.REGENERARSE);
        }
        if (monstruo instanceof Furioso) {
            habilidadesEnCurso.add(Habilidad.ENFURECERSE);
        }
    }

    private String describirHabilidades(Monstruo monstruo) {
        String texto = "";
        if (monstruo instanceof Mordedor) {
            texto = agregarHabilidad(texto, Habilidad.MORDER.getEtiqueta());
        }
        if (monstruo instanceof Volador) {
            texto = agregarHabilidad(texto, Habilidad.VOLAR.getEtiqueta());
        }
        if (monstruo instanceof Regenerable) {
            texto = agregarHabilidad(texto, Habilidad.REGENERARSE.getEtiqueta());
        }
        if (monstruo instanceof Furioso) {
            texto = agregarHabilidad(texto, Habilidad.ENFURECERSE.getEtiqueta());
        }
        return texto.isEmpty() ? "Ninguna" : texto;
    }

    private String agregarHabilidad(String texto, String etiqueta) {
        if (texto.isEmpty()) {
            return etiqueta;
        }
        return texto + ", " + etiqueta;
    }

    private String ejecutarHabilidad(Monstruo monstruo, Habilidad habilidad) {
        if (habilidad == Habilidad.MORDER && monstruo instanceof Mordedor) {
            return ((Mordedor) monstruo).morder();
        }
        if (habilidad == Habilidad.VOLAR && monstruo instanceof Volador) {
            return ((Volador) monstruo).volar();
        }
        if (habilidad == Habilidad.REGENERARSE && monstruo instanceof Regenerable) {
            return ((Regenerable) monstruo).regenerarse();
        }
        if (habilidad == Habilidad.ENFURECERSE && monstruo instanceof Furioso) {
            return ((Furioso) monstruo).enfurecerse();
        }
        return "";
    }

    private boolean estaUsado(Monstruo monstruo) {
        return monstruo != null && monstruosUsados.contains(monstruo);
    }

    private int indiceCatalogoDisponible(int indiceDisponible) {
        if (indiceDisponible < 0) {
            return -1;
        }
        int vistos = 0;
        for (int i = 0; i < monstruos.size(); i++) {
            if (!estaUsado(monstruos.get(i))) {
                if (vistos == indiceDisponible) {
                    return i;
                }
                vistos++;
            }
        }
        return -1;
    }

    private Monstruo monstruoDisponible(int indiceDisponible) {
        return monstruoSeguro(indiceCatalogoDisponible(indiceDisponible));
    }

    private Jugador jugadorRanking(int indice) {
        ArrayList<Jugador> ranking = rankingOrdenado();
        return jugadorSeguro(ranking, indice);
    }

    private ArrayList<Jugador> rankingOrdenado() {
        ArrayList<Jugador> ranking = new ArrayList<>();
        for (int i = 0; i < jugadores.size(); i++) {
            ranking.add(jugadores.get(i));
        }

        for (int i = 0; i < ranking.size(); i++) {
            int mejor = i;
            for (int j = i + 1; j < ranking.size(); j++) {
                if (esMejorEnRanking(ranking.get(j), ranking.get(mejor))) {
                    mejor = j;
                }
            }
            if (mejor != i) {
                Jugador temporal = ranking.get(i);
                ranking.set(i, ranking.get(mejor));
                ranking.set(mejor, temporal);
            }
        }
        return ranking;
    }

    private boolean esMejorEnRanking(Jugador candidato, Jugador actual) {
        if (candidato.getPuntaje() != actual.getPuntaje()) {
            return candidato.getPuntaje() > actual.getPuntaje();
        }
        return candidato.getNombre().compareToIgnoreCase(actual.getNombre()) < 0;
    }

    private Jugador buscarJugador(String nombre) {
        for (int i = 0; i < jugadores.size(); i++) {
            if (jugadores.get(i).getNombre().equals(nombre)) {
                return jugadores.get(i);
            }
        }
        return null;
    }

    private boolean enRango(int valor, int minimo, int maximo) {
        return valor >= minimo && valor <= maximo;
    }

    private Monstruo monstruoSeguro(int indice) {
        if (indice >= 0 && indice < monstruos.size()) {
            return monstruos.get(indice);
        }
        return null;
    }

    private Minijuego minijuegoSeguro(int indice) {
        if (indice >= 0 && indice < minijuegos.size()) {
            return minijuegos.get(indice);
        }
        return null;
    }

    private EntradaHistorial entradaSegura(int indice) {
        if (indice >= 0 && indice < historial.size()) {
            return historial.get(indice);
        }
        return null;
    }

    private Jugador jugadorSeguro(ArrayList<Jugador> lista, int indice) {
        if (lista != null && indice >= 0 && indice < lista.size()) {
            return lista.get(indice);
        }
        return null;
    }
}
