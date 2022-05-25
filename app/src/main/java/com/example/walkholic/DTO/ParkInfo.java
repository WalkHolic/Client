package com.example.walkholic.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParkInfo {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("parkId")
    @Expose
    private String parkId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("addrNew")
    @Expose
    private String addrNew;
    @SerializedName("addr")
    @Expose
    private String addr;
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lng")
    @Expose
    private Double lng;
    @SerializedName("area")
    @Expose
    private Integer area;
    @SerializedName("facilitySport")
    @Expose
    private String facilitySport;
    @SerializedName("facilityAmuse")
    @Expose
    private String facilityAmuse;
    @SerializedName("facilityConv")
    @Expose
    private String facilityConv;
    @SerializedName("facilityCul")
    @Expose
    private String facilityCul;
    @SerializedName("facilityEtc")
    @Expose
    private String facilityEtc;
    @SerializedName("manageAgency")
    @Expose
    private String manageAgency;
    @SerializedName("contact")
    @Expose
    private String contact;
    @SerializedName("providerName")
    @Expose
    private String providerName;
    @SerializedName("pngPath")
    @Expose
    private String pngPath;
    @SerializedName("hashtag")
    @Expose
    private String hashtag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getParkId() {
        return parkId;
    }

    public void setParkId(String parkId) {
        this.parkId = parkId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddrNew() {
        return addrNew;
    }

    public void setAddrNew(String addrNew) {
        this.addrNew = addrNew;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public String getFacilitySport() {
        return facilitySport;
    }

    public void setFacilitySport(String facilitySport) {
        this.facilitySport = facilitySport;
    }

    public String getFacilityAmuse() {
        return facilityAmuse;
    }

    public void setFacilityAmuse(String facilityAmuse) {
        this.facilityAmuse = facilityAmuse;
    }

    public String getFacilityConv() {
        return facilityConv;
    }

    public void setFacilityConv(String facilityConv) {
        this.facilityConv = facilityConv;
    }

    public String getFacilityCul() {
        return facilityCul;
    }

    public void setFacilityCul(String facilityCul) {
        this.facilityCul = facilityCul;
    }

    public String getFacilityEtc() {
        return facilityEtc;
    }

    public void setFacilityEtc(String facilityEtc) {
        this.facilityEtc = facilityEtc;
    }

    public String getManageAgency() {
        return manageAgency;
    }

    public void setManageAgency(String manageAgency) {
        this.manageAgency = manageAgency;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getPngPath() {
        return pngPath;
    }

    public void setPngPath(String pngPath) {
        this.pngPath = pngPath;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    @Override
    public String toString() {
        return "ParkInfo{" +
                "id=" + id +
                ", parkId='" + parkId + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", addrNew='" + addrNew + '\'' +
                ", addr='" + addr + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", area=" + area +
                ", facilitySport='" + facilitySport + '\'' +
                ", facilityAmuse='" + facilityAmuse + '\'' +
                ", facilityConv='" + facilityConv + '\'' +
                ", facilityCul='" + facilityCul + '\'' +
                ", facilityEtc='" + facilityEtc + '\'' +
                ", manageAgency='" + manageAgency + '\'' +
                ", contact='" + contact + '\'' +
                ", providerName='" + providerName + '\'' +
                ", pngPath='" + pngPath + '\'' +
                ", hashtag='" + hashtag + '\'' +
                '}';
    }
}