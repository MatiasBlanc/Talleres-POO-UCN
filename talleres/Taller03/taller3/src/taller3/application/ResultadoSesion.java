package taller3.application;

public class ResultadoSesion {
    private final int puntajeTotal;
    private final boolean jugo;
    private final boolean nuevo;
    private final boolean superoRecord;
    private final int recordAnterior;

    public ResultadoSesion(int puntajeTotal, boolean jugo, boolean nuevo, boolean superoRecord, int recordAnterior) {
        this.puntajeTotal = puntajeTotal;
        this.jugo = jugo;
        this.nuevo = nuevo;
        this.superoRecord = superoRecord;
        this.recordAnterior = recordAnterior;
    }

    public int getPuntajeTotal() {
        return puntajeTotal;
    }

    public boolean isJugo() {
        return jugo;
    }

    public boolean isNuevo() {
        return nuevo;
    }

    public boolean isSuperoRecord() {
        return superoRecord;
    }

    public int getRecordAnterior() {
        return recordAnterior;
    }
}
