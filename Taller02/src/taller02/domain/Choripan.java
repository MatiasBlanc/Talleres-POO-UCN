package taller02.domain;

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
        String l = (largo == (int) largo) ? String.valueOf((int) largo) : String.valueOf(largo);
        String a = (ancho == (int) ancho) ? String.valueOf((int) ancho) : String.valueOf(ancho);
        return "Choripan;" + getConsumidor() + ";" + l + ";" + a + ";" + getFecha();
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
