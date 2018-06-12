package com.jman.baking_time.models;

import java.util.List;

/**
 * Created by Justin on 10/06/2018.
 */

public class Recipes {

    private String id;

    private String name;

    private List<Ingredients> ingredients;

    private List<Steps> steps;

    private String servings;

    private String image;


    public Recipes(String id, String name, List<Ingredients> ingredients, List<Steps> steps, String servings, String image) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Ingredients> getIngredients() {
        return ingredients;
    }

    public List<Steps> getSteps() {
        return steps;
    }

    public String getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }
}
