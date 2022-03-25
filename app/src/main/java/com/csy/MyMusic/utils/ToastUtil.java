package com.csy.MyMusic.utils;

import android.widget.Toast;

import com.csy.MyMusic.MyApplication;


public class ToastUtil {

    public static void showToast(String message) {
        Toast.makeText(MyApplication.getInstance(), message, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(int resid) {
        Toast.makeText(MyApplication.getInstance(), MyApplication.getInstance().getString(resid), Toast.LENGTH_SHORT)
                .show();
    }
}