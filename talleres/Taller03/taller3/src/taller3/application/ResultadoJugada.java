package taller3.application;

public class ResultadoJugada {
    private final String mensajeHabilidad;
    private final boolean efectiva;
    private final int puntaje;

    public ResultadoJugada(String mensajeHabilidad, boolean efectiva, int puntaje) {
        this.mensajeHabilidad = mensajeHabilidad;
        this.efectiva = efectiva;
        this.puntaje = puntaje;
    }

    public String getMensajeHabilidad() {
        return mensajeHabilidad;
    }

    public boolean isEfectiva() {
        return efectiva;
    }

    public int getPuntaje() {
        return puntaje;
    }
}
