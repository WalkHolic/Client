package com.example.walkholic.DataClass.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserRoadUpdateRequestDto {
    @Expose
    @SerializedName("trailName")
    private String trailName;

    @Expose
    @SerializedName("description")
    private String description;

    @Expose
    @SerializedName("hashtag")
    private List<String> hashtag;

    public String getTrailName() {
        return trailName;
    }

    public void setTrailName(String trailName) {
        this.trailName = trailName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public List<String> getHashtag() {
        return hashtag;
    }

    public void setHashtag(List<String> hashtag) {
        this.hashtag = hashtag;
    }

    @Override
    public String toString() {
        return "UserRoadUpdateRequestDto{" +
                "trailName='" + trailName + '\'' +
                ", description='" + description + '\'' +
                ", hashtag=" + hashtag +
                '}';
    }
}
