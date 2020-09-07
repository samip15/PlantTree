package com.example.planttree.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.planttree.R;
import com.example.planttree.provider.PlantContract;
import com.example.planttree.utils.PlantUtils;

public class PlantDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{


    long mPlantId;
    private static final int SINGLE_LOADER_ID = 200;

    //views
    ImageView plantDetailIV;
    TextView plantDetailName, plantAgeNumber, plantAgeUnit, lastWateredNumber, lastWateredUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_detail);

        //views
        plantDetailIV = findViewById(R.id.plant_detail_image);
        plantDetailName = findViewById(R.id.plant_detail_name);
        plantAgeNumber = findViewById(R.id.plant_age_number);
        plantAgeUnit = findViewById(R.id.plant_age_unit);
        lastWateredNumber = findViewById(R.id.last_watered_number);
        lastWateredUnit = findViewById(R.id.last_watered_unit);

        //get extra
        mPlantId = getIntent().getLongExtra("plantId", PlantContract.INVALID_PLANT_ID);
        getSupportLoaderManager().initLoader(SINGLE_LOADER_ID, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Uri SINGLE_PLANT_URI = ContentUris.withAppendedId(
                PlantContract.BASE_CONTENT_URI.buildUpon().appendPath(PlantContract.PATH_PLANTS).build(), mPlantId);
        return new CursorLoader(this, SINGLE_PLANT_URI, null,
                null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) return;
        cursor.moveToFirst();
        //indexes
        int createTimeIndex = cursor.getColumnIndex(PlantContract.PlantEntry.COLUMN_CREATION_TIME);
        int waterTimeIndex = cursor.getColumnIndex(PlantContract.PlantEntry.COLUMN_LAST_WATERED_TIME);
        int planTypeIndex = cursor.getColumnIndex(PlantContract.PlantEntry.COLUMN_PLANT_TYPE);

        //data
        int plantType = cursor.getInt(planTypeIndex);
        long createdAt = cursor.getLong(createTimeIndex);
        long wateredAt = cursor.getLong(waterTimeIndex);
        long timeNow = System.currentTimeMillis();

        int plantImgRes = PlantUtils.getPlantImgRes(this, timeNow - createdAt, timeNow - wateredAt, plantType);

        //set data
        plantDetailIV.setImageResource(plantImgRes);
        plantDetailName.setText(String.valueOf(mPlantId));
        plantAgeNumber.setText(String.valueOf(PlantUtils.getDisplayAgeInt(timeNow - createdAt)));
        plantAgeUnit.setText(String.valueOf(PlantUtils.getDisplayAgeUnit(this, timeNow - createdAt)));
        lastWateredNumber.setText(String.valueOf(PlantUtils.getDisplayAgeInt(timeNow - wateredAt)));
        lastWateredUnit.setText(String.valueOf(PlantUtils.getDisplayAgeUnit(this, timeNow - wateredAt)));

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}