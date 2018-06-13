
package bakingapp.example.com.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecipeApi {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("ingredients")
    @Expose
    private List<IngredientApi> ingredients = null;
    @SerializedName("steps")
    @Expose
    private List<StepApi> steps = null;
    @SerializedName("servings")
    @Expose
    private Integer servings;
    @SerializedName("image")
    @Expose
    private String image;

    /**
     * No args constructor for use in serialization
     */
    public RecipeApi() {
    }

    /**
     * @param ingredients
     * @param id
     * @param servings
     * @param name
     * @param image
     * @param steps
     */
    public RecipeApi(Integer id, String name, List<IngredientApi> ingredients, List<StepApi> steps, Integer servings, String image) {
        super();
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<IngredientApi> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientApi> ingredients) {
        this.ingredients = ingredients;
    }

    public List<StepApi> getSteps() {
        return steps;
    }

    public void setSteps(List<StepApi> steps) {
        this.steps = steps;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
