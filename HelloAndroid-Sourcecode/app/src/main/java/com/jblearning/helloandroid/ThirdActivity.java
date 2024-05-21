package com.jblearning.helloandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.DividerItemDecoration;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class ThirdActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseHelper dbHelper;
    private ImageView expandedImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third2);

        recyclerView = findViewById(R.id.recycler_view_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        dbHelper = new DatabaseHelper(this);
        List<WeightEntry> weightList = dbHelper.getAllWeights();

        HistoryAdapter HistoryAdapter = new HistoryAdapter(this, weightList, expandedImageView);
        recyclerView.setAdapter(HistoryAdapter);

        Button btnFirst = findViewById(R.id.HomeActivityBtn);
        btnFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThirdActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button btnSecond = findViewById(R.id.AddActivityBtn);
        btnSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThirdActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });
        Button btnFourth = findViewById(R.id.AccountActivityBtn);
        btnFourth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThirdActivity.this, Fourth2Activity.class);
                startActivity(intent);
            }
        });
    }
}



