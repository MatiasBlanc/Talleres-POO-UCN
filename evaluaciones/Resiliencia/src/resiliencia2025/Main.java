package resiliencia2025;

public class Main {
    public static void main(String[] args) {
        Inventario inventario = new Inventario();

        inventario.cargarDiscos("discos.txt");
        inventario.cargarTracks("tracks.txt");

        inventario.mostrarPosicionesLibresYPorcentajeUso();
        inventario.mostrarDiscosPorArtista();
        inventario.proporcionArtistaEnContenedores();
        inventario.buscarTrack("Peces");
        inventario.intercambiar("intercambio.txt");
        inventario.buscarTrack("Peces");
        inventario.proporcionArtistaEnContenedores();
    }
}
