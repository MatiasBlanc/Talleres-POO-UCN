package resiliencia2025;

public class Contenedor {
	private int id;
	private Disco[][] repisas = new Disco[5][10];
	
	public Contenedor(int id, Disco[][] repisas) {
		super();
		this.id = id;
		this.repisas = repisas;
	}
	
	public int getId() {
		return id;
	}
	public Disco[][] getRepisas() {
		return repisas;
	}
	
	public void colocarDisco(Disco disco) {
        int filaY = disco.getY() - 1;
        int columnaX = disco.getX() - 1;
        this.repisas[filaY][columnaX] = disco;
    }
	
}
