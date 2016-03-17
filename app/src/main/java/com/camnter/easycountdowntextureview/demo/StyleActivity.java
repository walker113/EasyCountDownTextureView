package com.camnter.easycountdowntextureview.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Description：StyleActivity
 * Created by：CaMnter
 * Time：2016-03-17 17:20
 */
public class StyleActivity extends AppCompatActivity {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, StyleActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_style);
    }
}
