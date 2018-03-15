package com.example.louiemain.mobileplayer.acitivty;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;
import com.example.louiemain.mobileplayer.R;

/**
 * @Program: MobilePlayer
 * @Type: Class
 * @Description: 系统播放器
 * @Author: louiemain
 * @Create: 2018-03-14 16:25
 **/
public class SystemVideoPlayer extends Activity {

    private VideoView tv_video_player;
    private ImageButton ib_start_pause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 实例化视图
        setContentView(R.layout.activity_system_video_player);
        tv_video_player = (VideoView) findViewById(R.id.tv_video_player);
        ib_start_pause = (ImageButton) findViewById(R.id.ib_start_pause);

        // 准备好的监听
        tv_video_player.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
            @Override
            public void onPrepared(MediaPlayer mp) {
                tv_video_player.start();    // 开始播放
            }
        });

        // 播放错误的监听
        tv_video_player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(SystemVideoPlayer.this, "视频格式错误，无法播放。", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // 播放完成的监听
        tv_video_player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(SystemVideoPlayer.this, "播放完成", Toast.LENGTH_SHORT).show();
            }
        });

        // 设置播放属性
        Uri uri = getIntent().getData();
        if (uri != null) {
            // 获取播放地址
            tv_video_player.setVideoURI(uri);
            // 设置系统控制组件
            tv_video_player.setMediaController(new MediaController(this));
        }

        // 设置点击监听
        ib_start_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_video_player.isPlaying()) {
                    // 播放中，设置 暂停
                    tv_video_player.pause();
                    // 修改图标为播放
                    ib_start_pause.setImageDrawable(getResources().getDrawable(R.drawable.btn_play_selector, null));
                } else {
                    // 暂停，设置 播放
                    tv_video_player.start();
                    // 修改图标为暂停
                    ib_start_pause.setImageDrawable(getResources().getDrawable(R.drawable.btn_pause_selector, null));
                }
            }
        });
    }
}
