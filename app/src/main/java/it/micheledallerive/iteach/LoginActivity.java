package it.micheledallerive.iteach;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.progressindicator.CircularProgressIndicatorSpec;
import com.google.android.material.progressindicator.IndeterminateDrawable;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.apache.commons.validator.routines.EmailValidator;
import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

import it.micheledallerive.iteach.custom.PrimaryButton;

public class LoginActivity extends AppCompatActivity {

    private enum mButtonFunction{
        LOGIN,
        REGISTRAZIONE
    }

    private TextInputLayout mEmailLayout;
    private TextInputEditText mEmailInput;

    private TextInputLayout mPasswordLayout;
    private TextInputEditText mPasswordInput;

    private PrimaryButton mButton;

    private mButtonFunction mButtonFunction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailLayout = findViewById(R.id.email_layout);
        mEmailInput = findViewById(R.id.email_input);

        mPasswordLayout = findViewById(R.id.password_layout);
        mPasswordInput = findViewById(R.id.password_input);

        mButton = findViewById(R.id.button);

        mButtonFunction = mButtonFunction.REGISTRAZIONE;

        // Setup dell'input della mail
        mEmailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean validEmail = validateEmail(s.toString());
                if(mButtonFunction==mButtonFunction.LOGIN){ // Se sto modificando la mail quando è già in modalità "LOGIN", torno in modalità registrazione e controllo la nuova email
                    mPasswordLayout.setVisibility(View.GONE);
                    Utils.animateAlpha(mPasswordLayout, 0f, "medium"); // nascondo il campo password
                    mButtonFunction = mButtonFunction.REGISTRAZIONE; // imposto il pulsante in modalita registrazione (solo email)
                    mButton.setText("Avanti"); // cambio il testo del pulsante
                }
                if(!validEmail){ // Indirizzo email non valido
                    mButton.setEnabled(false);
                    mEmailLayout.setError("L'indirizzo email non è valido");
                }else{ // Indirizzo email valido, procedo a verifica se esiste l'account
                    mEmailLayout.setErrorEnabled(false);
                    checkExistingAccount(); // controllo se effettivamente esiste l'account
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

         // Setup dell'input della password (cancella l'errore di login fallito quando la modifico)
        mPasswordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(mPasswordLayout.isErrorEnabled()) mPasswordLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Setup del click del pulsante
        mButton.setOnClickListener((v)->{
            System.out.println(mButtonFunction);
            switch(mButtonFunction){
                case LOGIN:
                    loginFailed();
                    break;
                case REGISTRAZIONE:
                    break;
            }
        });

    }

    Timer existingTimer = null;

    private void checkExistingAccount(){
        startEmailLoading();
        if(existingTimer!=null){ // se la mail viene modificata prima che la precedente richiesta di controllo si sia conclusa bisogna annullarla
            existingTimer.cancel();
            existingTimer=null;
        }
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(()->mButton.setEnabled(true)); // attivo il pulsante

                // MAIL TROVATA
                mButtonFunction = mButtonFunction.LOGIN; // imposto il pulsante in modalità LOGIN

                mButton.setText("Login");
                runOnUiThread(()->mPasswordLayout.setVisibility(View.VISIBLE));
                Utils.animateAlpha(mPasswordLayout, 1f, "medium"); // mostro il campo "Password" per inserire la password del login

                stopEmailLoading(); // ho finito di controllare la mail, posso togliere la progressbar nella mail

                existingTimer=null;
            }
        }, 3000);
        existingTimer=t;
    }

    private void startEmailLoading(){
        Handler h = new Handler();
        h.postDelayed(
                ()->{
                    CircularProgressDrawable progressDrawable = new CircularProgressDrawable(this);
                    progressDrawable.setStyle(CircularProgressDrawable.LARGE);
                    progressDrawable.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
                    progressDrawable.start(); // creo e avvio la progressbar circolare
                    runOnUiThread(()->mEmailLayout.setEndIconDrawable(progressDrawable)); // imposto la progressbar come icona nella mail mentre controllo se esiste
                }, 100
        );
    }

    private void stopEmailLoading(){
        runOnUiThread(()->{
            mEmailLayout.setEndIconDrawable(null);
            mEmailLayout.setEndIconVisible(true);
        });
    }

    private boolean validateEmail(String email){
        return EmailValidator.getInstance().isValid(email);
    }

    private void loginSuccess(){

    }

    private void loginFailed(){
        mPasswordLayout.setError("Le credenziali inserite non sono corrette");
    }
}