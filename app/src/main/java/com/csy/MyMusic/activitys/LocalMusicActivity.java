package com.csy.MyMusic.activitys;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.csy.MyMusic.adapters.SongAdapter;
import com.csy.MyMusic.models.SongModel;
import com.csy.MyMusic.utils.ScanMusicUtils;
import com.csy.MyMusic.R;

import java.util.ArrayList;
import java.util.List;


public class LocalMusicActivity extends BaseActivity   {
    public static String TAG = BaseActivity.class.getSimpleName();
    public Context mContext;

    private RecyclerView mRecyclerView;
    private SeekBar seekbar;
    private TextView tvSongName;
    private Button btnLast;
    private Button btnStar;
    private Button btnStop;
    private Button btnNext;

    private SongAdapter mAdapter;
    private MusicPlayerHelper helper;
    List<SongModel> musicData;

    //歌曲数据源
    private List<SongModel> songsList = new ArrayList<>();

    //当前播放歌曲游标位置
    private int mPosition = 0;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_local_music);
        initCommonView();
        initView();
        initListener();
        initData();
    }

    protected void initCommonView() {
        mContext = this;
    }

    public void initView() {
        initNavBar(true,false,"本地音乐",true);

        mRecyclerView =  findViewById(R.id.mRecyclerView);
        seekbar =  findViewById(R.id.seekbar);
        tvSongName =  findViewById(R.id.tvSongName);
        btnLast =  findViewById(R.id.btnLast);
        btnStar =  findViewById(R.id.btnStar);
        btnStop =  findViewById(R.id.btnStop);
        btnNext =  findViewById(R.id.btnNext);

        // Init 播放 Helper
        helper = new MusicPlayerHelper(seekbar, tvSongName);
        helper.setOnCompletionListener(mp -> {
            Log.e(TAG, "next()");
            //下一曲
            next();
        });
        // Init Adapter
        mAdapter = new SongAdapter(mContext);
        //添加数据源
        mAdapter.addAll(songsList);
        // RecyclerView 增加适配器
        mRecyclerView.setAdapter(mAdapter);
        // RecyclerView 增加布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Adapter 增加 Item 监听
        mAdapter.setItemClickListener((object, position) -> {
            mPosition = position;
            //播放歌曲
            play((SongModel) object, true);
        });
    }


    //设置监听
    public void initListener() {
        btnStar.setOnClickListener(this::onClick);
        btnStop.setOnClickListener(this::onClick);
        btnLast.setOnClickListener(this::onClick);
        btnNext.setOnClickListener(this::onClick);
    }

    //初始化数据局
    public void initData() {
        musicData = ScanMusicUtils.getMusicData(mContext);
        if (!musicData.isEmpty()) {
            Toast.makeText(this, "已搜索到本地音乐！", Toast.LENGTH_SHORT).show();
            songsList.addAll(musicData);
            mAdapter.refresh(songsList);
        } else {
            Toast.makeText(this, "未搜索到本地歌曲", Toast.LENGTH_SHORT).show();
        }
    }

    //处理点击事件
    private void onClick(View v) {
        switch (v.getId()) {
            // 上一曲
            case R.id.btnLast:
                last();
                break;
            // 播放/暂停
            case R.id.btnStar:
                play(songsList.get(mPosition), false);
                break;
            // 停止
            case R.id.btnStop:
                stop();
                break;
            // 下一曲
            case R.id.btnNext:
                next();
                break;
            default:
                break;
        }
    }

    //播放歌曲
    private void play(SongModel songModel, Boolean isRestPlayer) {
        if (!TextUtils.isEmpty(songModel.getPath())) {
            Log.e(TAG, String.format("当前状态：%s  是否切换歌曲：%s", helper.isPlaying(), isRestPlayer));
            // 当前若是播放，则进行暂停
            if (!isRestPlayer && helper.isPlaying()) {
                btnStar.setText(R.string.btn_play);
                pause();
            } else {
                //进行切换歌曲播放
                helper.playBySongModel(songModel, isRestPlayer);
                btnStar.setText(R.string.btn_pause);
                // 正在播放的列表进行更新哪一首歌曲正在播放 主要是为了更新列表里面的显示
                for (int i = 0; i < songsList.size(); i++) {
                    songsList.get(i).setPlaying(mPosition == i);
                }
                mAdapter.notifyDataSetChanged();
            }
        } else {
            showToast("当前的播放地址无效");
        }
    }


    //上一首
    private void last() {
        mPosition--;
        //如果上一曲小于0则取最后一首
        if (mPosition < 0) {
            mPosition = songsList.size() - 1;
        }
        play(songsList.get(mPosition), true);
    }

    //下一首
    private void next() {
        mPosition++;
        //如果下一曲大于歌曲数量则取第一首
        if (mPosition >= songsList.size()) {
            mPosition = 0;
        }
        play(songsList.get(mPosition), true);
    }

    /**
     * 暂停播放
     */
    private void pause() {
        helper.pause();
    }

    /**
     * 停止播放
     */
    private void stop() {
        btnStar.setText(R.string.btn_star);
        helper.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        helper.destroy();
    }

    public void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

}
