package com.example.planttree;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

import com.example.planttree.provider.PlantContract;
import com.example.planttree.ui.MainActivity;
import com.example.planttree.ui.PlantDetailActivity;

/**
 * Implementation of App Widget functionality.
 */
public class PlantWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int imgRes,
                                long plantId,
                                boolean showWater,
                                int appWidgetId) {
        //change the type of view depending upon the width
        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        int width = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH);
        RemoteViews rv;
        if (width < 300) {
            rv = getSinglePlantRemoteView(context, imgRes, plantId, showWater);
        } else {
            rv = getGridPlantRemoteView(context);
        }

        //update the widget
        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        PlantWateringService.startActionUpdatePlantWidgets(context);
    }

    public static void updatePlantWidget(Context context, AppWidgetManager appWidgetManager, int imgRes, long plantId,
                                         boolean showWater,
                                         int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, imgRes, plantId, showWater, appWidgetId);
        }
    }


    // ========================= VIEWS ===================================

    private static RemoteViews getGridPlantRemoteView(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_grid_view);
        Intent intent = new Intent(context, GridWidgetService.class);
        views.setRemoteAdapter(R.id.widget_grid_view, intent);
        //plant detail activity
        Intent appIntent = new Intent(context, PlantDetailActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getService(context, 0, appIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_grid_view, appPendingIntent);
        views.setEmptyView(R.id.widget_grid_view, R.id.empty_view);
        return views;
    }

    private static RemoteViews getSinglePlantRemoteView(Context context, int imgRes, long plantId,
                                                        boolean showWater) {
        //create intent to open main activity
        Intent mainIntent;
        if (plantId == PlantContract.INVALID_PLANT_ID) {
            mainIntent = new Intent(context, MainActivity.class);
        } else {
            mainIntent = new Intent(context, PlantDetailActivity.class);
            mainIntent.putExtra("plantId", plantId);
        }
        PendingIntent mainPendingIntent = PendingIntent.getActivity(context, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //view create
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.plant_widget);
        //update id        //update image
        views.setTextViewText(R.id.widget_plant_name, String.valueOf(plantId));
        views.setImageViewResource(R.id.widget_plant_image, imgRes);
        //show water button
        if (showWater) {
            views.setViewVisibility(R.id.widget_water_button, View.VISIBLE);
        } else {
            views.setViewVisibility(R.id.widget_water_button, View.INVISIBLE);
        }

        views.setOnClickPendingIntent(R.id.widget_plant_image, mainPendingIntent);

        //create intent to water plants
        Intent wateringIntent = new Intent(context, PlantWateringService.class);
        wateringIntent.setAction(PlantWateringService.ACTION_WATER_PLANT);
        wateringIntent.putExtra(PlantWateringService.EXTRA_PLANT_ID, plantId);
        PendingIntent wateringPendingIntent = PendingIntent.getService(context, 0, wateringIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //set intent
        views.setOnClickPendingIntent(R.id.widget_water_button, wateringPendingIntent);
        return views;
    }


    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        PlantWateringService.startActionUpdatePlantWidgets(context);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {

    }
}

