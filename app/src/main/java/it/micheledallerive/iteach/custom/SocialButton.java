package it.micheledallerive.iteach.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import androidx.core.graphics.ColorUtils;

import com.google.android.material.button.MaterialButton;

import it.micheledallerive.iteach.R;
import it.micheledallerive.iteach.Utils;

public class SocialButton extends PrimaryButton {

    protected ImageView mIcon;
    TypedArray a;

    public SocialButton(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        mIcon = findViewById(R.id.icon);
        a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SocialButton, 0, 0);
    }

    @Override
    protected void draw() {
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
    protected void setButtonColor(MaterialButton button, int color) {
        mButton.setStrokeColor(Utils.createColorStateList(color));
        mButton.setTextColor(color);
        mButton.setRippleColor(Utils.createColorStateList(ColorUtils.blendARGB(color, Color.BLACK, 0.3f)));
        mShadowLayout.setShadowColor(ColorUtils.setAlphaComponent(color, 130));
    }

    @Override
    protected void inflateLayout() {
        inflate(getContext(), R.layout.social_button, this);
    }
}
