package de.shaladi.bakingapp.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "steps",
        foreignKeys = {
                @ForeignKey(entity = Recipe.class,
                        parentColumns = "id",
                        childColumns = "recipe_id",
                        onDelete = ForeignKey.CASCADE)},
        indices = @Index("recipe_id"))
public class Step {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @SerializedName("id")
    @Expose
    @ColumnInfo(name = "step_no")
    public int stepNo;

    @ColumnInfo(name = "recipe_id")
    public int recipeID;

    @SerializedName("shortDescription")
    @Expose
    @ColumnInfo(name = "short_descr")
    public String shortDescription;

    @SerializedName("description")
    @Expose
    public String description;

    @SerializedName("videoURL")
    @Expose
    @ColumnInfo(name = "video_url")
    public String videoURL;

    @SerializedName("thumbnailURL")
    @Expose
    @ColumnInfo(name = "thumbnail_url")
    public String thumbnailURL;


    /**
     * No args constructor for use in serialization
     */
    @Ignore
    public Step() {
    }

    public Step(Integer id, String shortDescription, String description, String videoURL, String thumbnailURL) {
        super();
        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
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

    public int getStepNo() {
        return stepNo;
    }

    public void setStepNo(int stepNo) {
        this.stepNo = stepNo;
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
