package ec.edu.espol.yugioh2;

import android.app.AlertDialog;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TextView fases_M;
    private TextView fases_J;
    private Jugador jugador;
    private ImageView[][] tableroJugador= new ImageView[2][3];
    private LinearLayout manoJugador;
    private ArrayList<ImageView> imagenesDeckJugador= new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        jugador= new Jugador("Juan",this);


        tableroJugador[0][0]= findViewById(R.id.J_cartaMonstruo1);
        tableroJugador[0][1]= findViewById(R.id.J_cartaMonstruo2);
        tableroJugador[0][2]= findViewById(R.id.J_cartaMonstruo3);
        tableroJugador[1][0]= findViewById(R.id.J_cartaMagica1);
        tableroJugador[1][1]= findViewById(R.id.J_cartaMagica2);
        tableroJugador[1][2]= findViewById(R.id.J_cartaMagica3);

        fases_M = (TextView) findViewById(R.id.fases_M);
        fases_J = (TextView) findViewById(R.id.fases_J);
        manoJugador= findViewById(R.id.manoJugador);



        //Se define un boton con el ID y se crea una variable
        Button btnCambiarFase = findViewById(R.id.boton_cambiar_fase); //se agrega el boton con el ID
        // Agrega un OnClickListener al botón
        btnCambiarFase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí va la lógica para cambiar de fase
                cambiarFase(fases_J);
                cambiarFase(fases_M);
            }

        });
        jugar();

    }



    private void cambiarFase(TextView j) {
        // Lógica para cambiar de fase
        String faseActual = j.getText().toString();
        String nuevaFase = "";
        if (faseActual.equals("Fase Tomar Carta")) {
            nuevaFase = "Fase Principal";
        }
        else if (faseActual.equals("Fase Principal")){
            nuevaFase = "Fase Batalla";
        }
        else if (faseActual.equals("Fase Batalla")){
            nuevaFase = "Fase Tomar Carta";
        }
        Toast.makeText(this, "Fase cambiada", Toast.LENGTH_SHORT).show();//muestra un pequeño mensaje que la fase se ha cambiado y luego se elimina
        j.setText(nuevaFase);


    }
    public void detallesCartaMano(Carta carta) {
        // Crear el cuadro de diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Detalles de la Carta");

        if (carta instanceof CartaMonstruo) {
            CartaMonstruo c = (CartaMonstruo) carta;
            builder.setMessage(c.toString());
            builder.setPositiveButton("Ataque", (dialog, which) -> {
                Toast.makeText(getApplicationContext(), "Carta colocada en Ataque", Toast.LENGTH_SHORT).show();
                colocarEnTablero(carta, true, false);  // Carta en ataque
            });

            builder.setNegativeButton("Defensa", (dialog, which) -> {
                Toast.makeText(getApplicationContext(), "Carta colocada en Defensa", Toast.LENGTH_SHORT).show();
                colocarEnTablero(carta, true, true);  // Carta en defensa
            });

            builder.setNeutralButton("Cancelar", (dialog, which) -> dialog.dismiss());
            builder.show();
        } else if (carta instanceof CartaMagica) {
            CartaMagica c = (CartaMagica) carta;
            builder.setMessage(c.toString());
            builder.setPositiveButton("Colocar", (dialog, which) -> {
                Toast.makeText(getApplicationContext(), "Carta colocada", Toast.LENGTH_SHORT).show();
                // Coloca la carta en la fila de cartas mágicas o trampas
                colocarEnTablero(carta, false, false);  // Falso para defensa, porque no aplica
            });
            builder.setNeutralButton("Cancelar", (dialog, which) -> dialog.dismiss());
            builder.show();
        } else if (carta instanceof CartaTrampa) {
            CartaTrampa c = (CartaTrampa) carta;  // Asegurándote de que sea una instancia de CartaTrampa
            builder.setMessage(c.toString());
            builder.setPositiveButton("Colocar", (dialog, which) -> {
                Toast.makeText(getApplicationContext(), "Carta colocada", Toast.LENGTH_SHORT).show();
                // Coloca la carta en la fila de cartas mágicas o trampas
                colocarEnTablero(carta, false, false);  // Falso para defensa, porque no aplica
            });
            builder.setNeutralButton("Cancelar", (dialog, which) -> dialog.dismiss());
            builder.show();
        }
    }

    public ImageView crearImagenesdeck(Carta c) {
        // Crear el ImageView
        ImageView imv = new ImageView(this);

        // Configurar la imagen desde los recursos
        Resources resources = getResources();
        int rid = resources.getIdentifier(c.getImagen(), "drawable", getPackageName());
        if (rid != 0) { // Verificar si el recurso existe
            imv.setImageResource(rid);
        } else {
            // Establecer una imagen predeterminada en caso de error
            imv.setImageResource(R.drawable.no_hay_carta);
        }

        // Configurar los LayoutParams
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                250, // Ancho en píxeles
                LinearLayout.LayoutParams.WRAP_CONTENT // Alto automático
        );
        params.setMargins(10, 0, 10, 0); // Añadir margen
        imv.setLayoutParams(params);

        // Configurar propiedades adicionales
        imv.setScaleType(ImageView.ScaleType.FIT_XY);
        imv.setPadding(10, 0, 10, 0);

        // Agregar una descripción accesible
        imv.setContentDescription("Imagen de la carta: " + c.getNombre());

        // Añadir a la lista de imágenes del deck del jugador
        imagenesDeckJugador.add(imv);

        return imv;
    }
    public void inicializarDeck(Jugador j)
    {
        for (int i = 0; i < 5; i++) {
            if (!j.getDeck().getCartas().isEmpty()) {
                Carta carta = j.getDeck().getCartas().get(0);
                j.getMano().add(carta);
                ImageView imv= crearImagenesdeck(carta);
                imv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        detallesCartaMano(carta);
                    }
                });
                if(j instanceof Jugador)
                    manoJugador.addView(imv);
                j.getDeck().getCartas().remove(carta);
            }

        }
    }

    private void colocarEnTablero(Carta carta, boolean esMonstruo, boolean enDefensa) {
        int fila = -1;

        // Si la carta es de monstruo, coloca en la fila 0
        if (esMonstruo) {
            fila = 0;  // Fila de cartas de monstruo
        }
        // Si la carta es mágica o trampa, coloca en la fila 1
        else if (carta instanceof CartaMagica || carta instanceof CartaTrampa) {
            fila = 1;  // Fila de cartas mágicas/trampas
        }

        // Si la fila no es válida, no se puede colocar la carta
        if (fila == -1) {
            Toast.makeText(this, "Tipo de carta no válida", Toast.LENGTH_SHORT).show();
            return;
        }

        for (int columna = 0; columna < tableroJugador[fila].length; columna++) {
            // Verificar si la carta en la posición del tablero es "no_hay_carta"
            if (tableroJugador[fila][columna].getDrawable() != null) {
                // Verificar si es la imagen de "no_hay_carta"
                if (tableroJugador[fila][columna].getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.no_hay_carta).getConstantState())) {
                    // Si es una carta de monstruo y está en defensa, poner boca abajo
                    if (esMonstruo && enDefensa) {
                        tableroJugador[fila][columna].setImageResource(R.drawable.carta_abajo);
                    } else {
                        // Si es una carta mágica o trampa, ponerla boca arriba
                        Resources resources = getResources();
                        int rid = resources.getIdentifier(carta.getImagen(), "drawable", getPackageName());
                        if (rid != 0) {
                            tableroJugador[fila][columna].setImageResource(rid);
                        } else {
                            tableroJugador[fila][columna].setImageResource(R.drawable.no_hay_carta);
                        }
                    }

                    // Remover la carta de la mano del jugador
                    manoJugador.removeView(imagenesDeckJugador.get(jugador.getMano().indexOf(carta)));
                    jugador.getMano().remove(carta);  // Asegurarse de que la carta se elimine de la mano

                    // Mostrar mensaje de éxito
                    Toast.makeText(this, "Carta colocada en el tablero", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }

        // Si no hay espacio disponible en la fila
        Toast.makeText(this, "No hay espacio disponible en esta sección del tablero", Toast.LENGTH_SHORT).show();
    }

    public void jugar()
    {
        inicializarDeck(jugador);



    }







}
