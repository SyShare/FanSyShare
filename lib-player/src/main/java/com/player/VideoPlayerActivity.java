package com.player;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * @date：2018/7/18
 * @author: SyShare
 */
@Route(path = "/com/player")
public class VideoPlayerActivity extends AppCompatActivity {

    private static final String EXTRA_URL = "extra_url";
    private static final String EXTRA_TITLE = "extra_title";
    private static final String EXTRA_THUMB = "extra_thumb";
    @Autowired
    public String videoUrl;
    @Autowired
    public String extraTitle;
    @Autowired
    public String thumb;

    private static Intent newIntent(Context context, String extraURL, String extraTitle, String thumb) {
        Intent intent = new Intent(context, VideoPlayerActivity.class);
        intent.putExtra(EXTRA_URL, extraURL);
        intent.putExtra(EXTRA_TITLE, extraTitle);
        intent.putExtra(EXTRA_THUMB, thumb);
        return intent;
    }

    public static void open(Context context, String extraURL, String extraTitle, String thumb) {
        context.startActivity(newIntent(context, extraURL, extraTitle, thumb));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        ARouter.getInstance().inject(this);
//        videoUrl = getIntent().getStringExtra(EXTRA_URL);
//        extraTitle = getIntent().getStringExtra(EXTRA_TITLE);
//        thumb = getIntent().getStringExtra(EXTRA_THUMB);
        initView();
    }

    private void initView() {
        JZVideoPlayerStandard jzVideoPlayerStandard = findViewById(R.id.videoplayer);
        jzVideoPlayerStandard.setUp(videoUrl
                , JZVideoPlayerStandard.SCREEN_WINDOW_FULLSCREEN, extraTitle);

        Glide.with(this)
                .load("")
                .thumbnail(0.1f)
                .into(jzVideoPlayerStandard.thumbImageView);
        JZVideoPlayer.hideSupportActionBar(this);
        //模拟用户点击开始按钮，NORMAL状态下点击开始播放视频，播放中点击暂停视频
        jzVideoPlayerStandard.startButton.performClick();
    }


    @Override
    public void onBackPressed() {
//        if (JZVideoPlayer.backPress()) {
//            return;
//        }
        JZVideoPlayer.quitFullscreenOrTinyWindow();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },100);

    }

    @Override
    protected void onDestroy() {
        JZVideoPlayer.releaseAllVideos();
        super.onDestroy();
    }
}
