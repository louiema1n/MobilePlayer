package com.example.louiemain.mobileplayer.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.louiemain.mobileplayer.R;
import com.example.louiemain.mobileplayer.domain.MediaItem;
import com.example.louiemain.mobileplayer.utils.NetWork;
import com.example.louiemain.mobileplayer.utils.Time;

import java.util.List;

/**
 * @Program: MobilePlayer
 * @Type: Class
 * @Description: 视频item pager适配器
 * @Author: louiemain
 * @Create: 2018-03-14 11:00
 **/
public class MyNetworkVideoItemAdapter extends BaseAdapter {

    private Time time;

    private Context context;
    private List<MediaItem> mediaItems;

    public MyNetworkVideoItemAdapter(Context context, List<MediaItem> mediaItems) {
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
        final ViewHolder viewHolder;
        if (convertView == null) {
            // 实例化item视图
            convertView = View.inflate(context, R.layout.rl_net_video_item, null);
            // 获取相应的组件
            viewHolder = new ViewHolder();
            viewHolder.iv_video_image = (ImageView) convertView.findViewById(R.id.iv_video_image);
            viewHolder.tv_net_video_name = (TextView) convertView.findViewById(R.id.tv_net_video_name);
            viewHolder.tv_desc = (TextView) convertView.findViewById(R.id.tv_desc);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 设置对应组件
        MediaItem mediaItem = mediaItems.get(position);

        new NetWork().bindViewAndResource(viewHolder.iv_video_image, mediaItem.getCoverImg());
        viewHolder.tv_net_video_name.setText(mediaItem.getDisplayName());
        viewHolder.tv_desc.setText(mediaItem.getDesc());

        return convertView;
    }

    static class ViewHolder {
        private ImageView iv_video_image;
        private TextView tv_net_video_name;
        private TextView tv_desc;

    }

}
