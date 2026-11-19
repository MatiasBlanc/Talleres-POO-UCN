package taller3.domain.minijuegos;

import taller3.domain.habilidades.Habilidad;
import taller3.domain.monstruos.Monstruo;

public abstract class MinijuegoAbstracto implements Minijuego {
    private final String nombre;
    private final String descripcion;
    private final Habilidad habilidadPremiada;

    protected MinijuegoAbstracto(String nombre, String descripcion, Habilidad habilidadPremiada) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.habilidadPremiada = habilidadPremiada;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public Habilidad getHabilidadPremiada() {
        return habilidadPremiada;
    }

    @Override
    public double calcularPotencial(Monstruo monstruo) {
        if (monstruo == null) {
            return 0;
        }
        return monstruo.aceptar(this);
    }
}
