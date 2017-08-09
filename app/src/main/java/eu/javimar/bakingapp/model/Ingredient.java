package eu.javimar.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;


public class Ingredient implements Parcelable
{
    private String mQuantity;
    private String mMeasureUnit;
    private String mIngredientName;


    public Ingredient(String mQuantity, String mMeasureUnit, String mIngredientName)
    {
        this.mQuantity = mQuantity;
        this.mMeasureUnit = mMeasureUnit;
        this.mIngredientName = mIngredientName;
    }

    public String getmQuantity() {
        return mQuantity;
    }

    public String getmMeasureUnit() {
        return mMeasureUnit;
    }

    public String getmIngredientName() {
        return mIngredientName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mQuantity);
        dest.writeString(this.mMeasureUnit);
        dest.writeString(this.mIngredientName);
    }

    protected Ingredient(Parcel in) {
        this.mQuantity = in.readString();
        this.mMeasureUnit = in.readString();
        this.mIngredientName = in.readString();
    }

    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel source) {
            return new Ingredient(source);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

}
