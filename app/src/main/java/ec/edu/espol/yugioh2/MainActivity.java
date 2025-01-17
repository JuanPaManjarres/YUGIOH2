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
    private Jugador jugador= new Jugador("Juan",this);
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

        tableroJugador[0][0]= findViewById(R.id.J_cartaMonstruo1);
        tableroJugador[0][1]= findViewById(R.id.J_cartaMonstruo2);
        tableroJugador[0][2]= findViewById(R.id.J_cartaMonstruo3);
        tableroJugador[1][0]= findViewById(R.id.J_cartaMagica1);
        tableroJugador[1][1]= findViewById(R.id.J_cartaMagica2);
        tableroJugador[1][2]= findViewById(R.id.J_cartaMagica3);

        fases_M = (TextView) findViewById(R.id.fases_M);
        fases_J = (TextView) findViewById(R.id.fases_J);
        manoJugador= findViewById(R.id.manoJugador);
        crearImagenesdeck(jugador);


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


    }

    // Metodo para cambiar de fase

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
    public void mostrarDetallesCarta(Carta carta) {
        // Crear el cuadro de diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Detalles de la Carta");

        if (carta instanceof CartaMonstruo) {
            CartaMonstruo c = (CartaMonstruo) carta;
            builder.setMessage(c.toString());
        } else if (carta instanceof CartaMagica) {
            CartaMagica c = (CartaMagica) carta;
            builder.setMessage(c.toString());
        } else if (carta instanceof CartaTrampa) {
            CartaTrampa c = (CartaTrampa) carta;
            builder.setMessage(c.toString());
        }
        // Botones del cuadro de diálogo
        builder.setPositiveButton("Ataque", (dialog, which) -> {
            Toast.makeText(getApplicationContext(), "Carta colocada en Ataque", Toast.LENGTH_SHORT).show();
            // Aquí puedes agregar la lógica para poner la carta en ataque (guardar el estado, etc.)
        });

        builder.setNegativeButton("Defensa", (dialog, which) -> {
            Toast.makeText(getApplicationContext(), "Carta colocada en Defensa", Toast.LENGTH_SHORT).show();
            // Aquí puedes agregar la lógica para poner la carta en defensa (guardar el estado, etc.)
        });

        builder.setNeutralButton("Cancelar", (dialog, which) -> dialog.dismiss());
        builder.show();

    }
    public void crearImagenesdeck(Jugador j) {


        Deck deck = jugador.getDeck();
        ArrayList<Carta> cartas = deck.getCartas();
        for (Carta c : cartas) {
            ImageView imv = new ImageView(this);
            Resources resources = getResources();
            int rid = resources.getIdentifier(c.getImagen(), "drawable", getPackageName());
            imv.setImageResource(rid);
            imagenesDeckJugador.add(imv);
            //imv.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
            imv.getLayoutParams().width = 250;
            imv.setPadding(10, 0, 10, 0);
            imv.setScaleType(ImageView.ScaleType.FIT_XY);
                /*imv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mostrarDetallesCarta(c);
                    }
                });

                 */
        }
    }
    public void inicializar()
    {
        for (int i = 0; i < 5; i++) {
        if (!jugador.getDeck().getCartas().isEmpty()) {
            Carta carta = jugador.getDeck().getCartas().get(0);
            jugador.getMano().add(carta);
            jugador.getDeck().getCartas().remove(carta);
        }
        manoJugador.addView(imagenesDeckJugador.get(i));
        imagenesDeckJugador.remove(i);
    }
    }





}
