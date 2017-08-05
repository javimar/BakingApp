package eu.javimar.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;


public class Ingredient implements Parcelable
{
    private double mQuantity;
    private String mMeasureUnit;
    private String mIngredientName;


    public Ingredient(double mQuantity, String mMeasureUnit, String mIngredientName)
    {
        this.mQuantity = mQuantity;
        this.mMeasureUnit = mMeasureUnit;
        this.mIngredientName = mIngredientName;
    }

    public double getmQuantity() {
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
        dest.writeDouble(this.mQuantity);
        dest.writeString(this.mMeasureUnit);
        dest.writeString(this.mIngredientName);
    }

    protected Ingredient(Parcel in) {
        this.mQuantity = in.readDouble();
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
