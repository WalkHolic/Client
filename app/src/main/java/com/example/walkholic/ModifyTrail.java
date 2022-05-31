package com.example.walkholic;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
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

import com.example.walkholic.DataClass.DTO.ReviewRequestDto;
import com.example.walkholic.DataClass.DTO.UserRoadRequestDto;
import com.example.walkholic.DataClass.DTO.UserRoadUpdateRequestDto;
import com.example.walkholic.DataClass.Data.UserRoad;
import com.example.walkholic.DataClass.Data.UserRoadPath;
import com.example.walkholic.DataClass.Response.ReviewRes;
import com.example.walkholic.DataClass.Response.UserRoadRes;
import com.example.walkholic.Service.ServerRequestApi;
import com.example.walkholic.Service.ServiceGenerator;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifyTrail extends AppCompatActivity {
    Context context;
    ContentResolver contentResolver;
    UserRoad temp = new UserRoad();
    List<UserRoad> userRoadList;
    List<UserRoadPath> userRoadPathList;
    UserRoadRes userRoadRes;
    Button btn_hash;
    MultipartBody.Part trailImage = null;
    UserRoadUpdateRequestDto dto;

    // 로그에 사용할 TAG 변수 선언
    final private String TAG = getClass().getSimpleName();

    // 사용할 컴포넌트 선언
    private EditText reviewEdit;
    private Button btn_modify_ok, getBtn_modify_cancel;
    private RatingBar reviewRating;
    private EditText trailName, trailDesc;
    private TextView hashlist;
    private ImageView trailImageView;

    // 리퀘스트 사용 변수
    private Uri imageUri;

    private int id = -1;
    private String name = null;
    private int road_id = -99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_trail);

        //원래는 리뷰 액티비티에서 해당 park의 id값을 넘겨줘야함
//        id = getIntent().getStringExtra("id");
//        name = getIntent().getStringExtra("name");
        name = "Test Name";
        road_id = getIntent().getIntExtra("rid",-1);


// 컴포넌트 초기화
        dto = new UserRoadUpdateRequestDto();

        trailName = findViewById(R.id.road_name);
        trailDesc = findViewById(R.id.road_desc);
        hashlist = findViewById(R.id.hashText);

        btn_modify_ok = findViewById(R.id.btn_modify_ok);
        getBtn_modify_cancel = findViewById(R.id.btn_modify_cancel);

        trailImageView = findViewById(R.id.trailImage);

        btn_hash = findViewById(R.id.btn_hash);

        btn_hash.setOnClickListener(new Button.OnClickListener() {
            final List<String> tempString = new ArrayList<String>();
            final String[] hashs = new String[]{"나들이", "물놀이", "아이와함께", "걷기좋은", "드라이브코스", "데이트코스", "분위기좋은", "런닝", "벚꽃명소", "힐링"};
            final String[] textTags = {""};

            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "버튼클릭", Toast.LENGTH_SHORT).show();
                tempString.clear();
                // TODO : click event
                new androidx.appcompat.app.AlertDialog.Builder(view.getContext()).setTitle("해시태그 선택")
                        .setMultiChoiceItems(hashs, null, new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                        if (b) {
                                            tempString.add(hashs[i]);
                                        } else {
                                            tempString.remove(i);
                                        }
                                    }
                                }
                        )
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (String temp : tempString
                                ) {
                                    textTags[0] = textTags[0] + "#" + temp + " ";
                                }
                                hashlist.setText(textTags[0]);
                                textTags[0] = "";
                            }
                        }).show();
                dto.setHashtag(tempString);
            }
        });
// 버튼 이벤트 추가
        btn_modify_ok.setOnClickListener(view -> {
// 산책로 변경 함수
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

                realFile = new File(realImagePath);
                Log.d("dlgochan", "realImagePath: " + realImagePath);
                Log.d("dlgochan", "fileName: " + realFile.getName());
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), realFile);
                thumbnail = MultipartBody.Part.createFormData("thumbnail", realFile.getName(), requestFile);
            }
            // 산책로 정보 갱신


            dto.setTrailName(trailName.getText().toString());
            dto.setDescription(trailDesc.getText().toString());
            Gson gson = new Gson();
            String stringDto = gson.toJson(dto);
            RequestBody requestBody1 = RequestBody.create(MediaType.parse("application/json"), stringDto);


            
            //업로드
            updateMyRoad(road_id, requestBody1, thumbnail);
            Intent intent1 = new Intent(getApplicationContext(), WalkListActivity.class);
            startActivity(intent1);
            finish();


        });
        getBtn_modify_cancel.setOnClickListener(view -> {
            Intent intent2 = new Intent(getApplicationContext(), WalkListActivity.class);
            startActivity(intent2);
            finish();
        });

        // 사진 등록 버튼
        trailImageView.setOnClickListener(view -> {
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
            Log.d("dlgochan", "if- contentUri.getPath() : " + contentUri.getPath());
            return contentUri.getPath();
        }
        String id = DocumentsContract.getDocumentId(contentUri).split(":")[1];
        String[] columns = {MediaStore.Files.FileColumns.DATA};
        String selection = MediaStore.Files.FileColumns._ID + "=" + id;
        Cursor cursor = getContentResolver().query(MediaStore.Files.getContentUri("external"), columns, selection, null, null);
        try {
            int columnIndex = cursor.getColumnIndex(columns[0]);
            if (cursor.moveToFirst()) {
                Log.d("dlgochan", "cursor.getString(columnIndex) : " + cursor.getString(columnIndex));
                return cursor.getString(columnIndex);
            }
        } finally {
            cursor.close();
        }
        return null;
    }


//    filepath는 String 변수로 갤러리에서 이미지를 가져올 때 photoUri.getPath()를 통해 받아온다.
//    File file = new File(filepath);
//    InputStream inputStream = null;
//    try {
//    inputStream = getContext().getContentResolver().openInputStream(photoUri);
//    }catch(IOException e) { e.printStackTrace(); }
//    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
//    RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), byteArrayOutputStream.toByteArray());
//    MultipartBody.Part uploadFile = MultipartBody.Part.createFormData("postImg", file.getName() ,requestBody);







    // 앨범 선택
    ActivityResultLauncher<String> getContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if (result != null) {
                        Log.d("dlgochan", "result: " + result);
                        imageUri = result;
                        trailImageView.setImageURI(result);
                    }
                }
            });

    public void doTakeAlbumAction() {
        getContent.launch("image/*");
    }

    public void updateMyRoad(int rid, RequestBody userRoadRequestDto, MultipartBody.Part file) {
        final String TAG = "dlgochan";
        ServerRequestApi service = ServiceGenerator.getService(ServerRequestApi.class);
        service.updateMyRoad(rid, userRoadRequestDto, file).enqueue(new Callback<UserRoadRes>() {
            @Override
            public void onResponse(Call<UserRoadRes> call, Response<UserRoadRes> response) {
                if (response.isSuccessful()) {
                    userRoadRes = response.body();
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
            public void onFailure(Call<UserRoadRes> call, Throwable t) {
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

