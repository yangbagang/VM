package com.ybg.rp.vm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ybg.rp.vm.R;
import com.ybg.rp.vm.entity.db.LayerBean;
import com.ybg.rp.vm.entity.db.TrackBean;
import com.ybg.rp.vm.util.helper.DbSetHelper;
import com.ybg.rp.vm.util.helper.EntityDBUtil;

import java.util.ArrayList;

/**
 * 格子柜列表适配
 * <p>
 * 包   名:     com.ybg.rp.vm.adapter
 * 类   名:     CabinetListAdapter
 * 版权所有:     版权所有(C)2010-2016
 * 公   司:
 * 版   本:          V1.0
 * 时   间:     2016/6/29 0029 09:16
 * 作   者:     yuyucheng
 */
public class CabinetListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<LayerBean> cabinetList;
    private CabinetListener listener;
    private DbSetHelper dHelper;
    private EntityDBUtil dbUtil;

    public CabinetListAdapter(CabinetListener listener,Context context, ArrayList<LayerBean> cabinetList) {
        this.listener=listener;
        this.mContext = context;
        this.cabinetList = cabinetList;
        this.dHelper = DbSetHelper.getInstance(mContext);
        this.dbUtil = EntityDBUtil.getInstance();
    }

    @Override
    public int getCount() {
        return cabinetList.size();
    }

    @Override
    public Object getItem(int position) {
        return cabinetList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_cabinet, parent, false);
            holder = new ViewHolder();
            holder.tv_no = (TextView) convertView.findViewById(R.id.cabinet_item_tv_no);
            holder.tv_num = (TextView) convertView.findViewById(R.id.cabinet_item_tv_num);
            holder.tv_max = (TextView) convertView.findViewById(R.id.cabinet_item_tv_max);
            holder.ll_data = (LinearLayout) convertView.findViewById(R.id.cabinet_item_ll_data);
            holder.tv_delete = (TextView) convertView.findViewById(R.id.cabinet_item_tv_delete);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        LayerBean cabinet = cabinetList.get(position);
        holder.tv_no.setText(cabinet.getLayerNo()+ "柜");
        holder.tv_num.setText(String.valueOf(cabinet.getTrackNum()));
        int max = findMax(cabinet.getLayerNo(), cabinet.getTrackNum());
        holder.tv_max.setText(String.valueOf(max));

        holder.ll_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.modifyCabinet(position);
            }
        });

        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.deleteCabinet(position);
            }
        });

        return convertView;
    }

    class ViewHolder {
        private TextView tv_no;
        private TextView tv_num;
        private TextView tv_max;
        private LinearLayout ll_data;
        private TextView tv_delete;
    }

    /**
     * 查找对应格子的最大排放量
     */
    private int findMax(String layerNo, int trackNum) {
        int max = 1;

        try {
            /**读取轨道样例 获取统一的排放量*/
            TrackBean tb = dbUtil.getDb().selector(TrackBean.class).where("LAYER_NO", "=", layerNo).findFirst();
            if (tb != null) {
                max = tb.getMaxInventory();
            } else {
                dHelper.initCabinetTrack(layerNo, trackNum, max);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return max;
    }

    public interface CabinetListener{
        public void modifyCabinet(int position);
        public void deleteCabinet(int position);
    }


}
