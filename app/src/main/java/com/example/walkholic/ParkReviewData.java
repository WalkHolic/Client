package com.example.walkholic;

public class ParkReviewData {


    private String username;
    double score;
    private String txt;
    private String png_path;

    public ParkReviewData(String username, double score, String txt, String png_path){
        this.username = username;
        this.score = score;
        this.txt = txt;
        this.png_path = png_path;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public String getPng_path() {
        return png_path;
    }

    public void setPng_path(String png_path) {
        this.png_path = png_path;
    }
}
