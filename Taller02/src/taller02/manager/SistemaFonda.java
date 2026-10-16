package taller02.manager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

import taller02.domain.Choripan;
import taller02.domain.Consumo;
import taller02.domain.Participante;
import taller02.domain.Terremoto;

public class SistemaFonda {

    private Participante[] participantes = new Participante[200];
    private int cantidadParticipantes = 0;

    private Consumo[] consumos = new Consumo[200];
    private int cantidadConsumos = 0;

    private String rutaParticipantes = "txts/Participantes.txt";
    private String rutaConsumido = "txts/Consumido.txt";

    public SistemaFonda() {
        if (!new File(rutaParticipantes).exists() && new File("Participantes.txt").exists()) {
            rutaParticipantes = "Participantes.txt";
        }
        if (!new File(rutaConsumido).exists() && new File("Consumido.txt").exists()) {
            rutaConsumido = "Consumido.txt";
        }
    }

    public void cargarParticipantes() {
        File archivo = new File(rutaParticipantes);
        if (!archivo.exists()) {
            System.out.println(">> ADVERTENCIA: no se encontro el archivo \"" + rutaParticipantes + "\".");
            return;
        }

        try (Scanner sc = new Scanner(archivo)) {
            while (sc.hasNextLine()) {
                String linea = sc.nextLine().trim();
                if (!linea.isEmpty() && cantidadParticipantes < 200) {
                    participantes[cantidadParticipantes++] = new Participante(linea);
                }
            }
        } catch (IOException e) {
            System.out.println(">> Error al leer participantes: " + e.getMessage());
        }
    }

    public void cargarConsumidos() {
        File archivo = new File(rutaConsumido);
        if (!archivo.exists()) {
            System.out.println(">> ADVERTENCIA: no se encontro el archivo \"Consumido.txt\".");
            System.out.println("   Se iniciara el programa sin registros previos.");
            return;
        }

        try (Scanner sc = new Scanner(archivo)) {
            while (sc.hasNextLine()) {
                String linea = sc.nextLine().trim();
                if (linea.isEmpty() || cantidadConsumos >= 200) {
                    continue;
                }

                String[] partes = linea.split(";");
                if (partes.length < 4) {
                    continue;
                }

                try {
                    String tipo = partes[0].trim();
                    String consumidor = partes[1].trim();

                    if (tipo.equalsIgnoreCase("Choripan") && partes.length >= 5) {
                        double largo = Double.parseDouble(partes[2].trim());
                        double ancho = Double.parseDouble(partes[3].trim());
                        String fecha = partes[4].trim();
                        consumos[cantidadConsumos++] = new Choripan(consumidor, fecha, largo, ancho);
                    } else if (tipo.equalsIgnoreCase("Terremoto")) {
                        double litros = Double.parseDouble(partes[2].trim());
                        String fecha = partes[3].trim();
                        consumos[cantidadConsumos++] = new Terremoto(consumidor, fecha, litros);
                    }
                } catch (NumberFormatException e) {
                }
            }
        } catch (IOException e) {
            System.out.println(">> Error al leer consumidos: " + e.getMessage());
        }
    }

    public void guardarConsumidos() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaConsumido))) {
            for (int i = 0; i < cantidadConsumos; i++) {
                bw.write(consumos[i].aFormatoArchivo());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println(">> Error al guardar archivo: " + e.getMessage());
        }
    }

    public Choripan agregarChoripan(String consumidor, double largo, double ancho, String fecha) {
        if (cantidadConsumos >= 200) {
            System.out.println(">> Error: se alcanzo la capacidad maxima de registros (200).");
            System.out.println("   No es posible agregar mas consumos.");
            return null;
        }
        Choripan nuevo = new Choripan(consumidor, fecha, largo, ancho);
        consumos[cantidadConsumos++] = nuevo;
        guardarConsumidos();
        return nuevo;
    }

    public Terremoto agregarTerremoto(String consumidor, double litros, String fecha) {
        if (cantidadConsumos >= 200) {
            System.out.println(">> Error: se alcanzo la capacidad maxima de registros (200).");
            System.out.println("   No es posible agregar mas consumos.");
            return null;
        }
        Terremoto nuevo = new Terremoto(consumidor, fecha, litros);
        consumos[cantidadConsumos++] = nuevo;
        guardarConsumidos();
        return nuevo;
    }

    public void eliminarConsumo(Consumo c) {
        int pos = -1;
        for (int i = 0; i < cantidadConsumos; i++) {
            if (consumos[i] == c) {
                pos = i;
                break;
            }
        }
        if (pos != -1) {
            for (int i = pos; i < cantidadConsumos - 1; i++) {
                consumos[i] = consumos[i + 1];
            }
            consumos[--cantidadConsumos] = null;
            guardarConsumidos();
        }
    }

    public int contarChoripanes() {
        int count = 0;
        for (int i = 0; i < cantidadConsumos; i++) {
            if (consumos[i] instanceof Choripan) {
                count++;
            }
        }
        return count;
    }

    public int contarTerremotos() {
        int count = 0;
        for (int i = 0; i < cantidadConsumos; i++) {
            if (consumos[i] instanceof Terremoto) {
                count++;
            }
        }
        return count;
    }

    public Choripan obtenerChoripan(int indice) {
        int count = 0;
        for (int i = 0; i < cantidadConsumos; i++) {
            if (consumos[i] instanceof Choripan) {
                count++;
                if (count == indice) {
                    return (Choripan) consumos[i];
                }
            }
        }
        return null;
    }

    public Terremoto obtenerTerremoto(int indice) {
        int count = 0;
        for (int i = 0; i < cantidadConsumos; i++) {
            if (consumos[i] instanceof Terremoto) {
                count++;
                if (count == indice) {
                    return (Terremoto) consumos[i];
                }
            }
        }
        return null;
    }

    public void mostrarChoripanes() {
        System.out.println("=============== CHORIPANES =================");
        System.out.printf("  %-4s%-14s%-8s%-8s%-14s%-8s%n", "#", "Consumidor", "Largo", "Ancho", "Fecha", "KCal");
        int n = 0;
        for (int i = 0; i < cantidadConsumos; i++) {
            if (consumos[i] instanceof Choripan) {
                n++;
                Choripan ch = (Choripan) consumos[i];
                String largo = (ch.getLargo() == (int) ch.getLargo()) ? ((int) ch.getLargo() + " cm") : (ch.getLargo() + " cm");
                String ancho = (ch.getAncho() == (int) ch.getAncho()) ? ((int) ch.getAncho() + " cm") : (ch.getAncho() + " cm");
                System.out.printf(Locale.US, "  %-4d%-14s%-8s%-8s%-14s%8.2f%n",
                        n, ch.getConsumidor(), largo, ancho, ch.getFecha(), ch.calcularKcal());
            }
        }
        System.out.println("--------------------------------------------------");
        System.out.println("  Total: " + n + " choripanes registrados");
        System.out.println("==================================================");
    }

    public void mostrarTerremotos() {
        System.out.println("=============== TERREMOTOS =================");
        System.out.printf("  %-4s%-14s%-9s%-14s%-8s%n", "#", "Consumidor", "Litros", "Fecha", "KCal");
        int n = 0;
        double totalLitros = 0.0;
        for (int i = 0; i < cantidadConsumos; i++) {
            if (consumos[i] instanceof Terremoto) {
                n++;
                Terremoto t = (Terremoto) consumos[i];
                totalLitros += t.getLitros();
                System.out.printf(Locale.US, "  %-4d%-14s%5.2f L   %-14s%8.2f%n",
                        n, t.getConsumidor(), t.getLitros(), t.getFecha(), t.calcularKcal());
            }
        }
        System.out.println("--------------------------------------------------");
        System.out.printf(Locale.US, "  Total: %d terremotos registrados (%.2f L)%n", n, totalLitros);
        System.out.println("==================================================");
    }

    public void mostrarChoripanesParaEdicion() {
        System.out.printf("  %-4s%-14s%-8s%-8s%-14s%n", "#", "Consumidor", "Largo", "Ancho", "Fecha");
        int n = 0;
        for (int i = 0; i < cantidadConsumos; i++) {
            if (consumos[i] instanceof Choripan) {
                n++;
                Choripan ch = (Choripan) consumos[i];
                String largo = (ch.getLargo() == (int) ch.getLargo()) ? ((int) ch.getLargo() + " cm") : (ch.getLargo() + " cm");
                String ancho = (ch.getAncho() == (int) ch.getAncho()) ? ((int) ch.getAncho() + " cm") : (ch.getAncho() + " cm");
                System.out.printf("  %-4d%-14s%-8s%-8s%-14s%n", n, ch.getConsumidor(), largo, ancho, ch.getFecha());
            }
        }
    }

    public void mostrarTerremotosParaEdicion() {
        System.out.printf("  %-4s%-14s%-9s%-14s%n", "#", "Consumidor", "Litros", "Fecha");
        int n = 0;
        for (int i = 0; i < cantidadConsumos; i++) {
            if (consumos[i] instanceof Terremoto) {
                n++;
                Terremoto t = (Terremoto) consumos[i];
                System.out.printf(Locale.US, "  %-4d%-14s%5.2f L   %-14s%n", n, t.getConsumidor(), t.getLitros(), t.getFecha());
            }
        }
    }

    public String[] obtenerUsuariosUnicos() {
        String[] temp = new String[200];
        int total = 0;

        for (int i = 0; i < cantidadParticipantes; i++) {
            temp[total++] = participantes[i].getNombre();
        }

        for (int i = 0; i < cantidadConsumos; i++) {
            String nombre = consumos[i].getConsumidor();
            boolean existe = false;
            for (int j = 0; j < total; j++) {
                if (temp[j].equalsIgnoreCase(nombre)) {
                    existe = true;
                    break;
                }
            }
            if (!existe) {
                temp[total++] = nombre;
            }
        }

        String[] resultado = new String[total];
        for (int i = 0; i < total; i++) {
            resultado[i] = temp[i];
        }
        return resultado;
    }

    public void mostrarTotalChoripanes() {
        System.out.println(">> Total de choripanes comidos: " + contarChoripanes());
    }

    public void mostrarTotalTerremotos() {
        double litros = 0.0;
        for (int i = 0; i < cantidadConsumos; i++) {
            if (consumos[i] instanceof Terremoto) {
                litros += ((Terremoto) consumos[i]).getLitros();
            }
        }
        System.out.printf(Locale.US, ">> Total de terremotos tomados: %d  (%.2f litros en total)%n", contarTerremotos(), litros);
    }

    public void mostrarChoripanesPorUsuario() {
        String[] usuarios = obtenerUsuariosUnicos();
        System.out.println("======= CHORIPANES POR USUARIO =============");
        System.out.printf("  %-14s%8s%n", "Usuario", "Cantidad");
        for (int i = 0; i < usuarios.length; i++) {
            int count = 0;
            for (int j = 0; j < cantidadConsumos; j++) {
                if (consumos[j] instanceof Choripan && consumos[j].getConsumidor().equalsIgnoreCase(usuarios[i])) {
                    count++;
                }
            }
            System.out.printf("  %-14s%8d%n", usuarios[i], count);
        }
        System.out.println("============================================");
    }

    public void mostrarTerremotosPorUsuario() {
        String[] usuarios = obtenerUsuariosUnicos();
        System.out.println("======= TERREMOTOS POR USUARIO =============");
        System.out.printf("  %-14s%8s%10s%n", "Usuario", "Cantidad", "Litros");
        for (int i = 0; i < usuarios.length; i++) {
            int count = 0;
            double litros = 0.0;
            for (int j = 0; j < cantidadConsumos; j++) {
                if (consumos[j] instanceof Terremoto && consumos[j].getConsumidor().equalsIgnoreCase(usuarios[i])) {
                    count++;
                    litros += ((Terremoto) consumos[j]).getLitros();
                }
            }
            System.out.printf(Locale.US, "  %-14s%8d%8.2f L%n", usuarios[i], count, litros);
        }
        System.out.println("============================================");
    }

    public void mostrarKcalPorUsuario() {
        String[] usuarios = obtenerUsuariosUnicos();
        System.out.println("============ KCAL POR USUARIO ==============");
        System.out.printf("  %-14s%10s%13s%9s%n", "Usuario", "Choripan", "Terremoto", "Total");
        double totalFonda = 0.0;
        for (int i = 0; i < usuarios.length; i++) {
            double kcalChori = 0.0;
            double kcalTerre = 0.0;
            for (int j = 0; j < cantidadConsumos; j++) {
                if (consumos[j].getConsumidor().equalsIgnoreCase(usuarios[i])) {
                    if (consumos[j] instanceof Choripan) {
                        kcalChori += consumos[j].calcularKcal();
                    } else if (consumos[j] instanceof Terremoto) {
                        kcalTerre += consumos[j].calcularKcal();
                    }
                }
            }
            double total = kcalChori + kcalTerre;
            totalFonda += total;
            System.out.printf(Locale.US, "  %-14s%10.2f%13.2f%9.2f%n", usuarios[i], kcalChori, kcalTerre, total);
        }
        System.out.println("--------------------------------------------");
        System.out.printf(Locale.US, "  Consumo total de la fonda: %.2f kcal%n", totalFonda);
        System.out.println("============================================");
    }

    public void mostrarDiasEbrioPorUsuario() {
        String[] usuarios = obtenerUsuariosUnicos();
        System.out.println("========= DIAS EBRIO POR USUARIO ===========");
        System.out.println("  (se cuenta un dia ebrio con 2 L o mas en la misma fecha)\n");
        System.out.printf("  %-14s%-12s %s%n", "Usuario", "Dias ebrio", "Detalle");

        for (int i = 0; i < usuarios.length; i++) {
            String[] fechas = new String[200];
            double[] litros = new double[200];
            int cantFechas = 0;

            for (int j = 0; j < cantidadConsumos; j++) {
                if (consumos[j] instanceof Terremoto && consumos[j].getConsumidor().equalsIgnoreCase(usuarios[i])) {
                    Terremoto t = (Terremoto) consumos[j];
                    int idx = -1;
                    for (int k = 0; k < cantFechas; k++) {
                        if (fechas[k].equals(t.getFecha())) {
                            idx = k;
                            break;
                        }
                    }
                    if (idx != -1) {
                        litros[idx] += t.getLitros();
                    } else {
                        fechas[cantFechas] = t.getFecha();
                        litros[cantFechas] = t.getLitros();
                        cantFechas++;
                    }
                }
            }

            int diasEbrio = 0;
            String detalle = "";
            for (int k = 0; k < cantFechas; k++) {
                if (litros[k] >= 2.0) {
                    diasEbrio++;
                    if (!detalle.isEmpty()) {
                        detalle += ", ";
                    }
                    detalle += fechas[k] + " (" + String.format(Locale.US, "%.2f L", litros[k]) + ")";
                }
            }
            if (diasEbrio == 0) {
                detalle = "-";
            }
            System.out.printf("  %-14s%10d   %s%n", usuarios[i], diasEbrio, detalle);
        }
        System.out.println("============================================");
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
}
