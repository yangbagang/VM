package com.ybg.rp.vm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ybg.rp.vm.R;
import com.ybg.rp.vm.entity.db.LayerBean;

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
public class TestLayerAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<LayerBean> lyList;

    public TestLayerAdapter(Context context, ArrayList<LayerBean> lyList) {
        this.mContext = context;
        this.lyList = lyList;
    }

    @Override
    public int getCount() {
        return lyList.size();
    }

    @Override
    public Object getItem(int position) {
        return lyList.get(position);
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
            holder.tv_layer=(TextView)convertView.findViewById(R.id.testBtn_tv_layer);

            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        LayerBean bean=lyList.get(position);
        int mark=bean.getGridMark();
        /**1：格子柜,0：不是格子柜*/
        if (mark==1){
            holder.tv_layer.setText(bean.getLayerNo()+"格子柜-"+bean.getTrackNum());
        }else if (mark==0){
            holder.tv_layer.setText("第"+Integer.parseInt(bean.getLayerNo())+"层测试");
        }
        return convertView;
    }

    class ViewHolder{
        private TextView tv_layer;
    }
}
