package com.example.walkholic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ParkReviewAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<ParkReviewData> reviewData;

    public ParkReviewAdapter(Context context, ArrayList<ParkReviewData> data) {
        mContext = context;
        reviewData = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return reviewData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public ParkReviewData getItem(int position) {
        return reviewData.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.park_review_item, null);

        TextView user_name = (TextView)view.findViewById(R.id.user_name);
        RatingBar rating = (RatingBar) view.findViewById(R.id.reviewRating);
        TextView review_txt = (TextView)view.findViewById(R.id.review_txt);
        ImageView review_park_img = (ImageView)view.findViewById(R.id.review_park_img);

        user_name.setText(reviewData.get(position).getUsername());
        rating.setRating((float)reviewData.get(position).getScore());
        review_txt.setText(reviewData.get(position).getTxt());

        String png_path = reviewData.get(position).getPng_path();
        //review_park_img.setText(reviewData.get(position).getGrade());
        if(png_path != null){
            Glide.with(mContext.getApplicationContext()).load(png_path).into(review_park_img);
        }else{
            review_park_img.setImageResource(R.drawable.basic_park);
        }


        return view;
    }

}
