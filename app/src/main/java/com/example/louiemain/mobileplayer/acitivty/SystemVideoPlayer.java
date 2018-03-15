package com.example.louiemain.mobileplayer.acitivty;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.*;
import com.example.louiemain.mobileplayer.R;
import com.example.louiemain.mobileplayer.utils.Time;

/**
 * @Program: MobilePlayer
 * @Type: Class
 * @Description: 系统播放器
 * @Author: louiemain
 * @Create: 2018-03-14 16:25
 **/
public class SystemVideoPlayer extends Activity implements View.OnClickListener {

    private static final int PROGRESS = 1;
    private VideoView vv_video_player;
    private Button btn_volume;
    private Button btn_switch_player;
    private ImageButton ib_return;
    private ImageButton ib_pre;
    private ImageButton ib_start_pause;
    private ImageButton ib_next;
    private ImageButton ib_switch_screen;
    private TextView tv_current_time;
    private SeekBar sb_video;
    private TextView tv_duration;

    private Time time;

    // 消息处理
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PROGRESS:
                    // 获取当前视频的播放进度
                    int currentPosition = vv_video_player.getCurrentPosition();
                    // 设置seekbar的进度
                    sb_video.setProgress(currentPosition);
                    // 更新当前播放时间文本
                    tv_current_time.setText(time.millisecond2Time(currentPosition));

                    removeMessages(PROGRESS);
                    // 延迟1秒发送消息自己处理
                    sendEmptyMessageDelayed(PROGRESS, 1000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 实例化视图
        setContentView(R.layout.activity_system_video_player);
        initView();

        time = new Time();

        // 设置播放属性
        Uri uri = getIntent().getData();
        if (uri != null) {
            // 获取播放地址
            vv_video_player.setVideoURI(uri);
            // 设置系统控制组件
            vv_video_player.setMediaController(new MediaController(this));
        }

    }

    private void initView() {
        vv_video_player = (VideoView) findViewById(R.id.vv_video_player);

        btn_volume = (Button) findViewById(R.id.btn_volume);
        btn_switch_player = (Button) findViewById(R.id.btn_switch_player);
        ib_return = (ImageButton) findViewById(R.id.ib_return);
        ib_pre = (ImageButton) findViewById(R.id.ib_pre);
        ib_start_pause = (ImageButton) findViewById(R.id.ib_start_pause);
        ib_next = (ImageButton) findViewById(R.id.ib_next);
        ib_switch_screen = (ImageButton) findViewById(R.id.ib_switch_screen);

        btn_volume.setOnClickListener(this);
        btn_switch_player.setOnClickListener(this);
        ib_return.setOnClickListener(this);
        ib_pre.setOnClickListener(this);
        ib_start_pause.setOnClickListener(this);
        ib_next.setOnClickListener(this);
        ib_switch_screen.setOnClickListener(this);
        tv_current_time = (TextView) findViewById(R.id.tv_current_time);
        tv_current_time.setOnClickListener(this);
        sb_video = (SeekBar) findViewById(R.id.sb_video);
        sb_video.setOnClickListener(this);
        tv_duration = (TextView) findViewById(R.id.tv_duration);
        tv_duration.setOnClickListener(this);

        // 准备好的监听
        vv_video_player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                vv_video_player.start();    // 开始播放
                // 1.获取视频总时长
                int duration = vv_video_player.getDuration();
                // 2.设置seekbar的总大小
                sb_video.setMax(duration);
                // 3.设置总视频大小文本
                tv_duration.setText(time.millisecond2Time(duration));
                // 4.每秒更新当前播放进度
                handler.sendEmptyMessage(PROGRESS);

            }
        });

        // 播放错误的监听
        vv_video_player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(SystemVideoPlayer.this, "视频格式错误，无法播放。", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // 播放完成的监听
        vv_video_player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(SystemVideoPlayer.this, "播放完成", Toast.LENGTH_SHORT).show();
            }
        });

        // 设置seekBar进度改变的监听
        sb_video.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             * @description 进度条改变的回调
             * @author louiemain
             * @date Created on 2018/3/15 23:29
             * @param seekBar 进度条
             * @param progress 进度
             * @param fromUser 是否来自用户的改变？是-true；否-false
             * @return void
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    // 来自用户的进度条改变
                    vv_video_player.seekTo(progress);
                    handler.sendEmptyMessage(PROGRESS);
                }
            }

            /**
             * @description 用户手指触碰的回调
             * @author louiemain
             * @date Created on 2018/3/15 23:29
             * @param seekBar
             * @return void
             */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            /**
             * @description 手指离开的回调
             * @author louiemain
             * @date Created on 2018/3/15 23:30
             * @param seekBar
             * @return void
             */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_volume:

                break;
            case R.id.btn_switch_player:

                break;
            case R.id.ib_return:

                break;
            case R.id.ib_pre:

                break;
            case R.id.ib_start_pause:
                if (vv_video_player.isPlaying()) {
                    // 播放中，设置 暂停
                    vv_video_player.pause();
                    // 修改图标为播放
                    ib_start_pause.setImageDrawable(getResources().getDrawable(R.drawable.btn_play_selector, null));
                } else {
                    // 暂停，设置 播放
                    vv_video_player.start();
                    // 修改图标为暂停
                    ib_start_pause.setImageDrawable(getResources().getDrawable(R.drawable.btn_pause_selector, null));
                }
                break;
            case R.id.ib_next:

                break;
            case R.id.ib_switch_screen:

                break;
        }
    }
}
