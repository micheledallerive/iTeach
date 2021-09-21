package it.micheledallerive.iteach.utils;

import android.app.Activity;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import it.micheledallerive.iteach.FirstScreen;
import it.micheledallerive.iteach.tutor.TutorActivity;
import it.micheledallerive.iteach.user.UserActivity;

public class LoginUtils {

    public enum AccountType{
        TUTOR,
        USER
    }

    public static AccountType accountType;

    public static AccountType getAccountType(){
        return accountType;
    }

    public static void getAccountType(String id, Callback callback){
        DbUtils.getReference().child("users").child(id).get().addOnCompleteListener(task->{
            if(task.isSuccessful()){
                callback.onSuccess((task.getResult().getValue(String.class).equals("tutor"))?AccountType.TUTOR:AccountType.USER);
            }else
                callback.onFailure(null);
        });
    }

    public static void checkAlreadyLoggedIn(Activity a){ // controllo se l'utente ha già fatto il login e, nel caso, lo mando alla schermata corretta
        FirebaseAuth mAuth = FirebaseAuth.getInstance(); // ottengo l'istanza di FirebaseAuth
        if(mAuth.getCurrentUser()!=null){ // se ho già fatto il login
            updateAfterLogin(a, mAuth.getCurrentUser());
        }else{
            // se non ho fatto il login lo mando alla prima schermata
            Intent i = new Intent(a, FirstScreen.class);
            a.startActivity(i);
            a.finish();
        }
    }

    public static void updateAfterLogin(Activity a, FirebaseUser user){
        System.out.println("LOGGED USER: "+user.getUid());
        FirebaseFirestore db = FirebaseFirestore.getInstance(); // ottengo l'istanza di FirebaseFirestore
        // devo riuscire a capire se è uno studente o un tutor
        // controllo se è un tutor (dovrebbero essere meno aka dovrebbe volerci meno) altrimenti è sicuramente uno studente (deve essere per forza uno dei due)
        db.collection("tutors").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                List<DocumentSnapshot> documents = task.getResult().getDocuments();
                boolean isTutor = documents.stream().anyMatch(d->d.getId().equals(user.getUid())); // controllo se ci sono documenti dei tutor che contengono l'uid dell'utente (se sì vuol dire che l'utente è un tutor)
                System.out.println("è un tutor? "+isTutor);
                Intent i = null;
                if(isTutor)
                    i = new Intent(a, TutorActivity.class);
                else
                    i = new Intent(a, UserActivity.class);
                a.startActivity(i);
                a.finish();
            }
        });
    }

}
