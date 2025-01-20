package ec.edu.espol.yugioh2;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Utilitaria {

    public static void crearDialogs(Context context, String titulo, String descripcion, String msj) {
        new AlertDialog.Builder(context)
                .setTitle(titulo)
                .setMessage(descripcion)
                .setPositiveButton("OK", (dialog, which) -> {
                    Toast.makeText(context, msj, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                })
                .show();
    }

    public static void fasesDialog(Context context, Carta carta, String fase, ImageView imageView, ImageView[] currentSelectedCard,ArrayList<CartaMonstruo> tableroM,ArrayList<Carta> tableroE,LinearLayout manoView,LinearLayout monstruosJ,LinearLayout especialesJ,ArrayList<Carta> cartas) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Detalles de la Carta");
        String boton = "";

        if (fase.equals("Fase Principal")) {
            if (carta instanceof CartaMonstruo) {
                CartaMonstruo c = (CartaMonstruo) carta;
                builder.setMessage(c.toString());
                boton = "Ataque";
                builder.setPositiveButton(boton, (dialog, which) -> {
                    currentSelectedCard[0] = imageView;
                    // Aquí puedes agregar la lógica para poner la carta en ataque (guardar el estado, etc.)
                    carta.setOrientacion(Orientacion.ARRIBA);
                    selecTablero(context, manoView, monstruosJ, especialesJ,currentSelectedCard, cartas,tableroM,tableroE);

                });
                builder.setNegativeButton("Defensa", (dialog, which) -> {
                    currentSelectedCard[0] = imageView;
                    // Aquí puedes agregar la lógica para poner la carta en ataque (guardar el estado, etc.)
                    c.setOrientacion(Orientacion.ABAJO);
                    selecTablero(context, manoView, monstruosJ, especialesJ,currentSelectedCard, cartas,tableroM,tableroE);
                });
            }
            if (carta instanceof CartaMagica) {
                CartaMagica c = (CartaMagica) carta;
                builder.setMessage(c.toString());
                boton = "Colocar";
                builder.setPositiveButton(boton, (dialog, which) -> {
                    currentSelectedCard[0] = imageView;
                    // Aquí puedes agregar la lógica para poner la carta en ataque (guardar el estado, etc.)
                    carta.setOrientacion(Orientacion.ARRIBA);
                    selecTablero(context, manoView, monstruosJ, especialesJ,currentSelectedCard, cartas,tableroM,tableroE);

                });
            }
            if (carta instanceof CartaTrampa) {
                CartaTrampa c = (CartaTrampa) carta;
                builder.setMessage(c.toString());
                boton = "Colocar";
                builder.setPositiveButton(boton, (dialog, which) -> {
                    currentSelectedCard[0] = imageView;
                    // Aquí puedes agregar la lógica para poner la carta en ataque (guardar el estado, etc.)
                    carta.setOrientacion(Orientacion.ARRIBA);
                    selecTablero(context, manoView, monstruosJ, especialesJ,currentSelectedCard, cartas,tableroM,tableroE);

                });
            }
            // Botones del cuadro de diálogo

            builder.setNeutralButton("Cancelar", (dialog, which) -> dialog.dismiss());
        }

        builder.show();

    }


    public static void crearyAgregar(Context context, Carta carta, LinearLayout contenedor) {
        int imagenId = context.getResources().getIdentifier(
                carta.getImagen(),"drawable",context.getPackageName()
        );
        ImageView cartaView = new ImageView(context);
        cartaView.setImageResource(imagenId);
        cartaView.setTag(carta.getImagen());
        contenedor.addView(cartaView);
        cartaView.getLayoutParams().width = 250;
        cartaView.setPadding(10,0,10,0);
        cartaView.setScaleType(ImageView.ScaleType.FIT_XY);

    }

    public static void reemplazar(Context context, Carta carta, LinearLayout contenedor) {
        int imagenId = context.getResources().getIdentifier(
                carta.getImagen(), "drawable", context.getPackageName()
        );

        // Obtener la imagen "no hay carta" para identificar los espacios vacíos
        int noHayCartaId = context.getResources().getIdentifier(
                "no_hay_carta", "drawable", context.getPackageName()
        );
        Drawable noHayCartaDrawable = context.getResources().getDrawable(noHayCartaId);

        ImageView cartaSeleccionada = null;

        // Recorre todos los elementos del contenedor
        for (int i = 0; i < contenedor.getChildCount(); i++) {
            ImageView imageView = (ImageView) contenedor.getChildAt(i);
            Drawable currentDrawable = imageView.getDrawable();

            // Si el ImageView tiene la imagen "no hay carta", es un espacio vacío
            if (currentDrawable != null && currentDrawable.getConstantState().equals(noHayCartaDrawable.getConstantState())) {
                cartaSeleccionada = imageView;
            }
        }

        if (cartaSeleccionada != null) {
            // Obtener el ID del contenedor (monstruos o magicas)
            String contenedorId = contenedor.getResources().getResourceEntryName(contenedor.getId());

            // Verificar el tipo de la carta
            String tipoCarta = carta.getClass().getSimpleName(); // Obtiene el tipo de carta (e.g., "CartaMonstruo", "CartaMagica", "CartaTrampa")

            // Verificar si el contenedor es para monstruos y la carta no es monstruo
            if (contenedorId.contains("monstruosJ") && !tipoCarta.equals("CartaMonstruo")) {
                Utilitaria.crearDialogs(context, "Error", "No se puede colocar una carta que no sea monstruo en el área de monstruos.", "OK");
                return;  // Salir de la función para evitar que la carta se coloque
            }

            // Verificar si el contenedor es para cartas mágicas/trampa y la carta no es mágica/trampa
            if (contenedorId.contains("magicasJ") && (tipoCarta.equals("CartaMonstruo"))) {
                Utilitaria.crearDialogs(context, "Error", "No se puede colocar una carta de monstruo en el área de cartas mágicas/trampa.", "OK");
                return;  // Salir de la función para evitar que la carta se coloque
            }

            // Si la carta es un monstruo, verificar la orientación
            if (tipoCarta.equals("CartaMonstruo")) {
                CartaMonstruo cm = (CartaMonstruo) carta;

                if (cm.getOrientacion() == Orientacion.ARRIBA) {
                    // Reemplazar la carta en el contenedor para cartas en orientación ARRIBA
                    cartaSeleccionada.setImageResource(imagenId);
                    cartaSeleccionada.setTag(carta.getImagen());  // Actualiza el tag con el nombre de la carta
                } else {
                    // Reemplazar la carta en el contenedor para cartas en orientación ABAJO
                    int cartaAbajoId = R.drawable.carta_abajo; // Asegúrate de usar un recurso adecuado para la carta en orientación abajo
                    cartaSeleccionada.setImageResource(cartaAbajoId);
                    cartaSeleccionada.setTag("carta_abajo");  // Cambia el tag a "carta_abajo" o el identificador adecuado
                }
            } else {
                // Si no es un monstruo, reemplazar la carta normalmente
                cartaSeleccionada.setImageResource(imagenId);
                cartaSeleccionada.setTag(carta.getImagen());  // Actualiza el tag con el nombre de la carta
            }
        }

    }
/*
    public static void cartaViewM(Context context, Carta carta, LinearLayout contenedor) {
        int imagenId = context.getResources().getIdentifier(
                carta.getImagen(), "drawable", context.getPackageName()
        );

        int noHayCartaId = context.getResources().getIdentifier(
                "no_hay_carta", "drawable", context.getPackageName()
        );

        Drawable noHayCartaDrawable = context.getResources().getDrawable(noHayCartaId);
        Drawable cartaCambiar = context.getResources().getDrawable(imagenId);

        // Buscar si existe un ImageView con la imagen "no_hay_carta"
        ImageView cartaSeleccionada = null;
        for (int i = 0; i < contenedor.getChildCount(); i++) {
            View child = contenedor.getChildAt(i);
            if (child instanceof ImageView) {
                ImageView imageView = (ImageView) child;

                // Verificar si el Drawable actual del ImageView es "no_hay_carta"
                Drawable currentDrawable = imageView.getDrawable();
                if (currentDrawable != null && currentDrawable.getConstantState().equals(noHayCartaDrawable.getConstantState())) {
                    cartaSeleccionada = imageView;
                }
            }
        }

        if (cartaSeleccionada != null) {
            //cartaSeleccionada.setImageDrawable(cartaCambiar);
            //cartaSeleccionada.setImageDrawable(noHayCartaDrawable);
            cartaSeleccionada.setImageResource(imagenId);
            //cartaSeleccionada.setScaleType(ImageView.ScaleType.FIT_XY);

        } else {
            // Crear un nuevo ImageView si no existe "no_hay_carta"
            ImageView cartaView = new ImageView(context);
            cartaView.setImageResource(imagenId);
            cartaView.setTag(carta.getImagen());
            contenedor.addView(cartaView);
            cartaView.getLayoutParams().width = 250;
            cartaView.setPadding(10, 0, 10, 0);
            cartaView.setScaleType(ImageView.ScaleType.FIT_XY);

        }
    }

 */

    public static void selecCarta1(Context context, ArrayList<Carta> cartas, LinearLayout mano, ImageView[] currentSelectedCard,String fase,ArrayList<CartaMonstruo> tableroM,ArrayList<Carta> tableroE,LinearLayout monstruosJ,LinearLayout especialesJ){
        for (int i = 0; i < mano.getChildCount(); i++) {

            ImageView imageView = (ImageView) mano.getChildAt(i); // Ajusta según el ID real de la carta

            Carta carta = Utilitaria.buscarCarta(cartas, (String) imageView.getTag());
            imageView.setOnClickListener(v -> {

                Utilitaria.fasesDialog(context, carta, fase, imageView, currentSelectedCard,tableroM,tableroE,mano,monstruosJ,especialesJ,cartas);

            });
        }
    }

    public static void selecTablero(Context context, LinearLayout mano, LinearLayout monstruosJ, LinearLayout especialesJ, ImageView[] currentSelectedCard, ArrayList<Carta> cartas,ArrayList<CartaMonstruo> tableroM,ArrayList<Carta> tableroE) {

        if (currentSelectedCard[0] != null) {
            String cartaTag = (String) currentSelectedCard[0].getTag();
            Carta c = buscarCarta(cartas, cartaTag);

            if (c.getOrientacion() == Orientacion.ABAJO) {
                CartaMonstruo cm= (CartaMonstruo) c;
                for (int i = 0; i < monstruosJ.getChildCount(); i++) {
                    ImageView carta = (ImageView) monstruosJ.getChildAt(i);
                    Drawable imagenActual = carta.getDrawable(); // Obtener el drawable actual
                    Drawable noHayCarta = context.getResources().getDrawable(R.drawable.no_hay_carta); // Drawable de referencia

                    if (imagenActual != null && imagenActual.getConstantState() != null &&
                            imagenActual.getConstantState().equals(noHayCarta.getConstantState())) {
                        mano.removeView(currentSelectedCard[0]);
                        // Cambiar la imagen de la carta boca abajo
                        int idCartaAbajo = R.drawable.carta_abajo; // Asegúrate de usar un recurso adecuado
                        Drawable cartaAbajo = context.getResources().getDrawable(idCartaAbajo);
                        carta.setImageDrawable(cartaAbajo);
                        carta.setTag(cm.getImagen());
                        currentSelectedCard[0] = null; // Resetear selección
                        tableroM.add(i,cm);
                        cartas.remove(cm);
                        Toast.makeText(context, "Carta colocada boca abajo en el tablero", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                Toast.makeText(context, "No se puede colocar más cartas monstruo", Toast.LENGTH_SHORT).show();
            } else {
                if (c instanceof CartaMonstruo) {
                    CartaMonstruo ctm= (CartaMonstruo) c;
                    for (int i = 0; i < monstruosJ.getChildCount(); i++) {
                        ImageView carta = (ImageView) monstruosJ.getChildAt(i);
                        Drawable imagenActual = carta.getDrawable(); // Obtener el drawable actual
                        Drawable noHayCarta = context.getResources().getDrawable(R.drawable.no_hay_carta); // Drawable de referencia

                        if (imagenActual != null && imagenActual.getConstantState() != null &&
                                imagenActual.getConstantState().equals(noHayCarta.getConstantState())) {
                            mano.removeView(currentSelectedCard[0]);
                            // Reemplazar la carta normalmente
                            carta.setImageDrawable(currentSelectedCard[0].getDrawable());
                            carta.setTag(ctm.getImagen());
                            currentSelectedCard[0] = null; // Resetear selección
                            cartas.remove(ctm);
                            tableroM.add(i,ctm);
                            Toast.makeText(context, "Carta colocada en el tablero", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    Toast.makeText(context, "No se puede colocar más cartas monstruo", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < especialesJ.getChildCount(); i++) {
                        ImageView carta = (ImageView) especialesJ.getChildAt(i);
                        Drawable imagenActual = carta.getDrawable(); // Obtener el drawable actual
                        Drawable noHayCarta = context.getResources().getDrawable(R.drawable.no_hay_carta); // Drawable de referencia

                        if (imagenActual != null && imagenActual.getConstantState() != null &&
                                imagenActual.getConstantState().equals(noHayCarta.getConstantState())) {
                            mano.removeView(currentSelectedCard[0]);
                            // Reemplazar la carta normalmente
                            carta.setImageDrawable(currentSelectedCard[0].getDrawable());
                            currentSelectedCard[0] = null; // Resetear selección
                            cartas.remove(c);
                            tableroE.add(i,c);
                            Toast.makeText(context, "Carta colocada en el tablero", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    Toast.makeText(context, "No se puede colocar más cartas especiales", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(context, "Selecciona una carta primero", Toast.LENGTH_SHORT).show();
        }
    }

    private static Carta buscarCarta(ArrayList<Carta> cartas, String nombre) {
        for (Carta carta : cartas) {
            if (carta.getImagen().equals(nombre)) {
                return carta;
            }
        }
        return null; // En caso de no encontrar la carta
    }

    private static CartaMonstruo buscarCartaMonstruo(ArrayList<CartaMonstruo> cartas, String nombre) {
        for (CartaMonstruo carta : cartas) {
            if (nombre.equals(carta.getImagen())) {
                return carta;
            }
        }
        return null;
    }

    public static Carta imageViewCarta(ImageView imageView, ArrayList<Carta> listaCartas, Context context) {
        // Obtener el Drawable del ImageView
        Drawable drawableImageView = imageView.getDrawable();

        if (drawableImageView == null) {
            return null; // Si el ImageView no tiene imagen, no hay carta asociada
        }
        // Iterar sobre el ArrayList de cartas para encontrar la que corresponde al Drawable
        for (Carta carta : listaCartas) {
            // Obtener el recurso Drawable de la carta basado en su atributo "imagen"
            int imagenId = context.getResources().getIdentifier(
                    carta.getImagen(), "drawable", context.getPackageName()
            );

            if (imagenId != 0) { // Asegurarse de que el recurso exista
                Drawable drawableCarta = context.getResources().getDrawable(imagenId);

                // Comparar si los Drawables son iguales
                if (drawableImageView.getConstantState().equals(drawableCarta.getConstantState())) {
                    return carta; // Devolver la carta asociada
                }
            }
        }
        return null; // No se encontró ninguna carta asociada
    }


    public static void colocarTablero(Context context,ArrayList<Carta> cartas, LinearLayout mano, LinearLayout monstruosJ,LinearLayout especialesJ, String fase,ArrayList<CartaMonstruo> tableroM,ArrayList<Carta> tableroE){
        final ImageView[] currentSelectedCard = {null};
        selecCarta1(context,cartas,mano,currentSelectedCard,fase,tableroM,tableroE,monstruosJ,especialesJ);
    }


    //funcion que cambia el texto del view de la vida por el nombre y los puntos de vida
    public static void vidaJugadorView(Jugador j,TextView textvida)
    {
        textvida.setText("LP "+j.getNombre()+": "+j.getPuntos());
    }
    //funcion que cambia el texto del view de turnos por el asignado
    public static void cambiarturnoView(int turno, TextView textturno)
    {
        String texto= ""+turno;
        textturno.setText("Turno: "+ texto);
    }
    public static ImageView crearImagen(Context context, Carta carta) {
        int imagenId = context.getResources().getIdentifier(
                carta.getImagen(),"drawable",context.getPackageName()
        );
        ImageView cartaView = new ImageView(context);
        cartaView.setImageResource(imagenId);
        cartaView.setTag(carta.getImagen());
        cartaView.getLayoutParams().width = 250;
        cartaView.setPadding(10,0,10,0);
        cartaView.setScaleType(ImageView.ScaleType.FIT_XY);
        return cartaView;

    }
    public static void quitarClickListeners(LinearLayout mano) {
        // Recorrer todos los hijos del LinearLayout (cartas en la mano)
        for (int i = 0; i < mano.getChildCount(); i++) {
            // Obtener la carta (ImageView)
            ImageView carta = (ImageView) mano.getChildAt(i);

            // Eliminar el OnClickListener de cada carta
            carta.setOnClickListener(null);
        }
    }

    public static void eliminarClickListenersTablero(LinearLayout monstruosJ, LinearLayout monstruosM, LinearLayout especialesJ, LinearLayout especialesM) {
        // Crear una lista de los LinearLayout del tablero
        LinearLayout[] tableros = {monstruosJ, monstruosM, especialesJ, especialesM};

        // Recorrer cada LinearLayout del tablero
        for (LinearLayout tablero : tableros) {
            // Recorrer todos los hijos del LinearLayout (cartas en el tablero)
            for (int i = 0; i < tablero.getChildCount(); i++) {
                // Obtener la carta (ImageView)
                ImageView carta = (ImageView) tablero.getChildAt(i);

                // Eliminar el OnClickListener de cada carta
                carta.setOnClickListener(null);
            }
        }
    }
    public static void mostrarDetallesbatalla(Context context, LinearLayout monstruosJ, LinearLayout monstruosM, LinearLayout magicasJ, LinearLayout magicasM,
                                              ArrayList<CartaMonstruo> tableroMonsJ, ArrayList<Carta> tableroEspeJ, ArrayList<CartaMonstruo> tableroMonsM, ArrayList<Carta> tableroEspeM, Jugador jugador, Jugador maquina,TextView vidaJugador,TextView vidaMaquina) {
        // Recorrer el LinearLayout de monstruos
        for (int i = 0; i < monstruosJ.getChildCount(); i++) {
            ImageView carta = (ImageView) monstruosJ.getChildAt(i);

            // Establecer el click listener para la ImageView
            carta.setOnClickListener(v -> {
                // Obtener el índice de la ImageView tocada
                int index = monstruosJ.indexOfChild(carta);

                // Verificar si el índice tiene una carta válida
                if (index < 0 || index >= tableroMonsJ.size() || tableroMonsJ.get(index) == null) {
                    return; // Ignorar clic si no hay carta
                }

                // Obtener la carta correspondiente en tableroM utilizando el índice
                CartaMonstruo cartaMonstruo = tableroMonsJ.get(index);

                // Verificar si la carta es "no_hay_carta"
                if ("no_hay_carta".equals(cartaMonstruo.getNombre())) {
                    return; // Ignorar clic si es una carta vacía
                }

                // Crear el AlertDialog para mostrar las especificaciones de la carta
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Detalles de la carta");

                // Mostrar detalles de la carta
                if (cartaMonstruo.getOrientacion() == Orientacion.ARRIBA) {
                    builder.setMessage(cartaMonstruo.toString()); // Mostrar detalles de la carta
                    // Solo agregar el botón de "Declarar batalla" si la carta está en modo ataque (posición vertical)
                    if (cartaMonstruo.getPosicion() == Posicion.VERTICAL) {
                        builder.setPositiveButton("Declarar batalla", (dialog, which) -> {
                          selecOponente(context,tableroMonsM,monstruosM,monstruosJ,jugador,maquina,cartaMonstruo,vidaJugador,vidaMaquina);
                          Toast.makeText(context, "Declaro batalla", Toast.LENGTH_SHORT).show();

                        });
                    }

                    // Botón para cambiar el modo (ataque/defensa)
                    String cambiara = (cartaMonstruo.getPosicion() == Posicion.VERTICAL) ? "defensa" : "ataque";
                    builder.setNegativeButton("Cambiar Modo " + cambiara, (dialog, which) -> {
                        // Cambiar entre vertical y horizontal
                        if (cartaMonstruo.getPosicion() == Posicion.VERTICAL) {
                            carta.setRotation(90); // Imagen en modo horizontal
                            cartaMonstruo.setPosicion(Posicion.HORIZONTAL); // Actualizar la posición a horizontal
                        } else {
                            carta.setRotation(0); // Imagen en modo vertical
                            cartaMonstruo.setPosicion(Posicion.VERTICAL); // Actualizar la posición a vertical
                        }
                        Toast.makeText(context, "Modo cambiado", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    // Si la carta está orientada "abajo", solo mostrar el nombre
                    builder.setMessage("Nombre de la carta: " + cartaMonstruo.getNombre());

                    // Botón para voltear la carta (solo cartas monstruo)
                    builder.setPositiveButton("Voltear carta", (dialog, which) -> {
                        // Obtener el identificador del recurso de la imagen desde el nombre
                        int imageResId = context.getResources().getIdentifier(cartaMonstruo.getImagen(), "drawable", context.getPackageName());

                        // Cambiar la imagen del ImageView por la imagen original
                        carta.setImageResource(imageResId); // Actualizar la imagen con el recurso correspondiente

                        // Voltear la carta (cambiar la orientación)
                        cartaMonstruo.setOrientacion(Orientacion.ARRIBA); // Cambiar la orientación de la carta a "arriba"
                        Toast.makeText(context, "Carta volteada", Toast.LENGTH_SHORT).show();
                    });
                }

                // Botón para cerrar el diálogo
                builder.setNeutralButton("Cerrar", null);

                // Mostrar el AlertDialog
                builder.show();
            });
        }

        // Recorrer el LinearLayout de cartas mágicas y trampas (magicasJ)
        for (int i = 0; i < magicasJ.getChildCount(); i++) {
            ImageView carta = (ImageView) magicasJ.getChildAt(i);

            // Establecer el click listener para la ImageView
            carta.setOnClickListener(v -> {
                // Obtener el índice de la ImageView tocada
                int index = magicasJ.indexOfChild(carta);

                // Verificar si el índice tiene una carta válida
                if (index < 0 || index >= tableroEspeJ.size() || tableroEspeJ.get(index) == null) {
                    return; // Ignorar clic si no hay carta
                }

                // Obtener la carta correspondiente en tableroE (cartas mágicas y trampas)
                Carta cartaSeleccionada = tableroEspeJ.get(index);

                // Verificar si la carta es "no_hay_carta"
                if ("no_hay_carta".equals(cartaSeleccionada.getNombre())) {
                    return; // Ignorar clic si es una carta vacía
                }

                // Crear el AlertDialog para mostrar las especificaciones de la carta
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Detalles de la carta");

                // Determinar si la carta es mágica o trampa
                if (cartaSeleccionada instanceof CartaMagica) {
                    CartaMagica cM = (CartaMagica) cartaSeleccionada;
                    builder.setMessage(cartaSeleccionada.toString());

                    // Botón para usar la carta mágica
                    builder.setPositiveButton("Usar carta", (dialog, which) -> {
                        // Lógica para usar la carta mágica
                        selecMejora(context,tableroMonsJ,monstruosJ,magicasJ,cM,jugador);
                        tableroEspeJ.remove(cartaSeleccionada);
                        Toast.makeText(context, "Carta mágica utilizada", Toast.LENGTH_SHORT).show();
                    });
                } else if (cartaSeleccionada instanceof CartaTrampa) {
                    builder.setMessage(cartaSeleccionada.toString());

                    // Solo botón de cerrar para cartas trampa
                    builder.setNegativeButton("Cerrar", null);
                }

                // Botón para cerrar el diálogo
                builder.setNegativeButton("Cerrar", null);

                // Mostrar el AlertDialog
                builder.show();
            });
        }
    }


    public static ArrayList<Carta> leerImagenesLayout(Context context,LinearLayout contenedor, ArrayList<Carta> cartas) {
        ArrayList<Carta> cartasContenedor = new ArrayList<>();
        for (int i = 0; i < contenedor.getChildCount(); i++) {

            ImageView imageView = (ImageView) contenedor.getChildAt(i); // Ajusta según el ID real de la carta

            Carta carta = Utilitaria.buscarCarta(cartas, (String) imageView.getTag());
            cartasContenedor.add(carta);
        }
        return cartasContenedor;

    }


    public static void removerImageView(Context context,LinearLayout mano, Carta carta){
        int imagenId = context.getResources().getIdentifier(carta.getImagen(), "drawable", context.getPackageName());

        if (imagenId == 0) {
            // Si la carta no tiene un drawable válido, no hacer nad
            return;
        }

        // Obtener el drawable de la carta específica
        Drawable drawableCarta = context.getResources().getDrawable(imagenId);
        ImageView cartaSeleccionada = null;

        // Recorre todos los elementos del LinearLayout
        for (int i = 0; i < mano.getChildCount(); i++) {
            ImageView imageView = (ImageView) mano.getChildAt(i);

                // Comparar el drawable del ImageView con el de la carta
            Drawable currentDrawable = imageView.getDrawable();
            if (currentDrawable != null && currentDrawable.getConstantState().equals(drawableCarta.getConstantState())) {
                cartaSeleccionada = imageView;
                // Continúa recorriendo para garantizar que solo la última coincidencia (si hay varias) será seleccionada
            }
        }
        // Si encontró el último ImageView correspondiente, eliminarlo
        if (cartaSeleccionada != null) {
            mano.removeView(cartaSeleccionada);
        }
    }

    public static void noHayCarta(Context context,LinearLayout contenedor, Carta carta){
        int imagenId = context.getResources().getIdentifier(carta.getImagen(), "drawable", context.getPackageName());
        //ID DEL DRWABLE
        if (imagenId == 0) {
            // Si la carta no tiene un drawable válido, no hacer nad
            return;
        }

        // Obtener el ID del drawable "no hay carta"
        int noHayCartaId = context.getResources().getIdentifier("no_hay_carta", "drawable", context.getPackageName());
        if (noHayCartaId == 0) {
            // Si no se encuentra el drawable "no_hay_carta", no hacer nada
            return;
        }

        // Obtener el drawable de la carta específica
        Drawable drawableCarta = context.getResources().getDrawable(imagenId);
        ImageView cartaSeleccionada = null;
        //CARTA SELECCIONADA ES LA QUE SE VA A CAMBIAR

       // Drawable noHayCartaDrawable = context.getResources().getDrawable(noHayCartaId);

        // Recorre todos los elementos del LinearLayout
        for (int i = 0; i < contenedor.getChildCount(); i++) {
            ImageView imageView = (ImageView) contenedor.getChildAt(i);

            // Comparar el drawable del ImageView con el de la carta
            Drawable currentDrawable = imageView.getDrawable();
            if (currentDrawable != null && currentDrawable.getConstantState().equals(drawableCarta.getConstantState())) {
                cartaSeleccionada = imageView;
                // Continúa recorriendo para garantizar que solo la última coincidencia (si hay varias) será seleccionada
            }
        }
        // Si encontró el último ImageView correspondiente, eliminarlo
        if (cartaSeleccionada != null) {
            //contenedor.removeView(cartaSeleccionada);
            cartaSeleccionada.setImageResource(noHayCartaId);
            cartaSeleccionada.setTag(noHayCartaId);
        }
    }

    public static void selecOponente(Context context, ArrayList<CartaMonstruo> cartasOponente, LinearLayout layoutOponente, LinearLayout layoutAtacante,
                                     Jugador atacante, Jugador oponente, CartaMonstruo cartaAtacante, TextView vidaJugador, TextView vidaMaquina) {

        // Verificar si no hay cartas en el tablero del oponente
        if (cartasOponente == null || cartasOponente.size() == 0) {
            // Realizar una batalla directa
            Juego.batallaDirecta(cartaAtacante, oponente, vidaMaquina); // Usamos vidaMaquina para actualizar la vida del oponente
            return;
        }

        // Obtener el ID de la imagen "no_hay_carta"
        int noHayCartaId = context.getResources().getIdentifier("no_hay_carta", "drawable", context.getPackageName());
        Drawable noHayCartaDrawable = context.getResources().getDrawable(noHayCartaId);

        // TENGO LA CARTA ATACANTE
        for (int i = 0; i < layoutOponente.getChildCount(); i++) {
            int finalIndex = i; // Necesario para acceder al índice en el OnClickListener
            ImageView cartaOponenteView = (ImageView) layoutOponente.getChildAt(i);

            cartaOponenteView.setOnClickListener(oponenteView -> {
                // Verificar si se está tocando un espacio del layoutOponente
                if (!layoutOponente.equals(oponenteView.getParent())) {
                    Toast.makeText(context, "No se puede atacar ahí. Seleccione un espacio válido.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Verificar si la imagen es "no_hay_carta"
                Drawable currentDrawable = cartaOponenteView.getDrawable();
                if (currentDrawable != null && currentDrawable.getConstantState().equals(noHayCartaDrawable.getConstantState())) {
                    Toast.makeText(context, "No hay cartas en ese espacio.", Toast.LENGTH_SHORT).show();
                    // DESHABILITAR CLICK LISTENER DESPUÉS DE QUE NO HAYA CARTA
                    for (int j = 0; j < layoutOponente.getChildCount(); j++) {
                        layoutOponente.getChildAt(j).setOnClickListener(null);
                    }
                    return;
                }

                // Verificar si la carta seleccionada está boca abajo
                CartaMonstruo cartaSeleccionada = cartasOponente.get(finalIndex); // Usar el índice del tablero
                if (cartaSeleccionada.getOrientacion() == Orientacion.ABAJO) {
                    // Realizar la batalla con la carta boca abajo en la posición seleccionada
                    String resultado = Juego.declararBatalla(cartaSeleccionada, cartaAtacante, oponente, atacante, context, layoutOponente, layoutAtacante, vidaJugador, vidaMaquina);
                    crearDialogs(context, "JUGADORES", resultado, "OK");

                    // DESHABILITAR CLICK LISTENER DESPUÉS DEL ATAQUE
                    for (int j = 0; j < layoutOponente.getChildCount(); j++) {
                        layoutOponente.getChildAt(j).setOnClickListener(null);
                    }
                    return;
                }

                // Si la carta no está boca abajo, proceder con la lógica estándar
                String cartaTag = (String) cartaOponenteView.getTag();
                CartaMonstruo cartaOponente = cartasOponente.get(finalIndex);

                if (cartaOponente != null) {
                    String resultado = Juego.declararBatalla(cartaOponente, cartaAtacante, oponente, atacante, context, layoutOponente, layoutAtacante, vidaJugador, vidaMaquina);
                    crearDialogs(context, "JUGADORES", resultado, "OK");

                    // DESHABILITAR CLICK LISTENER DESPUÉS DEL ATAQUE
                    for (int j = 0; j < layoutOponente.getChildCount(); j++) {
                        layoutOponente.getChildAt(j).setOnClickListener(null);
                    }
                } else {
                    Toast.makeText(context, "La carta seleccionada no puede ser atacada.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }



    public static void selecMejora(Context context,ArrayList<CartaMonstruo> monstruosMejorar, LinearLayout layoutMonstruos,LinearLayout layoutMagicas,
                                     CartaMagica cartaMagica, Jugador jugador) {

        //TENGO LA CARTA ATACANTE
        for (int i = 0; i < layoutMonstruos.getChildCount(); i++) {
            ImageView cartaOponenteView = (ImageView) layoutMonstruos.getChildAt(i);
            ArrayList<Carta> cartasM = new ArrayList<>();

            // Convertir cada CartaMonstruo a Carta
            for (CartaMonstruo cartaMonstruo : monstruosMejorar) {
                cartasM.add(cartaMonstruo); // CartaMonstruo es un tipo de Carta, por lo que se puede agregar directamente
            }

            cartaOponenteView.setOnClickListener(oponenteView -> {
                String cartaTag = (String) cartaOponenteView.getTag();
                Carta c = buscarCarta(cartasM, cartaTag);

                if (c != null) {
                    if(c instanceof CartaMonstruo) {
                        CartaMonstruo cartaMejora = (CartaMonstruo) c;
                        String resultado = cartaMagica.usar(cartaMejora);
                        if (!resultado.equals("No se puede usar, no son del mismo tipo de Monstruo")) {
                            organizarTablero(context,jugador.getTablero().getEspeciales(),layoutMagicas);
                        }
                        crearDialogs(context, "MEJORA", resultado, "OK");

                    }
                }
                else {
                    Toast.makeText(context, "Selecciona un Monstruo.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private static void fasesDialogBatalla(Context context,
                                           CartaMonstruo cartaAtacante, String fase, ImageView imageView,
                                           ImageView[] currentSelectedCard, LinearLayout layoutOponente,
                                           ArrayList<CartaMonstruo> cartasOponente, Jugador atacante, Jugador oponente) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Detalles de la Carta");
        builder.setMessage(cartaAtacante.toString());

        if ("Fase Batalla".equals(fase)) {
            builder.setPositiveButton("Declarar Batalla", (dialog, which) -> {
                // Seleccionar carta oponente tras confirmar ataque
                currentSelectedCard[0] = imageView;
                Toast.makeText(context, "Selecciona una carta oponente para atacar.", Toast.LENGTH_SHORT).show();
            });

            builder.setNeutralButton("Cancelar", (dialog, which) -> dialog.dismiss());
        }

        builder.show();

    }
    public static void organizarTablero(Context context, ArrayList<Carta> tableroLogico, LinearLayout tableroVisual) {
        // Verificar si el LinearLayout tiene suficientes espacios para representar el tablero lógico
        if (tableroVisual.getChildCount() < tableroLogico.size()) {
            throw new IllegalArgumentException("El LinearLayout no tiene suficientes espacios para representar el tablero lógico.");
        }

        // Obtener el ID de la imagen "no_hay_carta"
        int noHayCartaId = context.getResources().getIdentifier("no_hay_carta", "drawable", context.getPackageName());
        Drawable noHayCartaDrawable = context.getResources().getDrawable(noHayCartaId);

        // Recorrer el tablero visual y sincronizar con el tablero lógico
        for (int i = 0; i < tableroVisual.getChildCount(); i++) {
            ImageView imageView = (ImageView) tableroVisual.getChildAt(i);

            if (i < tableroLogico.size() && tableroLogico.get(i) != null) {
                // Obtener la imagen de la carta lógica
                String imagenCarta = tableroLogico.get(i).getImagen();
                int cartaDrawableId = context.getResources().getIdentifier(imagenCarta, "drawable", context.getPackageName());

                if (cartaDrawableId != 0) {
                    // Reemplazar el ImageView con la imagen de la carta lógica
                    Drawable cartaDrawable = context.getResources().getDrawable(cartaDrawableId);
                    imageView.setImageDrawable(cartaDrawable);

                    // Asignar el tag de la carta para referencia futura
                    imageView.setTag(tableroLogico.get(i).getImagen());
                } else {
                    // Si no se encuentra la imagen de la carta, usar la imagen "no_hay_carta"
                    imageView.setImageDrawable(noHayCartaDrawable);
                    imageView.setTag("no_hay_carta");
                }
            } else {
                // Si no hay carta lógica en esa posición, usar la imagen "no_hay_carta"
                imageView.setImageDrawable(noHayCartaDrawable);
                imageView.setTag("no_hay_carta");
            }
        }
    }


}
