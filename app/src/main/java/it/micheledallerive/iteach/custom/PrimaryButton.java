package it.micheledallerive.iteach.custom;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;

import com.gigamole.library.ShadowLayout;
import com.google.android.material.button.MaterialButton;

import it.micheledallerive.iteach.R;
import it.micheledallerive.iteach.Utils;

public class PrimaryButton extends FrameLayout {

    MaterialButton mButton;
    ShadowLayout mShadowLayout;

    public PrimaryButton (Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateLayout();

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PrimaryButton, 0, 0);
        String text="";
        int backgroundTint=-1;
        boolean enabled=true;
        try{
            text = a.getString(R.styleable.PrimaryButton_text);
            backgroundTint = a.getColor(R.styleable.PrimaryButton_backgroundTint, getResources().getColor(R.color.colorPrimary, context.getTheme()));
            enabled = a.getBoolean(R.styleable.PrimaryButton_enabled, true);
        }catch(Exception e){e.printStackTrace();
        }

        mButton = findViewById(R.id.material_button);
        mShadowLayout = findViewById(R.id.shadowLayout);

        mButton.setText(text);
        setButtonColor(mButton, backgroundTint);
        mButton.setEnabled(enabled);
    }

    protected void inflateLayout(){
        inflate(getContext(), R.layout.primary_button, this);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
        findViewById(R.id.material_button).setOnClickListener(l);
    }

    protected void setButtonColor(MaterialButton button, int color){
        ColorStateList colors = Utils.createColorStateList(color);
        button.setBackgroundTintList(colors);
        mShadowLayout.setShadowColor(ColorUtils.setAlphaComponent(color, 125));
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mButton.setEnabled(enabled);
    }

    public void setText(String text){
        mButton.setText(text);
    }
}
