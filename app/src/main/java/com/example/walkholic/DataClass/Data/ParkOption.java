package com.example.walkholic.DataClass.Data;

public class ParkOption {
    private boolean 축구 = false;
    private boolean 농구 = false;
    private boolean 배드민턴 = false;
    private boolean 테니스 = false;
    private boolean 게이트볼 = false;
    private boolean 사이클 = false;

    private boolean 운동 = false;
    private boolean 헬스 = false;

    private boolean 정자 = false;
    private boolean 분수 = false;

    private boolean 주차 = false;

    public boolean is축구() {
        return 축구;
    }

    public void set축구(boolean 축구) {
        this.축구 = 축구;
    }

    public boolean is농구() {
        return 농구;
    }

    public void set농구(boolean 농구) {
        this.농구 = 농구;
    }

    public boolean is배드민턴() {
        return 배드민턴;
    }

    public void set배드민턴(boolean 배드민턴) {
        this.배드민턴 = 배드민턴;
    }

    public boolean is테니스() {
        return 테니스;
    }

    public void set테니스(boolean 테니스) {
        this.테니스 = 테니스;
    }

    public boolean is게이트볼() {
        return 게이트볼;
    }

    public void set게이트볼(boolean 게이트볼) {
        this.게이트볼 = 게이트볼;
    }

    public boolean is사이클() {
        return 사이클;
    }

    public void set사이클(boolean 사이클) {
        this.사이클 = 사이클;
    }

    public boolean is운동() {
        return 운동;
    }

    public void set운동(boolean 운동) {
        this.운동 = 운동;
    }

    public boolean is헬스() {
        return 헬스;
    }

    public void set헬스(boolean 헬스) {
        this.헬스 = 헬스;
    }

    public boolean is정자() {
        return 정자;
    }

    public void set정자(boolean 정자) {
        this.정자 = 정자;
    }

    public boolean is분수() {
        return 분수;
    }

    public void set분수(boolean 분수) {
        this.분수 = 분수;
    }

    public boolean is주차() {
        return 주차;
    }

    public void set주차(boolean 주차) {
        this.주차 = 주차;
    }

    @Override
    public String toString() {
        return "ParkOption{" +
                "축구=" + 축구 +
                ", 농구=" + 농구 +
                ", 배드민턴=" + 배드민턴 +
                ", 테니스=" + 테니스 +
                ", 게이트볼=" + 게이트볼 +
                ", 사이클=" + 사이클 +
                ", 운동=" + 운동 +
                ", 헬스=" + 헬스 +
                ", 정자=" + 정자 +
                ", 분수=" + 분수 +
                ", 주차=" + 주차 +
                '}';
    }
}