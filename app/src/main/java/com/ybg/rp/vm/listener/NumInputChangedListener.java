package com.ybg.rp.vm.listener;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.cnpay.tigerbalm.utils.GsonUtils;
import com.cnpay.tigerbalm.utils.TbLog;
import com.cnpay.tigerbalm.utils.ToastUtil;
import com.ybg.rp.vm.activity.HomeActivity;
import com.ybg.rp.vm.activity.shopping.PayShoppingSingleActivity;
import com.ybg.rp.vm.app.OPApplication;
import com.ybg.rp.vm.entity.OrderInfo;
import com.ybg.rp.vm.entity.db.TrackBean;
import com.ybg.rp.vm.net.NetWorkUtil;
import com.ybg.rp.vm.net.WHAT;
import com.ybg.rp.vm.net.YFHttpListener;
import com.ybg.rp.vm.util.AppPreferences;
import com.ybg.rp.vm.util.dialog.YFDialogUtil;
import com.ybg.rp.vm.util.helper.EntityDBUtil;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 监听数字变化
 *
 * 包   名:     com.ybg.rp.vm.listener
 * 类   名:     NumInputChangedListener
 * 版权所有:     版权所有(C)2010-2016
 * 公   司:
 * 版   本:          V1.0
 * 时   间:     2016/7/6 0006 17:22
 * 作   者:     yuyucheng
 */
public class NumInputChangedListener implements TextWatcher {
    private final Logger log = Logger.getLogger(HomeActivity.class);

    private Context mContext;
    private TextView input_text;

    private EditText et_command;
    private EntityDBUtil dbUtil;
    private ArrayList<TrackBean> allTrack;

    public NumInputChangedListener(Context mContext,EditText et_command, TextView input_text
            , ArrayList<TrackBean> allTrack) {
        this.et_command = et_command;
        this.input_text = input_text;
        this.mContext = mContext;
        this.allTrack = allTrack;
        this.dbUtil=EntityDBUtil.getInstance();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        try {
            input_text.setText("");
            String str = s.toString().trim();
            if (null == allTrack || allTrack.size() <= 0) {
                input_text.setText("请先初始化基础信息");
                return;
            }
            if (str.length() >= 3) {
                TbLog.i("input: str=" + str);
                String track = str.substring(0, 3);
                /**标准轨道只有3位*/
                checkInputTrack(track);
            } else {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 轨道确认
     */
    private void checkInputTrack(String trackNo) {
        try {
            TrackBean first = dbUtil.getDb().selector(TrackBean.class).where("TRACK_NO", "=", trackNo).findFirst();
            if (null != first) {
                if (first.getFault() == 1) {
                    et_command.setText("");
                    input_text.setText("该轨道已故障,请选择其他轨道");
                } else {
                    /**
                     * 选中了单个商品，进入支付流程。
                     */
                    inputTrackNo(trackNo, ShoppingSelectListener.WX);
                }
            } else {
                et_command.setText("");
                input_text.setText("没有该编号,请重新输入");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 轨道编号查询商品
     *
     * @param trackNo 轨道编号
     * @param payType 支付类型 0：离线，1：支付宝，2：微信支付
     */
    public void inputTrackNo(final String trackNo, final String payType) {
        NetWorkUtil netWorkUtil = NetWorkUtil.getInstance();
        YFDialogUtil.showLoadding(mContext);
        Request<String> request = netWorkUtil.post("orderInfo/createOrderWithMachineIdAndTrackNo");
        // 添加请求参数
        request.add("machineId", AppPreferences.getInstance().getVMId());//机器ID
        request.add("trackNo", trackNo);//轨道编号
        netWorkUtil.add(mContext, WHAT.VM_INP_TRACKNO, request, new YFHttpListener<String>() {
            @Override
            public void onSuccess(int what, Response<String> response) {
                try {
                    JSONObject json = new JSONObject(response.get());
                    OrderInfo orderInfo = GsonUtils.createGson().fromJson(json.getString("orderInfo"), OrderInfo.class);

                    OPApplication.getInstance().setIsOpenTrack(orderInfo.getOrderNo());//设置订单号

                    log.info("--输入编号生成的订单--"+ orderInfo.toString());

                    /** 跳转支付单个页面*/
                    Intent payIntent = new Intent();
                    payIntent.setClass(mContext, PayShoppingSingleActivity.class);
                    payIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    payIntent.putExtra("orderInfo", orderInfo);
                    payIntent.putExtra("type", payType);
                    mContext.startActivity(payIntent);
                    /**清空文字*/
                    et_command.setText("");
                    input_text.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                YFDialogUtil.removeDialog(mContext);
            }

            @Override
            public void onFailed(int what, String msg) {
                TbLog.e(msg);
                et_command.setText("");
                input_text.setText("");
                ToastUtil.showCenterToast(mContext, msg);
                YFDialogUtil.removeDialog(mContext);
            }
        });
    }
}
