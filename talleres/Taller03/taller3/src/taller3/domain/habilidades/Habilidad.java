package taller3.domain.habilidades;

public enum Habilidad {
    MORDER,
    VOLAR,
    REGENERARSE,
    ENFURECERSE,
    NINGUNA;

    public String getEtiqueta() {
        switch (this) {
            case MORDER:
                return "Morder";
            case VOLAR:
                return "Volar";
            case REGENERARSE:
                return "Regenerarse";
            case ENFURECERSE:
                return "Enfurecerse";
            case NINGUNA:
            default:
                return "Ninguna";
        }
    }
}
