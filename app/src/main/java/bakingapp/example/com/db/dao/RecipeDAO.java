package bakingapp.example.com.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import bakingapp.example.com.db.model.Recipe;

@Dao
public interface RecipeDAO {


    @Query("select * from recipes")
    public List<Recipe> loadAllRecipes();

    @Query("select * from recipes where id = :id")
    public Recipe loadRecipe(int id);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAllRecipes(Recipe... recipes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertRecipe(Recipe recipe);


}

