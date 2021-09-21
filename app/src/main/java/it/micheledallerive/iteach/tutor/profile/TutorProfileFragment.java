package it.micheledallerive.iteach.tutor.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.Map;

import it.micheledallerive.iteach.R;
import it.micheledallerive.iteach.utils.CircleTransform;

public class TutorProfileFragment extends Fragment {

    public TutorProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    ImageView mPropic;
    TextView mNome, mEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tutor_profile, container, false);
        mPropic = view.findViewById(R.id.propic);
        mNome = view.findViewById(R.id.nome_text);
        mEmail = view.findViewById(R.id.email_text);

        FirebaseStorage.getInstance().getReference().child("/propics/").child(FirebaseAuth.getInstance().getCurrentUser().getUid()+".png").getDownloadUrl().addOnCompleteListener(task1 ->{
            if(task1.isSuccessful()){
                Picasso.get().load(task1.getResult()).transform(new CircleTransform()).error(R.drawable.default_propic).into(mPropic);
            }
        });

        FirebaseFirestore.getInstance().collection("tutors").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(task->{
            if(task.isSuccessful()){
                Map<String, Object> data = task.getResult().getData();
                mNome.setText(data.get("nome")+" "+data.get("cognome"));
                mEmail.setText((String)data.get("email"));
            }
        });
        return view;
    }

    private static TutorProfileFragment instance;

    public static TutorProfileFragment getInstance(){
        if(instance==null)
            instance = new TutorProfileFragment();
        return instance;
    }
}