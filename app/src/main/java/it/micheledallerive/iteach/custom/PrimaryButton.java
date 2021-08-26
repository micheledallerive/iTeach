package it.micheledallerive.iteach.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.gigamole.library.ShadowLayout;

import it.micheledallerive.iteach.R;

import static java.security.AccessController.getContext;

public class PrimaryButton extends FrameLayout {

    public PrimaryButton (Context context, AttributeSet attrs){
        super(context, attrs);
        inflate(getContext(), R.layout.layout_primary_button, this);
    }

}
