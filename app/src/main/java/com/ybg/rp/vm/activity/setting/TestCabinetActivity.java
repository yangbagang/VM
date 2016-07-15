package com.ybg.rp.vm.activity.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.cnpay.tigerbalm.utils.ToastUtil;
import com.ybg.rp.vm.R;
import com.ybg.rp.vm.activity.base.BaseActivity;
import com.ybg.rp.vm.adapter.TestRecordAdapter;
import com.ybg.rp.vm.adapter.TestTrackAdapter;
import com.ybg.rp.vm.entity.db.LayerBean;
import com.ybg.rp.vm.entity.db.TrackBean;
import com.ybg.rp.vm.listener.ResultCallback;
import com.ybg.rp.vm.util.helper.DbSetHelper;
import com.ybg.rp.vm.util.helper.MachinesHelper;

import java.util.ArrayList;

/**
 * 格子柜测试
 * <p>
 * 包            名:      com.ybg.rp.vm.activity.setting
 * 类            名:      TestCabinetActivity
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公            司:
 *
 * @author yuyucheng
 * @version V1.0
 * @date 2016/6/20 0020
 */
public class TestCabinetActivity extends BaseActivity {
    private GridView gv_trackList;
    private ListView lv_testResult;

    private MachinesHelper helper;
    private DbSetHelper dbHelper;
    private ArrayList<TrackBean> trackList;
    private TestTrackAdapter trackAdapter;
    private TestRecordAdapter recordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_manage_cabinet);
        initTitle("格子柜测试");
        findLayout();

        helper = MachinesHelper.getInstance(this);
        dbHelper = DbSetHelper.getInstance(this);
        trackList = new ArrayList<>();

        loadData();
    }

    private void findLayout() {
        gv_trackList = (GridView) findViewById(R.id.trackTest_cabinet_gv_trackList);
        lv_testResult = (ListView) findViewById(R.id.trackTest_cabinet_lv_testResult);
    }

    /**
     * 加载数据
     */
    private void loadData() {
        ArrayList<LayerBean> list = dbHelper.getCabinetList();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                trackList.addAll(dbHelper.getTrackList(list.get(i).getLayerNo()));
            }
            trackAdapter = new TestTrackAdapter(TestCabinetActivity.this, trackList);
            gv_trackList.setAdapter(trackAdapter);
            gv_trackList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    /**触发测试*/
                    TrackBean bean = trackList.get(position);
                    testTrack(bean);
                }
            });
        } else {
            ToastUtil.showToast(TestCabinetActivity.this, "请先设置格子柜");
            finish();
        }
    }

    /**
     * 测试指定轨道
     */
    private void testTrack(TrackBean track) {
        helper.testCabinetItem(track, new ResultCallback.ReturnListener() {
            @Override
            public void startRecord() {
                //YFDialogUtil.showLoadding(TestCabinetActivity.this);
            }

            @Override
            public void returnRecord(ArrayList<String> list) {
                showRecord(list);
                //YFDialogUtil.removeDialog(TestCabinetActivity.this);
            }
        });
    }

    /**
     * 结果显示
     */
    private void showRecord(ArrayList<String> records) {
        recordAdapter = new TestRecordAdapter(TestCabinetActivity.this, records);
        lv_testResult.setAdapter(recordAdapter);
        recordAdapter.notifyDataSetChanged();
    }
}
