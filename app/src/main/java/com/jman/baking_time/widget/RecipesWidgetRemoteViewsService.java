package com.jman.baking_time.widget;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jman.baking_time.R;
import com.jman.baking_time.models.Ingredient;
import com.jman.baking_time.ui.MainActivity;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
* This service connects the remote adapter (RemoteViewsFactory) to be able to request remote views
* */
public class RecipesWidgetRemoteViewsService extends RemoteViewsService {

    // Collections.emptyList() returns immutable list, and avoids creating a new object and NPEs
    private List<Ingredient> mIngredients = Collections.emptyList();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext());
    }

    /**
     * This takes the place of a traditional adapter in a normal Grid layout
    * */
    class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context mContext;


        /*
        * Context is needed for instantiating a new factory
        * */
        public GridRemoteViewsFactory(Context applicationContext) {
            this.mContext = applicationContext;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            // get the data from persistent datastore
            Gson gson = new Gson();
            Type ingredientsListType = new TypeToken<List<Ingredient>>(){}.getType();
            mIngredients = gson.fromJson(PreferenceManager
                    .getDefaultSharedPreferences(mContext)
                    .getString(MainActivity.
                            RECIPE_INGREDIENTS_DEFAULT_SHARED_PREF,null)
                    , ingredientsListType);
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {

            if(mIngredients == null) {
                return 0;
            }

            return mIngredients.size();
        }


        /*
        * same as the onBindViewHolder method in a noraml adapter
        * */
        @Override
        public RemoteViews getViewAt(int position) {

            Ingredient ingredient = mIngredients.get(position);

            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.recipes_widget_gridview_item);

            views.setTextColor(R.id.widget_ingredient_quantity, getResources().getColor(R.color.white));

            views.setTextViewText(R.id.widget_ingredient_quantity, ingredient.getQuantity() +
                    " " + ingredient.getMeasure() + " " + ingredient.getIngredient());

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
