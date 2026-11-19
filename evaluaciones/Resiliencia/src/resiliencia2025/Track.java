package resiliencia2025;

public class Track {
	private int idCancion;
	private String nombre;
	private String estilo;
	private int duracionSegundos;
	
	public Track(int idCancion, String nombre, String estilo, int duracionSegundos) {
		super();
		this.idCancion = idCancion;
		this.nombre = nombre;
		this.estilo = estilo;
		this.duracionSegundos = convertirASegundos(duracion);
	}

	public int getIdCancion() {
		return idCancion;
	}
	public String getNombre() {
		return nombre;
	}
	public String getEstilo() {
		return estilo;
	}
	public int getDuracion() {
		return duracionSegundos;
	}
	
	private int convertirASegundos(String duracion) {
		String[] partes = duracion.trim().split(":");
        int minutos = Integer.parseInt(partes[0]);
        int segundos = Integer.parseInt(partes[1]);
        int duracionSegundos = (minutos * 60) + segundos;
        return duracionSegundos;
	}
	
}
