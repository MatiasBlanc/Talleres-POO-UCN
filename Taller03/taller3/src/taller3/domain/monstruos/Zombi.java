package taller3.domain.monstruos;

import taller3.domain.habilidades.Mordedor;
import taller3.domain.habilidades.Regenerable;
import taller3.domain.minijuegos.VisitorMinijuego;

public class Zombi extends Monstruo implements Mordedor, Regenerable {
    private final int fuerzaMordida;

    public Zombi(int vida, int velocidad, int fuerza, int resistencia, int fuerzaMordida) {
        super(vida, velocidad, fuerza, resistencia);
        this.fuerzaMordida = fuerzaMordida;
    }

    public int getFuerzaMordida() {
        return fuerzaMordida;
    }

    @Override
    public String getTipo() {
        return "Zombi";
    }

    @Override
    public String describirAtributosPropios() {
        return "Mordida " + fuerzaMordida;
    }

    @Override
    public double aceptar(VisitorMinijuego visitor) {
        return visitor.visitar(this);
    }

    @Override
    public String morder() {
        return "El zombi se acerca y te muerde.";
    }

    @Override
    public String regenerarse() {
        return "El zombi recompone la carne suelta y se regenera.";
    }
}
