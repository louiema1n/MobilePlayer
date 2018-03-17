package com.example.louiemain.mobileplayer.utils;/**
 * @description
 * @author&date Created by louiemain on 2018/3/17 23:09
 */

/**
 * @Pragram: MobilePlayer
 * @Type: Class
 * @Description: 网络相关操作工具类
 * @Author: louiemain
 * @Created: 2018/3/17 23:09
 **/
public class NetWork {

    /**
     * @description 判断是否为网络资源
     * @author louiemain
     * @date Created on 2018/3/17 23:11
     * @param uri
     * @return boolean
     */
    public boolean isNetworkResource(String uri) {
        boolean isNr = false;
        uri = uri.toLowerCase();
        if (uri.startsWith("http") || uri.startsWith("rtsp") || uri.startsWith("mms")) {
            isNr = true;
        }
        return isNr;
    }
}
