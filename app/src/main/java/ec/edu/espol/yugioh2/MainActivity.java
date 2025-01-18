package ec.edu.espol.yugioh2;

import android.app.AlertDialog;
import android.content.res.AssetManager;
import android.content.res.Resources;
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
    private TextView fases;
    private TextView vidaJugador;
    private TextView turno;
    private LinearLayout manoJugador;
    private LinearLayout manoMaquina;
    private LinearLayout magicasJ;

    private LinearLayout monstruosJ;
    private LinearLayout magicasM;
    private LinearLayout monstruoM;

    private Deck deck;

    private ImageView selectedCard;
    private ImageView currentSelectedCard = null;
    private Juego juego;
    private Jugador j;


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

        fases = (TextView) findViewById(R.id.fases_M);


        manoJugador= findViewById(R.id.manoJugador);
        magicasJ = findViewById(R.id.magicasJ); // cambie monstruoJ por magicaJ incluso en la funcion del tablero
        monstruosJ= findViewById(R.id.monstruosJ);
        magicasM= findViewById(R.id.magicasM);
        monstruoM= findViewById(R.id.monstruosM);
        manoMaquina = findViewById(R.id.manoMaquina);
        vidaJugador= (TextView) findViewById(R.id.vidaJugador);
        turno= (TextView) findViewById(R.id.turno);


        j= new Jugador("Juan",this);
        j.setPuntos(100);
        Utilitaria.vidaJugadorView(j,vidaJugador);
        Utilitaria.cambiarturnoView(2,turno);



        //fases();
        //inicializar();

        Juego juego = new Juego(new Jugador("Alexa",this),this);
        juego.prueba(manoJugador,manoMaquina,monstruosJ,monstruoM,magicasJ,magicasM);

        //Utilitaria.colocarTablero(this,manoJugador,monstruosJ);


        //Se define un boton con el ID y se crea una variable
        Button btnCambiarFase = findViewById(R.id.boton_cambiar_fase); //se agrega el boton con el ID
        // Agrega un OnClickListener al botón
        btnCambiarFase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí va la lógica para cambiar de fase
                cambiarFase(fases);

            }

        });


    }
    public void fases(){
        if (fases.getText().toString().equals("Fase Tomar Carta"))
            inicializar();

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

    public void inicializar()
    {
        ArrayList<ImageView> imagenesDeck= new ArrayList<>();
        AssetManager am = this.getAssets();
        try {
            //Jugador j = new Jugador("Alexa",this);
            Deck deck= Deck.crearDeck(am);
            ArrayList<Carta> cartas= deck.getCartas();
            //ArrayList<Carta> cartas= j.getMano();

            for(Carta c : cartas)
            {
                ImageView imv = new ImageView(this);
                Resources resources = getResources();
                int rid = resources.getIdentifier(c.getImagen(),"drawable",getPackageName());
                imv.setImageResource(rid);
                manoJugador.addView(imv);
                //imv.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
                imv.getLayoutParams().width = 250;
                imv.setPadding(10,0,10,0);
                imv.setScaleType(ImageView.ScaleType.FIT_XY);
                imv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mostrarDetallesCarta(c);
                    }
                });
                imagenesDeck.add(imv);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
