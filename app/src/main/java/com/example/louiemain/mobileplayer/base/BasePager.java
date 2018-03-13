package com.example.louiemain.mobileplayer.base;

import android.content.Context;
import android.view.View;

/**
 * @Program: MobilePlayer
 * @Type: Class
 * @Description: 页面公共组件
 * @Author: louiemain
 * @Create: 2018-03-13 12:38
 **/
public abstract class BasePager {

    // 是否已经初始化数据-（不是static，每个实例单独继承一个 默认为false）
    public boolean isInitData;
    // 上下文
    public final Context context;
    // 视图
    public final View rootView;

    public BasePager(Context context) {
        this.context = context;
        rootView = initView();
    }

    /** 
     * @Description: 初始化视图 abstract-必须由继承的子类对象来实现
     * @Author: louiemain 
     * @Date: 2018-03-13 12:41 
     * @param  
     * @return: android.view.View 
     */ 
    public abstract View initView();

    /** 
     * @Description: 初始化数据-联网获取或者数据绑定时实现该方法
     * @Author: louiemain 
     * @Date: 2018-03-13 12:42 
     * @param  
     * @return: void 
     */ 
    public void initData() {
        
    }
}
