package it.micheledallerive.iteach;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        goToFirstScreen();
    }

    private void goToFirstScreen(){
        Intent i = new Intent(this, FirstScreen.class);
        startActivity(i);
        finish();
    }
/*
    private void goToMain(){
        Intent i = new Intent(this, );
        startActivity(i);
    }*/
}