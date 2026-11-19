import java.util.ArrayList;
import java.util.List;

public class Disco {
    private int id;
    private String nombre;
    private String artista;
    private int idContenedor;
    private int x;
    private int y;
    private List<Track> tracks;

    public Disco(int id, String nombre, String artista, int idContenedor, int x, int y) {
        this.id = id;
        this.nombre = nombre;
        this.artista = artista;
        this.idContenedor = idContenedor;
        this.x = x;
        this.y = y;
        this.tracks = new ArrayList<>();
    }

    public void agregarTrack(Track track) {
        this.tracks.add(track);
    }

    public boolean buscarTrack(String nombreTrack) {
        for (Track t : tracks) {
            if (t.getNombre().equalsIgnoreCase(nombreTrack.trim())) {
                return true;
            }
        }
        return false;
    }

    public int getDuracionTotalSegundos() {
        int total = 0;
        for (Track t : tracks) {
            total += t.getDuracionSegundos();
        }
        return total;
    }

    public void setUbicacion(int idContenedor, int x, int y) {
        this.idContenedor = idContenedor;
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getArtista() {
        return artista;
    }

    public int getIdContenedor() {
        return idContenedor;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public String getUbicacionString() {
        return "Contenedor: " + idContenedor + ", Repisa (Y): " + y + ", Posición (X): " + x;
    }

    @Override
    public String toString() {
        return "Disco #" + id + ": \"" + nombre + "\" de " + artista + " [" + getUbicacionString() + "]";
    }
}