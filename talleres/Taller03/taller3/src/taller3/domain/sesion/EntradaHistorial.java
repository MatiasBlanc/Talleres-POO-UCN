package taller3.domain.sesion;

public class EntradaHistorial {
    private final String minijuego;
    private final String monstruo;
    private final String habilidad;
    private final int puntaje;

    public EntradaHistorial(String minijuego, String monstruo, String habilidad, int puntaje) {
        this.minijuego = minijuego;
        this.monstruo = monstruo;
        this.habilidad = habilidad;
        this.puntaje = puntaje;
    }

    public String getMinijuego() {
        return minijuego;
    }

    public String getMonstruo() {
        return monstruo;
    }

    public String getHabilidad() {
        return habilidad;
    }

    public int getPuntaje() {
        return puntaje;
    }
}
