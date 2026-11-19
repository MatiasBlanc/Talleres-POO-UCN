package taller3.domain.minijuegos;

import taller3.domain.habilidades.Habilidad;
import taller3.domain.monstruos.HombreLobo;
import taller3.domain.monstruos.Momia;
import taller3.domain.monstruos.Vampiro;
import taller3.domain.monstruos.Zombi;

public class LanzamientoLapidas extends MinijuegoAbstracto {
    public LanzamientoLapidas() {
        super(
                "Lanzamiento de Lápidas",
                "lanza una lápida lo más lejos posible.",
                Habilidad.ENFURECERSE);
    }

    @Override
    public double visitar(Zombi zombi) {
        return zombi.getFuerza() * (double) zombi.getResistencia() / 100.0;
    }

    @Override
    public double visitar(Vampiro vampiro) {
        return vampiro.getFuerza() + vampiro.getVelocidadVuelo() / 4.0;
    }

    @Override
    public double visitar(Momia momia) {
        return momia.getFuerza() + momia.getLongitudVendajes() / 10.0;
    }

    @Override
    public double visitar(HombreLobo hombreLobo) {
        return hombreLobo.getFuerza()
                + hombreLobo.getFerocidad() * (double) hombreLobo.getFaseLunar() / 8.0;
    }
}
