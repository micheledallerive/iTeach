package it.micheledallerive.iteach.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import it.micheledallerive.iteach.R;
import it.micheledallerive.iteach.utils.BaseActivity;

public class UserActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
    }
}