package com.example.louiemain.mobileplayer.utils;/**
 * @description
 * @author&date Created by louiemain on 2018/3/17 23:09
 */

import android.content.Context;
import android.net.TrafficStats;

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
     * @description 根据上下文获取某一时间段内的网速
     * @author louiemain
     * @date Created on 2018/3/18 9:49
     * @param context
     * @return java.lang.String
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
}
