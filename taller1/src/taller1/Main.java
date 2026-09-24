/*
 * Matias Blanc - 22330528-8 - ICCI
 */

package taller1;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;

public class Main {

    static final int MAX = 100;

    // ----- ALUMNOS -----
    static String[] nombresAlumnos = new String[MAX];
    static String[] apellidosAlumnos = new String[MAX];
    static String[] rutsAlumnos = new String[MAX];
    static String[] paralelosAlumnos = new String[MAX];

    static int cantidadAlumnos = 0;

    // ----- SOLICITUDES -----
    static String[] nombresSolicitudes = new String[MAX];
    static String[] apellidosSolicitudes = new String[MAX];

    static int cantidadSolicitudes = 0;

    public static void main(String[] args) {
    	Scanner teclado = new Scanner(System.in);
    	int opcion = 0;
    	
    	while (opcion != 7) {
    		mostrarMenu();
            opcion = teclado.nextInt();
            
            switch (opcion) {

            case 1:
                cargarArchivos();
                break;

            case 2:
                procesarSolicitudes();
                break;

            case 3:
                inscripcionManual();
                break;

            case 4:
                administrarCurso();
                break;

            case 5:
                generarReportes();
                break;

            case 6:
                mostrarEstadisticas();
                break;

            case 7:
                System.out.println("Saliendo del programa...");
                break;

            default:
                System.out.println("Opcion invalida.");
        }

    	}
    }
    
    static void mostrarMenu() {
        System.out.println("""
                ===== Sistema de Control del Grupo POO =====
                1) Cargar archivos (Alumnos y Solicitudes)
                2) Procesar solicitudes (Filtrado automatico)
                3) Inscripcion manual al grupo
                4) Administracion del curso
                5) Generar reportes
                6) Analisis estadistico
                7) Salir
                """);
    }
    
    static void cargarArchivos() {

        cantidadAlumnos = 0;
        cantidadSolicitudes = 0;

        // ---------- CARGAR ALUMNOS ----------
        File archivoAlumnos = new File("Alumnos.txt");

        try {
            Scanner lectorAlumnos = new Scanner(archivoAlumnos);

            while (lectorAlumnos.hasNextLine() && cantidadAlumnos < MAX) {

                String linea = lectorAlumnos.nextLine();
                String[] datos = linea.split(";");

                if (datos.length == 4) {
                    nombresAlumnos[cantidadAlumnos] = datos[0];
                    apellidosAlumnos[cantidadAlumnos] = datos[1];
                    rutsAlumnos[cantidadAlumnos] = datos[2];
                    paralelosAlumnos[cantidadAlumnos] = datos[3];

                    cantidadAlumnos++;
                }
            }

            lectorAlumnos.close();

        } catch (IOException e) {
            System.out.println("No se pudo leer Alumnos.txt");
        }


        // ---------- CARGAR SOLICITUDES ----------
        File archivoSolicitudes = new File("Solicitudes.txt");

        try {
            Scanner lectorSolicitudes = new Scanner(archivoSolicitudes);

            while (lectorSolicitudes.hasNextLine() && cantidadSolicitudes < MAX) {

                String linea = lectorSolicitudes.nextLine();
                String[] datos = linea.split("-");

                if (datos.length == 2) {
                    nombresSolicitudes[cantidadSolicitudes] = datos[0];
                    apellidosSolicitudes[cantidadSolicitudes] = datos[1];

                    cantidadSolicitudes++;
                }
            }

            lectorSolicitudes.close();

        } catch (IOException e) {
            System.out.println("No se pudo leer Solicitudes.txt");
        }


        System.out.println("Alumnos cargados: " + cantidadAlumnos);
        System.out.println("Solicitudes cargadas: " + cantidadSolicitudes);
    }

    static void procesarSolicitudes() {
        System.out.println("Procesando solicitudes...");
    }

    static void inscripcionManual() {
        System.out.println("Inscripcion manual...");
    }

    static void administrarCurso() {
        System.out.println("Administracion del curso...");
    }

    static void generarReportes() {
        System.out.println("Generando reportes...");
    }

    static void mostrarEstadisticas() {
        System.out.println("Analisis estadistico...");
    }
}