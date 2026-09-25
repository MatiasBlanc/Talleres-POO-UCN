// Matias Blanc - 22330528-8 - ICCI

package taller02;

import java.util.Scanner;
import java.util.Locale;

import taller02.domain.Choripan;
import taller02.domain.Participante;
import taller02.domain.Terremoto;
import taller02.manager.SistemaFonda;

/**
 * Clase principal del sistema La Fonda del Papu.
 * Contiene el punto de entrada y la interaccion del menu con el usuario.
 */
public class Main {

    public static void main(String[] args) {
        SistemaFonda sistema = new SistemaFonda();
        sistema.cargarParticipantes();
        sistema.cargarConsumidos();

        Scanner sc = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            mostrarMenuPrincipal(sistema.getCantidadConsumos());
            int opcion = leerEntero(sc, "Seleccione una opcion: ", 1, 4);

            switch (opcion) {
                case 1:
                    menuVerRegistros(sc, sistema);
                    break;
                case 2:
                    menuActualizarRegistros(sc, sistema);
                    break;
                case 3:
                    menuCalcularMetricas(sc, sistema);
                    break;
                case 4:
                    salir = true;
                    System.out.println("\n>> Hasta luego...\n");
                    break;
                default:
                    break;
            }
        }

        sc.close();
    }

    private static void mostrarMenuPrincipal(int registrosCargados) {
        System.out.println("============================================");
        System.out.println("           LA FONDA DEL PAPU");
        System.out.println("============================================");
        System.out.println("  Registros cargados: " + registrosCargados);
        System.out.println("--------------------------------------------");
        System.out.println("  [1] Ver registros");
        System.out.println("  [2] Actualizar registros");
        System.out.println("  [3] Calcular metricas");
        System.out.println("  [4] Salir");
        System.out.println("============================================");
    }

    // ==========================================
    // 1. SUBMENU: VER REGISTROS
    // ==========================================
    private static void menuVerRegistros(Scanner sc, SistemaFonda sistema) {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n============ VER REGISTROS =================");
            System.out.println("  [1] Choripanes");
            System.out.println("  [2] Terremotos");
            System.out.println("  [3] Volver al menu principal");
            System.out.println("============================================");

            int opcion = leerEntero(sc, "Seleccione una opcion: ", 1, 3);
            switch (opcion) {
                case 1:
                    System.out.println();
                    sistema.mostrarChoripanes();
                    pausar(sc);
                    break;
                case 2:
                    System.out.println();
                    sistema.mostrarTerremotos();
                    pausar(sc);
                    break;
                case 3:
                    volver = true;
                    break;
                default:
                    break;
            }
        }
    }

    // ==========================================
    // 2. SUBMENU: ACTUALIZAR REGISTROS
    // ==========================================
    private static void menuActualizarRegistros(Scanner sc, SistemaFonda sistema) {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n========== ACTUALIZAR REGISTROS ============");
            System.out.println("  [1] Agregar");
            System.out.println("  [2] Editar");
            System.out.println("  [3] Eliminar");
            System.out.println("  [4] Volver al menu principal");
            System.out.println("============================================");

            int opcion = leerEntero(sc, "Seleccione una opcion: ", 1, 4);
            switch (opcion) {
                case 1:
                    flujoAgregarRegistro(sc, sistema);
                    break;
                case 2:
                    flujoEditarRegistro(sc, sistema);
                    break;
                case 3:
                    flujoEliminarRegistro(sc, sistema);
                    break;
                case 4:
                    volver = true;
                    break;
                default:
                    break;
            }
        }
    }

    private static void flujoAgregarRegistro(Scanner sc, SistemaFonda sistema) {
        System.out.println("\nTipo de registro:");
        System.out.println("  [1] Choripan");
        System.out.println("  [2] Terremoto");
        System.out.println("  [3] Cancelar");
        int tipo = leerEntero(sc, "Seleccione una opcion: ", 1, 3);
        if (tipo == 3) {
            return;
        }

        String consumidor = seleccionarConsumidor(sc, sistema);
        if (consumidor == null) {
            return;
        }

        if (tipo == 1) {
            double largo = leerDoublePositivo(sc, "Largo del choripan (cm): ");
            double ancho = leerDoublePositivo(sc, "Ancho del choripan (cm): ");
            String fecha = leerFecha(sc, "Fecha (YYYY-MM-DD): ");

            Choripan ch = sistema.agregarChoripan(consumidor, largo, ancho, fecha);
            if (ch != null) {
                String largoStr = ((long) ch.getLargo() == ch.getLargo()) ? String.valueOf((long) ch.getLargo()) : String.valueOf(ch.getLargo());
                String anchoStr = ((long) ch.getAncho() == ch.getAncho()) ? String.valueOf((long) ch.getAncho()) : String.valueOf(ch.getAncho());
                System.out.println("\n>> Registro agregado correctamente y guardado en Consumido.txt");
                System.out.printf(Locale.US, "   Choripan | %s | %s cm x %s cm | %s | %.2f kcal%n",
                        ch.getConsumidor(), largoStr, anchoStr, ch.getFecha(), ch.calcularKcal());
            }
        } else {
            double litros = leerDoublePositivo(sc, "Litros del terremoto: ");
            String fecha = leerFecha(sc, "Fecha (YYYY-MM-DD): ");

            Terremoto t = sistema.agregarTerremoto(consumidor, litros, fecha);
            if (t != null) {
                System.out.println("\n>> Registro agregado correctamente y guardado en Consumido.txt");
                System.out.printf(Locale.US, "   Terremoto | %s | %.2f L | %s | %.2f kcal%n",
                        t.getConsumidor(), t.getLitros(), t.getFecha(), t.calcularKcal());
            }
        }
    }

    private static String seleccionarConsumidor(Scanner sc, SistemaFonda sistema) {
        Participante[] participantes = sistema.getParticipantes();
        int cant = sistema.getCantidadParticipantes();

        if (cant == 0) {
            System.out.print("Ingrese nombre del consumidor: ");
            String nom = sc.nextLine().trim();
            while (nom.isEmpty()) {
                System.out.println(">> Error: el nombre no puede estar vacio. Intente nuevamente.");
                System.out.print("Ingrese nombre del consumidor: ");
                nom = sc.nextLine().trim();
            }
            return nom;
        }

        System.out.println("\nConsumidores disponibles:");
        for (int i = 0; i < cant; i++) {
            System.out.printf("  [%d] %s%n", (i + 1), participantes[i].getName());
        }
        int seleccion = leerEntero(sc, "Seleccione el consumidor: ", 1, cant);
        return participantes[seleccion - 1].getName();
    }

    private static void flujoEditarRegistro(Scanner sc, SistemaFonda sistema) {
        System.out.println("\nTipo de registro a editar:");
        System.out.println("  [1] Choripan");
        System.out.println("  [2] Terremoto");
        System.out.println("  [3] Cancelar");
        int tipo = leerEntero(sc, "Seleccione una opcion: ", 1, 3);
        if (tipo == 3) {
            return;
        }

        if (tipo == 1) {
            int total = sistema.contarChoripanes();
            if (total == 0) {
                System.out.println(">> No hay choripanes registrados para editar.");
                return;
            }
            System.out.println();
            sistema.mostrarChoripanesParaEdicion();
            System.out.println();

            int seleccion = leerEnteroOpcionalCero(sc, "Seleccione el registro a editar (0 para cancelar): ", 0, total);
            if (seleccion == 0) {
                return;
            }

            Choripan ch = sistema.obtenerChoripan(seleccion);
            if (ch == null) {
                System.out.println(">> Error: no existe un registro con ese numero.");
                return;
            }

            String largoActual = ((long) ch.getLargo() == ch.getLargo()) ? String.valueOf((long) ch.getLargo()) : String.valueOf(ch.getLargo());
            String anchoActual = ((long) ch.getAncho() == ch.getAncho()) ? String.valueOf((long) ch.getAncho()) : String.valueOf(ch.getAncho());
            System.out.printf("%nRegistro actual -> Choripan | %s | %s cm x %s cm | %s%n",
                    ch.getConsumidor(), largoActual, anchoActual, ch.getFecha());
            System.out.println("Deje el campo vacio para mantener el valor actual.\n");

            System.out.printf("Consumidor [%s]: ", ch.getConsumidor());
            String nuevoConsumidor = sc.nextLine().trim();
            if (!nuevoConsumidor.isEmpty()) {
                ch.setConsumidor(nuevoConsumidor);
            }

            double nuevoLargo = leerDoubleOpcional(sc, "Largo (cm) [" + largoActual + "]: ", ch.getLargo());
            ch.setLargo(nuevoLargo);

            double nuevoAncho = leerDoubleOpcional(sc, "Ancho (cm) [" + anchoActual + "]: ", ch.getAncho());
            ch.setAncho(nuevoAncho);

            System.out.printf("Fecha [%s]: ", ch.getFecha());
            String nuevaFecha = sc.nextLine().trim();
            if (!nuevaFecha.isEmpty()) {
                ch.setFecha(nuevaFecha);
            }

            sistema.guardarConsumidos();
            String lFinal = ((long) ch.getLargo() == ch.getLargo()) ? String.valueOf((long) ch.getLargo()) : String.valueOf(ch.getLargo());
            String aFinal = ((long) ch.getAncho() == ch.getAncho()) ? String.valueOf((long) ch.getAncho()) : String.valueOf(ch.getAncho());
            System.out.println("\n>> Registro actualizado correctamente y guardado en Consumido.txt");
            System.out.printf(Locale.US, "   Choripan | %s | %s cm x %s cm | %s | %.2f kcal%n",
                    ch.getConsumidor(), lFinal, aFinal, ch.getFecha(), ch.calcularKcal());

        } else {
            int total = sistema.contarTerremotos();
            if (total == 0) {
                System.out.println(">> No hay terremotos registrados para editar.");
                return;
            }
            System.out.println();
            sistema.mostrarTerremotosParaEdicion();
            System.out.println();

            int seleccion = leerEnteroOpcionalCero(sc, "Seleccione el registro a editar (0 para cancelar): ", 0, total);
            if (seleccion == 0) {
                return;
            }

            Terremoto t = sistema.obtenerTerremoto(seleccion);
            if (t == null) {
                System.out.println(">> Error: no existe un registro con ese numero.");
                return;
            }

            String litrosActual = ((long) t.getLitros() == t.getLitros()) ? String.valueOf((long) t.getLitros()) : String.valueOf(t.getLitros());
            System.out.printf("%nRegistro actual -> Terremoto | %s | %s L | %s%n",
                    t.getConsumidor(), litrosActual, t.getFecha());
            System.out.println("Deje el campo vacio para mantener el valor actual.\n");

            System.out.printf("Consumidor [%s]: ", t.getConsumidor());
            String nuevoConsumidor = sc.nextLine().trim();
            if (!nuevoConsumidor.isEmpty()) {
                t.setConsumidor(nuevoConsumidor);
            }

            double nuevosLitros = leerDoubleOpcional(sc, "Litros [" + litrosActual + "]: ", t.getLitros());
            t.setLitros(nuevosLitros);

            System.out.printf("Fecha [%s]: ", t.getFecha());
            String nuevaFecha = sc.nextLine().trim();
            if (!nuevaFecha.isEmpty()) {
                t.setFecha(nuevaFecha);
            }

            sistema.guardarConsumidos();
            String litFinal = ((long) t.getLitros() == t.getLitros()) ? String.valueOf((long) t.getLitros()) : String.valueOf(t.getLitros());
            System.out.println("\n>> Registro actualizado correctamente y guardado en Consumido.txt");
            System.out.printf(Locale.US, "   Terremoto | %s | %s L | %s | %.2f kcal%n",
                    t.getConsumidor(), litFinal, t.getFecha(), t.calcularKcal());
        }
    }

    private static void flujoEliminarRegistro(Scanner sc, SistemaFonda sistema) {
        System.out.println("\nTipo de registro a eliminar:");
        System.out.println("  [1] Choripan");
        System.out.println("  [2] Terremoto");
        System.out.println("  [3] Cancelar");
        int tipo = leerEntero(sc, "Seleccione una opcion: ", 1, 3);
        if (tipo == 3) {
            return;
        }

        if (tipo == 1) {
            int total = sistema.contarChoripanes();
            if (total == 0) {
                System.out.println(">> No hay choripanes registrados para eliminar.");
                return;
            }
            System.out.println();
            sistema.mostrarChoripanesParaEdicion();
            System.out.println();

            int seleccion = leerEnteroOpcionalCero(sc, "Seleccione el registro a eliminar (0 para cancelar): ", 0, total);
            if (seleccion == 0) {
                return;
            }

            Choripan ch = sistema.obtenerChoripan(seleccion);
            if (ch == null) {
                System.out.println(">> Error: no existe un registro con ese numero.");
                return;
            }

            String largoStr = ((long) ch.getLargo() == ch.getLargo()) ? String.valueOf((long) ch.getLargo()) : String.valueOf(ch.getLargo());
            String anchoStr = ((long) ch.getAncho() == ch.getAncho()) ? String.valueOf((long) ch.getAncho()) : String.valueOf(ch.getAncho());
            String detalle = "Choripan | " + ch.getConsumidor() + " | " + largoStr + " cm x " + anchoStr + " cm | " + ch.getFecha();

            System.out.printf("Confirma la eliminacion de \"%s\"? (s/n): ", detalle);
            String confirmacion = sc.nextLine().trim();
            if (confirmacion.equalsIgnoreCase("s") || confirmacion.equalsIgnoreCase("si")) {
                sistema.eliminarConsumo(ch);
                System.out.println("\n>> Registro eliminado correctamente. Cambios guardados en Consumido.txt");
            } else {
                System.out.println(">> Operacion cancelada.");
            }

        } else {
            int total = sistema.contarTerremotos();
            if (total == 0) {
                System.out.println(">> No hay terremotos registrados para eliminar.");
                return;
            }
            System.out.println();
            sistema.mostrarTerremotosParaEdicion();
            System.out.println();

            int seleccion = leerEnteroOpcionalCero(sc, "Seleccione el registro a eliminar (0 para cancelar): ", 0, total);
            if (seleccion == 0) {
                return;
            }

            Terremoto t = sistema.obtenerTerremoto(seleccion);
            if (t == null) {
                System.out.println(">> Error: no existe un registro con ese numero.");
                return;
            }

            String litrosStr = ((long) t.getLitros() == t.getLitros()) ? String.valueOf((long) t.getLitros()) : String.valueOf(t.getLitros());
            String detalle = "Terremoto | " + t.getConsumidor() + " | " + litrosStr + " L | " + t.getFecha();

            System.out.printf("Confirma la eliminacion de \"%s\"? (s/n): ", detalle);
            String confirmacion = sc.nextLine().trim();
            if (confirmacion.equalsIgnoreCase("s") || confirmacion.equalsIgnoreCase("si")) {
                sistema.eliminarConsumo(t);
                System.out.println("\n>> Registro eliminado correctamente. Cambios guardados en Consumido.txt");
            } else {
                System.out.println(">> Operacion cancelada.");
            }
        }
    }

    // ==========================================
    // 3. SUBMENU: CALCULAR METRICAS
    // ==========================================
    private static void menuCalcularMetricas(Scanner sc, SistemaFonda sistema) {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n============ CALCULAR METRICAS =============");
            System.out.println("  [1] Choripanes comidos en total");
            System.out.println("  [2] Terremotos tomados en total");
            System.out.println("  [3] Choripanes comidos por usuario");
            System.out.println("  [4] Terremotos tomados por usuario");
            System.out.println("  [5] KCal consumidas por usuario");
            System.out.println("  [6] Dias ebrio por usuario");
            System.out.println("  [7] Volver al menu principal");
            System.out.println("============================================");

            int opcion = leerEntero(sc, "Seleccione una opcion: ", 1, 7);
            switch (opcion) {
                case 1:
                    System.out.println();
                    sistema.mostrarTotalChoripanes();
                    pausar(sc);
                    break;
                case 2:
                    System.out.println();
                    sistema.mostrarTotalTerremotos();
                    pausar(sc);
                    break;
                case 3:
                    System.out.println();
                    sistema.mostrarChoripanesPorUsuario();
                    pausar(sc);
                    break;
                case 4:
                    System.out.println();
                    sistema.mostrarTerremotosPorUsuario();
                    pausar(sc);
                    break;
                case 5:
                    System.out.println();
                    sistema.mostrarKcalPorUsuario();
                    pausar(sc);
                    break;
                case 6:
                    System.out.println();
                    sistema.mostrarDiasEbrioPorUsuario();
                    pausar(sc);
                    break;
                case 7:
                    volver = true;
                    break;
                default:
                    break;
            }
        }
    }

    // ==========================================
    // UTILIDADES DE ENTRADA Y CONTROL DE ERRORES
    // ==========================================
    private static int leerEntero(Scanner sc, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println(">> Error: no ingreso ninguna opcion. Intente nuevamente.");
                continue;
            }
            try {
                int valor = Integer.parseInt(input);
                if (valor < min || valor > max) {
                    System.out.println(">> Error: opcion fuera de rango. Intente nuevamente.");
                    continue;
                }
                return valor;
            } catch (NumberFormatException e) {
                System.out.println(">> Error: la opcion debe ser un numero entre " + min + " y " + max + ". Intente nuevamente.");
            }
        }
    }

    private static int leerEnteroOpcionalCero(Scanner sc, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println(">> Error: no ingreso ninguna opcion. Intente nuevamente.");
                continue;
            }
            try {
                int valor = Integer.parseInt(input);
                if (valor < min || valor > max) {
                    System.out.println(">> Error: no existe un registro con ese numero. Intente nuevamente.");
                    continue;
                }
                return valor;
            } catch (NumberFormatException e) {
                System.out.println(">> Error: debe ingresar un numero valido. Intente nuevamente.");
            }
        }
    }

    private static double leerDoublePositivo(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim().replace(',', '.');
            if (input.isEmpty()) {
                System.out.println(">> Error: no ingreso ningun valor. Intente nuevamente.");
                continue;
            }
            try {
                double valor = Double.parseDouble(input);
                if (valor <= 0) {
                    System.out.println(">> Error: el valor debe ser un numero mayor a 0. Intente nuevamente.");
                    continue;
                }
                return valor;
            } catch (NumberFormatException e) {
                System.out.println(">> Error: debe ingresar un numero valido. Intente nuevamente.");
            }
        }
    }

    private static double leerDoubleOpcional(Scanner sc, String prompt, double valorActual) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim().replace(',', '.');
            if (input.isEmpty()) {
                return valorActual;
            }
            try {
                double valor = Double.parseDouble(input);
                if (valor <= 0) {
                    System.out.println(">> Error: el valor debe ser un numero mayor a 0. Intente nuevamente.");
                    continue;
                }
                return valor;
            } catch (NumberFormatException e) {
                System.out.println(">> Error: debe ingresar un numero valido. Intente nuevamente.");
            }
        }
    }

    private static String leerFecha(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String fecha = sc.nextLine().trim();
            if (fecha.isEmpty()) {
                System.out.println(">> Error: la fecha no puede estar vacia. Intente nuevamente.");
                continue;
            }
            return fecha;
        }
    }

    private static void pausar(Scanner sc) {
        System.out.println("\nPresione ENTER para continuar...");
        sc.nextLine();
    }
}
