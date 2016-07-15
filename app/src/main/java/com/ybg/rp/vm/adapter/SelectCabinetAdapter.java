package com.ybg.rp.vm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ybg.rp.vm.R;
import com.ybg.rp.vm.entity.TrackError;

import java.util.ArrayList;

/**
 * 包            名:      com.ybg.rp.vm.adapter
 * 类            名:      SelectCabinetAdapter
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公            司:
 *
 * @author yuyucheng
 * @version V1.0
 * @date 2016/6/28 0028
 */
public class SelectCabinetAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<TrackError> dataList;

    public SelectCabinetAdapter(Context context, ArrayList<TrackError> dataList) {
        this.mContext = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_list_select,parent,false);
            holder=new ViewHolder();
            holder.tv_num=(TextView)convertView.findViewById(R.id.select_item_tv_num);

            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        TrackError bean=dataList.get(position);
        holder.tv_num.setText(bean.getLayerNo());
        boolean isSelect=bean.isSelect();
        if (isSelect){
            holder.tv_num.setTextColor(mContext.getResources().getColor(R.color.green_btn_light));
            holder.tv_num.setBackgroundResource(R.drawable.border_layout_green_dark);
        }else {
            holder.tv_num.setBackgroundResource(R.drawable.border_layout_white);
            holder.tv_num.setTextColor(mContext.getResources().getColor(R.color.default_text_white));
        }
        return convertView;

    }

    class ViewHolder {
        private TextView tv_num;
    }
}
