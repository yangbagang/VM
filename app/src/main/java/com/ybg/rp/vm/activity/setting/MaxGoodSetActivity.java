package com.ybg.rp.vm.activity.setting;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.cnpay.tigerbalm.utils.StrUtil;
import com.ybg.rp.vm.R;
import com.ybg.rp.vm.activity.base.BaseActivity;
import com.ybg.rp.vm.adapter.MaxSetAdapter;
import com.ybg.rp.vm.entity.db.LayerBean;
import com.ybg.rp.vm.entity.db.TrackBean;
import com.ybg.rp.vm.listener.ResultCallback;
import com.ybg.rp.vm.util.helper.DbSetHelper;
import com.ybg.rp.vm.util.helper.EntityDBUtil;
import com.ybg.rp.vm.util.dialog.BaseProgressUtils;
import com.ybg.rp.vm.util.view.MaxSelectDialog;

import java.util.ArrayList;

/**
 * 最大排放量
 * <p>
 * 包            名:      com.ybg.rp.vm.activity.setting
 * 类            名:      MaxGoodSetActivity
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公            司:
 *
 * @author yuyucheng
 * @version V1.0
 * @date 2016/6/20 0020
 */
public class MaxGoodSetActivity extends BaseActivity {
    private TextView tv_title, tv_all;
    private ListView lv_track;

    private DbSetHelper helper;
    private MaxSetAdapter adapter;
    private ArrayList<TrackBean> trackList;
    private String selectLayer;//选中主机轨道
    private MaxSelectDialog selectDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_manage_max);
        initTitle("最大排放量");

        Intent intent = getIntent();
        selectLayer = intent.getStringExtra("layer");

        helper = DbSetHelper.getInstance(this);
        findLayout();
        loadData();
    }

    private void findLayout() {
        tv_title = (TextView) findViewById(R.id.maxSet_tv_layer);
        tv_all = (TextView) findViewById(R.id.maxSet_tv_changeAll);
        lv_track = (ListView) findViewById(R.id.maxSet_lv_track);
    }

    /**
     * 加载数据
     */
    private void loadData() {
        trackList = new ArrayList<>();
        trackList.addAll(helper.getTrackList(selectLayer));
        if (trackList.size() < 0) {
            /**数据不存在就初始化*/
            initLayout(selectLayer);
        }
        tv_title.setText("第" + Integer.parseInt(selectLayer) + "层排放数设置");
        tv_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**设置全部*/
                showSelect(null);
            }
        });
        adapter = new MaxSetAdapter(MaxGoodSetActivity.this, trackList);
        lv_track.setAdapter(adapter);
        lv_track.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /**点击单个 设置最大排放量*/
                TrackBean bean=trackList.get(position);
                showSelect(bean.getTrackNo());
            }
        });
    }

    /**
     * 如果数据为空
     * 初始化对应层级
     */
    private void initLayout(final String layerNo) {
        try {
            EntityDBUtil dbUtil = EntityDBUtil.getInstance();
            LayerBean lb = dbUtil.getDb().selector(LayerBean.class).where("LAYER_NO", "=", layerNo).findFirst();
            helper.setMainTrack(layerNo, lb.getTrackNum());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 设置单个格子
     */
    private void setTack(final String trackNo, final int max) {
        new AsyncTask<String, Integer, Boolean>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                BaseProgressUtils.showBaseDialog(MaxGoodSetActivity.this, "");
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (aBoolean) {
                    adapter.notifyDataSetChanged();
                }
                BaseProgressUtils.cancelBaseDialog();
            }

            @Override
            protected Boolean doInBackground(String... params) {
                helper.setTrackmax(trackNo, max);

                trackList.clear();
                trackList.addAll(helper.getTrackList(selectLayer));
                return true;
            }
        }.execute();
    }

    /**
     * 设置全部格子的排放量
     */
    private void setAllTrack(final int maxValue) {
        new AsyncTask<String, Integer, Boolean>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //YFDialogUtil.showLoadding(MaxGoodSetActivity.this);
                BaseProgressUtils.showBaseDialog(MaxGoodSetActivity.this,"");
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (aBoolean) {
                    adapter.notifyDataSetChanged();
                }
                //YFDialogUtil.removeDialog(MaxGoodSetActivity.this);
                BaseProgressUtils.cancelBaseDialog();
            }

            @Override
            protected Boolean doInBackground(String... params) {
                if (trackList.size() > 0 && adapter != null) {
                    for (int i = 0; i < trackList.size(); i++) {
                        trackList.get(i).setMaxInventory(maxValue);
                        /**写入数据库*/
                        helper.setTrackmax(trackList.get(i).getTrackNo(), maxValue);
                    }
                    return true;
                }
                return false;
            }
        }.execute();

    }

    /**
     * 弹框选择
     * @param trackNo 对应轨道或者格子 可为空
     */
    private void showSelect(final String trackNo) {
        selectDialog = new MaxSelectDialog(MaxGoodSetActivity.this, new ResultCallback.MaxListener() {
            @Override
            public void selectMax(int max) {
                if (!StrUtil.isEmpty(trackNo)) {
                    /**设置单个*/
                    setTack(trackNo, max);
                } else {
                    /**设置全部*/
                    setAllTrack(max);
                }
                selectDialog.hideDialog();
            }
        });
        selectDialog.showDialog();
    }
}
