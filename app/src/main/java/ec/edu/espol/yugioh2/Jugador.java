package ec.edu.espol.yugioh2;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;

public class Jugador {
    private final String nombre;
    private Deck deck;
    private int puntos;
    private Tablero tablero;
    private ArrayList<Carta> mano;

    public Jugador(String nombre, Context context) {
        this.nombre = nombre;
        try{
            deck = Deck.crearDeck(context.getAssets());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        puntos = 4000;
        tablero = new Tablero();
        mano = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            if (!deck.getCartas().isEmpty()) {
                Carta carta = deck.getCartas().get(0);
                mano.add(carta);
                deck.getCartas().remove(carta);
            }
        }
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }
    public int getPuntos() {
        return puntos;
    }
    public void setPuntos(int puntos) {
        if (puntos>0)
            this.puntos = puntos;
        else
            this.puntos = 0;
    }
    public ArrayList<Carta> getMano() {
        return mano;
    }
    public Tablero getTablero() {
        return tablero;
    }
    public Deck getDeck() {
        return deck;
    }

    // Métodos jugables
    public String tomarCarta() {
        if (!deck.getCartas().isEmpty()){
            Carta carta = deck.getCartas().get(0);
            mano.add(carta);
            return nombre + " toma la carta " + carta.getNombre();
        }
        else{
            return "No hay cartas en el deck";
        }
    }

    public String manoImprimir() {
        StringBuilder mostrar = new StringBuilder("Usted tiene en su mano:\n");
        ArrayList<Carta> cartas = this.getMano();
        
        for (Carta carta : cartas) {
            mostrar.append(carta.toString()).append("\n");
        }
        
        return mostrar.toString();
    }

    public Carta seleccionarCartaTablero(int indice) {
        return tablero.getCartasJugador()[0].get(indice);
    }

    public Carta seleccionarCartaMano(int indice) {
        return mano.get(indice);
    }

    public String agregarCartaTablero(int indice, String pos) {
        Carta carta = mano.get(indice);
        if (carta instanceof CartaMonstruo) {
            if (tablero.getCartasMons().size() < 3) {
                if (pos.equals("1")) {
                    ((CartaMonstruo) carta).modoAtaque();
                } else {
                    ((CartaMonstruo) carta).modoDefensa();
                }
                CartaMonstruo c1 = (CartaMonstruo) carta;
                tablero.getCartasMons().add(c1);
                mano.remove(indice);
                return "Se ha agregado la carta monstruo al tablero \n" + carta;
            } else {
                return "Espacio para carta tipo Monstruo lleno en el tablero";
            }
        } else {
            if (tablero.getEspeciales().size() < 3) {
                tablero.getEspeciales().add(carta);
                mano.remove(indice);
                return "Se ha agregado la carta especial al tablero \n" + carta;
            } else {
                return "Espacio para cartas tipo Magica o Trampa lleno en el tablero";
            }
        }
    }

    @Override
    public String toString() {
        String[] monstruos = new String[3];
        String[] especiales = new String[3];

        for (int i = 0; i < 3; i++) {
            if (i < tablero.getCartasMons().size()) {
                monstruos[i] = tablero.getCartasMons().get(i).toString();
            } else {
                monstruos[i] = "No hay cartas";
            }
        }

        for (int i = 0; i < 3; i++) {
            if (i < tablero.getEspeciales().size()) {
                especiales[i] = tablero.getEspeciales().get(i).toString();
            } else {
                especiales[i] = "No hay cartas";
            }
        }

        return " ----------------------------------------------------------------------"
                + " Monstruo: [" + monstruos[0] + "] [" + monstruos[1] + "] [" + monstruos[2] + "]\n"
                + "----------------------------------------------------------------------\n"
                + "Especiales: [" + especiales[0] + "] [" + especiales[1] + "] [" + especiales[2] + "]\n"
                + "----------------------------------------------------------------------\n"
                + nombre + " - Lp:" + puntos + "\n"
                + "----------------------------------------------------------------------";
    }
}
