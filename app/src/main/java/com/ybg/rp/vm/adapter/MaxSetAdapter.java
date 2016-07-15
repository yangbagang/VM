package com.ybg.rp.vm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ybg.rp.vm.R;
import com.ybg.rp.vm.entity.db.TrackBean;

import java.util.ArrayList;

/**
 * 最大排放量
 *
 * 包            名:      com.ybg.rp.vm.adapter
 * 类            名:      MaxSetAdapter
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公            司:
 *
 * @author yuyucheng
 * @version V1.0
 * @date 2016/6/23 0023
 */
public class MaxSetAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<TrackBean> trackList;

    public MaxSetAdapter(Context context, ArrayList<TrackBean> trackList) {
        this.mContext = context;
        this.trackList = trackList;
    }

    @Override
    public int getCount() {
        return trackList.size();
    }

    @Override
    public Object getItem(int position) {
        return trackList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_max,null);
            holder = new ViewHolder();
            holder.tv_layer = (TextView) convertView.findViewById(R.id.item_maxSet_tv_layer);
            holder.tv_max = (TextView) convertView.findViewById(R.id.item_maxSet_tv_num);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TrackBean bean = trackList.get(position);
        holder.tv_layer.setText(bean.getTrackNo());
        /**排放量*/
        int selectMax = bean.getMaxInventory();
        holder.tv_max.setText(String.valueOf(selectMax));
        
        return convertView;
    }

    class ViewHolder{
        TextView tv_layer;
        TextView tv_max;
    }
}
