package ec.edu.espol.yugioh2;

import java.util.ArrayList;

public class Tablero {
    private ArrayList<CartaMonstruo> cartasMons;
    private ArrayList<Carta> especiales;
    private ArrayList<Carta>[] cartasJugador;

    public Tablero(){
        cartasMons = new ArrayList<>();
        especiales = new ArrayList<>();
        cartasJugador = new ArrayList[2];

        for (Carta c: this.cartasMons){
            cartasJugador[0].add(c);
        }
        for (Carta c: this.especiales){
            cartasJugador[1].add(c);
        }
    }

    //Getters
    public ArrayList<CartaMonstruo> getCartasMons(){
        return cartasMons;
    }
    public ArrayList<Carta> getEspeciales(){
        return especiales;
    }
    public ArrayList<Carta>[] getCartasJugador(){
        return cartasJugador;
    }

    //remover carta

    public void removerCarta(Carta c) {
        if (c instanceof CartaMonstruo) {
            cartasMons.remove(c);
        }
        if ((c instanceof CartaMagica) || (c instanceof CartaTrampa)) {
            especiales.remove(c);
        }
    }

    public String toString() {
        ArrayList<String> monstruos = new ArrayList<>();
        ArrayList<String> especialesC = new ArrayList<>();
    
        for (int i = 0; i < 3; i++) {
            if (i < cartasMons.size()) {
                monstruos.add(cartasMons.get(i).toString());
            } else {
                monstruos.add("No hay cartas");
            }
        }
    
        // Añadir cartas especiales
        for (int i = 0; i < 3; i++) {
            if (i < especialesC.size()) {
                especialesC.add(this.especiales.get(i).toString()); 
            } else {
                especialesC.add("No hay cartas");
            }
        }
    
        // Construir el tablero como cadena
        return "\nTablero\n"
                + "----------------------------------------------------------------------\n"
                + "Monstruo: [" + monstruos.get(0) + "] [" + monstruos.get(1) + "] [" + monstruos.get(2) + "]\n"
                + "----------------------------------------------------------------------\n"
                + "Especiales: [" + especialesC.get(0) + "] [" + especialesC.get(1) + "] [" + especialesC.get(2) + "]\n"
                + "----------------------------------------------------------------------\n";
    }
}
