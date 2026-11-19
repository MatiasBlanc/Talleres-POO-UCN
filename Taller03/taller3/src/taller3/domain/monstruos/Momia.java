package taller3.domain.monstruos;

import taller3.domain.habilidades.Furioso;
import taller3.domain.habilidades.Regenerable;
import taller3.domain.minijuegos.VisitorMinijuego;

public class Momia extends Monstruo implements Regenerable, Furioso {
    private final int longitudVendajes;

    public Momia(int vida, int velocidad, int fuerza, int resistencia, int longitudVendajes) {
        super(vida, velocidad, fuerza, resistencia);
        this.longitudVendajes = longitudVendajes;
    }

    public int getLongitudVendajes() {
        return longitudVendajes;
    }

    @Override
    public String getTipo() {
        return "Momia";
    }

    @Override
    public String describirAtributosPropios() {
        return "Vendajes " + longitudVendajes;
    }

    @Override
    public double aceptar(VisitorMinijuego visitor) {
        return visitor.visitar(this);
    }

    @Override
    public String regenerarse() {
        return "La momia recompone sus vendajes y se regenera.";
    }

    @Override
    public String enfurecerse() {
        return "La momia aprieta los vendajes y se enfurece.";
    }
}
