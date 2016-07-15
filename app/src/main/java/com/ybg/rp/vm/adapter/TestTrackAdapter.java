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
 * 包            名:      com.ybg.rp.vm.adapter
 * 类            名:      TestTrackAdapter
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公            司:
 *
 * @author yuyucheng
 * @version V1.0
 * @date 2016/6/22 0022
 */
public class TestTrackAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<TrackBean> trackList;

    public TestTrackAdapter(Context context, ArrayList<TrackBean> trackList) {
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
        ViewHolder holder;
        if (convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_list_test,null);
            holder=new ViewHolder();
            holder.tv_track=(TextView)convertView.findViewById(R.id.testBtn_tv_layer);

            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        TrackBean bean=trackList.get(position);
        holder.tv_track.setText(bean.getTrackNo()+"测试");
        return convertView;
    }

    class ViewHolder{
        private TextView tv_track;
    }
}
