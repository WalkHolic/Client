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
        private Object facilityAmuse;
        @SerializedName("facilityConv")
        @Expose
        private Object facilityConv;
        @SerializedName("facilityCul")
        @Expose
        private Object facilityCul;
        @SerializedName("facilityEtc")
        @Expose
        private Object facilityEtc;
        @SerializedName("updated")
        @Expose
        private String updated;
        @SerializedName("manageAgency")
        @Expose
        private String manageAgency;
        @SerializedName("contact")
        @Expose
        private String contact;
        @SerializedName("dataBaseDate")
        @Expose
        private String dataBaseDate;
        @SerializedName("providerCode")
        @Expose
        private String providerCode;
        @SerializedName("providerName")
        @Expose
        private String providerName;

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

        public Object getFacilityAmuse() {
            return facilityAmuse;
        }

        public void setFacilityAmuse(Object facilityAmuse) {
            this.facilityAmuse = facilityAmuse;
        }

        public Object getFacilityConv() {
            return facilityConv;
        }

        public void setFacilityConv(Object facilityConv) {
            this.facilityConv = facilityConv;
        }

        public Object getFacilityCul() {
            return facilityCul;
        }

        public void setFacilityCul(Object facilityCul) {
            this.facilityCul = facilityCul;
        }

        public Object getFacilityEtc() {
            return facilityEtc;
        }

        public void setFacilityEtc(Object facilityEtc) {
            this.facilityEtc = facilityEtc;
        }

        public String getUpdated() {
            return updated;
        }

        public void setUpdated(String updated) {
            this.updated = updated;
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

        public String getDataBaseDate() {
            return dataBaseDate;
        }

        public void setDataBaseDate(String dataBaseDate) {
            this.dataBaseDate = dataBaseDate;
        }

        public String getProviderCode() {
            return providerCode;
        }

        public void setProviderCode(String providerCode) {
            this.providerCode = providerCode;
        }

        public String getProviderName() {
            return providerName;
        }

        public void setProviderName(String providerName) {
            this.providerName = providerName;
        }

}