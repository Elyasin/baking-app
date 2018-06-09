package bakingapp.example.com.db.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import bakingapp.example.com.retrofit.model.RecipeStep;

@Entity(tableName = "step",
        foreignKeys = {
                @ForeignKey(entity = Recipe.class,
                        parentColumns = "id",
                        childColumns = "recipe_id",
                        onDelete = ForeignKey.CASCADE)},
        indices = @Index("recipe_id"))
public class Step {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "step_id")
    public int stepId;

    @ColumnInfo(name = "recipe_id")
    public int recipeID;

    @ColumnInfo(name = "short_descr")
    public String shortDescription;

    public String description;

    @ColumnInfo(name = "video_url")
    public String videoURL;

    @ColumnInfo(name = "thumbnail_url")
    public String thumbnailURL;

    /*
     * constructor
     */
    public Step(int recipeID, int stepId, String shortDescription,
                String description, String videoURL, String thumbnailURL) {
        this.recipeID = recipeID;
        this.stepId = stepId;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }

    /*
     * Constructor for Retrofit model
     */
    public Step(int recipeID, RecipeStep step) {
        this.recipeID = recipeID;
        this.stepId = step.getStepId();
        this.shortDescription = step.getShortDescription();
        this.description = step.getDescription();
        this.videoURL = step.getVideoURL();
        this.thumbnailURL = step.getThumbnailURL();
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

    public int getStepId() {
        return stepId;
    }

    public void setStepId(int stepId) {
        this.stepId = stepId;
    }

    public int getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(int recipeID) {
        this.recipeID = recipeID;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }
}
