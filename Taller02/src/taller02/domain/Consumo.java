package taller02.domain;

/**
 * Clase abstracta base para los consumos realizados en la fonda.
 * Define la estructura comun y los metodos polimorficos para calculos y persistencia.
 */
public abstract class Consumo {
    private String consumidor;
    private String fecha;

    public Consumo(String consumidor, String fecha) {
        this.consumidor = consumidor;
        this.fecha = fecha;
    }

    /**
     * Calcula las kilocalorias correspondientes al consumo de acuerdo a la formula del tipo.
     * @return kilocalorias calculadas
     */
    public abstract double calcularKcal();

    /**
     * Serializa los datos del consumo en el formato esperado por Consumido.txt.
     * @return linea en formato CSV delimitada por ';'
     */
    public abstract String aFormatoArchivo();

    /**
     * Retorna el tipo de consumo ("Choripan" o "Terremoto").
     * @return nombre del tipo de consumo
     */
    public abstract String getTipo();

    public String getConsumidor() {
        return consumidor;
    }

    public void setConsumidor(String consumidor) {
        this.consumidor = consumidor;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
