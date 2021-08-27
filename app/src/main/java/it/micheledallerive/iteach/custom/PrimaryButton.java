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

public class PrimaryButton extends FrameLayout {

    MaterialButton mButton;
    ShadowLayout mShadowLayout;

    public PrimaryButton (Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateLayout();

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PrimaryButton, 0, 0);
        String text="";
        int backgroundTint=-1;
        try{
            text = a.getString(R.styleable.PrimaryButton_text);
            backgroundTint = a.getColor(R.styleable.PrimaryButton_backgroundTint, getResources().getColor(R.color.colorPrimary, context.getTheme()));
        }catch(Exception e){e.printStackTrace();
        }

        mButton = findViewById(R.id.material_button);
        mShadowLayout = findViewById(R.id.shadowLayout);

        mButton.setText(text);
        setButtonColor(mButton, backgroundTint);
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
        int[][] states = new int[][] {
                new int[] { android.R.attr.state_enabled}, // enabled
                new int[] {-android.R.attr.state_enabled}, // disabled
                new int[] {-android.R.attr.state_checked}, // unchecked
                new int[] { android.R.attr.state_pressed}  // pressed
        };

        int[] colors = new int[] {
                color,
                color,
                color,
                color
        };
        button.setBackgroundTintList(new ColorStateList(states,colors));
        mShadowLayout.setShadowColor(ColorUtils.setAlphaComponent(color, 153));
    }
}
