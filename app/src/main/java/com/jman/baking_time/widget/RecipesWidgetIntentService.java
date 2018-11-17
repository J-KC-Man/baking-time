package com.jman.baking_time.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.jman.baking_time.R;

/**
 * service that handles the communication from widget to activity
* */
public class RecipesWidgetIntentService extends IntentService {

    //public static final String ACTION_WIDGET_SET_INGREDIENTS = "android.appwidget.action.APPWIDGET_UPDATE";
    public static final String ACTION_WIDGET_SET_INGREDIENTS = "com.jman.baking_time.action.update_widget";


    public RecipesWidgetIntentService() {
        super("RecipesWidgetIntentService");
    }

    /* Updates any and all widgets */
    public static void startActionUpdateWidget(Context context) {
        Intent intent = new Intent(context, RecipesWidgetIntentService.class);
        intent.setAction(ACTION_WIDGET_SET_INGREDIENTS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();

            if (ACTION_WIDGET_SET_INGREDIENTS.equals(action)){
                handleActionUpdateWidget();
            }
        }
    }

    private void handleActionUpdateWidget() {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipesWidgetProvider.class));
        //Trigger data update to handle the GridView widgets and force a data refresh, calls onDataSetChanged in RecipesWidgetRemoteViewsService
       // appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_grid_view);
        //Now update all widgets
        RecipesWidgetProvider.updateAppWidgets(this, appWidgetManager, appWidgetIds);
    }
}
