package com.example.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tayyab on 8/2/2017.
 */

public class StepsData implements Parcelable {

    private int stepId;
    private String shortDesc;
    private String detailDesc;
    private String videoUrl;
    private String thumbUrl;

    public StepsData () {}

    private StepsData(Parcel in) {
        stepId = in.readInt();
        shortDesc = in.readString();
        detailDesc = in.readString();
        videoUrl = in.readString();
        thumbUrl = in.readString();
    }

    public static final Creator<StepsData> CREATOR = new Creator<StepsData>() {
        @Override
        public StepsData createFromParcel(Parcel in) {
            return new StepsData(in);
        }

        @Override
        public StepsData[] newArray(int size) {
            return new StepsData[size];
        }
    };

    public int getStepId() {
        return stepId;
    }

    public void setStepId(int stepId) {
        this.stepId = stepId;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getDetailDesc() {
        return detailDesc;
    }

    public void setDetailDesc(String detailDesc) {
        this.detailDesc = detailDesc;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(stepId);
        dest.writeString(shortDesc);
        dest.writeString(detailDesc);
        dest.writeString(videoUrl);
        dest.writeString(thumbUrl);
    }
}
