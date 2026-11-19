package taller3.domain.monstruos;

import taller3.domain.habilidades.Mordedor;
import taller3.domain.habilidades.Volador;
import taller3.domain.minijuegos.VisitorMinijuego;

public class Vampiro extends Monstruo implements Mordedor, Volador {
    private final int velocidadVuelo;
    private final int resistenciaSol;

    public Vampiro(int vida, int velocidad, int fuerza, int resistencia, int velocidadVuelo, int resistenciaSol) {
        super(vida, velocidad, fuerza, resistencia);
        this.velocidadVuelo = velocidadVuelo;
        this.resistenciaSol = resistenciaSol;
    }

    public int getVelocidadVuelo() {
        return velocidadVuelo;
    }

    public int getResistenciaSol() {
        return resistenciaSol;
    }

    @Override
    public String getTipo() {
        return "Vampiro";
    }

    @Override
    public String describirAtributosPropios() {
        return "Vuelo " + velocidadVuelo + " | Sol " + resistenciaSol;
    }

    @Override
    public double aceptar(VisitorMinijuego visitor) {
        return visitor.visitar(this);
    }

    @Override
    public String morder() {
        return "El vampiro muestra sus colmillos y muerde.";
    }

    @Override
    public String volar() {
        return "El vampiro se eleva y sale volando.";
    }
}
