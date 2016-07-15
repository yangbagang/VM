package com.ybg.rp.vm.activity.setting.baseload;

import android.os.AsyncTask;
import android.os.Bundle;

import com.cnpay.tigerbalm.utils.CharacterUtil;
import com.cnpay.tigerbalm.utils.GsonUtils;
import com.cnpay.tigerbalm.utils.TbLog;
import com.google.gson.reflect.TypeToken;
import com.ybg.rp.vm.R;
import com.ybg.rp.vm.activity.base.BaseActivity;
import com.ybg.rp.vm.entity.db.LayerBean;
import com.ybg.rp.vm.entity.db.TrackBean;
import com.ybg.rp.vm.net.NetWorkUtil;
import com.ybg.rp.vm.net.YFHttpListener;
import com.ybg.rp.vm.util.helper.EntityDBUtil;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 初始化机器基础数据
 * <p/>
 * 包   名:     com.ybg.rp.vm.activity.setting.baseload
 * 类   名:     DBImportActivity
 * 版权所有:     版权所有(C)2010-2016
 * 公   司:
 * 版   本:          V1.0
 * 时   间:     2016/7/6 0006 09:43
 * 作   者:     yuyucheng
 */
public class DBImportActivity extends BaseActivity {
    private ArrayList<DBLayerBean> dbList;
    private EntityDBUtil dbUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitle("数据初始化");
        setContentLayout(R.layout.activity_main);
        dbUtil=EntityDBUtil.getInstance();
        loadData();
    }

    private void loadData() {
        /**从服务器获取数据*/
        dbList = new ArrayList<>();

        NetWorkUtil net = NetWorkUtil.getInstance();
        Request<String> request = net.post("abc");
        request.add("machineId", "abc");//机器ID
        net.add(DBImportActivity.this, 10086, request, new YFHttpListener<String>() {
            @Override
            public void onSuccess(int what, Response<String> response) {
                try {
                    String result = response.get();
                    JSONObject json = new JSONObject(result);
                    Type type = new TypeToken<ArrayList<DBLayerBean>>() {
                    }.getType();
                    ArrayList<DBLayerBean> list = GsonUtils.createGson().fromJson(json.getString("data"), type);
                    if (list.size() > 0) {
                        dbList.addAll(list);
                        /**设置数据*/
                        setDbData(dbList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int what, String msg) {
                TbLog.e(msg);
            }
        });
    }

    /**
     * 设置数据库数据
     *
     * @param data 服务器获取到的基础数据
     */
    private void setDbData(ArrayList<DBLayerBean> data) {
        for (int i=0;i<data.size();i++){
            DBLayerBean bean=data.get(i);
            initLayer(bean.getLayerNo(),bean.getTrackNum(),bean.getTrackMax(),bean.getGridMark());
        }
    }

    /**
     * 初始化格子柜 主机信息
     * 基础层级信息 统一排放量
     *
     * @param max  排放量
     * @param mark 标记   1：格子柜,0：不是格子柜
     */
    public void initLayer(final String layerNo,final int trackNum, final int max, final int mark){
        new AsyncTask<String,Integer,Boolean>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
            }

            @Override
            protected Boolean doInBackground(String... params) {
                try {
                    LayerBean lb = dbUtil.getDb().selector(LayerBean.class).where("LAYER_NO", "=", layerNo).findFirst();
                    if (lb == null) {
                        /**没有就创建*/
                        lb = new LayerBean();
                        lb.setLayerNo(layerNo);//层编号
                    }
                    lb.setTrackNum(trackNum);
                    lb.setGridMark(mark);//1：格子柜,0：不是格子柜
                    dbUtil.getDb().saveOrUpdate(lb);
                    /**设置对应排放量*/
                    initLayerTrack(layerNo,trackNum,max,mark);
                }catch (Exception e){
                    e.printStackTrace();
                }

                return true;
            }
        }.execute();
    }


    /**
     * 初始化格子柜 主机信息
     * 基础轨道信息 统一排放量
     *
     * @param max  排放量
     * @param mark 标记   1：格子柜,0：不是格子柜
     */
    public boolean initLayerTrack(String layerNo, int trackNum, int max,int mark) {
        try {
            List<TrackBean> trackBeans = dbUtil.findAll(TrackBean.class, "LAYER_NO", "=", layerNo);
            if (null!=trackBeans && trackBeans.size()>0){
                TrackBean bean=trackBeans.get(0);
                if (trackBeans.size() == trackNum && bean.getMaxInventory()==max) {
                    /**轨道数相同 ,统一排放量相同*/
                    return false;
                }else {
                    for (int i = 0; i < trackBeans.size(); i++) {
                        dbUtil.getDb().delete(trackBeans.get(i));
                    }
                    TbLog.e("删除数据(TRACK_VENDING) - " + layerNo);
                }
            }

            for (int j = 1; j <= trackNum; j++) {
                TrackBean tb = new TrackBean();
                tb.setFault(TrackBean.FAULT_O);
                tb.setLayerNo(layerNo);//层级编号 01-08,1-6
                if (mark==0){
                    //011 021 主机轨道数<10 不需要自动补0
                    tb.setTrackNo(layerNo + "" + j + "");
                }else if (mark==1){
                    //101 111, 11 ,01 格子柜需要自动补0
                    String num = CharacterUtil.autoCompZero(String.valueOf(j), 2);
                    tb.setTrackNo(layerNo + num);//102,201,312
                }
                tb.setGridMark(mark);//1：格子柜,0：不是格子柜
                tb.setMaxInventory(max);//库存
                dbUtil.saveOrUpdate(tb);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
