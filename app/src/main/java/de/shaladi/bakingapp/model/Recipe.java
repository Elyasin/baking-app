package de.shaladi.bakingapp.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * This model is for room and retrofit. Not great, but well... it is a simple app.
 * <p>
 * Gson builder uses excludeFieldsWithoutExposeAnnotation.
 */
@Entity(tableName = "recipes")
public class Recipe {

    @SerializedName("id")
    @Expose
    @PrimaryKey
    protected int id;

    @SerializedName("name")
    @Expose
    protected String name;

    @SerializedName("servings")
    @Expose
    protected int servings;

    @SerializedName("image")
    @Expose
    protected String image;


    /**
     * For retrofit. room relation in {@link RecipeAndRelations}
     */
    @Ignore
    @Expose
    @SerializedName("ingredients")
    private List<Ingredient> ingredients = null;

    /**
     * For retrofit. room relation in {@link RecipeAndRelations}
     */
    @Ignore
    @Expose
    @SerializedName("steps")
    private List<Step> steps = null;


    /**
     * No args constructor for use in serialization
     */
    @Ignore
    public Recipe() {
    }

    public Recipe(int id, String name, int servings, String image) {
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.image = image;
    }

    /*
     * Getter/Setter methods
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

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
