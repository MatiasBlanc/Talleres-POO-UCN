package taller3.application;

public class ResultadoJugada {
    private final boolean exito;
    private final String mensajeError;
    private final String mensajeHabilidad;
    private final boolean usoHabilidad;
    private final boolean efectiva;
    private final int puntaje;

    private ResultadoJugada(
            boolean exito,
            String mensajeError,
            String mensajeHabilidad,
            boolean usoHabilidad,
            boolean efectiva,
            int puntaje) {
        this.exito = exito;
        this.mensajeError = mensajeError;
        this.mensajeHabilidad = mensajeHabilidad;
        this.usoHabilidad = usoHabilidad;
        this.efectiva = efectiva;
        this.puntaje = puntaje;
    }

    public static ResultadoJugada error(String mensajeError) {
        return new ResultadoJugada(false, mensajeError, "", false, false, 0);
    }

    public static ResultadoJugada ok(String mensajeHabilidad, boolean usoHabilidad, boolean efectiva, int puntaje) {
        return new ResultadoJugada(true, null, mensajeHabilidad, usoHabilidad, efectiva, puntaje);
    }

    public boolean isExito() {
        return exito;
    }

    public String getMensajeError() {
        return mensajeError;
    }

    public String getMensajeHabilidad() {
        return mensajeHabilidad;
    }

    public boolean isUsoHabilidad() {
        return usoHabilidad;
    }

    public boolean isEfectiva() {
        return efectiva;
    }

    public int getPuntaje() {
        return puntaje;
    }
}
