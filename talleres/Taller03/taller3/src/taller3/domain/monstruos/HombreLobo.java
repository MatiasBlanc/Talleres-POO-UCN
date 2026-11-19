package taller3.domain.monstruos;

import taller3.domain.habilidades.Furioso;
import taller3.domain.habilidades.Mordedor;
import taller3.domain.minijuegos.VisitorMinijuego;

public class HombreLobo extends Monstruo implements Mordedor, Furioso {
    private final int ferocidad;
    private final int faseLunar;

    public HombreLobo(int vida, int velocidad, int fuerza, int resistencia, int ferocidad, int faseLunar) {
        super(vida, velocidad, fuerza, resistencia);
        this.ferocidad = ferocidad;
        this.faseLunar = faseLunar;
    }

    public int getFerocidad() {
        return ferocidad;
    }

    public int getFaseLunar() {
        return faseLunar;
    }

    @Override
    public String getTipo() {
        return "HombreLobo";
    }

    @Override
    public String describirAtributosPropios() {
        return "Ferocidad " + ferocidad + " | Luna " + faseLunar;
    }

    @Override
    public double aceptar(VisitorMinijuego visitor) {
        return visitor.visitar(this);
    }

    @Override
    public String morder() {
        return "El hombre lobo enseña los colmillos y muerde.";
    }

    @Override
    public String enfurecerse() {
        return "El hombre lobo aúlla y se enfurece.";
    }
}
