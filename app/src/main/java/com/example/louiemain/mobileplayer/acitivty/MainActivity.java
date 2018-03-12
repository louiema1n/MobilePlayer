package com.example.louiemain.mobileplayer.acitivty;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.example.louiemain.mobileplayer.R;

/**
 * @Program: MobilePlayer
 * @Type: Class
 * @Description:
 * @Author: louiemain
 * @Create: 2018-03-12 17:48
 **/
public class MainActivity extends Activity {

    private RadioGroup rg_bottom_tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        
        // 实例化
        rg_bottom_tag = (RadioGroup) findViewById(R.id.rg_bottom_tag);

        // 默认选中
        rg_bottom_tag.check(R.id.rb_video);

    }
}
