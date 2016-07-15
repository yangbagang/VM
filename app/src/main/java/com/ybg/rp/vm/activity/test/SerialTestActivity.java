package com.ybg.rp.vm.activity.test;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

import com.cnpay.tigerbalm.utils.TbLog;
import com.ybg.rp.vm.R;
import com.ybg.rp.vm.activity.base.BaseActivity;
import com.ybg.rp.vm.entity.db.TrackBean;
import com.ybg.rp.vm.serial.BeanTrackSet;
import com.ybg.rp.vm.serial.SerialManager;
import com.ybg.rp.vm.util.Config;
import com.ybg.rp.vm.util.helper.EntityDBUtil;

import java.util.ArrayList;

/**
 * 包   名:     com.ybg.rp.vm.activity.test
 * 类   名:     SerialTestActivity
 * 版权所有:     版权所有(C)2010-2016
 * 公   司:
 * 版   本:          V1.0
 * 时   间:     2016/7/1 0001 11:44
 * 作   者:     yuyucheng
 */
public class SerialTestActivity extends BaseActivity implements View.OnClickListener{
    private TextView tv_main,tv_cabinet,tv_result;
    private EntityDBUtil dbUtil;
    private SerialManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial_test);
        initTitle("测试页面");

        dbUtil=EntityDBUtil.getInstance();
        manager=SerialManager.getInstance(this.getApplicationContext());

        tv_main=(TextView)findViewById(R.id.serial_tv_main);
        tv_cabinet=(TextView)findViewById(R.id.serial_tv_cabinet);
        tv_result=(TextView)findViewById(R.id.serial_tv_result);

        tv_main.setOnClickListener(this);
        tv_cabinet.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.serial_tv_main:
                testMainLayer("03");
                break;
            case R.id.serial_tv_cabinet:
                testCabinetLayer("1");
                break;
        }
    }

    /**
     * 测试主机 单层轨道
     *
     * @param layerBean 层级数据
     *
     */
    public void testMainLayer(final String layerBean) {
        new AsyncTask<String, Integer, ArrayList<String>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                /**启动*/
            }

            @Override
            protected void onPostExecute(ArrayList<String> list) {
                super.onPostExecute(list);
                /**返回*/
                TbLog.i(" result= " + list.toString());
                tv_result.setText(list.toString());
            }

            @Override
            protected ArrayList<String> doInBackground(String... params) {
                ArrayList<String> listStr = new ArrayList<String>();
                try {
                    TbLog.i("打开轨道(层)：" + layerBean);
                    ArrayList<TrackBean> list = (ArrayList<TrackBean>) dbUtil.getDb().selector(TrackBean.class).where("GRID_MARK", "=", "0")
                            .and("LAYER_NO", "=", layerBean).orderBy("TRACK_NO").findAll();

                    TbLog.i("打开轨道(层)：size=" + list.size());


                    manager.createSerial(1);// 1:主机 2:格子柜
                    if (null != list && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            TrackBean track = list.get(i);
                            String str = track.getTrackNo() + "轨道：";
                            BeanTrackSet trackSet =manager.openMachineTrack(track.getTrackNo());
                            if (trackSet.trackstatus == 1) {
                                str += "电机正常";
                            } else {
                                str += trackSet.errorinfo;
                                track.setFault(TrackBean.FAULT_E);
                                dbUtil.saveOrUpdate(track);
                                TbLog.e("轨道错误-------" + str);
                            }
                            listStr.add(str);
                            SystemClock.sleep(Config.CYCLE_INTERVAL);
                        }
                    }
                    manager.closeSerial();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return listStr;
            }
        }.execute();
    }


    /**
     * 测试格子柜 单个格子柜
     *
     * @param layer    层级轨道数据
     */
    public void testCabinetLayer(final String layer) {
        new AsyncTask<String, Integer, ArrayList<String>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                /**启动*/
            }

            @Override
            protected void onPostExecute(ArrayList<String> list) {
                super.onPostExecute(list);
                /**返回*/
                TbLog.i(" result= "+list.toString());
                tv_result.setText(list.toString());
            }

            @Override
            protected ArrayList<String> doInBackground(String... params) {
                ArrayList<String> listStr = new ArrayList<String>();
                try {
                    TbLog.i("打开格子柜(层)：" + layer);
                    ArrayList<TrackBean> itemList = (ArrayList<TrackBean>) dbUtil.getDb().selector(TrackBean.class).
                            where("LAYER_NO", "=", layer).and("GRID_MARK", "=", "1").orderBy("TRACK_NO").findAll();

                    TbLog.i("打开轨道(层)：size=" + itemList.size());

                    manager.createSerial(2);// 1:主机 2:格子柜
                    if (null != itemList && itemList.size() > 0) {
                        for (int i = 0; i < itemList.size(); i++) {
                            TrackBean track = itemList.get(i);
                            TbLog.i("-----TrackNo = " + track.getTrackNo());
                            String str = track.getTrackNo() + "-格子轨道：";
                            BeanTrackSet trackSet = manager.openMachineTrack(track.getTrackNo());
                            if (trackSet.trackstatus == 1) {
                                str += "电机正常";
                            } else {
                                str += trackSet.errorinfo;
                                track.setFault(TrackBean.FAULT_E);
                                dbUtil.saveOrUpdate(track);
                                TbLog.e("轨道错误-------" + str);
                            }
                            listStr.add(str);
                            SystemClock.sleep(Config.CYCLE_INTERVAL);
                        }
                    }
                    manager.closeSerial();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return listStr;
            }
        }.execute();
    }


}
