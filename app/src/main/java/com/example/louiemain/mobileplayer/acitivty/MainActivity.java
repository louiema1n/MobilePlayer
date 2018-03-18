package com.example.louiemain.mobileplayer.acitivty;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.*;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import com.example.louiemain.mobileplayer.R;
import com.example.louiemain.mobileplayer.base.BasePager;
import com.example.louiemain.mobileplayer.pager.MusicPager;
import com.example.louiemain.mobileplayer.pager.NetMusicPager;
import com.example.louiemain.mobileplayer.pager.NetVideoPager;
import com.example.louiemain.mobileplayer.pager.VideoPager;

import java.util.ArrayList;
import java.util.List;

/**
 * @Program: MobilePlayer
 * @Type: Class
 * @Description:
 * @Author: louiemain
 * @Create: 2018-03-12 17:48
 **/
public class MainActivity extends FragmentActivity {

    private RadioGroup rg_bottom_tag;

    // pager集合
    private List<BasePager> basePagers;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // 实例化
        rg_bottom_tag = (RadioGroup) findViewById(R.id.rg_bottom_tag);

        // 初始化pager集合
        basePagers = new ArrayList<>();
        basePagers.add(new VideoPager(this));
        basePagers.add(new MusicPager(this));
        basePagers.add(new NetVideoPager(this));
        basePagers.add(new NetMusicPager(this));

        // 设置RadioGroup监听
        rg_bottom_tag.setOnCheckedChangeListener(new MyOnCheckedChangeListener());

        // 默认选中
        rg_bottom_tag.check(R.id.rb_video);

        // 动态获取权限
        grantExternalRW();
    }

    /**
     * @Description: 实现OnCheckedChangeListener
     * @Author: louiemain
     * @Date: 2018-03-13 12:58
     * @param
     * @return:
     */
    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {
        /**
         * @Description: 实现RadioGroup的改变
         * @Author: louiemain
         * @Date: 2018-03-13 12:59
         * @param group
         * @param checkedId 选中的id
         * @return: void
         */
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                default:
                    position = 0;
                    break;
                case R.id.rb_music:
                    position = 1;
                    break;
                case R.id.rb_net_video:
                    position = 2;
                    break;
                case R.id.rb_net_music:
                    position = 3;
                    break;
            }

            // 设置Fragment
            setFragment();
        }
    }

    /** 
     * @Description: 将视图添加到fragment中 
     * @Author: louiemain 
     * @Date: 2018-03-13 13:21 
     * @param  
     * @return: void 
     */ 
    private void setFragment() {

        // 1.得到FragmentManager
        FragmentManager manager = getSupportFragmentManager();
        // 2.开启事务
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        // 3.替换
        fragmentTransaction.replace(R.id.fl_main_content, new Fragment() {
            @Nullable
            @Override
            public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
                // 得到当前选中的pager
                BasePager checkedPager = getBasePager();
                if (checkedPager != null) {
                    // 不为空，返回其视图
                    return checkedPager.rootView;
                }
                return null;
            }
        });
        // 4.提交事务
        fragmentTransaction.commit();
    }

    /**
     * @Description: 获取选中的pager
     * @Author: louiemain
     * @Date: 2018-03-13 13:15
     * @param
     * @return: com.example.louiemain.mobileplayer.base.BasePager
     */
    private BasePager getBasePager() {
        BasePager basePager = basePagers.get(position);
        if (basePager != null && !basePager.isInitData) {
            // 点击到某个选项，初始化其数据
            basePager.initData();
            basePager.isInitData = true;
            Log.i("Moblie", basePager.getClass().getSimpleName() + "被初始化了");
        }
        return basePager;
    }

    /**
     * @description 动态获取外部存储权限
     * Android 6.0以上权限问题
     * @author louiemain
     * @date Created on 2018/3/13 23:48
     * @param
     * @return void
     */
    public void grantExternalRW() {
        // 是否有被动态授权
        if (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 没有，动态申请权限
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
            }, 1);
        }
    }

    public void test(View view) {
        Intent intent = new Intent();
//        intent.setDataAndType(Uri.parse("http://192.168.1.103:8080/test.mp4"), "video/*");
        intent.setDataAndType(Uri.parse("http://vfx.mtime.cn/Video/2018/03/14/mp4/180314004557773986.mp4"), "video/*");

        startActivity(intent);
    }
}
