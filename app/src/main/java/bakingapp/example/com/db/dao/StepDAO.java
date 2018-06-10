package bakingapp.example.com.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import bakingapp.example.com.db.model.Step;

@Dao
public interface StepDAO {


    @Query("select * from step where recipe_id = :recipeID")
    public List<Step> loadStepsByRecipe(int recipeID);

    @Query("select * from step where recipe_id = :recipeId and step_id = :stepID")
    public Step loadStep(int recipeId, int stepID);

    @Query("select count(1) from step where recipe_id = :recipeId")
    public int loadNoOfStepsOfRecipe(int recipeId);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertStep(Step step);

}
