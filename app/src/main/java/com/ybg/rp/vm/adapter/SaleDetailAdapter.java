package com.ybg.rp.vm.adapter;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cnpay.tigerbalm.utils.StrUtil;
import com.cnpay.tigerbalm.utils.TbLog;
import com.cnpay.tigerbalm.view.list.listener.LoadFinishCallBack;
import com.cnpay.tigerbalm.view.list.listener.LoadResultCallBack;
import com.ybg.rp.vm.R;
import com.ybg.rp.vm.entity.db.TranOnlineData;
import com.ybg.rp.vm.util.helper.EntityDBUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 销售明细适配
 * <p>
 * 包            名:      com.ybg.rp.vm.adapter
 * 类            名:      SaleDetailAdapter
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公            司:
 *
 * @author yuyucheng
 * @version V1.0
 * @date 2016/6/22 0022
 */
public class SaleDetailAdapter extends RecyclerView.Adapter<SaleDetailAdapter.SaleHolder> {
    private int page;
    private int size = 20;

    private List<TranOnlineData> datas;

    private EntityDBUtil dbUtil;

    private LoadFinishCallBack mLoadFinisCallBack;
    private LoadResultCallBack mLoadResultCallBack;
    private Handler mHandler;


    public SaleDetailAdapter(LoadFinishCallBack mLoadFinisCallBack, LoadResultCallBack mLoadResultCallBack, Handler mHandler) {
        this.mLoadFinisCallBack = mLoadFinisCallBack;
        this.mLoadResultCallBack = mLoadResultCallBack;
        this.mHandler = mHandler;
        this.datas = new ArrayList<>();
        dbUtil = EntityDBUtil.getInstance();
    }

    @Override
    public SaleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_sale, parent, false);
        return new SaleHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SaleHolder holder, int position) {
        TranOnlineData data = datas.get(position);
        /**设置数据*/
        holder.tv_time.setText(data.getTranDate());
        holder.tv_orderNo.setText(StrUtil.isEmpty(data.getOrderNo()) ? "" : data.getOrderNo());
        holder.tv_track.setText(data.getTrackNos());
        double money = data.getOrderPrice();
        holder.tv_money.setText(StrUtil.doubleFormatTow(money));
        /**支付方式：0：com.ybg.rp.vm，1：支付宝，2：微信支付*/
        String payWay = data.getPayType();
        if (payWay.equals("0")) {
            holder.tv_payWay.setText("com.ybg.rp.vm");
        } else if (payWay.equals("1")) {
            holder.tv_payWay.setText("支付宝");
        } else if (payWay.equals("2")) {
            holder.tv_payWay.setText("微信支付");
        }
        /**交易结果 0 取消 1 成功 2 失败 3(卡交易不确定)*/
        String saleResult = data.getSaleResult();
        if (saleResult.equals("0")) {
            holder.tv_result.setText("取消");
        } else if (saleResult.equals("1")) {
            holder.tv_result.setText("成功");
        } else if (saleResult.equals("2")) {
            holder.tv_result.setText("失败");
        } else if (saleResult.equals("3")) {
            holder.tv_result.setText("不确定");
        }
    }

    @Override
    public int getItemCount() {
        if (datas.size() > 0) {
            return datas.size();
        }
        return 0;
    }

    public class SaleHolder extends RecyclerView.ViewHolder {
        TextView tv_time;
        TextView tv_orderNo;
        TextView tv_track;
        TextView tv_money;
        TextView tv_payWay;
        TextView tv_result;

        public SaleHolder(View itemView) {
            super(itemView);

            tv_time = (TextView) itemView.findViewById(R.id.saleDetail_item_tv_time);
            tv_orderNo = (TextView) itemView.findViewById(R.id.saleDetail_item_tv_orderNo);
            tv_track = (TextView) itemView.findViewById(R.id.saleDetail_item_tv_track);
            tv_money = (TextView) itemView.findViewById(R.id.saleDetail_item_tv_money);
            tv_payWay = (TextView) itemView.findViewById(R.id.saleDetail_item_tv_payWay);
            tv_result = (TextView) itemView.findViewById(R.id.saleDetail_item_tv_result);
        }
    }

    /**
     * 刷新
     */
    public void loadFirst(String startDate, String endDate) {
        datas.clear();
        page = 0;
        loadData(startDate, endDate);
    }

    /**
     * 加载更多
     */
    public void loadNextPage(String startDate, String endDate) {
        page++;
        loadData(startDate, endDate);
    }

    private void loadData(final String startDate, final String endDate) {

        new AsyncTask<String, Integer, Boolean>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                TbLog.i("----执行前---");
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                Message msg = new Message();
                msg.what = 10002;
                if (datas.size() >= 0) {
                    msg.obj = true;
                } else {
                    msg.obj = false;
                }
                mHandler.sendMessage(msg);

                mLoadFinisCallBack.loadFinish();
                TbLog.i("----执行 后---");
                mLoadResultCallBack.onSuccess(LoadResultCallBack.SUCCESS_OK, null);
                notifyDataSetChanged();
            }

            @Override
            protected Boolean doInBackground(String... params) {
                try {
                    if (page == 0) {
                        datas.clear();
                    }
                    ArrayList<TranOnlineData> arrayList = dbUtil.findTranData(startDate, endDate, page, size);
                    TbLog.i("[-list=" + arrayList.toString() + "]");
                    datas.addAll(arrayList);
//                    ArrayList<TranOnlineData> list=dbUtil.findAll(TranOnlineData.class);
//                    TbLog.i("[-list="+list.toString()+"]");
                    if (arrayList.size() < 1) {
                        page--;
                        TbLog.i("没有更多-销售数据");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return true;
            }
        }.execute();
    }
}
