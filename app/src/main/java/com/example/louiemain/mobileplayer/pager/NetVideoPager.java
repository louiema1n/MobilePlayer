package com.example.louiemain.mobileplayer.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import com.example.louiemain.mobileplayer.base.BasePager;

/**
 * @Program: MobilePlayer
 * @Type: Class
 * @Description: 网络视频pager
 * @Author: louiemain
 * @Create: 2018-03-13 12:44
 **/
public class NetVideoPager extends BasePager {

    private TextView textView;

    public NetVideoPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        // 父类context必须设为public
        textView = new TextView(context);
        textView.setTextColor(Color.RED);
        textView.setTextSize(30);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        textView.setText("这是网络视频。");
    }
}
