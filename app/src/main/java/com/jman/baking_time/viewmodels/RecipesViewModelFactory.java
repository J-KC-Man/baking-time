package com.jman.baking_time.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.jman.baking_time.repository.RecipeRepository;

import javax.inject.Inject;

public class RecipesViewModelFactory implements ViewModelProvider.Factory {

    final RecipeRepository recipeRepository;

   // @Inject
    public RecipesViewModelFactory(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new RecipesViewModel(recipeRepository);
    }
}
