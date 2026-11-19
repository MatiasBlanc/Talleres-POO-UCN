package problema2.domain.Dinosaurios;

public class Velociraptor extends Dinosaurio {
	private int distancia;

	public Velociraptor(int peso, double altura, int velocidad, String region, int distancia) {
		super(peso, altura, velocidad, region);
		this.distancia = distancia;
	}

	public int getDistancia() {
		return distancia;
	}
	
	
}
