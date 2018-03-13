package com.example.louiemain.mobileplayer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.louiemain.mobileplayer.R;

/**
 * @description 自定义组件
 * @author&date Created by louiemain on 2018/3/13 21:41
 */
public class TopBar extends LinearLayout implements View.OnClickListener {

    private View top_search;    // 搜索
    private View top_game;      // 游戏
    private View top_history;   // 历史记录

    // 上下文
    private Context context;

    /**
     * 代码中使用
     * @param context
     */
    public TopBar(Context context) {
        this(context, null);
    }

    /**
     * 布局文件中使用
     * @param context
     * @param attrs
     */
    public TopBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 样式文件中使用
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public TopBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    /**
     * @description 当布局文件加载完成的时候回调此方法
     * @author louiemain
     * @date Created on 2018/3/13 21:47
     * @param
     * @return void
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // 得到实例化的组件
        top_search = getChildAt(1);
        top_game = getChildAt(2);
        top_history = getChildAt(3);
        // 分别设置监听当前组件的点击事件
        top_search.setOnClickListener(this);
        top_game.setOnClickListener(this);
        top_history.setOnClickListener(this);
    }

    /**
     * @description 点击回调
     * @author louiemain
     * @date Created on 2018/3/13 21:54
     * @param v
     * @return void
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_search:
                Toast.makeText(context, "搜索", Toast.LENGTH_SHORT).show();
                break;
            case R.id.top_game:
                Toast.makeText(context, "游戏", Toast.LENGTH_SHORT).show();
                break;
            case R.id.top_history:
                Toast.makeText(context, "历史记录", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
