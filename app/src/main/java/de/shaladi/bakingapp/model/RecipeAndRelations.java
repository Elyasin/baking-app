package de.shaladi.bakingapp.model;

import android.arch.persistence.room.Relation;

import java.util.List;

/**
 * Not much data to deal with. So just get it all.
 */
public class RecipeAndRelations extends Recipe {

    //TODO Hmmm, this will include the private List<?> meant for Retrofit
    // not so good
    //public Recipe recipe;

    @Relation(parentColumn = "id", entityColumn = "recipe_id", entity = Ingredient.class)
    private List<Ingredient> ingredients;


    @Relation(parentColumn = "id", entityColumn = "recipe_id", entity = Step.class)
    private List<Step> steps;

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }
}
