package taller3.domain.minijuegos;

import taller3.domain.habilidades.Habilidad;
import taller3.domain.monstruos.HombreLobo;
import taller3.domain.monstruos.Momia;
import taller3.domain.monstruos.Vampiro;
import taller3.domain.monstruos.Zombi;

public class CazaCalabazas extends MinijuegoAbstracto {
    public CazaCalabazas() {
        super(
                "Caza de Calabazas",
                "muerde la mayor cantidad de calabazas del huerto en 60 segundos.",
                Habilidad.MORDER);
    }

    @Override
    public double visitar(Zombi zombi) {
        return zombi.getFuerzaMordida() * (double) zombi.getVida() / 100.0
                + zombi.getResistencia() / 2.0;
    }

    @Override
    public double visitar(Vampiro vampiro) {
        return (vampiro.getFuerza() + vampiro.getVelocidad()) / 3.0;
    }

    @Override
    public double visitar(Momia momia) {
        return momia.getLongitudVendajes() * (double) momia.getFuerza() / 200.0;
    }

    @Override
    public double visitar(HombreLobo hombreLobo) {
        return hombreLobo.getFerocidad() * (double) hombreLobo.getFaseLunar() / 4.0
                + hombreLobo.getVelocidad() / 4.0;
    }
}
