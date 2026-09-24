/*
 * Matias Blanc - 22330528-8 - ICCI
 */

package taller1;
import java.util.Scanner;

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
}