package it.micheledallerive.iteach.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButton;

import it.micheledallerive.iteach.R;

public class SocialButton extends PrimaryButton {

    ImageView mIcon;
    MaterialButton mButton;

    public SocialButton(Context context, AttributeSet attributeSet){
        super(context, attributeSet);

        mIcon = findViewById(R.id.icon);
        mButton = findViewById(R.id.material_button);

        TypedArray a = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.SocialButton, 0, 0);
        int social=-1;
        try{
            social = a.getInt(R.styleable.SocialButton_social, -1);
        }catch(Exception e){e.printStackTrace();}
        Log.e("iTeachLog", "Social: "+social);
        switch(social){
            case 0: // GOOGLE
                mIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_google, getContext().getTheme()));
                setButtonColor(mButton, getResources().getColor(R.color.google));
                mButton.setText("ACCEDI CON GOOGLE");
                break;
            case 1: // FACEBOOK
                mIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_facebook, getContext().getTheme()));
                setButtonColor(mButton, getResources().getColor(R.color.facebook));
                mButton.setText("ACCEDI CON FACEBOOK");

                break;
            case 2: // TWITTER
                mIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_twitter, getContext().getTheme()));
                setButtonColor(mButton, getResources().getColor(R.color.twitter));
                mButton.setText("ACCEDI CON TWITTER");

                break;
        }
    }

    @Override
    protected void inflateLayout() {
        inflate(getContext(), R.layout.social_button, this);
    }
}
