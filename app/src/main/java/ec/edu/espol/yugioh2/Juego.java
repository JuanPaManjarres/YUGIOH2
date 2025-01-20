package ec.edu.espol.yugioh2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Juego {
    private Maquina maquina;
    private Jugador jugador;
    private Context context;
    private int turno=1;

    private String fase;

    public Juego (Context context){
        this.context = context;
    }
    public Juego(Jugador jugador,Context context){
        maquina= new Maquina(context);
        this.jugador= jugador;
        this.context = context;
    }
    public String getJugador(){return jugador.getNombre();}
    public String getMaquina(){return maquina.getNombre();}


    public void setFase(String nuevaFase, LinearLayout manoJ, LinearLayout manoM,
                        LinearLayout monstruosJ, LinearLayout monstruosM,
                        LinearLayout especialesJ, LinearLayout especialesM,TextView turnosView,TextView vidaJView,TextView vidaMView) {
        this.fase = nuevaFase;
        // Llama automáticamente a la función prueba cuando se actualiza la fase
        prueba(manoJ, manoM, monstruosJ, monstruosM, especialesJ, especialesM,turnosView,vidaJView,vidaMView);
    }
    public static void batallaDirecta(CartaMonstruo monstruoAtacante, Jugador oponente,TextView vidaJugador){
        int puntos = oponente.getPuntos() - monstruoAtacante.getAtaque();
        oponente.setPuntos(puntos);
        vidaJugador.setText("LP de "+oponente.getNombre()+": "+oponente.getPuntos());
    }


    public static String declararBatalla(CartaMonstruo cartaOponente, CartaMonstruo cartaAtacante, Jugador oponente, Jugador atacante, Context context,LinearLayout monstruoO,LinearLayout monstruoA,TextView vidaJugador,TextView vidaMaquina) {
        // Ambas cartas en modo ataque
        if (cartaOponente.eModoAtaque() && cartaAtacante.eModoAtaque()) {
            if (cartaOponente.getAtaque() < cartaAtacante.getAtaque()) {
                int diferencia = cartaOponente.getAtaque() - cartaAtacante.getAtaque();
                int puntos = oponente.getPuntos() - Math.abs(diferencia);
                oponente.setPuntos(puntos);
                vidaMaquina.setText("LP de "+oponente.getNombre()+": "+oponente.getPuntos());
                cartaOponente.destruida();
                oponente.getTablero().removerCarta(cartaOponente);
                ArrayList<Carta> orgo= new ArrayList<>();
                for(CartaMonstruo c : oponente.getTablero().getCartasMons())
                    orgo.add(c);
                Utilitaria.organizarTablero(context,orgo,monstruoO);
                Utilitaria.crearDialogs(context,"Ataque",atacante.getNombre()+ " uso "+cartaAtacante.getNombre()+" y ataco a "+cartaOponente.getNombre(),"Se realizo el ataque");
                return "Carta de : "+oponente.getNombre()+" \n"+ cartaOponente.getNombre() + "destruida. \nPuntos de oponente actualizados. ";
            } else if (cartaAtacante.getAtaque() == cartaOponente.getAtaque()) {
                oponente.getTablero().removerCarta(cartaOponente);
                atacante.getTablero().removerCarta(cartaAtacante);
                cartaOponente.destruida();
                cartaAtacante.destruida();
                ArrayList<Carta> orga= new ArrayList<>();
                for(CartaMonstruo c : atacante.getTablero().getCartasMons())
                    orga.add(c);
                Utilitaria.organizarTablero(context,orga,monstruoA);
                ArrayList<Carta> orgo= new ArrayList<>();
                for(CartaMonstruo c : oponente.getTablero().getCartasMons())
                    orgo.add(c);
                Utilitaria.organizarTablero(context,orgo,monstruoO);
                return "Sus cartas fueron iguales y se destruyeron.\n";
            } else 
                return "Carta de: "+atacante.getNombre()+"\n" + cartaAtacante.getNombre() + "\n no pudo atacar a\n" + cartaOponente.getNombre() + "\n porque es de menor ataque";
        }
        else if (cartaAtacante.eModoAtaque() && cartaOponente.eModoDefensa()) {
            if (cartaAtacante.getAtaque() > cartaOponente.getDefensa()) {
                oponente.getTablero().removerCarta(cartaOponente);
                cartaOponente.destruida();
                ArrayList<Carta> orgo= new ArrayList<>();
                for(CartaMonstruo c : oponente.getTablero().getCartasMons())
                    orgo.add(c);
                Utilitaria.organizarTablero(context,orgo,monstruoO);
                return "Carta oponente destruida.\n";
            } else if (cartaAtacante.getAtaque() < cartaOponente.getDefensa()) {
                int diferencia = cartaAtacante.getAtaque() - cartaOponente.getDefensa();
                int puntos = atacante.getPuntos() - Math.abs(diferencia);
                atacante.setPuntos(puntos);
                vidaJugador.setText("LP de "+atacante.getNombre()+": "+atacante.getPuntos());
                cartaOponente.modoAtaque();
                cartaOponente.setPosicion(Posicion.HORIZONTAL);
                ArrayList<Carta> orgo= new ArrayList<>();
                for(CartaMonstruo c : oponente.getTablero().getCartasMons())
                    orgo.add(c);
                Utilitaria.organizarTablero(context,orgo,monstruoO);
                return "Ataque fallido, puntos del atacante actualizados.\n";
            } else {
                return "Carta " + cartaAtacante.getNombre() + " no pudo atacar " + cartaOponente.getNombre() + "\n Mismo ataque y defensa";
            }
        }
    
        // Tablero actualizado
        return "Tablero de " + atacante.getNombre() + ": " + atacante.getTablero().toString() + "\n" +
               "Tablero de " + oponente.getNombre() + ": " + oponente.getTablero().toString() + "\n";
    }

    public static String usarTrampas(Jugador j, CartaMonstruo monsM,ArrayList<CartaTrampa> trampas, Context context, LinearLayout especialesJ){
        StringBuilder resultado = new StringBuilder();
        ArrayList<Carta> cartasParaEliminar = new ArrayList<>();

        for (Carta especial: j.getTablero().getEspeciales()){
            if ((especial instanceof CartaTrampa) && (((CartaTrampa) especial).usar(monsM))) {
                trampas.add((CartaTrampa) especial);
                cartasParaEliminar.add(especial);
                Utilitaria.noHayCarta(context,especialesJ,especial);
                resultado.append("Se usó una carta trampa: ").append(especial).append("\n");
            }
        }
        if (trampas.isEmpty()) {
            resultado.append("No hay trampas para usar");
        }
        j.getTablero().getEspeciales().removeAll(cartasParaEliminar);
        return resultado.toString();
    }


    public void faseTomarCarta(){
        String msjJ = jugador.tomarCarta();
        String msjM = maquina.tomarCarta();
        String msj = msjJ+ "\n" + msjM;
        Utilitaria.crearDialogs(context,"Cartas tomadas", msj, "Se han tomado las cartas");
        //Buscar la imagen con ese nombre y colocarlo en el LinearLayout de la mano
        //Tiene que agregarse la carta a la mano visualmente
    }

    public void usarMagicasM(LinearLayout especialesM){
        ArrayList<Carta> especiales = maquina.getTablero().getEspeciales();
        ArrayList<CartaMonstruo> monstruos = maquina.getTablero().getCartasMons();
        ArrayList<Carta> usadas = new ArrayList<>();
        String mensajefinal= "";

        for (Carta carta: especiales){
            if (!usadas.contains(carta)){
                for (CartaMonstruo monstruo: monstruos){
                    if (carta instanceof CartaMagica){
                        ((CartaMagica) carta).usar(monstruo);

                    }
                }
                if (carta instanceof CartaMagica)
                    usadas.add(carta);
            }
        }
        for (Carta carta: usadas) {
            Utilitaria.noHayCarta(context, especialesM, carta);
            maquina.getTablero().removerCarta(carta);
            mensajefinal+= carta.toString()+"\n";
        }
        especiales.removeAll(usadas);
        if(usadas.size()>0)
            Utilitaria.crearDialogs(context,"Maquina uso las cartas:",mensajefinal,"Maquina uso todas sus cartas magicas");

    }

    public String mBatalla(Jugador jugador,LinearLayout layoutMaquina,LinearLayout layoutJugador, LinearLayout especialesJ,LinearLayout especialesM,TextView vidaJugador,TextView vidaMaquina) {
        usarMagicasM(especialesM);
        ArrayList<CartaMonstruo> monstruosJ = new ArrayList<>();
        ArrayList<Carta> usadas = new ArrayList<>();
        ArrayList<CartaTrampa> trampas = new ArrayList<>();

        StringBuilder resultado = new StringBuilder();

        for (CartaMonstruo monstruoJ: jugador.getTablero().getCartasMons())
            if (monstruoJ.eModoAtaque())
                monstruosJ.add(monstruoJ);

        for (CartaMonstruo monsM: maquina.getTablero().getCartasMons()){
            if (!usadas.contains(monsM) && (monsM.eModoAtaque())){
                resultado.append(Juego.usarTrampas(jugador, monsM, trampas,context,especialesJ));

                if (trampas.isEmpty()){
                    if (monstruosJ.isEmpty()){
                        Juego.batallaDirecta(monsM,jugador,vidaJugador);
                    }
                    else {
                        for (CartaMonstruo monsJ: monstruosJ){
                            if (monsM.getAtaque() > monsJ.getAtaque()||monsM.getAtaque() > monsJ.getDefensa()||monsM.getDefensa() < monsJ.getAtaque()||monsM.getAtaque() == monsJ.getAtaque()){
                                usadas.add(monsM);
                                resultado.append(Juego.declararBatalla(monsJ,monsM,jugador,maquina,context,layoutJugador,layoutMaquina,vidaMaquina,vidaJugador)).append("\n");
                            }
                        }
                    }
                }
            }
        }
        return resultado.toString();
    }

/*
    public void jugar(LinearLayout manoJ, LinearLayout manoM, LinearLayout monstruosJ, LinearLayout monstruosM, LinearLayout especialesJ,LinearLayout especialesM) {
        if (turno==0){

            for (Carta c : jugador.getMano()) {
                Utilitaria.crearyAgregar(context, c, manoJ);
            }
            for (Carta c : maquina.getMano()) {
                Utilitaria.crearyAgregar(context, c, manoM);
            }
            turno+=1;

        }
        if (fase.equals("Fase Tomar Carta")) {

            Carta ct= jugador.getDeck().getCartas().get(0);
            jugador.getMano().add(ct);
            jugador.getDeck().getCartas().remove(0);
            Toast.makeText(context, "Jugador tomo la carta "+ct.getNombre(), Toast.LENGTH_SHORT).show();
            Utilitaria.crearyAgregar(context,ct,manoJ);

            Carta ctm= maquina.getDeck().getCartas().get(0);
            maquina.getMano().add(ctm);
            maquina.getDeck().getCartas().remove(0);
            Toast.makeText(context, "La maquina tomo la carta "+ctm.getNombre(), Toast.LENGTH_SHORT).show();
            Utilitaria.crearyAgregar(context,ctm,manoM);
            Utilitaria.eliminarClickListenersTablero(monstruosJ, monstruosM, especialesJ, especialesM);

            turno++;
        }


        if (fase.equals("Fase Principal")) {

            Utilitaria.colocarTablero(context,jugador.getMano(),manoJ,monstruosJ, especialesJ,"Fase Principal",jugador.getTablero().getCartasMons(),jugador.getTablero().getEspeciales());
            maquina.mFasePrincipal();

            for (Carta carta : maquina.getTablero().getCartasMons()) {
                Utilitaria.reemplazar(context, carta, monstruosM);
                Utilitaria.removerImageView(context, manoM, carta);
            }
            for (Carta carta : maquina.getTablero().getEspeciales()) {
                Utilitaria.reemplazar(context, carta, especialesM);
                Utilitaria.removerImageView(context, manoM, carta);
            }

        }


        if (fase.equals("Fase Batalla")) {
            Utilitaria.quitarClickListeners(manoJ);
            Utilitaria.mostrarDetallesbatalla(context, monstruosJ,monstruosM,especialesJ,especialesM,jugador.getTablero().getCartasMons(),jugador.getTablero().getEspeciales(),maquina.getTablero().getCartasMons(),maquina.getTablero().getEspeciales(),jugador,maquina);
        }

    }
    */


    public void prueba(LinearLayout manoJ, LinearLayout manoM, LinearLayout monstruosJ, LinearLayout monstruosM, LinearLayout especialesJ, LinearLayout especialesM, TextView turnosView,TextView vidaJView,TextView vidaMView) {
        turnosView.setText("Turno: "+turno);
        vidaJView.setText("LP de "+jugador.getNombre()+": "+jugador.getPuntos());
        vidaMView.setText("LP de la "+maquina.getNombre()+": "+maquina.getPuntos());


        if (fase.equals("Fase Tomar Carta")) {

            if (turno==1){
                for (Carta c : jugador.getMano()) {
                    Utilitaria.crearyAgregar(context, c, manoJ);
                }
                for (Carta c : maquina.getMano()) {
                    Utilitaria.crearyAgregar(context, c, manoM);
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Comienza el juego!");
                builder.setMessage("La "+ maquina.getNombre()+" jugara contra "+jugador.getNombre());

                // Botón "OK" para cerrar el diálogo
                builder.setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss(); // Cierra el cuadro de diálogo
                });

                // Mostrar el AlertDialog
                builder.show();

            }
            //Se coloquen las cartas de la mano del jugador y de la maquina en el linearLayout

            Carta ct= jugador.getDeck().getCartas().get(0);
            jugador.getMano().add(ct);
            jugador.getDeck().getCartas().remove(0);
            Toast.makeText(context, "Jugador tomo la carta "+ct.getNombre(), Toast.LENGTH_SHORT).show();
            Utilitaria.crearyAgregar(context,ct,manoJ);

            Carta ctm= maquina.getDeck().getCartas().get(0);
            maquina.getMano().add(ctm);
            maquina.getDeck().getCartas().remove(0);
            Toast.makeText(context, "La maquina tomo la carta "+ctm.getNombre(), Toast.LENGTH_SHORT).show();
            Utilitaria.crearyAgregar(context,ctm,manoM);
            Utilitaria.eliminarClickListenersTablero(monstruosJ, monstruosM, especialesJ, especialesM);
            if (turno>=3)
            {
                String maquinaB = this.mBatalla(jugador,monstruosM,monstruosJ,especialesJ,especialesM,vidaJView,vidaMView);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Batalla de la Máquina");
                builder.setMessage(maquinaB);

                // Botón "OK" para cerrar el diálogo
                builder.setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss(); // Cierra el cuadro de diálogo
                });
            }
        }
        if (fase.equals("Fase Principal")) {

            Utilitaria.colocarTablero(context,jugador.getMano(),manoJ,monstruosJ, especialesJ,"Fase Principal",jugador.getTablero().getCartasMons(),jugador.getTablero().getEspeciales());
            maquina.mFasePrincipal();
          
            for (Carta carta : maquina.getTablero().getCartasMons()) {
                Utilitaria.reemplazar(context, carta, monstruosM);
                Utilitaria.removerImageView(context, manoM, carta);
            }
            for (Carta carta : maquina.getTablero().getEspeciales()) {
                Utilitaria.reemplazar(context, carta, especialesM);
                Utilitaria.removerImageView(context, manoM, carta);
            }
            Utilitaria.organizarTablero(context,maquina.getTablero().getEspeciales(),especialesM);
            ArrayList<Carta> orga= new ArrayList<>();
            for(CartaMonstruo c : maquina.getTablero().getCartasMons())
                orga.add(c);
            Utilitaria.organizarTablero(context,orga,monstruosM);

        }

        if (fase.equals("Fase Batalla")) {
            Utilitaria.quitarClickListeners(manoJ);
            if(turno<2) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Información del Turno");
                builder.setMessage("A partir del segundo turno puedes declarar batalla.\nSigue a la siguiente fase porfavor :)");

                // Botón "OK" para cerrar el diálogo
                builder.setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss(); // Cierra el cuadro de diálogo
                });

                // Mostrar el AlertDialog
                builder.show();
                turno+=1;
            }else {

                Utilitaria.mostrarDetallesbatalla(context, monstruosJ,monstruosM,especialesJ,especialesM,jugador.getTablero().getCartasMons(),jugador.getTablero().getEspeciales(),maquina.getTablero().getCartasMons(),maquina.getTablero().getEspeciales(),jugador,maquina,vidaJView,vidaMView);

                turno++;
            }

        }
        
    }

}
