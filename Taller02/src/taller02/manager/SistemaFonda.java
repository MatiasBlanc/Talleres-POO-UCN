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
}
