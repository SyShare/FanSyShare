package com.player.runalone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.player.R;

/**
 * Author by Administrator , Date on 2018/12/7.
 * PS: Not easy to write code, please indicate.
 */
public class VideoTestActivity extends AppCompatActivity {


    public static final String EXTRA_TITLE = "Ehan Baby\uD83C\uDFAF七夕";
    private static final String VIDEO_URL = "http://wvideo.spriteapp.cn/video/2018/0826/5b8258465c9ea_wpd.mp4";
    private static final String THUMP_URL = "http://wimg.spriteapp.cn/picture/2018/0826/28527749_776.jpg";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_test);


        findViewById(R.id.jump_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/com/player")
                        .withString("videoUrl", VIDEO_URL)
                        .withString("extraTitle", THUMP_URL)
                        .withString("thumb", EXTRA_TITLE)
                        .navigation();
            }
        });
    }
}
