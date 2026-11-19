package taller3.application;

public class ResultadoSesion {
    private final int puntajeTotal;
    private final boolean jugoAlMenosUno;
    private final boolean jugadorNuevo;
    private final boolean superoRecord;
    private final int recordAnterior;
    private final boolean debeGuardar;

    public ResultadoSesion(
            int puntajeTotal,
            boolean jugoAlMenosUno,
            boolean jugadorNuevo,
            boolean superoRecord,
            int recordAnterior,
            boolean debeGuardar) {
        this.puntajeTotal = puntajeTotal;
        this.jugoAlMenosUno = jugoAlMenosUno;
        this.jugadorNuevo = jugadorNuevo;
        this.superoRecord = superoRecord;
        this.recordAnterior = recordAnterior;
        this.debeGuardar = debeGuardar;
    }

    public int getPuntajeTotal() {
        return puntajeTotal;
    }

    public boolean isJugoAlMenosUno() {
        return jugoAlMenosUno;
    }

    public boolean isJugadorNuevo() {
        return jugadorNuevo;
    }

    public boolean isSuperoRecord() {
        return superoRecord;
    }

    public int getRecordAnterior() {
        return recordAnterior;
    }

    public boolean isDebeGuardar() {
        return debeGuardar;
    }
}
