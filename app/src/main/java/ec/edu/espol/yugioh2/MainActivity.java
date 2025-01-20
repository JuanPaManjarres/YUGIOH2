package ec.edu.espol.yugioh2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
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

    private TextView fases_J;
    private TextView vidaJugador;
    private TextView turno;
    private TextView vidaMaquina;
    private LinearLayout manoJugador;
    private LinearLayout manoMaquina;
    private LinearLayout magicasJ;

    private LinearLayout monstruosJ;
    private LinearLayout magicasM;
    private LinearLayout monstruoM;

    private Deck deck;

    private ImageView selectedCard;
    private ImageView currentSelectedCard = null;
    private Jugador j;

    private Juego juego; // Declaramos el juego aquí para tener acceso global

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Inicializar vistas
        fases_J = (TextView) findViewById(R.id.fases_M);
        manoJugador = findViewById(R.id.manoJugador);
        magicasJ = findViewById(R.id.magicasJ); // Cambié monstruoJ por magicaJ
        monstruosJ = findViewById(R.id.monstruosJ);
        magicasM = findViewById(R.id.magicasM);
        monstruoM = findViewById(R.id.monstruosM);
        manoMaquina = findViewById(R.id.manoMaquina);
        vidaJugador = (TextView) findViewById(R.id.vidaJugador);
        vidaMaquina = (TextView) findViewById(R.id.vidaMaquina);
        turno = (TextView) findViewById(R.id.turno);

        // Inicializamos los jugadores y la vida
        j = new Jugador("Juan", this);
        j.setPuntos(100);
        Utilitaria.vidaJugadorView(j, vidaJugador);
        Utilitaria.cambiarturnoView(2, turno);

        TextView textoInicioJuego = findViewById(R.id.texto_inicio_juego);
        textoInicioJuego.setVisibility(View.VISIBLE);

        // Ocultamos el texto de inicio después de 3 segundos
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                textoInicioJuego.setVisibility(View.GONE);
            }
        }, 3000);

        // Inicializamos el juego
        juego = new Juego(new Jugador("Alexa", this), this);

        // Configuramos el botón para cambiar de fase
        Button btnCambiarFase = findViewById(R.id.boton_cambiar_fase);
        btnCambiarFase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cambiar la fase del juego
                cambiarFase(fases_J);
                juego.setFase(fases_J.getText().toString(), manoJugador, manoMaquina, monstruosJ, monstruoM, magicasJ, magicasM, turno, vidaJugador, vidaMaquina);

                // Verificar si el jugador o la máquina ganaron
                verificarFinJuego();
            }
        });

        // Verificar si el juego ha terminado al inicio (antes de que el jugador haga clic)
        verificarFinJuego();
    }

    private void verificarFinJuego() {
        // Obtener la vida actual del jugador y la máquina
        String[] arr1 = vidaJugador.getText().toString().split(":");
        int ptsJ = Integer.parseInt(arr1[1].trim());

        String[] arr2 = vidaMaquina.getText().toString().split(":");
        int ptsM = Integer.parseInt(arr2[1].trim());

        // Verificar si la vida del jugador es menor que 0
        if (ptsJ <= 0) {
            mostrarDialogo("El juego ha terminado", "¡" +juego.getMaquina()+" ha ganado el juego!", true);
        } else if (ptsM <= 0) {
            mostrarDialogo("El juego ha terminado", "¡" +juego.getJugador()+" ha ganado el juego!", true);
        }
    }

    private void mostrarDialogo(String titulo, String mensaje, final boolean cerrar) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(titulo)
                .setMessage(mensaje)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (cerrar) {
                            finish(); // Cerrar la actividad
                        }
                    }
                })
                .setCancelable(false) // Evitar que se cierre tocando fuera del diálogo
                .show();
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
        //Toast.makeText(this, "Fase cambiada", Toast.LENGTH_SHORT).show();//muestra un pequeño mensaje que la fase se ha cambiado y luego se elimina
        j.setText(nuevaFase);
    }
}

