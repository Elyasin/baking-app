package bakingapp.example.com.db.model;

import android.arch.persistence.room.Relation;

import java.util.List;

public class RecipeWithRelations extends Recipe {

    @Relation(parentColumn = "id", entityColumn = "recipe_id", entity = Ingredient.class)
    public List<Ingredient> ingredients;

    @Relation(parentColumn = "id", entityColumn = "recipe_id", entity = Step.class)
    public List<Step> steps;

    public RecipeWithRelations(int id, String name, int servings, String image) {
        super(id, name, servings, image);
    }

}
