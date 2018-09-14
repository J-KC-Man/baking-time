package com.jman.baking_time.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.jman.baking_time.models.Recipe;
import com.jman.baking_time.repository.RecipeRepository;

import java.util.List;

import javax.inject.Inject;

public class RecipesViewModel extends ViewModel {

    private LiveData<List<Recipe>> recipes;
    private RecipeRepository recipeRepository;

    // Instructs Dagger 2 to provide the RecipeRepository parameter.
    @Inject
    public RecipesViewModel(RecipeRepository recipeRepository) {

        this.recipeRepository = recipeRepository;

        if(this.recipes == null) {
            this.recipes = this.recipeRepository.getRecipes();
        }

    }

    public LiveData<List<Recipe>> getRecipes() {
        return this.recipes;
    }


}
