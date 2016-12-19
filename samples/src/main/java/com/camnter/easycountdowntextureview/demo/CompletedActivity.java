package com.camnter.easycountdowntextureview.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.camnter.easycountdowntextureview.EasyCountDownTextureView;

/**
 * Description：CompletedActivity
 * Created by：CaMnter
 */

public class CompletedActivity extends AppCompatActivity
    implements EasyCountDownTextureView.EasyCountDownListener {

    private static final long DURATION_TIME = 6 * 1000;
    private static final String TAG = CompletedActivity.class.getSimpleName();


    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_completed);
        EasyCountDownTextureView easyCountDownTextureView
            = (EasyCountDownTextureView) this.findViewById(R.id.completed_countdown_text);
        easyCountDownTextureView.setTime(DURATION_TIME);
        easyCountDownTextureView.setEasyCountDownListener(this);
        easyCountDownTextureView.start();
    }


    /**
     * When count down start
     */
    @Override public void onCountDownStart() {

    }


    /**
     * When count down stop
     *
     * @param millisInFuture millisInFuture
     */
    @Override public void onCountDownStop(long millisInFuture) {

    }


    /**
     * When count down completed
     */
    @Override public void onCountDownCompleted() {
        Toast.makeText(this,"[" + TAG + "]      [onCountDownCompleted]",Toast.LENGTH_LONG).show();
    }

}
