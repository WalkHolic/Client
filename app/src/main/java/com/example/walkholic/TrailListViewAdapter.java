package com.example.walkholic;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.walkholic.DataClass.DTO.UserRoadRequestDto;
import com.example.walkholic.DataClass.Data.UserRoad;
import com.example.walkholic.DataClass.Data.UserRoadPath;
import com.example.walkholic.DataClass.Response.UserRoadRes;
import com.example.walkholic.Service.ServerRequestApi;
import com.example.walkholic.Service.ServiceGenerator;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

public class TrailListViewAdapter extends BaseAdapter {
    ArrayList<com.example.walkholic.TrailListViewAdapterData> list = new ArrayList<TrailListViewAdapterData>();
    private static final int PICK_FROM_ALBUM = 1;

    Context context;
    ContentResolver contentResolver;
    UserRoad temp = new UserRoad();
    List<UserRoad> userRoadList;
    List<UserRoadPath> userRoadPathList;
    UserRoadRes userRoadRes;
    Button btn_hash;
    MultipartBody.Part trailImage = null;
    private Uri imageUri;

    public TrailListViewAdapter(Context inContext) {
        this.context = inContext;
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

        final Context context = viewGroup.getContext();

        //리스트뷰에 아이템이 인플레이트 되어있는지 확인한후
        //아이템이 없다면 아래처럼 아이템 레이아웃을 인플레이트 하고 view객체에 담는다.
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.activity_myroad, viewGroup, false);
        }

        //이제 아이템에 존재하는 텍스트뷰 객체들을 view객체에서 찾아 가져온다
        ImageView trailImage = (ImageView) view.findViewById(R.id.trailImage);
        TextView trailName = (TextView) view.findViewById(R.id.trailName);

        TextView hashtags = (TextView) view.findViewById(R.id.hashTags);
        TextView trailDist = (TextView) view.findViewById(R.id.trailDistance);
        TextView trailStep = (TextView) view.findViewById(R.id.trailSteps);
        TextView trailTime = (TextView) view.findViewById(R.id.trailTime);

        TextView trailStart = (TextView) view.findViewById(R.id.trailStart);
        //TextView trailEnd = (TextView)view.findViewById(R.id.trailEnd);
        TextView trailDesc = (TextView) view.findViewById(R.id.trailDescription);

        Button btn_delete = (Button) view.findViewById(R.id.btn_deleteTrail);
        Button btn_modify = (Button) view.findViewById(R.id.btn_modifyTrail);

        contentResolver = view.getContext().getContentResolver();


        //현재 포지션에 해당하는 아이템에 글자를 적용하기 위해 list배열에서 객체를 가져온다.
        com.example.walkholic.TrailListViewAdapterData listdata = list.get(i);

        trailName.setText(listdata.getTrailName());
        String tempp = "";
        for (int y = 0; y <= listdata.getHashtags().size() - 1; y++) {
            tempp += "#" + listdata.getHashtags().get(y) + " ";
        }
        hashtags.setText(tempp);
        String temp3 = String.valueOf(listdata.getTrailDistance());
        if (temp3.length() >= 6) temp3 = temp3.substring(0, 5);
        trailDist.setText(temp3 + " km");
        int temp = (int) (listdata.getTrailDistance() / 0.00063);
        trailStep.setText(String.valueOf(temp) + " 걸음");
        int temp2 = (int) (listdata.getTrailDistance() / 0.08);
        trailTime.setText(String.valueOf(temp2) + " 분");
        trailStart.setText(listdata.getTrailStart());
        //trailEnd.setText(listdata.getTrailEnd());
        trailDesc.setText(listdata.getTrailDescription());

        //가져온 객체안에 있는 글자들을 각 뷰에 적용한다
        //trailName.setText(listdata.getName()); //원래 int형이라 String으로 형 변환
        //trailInfo.setText("총 길이 : "+listdata.getTotalDistance() +"km\n시작 주소 : " + listdata.getStartAddr() + "\n산책로 설명 : " + listdata.getRoad_desc());


        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(i);
            }
        });
        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(),ModifyTrail.class);
                intent.putExtra("rid",i);
                context.startActivity(intent);
            }
        });


        return view;
    }

    ;

    // 0: 이미지, 1 : 산책로명 2 : 해시태그들  3 : 산책로 길이  4 : 산책로시작지점 5 : 산책로 끝지점 6 : 산책로 설명
    public void addItemToList(String imageURL, String trailName, List<String> hashtags, Double trailDistance, String trailStart, String trailEnd, String trailDesc, int rid) {
        com.example.walkholic.TrailListViewAdapterData listdata = new com.example.walkholic.TrailListViewAdapterData();
        listdata.setImageURL(imageURL);
        listdata.setTrailName(trailName);
        listdata.setHashtags(hashtags);
        listdata.setTrailDistance(trailDistance);
        listdata.setTrailStart(trailStart);
        listdata.setTrailEnd(trailEnd);
        listdata.setTrailDescription(trailDesc);
        listdata.setRid(rid);

        //값들의 조립이 완성된 listdata객체 한개를 list배열에 추가
        list.add(listdata);
    }



    public void removeItemInList(int num) {
        list.remove(num);
    }

    //산책로 삭제 다이얼로그
    private void showDialog(int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("산책로 삭제").setMessage("해당 산책로를 삭제하시겠습니까?");

        builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                delMyRoad(list.get(i).getRid());
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



    public void delMyRoad(int rid) {
        final String TAG = "dlgochan";
        ServerRequestApi service = ServiceGenerator.getService(ServerRequestApi.class);
        service.delMyRoad(rid).enqueue(new Callback<UserRoadRes>() {
            @Override
            public void onResponse(Call<UserRoadRes> call, Response<UserRoadRes> response) {
                if (response.isSuccessful()) {
                    // 리스폰스 성공 시 200 OK
                    userRoadRes = response.body();
                    //Log.d(TAG, "onResponse Success : " + roadRes.toString());

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
            public void onFailure(Call<UserRoadRes> call, Throwable t) {
                Log.d(TAG, "onFailure : " + t.getMessage());
            }
        });
    }


}
