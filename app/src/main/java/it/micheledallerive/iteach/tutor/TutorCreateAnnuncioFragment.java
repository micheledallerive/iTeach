package it.micheledallerive.iteach.tutor;

import android.animation.Animator;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import it.micheledallerive.iteach.R;
import it.micheledallerive.iteach.enums.annuncio.OffroLezioni;
import it.micheledallerive.iteach.enums.annuncio.SonoDisponibile;
import it.micheledallerive.iteach.utils.Utils;

public class TutorCreateAnnuncioFragment extends Fragment {

    public TutorCreateAnnuncioFragment() {
        // Required empty public constructor
    }

    private String annuncioId = null;
    private boolean edit = false;

    private TextInputEditText mTitleInput;
    private TextInputEditText mMateriaInput;
    private TextInputEditText mMaxSpostamentoInput;
    private TextInputEditText mCostoSpostamentoInput;
    private TextInputEditText mIndirizzoInput;
    private TextInputEditText mPrezzoInput;

    private ConstraintLayout mSpostamentoLayout;

    private ChipGroup mOffroLezioniChips;
    private ChipGroup mSonoDisponibileChips;

    private View mConfirmButton;
    private View mDeleteButton;
    private View mCancelButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupEditMode();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tutor_create_annuncio, container, false);

        setupUI(view);
        setupChips(view);

        if(edit)
            loadData();

        return view;
    }

    private static TutorCreateAnnuncioFragment instance;

    public static TutorCreateAnnuncioFragment getInstance(String annuncioId){
        if(instance==null)
            instance = new TutorCreateAnnuncioFragment();

        Bundle b = new Bundle();
        b.putString("annuncioId", annuncioId);
        instance.setArguments(b);

        return instance;
    }


    private void setupChips(View view){
        ChipGroup mOffroLezioniChips = view.findViewById(R.id.offro_lezioni_chips);
        for(int i=0;i<mOffroLezioniChips.getChildCount();i++){
            Chip chip = (Chip)mOffroLezioniChips.getChildAt(i);
            chip.setOnClickListener(v -> {
                if(!chip.isChecked()) chip.setChecked(true);
            });
        }
    }

    private void setupEditMode(){
        if(getArguments()!=null)
            if(getArguments().containsKey("annuncioId"))
                annuncioId=getArguments().getString("annuncioId");
        if(annuncioId!=null){
            edit = true;
        }
    }

    private void setupUI(View view){
        mTitleInput = view.findViewById(R.id.title_input);
        mMateriaInput = view.findViewById(R.id.materia_input);
        mMaxSpostamentoInput = view.findViewById(R.id.max_spostamento_input);
        mCostoSpostamentoInput = view.findViewById(R.id.costo_spostamento_input);
        mIndirizzoInput = view.findViewById(R.id.indirizzo_input);
        mPrezzoInput = view.findViewById(R.id.prezzo_input);

        mSpostamentoLayout = view.findViewById(R.id.spostamento_inputs_layout);

        mOffroLezioniChips = view.findViewById(R.id.offro_lezioni_chips);
        mSonoDisponibileChips = view.findViewById(R.id.sono_disponibile_chips);

        mConfirmButton = view.findViewById(R.id.icon_confirm);
        mDeleteButton = view.findViewById(R.id.icon_delete);
        mCancelButton = view.findViewById(R.id.icon_cancel);

        if(!edit)
            mDeleteButton.setVisibility(View.GONE);

        for(int i=0;i<mSonoDisponibileChips.getChildCount();i++){
            mSonoDisponibileChips.getChildAt(i).setOnClickListener(v -> {
                List<Integer> checkedIds = mSonoDisponibileChips.getCheckedChipIds();
                if(checkedIds.contains(SonoDisponibile.PER_SPOSTARMI.getId())) {
                    if (mSpostamentoLayout.getVisibility() == View.GONE) {
                        mSpostamentoLayout.setVisibility(View.VISIBLE);
                        Utils.animateAlpha(mSpostamentoLayout, 1f, "short");
                    }
                }else {
                    if (mSpostamentoLayout.getVisibility() == View.VISIBLE) {
                        Utils.animateAlpha(mSpostamentoLayout, 0f, "short", new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                mSpostamentoLayout.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {
                            }
                        });
                    }
                }
            });
        }

        mDeleteButton.setOnClickListener(v->{
            if(edit){
                MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getContext());
                AlertDialog dialog = dialogBuilder.setTitle("Sicuro di voler eliminare l'annuncio?")
                        .setPositiveButton("Si", (dialog13, which) -> {
                            FirebaseFirestore.getInstance()
                                    .collection("tutors")
                                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .collection("annunci")
                                    .document(annuncioId)
                                    .delete()
                                    .addOnCompleteListener(task->{
                                        if(task.isSuccessful()){

                                        }
                                    });
                            dialog13.dismiss();
                            getActivity().getSupportFragmentManager().popBackStack();
                        })
                        .setNegativeButton("No", (dialog12, which) -> dialog12.dismiss())
                        .create();
                dialog.setOnShowListener(dialog1 -> {
                    Button button = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                    button.setTextSize(16);
                    button.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                });
                dialog.show();
            }
        });

        mCancelButton.setOnClickListener(v -> closeFragment());

        mConfirmButton.setOnClickListener(v -> createAnnuncio());
    }

    private void createAnnuncio(){
        if(!checkData()) return;
        Map<String, Object> data = new HashMap<>();
        data.put("titolo", mTitleInput.getText().toString());
        data.put("materia", mMateriaInput.getText().toString());

        data.put("offroLezioni", chipGroupToList(mOffroLezioniChips));
        List<String> sonoDisponibileList = chipGroupToList(mSonoDisponibileChips);
        data.put("sonoDisponibile", sonoDisponibileList);

        if(mSonoDisponibileChips.getCheckedChipIds().contains(R.id.chip_spostarmi)){
            data.put("maxSpostamento", Integer.parseInt(mMaxSpostamentoInput.getText().toString()));
            data.put("costoSpostamento", Integer.parseInt(mCostoSpostamentoInput.getText().toString()));
        }

        data.put("indirizzo", mIndirizzoInput.getText().toString());
        data.put("prezzo", Integer.parseInt(mPrezzoInput.getText().toString()));

        CollectionReference coll = FirebaseFirestore.getInstance()
                .collection("tutors")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("annunci");
        DocumentReference documentReference = (annuncioId==null)?coll.document():coll.document(annuncioId);

        documentReference
            .set(data, SetOptions.merge())
            .addOnCompleteListener(task->{
                if(task.isSuccessful()) closeFragment();
            });
    }

    private void closeFragment(){
        getActivity().getSupportFragmentManager().popBackStack();
    }

    private boolean checkData(){
        String emptyError = "Il campo non può essere vuoto";
        if(mTitleInput.getText().toString().isEmpty()){
            mTitleInput.setError(emptyError);
            return false;
        }
        if(mMateriaInput.getText().toString().isEmpty()){
            mMateriaInput.setError(emptyError);
            return false;
        }
        if(mSonoDisponibileChips.getCheckedChipIds().contains(R.id.chip_spostarmi)){
            if(mMaxSpostamentoInput.getText().toString().isEmpty()){
                mMaxSpostamentoInput.setError(emptyError);
                return false;
            }
            if(mCostoSpostamentoInput.getText().toString().isEmpty()){
                mCostoSpostamentoInput.setError(emptyError);
                return false;
            }
        }
        if(mIndirizzoInput.getText().toString().isEmpty()){
            mIndirizzoInput.setError(emptyError);
            return false;
        }
        if(mPrezzoInput.getText().toString().isEmpty()){
            mPrezzoInput.setError(emptyError);
            return false;
        }
        return true;
    }

    private List<String> chipGroupToList(ChipGroup chipGroup){
        List<Integer> selectedIds = chipGroup.getCheckedChipIds();
        List<String> selected = new ArrayList<>();
        for(Integer id : selectedIds){
            selected.add(((Chip)chipGroup.findViewById(id)).getText().toString());
        }
        return selected;
    }

    private void loadData(){
        FirebaseFirestore.getInstance()
                .collection("tutors")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("annunci")
                .document(annuncioId)
                .get()
                .addOnCompleteListener(task->{
                    if(task.isSuccessful()){
                        Map<String, Object> data = task.getResult().getData();

                        mTitleInput.setText((String)data.get("titolo"));
                        mMateriaInput.setText((String)data.get("materia"));
                        mIndirizzoInput.setText((String)data.get("indirizzo"));
                        mPrezzoInput.setText(String.valueOf((long)data.get("prezzo")));

                        List<String> offroLezioni = (List<String>) data.get("offroLezioni");
                        List<String> sonoDisponibile = (List<String>) data.get("sonoDisponibile");

                        if(sonoDisponibile.contains("Per spostarmi")){
                            mMaxSpostamentoInput.setText(String.valueOf((long)data.get("maxSpostamento")));
                            mCostoSpostamentoInput.setText(String.valueOf((long)data.get("costoSpostamento")));
                        }

                        OffroLezioni lezioni = OffroLezioni.getFromString(offroLezioni.get(0));
                        ((Chip)mOffroLezioniChips.findViewById(lezioni.getId())).setChecked(true);

                        for(String s : sonoDisponibile){
                            SonoDisponibile disponibile = SonoDisponibile.getFromString(s);
                            ((Chip)mSonoDisponibileChips.findViewById(disponibile.getId())).setChecked(true);
                        }
                    }
                });
    }

}