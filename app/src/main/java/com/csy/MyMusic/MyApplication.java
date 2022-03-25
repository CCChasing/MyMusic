package com.csy.MyMusic;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.Utils;
import com.csy.MyMusic.helps.RealmHelper;

import io.realm.Realm;

public class MyApplication extends Application {
    public static MyApplication instance;

    public static Context getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        Utils.init(this);
        Realm.init(this);

        RealmHelper.migration();
    }
}
