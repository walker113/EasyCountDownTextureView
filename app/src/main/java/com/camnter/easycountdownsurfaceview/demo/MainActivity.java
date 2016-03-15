package com.camnter.easycountdownsurfaceview.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.camnter.easycountdownsurfaceview.EasyCountDownSurfaceView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EasyCountDownSurfaceView baseSv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.baseSv = (EasyCountDownSurfaceView) this.findViewById(R.id.base_sv);
        this.findViewById(R.id.start).setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                this.baseSv.start();
                break;
        }
    }

}
