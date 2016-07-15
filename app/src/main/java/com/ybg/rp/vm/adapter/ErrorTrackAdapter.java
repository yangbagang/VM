package com.ybg.rp.vm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ybg.rp.vm.R;
import com.ybg.rp.vm.entity.TrackError;
import com.ybg.rp.vm.entity.db.TrackBean;

import java.util.ArrayList;

/**
 * 包            名:      com.ybg.rp.vm.adapter
 * 类            名:      ErrorTrackAdapter
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公            司:
 *
 * @author yuyucheng
 * @version V1.0
 * @date 2016/6/23 0023
 */
public class ErrorTrackAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<TrackError> errorList;

    public ErrorTrackAdapter(Context context, ArrayList<TrackError> errorList) {
        this.mContext = context;
        this.errorList = errorList;
    }

    @Override
    public int getCount() {
        return errorList.size();
    }

    @Override
    public Object getItem(int position) {
        return errorList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_list_test,parent,false);
            holder=new ViewHolder();
            holder.tv_track=(TextView)convertView.findViewById(R.id.testBtn_tv_layer);

            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        TrackError bean=errorList.get(position);
        holder.tv_track.setText(bean.getTrackNo());
        boolean isSelect=bean.isSelect();
        if (isSelect){
            holder.tv_track.setTextColor(mContext.getResources().getColor(R.color.green_btn_light));
            holder.tv_track.setBackgroundResource(R.drawable.border_bg_green_dark);
        }else {
            holder.tv_track.setBackgroundResource(R.drawable.border_bg_green);
            holder.tv_track.setTextColor(mContext.getResources().getColor(R.color.default_text_white));
        }
        return convertView;
    }

    class ViewHolder {
        private TextView tv_track;
    }

    /**
     * 初始化数据
     */
    public static ArrayList<TrackError> initErrorTrack(ArrayList<TrackBean> list, int position) {
        ArrayList<TrackError> errors = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            TrackBean bean = list.get(i);
            TrackError track = new TrackError();
            track.setLayerNo(bean.getLayerNo());
            track.setTrackNo(bean.getTrackNo());
            track.setIsSelect(false);

            errors.add(track);
        }
        if (position < errors.size()) {
            errors.get(position).setIsSelect(true);
        }
        return errors;
    }

    /**
     * 全选 全不选
     */
    public static ArrayList<TrackError> setAllSelet(boolean selectAll, ArrayList<TrackError> list) {
        ArrayList<TrackError> errors = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setIsSelect(selectAll);
        }
        errors.addAll(list);
        return errors;
    }

    /**
     * 获取选中的下标
     */
    public static ArrayList<Integer> getSelectTrack(ArrayList<TrackError> list) {
        ArrayList<Integer> indexList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            TrackError error = list.get(i);
            if (error.isSelect()) {
                indexList.add(i);
            }
        }
        return indexList;
    }
}
