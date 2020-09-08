package com.example.planttree.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.planttree.R;
import com.example.planttree.provider.PlantContract;

public class AddPlantActivity extends AppCompatActivity {

    private RecyclerView mTypeRecyclerview;
    private PlantTypeAdapter mTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);

        mTypeAdapter = new PlantTypeAdapter(this);
        mTypeRecyclerview = findViewById(R.id.plant_types_rv);
        mTypeRecyclerview.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mTypeRecyclerview.setAdapter(mTypeAdapter);
    }

    public void onPlantTypeClick(View view) {
        ImageView imgView = view.findViewById(R.id.plant_type_iv);
        int plantType = (int) imgView.getTag();
        long timeNow = System.currentTimeMillis();

        //insert into the db
        ContentValues contentValues = new ContentValues();
        contentValues.put(PlantContract.PlantEntry.COLUMN_PLANT_TYPE, plantType);
        contentValues.put(PlantContract.PlantEntry.COLUMN_CREATION_TIME, timeNow);
        contentValues.put(PlantContract.PlantEntry.COLUMN_LAST_WATERED_TIME, timeNow);
        getContentResolver().insert(PlantContract.PlantEntry.CONTENT_URI, contentValues);
        finish();
    }

    public void onBackButtonClick(View view) {
        finish();
    }
}