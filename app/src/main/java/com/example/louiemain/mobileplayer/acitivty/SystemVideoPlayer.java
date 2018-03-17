package com.example.louiemain.mobileplayer.acitivty;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import com.example.louiemain.mobileplayer.R;
import com.example.louiemain.mobileplayer.domain.MediaItem;
import com.example.louiemain.mobileplayer.utils.NetWork;
import com.example.louiemain.mobileplayer.utils.Time;
import com.example.louiemain.mobileplayer.view.VideoViewSelf;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @Program: MobilePlayer
 * @Type: Class
 * @Description: 系统播放器
 * @Author: louiemain
 * @Create: 2018-03-14 16:25
 **/
public class SystemVideoPlayer extends Activity implements View.OnClickListener {

    // 播放进度
    private static final int PROGRESS = 1;
    // 隐藏控制面板
    private static final int HIDE_MEDIA_CONTROLLER = 2;
    private VideoViewSelf vv_video_player;
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
    private TextView tv_system_time;
    private RelativeLayout media_controller;
    private SeekBar sb_volume;

    private Time time;

    // 消息处理
    private Handler handler = new Handler() {
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

                    // 每秒更新系统时间
                    tv_system_time.setText(getSystemTime());

                    // 更新网络资源的缓冲进度
                    if (isNetworkResources) {
                        // 获取缓冲百分比
                        int bufferPercentage = vv_video_player.getBufferPercentage();
                        // 获取进度条的最大数
                        int max = sb_video.getMax();
                        // 计算缓冲进度
                        int bufferProgress = bufferPercentage * max / 100;
                        // 设置进度条
                        sb_video.setSecondaryProgress(bufferProgress);
                    }

                    removeMessages(PROGRESS);
                    // 延迟1秒发送消息自己处理
                    sendEmptyMessageDelayed(PROGRESS, 1000);
                    break;

                case HIDE_MEDIA_CONTROLLER:
                    isShowMediaController = false;
                    showHideMediaControllerHandle();
                    break;

            }
        }
    };
    private TextView tv_video_display_name;
    /**
     * 播放视频列表
     */
    private ArrayList<MediaItem> mediaItems;
    /**
     * 播放视频位置
     */
    private int position;
    /**
     * 播放视频的uri
     */
    private Uri uri;
    /**
     * 是否显示控制面板
     */
    private boolean isShowMediaController;
    /**
     * 是否全屏
     */
    private boolean isFullScreen;
    /**
     * 是否静音
     */
    private boolean isMuted;
    // 实例化network工具类
    private NetWork netWork;
    /**
     * 是否为网络资源
     */
    private boolean isNetworkResources;
    private TextView buffer_network;

    private LinearLayout ll_buffer_loading;

    /**
     * @param
     * @Description: 获取当前系统时间
     * @Author: louiemain
     * @Date: 2018-03-16 14:30
     * @return: java.lang.String
     */
    private String getSystemTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date());
    }

    /**
     * 定义手势识别器
     */
    private GestureDetector gestureDetector;

    // 屏幕宽度
    private int screenWidth = 0;
    // 屏幕高度
    private int screenHeight = 0;
    // 视频宽度
    private int videoWidth = 0;
    // 视频高度
    private int videoHeight = 0;

    // 音频调节
    AudioManager audioManager;
    // 最大音量
    private int maxVolume;
    // 当前音量
    private int volume;

    // 手指滑动起始位置
    private float startY;
    // 滑动方向的总距离
    private int touchRang;
    // 手指触摸时的音量
    private int startVolume;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 实例化视图
        setContentView(R.layout.activity_system_video_player);
        initView();

        // 获取数据
        getData();
        // 设置数据
        setData();


    }

    /**
     * @param
     * @return void
     * @description 设置数据
     * @author louiemain
     * @date Created on 2018/3/17 11:12
     */
    private void setData() {
        if (mediaItems != null && mediaItems.size() > 0) {
            MediaItem mediaItem = mediaItems.get(position);
            // 判断是否为网络资源
            isNetworkResources = netWork.isNetworkResource(mediaItem.getData());
            // 设置当前播放器的path
            vv_video_player.setVideoPath(mediaItem.getData());
            // 设置当前播放视频的名称
            tv_video_display_name.setText(mediaItem.getDisplayName());
        } else if (uri != null) {
            // 判断是否为网络资源
            isNetworkResources = netWork.isNetworkResource(uri.toString());
            // 设置播放地址
            vv_video_player.setVideoURI(uri);
            // 设置当前播放视频的名称
            tv_video_display_name.setText(uri.toString());
        } else {
            Toast.makeText(this, "请传递播放视频数据~亲", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * @param
     * @return void
     * @description 设置数据
     * @author louiemain
     * @date Created on 2018/3/17 10:57
     */
    private void getData() {
        // 获取视频列表
        mediaItems = (ArrayList<MediaItem>) getIntent().getSerializableExtra("videoList");
        // 获取播放视频位置
        position = getIntent().getIntExtra("position", 0);

        // 获取播放属性
        uri = getIntent().getData();

    }

    /**
     * @param
     * @return void
     * @description 初始化视图
     * @author louiemain
     * @date Created on 2018/3/17 10:57
     */
    private void initView() {
        time = new Time();
        netWork = new NetWork();

        vv_video_player = (VideoViewSelf) findViewById(R.id.vv_video_player);

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
        media_controller = (RelativeLayout) findViewById(R.id.media_controller);
        sb_volume = (SeekBar) findViewById(R.id.sb_volume);

        buffer_network = (TextView) findViewById(R.id.buffer_network);
        ll_buffer_loading = (LinearLayout) findViewById(R.id.ll_buffer_loading);

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            /**
             * 单击
             * @param e
             * @return
             */
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                showHideMediaControllerHandle();
                return super.onSingleTapConfirmed(e);
            }

            /**
             * 单击弹起
             * @param e
             * @return
             */
//            @Override
//            public boolean onSingleTapUp(MotionEvent e) {
//                Toast.makeText(SystemVideoPlayer.this, "单击", Toast.LENGTH_SHORT).show();
//                return super.onSingleTapUp(e);
//            }

            /**
             * 长按
             * @param e
             */
            @Override
            public void onLongPress(MotionEvent e) {
                // 播放/暂停视频
                startPauseVideoHandle();
                super.onLongPress(e);
            }

            /**
             * 双击
             * @param e
             * @return
             */
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                // 切换全屏/默认播放视频
                switchScreenSizeHandle();
                return super.onDoubleTap(e);
            }
        });

        // 实例化AudioManager
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        // 获取最大音量(AudioManager.STREAM_MUSIC-媒体音量)
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        // 获取当前音量
        volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

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
                // 显示控制面板
                isShowMediaController = true;
                showHideMediaControllerHandle();

                // 获取视频的宽度、高度
                videoWidth = mp.getVideoWidth();
                videoHeight = mp.getVideoHeight();
                // 获取屏幕的宽度、高度
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                screenWidth = metrics.widthPixels;
                screenHeight = metrics.heightPixels;

                // 设置默认播放
                isFullScreen = true;
                switchScreenSizeHandle();

                // 设置最大音量
                sb_volume.setMax(maxVolume);
                // 设置当前音量
                sb_volume.setProgress(volume);
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
                nextVideoHandle();
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
             * @description 用户手指开始触碰的回调
             * @author louiemain
             * @date Created on 2018/3/15 23:29
             * @param seekBar
             * @return void
             */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeMessages(HIDE_MEDIA_CONTROLLER);
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
                handler.sendEmptyMessageDelayed(HIDE_MEDIA_CONTROLLER, 3000);
            }
        });
        sb_volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             * 进度条改变
             * @param seekBar
             * @param progress
             * @param fromUser
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if (progress > 0) {
                        isMuted = true;
                    } else {
                        isMuted = false;
                    }

                    changeVolumeHandle(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeMessages(HIDE_MEDIA_CONTROLLER);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                handler.sendEmptyMessageDelayed(HIDE_MEDIA_CONTROLLER, 3000);
            }
        });

        tv_system_time = (TextView) findViewById(R.id.tv_system_time);
        tv_system_time.setOnClickListener(this);
        tv_video_display_name = (TextView) findViewById(R.id.tv_video_display_name);
        tv_video_display_name.setOnClickListener(this);

        // 设置视频播放卡顿的监听-使用系统api
        vv_video_player.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                switch (what) {
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        // 开始卡顿
                        ll_buffer_loading.setVisibility(View.VISIBLE);
                        break;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                        // 卡顿结束
                        ll_buffer_loading.setVisibility(View.GONE);
                        break;
                }
                return false;
            }
        });

        buffer_network.setOnClickListener(this);
    }

    /**
     * @param progress
     * @return void
     * @description 改变音量
     * @author louiemain
     * @date Created on 2018/3/17 19:03
     */
    private void changeVolumeHandle(int progress) {
        if (isMuted) {
            // 是静音，改为正常音量
            // 设置当前系统音量(flags:0-不调起系统音量条；1-调起)
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            // 设置进度条的变化
            sb_volume.setProgress(progress);
            // 赋值给当前音量
            volume = progress;
            isMuted = false;
        } else {
            // 正常音量，改为静音
            // 设置当前系统音量(flags:0-不调起系统音量条；1-调起)
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            // 设置进度条的变化
            sb_volume.setProgress(0);
            isMuted = true;
        }

    }

    /**
     * @param
     * @return void
     * @description 显示/隐藏视频播放控制面板
     * @author louiemain
     * @date Created on 2018/3/17 12:44
     */
    private void showHideMediaControllerHandle() {
        // 移除控制面板隐藏消息
        handler.removeMessages(HIDE_MEDIA_CONTROLLER);
        if (isShowMediaController) {
            // 显示
            media_controller.setVisibility(View.VISIBLE);
            isShowMediaController = false;
            // 发送控制面板隐藏消息-3s后隐藏
            handler.sendEmptyMessageDelayed(HIDE_MEDIA_CONTROLLER, 3000);
        } else {
            // 隐藏
            media_controller.setVisibility(View.GONE);
            isShowMediaController = true;
        }
    }

    /**
     * @param event
     * @return boolean
     * @description 处理触摸事件
     * @author louiemain
     * @date Created on 2018/3/17 12:24
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 将事件传递给手势识别器
        gestureDetector.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:   // 手指按下
                // 记录手指起始的Y方向的位置
                startY = event.getY();
                // 记录滑动方向的总距离
                touchRang = Math.min(screenWidth, screenHeight);
                // 记录手机起始时的音量
                startVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                // 移除控制面板隐藏消息
                handler.removeMessages(HIDE_MEDIA_CONTROLLER);
                break;
            case MotionEvent.ACTION_MOVE:   // 手指移动
                // 记录手指当前Y方向的位置
                float endY = event.getY();
                // 计算滑动的距离
                float distance = startY - endY;
                // 计算音量的偏移量
                // 滑动距离:总距离 = 偏移的音量:总音量
                int offset = (int) ((distance / touchRang) * maxVolume);
                // 计算偏移后的音量
                int progress = Math.min(Math.max(startVolume + offset, 0), maxVolume);
                // 改变音量
                if (offset != 0) {
                    isMuted = true;
                    changeVolumeHandle(progress);
                }

                break;
            case MotionEvent.ACTION_UP:     // 手指离开
                // 延迟发送隐藏控制面板的消息
                handler.sendEmptyMessageDelayed(HIDE_MEDIA_CONTROLLER, 4000);
                break;
        }
        return super.onTouchEvent(event);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_volume:
                changeVolumeHandle(volume);
                break;
            case R.id.btn_switch_player:

                break;
            case R.id.ib_return:
                finish();
                break;
            case R.id.ib_pre:
                preVideoHandle();
                break;
            case R.id.ib_start_pause:
                startPauseVideoHandle();
                break;
            case R.id.ib_next:
                nextVideoHandle();
                break;
            case R.id.ib_switch_screen:
                switchScreenSizeHandle();
                break;
        }
        // 显示控制面板
        isShowMediaController = true;
        showHideMediaControllerHandle();
    }

    /***
     * @description 切换视频播放的屏幕大小
     * @author louiemain
     * @date Created on 2018/3/17 16:40
     * @param
     * @return void
     */
    private void switchScreenSizeHandle() {
        if (isFullScreen) {
            // 是全屏，切换换成默认(等比例缩放后的视频大小)
            // 根据视频及屏幕大小按比例缩放视频尺寸
            int width = screenWidth;    // 调整后的视频宽度
            int height = screenHeight;  // 调整后的视频高度
            // for compatibility, we adjust size based on aspect ratio
            if (videoWidth * screenHeight < screenWidth * videoHeight) {
                //Log.i("@@@", "image too wide, correcting");
                width = screenHeight * videoWidth / videoHeight;
            } else if (videoWidth * screenHeight > screenWidth * videoHeight) {
                //Log.i("@@@", "image too tall, correcting");
                height = screenWidth * videoHeight / videoWidth;
            }
            vv_video_player.setVideoSize(width, height);
            // 设置按钮状态
            ib_switch_screen.setImageDrawable(getResources().getDrawable(R.drawable.btn_full_screen_selector, null));
            isFullScreen = false;
        } else {
            // 是默认，切换成全屏(屏幕大小)
            vv_video_player.setVideoSize(screenWidth, screenHeight);
            // 设置按钮状态
            ib_switch_screen.setImageDrawable(getResources().getDrawable(R.drawable.btn_default_selector, null));
            isFullScreen = true;
        }
    }


    /**
     * @param
     * @return void
     * @description 播放/暂停视频切换
     * @author louiemain
     * @date Created on 2018/3/17 12:19
     */
    private void startPauseVideoHandle() {
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
    }

    /**
     * @param
     * @return void
     * @description 下一个视频
     * @author louiemain
     * @date Created on 2018/3/17 11:50
     */
    private void nextVideoHandle() {
        if (mediaItems != null && mediaItems.size() > 0) {
            position++;
            if (position > mediaItems.size() - 1) {
                position = mediaItems.size() - 1;
                Toast.makeText(this, "后面已经没有了", Toast.LENGTH_SHORT).show();
            } else {
                MediaItem mediaItem = mediaItems.get(position);
                // 判断是否为网络资源
                isNetworkResources = netWork.isNetworkResource(mediaItem.getData());
                vv_video_player.setVideoPath(mediaItem.getData());
                tv_video_display_name.setText(mediaItem.getDisplayName());
            }
        } else if (uri != null) {
            Toast.makeText(this, "后面已经没有了", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * @param
     * @return void
     * @description 上一个视频
     * @author louiemain
     * @date Created on 2018/3/17 11:42
     */
    private void preVideoHandle() {
        if (mediaItems != null && mediaItems.size() > 0) {
            position--;
            if (position < 0) {
                position = 0;
                Toast.makeText(this, "前面已经没有了", Toast.LENGTH_SHORT).show();
            } else {
                MediaItem mediaItem = mediaItems.get(position);
                // 判断是否为网络资源
                isNetworkResources = netWork.isNetworkResource(mediaItem.getData());
                vv_video_player.setVideoPath(mediaItem.getData());
                tv_video_display_name.setText(mediaItem.getDisplayName());
            }
        } else if (uri != null) {
            Toast.makeText(this, "前面已经没有了", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * @param keyCode
     * @param event
     * @return boolean
     * @description 监听物理按键
     * @author louiemain
     * @date Created on 2018/3/17 22:17
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            // 音量减小
            volume--;
            isMuted = true;
            changeVolumeHandle(volume);
            // 隐藏控制面板
            isShowMediaController = true;
            showHideMediaControllerHandle();
            // 阻止父类继续响应
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            // 音量加大
            volume++;
            isMuted = true;
            changeVolumeHandle(volume);
            // 隐藏控制面板
            isShowMediaController = true;
            showHideMediaControllerHandle();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
