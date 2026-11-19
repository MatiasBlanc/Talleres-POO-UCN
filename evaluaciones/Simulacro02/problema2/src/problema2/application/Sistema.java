package problema2.application;

import java.util.ArrayList;

import problema2.domain.Dinosaurios.Brachiosaurus;
import problema2.domain.Dinosaurios.Dinosaurio;
import problema2.domain.Dinosaurios.Tiranosaurio;
import problema2.domain.Dinosaurios.Triceratops;
import problema2.domain.Dinosaurios.Velociraptor;
public class Sistema {
	private ArrayList<Dinosaurio> listaFauna = new ArrayList<>();
	
	public void agregarDinosaurio(String[] partes) {
		String especie = partes[1];
		int peso = Integer.parseInt(partes[2]);
		double altura = Double.parseDouble(partes[3]);
		int velocidad = Integer.parseInt(partes[4]);
		String region = partes[5];
		Dinosaurio nuevo = null;
		switch(especie) {
		case "Tiranosaurio": {
			int cicatrices = Integer.parseInt(partes[6].trim());
			nuevo = new Tiranosaurio(peso, altura, velocidad, region, cicatrices);
			listaFauna.add(nuevo);
			break;
		}
		case "Triceratops": {
			String plantaFavorita = partes[6].trim();
			nuevo = new Triceratops(peso, altura, velocidad, region, plantaFavorita);
			listaFauna.add(nuevo);
			break;
		}
		case "Velociraptor": {
			int distancia = Integer.parseInt(partes[6].trim());
			nuevo = new Velociraptor(peso, altura, velocidad, region, distancia);
			listaFauna.add(nuevo);
			break;
		}
		case "Brachiosaurus": {
			double largoCuello = Double.parseDouble(partes[6].trim());
			nuevo = new Brachiosaurus(peso, altura, velocidad, region, largoCuello);
			listaFauna.add(nuevo);
			break;
		}
		}
		if (nuevo != null) {
            listaFauna.add(nuevo);
            System.out.println(nuevo);
        }
	}
	public void contarPorHemisferio() {}
	public void obtenerMasPesado() {}
	public void obtenerMasAlto() {}
	public void obtenerMasVeloz() {}
}
