package ec.edu.espol.yugioh2;

import android.content.Context;

import java.util.ArrayList;

public class Maquina extends Jugador {

    public Maquina(Context context){
        super("Maquina",context);
    }

    //Funciones de la maquina para agregar y ordenar
    public ArrayList<Carta>[] ordenarMano(){
        ArrayList<Carta> monstruos = new ArrayList<>();
        ArrayList<Carta> magicas = new ArrayList<>();
        ArrayList<Carta> trampas = new ArrayList<>();
        ArrayList<Carta>[] cartas = new ArrayList[3];
        cartas[0] = monstruos;
        cartas[1] = magicas;
        cartas[2] = trampas;

        for (Carta carta: getMano()){
            if (carta instanceof CartaMonstruo){
                CartaMonstruo c1 = (CartaMonstruo) carta;
                monstruos.add(c1);
            }
            if (carta instanceof CartaMagica){
                CartaMagica c1 = (CartaMagica) carta;
                magicas.add(c1);
            }
            if (carta instanceof CartaTrampa){
                CartaTrampa c1 = (CartaTrampa) carta;
                trampas.add(c1);
            }
        }
        return cartas;
    }

    public static ArrayList<CartaMonstruo> obtenerMejoresCartas(ArrayList<Carta> listaCartas){
        ArrayList<CartaMonstruo> cartasOrdenadas = new ArrayList<>();
        ArrayList<CartaMonstruo> tresMejores = new ArrayList<>();
        for (Carta carta: listaCartas){
            CartaMonstruo c = (CartaMonstruo) carta;
            cartasOrdenadas.add(c);
        }
        if (tresMejores.size() > 2){
            cartasOrdenadas.sort((c1, c2) -> Integer.compare((c2.getAtaque() + c2.getDefensa()), (c1.getAtaque() + c1.getDefensa())));
            for (int i = 0; i<3; i ++)
                tresMejores.add(cartasOrdenadas.get(i));
        }
        return tresMejores;
    }

    public void agregarMonstruoTablero(CartaMonstruo monstruo, String modo){
        if (getTablero().getCartasMons().size() < 3 ){
            if ("ataque".equals(modo))
                monstruo.modoAtaque();
            if ("defensa".equals(modo))
                monstruo.modoDefensa();
            getTablero().getCartasMons().add(monstruo);
            getMano().remove(monstruo);
        }
    }
    
    public void agregarEspecialesTablero(Carta especial){
        if (getTablero().getEspeciales().size() < 3)
            getTablero().getEspeciales().add(especial);
    }


    //FASE PRINCIPAL
    public void mFasePrincipal(){
        ArrayList<Carta>[] cartas =this.ordenarMano();
        ArrayList<Carta> monstruos = cartas[0];
        ArrayList<Carta> magicas = cartas[1];
        ArrayList<Carta> trampas = cartas[2];
        ArrayList<CartaMonstruo> monstruosMejores = Maquina.obtenerMejoresCartas(monstruos);

        for (CartaMonstruo monstruo: monstruosMejores){
            if (monstruo.getAtaque()< monstruo.getDefensa())
                agregarMonstruoTablero(monstruo, "defensa");
            else    
                agregarMonstruoTablero(monstruo, "ataque");
        }
        for (Carta magica: magicas){
            CartaMagica m = (CartaMagica) magica;
            agregarEspecialesTablero(m);
        }
        for (Carta trampa: trampas){
            CartaTrampa t = (CartaTrampa) trampa;
            agregarEspecialesTablero(t);
        }

    }

    //COMO LA MAQUINA USA LAS ESPECIALES
    public void usarMagicas(){
        ArrayList<Carta> especiales = this.getTablero().getEspeciales();
        ArrayList<CartaMonstruo> monstruos = this.getTablero().getCartasMons();
        ArrayList<Carta> usadas = new ArrayList<>();

        for (Carta carta: especiales){
            if (!usadas.contains(carta)){
                for (CartaMonstruo monstruo: monstruos){
                    if (carta instanceof CartaMagica){
                        ((CartaMagica) carta).usar(monstruo);
                        usadas.add(carta);
                    }
                }
            }
        }
    
    }
    
    public String mBatalla(Jugador jugador) {
        ArrayList<CartaMonstruo> monstruosJ = new ArrayList<>();
        ArrayList<Carta> usadas = new ArrayList<>();
        ArrayList<CartaTrampa> trampas = new ArrayList<>();
        StringBuilder resultado = new StringBuilder(); 

        for (CartaMonstruo monstruoJ: jugador.getTablero().getCartasMons())
            if (monstruoJ.eModoAtaque())
                monstruosJ.add(monstruoJ);
            
        for (CartaMonstruo monsM: getTablero().getCartasMons()){
            if (!usadas.contains(monsM) && (monsM.eModoAtaque())){
                resultado.append(Juego.usarTrampas(jugador, monsM, trampas));

                if (trampas.isEmpty()){
                    if (monstruosJ.isEmpty()){
                        Juego.batallaDirecta(monsM,jugador);
                    }
                    else {
                        for (CartaMonstruo monsJ: monstruosJ){
                            if (monsM.getAtaque() > monsJ.getAtaque()||monsM.getAtaque() > monsJ.getDefensa()||monsM.getDefensa() < monsJ.getAtaque()||monsM.getAtaque() == monsJ.getAtaque()){
                                usadas.add(monsM);
                                resultado.append(Juego.declararBatalla(monsJ, monsM, jugador, this)).append("\n");
                            }
                        }
                    }
                }
            }
        }
        return resultado.toString();  
    }
}
