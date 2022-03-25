package com.csy.MyMusic.activitys;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.csy.MyMusic.R;

public class BaseActivity extends Activity {

    private ImageView mIvBack, mIvMe, mIvLocalMusic;
    private TextView mTvTitle;

    protected <T extends View> T fd (@IdRes int id) {
        return findViewById(id);
    }

    protected void initNavBar (boolean isShowBack,boolean toLocal, String title, boolean isShowMe) {

        mIvBack = fd(R.id.iv_back);
        mTvTitle = fd(R.id.tv_title);
        mIvMe = fd(R.id.iv_me);
        mIvLocalMusic=fd(R.id.iv_localMusic);

        mIvBack.setVisibility(isShowBack ? View.VISIBLE : View.GONE);
        mIvLocalMusic.setVisibility(toLocal ? View.VISIBLE : View.GONE);
        mIvMe.setVisibility(isShowMe ? View.VISIBLE : View.GONE);
        mTvTitle.setText(title);

        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mIvMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BaseActivity.this, MeActivity.class));
            }
        });

        mIvLocalMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BaseActivity.this, LocalMusicActivity.class));
            }
        });
    }

}
