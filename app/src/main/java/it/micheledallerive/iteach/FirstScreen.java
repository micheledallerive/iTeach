package it.micheledallerive.iteach;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class FirstScreen extends AppCompatActivity {

    private static Map<String, String> frasi = new HashMap<String, String>() {{
        put("Martin Luther King", "Non hai bisogno di vedere l’intera scalinata. Inizia semplicemente a salire il primo gradino.");
        put("Jim Rohn", "Se vuoi realmente fare qualcosa troverai il modo… Se non vuoi veramente troverai una scusa.");
        put("Rabindranath Tagore", "Non puoi attraversare il mare semplicemente stando fermo e fissando le onde.");
        put("Leo Buscaglia", "Ogni volta che impariamo qualcosa di nuovo, noi stessi diventiamo qualcosa di nuovo.");
        put("Anonimo", "La vita è per il 10% cosa ti accade e per il 90% come reagisci.");
        put("David Eddings", "Nessuna giornata in cui si è imparato qualcosa è andata persa.");
        put("Robert Collier", "Il successo è la somma di piccoli sforzi, ripetuti giorno dopo giorno.");
        put("Helen Hayes", "L’esperto di tutto era una volta un principiante.");
        put("Albert Einstein", "Sembra sempre impossibile fino a quando non è fatto.");
    }};

    ScheduledThreadPoolExecutor executor;
    TextView mFrase;
    TextView mAutore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);

        mFrase = findViewById(R.id.frase_textview);
        mAutore = findViewById(R.id.autore_textview);

        setupFrasi();
    }

    private void setupFrasi(){
        executor = new ScheduledThreadPoolExecutor(1);
        executor.scheduleAtFixedRate(()->{
            int index = (new Random()).nextInt(frasi.size());
            String autore = (String)frasi.keySet().toArray()[index];
            String frase = frasi.get(autore);
            mFrase.setText(frase);
            mAutore.setText(autore);
        }, 0, 5, TimeUnit.SECONDS);
    }
}