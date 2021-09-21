package it.micheledallerive.iteach.adapters;

import android.app.Activity;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.micheledallerive.iteach.R;
import it.micheledallerive.iteach.tutor.TutorCreateAnnuncioFragment;
import it.micheledallerive.iteach.utils.BaseActivity;
import it.micheledallerive.iteach.utils.Utils;

public class TutorAnnunciAdapter extends RecyclerView.Adapter<TutorAnnunciAdapter.ViewHolder> {

    private Map<String, Map<String, Object>> dataSet;
    private BaseActivity activity;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View view) {
            super(view);
        }

        public View getView(){
            return this.itemView;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public TutorAnnunciAdapter(Map<String, Map<String, Object>> dataSet, BaseActivity a) {
        this.dataSet = dataSet;
        this.activity = a;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_tutor_annuncio, viewGroup, false);

        view.setOnClickListener(v -> {
            System.out.println(v);
            int index = viewGroup.indexOfChild(v);
            String annuncioId = (String)dataSet.keySet().toArray()[index];
            activity.replaceFragment(TutorCreateAnnuncioFragment.getInstance(annuncioId), true);
        });

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        Map<String, Object> data = dataSet.get(dataSet.keySet().toArray()[position]);
        View view = viewHolder.getView();

        TextView mTitoloText = view.findViewById(R.id.titolo_text);
        TextView mMateriaText = view.findViewById(R.id.materia_text);
        ChipGroup chips = view.findViewById(R.id.chips);

        mTitoloText.setText((String)data.get("titolo"));
        mMateriaText.setText((String)data.get("materia"));

        List<String> sonoDisponibile = (List<String>) data.get("sonoDisponibile");
        List<String> offroLezioni = (List<String>) data.get("offroLezioni");
        sonoDisponibile.addAll(offroLezioni);

        for(String s : sonoDisponibile){
            boolean contains=false;
            for(int i=0;i<chips.getChildCount();i++){
                if(((Chip)chips.getChildAt(i)).getText().equals(s))
                    contains=true;
            }
            if(!contains){
                Chip chip = new Chip(new ContextThemeWrapper(view.getContext(), R.style.Chip_Choice),null,0);
                chip.setText(s);
                chip.setChipBackgroundColor(Utils.createColorStateList(view.getContext().getColor(R.color.colorPrimary)));
                chip.setTextColor(view.getContext().getColor(R.color.textWhite));
                chips.addView(chip);
            }
        }
    }

    public void setData(Map<String, Map<String, Object>> data){
        // controllo se ci sono elementi da aggiungere
        for(String id : data.keySet()){
            if(!dataSet.containsKey(id)){
                dataSet.put(id, data.get(id));
                notifyItemInserted(dataSet.keySet().size()-1);
            }
        }

        // controllo se ci sono elementi da rimuovere
        for(String id : dataSet.keySet()){
            if(!data.containsKey(id)){
                dataSet.remove(id);
                notifyItemRemoved((new ArrayList<>(dataSet.keySet())).indexOf(id));
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
