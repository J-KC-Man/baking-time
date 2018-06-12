package com.jman.baking_time;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jman.baking_time.models.Recipes;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;

import static com.jman.baking_time.data.JsonData.json;

public class MainActivity extends AppCompatActivity {

    private List<Recipes> recipesList;


    public MainActivity() throws FileNotFoundException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Type recipesListType = new TypeToken<List<Recipes>>(){}.getType();

        recipesList = new Gson().fromJson(json, recipesListType);

    }

}
