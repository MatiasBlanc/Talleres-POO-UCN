package taller02.domain;

/**
 * Representa el consumo de un terremoto en la fonda.
 * Aplica la formula de kilocalorias: (Litros * 10) / 3.
 */
public class Terremoto extends Consumo {
    private double litros;

    public Terremoto(String consumidor, String fecha, double litros) {
        super(consumidor, fecha);
        this.litros = litros;
    }

    @Override
    public double calcularKcal() {
        return (litros * 10.0) / 3.0;
    }

    @Override
    public String aFormatoArchivo() {
        // Formato para Consumido.txt: Terremoto;Consumidor;Litros;Fecha
        String litrosStr = (litros == (long) litros) ? String.valueOf((long) litros) : String.valueOf(litros);
        return "Terremoto;" + getConsumidor() + ";" + litrosStr + ";" + getFecha();
    }

    @Override
    public String getTipo() {
        return "Terremoto";
    }

    public double getLitros() {
        return litros;
    }

    public void setLitros(double litros) {
        this.litros = litros;
    }
}
