package taller3.domain.minijuegos;

import taller3.domain.habilidades.Habilidad;
import taller3.domain.monstruos.Monstruo;

public interface Minijuego extends VisitorMinijuego {
    String getNombre();
    String getDescripcion();
    Habilidad getHabilidadPremiada();
    double calcularPotencial(Monstruo monstruo);
}
