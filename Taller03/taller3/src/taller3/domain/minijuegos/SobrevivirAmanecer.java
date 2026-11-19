package taller3.domain.minijuegos;

import taller3.domain.habilidades.Habilidad;
import taller3.domain.monstruos.HombreLobo;
import taller3.domain.monstruos.Momia;
import taller3.domain.monstruos.Vampiro;
import taller3.domain.monstruos.Zombi;

public class SobrevivirAmanecer extends MinijuegoAbstracto {
    public SobrevivirAmanecer() {
        super(
                "Sobrevivir al Amanecer",
                "aguanta en pie cuando salgan los primeros rayos de sol.",
                Habilidad.REGENERARSE);
    }

    @Override
    public double visitar(Zombi zombi) {
        return zombi.getResistencia() * (double) zombi.getVida() / 100.0;
    }

    @Override
    public double visitar(Vampiro vampiro) {
        return vampiro.getResistencia() * (double) vampiro.getResistenciaSol() / 100.0;
    }

    @Override
    public double visitar(Momia momia) {
        return (momia.getResistencia() + momia.getVida()) / 2.0
                * (1.0 + momia.getLongitudVendajes() / 300.0);
    }

    @Override
    public double visitar(HombreLobo hombreLobo) {
        return (hombreLobo.getResistencia() + hombreLobo.getVida()) / 2.0
                * (5.0 - hombreLobo.getFaseLunar()) / 4.0;
    }
}
