package com.ybg.rp.vm.activity.setting;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnpay.tigerbalm.utils.Arith;
import com.cnpay.tigerbalm.utils.CharacterUtil;
import com.cnpay.tigerbalm.utils.TbLog;
import com.cnpay.tigerbalm.view.list.AutoLoadRecyclerView;
import com.cnpay.tigerbalm.view.list.listener.LoadMoreListener;
import com.cnpay.tigerbalm.view.list.listener.LoadResultCallBack;
import com.ybg.rp.vm.R;
import com.ybg.rp.vm.activity.base.BaseActivity;
import com.ybg.rp.vm.adapter.SaleDetailAdapter;
import com.ybg.rp.vm.util.helper.EntityDBUtil;
import com.ybg.rp.vm.util.dialog.MDatePickerDialog;

import java.util.Calendar;
import java.util.List;

import xutils.common.KeyValue;
import xutils.db.sqlite.SqlInfo;
import xutils.db.table.DbModel;

/**
 * 销售明细
 * <p>
 * 包            名:      com.ybg.rp.vm.activity.setting
 * 类            名:      SaleDetailActivity
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公            司:
 *
 * @author yuyucheng
 * @version V1.0
 * @date 2016/6/20 0020
 */
public class SaleDetailActivity extends BaseActivity implements LoadResultCallBack, View.OnClickListener {
    private AutoLoadRecyclerView mRecyclerView;
    private LinearLayout ll_no_data;    //无数据
    private SwipeRefreshLayout sl_swipeRefreshLayout;

    private EntityDBUtil dbUtil;
    private SaleDetailAdapter adapter;

    private LinearLayout ll_start_date; //开始时间
    private LinearLayout ll_end_date;   //结束时间
    private TextView tv_startTime, tv_endTime;
    private TextView tv_total_money;    //总金额
    private TextView tv_total_count;    //总笔数

    private String startDate, endDate;
    private String sYear, sMouth, sDay;//选择开始日期,
    private String eYear, eMouth, eDay;//选择结束日期

    private String totalSell = "￥ 0.00";
    private int totalCount = 0;

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 10002:
                    boolean isok = (boolean) msg.obj;
                    /**适配*/
                    if (isok) {
                        //显示数据
                        ll_no_data.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                    } else {
                        ll_no_data.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_manage_sale);

        initTitle("销售明细", true);
        setRightContent(R.mipmap.icon_machine_download, "下载");
        setRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**点击下载*/
                //YFDialogUtil.showListDeviceDialog(SellDetailActivity.this);
            }
        });
        dbUtil=EntityDBUtil.getInstance();
        findLayout();
        initCurrentDate();
        setListener();
        loadData();
    }

    private void findLayout() {
        mRecyclerView = (AutoLoadRecyclerView) findViewById(R.id.saleDetail_recycler_view);
        ll_no_data = (LinearLayout) findViewById(R.id.saleDetail_ll_nodata);
        sl_swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.sale_sl_swipeRefreshLayout);

        ll_start_date = (LinearLayout) findViewById(R.id.saleDetail_ll_start_time);
        ll_end_date = (LinearLayout) findViewById(R.id.saleDetail_ll_end_time);
        tv_startTime = (TextView) findViewById(R.id.saleDetail_tv_startTime);
        tv_endTime = (TextView) findViewById(R.id.saleDetail_tv_endTime);

        tv_total_money = (TextView) findViewById(R.id.saleDetail_tv_totalMoney);
        tv_total_count = (TextView) findViewById(R.id.saleDetail_tv_totalCount);
    }

    /**
     * 获取当前记录所有销售额
     * 与选择时间无关
     */
    private void loadData() {
        new AsyncTask<String, Integer, Boolean>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                /**开始*/
                //YFDialogUtil.showLoadding(SellDetailActivity.this);
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                /**结果返回*/
                tv_total_money.setText(totalSell);
                tv_total_count.setText(String.valueOf(totalCount));

                //YFDialogUtil.removeDialog(SellDetailActivity.this);
            }

            @Override
            protected Boolean doInBackground(String... params) {
                try {
                    /** 不等于取消的订单*/
                    String sql = "select * from TRAN_ONLINE t where t.SALE_RESULT != :status ";
                    SqlInfo sqlInfo = new SqlInfo();
                    sqlInfo.setSql(sql);
                    sqlInfo.addBindArg(new KeyValue("status", 0));
                    List<DbModel> dbModels = dbUtil.getDb().findDbModelAll(sqlInfo);
                    TbLog.i("[-dbModels=" + dbModels.toString() + "]");
                    double sum = 0.0;
                    for (int i = 0; i < dbModels.size(); i++) {
                        sum = Arith.add(dbModels.get(i).getDouble("ORDER_PRICE"), sum);
                    }
                    totalSell = "￥ " + sum;
                    totalCount = dbModels.size();
                    TbLog.i("总销售额：" + sum);
                    TbLog.i("总笔数：" + dbModels.size());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        }.execute();
    }

    private void setListener() {
        ll_start_date.setOnClickListener(this);
        ll_end_date.setOnClickListener(this);

        /**下拉*/
        sl_swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        sl_swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.loadFirst(startDate, endDate);
            }
        });


        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void loadMore() {
                /** 分页更多*/
                adapter.loadNextPage(startDate, endDate);
            }
        });
        adapter = new SaleDetailAdapter(mRecyclerView, this, mHandler);
        mRecyclerView.setAdapter(adapter);
        adapter.loadFirst(startDate, endDate);
    }

    @Override
    public void onSuccess(int result, Object object) {
        if (sl_swipeRefreshLayout.isRefreshing()) {
            sl_swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onError(int code, String msg) {
        if (sl_swipeRefreshLayout.isRefreshing()) {
            sl_swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saleDetail_ll_start_time:
                /**开始时间*/
                showStartDate();
                break;
            case R.id.saleDetail_ll_end_time:
                /**结束时间*/
                showEndDate();
                break;
        }
    }


    /*************************************************************
     *                  时间选择
     * ***********************************************************/
    /**
     * 初始化时间选择数据
     */
    private void initCurrentDate() {
        // 获取日历对象
        Calendar calendar = Calendar.getInstance();
        // 获取当前对应的年、月、日的信息
        int cYear = calendar.get(Calendar.YEAR);
        int cMouth = calendar.get(Calendar.MONTH) + 1;
        int cDay = calendar.get(Calendar.DAY_OF_MONTH);

        sYear = String.valueOf(cYear);
        eYear = String.valueOf(cYear);

        String mouth = String.valueOf(cMouth);
        sMouth = CharacterUtil.autoCompZero(String.valueOf(mouth), 2);
        eMouth = CharacterUtil.autoCompZero(String.valueOf(mouth), 2);

        sDay = CharacterUtil.autoCompZero(String.valueOf(cDay), 2);
        eDay = CharacterUtil.autoCompZero(String.valueOf(cDay), 2);

        startDate = sYear + "-" + sMouth + "-" + sDay + " 00:00:00";
        endDate = eYear + "-" + eMouth + "-" + eDay + " 23:59:59";

        tv_startTime.setText(startDate);
        tv_endTime.setText(endDate);

    }

    /**
     * 选择开始时间
     */
    private void showStartDate() {
        MDatePickerDialog dateDialog = new MDatePickerDialog(SaleDetailActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        sYear = String.valueOf(year);
                        String month = CharacterUtil.autoCompZero(String.valueOf(monthOfYear + 1), 2);// 11 ,01
                        sMouth = month;
                        String day = CharacterUtil.autoCompZero(String.valueOf(dayOfMonth), 2);//
                        sDay = day;

                        startDate = sYear + "-" + sMouth + "-" + sDay + " 00:00:00";
                        tv_startTime.setText(startDate);
                        TbLog.i("[startDate=" + startDate + "]");
                        adapter.loadFirst(startDate, endDate);
                    }
                }, Integer.parseInt(sYear), Integer.parseInt(sMouth) - 1, Integer.parseInt(sDay));
        dateDialog.show();

    }


    /**
     * 选择结束时间
     */
    private void showEndDate() {
        MDatePickerDialog dateDialog = new MDatePickerDialog(SaleDetailActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        eYear = String.valueOf(year);
                        String month = CharacterUtil.autoCompZero(String.valueOf(monthOfYear + 1), 2);// 11 ,01
                        eMouth = month;
                        String day = CharacterUtil.autoCompZero(String.valueOf(dayOfMonth), 2);//
                        eDay = day;

                        endDate = eYear + "-" + eMouth + "-" + eDay + " 23:59:59";
                        tv_endTime.setText(endDate);
                        TbLog.i("[endDate=" + endDate + "]");
                        adapter.loadFirst(startDate, endDate);
                    }
                }, Integer.parseInt(eYear), Integer.parseInt(eMouth) - 1, Integer.parseInt(eDay));
        dateDialog.show();

    }


}
