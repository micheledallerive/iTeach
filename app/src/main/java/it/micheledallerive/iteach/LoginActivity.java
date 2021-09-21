package it.micheledallerive.iteach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import org.apache.commons.validator.routines.EmailValidator;

import it.micheledallerive.iteach.custom.PrimaryButton;
import it.micheledallerive.iteach.utils.Callback;
import it.micheledallerive.iteach.utils.LoginUtils;
import it.micheledallerive.iteach.utils.Utils;

public class LoginActivity extends AppCompatActivity {

    private enum mButtonFunction{
        LOGIN,
        REGISTRAZIONE
    }

    private enum EmailCheckResult{
        NOT_REGISTERED,
        REGISTERED_WITH_GOOGLE,
        REGISTERED_WITH_FACEBOOK,
        REGISTERED_WITH_TWITTER
    }

    private FirebaseAuth mAuth;

    private TextInputLayout mEmailLayout;
    private TextInputEditText mEmailInput;

    private TextInputLayout mPasswordLayout;
    private TextInputEditText mPasswordInput;

    private PrimaryButton mButton;

    private mButtonFunction mButtonFunction;

    private LoginActivity activityInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        activityInstance = this;

        mAuth = FirebaseAuth.getInstance();

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
                    checkExistingAccount(s.toString(), new Callback() { // controllo se effettivamente esiste l'account
                        @Override
                        public void onSuccess(Object obj) { // se trovo un account registrato con questo indirizzo email

                            runOnUiThread(()->mButton.setEnabled(true)); // attivo il pulsante

                            mButtonFunction = mButtonFunction.LOGIN; // imposto il pulsante in modalità LOGIN
                            mButton.setText("Login");

                            runOnUiThread(()->mPasswordLayout.setVisibility(View.VISIBLE));
                            Utils.animateAlpha(mPasswordLayout, 1f, "medium"); // mostro il campo "Password" per inserire la password del login

                        }

                        @Override
                        public void onFailure(Object obj) { // se non lo trovo
                            EmailCheckResult result = (EmailCheckResult) obj;
                            switch(result){
                                case NOT_REGISTERED:
                                    mPasswordLayout.setError("La password inserita non è corretta");
                                    break;
                                case REGISTERED_WITH_FACEBOOK:
                                    break;
                                case REGISTERED_WITH_GOOGLE:
                                    break;
                                case REGISTERED_WITH_TWITTER:
                                    break;
                            }
                        }
                    });
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
            switch(mButtonFunction){
                case LOGIN:

                    mAuth.signInWithEmailAndPassword(mEmailInput.getText().toString(), mPasswordInput.getText().toString()).addOnCompleteListener(task -> {
                       if(task.isSuccessful()){
                           // LOGIN EFFETTUATO, VAI OLTRE
                           System.out.println("LOGIN EFFETTUATO: "+task.getResult().getUser().getUid());
                           LoginUtils.updateAfterLogin(activityInstance, task.getResult().getUser());
                       }else{
                           // LOGIN FALLITO
                           System.out.println(task.getException().getMessage());
                       }
                    });

                    break;
                case REGISTRAZIONE:
                    break;
            }
        });

    }



    private void checkExistingAccount(String email, Callback callback){
        startEmailLoading(); // mostro la progressbar per far capire che sto caricando qualcosa lol

        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener((task)->{
            if(task.isSuccessful() && email.equals(mEmailInput.getText().toString())){ // SE riesco a controllare se l'email è registrata E la mail che ho controllato è la stessa che c'è ora
                if(task.getResult().getSignInMethods()!=null){
                    if(task.getResult().getSignInMethods().contains("password")){
                        callback.onSuccess(null);
                    }else if(task.getResult().getSignInMethods().size()>0){
                        // CONTROLLO SE è REGISTRATO CON GOOGLE, FACEBOOK, TWITTER ETC
                    }else{
                        callback.onFailure(EmailCheckResult.NOT_REGISTERED);
                    }
                }
            }
            stopEmailLoading(); // ho finito di controllare la mail, posso togliere la progressbar
        });
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