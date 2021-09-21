package it.micheledallerive.iteach.tutor;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.micheledallerive.iteach.R;

public class TutorRecensioniFragment extends Fragment {

    public TutorRecensioniFragment() {
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
        return inflater.inflate(R.layout.fragment_tutor_recensioni, container, false);
    }

    private static TutorRecensioniFragment instance;

    public static TutorRecensioniFragment getInstance(){
        if(instance==null)
            instance = new TutorRecensioniFragment();
        return instance;
    }
}