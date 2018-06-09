package bakingapp.example.com.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import bakingapp.example.com.db.model.Ingredient;

@Dao
public interface IngredientDAO {


    @Query("select * from ingredients where recipe_id = :recipeID")
    public List<Ingredient> loadAllIngredientsByRecipe(int recipeID);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAllIngredients(Ingredient... ingredients);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertIngredient(Ingredient ingredient);


}

