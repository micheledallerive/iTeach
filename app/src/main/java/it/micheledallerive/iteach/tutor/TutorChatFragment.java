package it.micheledallerive.iteach.tutor;

import android.animation.Animator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonParser;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import it.micheledallerive.iteach.R;
import it.micheledallerive.iteach.adapters.ChatAdapter;
import it.micheledallerive.iteach.chat.ChatMessage;
import it.micheledallerive.iteach.utils.BaseActivity;
import it.micheledallerive.iteach.utils.DbUtils;
import it.micheledallerive.iteach.utils.Utils;

public class TutorChatFragment extends Fragment {

    public TutorChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_tutor_chat, container, false);

        List<String> chatIds = new ArrayList<>();
        ChatAdapter adapter = new ChatAdapter(chatIds, (BaseActivity)getActivity(), view.findViewById(R.id.progress));

        // imposto l'adapter delle chat nel recyclerview
        RecyclerView mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setAnimationCacheEnabled(true);

        // ottengo dal database gli ids delle chat dell'utente
        DbUtils.getReference().child("userChats").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterator<DataSnapshot> iterator = snapshot.getChildren().iterator();
                while(iterator.hasNext()){
                    DataSnapshot data = iterator.next();
                    adapter.dataSet.add(0, data.getValue(String.class));
                    adapter.notifyItemInserted(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    private static TutorChatFragment instance;

    public static TutorChatFragment getInstance(){
        if(instance==null)
            instance = new TutorChatFragment();
        return instance;
    }
}