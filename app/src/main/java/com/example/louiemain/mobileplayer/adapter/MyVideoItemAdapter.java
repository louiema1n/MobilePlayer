package com.example.louiemain.mobileplayer.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.louiemain.mobileplayer.R;
import com.example.louiemain.mobileplayer.domain.MediaItem;
import com.example.louiemain.mobileplayer.utils.Time;

import java.util.ArrayList;

/**
 * @Program: MobilePlayer
 * @Type: Class
 * @Description: 视频item pager适配器
 * @Author: louiemain
 * @Create: 2018-03-14 11:00
 **/
public class MyVideoItemAdapter extends BaseAdapter {

    private Time time;

    private final Context context;
    private final ArrayList<MediaItem> mediaItems;

    public MyVideoItemAdapter(Context context, ArrayList<MediaItem> mediaItems) {
        this.context = context;
        this.mediaItems = mediaItems;
        this.time = new Time();
    }

    @Override
    public int getCount() {
        return mediaItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoder viewHoder;
        if (convertView == null) {
            // 实例化item视图
            convertView = View.inflate(context, R.layout.rl_video_item, null);
            // 获取相应的组件
            viewHoder = new ViewHoder();
            viewHoder.iv_ic_video = (ImageView) convertView.findViewById(R.id.iv_ic_video);
            viewHoder.tv_display_name = (TextView) convertView.findViewById(R.id.tv_display_name);
            viewHoder.tv_duration = (TextView) convertView.findViewById(R.id.tv_duration);
            viewHoder.tv_size = (TextView) convertView.findViewById(R.id.tv_size);

            convertView.setTag(viewHoder);
        } else {
            viewHoder = (ViewHoder) convertView.getTag();
        }

        // 设置对应组件
        MediaItem mediaItem = mediaItems.get(position);

        viewHoder.tv_display_name.setText(mediaItem.getDisplayName());
        viewHoder.tv_duration.setText(String.valueOf(time.millisecond2Time((int) mediaItem.getDuration())));
        viewHoder.tv_size.setText(Formatter.formatFileSize(context, mediaItem.getSize()));

        return convertView;
    }

    static class ViewHoder {
        ImageView iv_ic_video;
        TextView tv_display_name;
        TextView tv_duration;
        TextView tv_size;
    }
}
