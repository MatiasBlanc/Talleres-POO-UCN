/*
 * Matias Blanc - 22330528-8 - ICCI
 */

package taller1;

import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
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
                    administrarCurso(teclado);
                    break;
                case 5:
                    generarReportes(teclado);
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

    /**
     * Muestra el menu de administracion y permite modificar la lista del curso.
     *
     * @param teclado lector utilizado para recibir los datos ingresados
     */
    static void administrarCurso(Scanner teclado) {
        if (!archivosCargados) {
            System.out.println("Primero debe cargar los archivos.");
            return;
        }

        int opcion = 0;

        while (opcion != 4) {
            System.out.println("""
                    --- Administracion del curso ---
                    1) Cambiar paralelo de un alumno
                    2) Eliminar alumno del curso
                    3) Inscribir alumno nuevo
                    4) Volver
                    """);
            opcion = leerOpcion(teclado);

            switch (opcion) {
                case 1:
                    cambiarParalelo(teclado);
                    break;
                case 2:
                    eliminarAlumno(teclado);
                    break;
                case 3:
                    agregarAlumno(teclado);
                    break;
                case 4:
                    break;
                case 7:
                    // leerOpcion retorna 7 cuando se termina la entrada por consola.
                    return;
                default:
                    System.out.println("Opcion invalida.");
            }
        }
    }

    /**
     * Cambia el paralelo de un alumno identificado por su RUT.
     * Si ya pertenece al grupo, tambien actualiza su paralelo como miembro.
     *
     * @param teclado lector utilizado para recibir los datos ingresados
     */
    static void cambiarParalelo(Scanner teclado) {
        System.out.print("Ingrese RUT del alumno: ");
        String rut = leerTexto(teclado);

        if (rut.isEmpty()) {
            System.out.println("El RUT no puede estar vacio.");
            return;
        }

        int posicionAlumno = buscarAlumnoPorRut(rut);

        if (posicionAlumno == -1) {
            System.out.println("No existe un alumno con ese RUT.");
            return;
        }

        System.out.println("Alumno: " + nombresAlumnos[posicionAlumno] + " "
                + apellidosAlumnos[posicionAlumno] + " (actualmente en "
                + paralelosAlumnos[posicionAlumno] + ")");
        System.out.print("Nuevo paralelo (C1/C2): ");
        String nuevoParalelo = leerTexto(teclado).toUpperCase();

        if (!esParaleloValido(nuevoParalelo)) {
            System.out.println("El paralelo debe ser C1 o C2.");
            return;
        }

        if (nuevoParalelo.equals(paralelosAlumnos[posicionAlumno])) {
            System.out.println("El alumno ya pertenece a " + nuevoParalelo + ".");
            return;
        }

        String paraleloAnterior = paralelosAlumnos[posicionAlumno];
        paralelosAlumnos[posicionAlumno] = nuevoParalelo;

        int posicionMiembro = buscarMiembroPorRut(rut);
        if (posicionMiembro != -1) {
            paralelosMiembros[posicionMiembro] = nuevoParalelo;
        }

        if (guardarAlumnos()) {
            System.out.println("Paralelo actualizado y guardado en Alumnos.txt.");
        } else {
            paralelosAlumnos[posicionAlumno] = paraleloAnterior;
            if (posicionMiembro != -1) {
                paralelosMiembros[posicionMiembro] = paraleloAnterior;
            }
        }
    }

    /**
     * Elimina de la lista a un alumno identificado por su RUT.
     * Tambien lo elimina del grupo si habia sido admitido.
     *
     * @param teclado lector utilizado para recibir los datos ingresados
     */
    static void eliminarAlumno(Scanner teclado) {
        System.out.print("Ingrese RUT del alumno que desea eliminar: ");
        String rut = leerTexto(teclado);

        if (rut.isEmpty()) {
            System.out.println("El RUT no puede estar vacio.");
            return;
        }

        int posicionAlumno = buscarAlumnoPorRut(rut);

        if (posicionAlumno == -1) {
            System.out.println("No existe un alumno con ese RUT.");
            return;
        }

        String nombreCompleto = nombresAlumnos[posicionAlumno] + " "
                + apellidosAlumnos[posicionAlumno];

        for (int i = posicionAlumno; i < cantidadAlumnos - 1; i++) {
            nombresAlumnos[i] = nombresAlumnos[i + 1];
            apellidosAlumnos[i] = apellidosAlumnos[i + 1];
            rutsAlumnos[i] = rutsAlumnos[i + 1];
            paralelosAlumnos[i] = paralelosAlumnos[i + 1];
        }

        cantidadAlumnos--;
        nombresAlumnos[cantidadAlumnos] = null;
        apellidosAlumnos[cantidadAlumnos] = null;
        rutsAlumnos[cantidadAlumnos] = null;
        paralelosAlumnos[cantidadAlumnos] = null;

        int posicionMiembro = buscarMiembroPorRut(rut);
        if (posicionMiembro != -1) {
            eliminarMiembro(posicionMiembro);
        }

        if (guardarAlumnos()) {
            System.out.println(nombreCompleto + " fue eliminado del curso.");
        }
    }

    /**
     * Agrega un alumno nuevo a la lista, validando sus datos y su RUT.
     * El alumno se guarda en el archivo, pero no se agrega automaticamente al grupo.
     *
     * @param teclado lector utilizado para recibir los datos ingresados
     */
    static void agregarAlumno(Scanner teclado) {
        if (cantidadAlumnos == MAX) {
            System.out.println("No hay espacio para inscribir mas alumnos.");
            return;
        }

        System.out.print("Ingrese nombre: ");
        String nombre = leerTexto(teclado);
        System.out.print("Ingrese apellido: ");
        String apellido = leerTexto(teclado);
        System.out.print("Ingrese RUT: ");
        String rut = leerTexto(teclado);
        System.out.print("Ingrese paralelo (C1/C2): ");
        String paralelo = leerTexto(teclado).toUpperCase();

        if (nombre.isEmpty() || apellido.isEmpty() || rut.isEmpty() || paralelo.isEmpty()) {
            System.out.println("Ningun dato puede estar vacio.");
            return;
        }

        if (!esParaleloValido(paralelo)) {
            System.out.println("El paralelo debe ser C1 o C2.");
            return;
        }

        if (buscarAlumnoPorRut(rut) != -1) {
            System.out.println("Ya existe un alumno con ese RUT.");
            return;
        }

        nombresAlumnos[cantidadAlumnos] = nombre;
        apellidosAlumnos[cantidadAlumnos] = apellido;
        rutsAlumnos[cantidadAlumnos] = rut;
        paralelosAlumnos[cantidadAlumnos] = paralelo;
        cantidadAlumnos++;

        if (guardarAlumnos()) {
            System.out.println(nombre + " " + apellido + " fue inscrito en " + paralelo + ".");
        } else {
            cantidadAlumnos--;
            nombresAlumnos[cantidadAlumnos] = null;
            apellidosAlumnos[cantidadAlumnos] = null;
            rutsAlumnos[cantidadAlumnos] = null;
            paralelosAlumnos[cantidadAlumnos] = null;
        }
    }

    /**
     * Elimina un miembro y desplaza los elementos posteriores de sus vectores.
     *
     * @param posicion posicion del miembro que se eliminara
     */
    static void eliminarMiembro(int posicion) {
        for (int i = posicion; i < cantidadMiembros - 1; i++) {
            nombresMiembros[i] = nombresMiembros[i + 1];
            apellidosMiembros[i] = apellidosMiembros[i + 1];
            rutsMiembros[i] = rutsMiembros[i + 1];
            paralelosMiembros[i] = paralelosMiembros[i + 1];
        }

        cantidadMiembros--;
        nombresMiembros[cantidadMiembros] = null;
        apellidosMiembros[cantidadMiembros] = null;
        rutsMiembros[cantidadMiembros] = null;
        paralelosMiembros[cantidadMiembros] = null;
    }

    /**
     * Reescribe Alumnos.txt con los datos actuales de los vectores.
     *
     * @return true si el archivo se guardo correctamente; false si ocurrio un error
     */
    static boolean guardarAlumnos() {
        File archivoAlumnos = new File("Alumnos.txt");

        try {
            BufferedWriter escritor = new BufferedWriter(new FileWriter(archivoAlumnos));

            for (int i = 0; i < cantidadAlumnos; i++) {
                escritor.write(nombresAlumnos[i] + ";" + apellidosAlumnos[i] + ";"
                        + rutsAlumnos[i] + ";" + paralelosAlumnos[i]);
                escritor.newLine();
            }

            escritor.close();
            return true;
        } catch (IOException e) {
            System.out.println("No se pudieron guardar los cambios en Alumnos.txt.");
            return false;
        }
    }

    /**
     * Muestra el menu para generar reportes de miembros y de rechazos.
     * Cada reporte nuevo se guarda con su propia version.
     *
     * @param teclado lector utilizado para recibir la opcion del usuario
     */
    static void generarReportes(Scanner teclado) {
        if (!archivosCargados) {
            System.out.println("Primero debe cargar los archivos.");
            return;
        }

        int opcion = 0;

        while (opcion != 4) {
            System.out.println("""
                    --- Generacion de reportes ---
                    1) Reporte de miembros de C1
                    2) Reporte de miembros de C2
                    3) Reporte de solicitudes rechazadas
                    4) Volver
                    """);
            opcion = leerOpcion(teclado);

            switch (opcion) {
                case 1:
                    generarReporteParalelo("C1");
                    break;
                case 2:
                    generarReporteParalelo("C2");
                    break;
                case 3:
                    generarReporteRechazados();
                    break;
                case 4:
                    break;
                case 7:
                    // Permite terminar si ya no quedan datos en la entrada por consola.
                    return;
                default:
                    System.out.println("Opcion invalida.");
            }
        }
    }

    /**
     * Genera un archivo con los miembros actuales de un paralelo.
     *
     * @param paralelo paralelo que se incluira en el reporte, C1 o C2
     */
    static void generarReporteParalelo(String paralelo) {
        File carpetaReportes = prepararCarpetaReportes();

        if (carpetaReportes == null) {
            return;
        }

        String nombreBase = "Reporte" + paralelo;
        int version = buscarSiguienteVersion(carpetaReportes, nombreBase);
        File archivoReporte = new File(carpetaReportes,
                nombreBase + "-V" + version + ".txt");

        try {
            BufferedWriter escritor = new BufferedWriter(new FileWriter(archivoReporte));
            escritor.write("=== Miembros del grupo - Paralelo " + paralelo + " ===");
            escritor.newLine();

            for (int i = 0; i < cantidadMiembros; i++) {
                if (paralelo.equals(paralelosMiembros[i])) {
                    escritor.write(nombresMiembros[i] + " " + apellidosMiembros[i]
                            + " - " + rutsMiembros[i]);
                    escritor.newLine();
                }
            }

            escritor.close();
            System.out.println("Reporte creado: " + archivoReporte.getPath());
        } catch (IOException e) {
            System.out.println("No se pudo crear el reporte de " + paralelo + ".");
        }
    }

    /**
     * Genera un archivo con todas las solicitudes rechazadas registradas.
     */
    static void generarReporteRechazados() {
        File carpetaReportes = prepararCarpetaReportes();

        if (carpetaReportes == null) {
            return;
        }

        String nombreBase = "Rechazados";
        int version = buscarSiguienteVersion(carpetaReportes, nombreBase);
        File archivoReporte = new File(carpetaReportes,
                nombreBase + "-V" + version + ".txt");

        try {
            BufferedWriter escritor = new BufferedWriter(new FileWriter(archivoReporte));
            escritor.write("=== Solicitudes rechazadas ===");
            escritor.newLine();

            for (int i = 0; i < cantidadRechazados; i++) {
                if (rechazados[i].startsWith("Sin nombre registrado, RUT:")) {
                    escritor.write(rechazados[i]);
                } else {
                    escritor.write(rechazados[i]
                            + " - No pertenece a ningun paralelo del curso");
                }
                escritor.newLine();
            }

            escritor.close();
            System.out.println("Reporte creado: " + archivoReporte.getPath());
        } catch (IOException e) {
            System.out.println("No se pudo crear el reporte de rechazados.");
        }
    }

    /**
     * Crea la carpeta Reportes cuando todavia no existe.
     *
     * @return la carpeta de reportes o null si no pudo prepararse
     */
    static File prepararCarpetaReportes() {
        File carpetaReportes = new File("Reportes");

        if (carpetaReportes.exists()) {
            if (carpetaReportes.isDirectory()) {
                return carpetaReportes;
            }

            System.out.println("No se pueden generar reportes porque Reportes no es una carpeta.");
            return null;
        }

        if (carpetaReportes.mkdir()) {
            return carpetaReportes;
        }

        System.out.println("No se pudo crear la carpeta Reportes.");
        return null;
    }

    /**
     * Busca el primer numero de version que aun no ha sido utilizado.
     *
     * @param carpetaReportes carpeta en la que se guardan los reportes
     * @param nombreBase inicio del nombre del archivo, sin version ni extension
     * @return siguiente numero de version disponible
     */
    static int buscarSiguienteVersion(File carpetaReportes, String nombreBase) {
        int version = 1;
        File archivo = new File(carpetaReportes,
                nombreBase + "-V" + version + ".txt");

        while (archivo.exists()) {
            version++;
            archivo = new File(carpetaReportes,
                    nombreBase + "-V" + version + ".txt");
        }

        return version;
    }

    static void mostrarEstadisticas() {
        System.out.println("Analisis estadistico...");
    }
}
