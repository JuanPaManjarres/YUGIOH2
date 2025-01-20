package ec.edu.espol.yugioh2;

public class CartaMagica extends Carta{
    private final TipoMonstruo tipo;
    private final int defensa;
    private final int ataque;

    public CartaMagica(String nombre, String descripcion, Posicion posicion, Orientacion orientacion, int ataque, int defensa, TipoMonstruo tipo, String imagen) {
        super(nombre, descripcion, posicion, orientacion,imagen);
        this.tipo = tipo;
        this.defensa = defensa;
        this.ataque = ataque;
    }

    // Getters
    public TipoMonstruo getTipo() {
        return tipo;
    }
    public int getAtaque() {
        return ataque;
    }
    public int getDefensa() {
        return defensa;
    }

    // Función a usar en el juego
    public String usar(CartaMonstruo cartaMonstruo) {
        if (tipo == cartaMonstruo.getTipo()) {
            if (defensa == 0) { // Carta mágica que aumenta el ataque
                int nuevoAtaque = cartaMonstruo.getAtaque() + ataque;
                cartaMonstruo.setAtaque(nuevoAtaque);
            } else if (this.ataque == 0) { // Carta mágica que aumenta la defensa
                int nuevaDefensa = cartaMonstruo.getDefensa() + this.defensa;
                cartaMonstruo.setDefensa(nuevaDefensa);
            }
            return toString();
        } else {
            return "No se puede usar, no son del mismo tipo de Monstruo";
        }
    }

    // Método toString
    @Override
    public String toString() {
        if (this.ataque == 0) {
            return "Carta Mágica: " + getNombre() + ", incrementa en " + defensa + " la defensa de monstruos de tipo " + tipo;
        } else {
            return "Carta Mágica: " + getNombre() + ", incrementa en " + ataque + " el ataque de monstruos de tipo " + tipo;
        }
    }

}
