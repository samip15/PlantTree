package com.example.planttree.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.planttree.R;

public class AddPlantActivity extends AppCompatActivity {

    private RecyclerView mTypeRecyclerview;
    private PlantTypeAdapter mTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);

        mTypeRecyclerview = findViewById(R.id.plant_types_rv);
        mTypeRecyclerview.setLayoutManager(
                new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mTypeRecyclerview.setAdapter(mTypeAdapter);
    }
}