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

public class TmapSearchListActivity extends AppCompatActivity {

    private static final String TAG = "dlgochan";

    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmap_search_list);

        ArrayList<SearchItem> searchList = new ArrayList<>();

        String keyword = getIntent().getStringExtra("keyword");
        searchList = getIntent().getParcelableArrayListExtra("searchList");

        ItemListAdapter adapter = new ItemListAdapter(this, R.layout.dialog_item, searchList);
        mListView = (ListView) findViewById(R.id.itemListView);
        mListView.setAdapter(adapter);

        //item 클릭 이벤트 처리
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchItem item = adapter.getItem(position);
                Intent intent = new Intent(getApplicationContext(), Search_ParkActivity.class);
                intent.putExtra("itemName", item.getName());
                intent.putExtra("itemLat", item.getLat());
                intent.putExtra("itemLng", item.getLng());
                startActivity(intent);
                finish();
            }
        });

    }
}