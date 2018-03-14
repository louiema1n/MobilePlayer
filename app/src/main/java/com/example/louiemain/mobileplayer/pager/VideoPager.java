package com.example.louiemain.mobileplayer.pager;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.louiemain.mobileplayer.R;
import com.example.louiemain.mobileplayer.acitivty.SystemVideoPlayer;
import com.example.louiemain.mobileplayer.adapter.MyVideoItemAdapter;
import com.example.louiemain.mobileplayer.base.BasePager;
import com.example.louiemain.mobileplayer.domain.MediaItem;
import com.example.louiemain.mobileplayer.utils.Time;

import java.util.ArrayList;

/**
 * @Program: MobilePlayer
 * @Type: Class
 * @Description: 本地视频pager
 * @Author: louiemain
 * @Create: 2018-03-13 12:44
 **/
public class VideoPager extends BasePager {

    private ListView lv_item;
    private ProgressBar pb_loading;
    private TextView tv_no_more_media;

    private ArrayList<MediaItem> mediaItemList;

    public VideoPager(Context context) {
        super(context);
    }

    // 处理子线程发送的消息
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (mediaItemList != null && mediaItemList.size() > 0) {
                // 有数据, 显示数据，隐藏提示文本
                lv_item.setAdapter(new MyVideoItemAdapter(context, mediaItemList));
            } else {
                // 无数据，显示提示文本
                tv_no_more_media.setVisibility(View.VISIBLE);
            }
            // 隐藏loading
            pb_loading.setVisibility(View.GONE);
        }
    };

    @Override
    public View initView() {
        // 初始化视图
        View view = View.inflate(context, R.layout.video_pager, null);
        // 获取组件
        lv_item = (ListView) view.findViewById(R.id.lv_item);
        pb_loading = (ProgressBar) view.findViewById(R.id.pb_loading);
        tv_no_more_media = (TextView) view.findViewById(R.id.tv_no_more_media);

        // 设置item的点击监听
        lv_item.setOnItemClickListener(new MyItemOnClickListener());

        return view;
    }



    @Override
    public void initData() {
        super.initData();
        // 获取本地视频
        getMediaFromLocal();
    }

    /**
     * @param
     * @return void
     * @description 获取本地媒体文件
     * 1.遍历sdcard-速度慢，浪费资源
     * 2.从内容提供者解析-推荐
     * 3.6.0以上需要动态获取权限
     * @author louiemain
     * @date Created on 2018/3/13 23:12
     */
    private void getMediaFromLocal() {
        // 创建子线程--(Thread) run() -> {}
        new Thread() {
            @Override
            public void run() {
                super.run();

                // 休眠
                SystemClock.sleep(1000);

                // 从当前上下文获取内容解析者
                ContentResolver resolver = context.getContentResolver();
                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;  // 外置存储设备Uri
                String[] objs = {                                       // 需要解析的内容字段
                        MediaStore.Video.Media.DISPLAY_NAME,            // 显示的文件名称
                        MediaStore.Video.Media.DURATION,                // 文件总时长
                        MediaStore.Video.Media.SIZE,                    // 文件大小
                        MediaStore.Video.Media.DATA,                    // 文件绝对路径
                        MediaStore.Video.Media.ARTIST,                  // 艺术家
                };
                Cursor cursor = resolver.query(uri, objs, null, null, null);

                mediaItemList = new ArrayList<MediaItem>();

                // 初始化mediaItemList
                if (cursor != null) {
                    initMediaItem(cursor);
                }

                // 发送空消息
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    /**
     * @description 根据cursor初始化成MediaItem
     * @author louiemain
     * @date Created on 2018/3/13 23:25
     * @param cursor
     * @return void
     */
    private void initMediaItem(Cursor cursor) {
        while (cursor.moveToNext()) {
            MediaItem mediaItem = new MediaItem();

            // List中存的是地址
            mediaItemList.add(mediaItem);

            mediaItem.setDisplayName(cursor.getString(0));
            mediaItem.setDuration(cursor.getLong(1));
            mediaItem.setSize(cursor.getLong(2));
            mediaItem.setData(cursor.getString(3));
            mediaItem.setArtist(cursor.getString(4));
        }
        // 释放cursor
        cursor.close();
    }

    /**
     * @Description: 自定义点击item监听器
     * @Author: louiemain
     * @Date: 2018-03-14 11:14
     * @param
     * @return:
     */
    private class MyItemOnClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // 获取当前点击的mediaItem
            MediaItem mediaItem = mediaItemList.get(position);
            // 创建隐式intent-调起系统视频播放器
//            Intent intent = new Intent();
//            intent.setDataAndType(Uri.parse(mediaItem.getData()), "video/*");

            // 自定义系统播放器
            Intent intent = new Intent(context, SystemVideoPlayer.class);
            intent.setDataAndType(Uri.parse(mediaItem.getData()), "video/*");

            context.startActivity(intent);
        }
    }
}
