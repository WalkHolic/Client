package com.example.walkholic.Service;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

public final class ATokenSharedPreferenceD {
    private final String key_accessToken;
    private final String key_accessTokenExpireDate;
    private final String key_grantType;
    private final String key_refreshToken;
    private final SharedPreferences prefs;

    @Nullable
    public final String getAccessToken() {
        return this.prefs.getString(this.key_accessToken, "");
    }

    public final void setAccessToken(@Nullable String value) {
        this.prefs.edit().putString(this.key_accessToken, value).apply();
    }

    public final int getAccessTokenExpireDate() {
        return this.prefs.getInt(this.key_accessTokenExpireDate, 0);
    }

    public final void setAccessTokenExpireDate(int value) {
        this.prefs.edit().putInt(this.key_accessTokenExpireDate, value).apply();
    }

    @Nullable
    public final String getGrantType() {
        return this.prefs.getString(this.key_grantType, "");
    }

    public final void setGrantType(@Nullable String value) {
        this.prefs.edit().putString(this.key_grantType, value).apply();
    }

    @Nullable
    public final String getRefreshToken() {
        return this.prefs.getString(this.key_refreshToken, "");
    }

    public final void setRefreshToken(@Nullable String value) {
        this.prefs.edit().putString(this.key_refreshToken, value).apply();
    }

    public ATokenSharedPreferenceD(@NotNull Context context) {
//        Intrinsics.checkNotNullParameter(context, "context");
        super();
        String prefsFilename = "token_prefs";
        this.key_accessToken = "accessToken";
        this.key_accessTokenExpireDate = "accessTokenExpireDate";
        this.key_grantType = "grantType";
        this.key_refreshToken = "refreshToken";
        SharedPreferences prefs = context.getSharedPreferences(prefsFilename, 0);
//        Intrinsics.checkNotNullExpressionValue(prefs, "context.getSharedPreferences(prefsFilename,0)");
        this.prefs = prefs;
    }
}