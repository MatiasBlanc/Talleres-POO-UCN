package problema2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import problema2.application.Sistema;

public class Main {
	public static void main(String[] args) {
		Sistema sistema = new Sistema();
		cargarFauna(sistema, "fauna.txt");
	}
	
	public static void cargarFauna(Sistema sistema, String arch) {
		File file = new File(arch);
		try (Scanner scanner = new Scanner(file)) {
			while (scanner.hasNextLine()) {
				String linea = scanner.nextLine();
				String[] partes = linea.split(",");
				sistema.agregarDinosaurio(partes);
 			}
		} catch (FileNotFoundException e) {
            System.out.println("No se encontró el archivo: " + arch);
            e.printStackTrace();
		}
	}

}
