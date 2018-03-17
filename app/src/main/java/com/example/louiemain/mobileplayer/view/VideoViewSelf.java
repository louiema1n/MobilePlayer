package com.example.louiemain.mobileplayer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.VideoView;

/**
 * @description 自定义VideoView
 * @author&date Created by louiemain on 2018/3/17 16:09
 */
public class VideoViewSelf extends VideoView {
    /**
     * 代码中使用
     * @param context
     */
    public VideoViewSelf(Context context) {
        this(context, null);
    }

    /**
     * 布局文件中使用
     * @param context
     * @param attrs
     */
    public VideoViewSelf(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 样式文件中使用
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public VideoViewSelf(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 测量
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * @description 设置视频播放大小
     * @author louiemain
     * @date Created on 2018/3/17 16:17
     * @param videoWidth 视频宽度
     * @param videoHeight 视频高度
     * @return void
     */
    public void setVideoSize(int videoWidth, int videoHeight) {
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = videoHeight;
        params.width = videoWidth;
//        requestLayout();
        setLayoutParams(params);
    }
}
