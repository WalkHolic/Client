package com.example.walkholic;

import java.util.List;

public class TrailListViewAdapterData {

    private String imageURL;
    private String trailName;
    private Double trailDistance;
    private String trailStart;
    private String trailEnd;
    private String trailDescription;
    private List<String> hashtags;
    private int rid;


    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }


    public List<String> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<String> hashtags) {
        this.hashtags = hashtags;
    }

    public String getTrailDescription() {
        return trailDescription;
    }

    public void setTrailDescription(String trailDescription) {
        this.trailDescription = trailDescription;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getTrailName() {
        return trailName;
    }

    public void setTrailName(String trailName) {
        this.trailName = trailName;
    }


    public Double getTrailDistance() {
        return trailDistance;
    }

    public void setTrailDistance(Double trailDistance) {
        this.trailDistance = trailDistance;
    }

    public String getTrailStart() {
        return trailStart;
    }

    public void setTrailStart(String trailStart) {
        this.trailStart = trailStart;
    }

    public String getTrailEnd() {
        return trailEnd;
    }

    public void setTrailEnd(String trailEnd) {
        this.trailEnd = trailEnd;
    }


}
