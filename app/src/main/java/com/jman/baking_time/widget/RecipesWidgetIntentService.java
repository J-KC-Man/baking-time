package com.jman.baking_time.widget;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * service that handles the communication from widget to activity
* */
public class RecipesWidgetIntentService extends IntentService {


    public RecipesWidgetIntentService() {
        super("RecipesWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
