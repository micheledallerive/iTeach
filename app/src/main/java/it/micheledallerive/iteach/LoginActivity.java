package it.micheledallerive.iteach;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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

    private enum ButtonFunction{
        LOGIN,
        REGISTRAZIONE
    }

    private TextInputLayout mEmailLayout;
    private TextInputEditText mEmailInput;

    private TextInputLayout mPasswordLayout;
    private TextInputEditText mPasswordInput;

    private PrimaryButton mButton;
    private ProgressBar mProgress;

    private ButtonFunction buttonFunction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailLayout = findViewById(R.id.email_layout);
        mEmailInput = findViewById(R.id.email_input);

        mPasswordLayout = findViewById(R.id.password_layout);
        mPasswordInput = findViewById(R.id.password_input);

        mButton = findViewById(R.id.button);

        mProgress = findViewById(R.id.progress);

        buttonFunction = ButtonFunction.REGISTRAZIONE;

        // Setup dell'input della mail
        mEmailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean validEmail = validateEmail(s.toString());
                if(!validEmail){ // Indirizzo email non valido
                    mButton.setEnabled(false);
                    mEmailLayout.setError("L'indirizzo email non è valido");
                    Utils.animateAlpha(mProgress, 0f, "short");
                }else{ // Indirizzo email valido, procedo a verifica se esiste l'account
                    mEmailLayout.setErrorEnabled(false);
                    checkExistingAccount();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Setup del click del pulsante
        mButton.setOnClickListener((v)->{
            switch(buttonFunction){
                case LOGIN:
                    break;
                case REGISTRAZIONE:
                    break;
            }
        });

    }

    private void checkExistingAccount(){
        Utils.animateAlpha(mProgress, 0f, "short");
        Utils.animateAlpha(mProgress, 1f, "medium"); // mostro la ProgressBar in alto per mostrare che sto cercando se l'account esiste
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                Utils.animateAlpha(mProgress, 0f, "short"); // nascondo la ProgressBar
                runOnUiThread(()->mButton.setEnabled(true)); // attivo il pulsante

                // MAIL TROVATA
                buttonFunction = ButtonFunction.LOGIN;
                mButton.setText("Login");

                runOnUiThread(()->mPasswordLayout.setVisibility(View.VISIBLE));
                Utils.animateAlpha(mPasswordLayout, 1f, "medium");
            }
        }, 3000);
    }

    private boolean validateEmail(String email){
        return EmailValidator.getInstance().isValid(email);
    }
}