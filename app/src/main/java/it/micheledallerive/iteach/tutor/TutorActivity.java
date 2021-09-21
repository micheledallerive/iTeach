package it.micheledallerive.iteach.tutor;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.graphics.Point;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.gigamole.library.ShadowLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import it.micheledallerive.iteach.R;
import it.micheledallerive.iteach.tutor.profile.TutorProfileFragment;
import it.micheledallerive.iteach.utils.BaseActivity;

public class TutorActivity extends BaseActivity {

    BottomNavigationView mNavigationView;
    ShadowLayout mShadowLayout;
    FrameLayout mFrameLayout;
    boolean layoutFixed=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor);

        mNavigationView = findViewById(R.id.navigation_bar);
        mShadowLayout = findViewById(R.id.shadowLayout);
        mFrameLayout = findViewById(R.id.fragment_container);

        mShadowLayout.setPadding(0,mShadowLayout.getPaddingTop(),0,0);

        mFrameLayout.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if(!layoutFixed){
                ConstraintLayout.LayoutParams params = ((ConstraintLayout.LayoutParams)mFrameLayout.getLayoutParams());
                Point size = new Point();
                getWindowManager().getDefaultDisplay().getSize(size);
                params.height = size.y-mShadowLayout.getHeight()+mShadowLayout.getPaddingTop();
                mFrameLayout.setLayoutParams(params);
                layoutFixed=true;
            }
        });

        mNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment = null;
            switch(item.getItemId()){
                case R.id.icon_annunci:
                    fragment = TutorAnnunciFragment.getInstance();
                    break;
                case R.id.icon_chat:
                    fragment = TutorChatFragment.getInstance();
                    break;
                case R.id.icon_recensioni:
                    fragment = TutorRecensioniFragment.getInstance();
                    break;
                case R.id.icon_profile:
                    fragment = TutorProfileFragment.getInstance();
                    break;
            }
            if(savedInstanceState!=null) return true;
            replaceFragment(fragment, false);
            return true;
        });
        mNavigationView.setSelectedItemId(R.id.icon_annunci);
    }
}