package com.ybg.rp.vm.activity.setting;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;

import com.cnpay.tigerbalm.view.list.AutoLoadRecyclerView;
import com.cnpay.tigerbalm.view.list.listener.LoadMoreListener;
import com.cnpay.tigerbalm.view.list.listener.LoadResultCallBack;
import com.ybg.rp.vm.R;
import com.ybg.rp.vm.activity.base.BaseActivity;
import com.ybg.rp.vm.adapter.MachinesLogAdapter;

/**
 * 操作日志
 * <p>
 * 包            名:      com.ybg.rp.vm.activity.setting
 * 类            名:      MachinesLogActivity
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公            司:
 *
 * @author yuyucheng
 * @version V1.0
 * @date 2016/6/20 0020
 */
public class MachinesLogActivity extends BaseActivity implements LoadResultCallBack {

    private AutoLoadRecyclerView av_recycler;
    private RelativeLayout ll_noDataLayout;
    private SwipeRefreshLayout sl_swipeRefreshLayout;
    private MachinesLogAdapter logAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_manage_log);
        initTitle("操作日志", true);
        setRightContent(R.mipmap.icon_machine_download, "下载");
        setRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**点击下载*/
                //YFDialogUtil.showListDeviceDialog(MachinesLogActivity.this);
            }
        });

        findLayout();
        initListener();
    }

    private void findLayout() {
        av_recycler = (AutoLoadRecyclerView) findViewById(R.id.log_av_recycler);
        ll_noDataLayout = (RelativeLayout) findViewById(R.id.log_ll_noDataLayout);
        sl_swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.log_sl_swipeRefreshLayout);
    }

    /**
     * 设置监听
     */
    private void initListener() {

        /**下拉*/
        sl_swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        sl_swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                logAdapter.loadFirst();
            }
        });
        av_recycler.setLayoutManager(new LinearLayoutManager(MachinesLogActivity.this));
        av_recycler.setHasFixedSize(true);
        /**加载更多*/
        av_recycler.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void loadMore() {
                logAdapter.loadNextPage();
            }
        });


        logAdapter = new MachinesLogAdapter(av_recycler, this);
        av_recycler.setAdapter(logAdapter);
        logAdapter.loadFirst();
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
}
