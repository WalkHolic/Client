package com.example.walkholic;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.walkholic.ListItem.ItemListAdapter;
import com.example.walkholic.ListItem.SearchItem;
import java.util.ArrayList;

public class TmapSearchListActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "dlgochan";

    Button btn_back;

    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmap_search_list);

        btn_back = findViewById(R.id.back_btn);
        btn_back.setOnClickListener(this);

        ArrayList<SearchItem> searchList = new ArrayList<>();

        String keyword = getIntent().getStringExtra("keyword");
        searchList = getIntent().getParcelableArrayListExtra("searchList");

        boolean isPark = getIntent().getBooleanExtra("park", false);
        boolean isRoad = getIntent().getBooleanExtra("road", false);
        boolean isUserRoad = getIntent().getBooleanExtra("userRoad", false);

        ItemListAdapter adapter = new ItemListAdapter(this, R.layout.dialog_item, searchList);
        mListView = (ListView) findViewById(R.id.itemListView);
        mListView.setAdapter(adapter);

        //item 클릭 이벤트 처리
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchItem item = adapter.getItem(position);
                Intent intent;
                if (isPark) {
                    intent = new Intent(getApplicationContext(), Search_ParkActivity.class);
                } else if (isRoad) {
                    intent = new Intent(getApplicationContext(), Search_WalkActivity.class);
                } else if (isUserRoad) {
                    intent = new Intent(getApplicationContext(), Search_SharedActivity.class);
                } else return;

                intent.putExtra("itemName", item.getName());
                intent.putExtra("itemLat", item.getLat());
                intent.putExtra("itemLng", item.getLng());
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                onBackPressed();
                break;

        }
    }
}