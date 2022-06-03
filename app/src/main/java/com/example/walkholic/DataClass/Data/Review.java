package com.example.walkholic.DataClass.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Review {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName(value="fk", alternate={"parkId", "roadId", "userRoadId"})
    @Expose
    private Integer fk;
    @SerializedName("score")
    @Expose
    private Double score;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("pngPath")
    @Expose
    private String pngPath;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFk() {
        return fk;
    }

    public void setFk(Integer fk) {
        this.fk = fk;
    }

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

    public String getPngPath() {
        return pngPath;
    }

    public void setPngPath(String pngPath) {
        this.pngPath = pngPath;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", name='" + name + '\'' +
                ", fk=" + fk +
                ", score=" + score +
                ", content='" + content + '\'' +
                ", pngPath='" + pngPath + '\'' +
                '}';
    }
}