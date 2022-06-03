package com.example.walkholic;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.walkholic.DataClass.Data.UserRoad;
import com.example.walkholic.DataClass.Data.UserRoadPath;
import com.example.walkholic.DataClass.Response.ParkRes;
import com.example.walkholic.DataClass.Response.ReviewRes;
import com.example.walkholic.DataClass.Response.RoadRes;
import com.example.walkholic.DataClass.Response.UserRoadRes;
import com.example.walkholic.DataClass.Response.UserRoadSharedRes;
import com.example.walkholic.Service.ServerRequestApi;
import com.example.walkholic.Service.ServiceGenerator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewListViewAdapter extends BaseAdapter {

    ArrayList<ReviewListViewAdapterData> list = new ArrayList<ReviewListViewAdapterData>();
    private static final int PICK_FROM_ALBUM = 1;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    Context context;
    ContentResolver contentResolver;
    UserRoad temp = new UserRoad();
    UserRoadSharedRes userRoadSharedRes = new UserRoadSharedRes();
    RoadRes roadRes = new RoadRes();
    List<UserRoad> userRoadList;
    List<UserRoadPath> userRoadPathList;
    UserRoadRes userRoadRes;
    Button btn_hash;
    Handler handler = new Handler();
    MultipartBody.Part trailImage = null;
    private Uri imageUri;
    ReviewRes reviewRes;
    private int objectType = -1;
    private int objectId = -1;
    ParkRes park;
    RoadRes road;
    UserRoadRes userRoad;

    public ReviewListViewAdapter(Context incontext) {
        this.context = incontext;
    }

    @Override
    public int getCount() {
        return list.size(); //그냥 배열의 크기를 반환하면 됨
    }

    @Override
    public Object getItem(int i) {
        return list.get(i); //배열에 아이템을 현재 위치값을 넣어 가져옴
    }

    @Override
    public long getItemId(int i) {
        return i; //그냥 위치값을 반환해도 되지만 원한다면 아이템의 num 을 반환해도 된다.
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Context context3 = viewGroup.getContext();
        preferences = PreferenceManager.getDefaultSharedPreferences(context);

        editor = preferences.edit();

        objectType = preferences.getInt("objectType", 1);
        //리스트뷰에 아이템이 인플레이트 되어있는지 확인한후
        //아이템이 없다면 아래처럼 아이템 레이아웃을 인플레이트 하고 view객체에 담는다.
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context3.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.activity_myreview, viewGroup, false);
        }

        Context context2 = view.getContext();

        //이제 아이템에 존재하는 텍스트뷰 객체들을 view객체에서 찾아 가져온다
        ImageView reviewImage = (ImageView) view.findViewById(R.id.review_Image);
        ImageView reviewProfile = (ImageView) view.findViewById(R.id.review_Profile);
        TextView reviewName = (TextView) view.findViewById(R.id.review_Name);

        TextView reviewContent = (TextView) view.findViewById(R.id.review_Content);
        TextView reviewObjectName = (TextView) view.findViewById(R.id.review_objectName) ;
        //TextView reviewScore = (TextView) view.findViewById(R.id.review_Score);
        RatingBar reviewRating = (RatingBar) view.findViewById(R.id.ratingBar);


        Button btn_delete = (Button) view.findViewById(R.id.btn_review_delete);
        Button btn_modify = (Button) view.findViewById(R.id.btn_review_modify);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            reviewName.setText(user.getDisplayName());
        }

        contentResolver = view.getContext().getContentResolver();


        //현재 포지션에 해당하는 아이템에 글자를 적용하기 위해 list배열에서 객체를 가져온다.
        com.example.walkholic.ReviewListViewAdapterData listdata = list.get(i);
        Log.d("dlgochan", "listdata url : " + listdata.getImageURL());
        //URI로 이미지 미리보기 띄우기
        if (listdata.getImageURL() != null) {
            Glide.with(context2.getApplicationContext()).load(listdata.getImageURL()).into(reviewImage);
        } else {
            Glide.with(context2.getApplicationContext()).load(R.drawable.noimages).into(reviewImage);
        }
        Glide.with(context2.getApplicationContext()).load(user.getPhotoUrl()).into(reviewProfile);
        reviewContent.setText(listdata.getReviewContent());
        //reviewScore.setText(listdata.getScore().toString());
        reviewRating.setRating(listdata.getScore().floatValue());

        //objectId = preferences.getInt("objectID",0);
        objectId = listdata.getParkID();
        if(objectType == 1)getParkById(objectId);
        else if(objectType == 2)getRoadById(objectId);
        else if(objectType == 3)getUserRoadById(objectId);

        handler.postDelayed(new Runnable() {
            public void run() {
                // 시간 지난 후 실행할 코딩
                Log.d("dlgochan", "objectType3 : " + objectType);
                if(objectType == 1)reviewObjectName.setText(park.getData().get(0).getName());
                else if(objectType == 2)reviewObjectName.setText(road.getData().get(0).getRoadName());
                else if(objectType == 3)reviewObjectName.setText(userRoad.getData().get(0).getTrailName());
            }
        }, 500); // 0.5초후


        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(i);
            }
        });
        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), ModifyReview.class);
                intent.putExtra("reviewContent", listdata.getReviewContent());
                intent.putExtra("reviewScore", listdata.getScore());
                intent.putExtra("reviewImage", listdata.getImageURL());
                intent.putExtra("objectId", listdata.getParkID());
                intent.putExtra("reviewId", listdata.getReviewID());
                intent.putExtra("objectType", preferences.getInt("objectType",1));


                //((Activity) context).finish(); //현재 액티비티 종료 실시
                //((Activity) context).overridePendingTransition(0, 0); //효과 없애기
                ((Activity) context).startActivity(intent); //현재 액티비티 재실행 실시
                //((Activity) context).overridePendingTransition(0, 0); //효과 없애기
            }
        });

        return view;

    }// 0: 리뷰내용 1: 리뷰별점 2: 리뷰사진

    public void addItemToList(String reviewCont, Double score, String url, int id, int parkid) {
        com.example.walkholic.ReviewListViewAdapterData listdata = new com.example.walkholic.ReviewListViewAdapterData();
        listdata.setImageURL(url);
        listdata.setReviewContent(reviewCont);
        listdata.setScore(score);
        listdata.setReviewID(id);
        listdata.setParkID(parkid);

        //값들의 조립이 완성된 listdata객체 한개를 list배열에 추가
        list.add(listdata);
    }

    //리뷰 삭제 다이얼로그
    private void showDialog(int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("리뷰 삭제").setMessage("해당 리뷰를 삭제하시겠습니까?");

        builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (objectType == 1) delParkReview(list.get(i).getReviewID());
                if (objectType == 2) delRoadReview(list.get(i).getReviewID());
                if (objectType == 3) delUserRoadReview(list.get(i).getReviewID());

                Intent intent = ((Activity) context).getIntent();

                ((Activity) context).finish(); //현재 액티비티 종료 실시
                ((Activity) context).overridePendingTransition(0, 0); //효과 없애기
                ((Activity) context).startActivity(intent); //현재 액티비티 재실행 실시
                ((Activity) context).overridePendingTransition(0, 0); //효과 없애기
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void delParkReview(int id) {
        final String TAG = "dlgochan";
        ServerRequestApi service = ServiceGenerator.getService(ServerRequestApi.class);
        service.delParkReview(id).enqueue(new Callback<ReviewRes>() {
            @Override
            public void onResponse(Call<ReviewRes> call, Response<ReviewRes> response) {
                if (response.isSuccessful()) {
                    reviewRes = response.body();
                    Log.d(TAG, "onResponse Success : " + reviewRes.toString());
                } else {
                    Log.d(TAG, "RES msg : " + response.message());
                    try {
                        Log.d(TAG, "RES errorBody : " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, String.format("RES err code : %d", response.code()));
                }
            }

            @Override
            public void onFailure(Call<ReviewRes> call, Throwable t) {
                Log.d(TAG, "onFailure : " + t.getMessage());
            }
        });
    }
    public void delRoadReview(int id) {
        final String TAG = "dlgochan";
        ServerRequestApi service = ServiceGenerator.getService(ServerRequestApi.class);
        service.delRoadReview(id).enqueue(new Callback<ReviewRes>() {
            @Override
            public void onResponse(Call<ReviewRes> call, Response<ReviewRes> response) {
                if (response.isSuccessful()) {
                    reviewRes = response.body();
                    Log.d(TAG, "onResponse Success : " + roadRes.toString());
                } else {
                    Log.d(TAG, "RES msg : " + response.message());
                    try {
                        Log.d(TAG, "RES errorBody : " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, String.format("RES err code : %d", response.code()));
                }
            }

            @Override
            public void onFailure(Call<ReviewRes> call, Throwable t) {
                Log.d(TAG, "onFailure : " + t.getMessage());
            }
        });
    }
    public void delUserRoadReview(int id) {
        final String TAG = "dlgochan";
        ServerRequestApi service = ServiceGenerator.getService(ServerRequestApi.class);
        service.delUserRoadReview(id).enqueue(new Callback<ReviewRes>() {
            @Override
            public void onResponse(Call<ReviewRes> call, Response<ReviewRes> response) {
                if (response.isSuccessful()) {
                    reviewRes = response.body();
                    Log.d(TAG, "onResponse Success : " + roadRes.toString());
                } else {
                    Log.d(TAG, "RES msg : " + response.message());
                    try {
                        Log.d(TAG, "RES errorBody : " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, String.format("RES err code : %d", response.code()));
                }
            }

            @Override
            public void onFailure(Call<ReviewRes> call, Throwable t) {
                Log.d(TAG, "onFailure : " + t.getMessage());
            }
        });
    }
    public void getParkById(int id) {
        final String TAG = "dlgochan";
        ServerRequestApi service = ServiceGenerator.getService(ServerRequestApi.class);
        service.getParkById(id).enqueue(new Callback<ParkRes>() {
            @Override
            public void onResponse(Call<ParkRes> call, Response<ParkRes> response) {
                if (response.isSuccessful()) {
                    // 리스폰스 성공 시 200 OK
                    park = response.body();
                    Log.d(TAG, "onResponse Success : " + park.toString());
                } else {
                    // 리스폰스 실패  400, 500 등
                    Log.d(TAG, "RES msg : " + response.message());
                    try {
                        Log.d(TAG, "RES errorBody : " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, String.format("RES err code : %d", response.code()));
                }
            }

            @Override
            public void onFailure(Call<ParkRes> call, Throwable t) {
                // 통신 실패 시 (인터넷 연결 끊김, SSL 인증 실패 등)
                Log.d(TAG, "onFailure : " + t.getMessage());
            }
        });
    }
    public void getRoadById(int id) {
        final String TAG = "dlgochan";
        ServerRequestApi service = ServiceGenerator.getService(ServerRequestApi.class);
        service.getRoadById(id).enqueue(new Callback<RoadRes>() {
            @Override
            public void onResponse(Call<RoadRes> call, Response<RoadRes> response) {
                if (response.isSuccessful()) {
                    // 리스폰스 성공 시 200 OK
                    road = response.body();
                    Log.d(TAG, "onResponse Success : " + road.toString());
                } else {
                    // 리스폰스 실패  400, 500 등
                    Log.d(TAG, "RES msg : " + response.message());
                    try {
                        Log.d(TAG, "RES errorBody : " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, String.format("RES err code : %d", response.code()));
                }
            }

            @Override
            public void onFailure(Call<RoadRes> call, Throwable t) {
                // 통신 실패 시 (인터넷 연결 끊김, SSL 인증 실패 등)
                Log.d(TAG, "onFailure : " + t.getMessage());
            }
        });
    }
    public void getUserRoadById(int id) {
        final String TAG = "dlgochan";
        ServerRequestApi service = ServiceGenerator.getService(ServerRequestApi.class);
        service.getUserRoadById(id).enqueue(new Callback<UserRoadRes>() {
            @Override
            public void onResponse(Call<UserRoadRes> call, Response<UserRoadRes> response) {
                if (response.isSuccessful()) {
                    userRoad = response.body();
                    Log.d(TAG, "onResponse Success : " + userRoad.toString());
                } else {
                    Log.d(TAG, "RES msg : " + response.message());
                    try {
                        Log.d(TAG, "RES errorBody : " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, String.format("RES err code : %d", response.code()));
                }
            }

            @Override
            public void onFailure(Call<UserRoadRes> call, Throwable t) {
                Log.d(TAG, "onFailure : " + t.getMessage());
            }
        });
    }

}
