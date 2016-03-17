package com.camnter.easycountdowntextureview.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.camnter.easycountdowntextureview.EasyCountDownTextureView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EasyCountDownTextureView edtv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.findViewById(R.id.style_bt).setOnClickListener(this);
        this.edtv = (EasyCountDownTextureView) this.findViewById(R.id.edtv);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.style_bt:
                StyleActivity.startActivity(this);
                break;
        }
    }
}
