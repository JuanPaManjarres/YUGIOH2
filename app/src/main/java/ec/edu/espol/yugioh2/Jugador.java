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
    public void setMano(ArrayList<Carta> mano){this.mano= mano;}
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

    public void agregarCartaTablero(Carta carta, int fila, int columna) {
        // Si la carta es de tipo monstruo
        if (carta instanceof CartaMonstruo) {
            // Verificar si hay espacio para cartas de monstruo
            if (tablero.getCartasMons().size() < 3) {
                // Si la fila es 1, la carta va en modo ataque, si no en defensa
                if (fila == 1) {
                    ((CartaMonstruo) carta).modoAtaque();  // Modo ataque
                } else {
                    ((CartaMonstruo) carta).modoDefensa();  // Modo defensa
                }

                // Coloca la carta de monstruo en el tablero en la posición indicada
                tablero.getCartasMons().add((CartaMonstruo) carta);

                // Elimina la carta de la mano
                mano.remove(carta);

            }

        } else {
            // Si la carta es de tipo especial (mágica o trampa)
            if (tablero.getEspeciales().size() < 3) {
                // Coloca la carta especial en el tablero en la posición indicada
                tablero.getEspeciales().add(carta);

                // Elimina la carta de la mano
                mano.remove(carta);

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
