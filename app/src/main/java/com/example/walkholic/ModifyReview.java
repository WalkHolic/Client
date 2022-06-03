package com.example.walkholic;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.walkholic.DataClass.DTO.ReviewRequestDto;
import com.example.walkholic.DataClass.Response.ParkRes;
import com.example.walkholic.DataClass.Response.ReviewRes;
import com.example.walkholic.DataClass.Response.UserRoadRes;
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

public class ModifyReview extends AppCompatActivity {
    // 로그에 사용할 TAG 변수 선언
    final private String TAG = getClass().getSimpleName();

    // 사용할 컴포넌트 선언
    private EditText reviewEdit;
    private Button reg_button;
    private RatingBar reviewRating;
    private TextView titleText;
    ParkRes park;

    // 리퀘스트 사용 변수
    private Uri imageUri;
    private ImageView reviewImageview;
    private ReviewRes reviewRes = new ReviewRes();
    private int objectId = -1;
    private int reviewId = -1;
    private String content = null;
    private String url = null;
    private float score = 0;
    Handler handler = new Handler();
    private int objectType = -1;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_review);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        editor = preferences.edit();

        objectType = preferences.getInt("objectType", 1);

        //원래는 리뷰 액티비티에서 해당 park의 id값을 넘겨줘야함
        objectId = getIntent().getIntExtra("objectId", 0);
        reviewId = getIntent().getIntExtra("reviewId", 0);
        content = getIntent().getStringExtra("reviewContent");
        url = getIntent().getStringExtra("reviewImage");
        score = getIntent().getFloatExtra("reviewScore", 0);
        objectType = getIntent().getIntExtra("objectType", 0);

// 컴포넌트 초기화
        titleText = findViewById(R.id.titleText);
        reviewEdit = findViewById(R.id.reviewEdit);
        reviewRating = findViewById(R.id.reviewRating);
        reg_button = findViewById(R.id.reg_button);
        reviewImageview = findViewById(R.id.reviewImageview);

        getParkById(objectId);

        handler.postDelayed(new Runnable() {
            public void run() {
                // 시간 지난 후 실행할 코딩
                titleText.setText(park.getData().get(0).getName());
            }
        }, 500); // 0.5초후
        reviewEdit.setText(content);
        reviewRating.setRating(score);
        Glide.with(this.getApplicationContext()).load(url).into(reviewImageview);
// 버튼 이벤트 추가
        reg_button.setOnClickListener(view -> {
// 리뷰 등록 함수
            // 사진
            File realFile = null;
            MultipartBody.Part thumbnail = null;
            if (imageUri != null) {
                Log.d("dlgochan", "image_uri: " + imageUri);
                String realImagePath = getPath(this, imageUri);
//                String realImagePath = imageUri.getPath();
                if(realImagePath == null){
                    Log.d("dlgochan", "realImagePath is Null@@");
                }
                //realImagePath = realImagePath.replaceAll(" ","");

                realFile = new File(realImagePath);

                Log.d("dlgochan", "realImagePath: " + realImagePath);
                Log.d("dlgochan", "fileName: " + realFile.getName());

                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), realFile);
                thumbnail = MultipartBody.Part.createFormData("thumbnail", realFile.getName(), requestFile);
            }
            // 리뷰
            ReviewRequestDto tmp = new ReviewRequestDto();
            tmp.setContent(reviewEdit.getText().toString());
            tmp.setScore((double) reviewRating.getRating());
            Gson gson = new Gson();
            String stringDto = gson.toJson(tmp);
            RequestBody requestBody1 = RequestBody.create(MediaType.parse("application/json"), stringDto);

            //업로드

            updateParkReview(reviewId, requestBody1, thumbnail);
            Intent intent1 = new Intent(getApplicationContext(), ReviewListActivity_park.class);
            startActivity(intent1);
            finish();
        });

        // 사진 등록 버튼
        reviewImageview.setOnClickListener(view -> {
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
                        reviewImageview.setImageURI(result);
                    }
                }
            });

    public void doTakeAlbumAction() {
        getContent.launch("image/*");
    }

    public void updateParkReview(int id, RequestBody body, MultipartBody.Part file) {
        final String TAG = "dlgochan";
        ServerRequestApi service = ServiceGenerator.getService(ServerRequestApi.class);
        service.updateParkReview(id, body, file).enqueue(new Callback<ReviewRes>() {
            @Override
            public void onResponse(Call<ReviewRes> call, Response<ReviewRes> response) {
                if (response.isSuccessful()) {
                    reviewRes = response.body();
                    //Log.d(TAG, "onResponse Success : " + roadRes.toString());
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
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

}
