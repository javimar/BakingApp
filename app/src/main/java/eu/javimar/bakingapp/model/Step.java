package eu.javimar.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Step implements Parcelable {
    private int mStepId;
    private String mShortDescription;
    private String mDescription;
    private String mVideoUrl;
    private String mThumbnailUrl;

    public Step(int mStepId, String mShortDescription, String mDescription,
                String mVideoUrl, String mThumbnailUrl)
    {
        this.mStepId = mStepId;
        this.mShortDescription = mShortDescription;
        this.mDescription = mDescription;
        this.mVideoUrl = mVideoUrl;
        this.mThumbnailUrl = mThumbnailUrl;
    }

    public int getmStepId() {
        return mStepId;
    }

    public String getmShortDescription() {
        return mShortDescription;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmVideoUrl() {
        return mVideoUrl;
    }

    public String getmThumbnailUrl() {
        return mThumbnailUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mStepId);
        dest.writeString(this.mShortDescription);
        dest.writeString(this.mDescription);
        dest.writeString(this.mVideoUrl);
        dest.writeString(this.mThumbnailUrl);
    }

    protected Step(Parcel in) {
        this.mStepId = in.readInt();
        this.mShortDescription = in.readString();
        this.mDescription = in.readString();
        this.mVideoUrl = in.readString();
        this.mThumbnailUrl = in.readString();
    }

    public static final Parcelable.Creator<Step> CREATOR = new Parcelable.Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel source) {
            return new Step(source);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };
}
