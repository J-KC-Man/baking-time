package com.jman.baking_time.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

/**
* This service handles
* */
public class RecipesWidgetRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext());
    }

    /**
     * This takes the place of a traditional adapter in a normal Grid layout
    * */
    class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context context;

        public GridRemoteViewsFactory(Context applicationContext) {
            this.context = applicationContext;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            return null;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 0;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}
