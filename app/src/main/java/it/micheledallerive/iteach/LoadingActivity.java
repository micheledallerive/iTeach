package it.micheledallerive.iteach;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
    }

    private void goToFirstScreen(){
        Intent i = new Intent(this, );
        startActivity(i);
    }

    private void goToMain(){
        Intent i = new Intent(this, );
        startActivity(i);
    }
}