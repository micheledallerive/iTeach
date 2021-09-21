package it.micheledallerive.iteach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import it.micheledallerive.iteach.custom.PrimaryButton;

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

    Timer timer;
    TextView mFrase;
    TextView mAutore;
    PrimaryButton mButton;

    FirstScreen instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);

        instance = this;

        mFrase = findViewById(R.id.frase_textview);
        mAutore = findViewById(R.id.autore_textview);
        mButton = findViewById(R.id.button);

        setupFrasi();

        final Intent i = new Intent(this, LoginActivity.class);
        mButton.setOnClickListener(v -> {
            instance.startActivity(i);
            instance.finish();
        });
    }

    private void setupFrasi(){
        timer = new Timer();
        final int shortAnimationDuration = getResources().getInteger(
                android.R.integer.config_mediumAnimTime);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                int index = (new Random()).nextInt(frasi.size());
                String autore = (String)frasi.keySet().toArray()[index];
                String frase = frasi.get(autore);
                mFrase.animate().alpha(0f).setDuration(shortAnimationDuration).setListener(null);
                mAutore.animate().alpha(0f).setDuration(shortAnimationDuration).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        runOnUiThread(()->{
                            mFrase.setText(frase);
                            mAutore.setText("- "+autore);

                            mFrase.animate().alpha(1f).setDuration(shortAnimationDuration).setListener(null);
                            mAutore.animate().alpha(1f).setDuration(shortAnimationDuration).setListener(null);
                        });
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }
        }, 0, 7500);

    }
}