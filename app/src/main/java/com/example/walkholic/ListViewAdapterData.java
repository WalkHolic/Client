package com.example.walkholic;

public class ListViewAdapterData {
    // 0 : uid , 1 : 산책로 이름 , 2 : 산책로 설명 , 3 : 산책로 리스트, 4 : 총 거리, 5 : 시작주소
    private Long uid;
    private String name;
    private String road_desc;
    private String jsonList;
    private String totalDistance;
    private String startAddr;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoad_desc() {
        return road_desc;
    }

    public void setRoad_desc(String road_desc) {
        this.road_desc = road_desc;
    }

    public String getJsonList() {
        return jsonList;
    }

    public void setJsonList(String jsonList) {
        this.jsonList = jsonList;
    }

    public String getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(String totalDistance) {
        this.totalDistance = totalDistance;
    }

    public String getStartAddr() {
        return startAddr;
    }

    public void setStartAddr(String startAddr) {
        this.startAddr = startAddr;
    }
}
