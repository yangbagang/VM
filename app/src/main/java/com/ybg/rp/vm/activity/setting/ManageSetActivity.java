package com.ybg.rp.vm.activity.setting;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnpay.tigerbalm.utils.Arith;
import com.cnpay.tigerbalm.utils.DateUtil;
import com.cnpay.tigerbalm.utils.SimpleUtil;
import com.cnpay.tigerbalm.utils.StrUtil;
import com.cnpay.tigerbalm.utils.TbLog;
import com.cnpay.tigerbalm.utils.ToastUtil;
import com.ybg.rp.vm.R;
import com.ybg.rp.vm.activity.base.BaseActivity;
import com.ybg.rp.vm.adapter.ErrorTrackAdapter;
import com.ybg.rp.vm.app.OPApplication;
import com.ybg.rp.vm.entity.TrackError;
import com.ybg.rp.vm.entity.db.TrackBean;
import com.ybg.rp.vm.entity.db.TranOnlineData;
import com.ybg.rp.vm.serial.BeanTrackSet;
import com.ybg.rp.vm.serial.SerialManager;
import com.ybg.rp.vm.util.AppPreferences;
import com.ybg.rp.vm.util.Config;
import com.ybg.rp.vm.util.helper.DbSetHelper;
import com.ybg.rp.vm.util.helper.EntityDBUtil;
import com.ybg.rp.vm.util.helper.MachinesHelper;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.Response;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.util.ArrayList;

/**
 * 设置管理
 */
public class ManageSetActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 轨道状态
     */
    private TextView tv_trackState;
    /**
     * 错误轨道
     */
    private LinearLayout ll_error_track;
    /**
     * 选择全部
     */
    private CheckBox cb_selectAll;
    private GridView gv_track;
    /**
     * 测试轨道,修复轨道,跳转系统设置
     */
    private Button bt_testTrack, bt_fixTrack, btn_setting;
    /**
     * 测试与修复操作
     */
    private TextView tv_operate, tv_operateInfo;
    private LinearLayout ll_operate;


    /**
     * 当日销售额
     */
    private PercentLinearLayout ll_salesdata;
    private TextView tv_shellMoney;
    /**
     * 轨道测试
     */
    private PercentLinearLayout ll_trackTest;
    /**
     * 操作日志
     */
    private PercentLinearLayout ll_log;
    /**
     * 基础设置,支付方式设置,机器品牌设置
     */
    private PercentLinearLayout ll_baseSet, ll_payselect, ll_brand;

    /**
     * 今天销售额
     */
    private String currentDaySell = "￥ 0.00";

    private DbSetHelper setHelper;
    private MachinesHelper mHelper;
    private SerialManager manager;
    private ErrorTrackAdapter errorAdapter;
    private ArrayList<TrackBean> errorList;//当前的故障轨道
    private ArrayList<TrackBean> selectList;//选中的错误轨道
    private ArrayList<TrackError> errors;//适配数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_manage_setting);

        initTitle("操作主页", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EntityDBUtil.getInstance().saveLog("退出登录");
                finish();
            }
        });

        findLayout();
        setHelper = DbSetHelper.getInstance(this);
        mHelper = MachinesHelper.getInstance(this);
        manager = SerialManager.getInstance(this.getApplicationContext());

        errorList = new ArrayList<>();
        selectList = new ArrayList<>();
        errors = new ArrayList<>();

        setListener();
    }

    private void findLayout() {
        tv_trackState = (TextView) findViewById(R.id.manage_tv_trackState);

        cb_selectAll = (CheckBox) findViewById(R.id.manage_cb_selectAll);
        gv_track = (GridView) findViewById(R.id.manage_gv_track);
        bt_testTrack = (Button) findViewById(R.id.manage_bt_testTrack);
        bt_fixTrack = (Button) findViewById(R.id.manage_bt_fixTrack);
        ll_error_track = (LinearLayout) findViewById(R.id.manage_ll_error_track);

        tv_operate = (TextView) findViewById(R.id.manage_tv_operate);
        tv_operateInfo = (TextView) findViewById(R.id.manage_tv_operateInfo);
        ll_operate = (LinearLayout) findViewById(R.id.manage_ll_operate);

        ll_salesdata = (PercentLinearLayout) findViewById(R.id.manage_ll_salesdata);
        //tv_shellState = (TextView) findViewById(R.id.manage_tv_shellState);
        tv_shellMoney = (TextView) findViewById(R.id.manage_tv_shellMoney);

        ll_trackTest = (PercentLinearLayout) findViewById(R.id.manage_ll_trackTest);
        ll_log = (PercentLinearLayout) findViewById(R.id.manage_ll_log);
        ll_baseSet = (PercentLinearLayout) findViewById(R.id.manage_ll_baseSet);
        ll_payselect = (PercentLinearLayout) findViewById(R.id.manage_ll_paySelect);
        ll_brand = (PercentLinearLayout) findViewById(R.id.manage_ll_machineSelect);

        btn_setting = (Button) findViewById(R.id.manage_btn_setting);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.manage_bt_testTrack:
                /**轨道测试*/
                testSelectTrack();
                break;
            case R.id.manage_bt_fixTrack:
                /**轨道修复*/
                repairTrackNo(selectList);
                break;

            case R.id.manage_ll_salesdata:
                /**销售*/
                Intent salesIntent = new Intent(ManageSetActivity.this, SaleDetailActivity.class);
                salesIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(salesIntent);
                break;
            case R.id.manage_ll_trackTest:
                /**轨道测试*/
                Intent testIntent = new Intent(ManageSetActivity.this, TestMachineActivity.class);
                testIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(testIntent);
                break;
            case R.id.manage_ll_log:
                /**操作日志*/
                Intent logIntent = new Intent(ManageSetActivity.this, MachinesLogActivity.class);
                logIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(logIntent);
                break;
            case R.id.manage_ll_baseSet:
                /**基础设置*/
                Intent baseIntent = new Intent(ManageSetActivity.this, BaseSetActivity.class);
                baseIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(baseIntent);
                break;
            case R.id.manage_ll_paySelect:
                /**支付方式*/
                Intent payIntent = new Intent(ManageSetActivity.this, PaySelectActivity.class);
                payIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(payIntent);
                break;
            case R.id.manage_ll_machineSelect:
                /**选择机器设置串口*/
                Intent bIntent = new Intent(ManageSetActivity.this, BrandSelectActivity.class);
                bIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(bIntent);
                break;
            case R.id.manage_btn_setting:
                /**跳转到系统设置*/
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        new AsyncTask<String, Integer, Boolean>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                /**开始*/
                //YFDialogUtil.showLoadding(ManageSetActivity.this);
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);

                updUi();
                tv_shellMoney.setText(currentDaySell);

                /**结果返回*/
                //YFDialogUtil.removeDialog(ManageSetActivity.this);
            }

            @Override
            protected Boolean doInBackground(String... params) {
                try {
                    /**错误轨道*/
                    selectList.clear();
                    errors.clear();
                    errorList.clear();
                    errorList.addAll(setHelper.getErrorTack());
                    /**获取当日销售额*/
                    ArrayList<TranOnlineData> arrayList = (ArrayList<TranOnlineData>) EntityDBUtil.getInstance().getDb().selector(TranOnlineData.class).
                            where("CREATE_DATE", "like", DateUtil.getCurrentDate(DateUtil.dateFormatYMD) + "%").
                            and("SALE_RESULT", "!=", "0").findAll();
                    if (null != arrayList) {
                        double sum = 0.0;
                        for (int i = 0; i < arrayList.size(); i++) {
                            sum += arrayList.get(i).getOrderPrice();
                            TbLog.i("sum += " + arrayList.get(i).getOrderPrice());
                        }
                        currentDaySell = "￥ " + StrUtil.doubleFormat(sum);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        }.execute();
    }

    private void setListener() {
        ll_salesdata.setOnClickListener(this);
        ll_trackTest.setOnClickListener(this);
        ll_log.setOnClickListener(this);
        ll_baseSet.setOnClickListener(this);
        ll_payselect.setOnClickListener(this);
        ll_brand.setOnClickListener(this);

        /**故障轨道相关*/
        bt_testTrack.setOnClickListener(this);
        bt_fixTrack.setOnClickListener(this);
        btn_setting.setOnClickListener(this);

        errorAdapter = new ErrorTrackAdapter(ManageSetActivity.this, errors);
        gv_track.setAdapter(errorAdapter);

        gv_track.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /**添加选中*/
                boolean isSelect = errors.get(position).isSelect();
                if (isSelect) {
                    errors.get(position).setIsSelect(false);
                    selectList.remove(errorList.get(position));//从选择数据中移除
                } else {
                    errors.get(position).setIsSelect(true);
                    selectList.add(errorList.get(position));//添加进选择数据
                }
                refreshView();
            }
        });
        /**选中全部*/
        cb_selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (errors.size() > 0) {
                    /**设置适配数据*/
                    ArrayList<TrackError> list = ErrorTrackAdapter.setAllSelet(isChecked, errors);
                    errors.clear();
                    errors.addAll(list);
                    /**设置选中数据*/
                    selectList.clear();
                    if (isChecked) {
                        selectList.addAll(errorList);
                    }
                    refreshView();
                }
            }
        });
    }

    /**
     * 刷新页面
     */
    private void refreshView() {
        SimpleUtil.setListViewHeightBasedOnChildren(gv_track, 6);
        errorAdapter.notifyDataSetChanged();
    }


    /**
     * 更新UI
     */
    private void updUi() {
        /** 没有错误数据*/
        if (errorList.size() <= 0) {
            ll_error_track.setVisibility(View.GONE);
            cb_selectAll.setVisibility(View.GONE);
            tv_trackState.setText("良好");
        } else {
            ll_error_track.setVisibility(View.VISIBLE);
            cb_selectAll.setVisibility(View.VISIBLE);
            tv_trackState.setText("错误");

            int pos = 0;
            errors.addAll(ErrorTrackAdapter.initErrorTrack(errorList, pos));
            selectList.add(errorList.get(pos));
            refreshView();
        }
    }


    /********************************************************
     *                  测试与修复
     * *******************************************************/

    /**
     * 触发轨道测试
     */
    private void testSelectTrack() {
        if (errorList.size() > 0) {
            if (selectList.size() > 0) {
                testTrack(selectList);
            } else {
                ToastUtil.showToast(ManageSetActivity.this, "请先选择轨道");
            }
        } else {
            ToastUtil.showToast(ManageSetActivity.this, "当前无错误轨道");
        }
    }

    /**
     * 测试单个轨道
     *
     * @param list 轨道数据
     */
    private void testTrack(final ArrayList<TrackBean> list) {
        new AsyncTask<String, Integer, ArrayList<String>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //YFDialogUtil.showLoadding(ManageSetActivity.this);
            }

            @Override
            protected void onPostExecute(ArrayList<String> list) {
                super.onPostExecute(list);
                /**测试结果显示*/
                StringBuffer result = new StringBuffer();
                for (String str : list) {
                    result.append(str).append(",");
                }
                ll_operate.setVisibility(View.VISIBLE);
                tv_operate.setText("测试结果");
                tv_operateInfo.setText(result);
                /**结果返回*/
                //YFDialogUtil.removeDialog(ManageSetActivity.this);
            }

            @Override
            protected ArrayList<String> doInBackground(String... params) {
                ArrayList<String> resultList = new ArrayList<String>();
                try {

                    if (null != list && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            TrackBean track = list.get(i);
                            String str = track.getTrackNo() + "轨道：";
                            EntityDBUtil.getInstance().saveLog("测试轨道(修复)：" + track.getTrackNo());
                            /**判断轨道类型 1：格子柜,0：不是格子柜 */
                            BeanTrackSet trackSet = null;
                            int gridMark = track.getGridMark();
                            if (gridMark == 0) {
                                manager.createSerial(0);// 1:主机 2:格子柜
                                trackSet = manager.openMachineTrack(track.getTrackNo());
                            } else if (gridMark == 1) {
                                manager.createSerial(1);// 1:主机 2:格子柜
                                trackSet = manager.openMachineTrack(track.getTrackNo());
                            }
                            if (trackSet.trackstatus == 1) {
                                str += "电机正常";
                            } else {
                                str += trackSet.errorinfo;
                                TbLog.e("轨道错误-------" + str);
                            }
                            resultList.add(str);
                            SystemClock.sleep(Config.CYCLE_INTERVAL);
                        }
                    }
                    manager.closeSerial();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return resultList;
            }
        }.execute();
    }


    /**
     * 修复错误轨道
     *
     * @param selectList 错误轨道信息
     */
    public void repairTrackNo(final ArrayList<TrackBean> selectList) {
        new AsyncTask<String, Integer, Boolean>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //YFDialogUtil.showLoadding(ManageSetActivity.this);
            }

            @Override
            protected Boolean doInBackground(String... params) {
                boolean ok = false;
                Request<String> request = NoHttp.createStringRequest(Config.URL_COMM +
                        "vendMachineInfo/fixError", RequestMethod.POST);
                if (null != selectList && selectList.size() > 0) {
                    TbLog.i("[selectList size=" + selectList.size() + "]");
                    EntityDBUtil dbUtil = EntityDBUtil.getInstance();
                    for (int i = 0; i < selectList.size(); i++) {
                        TrackBean t = selectList.get(i);
                        if (t.getFault() == 1) {
                            // 添加请求参数
                            request.add("orbitalNo", t.getTrackNo().toUpperCase());
                            request.add("machineId", AppPreferences.getInstance().getVMId());
                            Response<String> response = NoHttp.startRequestSync(request);
                            if (response.isSucceed()) {
                                // 请求成功
                                TbLog.i("修复轨道返回: " + response.get());
                                EntityDBUtil.getInstance().saveLog("成功修复错误轨道-" + t.getTrackNo());
                                //设置为正常
                                t.setFault(0);
                                dbUtil.saveOrUpdate(t);
                            } else {
                                // 请求失败
                                TbLog.e("修复轨道返回: error " + response.getException().getMessage());
                                EntityDBUtil.getInstance().saveLog("修复错误轨道失败-" + t.getTrackNo() + "-"
                                        + response.getException().getMessage());
                            }
                        }
                    }
                    ok = true;
                }
                return ok;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                //YFDialogUtil.removeDialog(ManageSetActivity.this);

                if (aBoolean) {
                    ToastUtil.showToast(ManageSetActivity.this, "修改服务器错误轨道完成！");
                } else {
                    ToastUtil.showToast(ManageSetActivity.this, "没有需要修复的轨道");
                }
                TbLog.i("修改服务器错误轨道完成！");
                /**加载数据 刷新UI*/
                loadData();
            }
        }.execute();
    }

}
