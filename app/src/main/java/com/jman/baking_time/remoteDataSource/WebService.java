package com.jman.baking_time.remoteDataSource;

import com.jman.baking_time.models.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Justin on 28/08/2018.
 */

public interface WebService {

    /*
    * other half of the request execution mechanism:
    *
    * Converters turn bytes to objects,
    * Call adapters deal makes the network request and
    * handles request execution (sync/async, threading) using the Call object.
    * Separate call objects hold the request and response as a MovieResults object.
    * This object includes Java code that Gson looks for
    * to map the json properties to java properties. This includes list and field names.
    *
    * */

    /*
    * Methods declared on an interface represent a single remote API endpoint.
    * Annotations describe how the method maps to an HTTP request.
    * */
    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getRecipesJson();
}
