package com.csy.MyMusic.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.csy.MyMusic.models.SongModel;

import java.util.ArrayList;
import java.util.List;

public class ScanMusicUtils {

    public static List<SongModel> list;

    public static SongModel song;
    private static String name;
    private static String singer;
    private static String path;
    private static int duration;
    private static long size;
    public static List<SongModel> getMusicData(Context context) {
        list = new ArrayList<>();
        String[] selectionArgs = new String[]{"%Music%"};
        String selection = MediaStore.Audio.Media.DATA + " like ? ";
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, selection,
                selectionArgs, MediaStore.Audio.AudioColumns.IS_MUSIC);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                song = new SongModel();
                name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                singer = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                //list.add(song);
                //把歌曲名字和歌手切割开
                //song.setName(name);
                song.setSinger(singer);
                song.setPath(path);
                song.setDuration(duration);
                song.setSize(size);
                if (size > 1000 * 800) {
                    if (name.contains("-")) {
                        String[] str = name.split("-");
                        singer = str[0];
                        song.setSinger(singer);
                        name = str[1];
                        song.setName(name);
                    } else {
                        song.setName(name);
                    }
                    list.add(song);
                }
            }
        }
        cursor.close();
        return list;
    }

    public static String formatTime(int time) {
        if (time / 1000 % 60 < 10) {
            return (time / 1000 / 60) + ":0" + time / 1000 % 60;
        } else {
            return (time / 1000 / 60) + ":" + time / 1000 % 60;
        }
    }
}