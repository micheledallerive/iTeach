package it.micheledallerive.iteach.utils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import it.micheledallerive.iteach.R;

public class BaseActivity extends AppCompatActivity {

    public void replaceFragment(Fragment fragment, boolean addToBackStack){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment);
        if(addToBackStack) transaction.addToBackStack("");
        transaction.commit();
    }

}
