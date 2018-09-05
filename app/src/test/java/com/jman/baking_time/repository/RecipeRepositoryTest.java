package com.jman.baking_time.repository;

import android.arch.lifecycle.LiveData;

import com.jman.baking_time.models.Recipe;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Justin on 02/09/2018.
 */
public class RecipeRepositoryTest {
    @Test
    public void getRecipes() throws Exception {

        RecipeRepository repository = new RecipeRepository();

        LiveData<List<Recipe>> data = repository.getRecipes();

    }

}