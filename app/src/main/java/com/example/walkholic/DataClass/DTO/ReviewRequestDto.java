package com.example.walkholic.DataClass.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewRequestDto {

    @Expose
    @SerializedName("score")
    private Double score;

    @Expose
    @SerializedName("content")
    private String content;

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "\"{" +
                "\"score\": " + score +
                ", \"content\": \"" + content + '\"' +
                "}";
    }
}
