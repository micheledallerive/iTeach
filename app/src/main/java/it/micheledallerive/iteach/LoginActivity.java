package it.micheledallerive.iteach;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.apache.commons.validator.routines.EmailValidator;
import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout mEmailLayout;
    private TextInputEditText mEmailInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailLayout = findViewById(R.id.email_layout);
        mEmailInput = findViewById(R.id.email_input);

        mEmailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println(validateEmail(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean validateEmail(String email){
        return EmailValidator.getInstance().isValid(email);
    }
}