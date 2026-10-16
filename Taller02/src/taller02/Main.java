// Matias Blanc - 22330528-8 - ICCI

package taller02;

import java.util.Locale;
import java.util.Scanner;

import taller02.domain.Choripan;
import taller02.domain.Participante;
import taller02.domain.Terremoto;
import taller02.manager.SistemaFonda;

public class Main {

    public static void main(String[] args) {
        SistemaFonda sistema = new SistemaFonda();
        sistema.cargarParticipantes();
        sistema.cargarConsumidos();

        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            mostrarMenuPrincipal(sistema.getCantidadConsumos());
            opcion = leerEntero(sc, "Seleccione una opcion: ", 1, 4);

            if (opcion == 1) {
                menuVer(sc, sistema);
            } else if (opcion == 2) {
                menuActualizar(sc, sistema);
            } else if (opcion == 3) {
                menuMetricas(sc, sistema);
            } else if (opcion == 4) {
                System.out.println("\n>> Hasta luego...\n");
            }
        } while (opcion != 4);

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

    private static void menuVer(Scanner sc, SistemaFonda sistema) {
        int opcion;
        do {
            System.out.println("\n============ VER REGISTROS =================");
            System.out.println("  [1] Choripanes");
            System.out.println("  [2] Terremotos");
            System.out.println("  [3] Volver al menu principal");
            System.out.println("============================================");

            opcion = leerEntero(sc, "Seleccione una opcion: ", 1, 3);
            if (opcion == 1) {
                System.out.println();
                sistema.mostrarChoripanes();
                pausar(sc);
            } else if (opcion == 2) {
                System.out.println();
                sistema.mostrarTerremotos();
                pausar(sc);
            }
        } while (opcion != 3);
    }

    private static void menuActualizar(Scanner sc, SistemaFonda sistema) {
        int opcion;
        do {
            System.out.println("\n========== ACTUALIZAR REGISTROS ============");
            System.out.println("  [1] Agregar");
            System.out.println("  [2] Editar");
            System.out.println("  [3] Eliminar");
            System.out.println("  [4] Volver al menu principal");
            System.out.println("============================================");

            opcion = leerEntero(sc, "Seleccione una opcion: ", 1, 4);
            if (opcion == 1) {
                agregar(sc, sistema);
            } else if (opcion == 2) {
                editar(sc, sistema);
            } else if (opcion == 3) {
                eliminar(sc, sistema);
            }
        } while (opcion != 4);
    }

    private static void menuMetricas(Scanner sc, SistemaFonda sistema) {
        int opcion;
        do {
            System.out.println("\n============ CALCULAR METRICAS =============");
            System.out.println("  [1] Choripanes comidos en total");
            System.out.println("  [2] Terremotos tomados en total");
            System.out.println("  [3] Choripanes comidos por usuario");
            System.out.println("  [4] Terremotos tomados por usuario");
            System.out.println("  [5] KCal consumidas por usuario");
            System.out.println("  [6] Dias ebrio por usuario");
            System.out.println("  [7] Volver al menu principal");
            System.out.println("============================================");

            opcion = leerEntero(sc, "Seleccione una opcion: ", 1, 7);
            if (opcion == 1) {
                System.out.println();
                sistema.mostrarTotalChoripanes();
                pausar(sc);
            } else if (opcion == 2) {
                System.out.println();
                sistema.mostrarTotalTerremotos();
                pausar(sc);
            } else if (opcion == 3) {
                System.out.println();
                sistema.mostrarChoripanesPorUsuario();
                pausar(sc);
            } else if (opcion == 4) {
                System.out.println();
                sistema.mostrarTerremotosPorUsuario();
                pausar(sc);
            } else if (opcion == 5) {
                System.out.println();
                sistema.mostrarKcalPorUsuario();
                pausar(sc);
            } else if (opcion == 6) {
                System.out.println();
                sistema.mostrarDiasEbrioPorUsuario();
                pausar(sc);
            }
        } while (opcion != 7);
    }

    private static void agregar(Scanner sc, SistemaFonda sistema) {
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
            double largo = leerDouble(sc, "Largo del choripan (cm): ");
            double ancho = leerDouble(sc, "Ancho del choripan (cm): ");
            String fecha = leerTexto(sc, "Fecha (YYYY-MM-DD): ");

            Choripan ch = sistema.agregarChoripan(consumidor, largo, ancho, fecha);
            if (ch != null) {
                String l = (ch.getLargo() == (int) ch.getLargo()) ? String.valueOf((int) ch.getLargo()) : String.valueOf(ch.getLargo());
                String a = (ch.getAncho() == (int) ch.getAncho()) ? String.valueOf((int) ch.getAncho()) : String.valueOf(ch.getAncho());
                System.out.println("\n>> Registro agregado correctamente y guardado en Consumido.txt");
                System.out.printf(Locale.US, "   Choripan | %s | %s cm x %s cm | %s | %.2f kcal%n",
                        ch.getConsumidor(), l, a, ch.getFecha(), ch.calcularKcal());
            }
        } else {
            double litros = leerDouble(sc, "Litros del terremoto: ");
            String fecha = leerTexto(sc, "Fecha (YYYY-MM-DD): ");

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
            return leerTexto(sc, "Ingrese nombre del consumidor: ");
        }

        System.out.println("\nConsumidores disponibles:");
        for (int i = 0; i < cant; i++) {
            System.out.printf("  [%d] %s%n", (i + 1), participantes[i].getNombre());
        }
        int seleccion = leerEntero(sc, "Seleccione el consumidor: ", 1, cant);
        return participantes[seleccion - 1].getNombre();
    }

    private static void editar(Scanner sc, SistemaFonda sistema) {
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
            String lActual = (ch.getLargo() == (int) ch.getLargo()) ? String.valueOf((int) ch.getLargo()) : String.valueOf(ch.getLargo());
            String aActual = (ch.getAncho() == (int) ch.getAncho()) ? String.valueOf((int) ch.getAncho()) : String.valueOf(ch.getAncho());

            System.out.printf("%nRegistro actual -> Choripan | %s | %s cm x %s cm | %s%n",
                    ch.getConsumidor(), lActual, aActual, ch.getFecha());
            System.out.println("Deje el campo vacio para mantener el valor actual.\n");

            System.out.printf("Consumidor [%s]: ", ch.getConsumidor());
            String nuevoConsumidor = sc.nextLine().trim();
            if (!nuevoConsumidor.isEmpty()) {
                ch.setConsumidor(nuevoConsumidor);
            }

            ch.setLargo(leerDoubleOpcional(sc, "Largo (cm) [" + lActual + "]: ", ch.getLargo()));
            ch.setAncho(leerDoubleOpcional(sc, "Ancho (cm) [" + aActual + "]: ", ch.getAncho()));

            System.out.printf("Fecha [%s]: ", ch.getFecha());
            String nuevaFecha = sc.nextLine().trim();
            if (!nuevaFecha.isEmpty()) {
                ch.setFecha(nuevaFecha);
            }

            sistema.guardarConsumidos();
            String lFinal = (ch.getLargo() == (int) ch.getLargo()) ? String.valueOf((int) ch.getLargo()) : String.valueOf(ch.getLargo());
            String aFinal = (ch.getAncho() == (int) ch.getAncho()) ? String.valueOf((int) ch.getAncho()) : String.valueOf(ch.getAncho());
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
            String litActual = (t.getLitros() == (int) t.getLitros()) ? String.valueOf((int) t.getLitros()) : String.valueOf(t.getLitros());

            System.out.printf("%nRegistro actual -> Terremoto | %s | %s L | %s%n",
                    t.getConsumidor(), litActual, t.getFecha());
            System.out.println("Deje el campo vacio para mantener el valor actual.\n");

            System.out.printf("Consumidor [%s]: ", t.getConsumidor());
            String nuevoConsumidor = sc.nextLine().trim();
            if (!nuevoConsumidor.isEmpty()) {
                t.setConsumidor(nuevoConsumidor);
            }

            t.setLitros(leerDoubleOpcional(sc, "Litros [" + litActual + "]: ", t.getLitros()));

            System.out.printf("Fecha [%s]: ", t.getFecha());
            String nuevaFecha = sc.nextLine().trim();
            if (!nuevaFecha.isEmpty()) {
                t.setFecha(nuevaFecha);
            }

            sistema.guardarConsumidos();
            String litFinal = (t.getLitros() == (int) t.getLitros()) ? String.valueOf((int) t.getLitros()) : String.valueOf(t.getLitros());
            System.out.println("\n>> Registro actualizado correctamente y guardado en Consumido.txt");
            System.out.printf(Locale.US, "   Terremoto | %s | %s L | %s | %.2f kcal%n",
                    t.getConsumidor(), litFinal, t.getFecha(), t.calcularKcal());
        }
    }

    private static void eliminar(Scanner sc, SistemaFonda sistema) {
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
            String l = (ch.getLargo() == (int) ch.getLargo()) ? String.valueOf((int) ch.getLargo()) : String.valueOf(ch.getLargo());
            String a = (ch.getAncho() == (int) ch.getAncho()) ? String.valueOf((int) ch.getAncho()) : String.valueOf(ch.getAncho());
            String detalle = "Choripan | " + ch.getConsumidor() + " | " + l + " cm x " + a + " cm | " + ch.getFecha();

            System.out.printf("Confirma la eliminacion de \"%s\"? (s/n): ", detalle);
            String confirm = sc.nextLine().trim();
            if (confirm.equalsIgnoreCase("s") || confirm.equalsIgnoreCase("si")) {
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
            String lit = (t.getLitros() == (int) t.getLitros()) ? String.valueOf((int) t.getLitros()) : String.valueOf(t.getLitros());
            String detalle = "Terremoto | " + t.getConsumidor() + " | " + lit + " L | " + t.getFecha();

            System.out.printf("Confirma la eliminacion de \"%s\"? (s/n): ", detalle);
            String confirm = sc.nextLine().trim();
            if (confirm.equalsIgnoreCase("s") || confirm.equalsIgnoreCase("si")) {
                sistema.eliminarConsumo(t);
                System.out.println("\n>> Registro eliminado correctamente. Cambios guardados en Consumido.txt");
            } else {
                System.out.println(">> Operacion cancelada.");
            }
        }
    }

    private static int leerEntero(Scanner sc, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println(">> Error: no ingreso ninguna opcion. Intente nuevamente.");
                continue;
            }
            try {
                int val = Integer.parseInt(input);
                if (val < min || val > max) {
                    System.out.println(">> Error: opcion fuera de rango. Intente nuevamente.");
                    continue;
                }
                return val;
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
                int val = Integer.parseInt(input);
                if (val < min || val > max) {
                    System.out.println(">> Error: no existe un registro con ese numero. Intente nuevamente.");
                    continue;
                }
                return val;
            } catch (NumberFormatException e) {
                System.out.println(">> Error: debe ingresar un numero valido. Intente nuevamente.");
            }
        }
    }

    private static double leerDouble(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim().replace(',', '.');
            if (input.isEmpty()) {
                System.out.println(">> Error: no ingreso ningun valor. Intente nuevamente.");
                continue;
            }
            try {
                double val = Double.parseDouble(input);
                if (val <= 0) {
                    System.out.println(">> Error: el valor debe ser un numero mayor a 0. Intente nuevamente.");
                    continue;
                }
                return val;
            } catch (NumberFormatException e) {
                System.out.println(">> Error: debe ingresar un numero valido. Intente nuevamente.");
            }
        }
    }

    private static double leerDoubleOpcional(Scanner sc, String prompt, double actual) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim().replace(',', '.');
            if (input.isEmpty()) {
                return actual;
            }
            try {
                double val = Double.parseDouble(input);
                if (val <= 0) {
                    System.out.println(">> Error: el valor debe ser un numero mayor a 0. Intente nuevamente.");
                    continue;
                }
                return val;
            } catch (NumberFormatException e) {
                System.out.println(">> Error: debe ingresar un numero valido. Intente nuevamente.");
            }
        }
    }

    private static String leerTexto(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println(">> Error: el campo no puede estar vacio. Intente nuevamente.");
                continue;
            }
            return input;
        }
    }

    private static void pausar(Scanner sc) {
        System.out.println("\nPresione ENTER para continuar...");
        sc.nextLine();
    }
}
