package taller02.domain;

/**
 * Representa el consumo de un choripan en la fonda.
 * Aplica la formula de kilocalorias: (Largo * Ancho) / 3.
 */
public class Choripan extends Consumo {
    private double largo;
    private double ancho;

    public Choripan(String consumidor, String fecha, double largo, double ancho) {
        super(consumidor, fecha);
        this.largo = largo;
        this.ancho = ancho;
    }

    @Override
    public double calcularKcal() {
        return (largo * ancho) / 3.0;
    }

    @Override
    public String aFormatoArchivo() {
        // Formato para Consumido.txt: Choripan;Consumidor;Largo;Ancho;Fecha
        String largoStr = (largo == (long) largo) ? String.valueOf((long) largo) : String.valueOf(largo);
        String anchoStr = (ancho == (long) ancho) ? String.valueOf((long) ancho) : String.valueOf(ancho);
        return "Choripan;" + getConsumidor() + ";" + largoStr + ";" + anchoStr + ";" + getFecha();
    }

    @Override
    public String getTipo() {
        return "Choripan";
    }

    public double getLargo() {
        return largo;
    }

    public void setLargo(double largo) {
        this.largo = largo;
    }

    public double getAncho() {
        return ancho;
    }

    public void setAncho(double ancho) {
        this.ancho = ancho;
    }
}
