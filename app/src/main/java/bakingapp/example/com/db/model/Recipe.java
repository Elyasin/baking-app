package bakingapp.example.com.db.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import bakingapp.example.com.retrofit.model.RecipeApi;

@Entity(tableName = "recipes")
public class Recipe {

    @PrimaryKey
    public int id;

    public String name;

    public int servings;

    public String image;

    /*
     * Constructor
     */
    public Recipe(int id, String name, int servings, String image) {
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.image = image;
    }

    /*
     * Constructor for Retrofit model
     */
    public Recipe(RecipeApi recipe) {
        id = recipe.getId();
        name = recipe.getName();
        servings = recipe.getServings();
        image = recipe.getImage();
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
}
