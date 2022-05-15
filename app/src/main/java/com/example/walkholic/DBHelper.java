package com.example.walkholic;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.skt.Tmap.TMapPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "localTrails.db";

    // DBHelper 생성자
    public DBHelper(Context context, int version) {
        super(context, DATABASE_NAME, null, version);
    }

    // Trails Table 생성
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Trails(uid INTEGER , name TEXT, road_desc TEXT, jsonList TEXT, totalDist TEXT, startAddr TEXT)");
    }

    // Trails Table Upgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Trails");
        onCreate(db);
    }

    // Trails Table 데이터 입력
    public void insert(long uid, String name, String road_desc, ArrayList<TMapPoint> coordinateList, String totalDist, String startAddr) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();
        String json = arrayListToJson(coordinateList);
        Log.d("확인", "*** uid : " + uid);
        Log.d("확인", "*** totalDist : " + totalDist);
        Log.d("확인", "JSON리스트: " + json);
        Log.d("확인", "인서트문: " + "INSERT INTO Trails VALUES" + "("
                + uid + ","
                + "'" +  name  + "',"
                + "'" + road_desc  + "',"
                + "'" + json  + "',"
                + "'" + totalDist + "',"
                + "'" + startAddr + "'"
                + ")");
        db.execSQL("INSERT INTO Trails VALUES" + "("
                + uid + ","
                + "'" +  name  + "',"
                + "'" + road_desc  + "',"
                + "'" + json  + "',"
                + "'" + totalDist + "',"
                + "'" + startAddr + "'"
                + ")");

        db.close();
    }

    // Trails Table 데이터 수정
    /*public void Update(String name, int age, String Addr) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE Trails SET age = " + age + ", ADDR = '" + Addr + "'" + " WHERE NAME = '" + name + "'");
        db.close();
    }*/

    // Trails Table 데이터 삭제
    public void Delete(String json) {
        SQLiteDatabase db = getWritableDatabase();
        Log.d("확인", "JSON리스트: " + json);
        String sql = "DELETE FROM Trails WHERE jsonList = '" + json + "'";
        db.execSQL(sql);
        //db.close();
    }

    // Trails Table 조회
    public String getResult() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM Trails", null);
        while (cursor.moveToNext()) {

            // 0 : 이름 , 1 : 제이슨리스트 , 2 : 총 길이 , 3 : 주소 , 4 : 주소
            result += " 산책로명 : " + cursor.getString(0)
                    + ", 제이슨리스트 : "
                    + cursor.getString(1)
                    + ", 총 길이 : "
                    + cursor.getDouble(2)
                    + ", 시작 주소 : "
                    + cursor.getString(3)
                    + ", 끝 주소 : "
                    + cursor.getString(4)
                    + "\n";
        }

        return result;
    }

    public ArrayList<TMapPoint> jsonToArrayList(String json) {
        StringBuffer sb = new StringBuffer();
        String str =
                "[{'id':'AAA','name':'김삿갓','pwd':'111'}," +
                        "{'id':'BBB','name':'구상진','pwd':'222'}," +
                        "{'id':'CCC','name':'바디리','pwd':'333'}]";

        try {
            JSONArray jarray = new JSONArray(str);
            ArrayList<TMapPoint> list = new ArrayList<>();
            for (int i = 0; i < jarray.length(); i++) {
                JSONObject jObject = jarray.getJSONObject(i);
                String latS = jObject.getString("latitude");
                String lonS = jObject.getString("longitude");
                Double lat = Double.parseDouble(latS);
                Double lon = Double.parseDouble(lonS);

                sb.append("latitude : " + latS + ", longitude : " + lonS + "\n");

                TMapPoint tMapPoint = new TMapPoint(lat, lon);
                list.add(tMapPoint);

            }
            Log.d("JSON", sb.toString());
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public String arrayListToJson(ArrayList<TMapPoint> list) throws JSONException {
        try {
            JSONArray jArray = new JSONArray();//배열
            for (int i = 0; i < list.size(); i++) {
                JSONObject sObject = new JSONObject();//배열 내에 들어갈 json
                sObject.put("latitude", list.get(i).getLatitude());
                sObject.put("longitude", list.get(i).getLongitude());
                jArray.put(sObject);
            }

            Log.d("JSON Test", jArray.toString());
            return jArray.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "fail";
        }

    }
}
