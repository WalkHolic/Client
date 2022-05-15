package com.example.walkholic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    ArrayList<com.example.walkholic.ListViewAdapterData> list = new ArrayList<com.example.walkholic.ListViewAdapterData>();

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
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.activity_list_item,viewGroup,false);
        }

        //이제 아이템에 존재하는 텍스트뷰 객체들을 view객체에서 찾아 가져온다
        TextView trailName = (TextView)view.findViewById(R.id.trailName);
        TextView trailInfo = (TextView)view.findViewById(R.id.trailInfo);

        //현재 포지션에 해당하는 아이템에 글자를 적용하기 위해 list배열에서 객체를 가져온다.
        com.example.walkholic.ListViewAdapterData listdata = list.get(i);

        //가져온 객체안에 있는 글자들을 각 뷰에 적용한다
        trailName.setText(listdata.getName()); //원래 int형이라 String으로 형 변환
        trailInfo.setText("총 길이 : "+listdata.getTotalDistance() +"km\n시작 주소 : " + listdata.getStartAddr() + "\n산책로 설명 : " + listdata.getRoad_desc());



        return view;
    }
    // 0 : uid , 1 : 산책로 이름 , 2 : 산책로 설명 , 3 : 산책로 리스트, 4 : 총 거리, 5 : 시작주소
    public void addItemToList(long uid , String name, String road_desc, String jsonList, String totalDist, String startAddr) {
        com.example.walkholic.ListViewAdapterData listdata = new com.example.walkholic.ListViewAdapterData();

        listdata.setUid(uid);
        listdata.setName(name);
        listdata.setRoad_desc(road_desc);
        listdata.setJsonList(jsonList);
        listdata.setTotalDistance(totalDist);
        listdata.setStartAddr(startAddr);

        //값들의 조립이 완성된 listdata객체 한개를 list배열에 추가
        list.add(listdata);
    }
    public void removeItemInList(int num) {
        list.remove(num);
    }

}
