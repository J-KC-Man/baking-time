package com.jman.baking_time.ui;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jman.baking_time.R;
import com.jman.baking_time.models.Recipe;

import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.List;

import static com.jman.baking_time.data.JsonData.json;

public class MainActivity extends AppCompatActivity {




    public MainActivity() throws FileNotFoundException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create new instance of fragment and display using fragment manager
        RecipesFragment recipesFragment = new RecipesFragment();

        // use a FragmentManager and transaction to add fragment to the screen
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Fragment transaction
        fragmentManager.beginTransaction()
                .add(R.id.recipes_container, recipesFragment)
                .commit();

    }

}
