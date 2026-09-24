/*
 * Matias Blanc - 22330528-8 - ICCI
 */

package taller1;

import java.util.Scanner;
import java.io.File;
import java.io.IOException;

public class Main {

    static final int MAX = 100;

    // Alumnos
    static String[] nombresAlumnos = new String[MAX];
    static String[] apellidosAlumnos = new String[MAX];
    static String[] rutsAlumnos = new String[MAX];
    static String[] paralelosAlumnos = new String[MAX];
    static int cantidadAlumnos = 0;

    // Solicitudes
    static String[] nombresSolicitudes = new String[MAX];
    static String[] apellidosSolicitudes = new String[MAX];
    static int cantidadSolicitudes = 0;

    // Miembros admitidos
    static String[] nombresMiembros = new String[MAX];
    static String[] apellidosMiembros = new String[MAX];
    static String[] rutsMiembros = new String[MAX];
    static String[] paralelosMiembros = new String[MAX];
    static int cantidadMiembros = 0;

    // Rechazados
    static String[] rechazados = new String[MAX];
    static int cantidadRechazados = 0;

    static boolean archivosCargados = false;
    static boolean solicitudesProcesadas = false;

    /**
     * Ejecuta el menu principal del programa.
     *
     * @param args argumentos de ejecucion; no se utilizan
     */
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        int opcion = 0;

        while (opcion != 7) {
            mostrarMenu();
            opcion = leerOpcion(teclado);

            switch (opcion) {
                case 1:
                    cargarArchivos();
                    break;
                case 2:
                    procesarSolicitudes();
                    break;
                case 3:
                    inscripcionManual(teclado);
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

        teclado.close();
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

    static int leerOpcion(Scanner teclado) {
        System.out.print("Ingrese opcion: ");

        if (!teclado.hasNextLine()) {
            return 7;
        }

        String entrada = teclado.nextLine().trim();

        try {
            return Integer.parseInt(entrada);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Lee Alumnos.txt y Solicitudes.txt y guarda sus datos en vectores paralelos.
     * Los errores de lectura se informan en pantalla sin detener el programa.
     */
    static void cargarArchivos() {
        cantidadAlumnos = 0;
        cantidadSolicitudes = 0;
        cantidadMiembros = 0;
        cantidadRechazados = 0;
        archivosCargados = false;
        solicitudesProcesadas = false;

        boolean alumnosLeidos = false;
        boolean solicitudesLeidas = false;

        File archivoAlumnos = new File("Alumnos.txt");

        try {
            Scanner lectorAlumnos = new Scanner(archivoAlumnos);

            while (lectorAlumnos.hasNextLine()) {
                if (cantidadAlumnos == MAX) {
                    System.out.println("No se pueden cargar mas de " + MAX + " alumnos.");
                    break;
                }

                String linea = lectorAlumnos.nextLine();
                String[] datos = linea.split(";", -1);

                if (datos.length == 4) {
                    String nombre = datos[0].trim();
                    String apellido = datos[1].trim();
                    String rut = datos[2].trim();
                    String paralelo = datos[3].trim().toUpperCase();

                    boolean camposCompletos = !nombre.isEmpty() && !apellido.isEmpty()
                            && !rut.isEmpty() && !paralelo.isEmpty();
                    boolean rutRepetido = buscarAlumnoPorRut(rut) != -1;

                    if (camposCompletos && esParaleloValido(paralelo) && !rutRepetido) {
                        nombresAlumnos[cantidadAlumnos] = nombre;
                        apellidosAlumnos[cantidadAlumnos] = apellido;
                        rutsAlumnos[cantidadAlumnos] = rut;
                        paralelosAlumnos[cantidadAlumnos] = paralelo;
                        cantidadAlumnos++;
                    } else {
                        System.out.println("Se omitio una linea invalida de Alumnos.txt: " + linea);
                    }
                } else {
                    System.out.println("Se omitio una linea invalida de Alumnos.txt: " + linea);
                }
            }

            lectorAlumnos.close();
            alumnosLeidos = true;
        } catch (IOException e) {
            System.out.println("No se pudo leer Alumnos.txt.");
        }

        File archivoSolicitudes = new File("Solicitudes.txt");

        try {
            Scanner lectorSolicitudes = new Scanner(archivoSolicitudes);

            while (lectorSolicitudes.hasNextLine()) {
                if (cantidadSolicitudes == MAX) {
                    System.out.println("No se pueden cargar mas de " + MAX + " solicitudes.");
                    break;
                }

                String linea = lectorSolicitudes.nextLine();
                String[] datos = linea.split("-", -1);

                if (datos.length == 2 && !datos[0].trim().isEmpty()
                        && !datos[1].trim().isEmpty()) {
                    nombresSolicitudes[cantidadSolicitudes] = datos[0].trim();
                    apellidosSolicitudes[cantidadSolicitudes] = datos[1].trim();
                    cantidadSolicitudes++;
                } else {
                    System.out.println("Se omitio una linea invalida de Solicitudes.txt: " + linea);
                }
            }

            lectorSolicitudes.close();
            solicitudesLeidas = true;
        } catch (IOException e) {
            System.out.println("No se pudo leer Solicitudes.txt.");
        }

        archivosCargados = alumnosLeidos && solicitudesLeidas;

        System.out.println("Alumnos cargados: " + cantidadAlumnos);
        System.out.println("Solicitudes cargadas: " + cantidadSolicitudes);
    }

    /**
     * Compara cada solicitud con la lista de alumnos y guarda a los miembros y rechazados.
     */
    static void procesarSolicitudes() {
        if (!archivosCargados) {
            System.out.println("Primero debe cargar los archivos.");
            return;
        }

        if (solicitudesProcesadas) {
            System.out.println("Las solicitudes ya fueron procesadas.");
            return;
        }

        int admitidos = 0;
        int rechazadosNuevos = 0;
        int duplicados = 0;

        System.out.println("Procesando solicitudes...");

        for (int i = 0; i < cantidadSolicitudes; i++) {
            int posicionAlumno = buscarAlumnoPorNombre(
                    nombresSolicitudes[i], apellidosSolicitudes[i]);

            if (posicionAlumno != -1) {
                int posicionMiembro = buscarMiembroPorRut(rutsAlumnos[posicionAlumno]);

                if (posicionMiembro == -1) {
                    if (agregarMiembro(posicionAlumno)) {
                        admitidos++;
                        System.out.println("[OK] " + nombresAlumnos[posicionAlumno] + " "
                                + apellidosAlumnos[posicionAlumno] + " -> admitido en "
                                + paralelosAlumnos[posicionAlumno]);
                    }
                } else {
                    duplicados++;
                    System.out.println("[DUPLICADA] " + nombresSolicitudes[i] + " "
                            + apellidosSolicitudes[i] + " -> ya pertenece al grupo.");
                }
            } else {
                String nombreCompleto = nombresSolicitudes[i] + " " + apellidosSolicitudes[i];

                if (agregarRechazado(nombreCompleto)) {
                    rechazadosNuevos++;
                    System.out.println("[RECHAZO] " + nombreCompleto
                            + " -> no pertenece a ningun paralelo.");
                }
            }
        }

        solicitudesProcesadas = true;
        System.out.println("Resumen: " + admitidos + " admitidos / "
                + rechazadosNuevos + " rechazados / " + duplicados + " duplicados.");
    }

    /**
     * Permite inscribir manualmente a una persona mediante su nombre o su RUT.
     *
     * @param teclado lector utilizado para recibir los datos ingresados
     */
    static void inscripcionManual(Scanner teclado) {
        if (!archivosCargados) {
            System.out.println("Primero debe cargar los archivos.");
            return;
        }

        System.out.println("""
                --- Inscripcion manual ---
                1) Por nombre completo
                2) Por RUT
                3) Volver
                """);

        int opcion = leerOpcion(teclado);

        if (opcion == 1) {
            System.out.print("Ingrese nombre: ");
            String nombre = leerTexto(teclado);
            System.out.print("Ingrese apellido: ");
            String apellido = leerTexto(teclado);

            if (nombre.isEmpty() || apellido.isEmpty()) {
                System.out.println("El nombre y el apellido no pueden estar vacios.");
                return;
            }

            int posicionAlumno = buscarAlumnoPorNombre(nombre, apellido);

            if (posicionAlumno != -1) {
                inscribirAlumno(posicionAlumno);
            } else {
                agregarRechazado(nombre + " " + apellido);
                System.out.println(nombre + " " + apellido
                        + " no pertenece a ningun paralelo del curso.");
            }
        } else if (opcion == 2) {
            System.out.print("Ingrese RUT: ");
            String rut = leerTexto(teclado);

            if (rut.isEmpty()) {
                System.out.println("El RUT no puede estar vacio.");
                return;
            }

            int posicionAlumno = buscarAlumnoPorRut(rut);

            if (posicionAlumno != -1) {
                inscribirAlumno(posicionAlumno);
            } else {
                agregarRechazado("Sin nombre registrado, RUT: " + rut);
                System.out.println("El RUT " + rut
                        + " no pertenece a ningun paralelo del curso.");
                System.out.println("Se registrara solamente el RUT en los rechazados.");
            }
        } else if (opcion != 3) {
            System.out.println("Opcion invalida.");
        }
    }

    static String leerTexto(Scanner teclado) {
        if (teclado.hasNextLine()) {
            return teclado.nextLine().trim();
        }
        return "";
    }

    static int buscarAlumnoPorNombre(String nombre, String apellido) {
        for (int i = 0; i < cantidadAlumnos; i++) {
            if (nombre.equalsIgnoreCase(nombresAlumnos[i])
                    && apellido.equalsIgnoreCase(apellidosAlumnos[i])) {
                return i;
            }
        }
        return -1;
    }

    static int buscarAlumnoPorRut(String rut) {
        for (int i = 0; i < cantidadAlumnos; i++) {
            if (rut.equalsIgnoreCase(rutsAlumnos[i])) {
                return i;
            }
        }
        return -1;
    }

    static int buscarMiembroPorRut(String rut) {
        for (int i = 0; i < cantidadMiembros; i++) {
            if (rut.equalsIgnoreCase(rutsMiembros[i])) {
                return i;
            }
        }
        return -1;
    }

    static boolean agregarMiembro(int posicionAlumno) {
        if (cantidadMiembros == MAX) {
            System.out.println("No hay espacio para agregar mas miembros.");
            return false;
        }

        nombresMiembros[cantidadMiembros] = nombresAlumnos[posicionAlumno];
        apellidosMiembros[cantidadMiembros] = apellidosAlumnos[posicionAlumno];
        rutsMiembros[cantidadMiembros] = rutsAlumnos[posicionAlumno];
        paralelosMiembros[cantidadMiembros] = paralelosAlumnos[posicionAlumno];
        cantidadMiembros++;
        return true;
    }

    static void inscribirAlumno(int posicionAlumno) {
        if (buscarMiembroPorRut(rutsAlumnos[posicionAlumno]) != -1) {
            System.out.println(nombresAlumnos[posicionAlumno] + " "
                    + apellidosAlumnos[posicionAlumno] + " ya pertenece al grupo.");
            return;
        }

        if (agregarMiembro(posicionAlumno)) {
            System.out.println(nombresAlumnos[posicionAlumno] + " "
                    + apellidosAlumnos[posicionAlumno] + " fue admitido en "
                    + paralelosAlumnos[posicionAlumno] + ".");
        }
    }

    static boolean agregarRechazado(String detalle) {
        if (cantidadRechazados == MAX) {
            System.out.println("No hay espacio para registrar mas rechazados.");
            return false;
        }

        rechazados[cantidadRechazados] = detalle;
        cantidadRechazados++;
        return true;
    }

    static boolean esParaleloValido(String paralelo) {
        return paralelo.equals("C1") || paralelo.equals("C2");
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
