package bakingapp.example.com.db.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import bakingapp.example.com.retrofit.model.IngredientApi;

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

    public String ingredient;

    public Float quantity;

    public String measure;

    /*
     * Constructor
     */
    public Ingredient(int recipeID, String ingredient, float quantity, String measure) {
        this.recipeID = recipeID;
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.measure = measure;
    }

    /*
     * Constructor for Retrofit model
     */
    public Ingredient(int recipeID, IngredientApi ingredient) {
        this.recipeID = recipeID;
        this.ingredient = ingredient.getIngredient();
        this.quantity = ingredient.getQuantity();
        this.measure = ingredient.getMeasure();
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
