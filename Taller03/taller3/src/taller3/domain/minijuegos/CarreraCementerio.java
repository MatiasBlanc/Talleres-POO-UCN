package taller3.domain.minijuegos;

import taller3.domain.habilidades.Habilidad;
import taller3.domain.monstruos.HombreLobo;
import taller3.domain.monstruos.Momia;
import taller3.domain.monstruos.Vampiro;
import taller3.domain.monstruos.Zombi;

public class CarreraCementerio extends MinijuegoAbstracto {
    public CarreraCementerio() {
        super(
                "Carrera del Cementerio",
                "recorre el cementerio de punta a punta antes de que el sepulturero te vea.",
                Habilidad.VOLAR);
    }

    @Override
    public double visitar(Zombi zombi) {
        return zombi.getVelocidad() * (double) zombi.getResistencia() / 100.0;
    }

    @Override
    public double visitar(Vampiro vampiro) {
        return (vampiro.getVelocidad() + 2.0 * vampiro.getVelocidadVuelo()) / 2.0;
    }

    @Override
    public double visitar(Momia momia) {
        return momia.getVelocidad() * 100.0 / (100.0 + momia.getLongitudVendajes());
    }

    @Override
    public double visitar(HombreLobo hombreLobo) {
        return (hombreLobo.getVelocidad() + hombreLobo.getFerocidad() / 5.0)
                * hombreLobo.getFaseLunar() / 4.0;
    }
}
