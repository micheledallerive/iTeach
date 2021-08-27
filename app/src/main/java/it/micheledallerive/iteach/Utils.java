package it.micheledallerive.iteach;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;

import androidx.core.graphics.ColorUtils;

public class Utils {

    public static ColorStateList createColorStateList(int color){
        int[][] states = new int[][] {
                new int[] { android.R.attr.state_enabled}, // enabled
                new int[] {-android.R.attr.state_enabled}, // disabled
                new int[] {-android.R.attr.state_checked}, // unchecked
                new int[] { android.R.attr.state_pressed}  // pressed
        };

        int[] colors = new int[] {
                color,
                ColorUtils.blendARGB(color, Color.GRAY, .5f),
                color,
                color
        };
        return new ColorStateList(states,colors);
    }

    public static void animateAlpha(View view, float toAlpha, String duration){
        int durationId=android.R.integer.config_shortAnimTime;
        switch(duration){
            case "short":
                durationId = android.R.integer.config_shortAnimTime;
                break;
            case "medium":
                durationId = android.R.integer.config_mediumAnimTime;
                break;
            case "long":
                durationId = android.R.integer.config_longAnimTime;
                break;
        }
        long durationFloat = view.getResources().getInteger(durationId);
        view.animate().alpha(toAlpha).setDuration(durationFloat).setListener(null);
    }

}
