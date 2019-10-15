package com.example.waytowork;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class ClassSharedPreference {

    private final String PrefenceName = "appData"; // 아이디값 저장하는 변수
    static Context ctx;

    public ClassSharedPreference(Context ctx) {
        this.ctx = ctx;
    }

    public void put(String key, String value) {
        SharedPreferences preferences = ctx.getSharedPreferences(PrefenceName, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(key, value);
        editor.commit();

        // 아이디 비밀번호 저장
    }

    public void put(String key, boolean value) {   //세이브 로그인 데이터
        SharedPreferences preferences = ctx.getSharedPreferences(PrefenceName, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean(key, value);
        editor.commit();
    }
    // 로그인할때 아이디 비밀번호 저장하겠냐 (체크 했으면 참, 체크 안했으면 거짓)

    public String getValue(String key, String defaultValue) {
        SharedPreferences preferences = ctx.getSharedPreferences(PrefenceName, Activity.MODE_PRIVATE);

        try {
            return preferences.getString(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public boolean getValue(String key, boolean defaultValue) { //세이브 로그인 데이터
        SharedPreferences preferences = ctx.getSharedPreferences(PrefenceName, Activity.MODE_PRIVATE);

        try {
            return preferences.getBoolean(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }
}