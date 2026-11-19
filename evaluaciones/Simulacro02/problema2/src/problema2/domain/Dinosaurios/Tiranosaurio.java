package problema2.domain.Dinosaurios;

public class Tiranosaurio extends Dinosaurio {
	private int cicatrices;

	public Tiranosaurio(int peso, double altura, int velocidad, String region, int cicatrices) {
		super(peso, altura, velocidad, region);
		this.cicatrices = cicatrices;
	}

	public int getCicatrices() {
		return cicatrices;
	}
}
