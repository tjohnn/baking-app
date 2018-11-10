package com.tjohnn.baking.data.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Step implements Parcelable {

    public long id;

    public String shortDescription;

    public String description;

    @SerializedName("videoURL")
    public String videoUrl;

    @SerializedName("thumbnailURL")
    public String thumbnailUrl;


    protected Step(Parcel in) {
        id = in.readLong();
        shortDescription = in.readString();
        description = in.readString();
        videoUrl = in.readString();
        thumbnailUrl = in.readString();
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoUrl);
        dest.writeString(thumbnailUrl);
    }


    @Override
    public String toString() {
        return "Step{" +
                "id=" + id +
                ", shortDescription='" + shortDescription + '\'' +
                ", description='" + description + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                '}';
    }

}
