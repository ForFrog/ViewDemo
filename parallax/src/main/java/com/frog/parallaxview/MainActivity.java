package com.frog.parallaxview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemAdapter adapter = new ItemAdapter(this, itemList());
        recyclerView.setAdapter(adapter);
    }

    private List<Item> itemList() {
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            if (i % 2 == 0) {
                items.add(new Item(R.mipmap.ic_launcher, "赵丽颖"));
            } else {
                items.add(new Item(R.mipmap.ic_launcher_round, "赵丽颖"));
            }
        }
        return items;
    }
}
