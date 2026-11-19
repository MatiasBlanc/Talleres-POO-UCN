package taller02.domain;

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
        String lit = (litros == (int) litros) ? String.valueOf((int) litros) : String.valueOf(litros);
        return "Terremoto;" + getConsumidor() + ";" + lit + ";" + getFecha();
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
