package resiliencia2025;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Inventario {
	private ArrayList<Disco> discos = new ArrayList<>();
	private Contenedor[] contenedores = new Contenedor[4];

	public Inventario() {
        for (int i = 0; i < 4; i++) {
            contenedores[i] = new Contenedor(i + 1);
        }
    }
	
	
	public void cargarDiscos(String arch) throws Exception {
		File file = new File(arch);
		Scanner sc = new Scanner(file); 
		
		while (sc.hasNextLine()) {
		    String linea = sc.nextLine();
		    String[] partes = linea.split(",");
		    
		    int idDisco = Integer.parseInt(partes[0]);
		    String nombre = partes[1];
		    String artista = partes[2];
		    int idContenedor = Integer.parseInt(partes[3]);
		    int x = Integer.parseInt(partes[4]);
		    int y = Integer.parseInt(partes[5]);
		    
		    Disco nuevoDisco = new Disco(idDisco, nombre, artista, idContenedor, x, y);
		    discos.add(nuevoDisco);
		    contenedores[idContenedor - 1].colocarDisco(nuevoDisco);
		}
		
	}
	
	public void cargarTracks(String arch) {
	}
	
	public void mostrarPosicionesLibresYPorcentajeUso() {
		
	}
	public void mostrarDiscosPorArtista() {
    	
    }
	public void proporcionArtistaEnContenedores() {
    	
    }
	public void buscarTrack(String nombre) {
    	
    }
	public void intercambiar(String arch) {
    }
}
