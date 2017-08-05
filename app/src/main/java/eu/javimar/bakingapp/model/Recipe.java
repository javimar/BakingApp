package eu.javimar.bakingapp.model;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
/**
    JSON template:
    RECIPE:
    ID:1
    NAME: "Nutella pie"
    INGREDIENTS:
    QUANTITY:4
    MEASURE:"OZ"
    INGREDIENT:"cream cheese"
        ...
    STEPS
    ID:0
    SHORT DESCRIPTION:"Intro recipe"
    DESCRIPTION:"bla bla"
    VIDEO URL:"http....mp4"
    thumbnailURL:""
        ...
    SERVINGS:8
    IMAGE:""
*/

public class Recipe implements Parcelable
{
    private int mRecipeId;
    private String mRecipeName;
    private List<Ingredient> mIngredientsList;
    private List<Step> mStepsList;
    private int mServings;
    private String mRecipeImage;

    public Recipe(int mRecipeId, String mRecipeName, List<Ingredient> mIngredientsList,
                  List<Step> mStepsList, int mServings, String mRecipeImage)
    {
        this.mRecipeId = mRecipeId;
        this.mRecipeName = mRecipeName;
        this.mIngredientsList = mIngredientsList;
        this.mStepsList = mStepsList;
        this.mServings = mServings;
        this.mRecipeImage = mRecipeImage;
    }

    public int getmRecipeId() {
        return mRecipeId;
    }

    public String getmRecipeName() {
        return mRecipeName;
    }

    public List<Ingredient> getmIngredientsList() {
        return mIngredientsList;
    }

    public List<Step> getmStepsList() {
        return mStepsList;
    }

    public int getmServings() {
        return mServings;
    }

    public String getmRecipeImage() {
        return mRecipeImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mRecipeId);
        dest.writeString(this.mRecipeName);
        dest.writeList(this.mIngredientsList);
        dest.writeList(this.mStepsList);
        dest.writeInt(this.mServings);
        dest.writeString(this.mRecipeImage);
    }

    protected Recipe(Parcel in) {
        this.mRecipeId = in.readInt();
        this.mRecipeName = in.readString();
        this.mIngredientsList = new ArrayList<Ingredient>();
        in.readList(this.mIngredientsList, Ingredient.class.getClassLoader());
        this.mStepsList = new ArrayList<Step>();
        in.readList(this.mStepsList, Step.class.getClassLoader());
        this.mServings = in.readInt();
        this.mRecipeImage = in.readString();
    }

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}
