package it.micheledallerive.iteach.tutor.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.JsonObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import it.micheledallerive.iteach.R;

public class TutorProfileQrFragment extends Fragment {

    private static TutorProfileQrFragment instance;

    public TutorProfileQrFragment() {
        instance = null;
    }

    public static TutorProfileQrFragment getInstance(){
        if(instance==null) instance = new TutorProfileQrFragment();
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    ImageView qrCodeImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tutor_profile_qr, container, false);

        qrCodeImage = view.findViewById(R.id.qr_code);

        try{
            String url = "";
            JSONObject data = new JSONObject();

            data.put("data", "https://www.google.com");
            JSONObject config = new JSONObject();
            config.put("body", "circle");
            config.put("logo", "#facebook");
            data.put("config", config);
            data.put("size", 300);
            data.put("download", false);
            data.put("file", "svg");

            HttpPost httpPost = new HttpPost(url);
            StringEntity entity = new StringEntity(data.toString(), HTTP.UTF_8);
            entity.setContentType("application/json");
            httpPost.setEntity(entity);

            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(httpPost);
            System.out.println(response.getStatusLine().toString());
        }catch(Exception e){
            e.printStackTrace();
        }

        return view;
    }
}