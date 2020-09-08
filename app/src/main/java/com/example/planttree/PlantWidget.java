package com.example.planttree;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.planttree.ui.MainActivity;

/**
 * Implementation of App Widget functionality.
 */
public class PlantWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        //create intent to open main activity
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
        //view create
        RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.plant_widget);
        //set intent
        views.setOnClickPendingIntent(R.id.widget_plant_image,pendingIntent);
        //update the widget
        appWidgetManager.updateAppWidget(appWidgetId,views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

