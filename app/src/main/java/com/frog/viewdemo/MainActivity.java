package com.frog.viewdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_1:
                startActivity(new Intent(this, ViewActivity.class));
                break;
            case R.id.tv_2:
                startActivity(new Intent(this, UserPageActivity.class));
                break;
            case R.id.tv_3:
                startActivity(new Intent(this, ZhiFuBaoActivity.class));
                break;
        }
    }
}
