package ec.edu.espol.yugioh2;

import android.content.Context;

import java.util.ArrayList;

public class Juego {
    private Maquina maquina;
    private Jugador jugador;
    private Context context;

    public Juego (Context context){
        this.context = context;
    }
    public Juego(Jugador jugador, Context context){
        maquina= new Maquina(context);
        this.jugador= jugador;
        this.context = context;
    }
    public static void batallaDirecta(CartaMonstruo monstruoAtacante, Jugador oponente){
        int puntos = oponente.getPuntos() - monstruoAtacante.getAtaque();
        oponente.setPuntos(puntos);
    }

    public static String declararBatalla(CartaMonstruo cartaOponente, CartaMonstruo cartaAtacante, Jugador oponente, Jugador atacante) {
        // Ambas cartas en modo ataque
        if (cartaOponente.eModoAtaque() && cartaAtacante.eModoAtaque()) {
            if (cartaOponente.getAtaque() < cartaAtacante.getAtaque()) {
                int diferencia = cartaOponente.getAtaque() - cartaAtacante.getAtaque();
                int puntos = oponente.getPuntos() - Math.abs(diferencia);
                oponente.setPuntos(puntos);
                cartaOponente.destruida();
                oponente.getTablero().removerCarta(cartaOponente);
                return "Carta: \n"+ cartaOponente + "\ndestruida, puntos de oponente "+oponente.getPuntos()+" .\n";
            } else if (cartaAtacante.getAtaque() == cartaOponente.getAtaque()) {
                oponente.getTablero().removerCarta(cartaOponente);
                atacante.getTablero().removerCarta(cartaAtacante);
                cartaOponente.destruida();
                cartaAtacante.destruida();
                return "Sus cartas fueron iguales.\n";
            } else 
                return "Carta: \n" + cartaAtacante + "\n no pudo atacar\n" + cartaOponente + "\n";
        }
        else if (cartaAtacante.eModoAtaque() && cartaOponente.eModoDefensa()) {
            if (cartaAtacante.getAtaque() > cartaOponente.getDefensa()) {
                oponente.getTablero().removerCarta(cartaOponente);
                cartaOponente.destruida();
                return "Carta oponente destruida.\n";
            } else if (cartaAtacante.getAtaque() < cartaOponente.getDefensa()) {
                int diferencia = cartaAtacante.getAtaque() - cartaOponente.getDefensa();
                int puntos = atacante.getPuntos() - Math.abs(diferencia);
                atacante.setPuntos(puntos);
                cartaOponente.modoAtaque();
                cartaOponente.setPosicion(Posicion.HORIZONTAL);
                return "Ataque fallido, puntos del atacante actualizados.\n";
            } else {
                return "Carta " + cartaAtacante + " no pudo atacar " + cartaOponente + "\n";
            }
        }
    
        // Tablero actualizado
        return "Tablero de " + atacante.getNombre() + ": " + atacante.getTablero().toString() + "\n" +
               "Tablero de " + oponente.getNombre() + ": " + oponente.getTablero().toString() + "\n";
    }

    public static String usarTrampas(Jugador j, CartaMonstruo monsM,ArrayList<CartaTrampa> trampas){
        StringBuilder resultado = new StringBuilder();
        for (Carta especial: j.getTablero().getEspeciales()){
            if ((especial instanceof CartaTrampa) && (((CartaTrampa) especial).usar(monsM))) {
                trampas.add((CartaTrampa) especial);
                j.getTablero().getEspeciales().remove(especial);
                resultado.append("Se usó una carta trampa: ").append(especial.toString()).append("\n");
            }
            else
                resultado.append("");
        }
        return resultado.toString();
    }
    
}
