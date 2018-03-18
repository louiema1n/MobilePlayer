package com.example.louiemain.mobileplayer.utils;/**
 * @description
 * @author&date Created by louiemain on 2018/3/17 23:09
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @Pragram: MobilePlayer
 * @Type: Class
 * @Description: 网络相关操作工具类
 * @Author: louiemain
 * @Created: 2018/3/17 23:09
 **/
public class NetWork {

    private long lastTimeStamp;     // 上一次时间
    private long lastTotalRxBytes;  // 上一次数据量

    /**
     * @param uri
     * @return boolean
     * @description 判断是否为网络资源
     * @author louiemain
     * @date Created on 2018/3/17 23:11
     */
    public boolean isNetworkResource(String uri) {
        boolean isNr = false;
        uri = uri.toLowerCase();
        if (uri.startsWith("http") || uri.startsWith("rtsp") || uri.startsWith("mms")) {
            isNr = true;
        }
        return isNr;
    }

    /**
     * @param context
     * @return java.lang.String
     * @description 根据上下文获取某一时间段内的网速
     * @author louiemain
     * @date Created on 2018/3/18 9:49
     */
    public String getNetSpeed(Context context) {
        String netSpeed = "0 kb/s";
        // 获取supported的网络数据量
        long nowTotalRxBytes = TrafficStats.getUidRxBytes(context.getApplicationInfo().uid) == TrafficStats.UNSUPPORTED ? 0 : (TrafficStats.getTotalRxBytes() / 1024);  // 转换成kb
        // 获取当前时间戳
        long nowTimeStamp = System.currentTimeMillis();
        // 计算网速
        long speed = (nowTotalRxBytes - lastTotalRxBytes) * 1000 / (nowTimeStamp - lastTimeStamp);  // 毫秒转换

        lastTotalRxBytes = nowTotalRxBytes;
        lastTimeStamp = nowTimeStamp;

        netSpeed = String.valueOf(speed) + " kb/s";
        return netSpeed;
    }

    /**
     * @description 在子线程中绑定视图
     * @author louiemain
     * @date Created on 2018/3/18 20:05
     * @param null
     * @return
     */
    private Bitmap bitmap;
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    public Bitmap getBitmap() {
        return bitmap;
    }
    private ImageView iv;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            iv.setImageBitmap(getBitmap());
        }
    };
    public void bindViewAndResource(View view, String url) {
        final String str = url;
        iv = (ImageView) view;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                URL bmpUrl = null;
                try {
                    bmpUrl = new URL(str);
                    HttpURLConnection conn = (HttpURLConnection) bmpUrl.openConnection();
                    conn.setConnectTimeout(3000);
                    conn.setReadTimeout(3000);
                    conn.setDoInput(true);

                    InputStream is = conn.getInputStream();
                    setBitmap(BitmapFactory.decodeStream(is));
                    is.close();

                    handler.sendEmptyMessage(0);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }
}
