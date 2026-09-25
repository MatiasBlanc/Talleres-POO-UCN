package taller02.domain;

/**
 * Representa a un participante de la fonda.
 */
public class Participante {
    private String nombre;

    public Participante(String nombre) {
        this.nombre = nombre;
    }

    public String getName() {
        return nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
