package com.frog.viewdemo;

import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

public class UserPageActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {


    private AppBarLayout abl;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView iv;
    private SwipeRefreshLayout srlRefresh;
    private RecyclerView rvList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
        abl = (AppBarLayout) findViewById(R.id.abl);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        iv = (ImageView) findViewById(R.id.iv);
        srlRefresh = (SwipeRefreshLayout) findViewById(R.id.srl_refresh);
        srlRefresh.setOnRefreshListener(this);

        rvList = (RecyclerView) findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setAdapter(new ListAdapter());
    }

    @Override
    public void onRefresh() {
        srlRefresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                srlRefresh.setRefreshing(false);
            }
        }, 2000);
    }
}
