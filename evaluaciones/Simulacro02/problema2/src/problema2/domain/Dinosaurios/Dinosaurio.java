package problema2.domain.Dinosaurios;

public abstract class Dinosaurio {
	private int peso;
	private double altura;
	private int velocidad;
	private String region;
	
	public Dinosaurio(int peso, double altura, int velocidad, String region) {
		super();
		this.peso = peso;
		this.altura = altura;
		this.velocidad = velocidad;
		this.region = region;
	}
	public int getPeso() {
		return peso;
	}
	public double getAltura() {
		return altura;
	}
	public int getVelocidad() {
		return velocidad;
	}
	public String getRegion() {
		return region;
	}
	
	
}
