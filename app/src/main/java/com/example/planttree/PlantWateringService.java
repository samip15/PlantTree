package com.example.planttree;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.Nullable;

import com.example.planttree.provider.PlantContract;
import com.example.planttree.utils.PlantUtils;

public class PlantWateringService extends IntentService {

    public static final String ACTION_WATER_PLANT = "np.com.manishtuladhar.planttree.action.water_plant";
    public static final String ACTION_UPDATE_PLANTS_WIDGET = "np.com.manishtuladhar.planttree.action.update_plants_widget";
    public static final String EXTRA_PLANT_ID = "np.com.manishtuladhar.planttree.extra.PLANT_ID";

    public PlantWateringService() {
        super("PlantWateringService");

    }

    /**
     * Starts the service to perform watering
     */
    public static void startActionWaterPlant(Context context, long plantId) {
        Intent intent = new Intent(context, PlantWateringService.class);
        intent.setAction(ACTION_WATER_PLANT);
        intent.putExtra(EXTRA_PLANT_ID, plantId);
        context.startService(intent);
    }

    /**
     * Starts the service to perform update plant widget
     */
    public static void startActionUpdatePlantWidgets(Context context) {
        Intent intent = new Intent(context, PlantWateringService.class);
        intent.setAction(ACTION_UPDATE_PLANTS_WIDGET);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_WATER_PLANT.equals(action)) {
                //start watering plant task
                final long plantId = intent.getLongExtra(EXTRA_PLANT_ID, PlantContract.INVALID_PLANT_ID);
                handleActionWaterPlant(plantId);
            } else if (ACTION_UPDATE_PLANTS_WIDGET.equals(action)) {
                handleActionUpdatePlantWidget();
            }
        }
    }

    /**
     * Handle action waterplant and update the plants
     */
    private void handleActionWaterPlant(long plantId) {
        Uri SINGLE_PLANT_URI = ContentUris.withAppendedId(PlantContract.BASE_CONTENT_URI.buildUpon().appendPath(PlantContract.PATH_PLANTS)
                .build(), plantId);
        ContentValues contentValues = new ContentValues();
        long timeNow = System.currentTimeMillis();
        contentValues.put(PlantContract.PlantEntry.COLUMN_LAST_WATERED_TIME, timeNow);
        //update plants that are still alive
        getContentResolver().update(
                SINGLE_PLANT_URI,
                contentValues,
                PlantContract.PlantEntry.COLUMN_LAST_WATERED_TIME + ">?",
                new String[]{String.valueOf(timeNow - PlantUtils.MAX_AGE_WITHOUT_WATER)}
        );
        //update the widget
        startActionUpdatePlantWidgets(this);

    }

    /**
     * Handle action update plant widget
     */
    private void handleActionUpdatePlantWidget() {
        Uri PLANTS_URI = PlantContract.BASE_CONTENT_URI.buildUpon().appendPath(PlantContract.PATH_PLANTS).build();
        Cursor cursor = getContentResolver().query(
                PLANTS_URI,
                null,
                null,
                null,
                PlantContract.PlantEntry.COLUMN_LAST_WATERED_TIME
        );
        //Extract plant details
        int imgRes = R.drawable.grass;
        boolean canWater = false; // hide water button
        long plantId = PlantContract.INVALID_PLANT_ID;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            int idIndex = cursor.getColumnIndex(PlantContract.PlantEntry._ID);
            int createTimeIndex = cursor.getColumnIndex(PlantContract.PlantEntry.COLUMN_CREATION_TIME);
            int waterTimeIndex = cursor.getColumnIndex(PlantContract.PlantEntry.COLUMN_LAST_WATERED_TIME);
            int plantTypeIndex = cursor.getColumnIndex(PlantContract.PlantEntry.COLUMN_PLANT_TYPE);

            plantId = cursor.getLong(idIndex);
            int plantType = cursor.getInt(plantTypeIndex);
            long createdAt = cursor.getLong(createTimeIndex);
            long wateredAt = cursor.getLong(waterTimeIndex);
            long timeNow = System.currentTimeMillis();
            cursor.close();

            canWater = (timeNow-wateredAt)>PlantUtils.MIN_AGE_BETWEEN_WATER && (timeNow - wateredAt) < PlantUtils.MAX_AGE_WITHOUT_WATER;
            imgRes = PlantUtils.getPlantImgRes(this,
                    timeNow - createdAt,
                    timeNow - wateredAt, plantType);
        }
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, PlantWidget.class));

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,R.id.widget_grid_view);
        //now update the all widgets
        PlantWidget.updatePlantWidget(this,
                appWidgetManager,
                imgRes,
                plantId,
                canWater,
                appWidgetIds);
    }
}