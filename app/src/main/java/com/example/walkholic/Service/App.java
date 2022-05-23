package com.example.walkholic.Service;

import android.app.Application;
import android.content.Context;
import org.jetbrains.annotations.NotNull;

public class App extends Application {
    @NotNull public static TokenSharedPreference token_prefs;

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
        token_prefs = new TokenSharedPreference(applicationContext);
        super.onCreate();
    }
}