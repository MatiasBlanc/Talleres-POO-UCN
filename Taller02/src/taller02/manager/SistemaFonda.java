package taller02.manager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import taller02.domain.Choripan;
import taller02.domain.Consumo;
import taller02.domain.Participante;
import taller02.domain.Terremoto;

/**
 * Gestor principal del sistema de la fonda.
 * Administra los vectores estaticos de participantes y consumos,
 * la persistencia y las operaciones del dominio.
 */
public class SistemaFonda {

    public static final int CAPACIDAD_MAXIMA = 200;

    private Participante[] participantes;
    private int cantidadParticipantes;

    private Consumo[] consumos;
    private int cantidadConsumos;

    private String rutaParticipantes;
    private String rutaConsumido;

    public SistemaFonda() {
        this.participantes = new Participante[CAPACIDAD_MAXIMA];
        this.cantidadParticipantes = 0;
        this.consumos = new Consumo[CAPACIDAD_MAXIMA];
        this.cantidadConsumos = 0;

        this.rutaParticipantes = resolverRutaArchivo("Participantes.txt");
        this.rutaConsumido = resolverRutaArchivo("Consumido.txt");
    }

    /**
     * Busca la ruta existente mas adecuada para un archivo txt.
     * @param nombreArchivo nombre del archivo
     * @return ruta del archivo
     */
    private String resolverRutaArchivo(String nombreArchivo) {
        String[] posiblesRutas = {
            "txts/" + nombreArchivo,
            nombreArchivo,
            "Taller02/txts/" + nombreArchivo,
            "Taller02/" + nombreArchivo
        };
        for (int i = 0; i < posiblesRutas.length; i++) {
            File f = new File(posiblesRutas[i]);
            if (f.exists() && f.isFile()) {
                return posiblesRutas[i];
            }
        }
        return "txts/" + nombreArchivo;
    }

    /**
     * Carga los participantes desde el archivo Participantes.txt.
     */
    public void cargarParticipantes() {
        File archivo = new File(this.rutaParticipantes);
        if (!archivo.exists()) {
            System.out.println(">> ADVERTENCIA: no se encontro el archivo \"" + this.rutaParticipantes + "\".");
            return;
        }

        Scanner sc = null;
        try {
            sc = new Scanner(archivo);
            while (sc.hasNextLine()) {
                String linea = sc.nextLine().trim();
                if (linea.isEmpty()) {
                    continue;
                }
                if (cantidadParticipantes < CAPACIDAD_MAXIMA) {
                    participantes[cantidadParticipantes] = new Participante(linea);
                    cantidadParticipantes++;
                } else {
                    System.out.println(">> Advertencia: se alcanzo la capacidad maxima de participantes (" + CAPACIDAD_MAXIMA + ").");
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println(">> Error al leer el archivo de participantes: " + e.getMessage());
        } finally {
            if (sc != null) {
                sc.close();
            }
        }
    }

    /**
     * Carga los consumos desde el archivo Consumido.txt tolerando lineas mal formadas.
     */
    public void cargarConsumidos() {
        File archivo = new File(this.rutaConsumido);
        if (!archivo.exists()) {
            System.out.println(">> ADVERTENCIA: no se encontro el archivo \"Consumido.txt\".");
            System.out.println("   Se iniciara el programa sin registros previos.");
            return;
        }

        Scanner sc = null;
        try {
            sc = new Scanner(archivo);
            while (sc.hasNextLine()) {
                String linea = sc.nextLine().trim();
                if (linea.isEmpty()) {
                    continue;
                }
                if (cantidadConsumos >= CAPACIDAD_MAXIMA) {
                    System.out.println(">> Advertencia: se alcanzo la capacidad maxima de consumos (" + CAPACIDAD_MAXIMA + ").");
                    break;
                }

                String[] partes = linea.split(";");
                if (partes.length < 4) {
                    continue; // Descarta tolerante linea incompleta
                }

                String tipo = partes[0].trim();
                String consumidor = partes[1].trim();

                try {
                    if (tipo.equalsIgnoreCase("Choripan") && partes.length >= 5) {
                        double largo = Double.parseDouble(partes[2].trim());
                        double ancho = Double.parseDouble(partes[3].trim());
                        String fecha = partes[4].trim();
                        consumos[cantidadConsumos] = new Choripan(consumidor, fecha, largo, ancho);
                        cantidadConsumos++;
                    } else if (tipo.equalsIgnoreCase("Terremoto")) {
                        double litros = Double.parseDouble(partes[2].trim());
                        String fecha = partes[3].trim();
                        consumos[cantidadConsumos] = new Terremoto(consumidor, fecha, litros);
                        cantidadConsumos++;
                    }
                } catch (NumberFormatException e) {
                    // Linea mal formada descartada tolerante
                    continue;
                }
            }
        } catch (IOException e) {
            System.out.println(">> Error al leer el archivo de consumos: " + e.getMessage());
        } finally {
            if (sc != null) {
                sc.close();
            }
        }
    }

    public int getCantidadConsumos() {
        return cantidadConsumos;
    }

    public int getCantidadParticipantes() {
        return cantidadParticipantes;
    }

    public Participante[] getParticipantes() {
        return participantes;
    }

    public Consumo[] getConsumos() {
        return consumos;
    }

    /**
     * Cuenta el total de choripanes en el sistema.
     * @return cantidad de choripanes
     */
    public int contarChoripanes() {
        int cuenta = 0;
        for (int i = 0; i < cantidadConsumos; i++) {
            if (consumos[i] instanceof Choripan) {
                cuenta++;
            }
        }
        return cuenta;
    }

    /**
     * Cuenta el total de terremotos en el sistema.
     * @return cantidad de terremotos
     */
    public int contarTerremotos() {
        int cuenta = 0;
        for (int i = 0; i < cantidadConsumos; i++) {
            if (consumos[i] instanceof Terremoto) {
                cuenta++;
            }
        }
        return cuenta;
    }

    /**
     * Muestra la tabla detallada de choripanes registrados con sus kilocalorias.
     */
    public void mostrarChoripanes() {
        System.out.println("=============== CHORIPANES =================");
        System.out.printf("  %-4s%-14s%-8s%-8s%-14s%-8s%n", "#", "Consumidor", "Largo", "Ancho", "Fecha", "KCal");
        int cuenta = 0;
        for (int i = 0; i < cantidadConsumos; i++) {
            if (consumos[i] instanceof Choripan) {
                cuenta++;
                Choripan ch = (Choripan) consumos[i];
                String largoStr = ((long) ch.getLargo() == ch.getLargo()) ? ((long) ch.getLargo() + " cm") : (ch.getLargo() + " cm");
                String anchoStr = ((long) ch.getAncho() == ch.getAncho()) ? ((long) ch.getAncho() + " cm") : (ch.getAncho() + " cm");
                System.out.printf("  %-4d%-14s%-8s%-8s%-14s%8.2f%n",
                        cuenta,
                        ch.getConsumidor(),
                        largoStr,
                        anchoStr,
                        ch.getFecha(),
                        ch.calcularKcal());
            }
        }
        System.out.println("--------------------------------------------------");
        System.out.println("  Total: " + cuenta + " choripanes registrados");
        System.out.println("==================================================");
    }

    /**
     * Muestra la tabla detallada de terremotos registrados con sus litros y kilocalorias.
     */
    public void mostrarTerremotos() {
        System.out.println("=============== TERREMOTOS =================");
        System.out.printf("  %-4s%-14s%-9s%-14s%-8s%n", "#", "Consumidor", "Litros", "Fecha", "KCal");
        int cuenta = 0;
        double sumaLitros = 0.0;
        for (int i = 0; i < cantidadConsumos; i++) {
            if (consumos[i] instanceof Terremoto) {
                cuenta++;
                Terremoto t = (Terremoto) consumos[i];
                sumaLitros += t.getLitros();
                System.out.printf("  %-4d%-14s%5.2f L   %-14s%8.2f%n",
                        cuenta,
                        t.getConsumidor(),
                        t.getLitros(),
                        t.getFecha(),
                        t.calcularKcal());
            }
        }
        System.out.println("--------------------------------------------------");
        System.out.printf(java.util.Locale.US, "  Total: %d terremotos registrados (%.2f L)%n", cuenta, sumaLitros);
        System.out.println("==================================================");
    }

    /**
     * Guarda todos los consumos en memoria dentro del archivo Consumido.txt.
     * @return true si se guardo con exito, false si ocurrio un error
     */
    public boolean guardarConsumidos() {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(this.rutaConsumido, false));
            for (int i = 0; i < cantidadConsumos; i++) {
                if (consumos[i] != null) {
                    bw.write(consumos[i].aFormatoArchivo());
                    bw.newLine();
                }
            }
            return true;
        } catch (IOException e) {
            System.out.println(">> Error al guardar en \"" + this.rutaConsumido + "\": " + e.getMessage());
            return false;
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    System.out.println(">> Error al cerrar el archivo: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Agrega un nuevo consumo de choripan y lo persiste en el archivo.
     * @param consumidor nombre del consumidor
     * @param largo largo en cm
     * @param ancho ancho en cm
     * @param fecha fecha en formato YYYY-MM-DD
     * @return el objeto Choripan creado o null si se supero la capacidad
     */
    public Choripan agregarChoripan(String consumidor, double largo, double ancho, String fecha) {
        if (cantidadConsumos >= CAPACIDAD_MAXIMA) {
            System.out.println(">> Error: se alcanzo la capacidad maxima de registros (" + CAPACIDAD_MAXIMA + ").");
            System.out.println("   No es posible agregar mas consumos.");
            return null;
        }
        Choripan nuevo = new Choripan(consumidor, fecha, largo, ancho);
        consumos[cantidadConsumos] = nuevo;
        cantidadConsumos++;
        guardarConsumidos();
        return nuevo;
    }

    /**
     * Agrega un nuevo consumo de terremoto y lo persiste en el archivo.
     * @param consumidor nombre del consumidor
     * @param litros litros de terremoto
     * @param fecha fecha en formato YYYY-MM-DD
     * @return el objeto Terremoto creado o null si se supero la capacidad
     */
    public Terremoto agregarTerremoto(String consumidor, double litros, String fecha) {
        if (cantidadConsumos >= CAPACIDAD_MAXIMA) {
            System.out.println(">> Error: se alcanzo la capacidad maxima de registros (" + CAPACIDAD_MAXIMA + ").");
            System.out.println("   No es posible agregar mas consumos.");
            return null;
        }
        Terremoto nuevo = new Terremoto(consumidor, fecha, litros);
        consumos[cantidadConsumos] = nuevo;
        cantidadConsumos++;
        guardarConsumidos();
        return nuevo;
    }

    /**
     * Muestra la lista de choripanes para edicion o eliminacion.
     */
    public void mostrarChoripanesParaEdicion() {
        System.out.printf("  %-4s%-14s%-8s%-8s%-14s%n", "#", "Consumidor", "Largo", "Ancho", "Fecha");
        int cuenta = 0;
        for (int i = 0; i < cantidadConsumos; i++) {
            if (consumos[i] instanceof Choripan) {
                cuenta++;
                Choripan ch = (Choripan) consumos[i];
                String largoStr = ((long) ch.getLargo() == ch.getLargo()) ? ((long) ch.getLargo() + " cm") : (ch.getLargo() + " cm");
                String anchoStr = ((long) ch.getAncho() == ch.getAncho()) ? ((long) ch.getAncho() + " cm") : (ch.getAncho() + " cm");
                System.out.printf("  %-4d%-14s%-8s%-8s%-14s%n",
                        cuenta,
                        ch.getConsumidor(),
                        largoStr,
                        anchoStr,
                        ch.getFecha());
            }
        }
    }

    /**
     * Muestra la lista de terremotos para edicion o eliminacion.
     */
    public void mostrarTerremotosParaEdicion() {
        System.out.printf("  %-4s%-14s%-9s%-14s%n", "#", "Consumidor", "Litros", "Fecha");
        int cuenta = 0;
        for (int i = 0; i < cantidadConsumos; i++) {
            if (consumos[i] instanceof Terremoto) {
                cuenta++;
                Terremoto t = (Terremoto) consumos[i];
                System.out.printf(java.util.Locale.US, "  %-4d%-14s%5.2f L   %-14s%n",
                        cuenta,
                        t.getConsumidor(),
                        t.getLitros(),
                        t.getFecha());
            }
        }
    }

    /**
     * Obtiene el Choripan en la posicion 1-indexada dentro de los choripanes.
     * @param indice 1-indexado
     * @return Choripan encontrado o null
     */
    public Choripan obtenerChoripan(int indice) {
        int cuenta = 0;
        for (int i = 0; i < cantidadConsumos; i++) {
            if (consumos[i] instanceof Choripan) {
                cuenta++;
                if (cuenta == indice) {
                    return (Choripan) consumos[i];
                }
            }
        }
        return null;
    }

    /**
     * Obtiene el Terremoto en la posicion 1-indexada dentro de los terremotos.
     * @param indice 1-indexado
     * @return Terremoto encontrado o null
     */
    public Terremoto obtenerTerremoto(int indice) {
        int cuenta = 0;
        for (int i = 0; i < cantidadConsumos; i++) {
            if (consumos[i] instanceof Terremoto) {
                cuenta++;
                if (cuenta == indice) {
                    return (Terremoto) consumos[i];
                }
            }
        }
        return null;
    }

    /**
     * Elimina un consumo del arreglo estatico y persiste los cambios.
     * Desplaza los elementos para evitar huecos en el vector.
     * @param consumo consumo a eliminar
     * @return true si se elimino con exito
     */
    public boolean eliminarConsumo(Consumo consumo) {
        int posicion = -1;
        for (int i = 0; i < cantidadConsumos; i++) {
            if (consumos[i] == consumo) {
                posicion = i;
                break;
            }
        }
        if (posicion == -1) {
            return false;
        }

        for (int i = posicion; i < cantidadConsumos - 1; i++) {
            consumos[i] = consumos[i + 1];
        }
        consumos[cantidadConsumos - 1] = null;
        cantidadConsumos--;

        guardarConsumidos();
        return true;
    }

    /**
     * Retorna un arreglo con los nombres de todos los usuarios registrados (participantes y consumidores).
     * @return arreglo de nombres unicos
     */
    public String[] obtenerUsuariosUnicos() {
        String[] usuarios = new String[CAPACIDAD_MAXIMA];
        int cuenta = 0;

        // Primero agregar participantes
        for (int i = 0; i < cantidadParticipantes; i++) {
            String nombre = participantes[i].getName();
            boolean existe = false;
            for (int j = 0; j < cuenta; j++) {
                if (usuarios[j].equalsIgnoreCase(nombre)) {
                    existe = true;
                    break;
                }
            }
            if (!existe && cuenta < CAPACIDAD_MAXIMA) {
                usuarios[cuenta] = nombre;
                cuenta++;
            }
        }

        // Agregar consumidores que quiza no estaban en participantes
        for (int i = 0; i < cantidadConsumos; i++) {
            String nombre = consumos[i].getConsumidor();
            boolean existe = false;
            for (int j = 0; j < cuenta; j++) {
                if (usuarios[j].equalsIgnoreCase(nombre)) {
                    existe = true;
                    break;
                }
            }
            if (!existe && cuenta < CAPACIDAD_MAXIMA) {
                usuarios[cuenta] = nombre;
                cuenta++;
            }
        }

        String[] resultado = new String[cuenta];
        for (int i = 0; i < cuenta; i++) {
            resultado[i] = usuarios[i];
        }
        return resultado;
    }

    /**
     * Muestra el total de choripanes consumidos.
     */
    public void mostrarTotalChoripanes() {
        System.out.println(">> Total de choripanes comidos: " + contarChoripanes());
    }

    /**
     * Muestra el total de terremotos tomados y la suma de litros.
     */
    public void mostrarTotalTerremotos() {
        int cuenta = 0;
        double litros = 0.0;
        for (int i = 0; i < cantidadConsumos; i++) {
            if (consumos[i] instanceof Terremoto) {
                cuenta++;
                litros += ((Terremoto) consumos[i]).getLitros();
            }
        }
        System.out.printf(java.util.Locale.US, ">> Total de terremotos tomados: %d  (%.2f litros en total)%n", cuenta, litros);
    }

    /**
     * Muestra la tabla de choripanes consumidos por cada usuario.
     */
    public void mostrarChoripanesPorUsuario() {
        String[] usuarios = obtenerUsuariosUnicos();
        System.out.println("======= CHORIPANES POR USUARIO =============");
        System.out.printf("  %-14s%8s%n", "Usuario", "Cantidad");
        for (int i = 0; i < usuarios.length; i++) {
            String u = usuarios[i];
            int cantidad = 0;
            for (int j = 0; j < cantidadConsumos; j++) {
                if (consumos[j] instanceof Choripan && consumos[j].getConsumidor().equalsIgnoreCase(u)) {
                    cantidad++;
                }
            }
            System.out.printf("  %-14s%8d%n", u, cantidad);
        }
        System.out.println("============================================");
    }

    /**
     * Muestra la tabla de terremotos consumidos y litros por cada usuario.
     */
    public void mostrarTerremotosPorUsuario() {
        String[] usuarios = obtenerUsuariosUnicos();
        System.out.println("======= TERREMOTOS POR USUARIO =============");
        System.out.printf("  %-14s%8s%10s%n", "Usuario", "Cantidad", "Litros");
        for (int i = 0; i < usuarios.length; i++) {
            String u = usuarios[i];
            int cantidad = 0;
            double litros = 0.0;
            for (int j = 0; j < cantidadConsumos; j++) {
                if (consumos[j] instanceof Terremoto && consumos[j].getConsumidor().equalsIgnoreCase(u)) {
                    cantidad++;
                    litros += ((Terremoto) consumos[j]).getLitros();
                }
            }
            System.out.printf(java.util.Locale.US, "  %-14s%8d%8.2f L%n", u, cantidad, litros);
        }
        System.out.println("============================================");
    }

    /**
     * Muestra la tabla de KCal consumidas por cada usuario y el total de la fonda.
     */
    public void mostrarKcalPorUsuario() {
        String[] usuarios = obtenerUsuariosUnicos();
        System.out.println("============ KCAL POR USUARIO ==============");
        System.out.printf("  %-14s%10s%13s%9s%n", "Usuario", "Choripan", "Terremoto", "Total");
        double totalFonda = 0.0;
        for (int i = 0; i < usuarios.length; i++) {
            String u = usuarios[i];
            double kcalChori = 0.0;
            double kcalTerre = 0.0;
            for (int j = 0; j < cantidadConsumos; j++) {
                if (consumos[j].getConsumidor().equalsIgnoreCase(u)) {
                    if (consumos[j] instanceof Choripan) {
                        kcalChori += consumos[j].calcularKcal();
                    } else if (consumos[j] instanceof Terremoto) {
                        kcalTerre += consumos[j].calcularKcal();
                    }
                }
            }
            double totalUsuario = kcalChori + kcalTerre;
            totalFonda += totalUsuario;
            System.out.printf(java.util.Locale.US, "  %-14s%10.2f%13.2f%9.2f%n", u, kcalChori, kcalTerre, totalUsuario);
        }
        System.out.println("--------------------------------------------");
        System.out.printf(java.util.Locale.US, "  Consumo total de la fonda: %.2f kcal%n", totalFonda);
        System.out.println("============================================");
    }

    /**
     * Muestra los dias ebrios por usuario (2 o mas litros en una misma fecha).
     */
    public void mostrarDiasEbrioPorUsuario() {
        String[] usuarios = obtenerUsuariosUnicos();
        System.out.println("========= DIAS EBRIO POR USUARIO ===========");
        System.out.println("  (se cuenta un dia ebrio con 2 L o mas en la misma fecha)\n");
        System.out.printf("  %-14s%-12s %s%n", "Usuario", "Dias ebrio", "Detalle");
        for (int i = 0; i < usuarios.length; i++) {
            String u = usuarios[i];
            // Agrupar litros por fecha para este usuario
            String[] fechas = new String[CAPACIDAD_MAXIMA];
            double[] litrosFecha = new double[CAPACIDAD_MAXIMA];
            int cantidadFechas = 0;

            for (int j = 0; j < cantidadConsumos; j++) {
                if (consumos[j] instanceof Terremoto && consumos[j].getConsumidor().equalsIgnoreCase(u)) {
                    Terremoto t = (Terremoto) consumos[j];
                    String f = t.getFecha();
                    int indiceFecha = -1;
                    for (int k = 0; k < cantidadFechas; k++) {
                        if (fechas[k].equals(f)) {
                            indiceFecha = k;
                            break;
                        }
                    }
                    if (indiceFecha != -1) {
                        litrosFecha[indiceFecha] += t.getLitros();
                    } else if (cantidadFechas < CAPACIDAD_MAXIMA) {
                        fechas[cantidadFechas] = f;
                        litrosFecha[cantidadFechas] = t.getLitros();
                        cantidadFechas++;
                    }
                }
            }

            int diasEbrio = 0;
            String detalle = "";
            for (int k = 0; k < cantidadFechas; k++) {
                if (litrosFecha[k] >= 2.0) {
                    diasEbrio++;
                    if (!detalle.isEmpty()) {
                        detalle += ", ";
                    }
                    detalle += fechas[k] + " (" + String.format(java.util.Locale.US, "%.2f L", litrosFecha[k]) + ")";
                }
            }

            if (diasEbrio == 0) {
                detalle = "-";
            }

            System.out.printf("  %-14s%10d   %s%n", u, diasEbrio, detalle);
        }
        System.out.println("============================================");
    }
}
