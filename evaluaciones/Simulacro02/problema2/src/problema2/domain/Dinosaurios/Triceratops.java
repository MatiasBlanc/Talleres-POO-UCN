package problema2.domain.Dinosaurios;

public class Triceratops extends Dinosaurio {
	private String plantaFavorita;

	public Triceratops(int peso, double altura, int velocidad, String region, String plantaFavorita) {
		super(peso, altura, velocidad, region);
		this.plantaFavorita = plantaFavorita;
	}

	public String getPlantaFavorita() {
		return plantaFavorita;
	}
	
	
}
