package com.camnter.easycountdowntextureview.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import com.camnter.easycountdowntextureview.EasyCountDownTextureView;

/**
 * Description：StyleActivity
 * Created by：CaMnter
 * Time：2016-03-17 17:20
 */
public class StyleActivity extends AppCompatActivity
        implements View.OnClickListener, EasyCountDownTextureView.EasyCountDownListener {

    private EasyCountDownTextureView styleTv;


    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_style);
        this.styleTv = (EasyCountDownTextureView) this.findViewById(R.id.style_tv);
        this.findViewById(R.id.style_hour_bt).setOnClickListener(this);
        this.findViewById(R.id.style_minute_bt).setOnClickListener(this);
        this.findViewById(R.id.style_second_bt).setOnClickListener(this);
        this.findViewById(R.id.style_start_bt).setOnClickListener(this);
        this.findViewById(R.id.style_stop_bt).setOnClickListener(this);
        this.styleTv.setEasyCountDownListener(this);
    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.style_hour_bt:
                this.styleTv.setTimeHour(1);
                break;
            case R.id.style_minute_bt:
                this.styleTv.setTimeMinute(1);
                break;
            case R.id.style_second_bt:
                this.styleTv.setTimeSecond(1);
                break;
            case R.id.style_start_bt:
                this.styleTv.start();
                break;
            case R.id.style_stop_bt:
                this.styleTv.stop();
                break;
        }
    }


    /**
     * When count down start
     */
    @Override public void onCountDownStart() {
        Toast.makeText(StyleActivity.this, "onCountDownStart", Toast.LENGTH_SHORT).show();
    }


    /**
     * When count down stop
     *
     * @param millisInFuture millisInFuture
     */
    @Override public void onCountDownStop(long millisInFuture) {
        Toast.makeText(StyleActivity.this, "onCountDownStop", Toast.LENGTH_SHORT).show();
    }
}
