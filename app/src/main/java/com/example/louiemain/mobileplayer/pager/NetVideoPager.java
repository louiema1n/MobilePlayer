package com.example.louiemain.mobileplayer.pager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.louiemain.mobileplayer.R;
import com.example.louiemain.mobileplayer.acitivty.SystemVideoPlayer;
import com.example.louiemain.mobileplayer.adapter.MyNetworkVideoItemAdapter;
import com.example.louiemain.mobileplayer.base.BasePager;
import com.example.louiemain.mobileplayer.domain.MediaItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @Program: MobilePlayer
 * @Type: Class
 * @Description: 网络视频pager
 * @Author: louiemain
 * @Create: 2018-03-13 12:44
 **/
public class NetVideoPager extends BasePager {


    private ListView lv_net_item;
    private ProgressBar pb_net_loading;
    private TextView tv_no_more_net_media;

    private ArrayList<MediaItem> mediaItemList;

    public NetVideoPager(Context context) {
        super(context);
    }

    // 处理子线程发送的消息
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (mediaItemList != null && mediaItemList.size() > 0) {
                // 有数据, 显示数据，隐藏提示文本
                lv_net_item.setAdapter(new MyNetworkVideoItemAdapter(context, mediaItemList));
            } else {
                // 无数据，显示提示文本
                tv_no_more_net_media.setVisibility(View.VISIBLE);
            }
            // 隐藏loading
            pb_net_loading.setVisibility(View.GONE);
        }
    };

    @Override
    public View initView() {
        // 初始化视图
        View view = View.inflate(context, R.layout.net_video_pager, null);
        // 初始化组件
        lv_net_item = (ListView) view.findViewById(R.id.lv_net_item);
        pb_net_loading = (ProgressBar) view.findViewById(R.id.pb_net_loading);
        tv_no_more_net_media = (TextView) view.findViewById(R.id.tv_no_more_net_media);

        // 设置item点击监听
        lv_net_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 给自定义播放器传递视频列表
                Intent intent = new Intent(context, SystemVideoPlayer.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("videoList", mediaItemList);
                intent.putExtras(bundle);
                // 传递当前位置
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void initData() {
        super.initData();

        // 获取网络视频资源
        getMediaFromNetwork();
    }

    private void getMediaFromNetwork() {
        // 创建子线程--(Thread) run() -> {}
        new Thread() {
            @Override
            public void run() {
                super.run();

                // 从http请求数据
                URL url = null;
                try {
                    // 设置请求URL
                    url = new URL("http://api.m.mtime.cn/PageSubArea/TrailerList.api");
                    // 获得HTTPURLConnection对象
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    // 设置 Get请求
                    connection.setRequestMethod("GET");
                    // 设置链接超时
                    connection.setConnectTimeout(6000);
                    // 获取请求数据
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    String str = null;
                    StringBuffer sb = new StringBuffer();

                    while ((str = br.readLine()) != null) {
                        // 还有数据
                        sb.append(str);
                    }
                    br.close();
                    inputStream.close();

                    // 初始化获取到的Json数据
                    formatJsonString2Obj(sb.toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // 发送空消息
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    /**
     * @description 格式化json字符串成MediaItem
     * @author louiemain
     * @date Created on 2018/3/18 14:29
     * @param json
     * @return void
     */
    private void formatJsonString2Obj(String json) {
        mediaItemList = new ArrayList<MediaItem>();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            JSONArray trailers = jsonObject.optJSONArray("trailers");
            if (trailers != null && trailers.length() > 0) {
                // 遍历JsonArray
                for (int i = 0; i < trailers.length(); i++) {
                    JSONObject jsonObjectItem = (JSONObject) trailers.get(i);
                    if (jsonObjectItem != null) {
                        MediaItem mediaItem = new MediaItem();
                        mediaItem.setDisplayName(jsonObjectItem.getString("movieName"));
                        mediaItem.setData(jsonObjectItem.getString("hightUrl"));
                        mediaItem.setCoverImg(jsonObjectItem.getString("coverImg"));
                        mediaItem.setDesc(jsonObjectItem.getString("videoTitle"));

                        mediaItemList.add(mediaItem);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
