package com.example.planttree.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.planttree.R;
import com.example.planttree.provider.PlantContract;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView mPlantRV;
    private PlantListAdapter mAdapter;

    //loader id
    private static final int PLANT_LOADER_ID = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPlantRV = findViewById(R.id.plants_list_rv);
        mPlantRV.setLayoutManager(new GridLayoutManager(this, 4));
        mAdapter = new PlantListAdapter(this, null);
        mPlantRV.setAdapter(mAdapter);

        getSupportLoaderManager().initLoader(PLANT_LOADER_ID, null, this);
    }

    // =================== BUTTONS =========================
    public void onAddFabClick(View view) {
        Intent intent = new Intent(this, AddPlantActivity.class);
        startActivity(intent);
    }

    public void onPlantClick(View view) {
        ImageView imgView = view.findViewById(R.id.plant_list_item_iv);
        long plantId = (long) imgView.getTag();
        Intent intent = new Intent(this, PlantDetailActivity.class);
        intent.putExtra("plantId", plantId);
        startActivity(intent);
    }

    // =================== LOADERS =========================

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Uri PLANT_URI = PlantContract.BASE_CONTENT_URI.buildUpon().appendPath(PlantContract.PATH_PLANTS).build();
        return new CursorLoader(this, PLANT_URI, null, null, null,
                PlantContract.PlantEntry.COLUMN_CREATION_TIME);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        cursor.moveToFirst();
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

}