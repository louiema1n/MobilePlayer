package com.example.louiemain.mobileplayer.utils;

import java.util.Formatter;
import java.util.Locale;

/**
 * @Program: MobilePlayer
 * @Type: Class
 * @Description: 时间相关操作工具类
 * @Author: louiemain
 * @Create: 2018-03-14 10:01
 **/
public class Time {

    private StringBuilder stringBuilder;
    private Formatter formatter;

    public Time() {
        this.stringBuilder = new StringBuilder();
        this.formatter = new Formatter(stringBuilder, Locale.getDefault());
    }

    /**
     * @Description: 毫秒转成时间
     * @Author: louiemain
     * @Date: 2018-03-14 10:03
     * @param ms 毫秒
     * @return: java.lang.String
     */
    public String millisecond2Time(int ms) {
        int totalSecends = ms / 1000;
        int secends = totalSecends % 60;
        int minutes = (totalSecends / 60) % 60;
        int hours = totalSecends / 3600;

        stringBuilder.setLength(0);
        if (hours > 0) {
            return formatter.format("%d:%02d:%02d", hours, minutes, secends).toString();
        } else {
            return formatter.format("%02d:%02d", minutes, secends).toString();
        }
    }
}
