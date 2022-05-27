package com.example.walkholic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.walkholic.DataClass.DTO.ReviewRequestDto;
import com.example.walkholic.DataClass.Response.ReviewRes;
import com.example.walkholic.Service.ServerRequestApi;
import com.example.walkholic.Service.ServiceGenerator;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WriteReviewActivity extends AppCompatActivity {


    // 로그에 사용할 TAG 변수 선언
    final private String TAG = getClass().getSimpleName();

    // 사용할 컴포넌트 선언
    EditText content_et;
    Button reg_button, btn_PostPicture;

    // 리퀘스트 사용 변수
    Uri imageUri;
    ImageView imageView;
    private ReviewRes reviewRes = new ReviewRes();
    private int id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writereview);

        //원래는 리뷰 액티비티에서 해당 park의 id값을 넘겨줘야함
//        id = getIntent().getStringExtra("id");
        id = 2222;
// 컴포넌트 초기화
        content_et = findViewById(R.id.content_et);
        reg_button = findViewById(R.id.reg_button);
        btn_PostPicture = findViewById(R.id.btn_PostPicture);
        imageView = findViewById(R.id.user_image);
// 버튼 이벤트 추가
        reg_button.setOnClickListener(view -> {
            Log.d("dlgochan", "reg_button click!!");
// 리뷰 등록 함수
            // 사진
            File realFile = null;
            MultipartBody.Part thumbnail = null;
            if (imageUri != null) {
                String realImagePath = getRealPathFromUri(imageUri);
                realFile = new File(realImagePath);
                Log.d("dlgochan", "realImagePath: " + realImagePath);
                Log.d("dlgochan", "fileName: " + realFile.getName());
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), realFile);
                thumbnail = MultipartBody.Part.createFormData("thumbnail", realFile.getName(), requestFile);
            }
            // 리뷰
            ReviewRequestDto tmp = new ReviewRequestDto();
            tmp.setContent(content_et.getText().toString());
            tmp.setScore(5.0);
            Gson gson = new Gson();
            String stringDto = gson.toJson(tmp);
            RequestBody requestBody1 = RequestBody.create(MediaType.parse("application/json"), stringDto);

            //업로드
            uploadParkReview(id, requestBody1, thumbnail);


        });

        // 사진 등록 버튼
        btn_PostPicture.setOnClickListener(view -> {
            imageUri = null;
            DialogInterface.OnClickListener albumListner = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    doTakeAlbumAction();
                }
            };
            DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            };
            new AlertDialog.Builder(this)
                    .setTitle("업로드할 이미지 선택")
                    .setNeutralButton("앨범선택", albumListner)
                    .setNegativeButton("취소", cancelListener)
                    .show();
        });
    }

    private String getRealPathFromUri(Uri contentUri) {
        if (contentUri.getPath().startsWith("/storage")) {
            return contentUri.getPath();
        }
        String id = DocumentsContract.getDocumentId(contentUri).split(":")[1];
        String[] columns = {MediaStore.Files.FileColumns.DATA};
        String selection = MediaStore.Files.FileColumns._ID + "=" + id;
        Cursor cursor = getContentResolver().query(MediaStore.Files.getContentUri("external"), columns, selection, null, null);
        try {
            int columnIndex = cursor.getColumnIndex(columns[0]);
            if (cursor.moveToFirst()) {
                return cursor.getString(columnIndex);
            }
        } finally {
            cursor.close();
        }
        return null;
    }

    // 앨범 선택
    ActivityResultLauncher<String> getContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if (result != null) {
                        Log.d("dlgochan", "result: " + result);
                        imageUri = result;
                        imageView.setImageURI(result);
                    }
                }
            });

    public void doTakeAlbumAction() {
        getContent.launch("image/*");
    }

    public void uploadParkReview(int id, RequestBody reviewRequestDto, MultipartBody.Part file) {
        final String TAG = "dlgochan";
        ServerRequestApi service = ServiceGenerator.getService(ServerRequestApi.class);
        service.uploadParkReview(id, reviewRequestDto, file).enqueue(new Callback<ReviewRes>() {
            @Override
            public void onResponse(Call<ReviewRes> call, Response<ReviewRes> response) {
                if (response.isSuccessful()) {
                    reviewRes = response.body();
                    Log.d(TAG, "onResponse Success : " + reviewRes.toString());
                    Toast.makeText(WriteReviewActivity.this, "등록되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Log.d(TAG, "RES msg : " + response.message());
                    try {
                        Log.d(TAG, "RES errorBody : " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, String.format("RES err code : %d", response.code()));
                    Toast.makeText(WriteReviewActivity.this, "request error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReviewRes> call, Throwable t) {
                Log.d(TAG, "onFailure : " + t.getMessage());
                Toast.makeText(WriteReviewActivity.this, "request fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

