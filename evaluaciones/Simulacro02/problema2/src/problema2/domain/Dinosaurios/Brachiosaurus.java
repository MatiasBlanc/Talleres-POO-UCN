package problema2.domain.Dinosaurios;

public class Brachiosaurus extends Dinosaurio {
	private double largoCuello;

	public Brachiosaurus(int peso, double altura, int velocidad, String region, double largoCuello) {
		super(peso, altura, velocidad, region);
		this.largoCuello = largoCuello;
	}

	public double getLargoCuello() {
		return largoCuello;
	}
}
