package com.ybg.rp.vm.activity.setting;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.cnpay.tigerbalm.utils.SimpleUtil;
import com.ybg.rp.vm.R;
import com.ybg.rp.vm.activity.base.BaseActivity;
import com.ybg.rp.vm.adapter.TestLayerAdapter;
import com.ybg.rp.vm.adapter.TestRecordAdapter;
import com.ybg.rp.vm.entity.db.LayerBean;
import com.ybg.rp.vm.listener.ResultCallback;
import com.ybg.rp.vm.util.helper.DbSetHelper;
import com.ybg.rp.vm.util.helper.MachinesHelper;

import java.util.ArrayList;

/**
 * 机器测试
 * <p>
 * 包            名:      com.ybg.rp.vm.activity.setting
 * 类            名:      TestMachineActivity
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公            司:
 *
 * @author yuyucheng
 * @version V1.0
 * @date 2016/6/23 0023
 */
public class TestMachineActivity extends BaseActivity {
    private LinearLayout ll_test_main;
    private LinearLayout ll_test_cabinet;
    private GridView gv_main_list, gv_cabinet_list;
    private Button bt_main_all, bt_cabinet_all;
    private ListView lv_track_test;

    private MachinesHelper mHelper;
    private DbSetHelper dbHelper;
    private TestLayerAdapter mainAdapter, cabinetAdapter;
    private ArrayList<LayerBean> lyList; // 主机数据
    private ArrayList<LayerBean> cabinetList; // 格子柜数据
    private ArrayList<String> records;//测试数据
    private TestRecordAdapter recordAdapter; // 测试结果

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_manage_test);
        initTitle("货道测试");
        findLayout();

        mHelper=MachinesHelper.getInstance(this);
        dbHelper=DbSetHelper.getInstance(this);

        lyList = new ArrayList<>();
        cabinetList = new ArrayList<>();
        records = new ArrayList<>();

        setListener();
        loadData();
    }

    private void findLayout() {
        ll_test_main = (LinearLayout) findViewById(R.id.trackTest_ll_test_main);
        ll_test_cabinet = (LinearLayout) findViewById(R.id.trackTest_ll_test_cabinet);
        gv_main_list = (GridView) findViewById(R.id.trackTest_gv_main_list);
        gv_cabinet_list = (GridView) findViewById(R.id.trackTest_gv_cabinet_list);
        bt_main_all = (Button) findViewById(R.id.trackTest_bt_main_all);
        bt_cabinet_all = (Button) findViewById(R.id.trackTest_bt_cabinet_all);
        lv_track_test = (ListView) findViewById(R.id.trackTest_lv_track_test);
    }

    /**
     * 初始化监听
     */
    private void setListener() {
        /** 测试数据*/
        recordAdapter = new TestRecordAdapter(TestMachineActivity.this, records);
        lv_track_test.setAdapter(recordAdapter);

        /** 主机数据*/
        mainAdapter = new TestLayerAdapter(TestMachineActivity.this, lyList);
        gv_main_list.setAdapter(mainAdapter);
        SimpleUtil.setListViewHeightBasedOnChildren(gv_main_list, 5);
        gv_main_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /**触发测试*/
                LayerBean cabinet = lyList.get(position);
                testMainLayer(cabinet);
            }
        });

        /** 格子柜数据*/
        cabinetAdapter = new TestLayerAdapter(TestMachineActivity.this, cabinetList);
        gv_cabinet_list.setAdapter(cabinetAdapter);
        SimpleUtil.setListViewHeightBasedOnChildren(gv_cabinet_list, 5);
        gv_cabinet_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /**触发测试*/
                LayerBean cabinet = cabinetList.get(position);
                testCabinetLayer(cabinet);
            }
        });


        bt_main_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**测试全部-主机*/
                testMainAll();
            }
        });
        bt_cabinet_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**测试全部格子柜*/
                testCabinetAll();
            }
        });


        /**跳转 主机轨道详情*/
        ll_test_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestMachineActivity.this, TestMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        /**跳转 格子柜轨道详情*/
        ll_test_cabinet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestMachineActivity.this, TestCabinetActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }


    /**
     * 加载数据
     */
    private void loadData() {
        new AsyncTask<String, Integer, Boolean>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                /**开始*/
                //YFDialogUtil.showLoadding(TestMachineActivity.this);
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                mainAdapter.notifyDataSetChanged();
                SimpleUtil.setListViewHeightBasedOnChildren(gv_main_list, 5);

                cabinetAdapter.notifyDataSetChanged();
                SimpleUtil.setListViewHeightBasedOnChildren(gv_cabinet_list, 5);
                /**结果返回*/
                //YFDialogUtil.removeDialog(TestMachineActivity.this);
            }

            @Override
            protected Boolean doInBackground(String... params) {
                ArrayList<LayerBean> mains = dbHelper.getMainLayer();
                ArrayList<LayerBean> cabinets = dbHelper.getCabinetList();

                lyList.addAll(mains);
                cabinetList.addAll(cabinets);
                return true;
            }
        }.execute();
    }

    /**
     * 所有主机轨道
     */
    private void testMainAll() {
        /**所有主机轨道*/
        mHelper.testMainAll(lyList, new ResultCallback.ReturnListener() {
            @Override
            public void startRecord() {
                //YFDialogUtil.showLoadding(TestMachineActivity.this);
            }

            @Override
            public void returnRecord(ArrayList<String> list) {
                showRecord(list);
                //YFDialogUtil.removeDialog(TestMachineActivity.this);
            }
        });

    }

    /**
     * 主机单层
     *
     * @param layerBean 层级数据
     */
    private void testMainLayer(LayerBean layerBean) {
        /**单层轨道*/
        mHelper.testMainLayer(layerBean, new ResultCallback.ReturnListener() {
            @Override
            public void startRecord() {
                //YFDialogUtil.showLoadding(TestMachineActivity.this);
            }

            @Override
            public void returnRecord(ArrayList<String> list) {
                showRecord(list);
                //YFDialogUtil.removeDialog(TestMachineActivity.this);
            }
        });

    }

    /**
     * 所有格子柜
     */
    private void testCabinetAll() {
        /**所有格子柜*/
        mHelper.testCabinetAll(cabinetList, new ResultCallback.ReturnListener() {
            @Override
            public void startRecord() {
                //YFDialogUtil.showLoadding(TestMachineActivity.this);
            }

            @Override
            public void returnRecord(ArrayList<String> list) {
                showRecord(list);
                //YFDialogUtil.removeDialog(TestMachineActivity.this);
            }
        });
    }

    /**
     * 单层格子柜
     *
     * @param cabinet 层级数据
     */
    private void testCabinetLayer(LayerBean cabinet) {
        /**测试单个格子柜*/
        mHelper.testCabinetLayer(cabinet, new ResultCallback.ReturnListener() {
            @Override
            public void startRecord() {
                //YFDialogUtil.showLoadding(TestMachineActivity.this);
            }

            @Override
            public void returnRecord(ArrayList<String> list) {
                showRecord(list);
                //YFDialogUtil.removeDialog(TestMachineActivity.this);
            }
        });
    }


    /**
     * 显示结果
     *
     * @param list
     */
    private void showRecord(ArrayList<String> list) {
        records.clear();
        records.addAll(list);
        recordAdapter.notifyDataSetChanged();
    }

}
