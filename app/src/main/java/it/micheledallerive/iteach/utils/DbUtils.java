package it.micheledallerive.iteach.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.micheledallerive.iteach.R;

public class DbUtils {

    public static DatabaseReference getReference(){
        return FirebaseDatabase.getInstance("https://iteach-69-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
    }

}
