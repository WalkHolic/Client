package com.example.walkholic.Service;

import android.app.Application;
import android.content.Context;
import org.jetbrains.annotations.NotNull;

public class AppD extends Application {
    @NotNull public static ATokenSharedPreferenceD token_prefs;

//    @NotNull
//    public final TokenSharedPreference getToken_prefs() {
//        TokenSharedPreference token_prefs = this.token_prefs;
//        return App.token_prefs;
//    }
//
//    public final void setToken_prefs(@NotNull TokenSharedPreference tmp) {
//        this.token_prefs = tmp;
//    }

    @Override
    public void onCreate() {
        Context applicationContext = this.getApplicationContext();
        token_prefs = new ATokenSharedPreferenceD(applicationContext);
        super.onCreate();
    }
}