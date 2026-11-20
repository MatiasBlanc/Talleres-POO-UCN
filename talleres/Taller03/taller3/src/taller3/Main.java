// Matias Blanc - 22.330.528-8 - ICCI

package taller3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import taller3.application.ResultadoJugada;
import taller3.application.ResultadoSesion;
import taller3.application.Sistema;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            ejecutar(scanner);
        } catch (Exception e) {
            System.out.println("[!] Se produjo un error inesperado.");
        } finally {
            scanner.close();
        }
    }

    private static void ejecutar(Scanner scanner) {
        Sistema sistema = new Sistema();
        File archivoMonstruos = resolverMonstruos();
        File archivoRegistros = resolverRegistros(archivoMonstruos);

        System.out.println("Cargando archivos...");
        if (!cargarMonstruos(archivoMonstruos, sistema)) {
            return;
        }
        cargarRegistros(archivoRegistros, sistema);

        boolean seguir = true;
        while (seguir) {
            if (!solicitarUsuario(scanner, sistema)) {
                break;
            }
            seguir = ejecutarMenu(scanner, sistema, archivoRegistros);
        }
    }

    private static boolean cargarMonstruos(File archivo, Sistema sistema) {
        if (archivo == null || !archivo.exists()) {
            System.out.println("[!] No se encontró Monstruos.txt. El programa no puede continuar.");
            return false;
        }

        int numeroLinea = 0;
        try (Scanner lector = new Scanner(archivo)) {
            while (lector.hasNextLine()) {
                numeroLinea++;
                String linea = lector.nextLine();
                if (numeroLinea == 1 && linea.startsWith("\uFEFF")) {
                    linea = linea.substring(1);
                }
                if (linea.trim().isEmpty()) {
                    continue;
                }
                String motivo = sistema.registrarMonstruo(linea.split(";", -1));
                if (motivo != null) {
                    System.out.println("[!] Línea " + numeroLinea + " de " + archivo.getName()
                            + " descartada: \"" + linea.trim() + "\" (" + motivo + ").");
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("[!] No se pudo abrir Monstruos.txt. El programa no puede continuar.");
            return false;
        }

        System.out.println(" > " + sistema.cantidadMonstruos() + " monstruos cargados desde " + archivo.getName());
        if (sistema.cantidadMonstruos() == 0) {
            System.out.println("[!] Monstruos.txt no contiene monstruos válidos. El programa no puede continuar.");
            return false;
        }
        return true;
    }

    private static void cargarRegistros(File archivo, Sistema sistema) {
        if (archivo == null || !archivo.exists()) {
            System.out.println(" > Registros.txt no existe. Se iniciará sin jugadores y se creará al guardar.");
            return;
        }

        int numeroLinea = 0;
        try (Scanner lector = new Scanner(archivo)) {
            while (lector.hasNextLine()) {
                numeroLinea++;
                String linea = lector.nextLine();
                if (numeroLinea == 1 && linea.startsWith("\uFEFF")) {
                    linea = linea.substring(1);
                }
                if (linea.trim().isEmpty()) {
                    continue;
                }
                String motivo = sistema.registrarJugador(linea.split(";", -1));
                if (motivo != null) {
                    System.out.println("[!] Línea " + numeroLinea + " de " + archivo.getName()
                            + " descartada: \"" + linea.trim() + "\" (" + motivo + ").");
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(" > No se pudo leer Registros.txt. Se iniciará sin jugadores.");
            return;
        }

        System.out.println(" > " + sistema.cantidadJugadores() + " jugadores cargados desde " + archivo.getName());
    }

    private static boolean solicitarUsuario(Scanner scanner, Sistema sistema) {
        mostrarBanner();
        while (true) {
            System.out.print("Ingrese su nombre de usuario: ");
            if (!scanner.hasNextLine()) {
                return false;
            }
            String nombre = scanner.nextLine();
            String error = sistema.validarNombre(nombre);
            if (error != null) {
                System.out.println(error);
                continue;
            }

            boolean conocido = sistema.iniciarSesion(nombre);
            System.out.println();
            if (conocido) {
                System.out.println("¡Bienvenido de vuelta, " + sistema.getNombreSesion()
                        + "! Tu récord actual es de " + sistema.getRecordActual() + " puntos.");
            } else {
                System.out.println("¡Bienvenido, " + sistema.getNombreSesion() + "! Es tu primera vez jugando.");
            }
            return true;
        }
    }

    private static boolean ejecutarMenu(Scanner scanner, Sistema sistema, File archivoRegistros) {
        while (true) {
            try {
                mostrarMenuPrincipal(sistema.getNombreSesion());
                int opcion = leerEntero(scanner);
                if (opcion == -1) {
                    cerrarSesion(sistema, archivoRegistros, true);
                    return false;
                }

                switch (opcion) {
                    case 1:
                        jugar(scanner, sistema);
                        break;
                    case 2:
                        mostrarCatalogo(sistema);
                        break;
                    case 3:
                        mostrarHistorial(sistema);
                        break;
                    case 4:
                        mostrarRanking(sistema);
                        break;
                    case 5:
                        cerrarSesion(sistema, archivoRegistros, false);
                        return true;
                    case 6:
                        cerrarSesion(sistema, archivoRegistros, true);
                        return false;
                    default:
                        System.out.println("[!] Opción fuera de rango. Intente nuevamente.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("[!] Ocurrió un problema inesperado.");
            }
        }
    }

    private static void jugar(Scanner scanner, Sistema sistema) {
        if (sistema.cantidadMonstruosDisponibles() == 0) {
            System.out.println("No quedan monstruos disponibles.");
            return;
        }

        while (true) {
            mostrarMinijuegos(sistema);
            int opcionMinijuego = leerEntero(scanner);
            if (opcionMinijuego == 0) {
                return;
            }
            if (opcionMinijuego < 1 || opcionMinijuego > sistema.cantidadMinijuegos()) {
                System.out.println("[!] Opción fuera de rango. Intente nuevamente.");
                continue;
            }
            if (sistema.isMinijuegoJugado(opcionMinijuego - 1)) {
                System.out.println("[!] Ya jugaste " + sistema.getNombreMinijuego(opcionMinijuego - 1) + " en esta sesión.");
                continue;
            }

            if (elegirMonstruo(scanner, sistema, opcionMinijuego - 1)) {
                return;
            }
        }
    }

    private static boolean elegirMonstruo(Scanner scanner, Sistema sistema, int idxMinijuego) {
        while (true) {
            if (sistema.cantidadMonstruosDisponibles() == 0) {
                System.out.println("No quedan monstruos disponibles.");
                return true;
            }

            System.out.println();
            System.out.println(sistema.getNombreMinijuego(idxMinijuego) + ": " + sistema.getDescripcionMinijuego(idxMinijuego));
            System.out.println();
            System.out.println("Elige el monstruo en el que te transformarás:");
            int cantidad = sistema.cantidadMonstruosDisponibles();
            for (int i = 0; i < cantidad; i++) {
                System.out.println((i + 1) + ") " + sistema.describirMonstruoDisponible(i));
            }
            System.out.println("0) Volver");
            System.out.print("> ");

            int opcionMonstruo = leerEntero(scanner);
            if (opcionMonstruo == 0) {
                return false;
            }
            if (opcionMonstruo < 1 || opcionMonstruo > cantidad) {
                System.out.println("[!] Opción fuera de rango. Intente nuevamente.");
                continue;
            }

            int idxMonstruo = opcionMonstruo - 1;
            System.out.println();
            System.out.println("Te comes un dulce de dudosa procedencia...");
            System.out.println("Potencial para " + sistema.getNombreMinijuego(idxMinijuego)
                    + ": " + formatearDecimal(sistema.calcularPotencial(idxMinijuego, idxMonstruo)));
            System.out.println();

            if (elegirHabilidad(scanner, sistema, idxMinijuego, idxMonstruo)) {
                return true;
            }
        }
    }

    private static boolean elegirHabilidad(Scanner scanner, Sistema sistema, int idxMinijuego, int idxMonstruo) {
        while (true) {
            System.out.println("¿Quieres usar una habilidad?");
            int cantidad = sistema.cantidadHabilidades(idxMonstruo);
            for (int i = 0; i < cantidad; i++) {
                System.out.println((i + 1) + ") " + sistema.getNombreHabilidad(idxMonstruo, i));
            }
            System.out.println((cantidad + 1) + ") No usar habilidad");
            System.out.println("0) Volver");
            System.out.print("> ");

            int opcion = leerEntero(scanner);
            if (opcion == 0) {
                return false;
            }
            if (opcion < 1 || opcion > cantidad + 1) {
                System.out.println("[!] Opción fuera de rango. Intente nuevamente.");
                continue;
            }

            int opcionHabilidad = (opcion == cantidad + 1) ? 0 : opcion;
            ResultadoJugada resultado = sistema.jugar(idxMinijuego, idxMonstruo, opcionHabilidad);
            mostrarResultadoJugada(resultado);
            return true;
        }
    }

    private static void mostrarResultadoJugada(ResultadoJugada resultado) {
        if (resultado.getMensajeHabilidad() != null && !resultado.getMensajeHabilidad().isEmpty()) {
            System.out.println(resultado.getMensajeHabilidad());
            if (resultado.isEfectiva()) {
                System.out.println("¡La habilidad fue efectiva! Puntaje x1.5");
            } else {
                System.out.println("La habilidad no tuvo efecto en este minijuego.");
            }
        }
        System.out.println();
        System.out.println(">> Puntaje obtenido: " + resultado.getPuntaje() + " puntos");
    }

    private static void mostrarCatalogo(Sistema sistema) {
        System.out.println();
        System.out.println("------ CATÁLOGO DE MONSTRUOS ------");
        int cantidad = sistema.cantidadMonstruos();
        if (cantidad == 0) {
            System.out.println("No hay monstruos cargados.");
            return;
        }
        for (int i = 0; i < cantidad; i++) {
            System.out.println((i + 1) + ") " + sistema.describirCatalogo(i));
        }
    }

    private static void mostrarHistorial(Sistema sistema) {
        System.out.println();
        System.out.println("------ HISTORIAL DE LA SESIÓN ------");
        int cantidad = sistema.cantidadHistorial();
        if (cantidad == 0) {
            System.out.println("Aún no has jugado ningún minijuego en esta sesión.");
            return;
        }
        for (int i = 0; i < cantidad; i++) {
            System.out.println((i + 1) + ". " + sistema.getHistorialMinijuego(i)
                    + " | " + sistema.getHistorialMonstruo(i)
                    + " | Habilidad: " + sistema.getHistorialHabilidad(i)
                    + " | " + sistema.getHistorialPuntaje(i) + " pts");
        }
        System.out.println("Puntaje acumulado: " + sistema.getPuntajeAcumulado() + " pts");
    }

    private static void mostrarRanking(Sistema sistema) {
        System.out.println();
        System.out.println("------ RANKING ------");
        int cantidad = sistema.cantidadRanking();
        if (cantidad == 0) {
            System.out.println("No hay jugadores registrados.");
            return;
        }
        for (int i = 0; i < cantidad; i++) {
            System.out.println((i + 1) + ". " + rellenar(sistema.getNombreRanking(i), 16)
                    + sistema.getPuntajeRanking(i));
        }
    }

    private static void cerrarSesion(Sistema sistema, File archivoRegistros, boolean salirPrograma) {
        ResultadoSesion resultado = sistema.terminarSesion();
        System.out.println();
        if (!resultado.isJugo()) {
            System.out.println("No jugaste ningún minijuego, no se guardó registro.");
        } else {
            System.out.println("Sesión terminada. Puntaje total: " + resultado.getPuntajeTotal() + " puntos.");
            if (resultado.isNuevo()) {
                System.out.println("¡Quedaste registrado con " + resultado.getPuntajeTotal() + " puntos!");
            } else if (resultado.isSuperoRecord()) {
                System.out.println("¡Felicitaciones! Superaste tu récord (" + resultado.getRecordAnterior()
                        + "). Nuevo récord: " + resultado.getPuntajeTotal() + " puntos.");
            } else {
                System.out.println("No superaste tu récord (" + resultado.getRecordAnterior()
                        + "). ¡Suerte para la próxima!");
            }
            if (guardarRegistros(archivoRegistros, sistema)) {
                System.out.println("Registros guardados en " + archivoRegistros.getName() + ".");
            }
        }
        if (salirPrograma) {
            System.out.println("Hasta pronto.");
        }
    }

    private static boolean guardarRegistros(File archivo, Sistema sistema) {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(archivo))) {
            int cantidad = sistema.cantidadJugadores();
            for (int i = 0; i < cantidad; i++) {
                String linea = sistema.lineaRegistro(i);
                if (!linea.isEmpty()) {
                    escritor.write(linea);
                    escritor.newLine();
                }
            }
            return true;
        } catch (IOException e) {
            System.out.println("[!] No se pudo guardar Registros.txt: " + e.getMessage());
            return false;
        }
    }

    private static void mostrarBanner() {
        System.out.println();
        System.out.println("=========================================");
        System.out.println("             SPOOKY SEASON");
        System.out.println("=========================================");
    }

    private static void mostrarMenuPrincipal(String nombre) {
        System.out.println();
        System.out.println("------ MENÚ PRINCIPAL (" + nombre + ") ------");
        System.out.println("1) Jugar un minijuego");
        System.out.println("2) Ver catálogo de monstruos");
        System.out.println("3) Ver historial de la sesión");
        System.out.println("4) Ver ranking de jugadores");
        System.out.println("5) Terminar sesión");
        System.out.println("6) Salir");
        System.out.print("> ");
    }

    private static void mostrarMinijuegos(Sistema sistema) {
        System.out.println();
        System.out.println("------ MINIJUEGOS ------");
        int cantidad = sistema.cantidadMinijuegos();
        for (int i = 0; i < cantidad; i++) {
            String estado = sistema.isMinijuegoJugado(i)
                    ? "[JUGADO - " + sistema.getPuntajeMinijuego(i) + " pts]"
                    : "[PENDIENTE]";
            System.out.println((i + 1) + ") " + rellenar(sistema.getNombreMinijuego(i), 24) + estado);
        }
        System.out.println("0) Volver");
        System.out.print("> ");
    }

    private static int leerEntero(Scanner scanner) {
        while (true) {
            if (!scanner.hasNextLine()) {
                return -1;
            }
            String linea = scanner.nextLine().trim();
            if (linea.isEmpty()) {
                System.out.println("[!] Debe ingresar un número. Intente nuevamente.");
                System.out.print("> ");
                continue;
            }
            try {
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.println("[!] Debe ingresar un número. Intente nuevamente.");
                System.out.print("> ");
            }
        }
    }

    private static String formatearDecimal(double valor) {
        return String.format("%.2f", valor).replace(',', '.');
    }

    private static String rellenar(String texto, int ancho) {
        String base = texto == null ? "" : texto;
        StringBuilder builder = new StringBuilder(base);
        while (builder.length() < ancho) {
            builder.append(' ');
        }
        return builder.toString();
    }

    private static File resolverMonstruos() {
        File directo = new File("Monstruos.txt");
        if (directo.exists()) {
            return directo;
        }
        File enProyecto = new File("talleres/Taller03/taller3", "Monstruos.txt");
        if (enProyecto.exists()) {
            return enProyecto;
        }
        File enTaller = new File("taller3", "Monstruos.txt");
        if (enTaller.exists()) {
            return enTaller;
        }
        return directo;
    }

    private static File resolverRegistros(File archivoMonstruos) {
        File carpeta = archivoMonstruos.getParentFile();
        File juntoAMonstruos = carpeta == null
                ? new File("Registros.txt")
                : new File(carpeta, "Registros.txt");
        if (juntoAMonstruos.exists()) {
            return juntoAMonstruos;
        }
        File directo = new File("Registros.txt");
        if (directo.exists()) {
            return directo;
        }
        return juntoAMonstruos;
    }
}
