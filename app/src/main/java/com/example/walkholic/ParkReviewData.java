package com.example.walkholic;

public class ParkReviewData {


    private String username;
    private String txt;
    private String png_path;

    public ParkReviewData(String username, String txt, String png_path){
        this.username = username;
        this.txt = txt;
        this.png_path = png_path;
    }

    public String getusername()
    {
        return this.username;
    }

    public String gettxt()
    {
        return this.txt;
    }

    public String getpng_path()
    {
        return this.png_path;
    }
}
