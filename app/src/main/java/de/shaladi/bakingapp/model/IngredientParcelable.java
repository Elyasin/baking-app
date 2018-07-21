package de.shaladi.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class IngredientParcelable implements Parcelable {


    private String ingredient;

    private Float quantity;

    private String measure;


    public String getIngredient() {
        return ingredient;
    }

    public Float getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }


    public IngredientParcelable(String ingredient, Float quantity, String measure) {
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.measure = measure;
    }

    public static ArrayList<IngredientParcelable> makeParcelable(List<Ingredient> ingredientList) {
        ArrayList<IngredientParcelable> newList = new ArrayList<>();
        for (Ingredient ingredient : ingredientList) {
            newList.add(new IngredientParcelable(
                    ingredient.getIngredient(),
                    ingredient.getQuantity(),
                    ingredient.getMeasure()
            ));
        }

        return newList;
    }

    protected IngredientParcelable(Parcel in) {
        ingredient = in.readString();
        quantity = in.readByte() == 0x00 ? null : in.readFloat();
        measure = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ingredient);
        if (quantity == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeFloat(quantity);
        }
        dest.writeString(measure);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<IngredientParcelable> CREATOR = new Parcelable.Creator<IngredientParcelable>() {
        @Override
        public IngredientParcelable createFromParcel(Parcel in) {
            return new IngredientParcelable(in);
        }

        @Override
        public IngredientParcelable[] newArray(int size) {
            return new IngredientParcelable[size];
        }
    };

}
