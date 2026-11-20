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
    private final boolean[] minijuegosJugados = new boolean[4];
    private final int[] puntajesMinijuegos = new int[4];
    private final ArrayList<Monstruo> monstruosUsados = new ArrayList<>();
    private final LinkedList<EntradaHistorial> historial = new LinkedList<>();

    private String nombreSesion = "";
    private Jugador jugadorActual;
    private int puntajeSesion;

    public Sistema() {
        minijuegos.add(new CarreraCementerio());
        minijuegos.add(new CazaCalabazas());
        minijuegos.add(new LanzamientoLapidas());
        minijuegos.add(new SobrevivirAmanecer());
    }

    public String registrarMonstruo(String[] partes) {
        if (partes == null || partes.length < 6) {
            return "cantidad incorrecta de campos";
        }

        String tipo = partes[0].trim();
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
        if (partes.length != esperados) {
            return "cantidad incorrecta de campos";
        }

        int[] valores = new int[partes.length - 1];
        for (int i = 1; i < partes.length; i++) {
            try {
                valores[i - 1] = Integer.parseInt(partes[i].trim());
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

        String nombre = partes[0].trim();
        if (nombre.isEmpty()) {
            return "nombre vacío";
        }

        try {
            int puntaje = Integer.parseInt(partes[1].trim());
            jugadores.add(new Jugador(nombre, puntaje));
            return null;
        } catch (NumberFormatException e) {
            return "valor no numérico";
        }
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
        puntajeSesion = 0;
        historial.clear();
        monstruosUsados.clear();
        for (int i = 0; i < minijuegosJugados.length; i++) {
            minijuegosJugados[i] = false;
            puntajesMinijuegos[i] = 0;
        }

        nombreSesion = nombre.trim();
        jugadorActual = buscarJugador(nombreSesion);
        return jugadorActual != null;
    }

    public String getNombreSesion() {
        return nombreSesion;
    }

    public int getRecordActual() {
        return jugadorActual == null ? 0 : jugadorActual.getPuntaje();
    }

    public int cantidadMinijuegos() {
        return minijuegos.size();
    }

    public String getNombreMinijuego(int indice) {
        if (indice >= 0 && indice < minijuegos.size()) {
            return minijuegos.get(indice).getNombre();
        }
        return "";
    }

    public String getDescripcionMinijuego(int indice) {
        if (indice >= 0 && indice < minijuegos.size()) {
            return minijuegos.get(indice).getDescripcion();
        }
        return "";
    }

    public boolean isMinijuegoJugado(int indice) {
        if (indice >= 0 && indice < minijuegosJugados.length) {
            return minijuegosJugados[indice];
        }
        return false;
    }

    public int getPuntajeMinijuego(int indice) {
        if (indice >= 0 && indice < puntajesMinijuegos.length) {
            return puntajesMinijuegos[indice];
        }
        return 0;
    }

    public int cantidadMonstruosDisponibles() {
        int disponibles = 0;
        for (Monstruo m : monstruos) {
            if (!monstruosUsados.contains(m)) {
                disponibles++;
            }
        }
        return disponibles;
    }

    public String describirMonstruoDisponible(int indiceDisponible) {
        Monstruo m = getMonstruoDisponible(indiceDisponible);
        return m == null ? "" : m.describir();
    }

    public double calcularPotencial(int idxMinijuego, int idxMonstruoDisponible) {
        Minijuego juego = minijuegos.get(idxMinijuego);
        Monstruo m = getMonstruoDisponible(idxMonstruoDisponible);
        return juego.calcularPotencial(m);
    }

    public int cantidadHabilidades(int idxMonstruoDisponible) {
        Monstruo m = getMonstruoDisponible(idxMonstruoDisponible);
        return m == null ? 0 : obtenerHabilidades(m).size();
    }

    public String getNombreHabilidad(int idxMonstruoDisponible, int idxHabilidad) {
        Monstruo m = getMonstruoDisponible(idxMonstruoDisponible);
        if (m == null) return "";
        ArrayList<Habilidad> habs = obtenerHabilidades(m);
        if (idxHabilidad >= 0 && idxHabilidad < habs.size()) {
            return habs.get(idxHabilidad).getEtiqueta();
        }
        return "";
    }

    public ResultadoJugada jugar(int idxMinijuego, int idxMonstruoDisponible, int opcionHabilidad) {
        Minijuego juego = minijuegos.get(idxMinijuego);
        Monstruo m = getMonstruoDisponible(idxMonstruoDisponible);
        double potencial = juego.calcularPotencial(m);

        ArrayList<Habilidad> habs = obtenerHabilidades(m);
        String mensajeHabilidad = "";
        boolean efectiva = false;
        double multiplicador = 1.0;

        if (opcionHabilidad > 0 && opcionHabilidad <= habs.size()) {
            Habilidad elegida = habs.get(opcionHabilidad - 1);
            mensajeHabilidad = ejecutarHabilidad(m, elegida);
            if (elegida == juego.getHabilidadPremiada()) {
                efectiva = true;
                multiplicador = 1.5;
            }
        }

        int puntaje = (int) Math.round(potencial * 10.0 * multiplicador);
        monstruosUsados.add(m);
        minijuegosJugados[idxMinijuego] = true;
        puntajesMinijuegos[idxMinijuego] = puntaje;
        puntajeSesion += puntaje;

        String nombreHab = (opcionHabilidad > 0 && opcionHabilidad <= habs.size())
                ? habs.get(opcionHabilidad - 1).getEtiqueta() : "Ninguna";
        historial.add(new EntradaHistorial(juego.getNombre(), m.getTipo(), nombreHab, puntaje));

        return new ResultadoJugada(mensajeHabilidad, efectiva, puntaje);
    }

    public String describirCatalogo(int indice) {
        if (indice < 0 || indice >= monstruos.size()) return "";
        Monstruo m = monstruos.get(indice);
        String estado = monstruosUsados.contains(m) ? "USADO" : "DISPONIBLE";
        return m.describir() + " | Habilidades: " + describirHabilidades(m) + " | " + estado;
    }

    public int cantidadHistorial() {
        return historial.size();
    }

    public String getHistorialMinijuego(int indice) {
        return (indice >= 0 && indice < historial.size()) ? historial.get(indice).getMinijuego() : "";
    }

    public String getHistorialMonstruo(int indice) {
        return (indice >= 0 && indice < historial.size()) ? historial.get(indice).getMonstruo() : "";
    }

    public String getHistorialHabilidad(int indice) {
        return (indice >= 0 && indice < historial.size()) ? historial.get(indice).getHabilidad() : "";
    }

    public int getHistorialPuntaje(int indice) {
        return (indice >= 0 && indice < historial.size()) ? historial.get(indice).getPuntaje() : 0;
    }

    public int getPuntajeAcumulado() {
        return puntajeSesion;
    }

    public int cantidadRanking() {
        return jugadores.size();
    }

    public String getNombreRanking(int indice) {
        ArrayList<Jugador> ranking = rankingOrdenado();
        return (indice >= 0 && indice < ranking.size()) ? ranking.get(indice).getNombre() : "";
    }

    public int getPuntajeRanking(int indice) {
        ArrayList<Jugador> ranking = rankingOrdenado();
        return (indice >= 0 && indice < ranking.size()) ? ranking.get(indice).getPuntaje() : 0;
    }

    public ResultadoSesion terminarSesion() {
        int total = puntajeSesion;
        boolean jugo = !historial.isEmpty();
        boolean nuevo = (jugadorActual == null);
        boolean supero = false;
        int anterior = (jugadorActual != null) ? jugadorActual.getPuntaje() : 0;

        if (jugo) {
            if (nuevo) {
                Jugador j = new Jugador(nombreSesion, total);
                jugadores.add(j);
                jugadorActual = j;
            } else if (total > jugadorActual.getPuntaje()) {
                supero = true;
                jugadorActual.setPuntaje(total);
            }
        }

        return new ResultadoSesion(total, jugo, nuevo, supero, anterior);
    }

    public String lineaRegistro(int indice) {
        if (indice >= 0 && indice < jugadores.size()) {
            Jugador j = jugadores.get(indice);
            return j.getNombre() + ";" + j.getPuntaje();
        }
        return "";
    }

    private Monstruo getMonstruoDisponible(int indiceDisponible) {
        int vistos = 0;
        for (Monstruo m : monstruos) {
            if (!monstruosUsados.contains(m)) {
                if (vistos == indiceDisponible) {
                    return m;
                }
                vistos++;
            }
        }
        return null;
    }

    private ArrayList<Habilidad> obtenerHabilidades(Monstruo m) {
        ArrayList<Habilidad> lista = new ArrayList<>();
        if (m instanceof Mordedor) lista.add(Habilidad.MORDER);
        if (m instanceof Volador) lista.add(Habilidad.VOLAR);
        if (m instanceof Regenerable) lista.add(Habilidad.REGENERARSE);
        if (m instanceof Furioso) lista.add(Habilidad.ENFURECERSE);
        return lista;
    }

    private String describirHabilidades(Monstruo m) {
        ArrayList<Habilidad> habs = obtenerHabilidades(m);
        if (habs.isEmpty()) return "Ninguna";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < habs.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(habs.get(i).getEtiqueta());
        }
        return sb.toString();
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

    private ArrayList<Jugador> rankingOrdenado() {
        ArrayList<Jugador> ranking = new ArrayList<>();
        for (int i = 0; i < jugadores.size(); i++) {
            ranking.add(jugadores.get(i));
        }

        for (int i = 0; i < ranking.size(); i++) {
            int mejor = i;
            for (int j = i + 1; j < ranking.size(); j++) {
                if (ranking.get(j).getPuntaje() > ranking.get(mejor).getPuntaje()) {
                    mejor = j;
                } else if (ranking.get(j).getPuntaje() == ranking.get(mejor).getPuntaje()) {
                    if (ranking.get(j).getNombre().compareToIgnoreCase(ranking.get(mejor).getNombre()) < 0) {
                        mejor = j;
                    }
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

    private Jugador buscarJugador(String nombre) {
        for (Jugador j : jugadores) {
            if (j.getNombre().equalsIgnoreCase(nombre)) {
                return j;
            }
        }
        return null;
    }

    private boolean enRango(int valor, int minimo, int maximo) {
        return valor >= minimo && valor <= maximo;
    }
}
