package com.jman.baking_time.remoteDataSource;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Justin on 29/08/2018.
 *
 * Builds the query URL from the WebService interface
 */

public class WebServiceGenerator {

    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/";

    public static WebService createService() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
// add your other interceptors …

// add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!

        Retrofit builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        return builder.create(WebService.class);
    }
}
