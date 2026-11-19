package taller3.domain.minijuegos;

import taller3.domain.monstruos.HombreLobo;
import taller3.domain.monstruos.Momia;
import taller3.domain.monstruos.Vampiro;
import taller3.domain.monstruos.Zombi;

public interface VisitorMinijuego {
    double visitar(Zombi zombi);
    double visitar(Vampiro vampiro);
    double visitar(Momia momia);
    double visitar(HombreLobo hombreLobo);
}
