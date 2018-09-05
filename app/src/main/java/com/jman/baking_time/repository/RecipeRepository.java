package com.jman.baking_time.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;


import com.jman.baking_time.models.Recipe;
import com.jman.baking_time.remoteDataSource.WebService;
import com.jman.baking_time.remoteDataSource.WebServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Justin on 30/08/2018.
 */

public class RecipeRepository {

    /* The client to implement the interface in retrofit object to make requests*/
    private WebService apiClient;
    List<Recipe> recipes;

    public LiveData<List<Recipe>> getRecipes() {

        /* List of recipes */
        final MutableLiveData<List<Recipe>> data = new MutableLiveData<>();

        // assign retrofit client object that implements the WebService interface
        apiClient = WebServiceGenerator.createService();

        /* declare and assign a call object to make the request*/
        Call<List<Recipe>> networkRequest = apiClient.getRecipesJson();

        networkRequest.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                 data.setValue(response.body());

              // recipes =  response.body();
                //recipes.size();
                Log.d("response", response.body().toString());

            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Toast.makeText(null,
                        "network request failed",
                        Toast.LENGTH_LONG).show();
                t.getStackTrace();


            }
        });


        return data;
    }


}
