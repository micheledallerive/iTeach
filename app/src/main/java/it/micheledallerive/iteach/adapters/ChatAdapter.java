package it.micheledallerive.iteach.adapters;

import android.animation.Animator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import it.micheledallerive.iteach.R;
import it.micheledallerive.iteach.utils.BaseActivity;
import it.micheledallerive.iteach.utils.Callback;
import it.micheledallerive.iteach.utils.CircleTransform;
import it.micheledallerive.iteach.utils.DbUtils;
import it.micheledallerive.iteach.utils.LoginUtils;
import it.micheledallerive.iteach.utils.Utils;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View view){
            super(view);
        }

        public View getView(){
            return this.itemView;
        }
    }

    public List<String> dataSet;
    private BaseActivity activity;
    private View progressView;

    public ChatAdapter(List<String> chatIds, BaseActivity a, View progressView){
        this.dataSet = chatIds;
        this.activity = a;
        this.progressView = progressView;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_chat, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        View view = viewHolder.getView();

        String chatId = dataSet.get(position);
        // GUARDO CON CHI è QUESTA CHAT
        DbUtils.getReference().child("chats").child(chatId).child("members").get().addOnCompleteListener(task->{
            if(task.isSuccessful()){
                Iterator<DataSnapshot> iterator = task.getResult().getChildren().iterator();
                String receiverIdTemp = "";
                while(iterator.hasNext()){
                    String id = iterator.next().getValue(String.class);
                    if(id!=null){
                        if(!id.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            // SE L'ID è DIVERSO DALL'ID DELL'UTENTE LOGGATO, ALLORA è L'ID DEL DESTINATARIO
                            receiverIdTemp = id;
                            break;
                        }
                    }
                }
                final String receiverId = receiverIdTemp;
                // se non lo trovo per qualche strano motivo lo dico
                if(receiverId==""){
                    Toast.makeText(view.getContext(), "Errore :(", Toast.LENGTH_LONG).show();
                }else{
                    // ottengo che tipo di account è il ricevente
                    new Thread((Runnable) () -> LoginUtils.getAccountType(receiverId, new Callback() {
                        @Override
                        public void onSuccess(Object obj) {
                            LoginUtils.AccountType accountType = (LoginUtils.AccountType) obj;
                            // ora ottengo i dati della persona con cui sto parlando
                            FirebaseFirestore.getInstance().collection(accountType== LoginUtils.AccountType.TUTOR?"tutors":"users")
                                    .document(receiverId)
                                    .get()
                                    .addOnCompleteListener(task1 ->{
                                        Map<String, Object> data = task1.getResult().getData();
                                        activity.runOnUiThread(()->{((TextView)view.findViewById(R.id.nome)).setText((String)data.get("nome")+data.get("cognome"));});
                                        if(position==(getItemCount()-1)){
                                            Utils.animateAlpha(progressView, 0f, "medium", new Animator.AnimatorListener() {
                                                @Override
                                                public void onAnimationStart(Animator animation) {}
                                                @Override
                                                public void onAnimationEnd(Animator animation) {
                                                    // ora non è più visibile ma occupa ancora spazio, allora metto la visibilità a GONE
                                                    progressView.setVisibility(View.GONE);
                                                }
                                                @Override
                                                public void onAnimationCancel(Animator animation) {}
                                                @Override
                                                public void onAnimationRepeat(Animator animation) {}
                                            });
                                        }
                                    });
                        }

                        @Override
                        public void onFailure(Object obj) {

                        }
                    })).start();

                    FirebaseStorage.getInstance().getReference().child("/propics/").child(receiverId+".png").getDownloadUrl().addOnCompleteListener(task1 ->{
                        if(task1.isSuccessful()){
                            Picasso.get().load(task1.getResult()).transform(new CircleTransform()).error(R.drawable.default_propic).into((ImageView)view.findViewById(R.id.propic));
                        }
                    });
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
