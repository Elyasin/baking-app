package de.shaladi.bakingapp.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "ingredients",
        foreignKeys = {
                @ForeignKey(entity = Recipe.class,
                        parentColumns = "id",
                        childColumns = "recipe_id",
                        onDelete = ForeignKey.CASCADE)},
        indices = @Index("recipe_id"))
public class Ingredient {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "recipe_id")
    public int recipeID;

    @SerializedName("ingredient")
    @Expose
    public String ingredient;

    @SerializedName("quantity")
    @Expose
    public Float quantity;

    @SerializedName("measure")
    @Expose
    public String measure;


    /**
     * No args constructor for use in serialization
     */
    @Ignore
    public Ingredient() {
    }

    public Ingredient(Float quantity, String measure, String ingredient) {
        super();
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
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

    public int getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(int recipeID) {
        this.recipeID = recipeID;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }
}
