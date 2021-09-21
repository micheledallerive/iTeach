package it.micheledallerive.iteach.tutor;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.micheledallerive.iteach.R;
import it.micheledallerive.iteach.adapters.TutorAnnunciAdapter;
import it.micheledallerive.iteach.utils.Callback;

public class TutorAnnunciFragment extends Fragment {

    public TutorAnnunciFragment() {
        // Required empty public constructor
        instance = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    TutorAnnunciAdapter adapter;

    RecyclerView mRecycler;
    View mAddButton;
    SwipeRefreshLayout mRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tutor_annunci, container, false);
        mRecycler = view.findViewById(R.id.recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        mRecycler.setLayoutManager(layoutManager);

        adapter = new TutorAnnunciAdapter(new HashMap<>(), (TutorActivity)getActivity());
        mRecycler.setAdapter(adapter);

        updateData(null);


        // premendo il bottone faccio aprire il fragment per creare l'annuncio
        mAddButton = view.findViewById(R.id.add_button);
        mAddButton.setOnClickListener(v -> {
            if(getActivity()!=null)
                ((TutorActivity) getActivity()).replaceFragment(new TutorCreateAnnuncioFragment(), true);
        });

        mRefreshLayout = view.findViewById(R.id.refresh_layout);
        mRefreshLayout.setOnRefreshListener(() -> {
            updateData(new Callback(){
                @Override
                public void onSuccess(Object obj) {
                    mRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(Object obj) {

                }
            });
        });

        return view;
    }

    private static TutorAnnunciFragment instance;
    public static TutorAnnunciFragment getInstance(){
        if(instance==null)
            instance = new TutorAnnunciFragment();
        return instance;
    }

    private void updateData(Callback callback){
        FirebaseFirestore.getInstance()
                .collection("tutors")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("annunci")
                .get()
                .addOnCompleteListener(task->{
                    if(task.isSuccessful()){
                        // OTTENGO LA LISTA DEGLI ID DEGLI ANNUNCI PUBBLICATI DAL TUTOR
                        Map<String, Map<String, Object>> annunciData = new HashMap<>();
                        for(DocumentSnapshot document : task.getResult().getDocuments()){
                            annunciData.put(document.getId(), document.getData());
                        }

                        // IMPOSTO LA LISTA DEGLI ANNUNCI NELLA RECYCLERVIEW
                        adapter.setData(annunciData);

                        if(callback!=null) callback.onSuccess(null);

                    }else if(callback!=null) callback.onFailure(task.getException().getMessage());
                });
    }

}