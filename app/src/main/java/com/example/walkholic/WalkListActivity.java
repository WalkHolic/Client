package com.example.walkholic;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.walkholic.DataClass.DTO.ReviewRequestDto;
import com.example.walkholic.DataClass.Response.ReviewRes;
import com.example.walkholic.Service.ServerRequestApi;
import com.example.walkholic.Service.ServiceGenerator;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalkListActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_home;
    Button btn_search;
    Button btn_walking;
    Button btn_mypage;

    ImageView imageView;
    Uri imageUri;

    private ReviewRes reviewRes;
    private ReviewRequestDto reviewRequestDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_list);

        btn_home = findViewById(R.id.btn_home);
        btn_search = findViewById(R.id.btn_search);
        btn_walking = findViewById(R.id.btn_walking);
        btn_mypage = findViewById(R.id.btn_mypage);

        btn_home.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        btn_walking.setOnClickListener(this);
        btn_mypage.setOnClickListener(this);

        imageView = findViewById(R.id.user_image);

        reviewRequestDto = new ReviewRequestDto();
        reviewRequestDto.setContent("좋아요");
        reviewRequestDto.setScore(4.0);
        Log.d("dlgochan", "reviewRequestDto: " + reviewRequestDto.toString());
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
    // 카메라 촬영
    ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Log.d("dlgochan", "result: " + result);
                        imageView.setImageURI(camUri);
                    }
                }
            });

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_home:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_search:
                Intent intent2 = new Intent(getApplicationContext(), Search_ParkActivity.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.btn_walking:
                Intent intent3 = new Intent(getApplicationContext(), WalkingActivity.class);
                startActivity(intent3);
                finish();
                break;
            case R.id.btn_mypage:
                Intent intent4 = new Intent(getApplicationContext(), WalkListActivity.class);
                startActivity(intent4);
                finish();
                break;
            case R.id.btn_PostPicture:
                try {

                    String realImagePath = getRealPathFromUri(imageUri);
                    File realFile = new File(realImagePath);
                    Log.d("dlgochan", "realImagePath: " + realImagePath);
                    Log.d("dlgochan", "imagePath: " + realFile.getAbsolutePath());
                    Log.d("dlgochan", "fileName: " + realFile.getName());
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), realFile);
                    MultipartBody.Part thumbnail = MultipartBody.Part.createFormData("thumbnail", realFile.getName(), requestFile);
                    Gson gson = new Gson();
                    String stringDto = gson.toJson(reviewRequestDto);
                    RequestBody requestBody1 = RequestBody.create(MediaType.parse("application/json"), stringDto);
                    uploadParkReview(3, requestBody1, thumbnail);
                } catch (Exception e) {
                    Log.d("dlgochan", e.getMessage());
                }

                break;
            // 사진 업로드 예시 코드
            case R.id.btn_UploadPicture:
                DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        doTakePhotoAction();
                    }
                };
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
                        .setPositiveButton("사진촬영", cameraListener)
                        .setNeutralButton("앨범선택", albumListner)
                        .setNegativeButton("취소", cancelListener)
                        .show();
                break;
        }
    }

    private String getRealPathFromUri(Uri contentUri) {
        if (contentUri.getPath().startsWith("/storage")) {
            return contentUri.getPath();
        }
        String id = DocumentsContract.getDocumentId(contentUri).split(":")[1];
        String[] columns = { MediaStore.Files.FileColumns.DATA };
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

    Uri camUri;
    // 카메라 통해서 사진 찍기
    public void doTakePhotoAction() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(this, "카메라 권한 설정이 필요합니다.", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        camUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, camUri);
        cameraLauncher.launch(cameraIntent);
    }

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
}