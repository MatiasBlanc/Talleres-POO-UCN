package solve;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MySolution {
	 public static void main(String[] args) throws FileNotFoundException {
	       String[][][] sopas = leerArchivoSopas();
	       String[][] palabras = leerArchivoAgenda();
	   }

	   public static String[][][] leerArchivoSopas() throws FileNotFoundException {
	       Scanner scanner = new Scanner(new File("txts/sopas.txt"));
	       int cantidad = Integer.parseInt(scanner.nextLine());
	       String[][][] sopas = new String[cantidad][][];

	       for (int i = 0; i < cantidad; i++) {
	           String[] dimensiones = scanner.nextLine().split("x");
	           int filas = Integer.parseInt(dimensiones[0]);
	           int columnas = Integer.parseInt(dimensiones[1]);

	           sopas[i] = new String[filas][columnas];

	           for (int fila = 0; fila < filas; fila++) {
	               String[] letras = scanner.nextLine().split(",");

	               for (int columna = 0; columna < columnas; columna++) {
	                   sopas[i][fila][columna] = letras[columna];
	               }
	           }
	           
	       }

	       scanner.close();
	       return sopas;
	   }

	   public static String[][] leerArchivoAgenda() throws FileNotFoundException {
	       Scanner scanner = new Scanner(new File("txts/agenda.txt"));
	       String[] palabras = new String[100];
	       int cantidad = 0;

	       while (scanner.hasNextLine() && cantidad < palabras.length) {
	           palabras[cantidad] = scanner.nextLine();
	           cantidad++;
	       }

	       scanner.close();
	       return palabras;
	   }
}
