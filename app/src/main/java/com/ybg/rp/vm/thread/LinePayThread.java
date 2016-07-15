package com.ybg.rp.vm.thread;

import android.content.Context;
import android.os.SystemClock;

import com.cnpay.tigerbalm.utils.TbLog;
import com.ybg.rp.vm.app.OPApplication;
import com.ybg.rp.vm.entity.BackPay;
import com.ybg.rp.vm.entity.OrderInfo;
import com.ybg.rp.vm.net.NetWorkUtil;
import com.ybg.rp.vm.serial.PushOpenTrackNoUtils;
import com.ybg.rp.vm.util.Config;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;

import org.json.JSONObject;

/**
 * 线上支付，主动查询
 * 包            名:      com.cnpay.vending.yifeng.thread
 * 类            名:      LinePayThread
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公             司:
 *
 * @author liyuanming
 * @version V1.0
 * @date 2016/4/13
 */
public class LinePayThread extends Thread {
    private Context mActivity;

    public OrderInfo orderInfo;


    /**
     * 主动请求-计数
     */
    private int findCount = 0;

    public LinePayThread(Context mActivity, OrderInfo orderInfo) {
        super();
        this.mActivity = mActivity;
        this.orderInfo = orderInfo;
        this.setName("线上支付LinePay线程");

    }

    @Override
    public void run() {
        super.run();
        Request<String> request = NetWorkUtil.getInstance().post("queryOrderInfo");
        TbLog.i("---" + orderInfo.toString());
        //是否打开柜门
        String isOpenTrack = OPApplication.getInstance().getIsOpenTrack(orderInfo.getOrderNo());

        while (!interrupted()) {
            // 添加请求参数
            request.add("orderSn", orderInfo.getOrderNo());
            //同步请求
            Response<String> response = NoHttp.startRequestSync(request);
            TbLog.i("-----" + orderInfo.getOrderNo());
            if (response.isSucceed()) {
                // 请求成功
                TbLog.i("请求返回数据: " + response.get());
                try {
                    //TODO 是否打开柜门 -- 重新赋值
                    isOpenTrack = OPApplication.getInstance().getIsOpenTrack(orderInfo.getOrderNo());
                    TbLog.i("---------获取到缓存的订单号-----" + isOpenTrack);

                    //BackPay backPay = GsonUtils.createGson().fromJson(response.get(), BackPay.class);
                    BackPay backPay = new BackPay();
                    JSONObject json = new JSONObject(response.get());
                    backPay.setIsPay(json.getBoolean("isPay"));
                    backPay.setDeliveryStatus(json.getString("deliveryStatus"));
                    backPay.setPayWay(json.getString("payWay"));
                    if (backPay.isPay() && null != isOpenTrack && backPay.getDeliveryStatus().equals("0")) {

                        //TODO 柜门打开后，进行数据初始化
                        OPApplication.getInstance().isOpenTrackRemove(orderInfo.getOrderNo());
                        //打开柜门 - 支付成功-本地没出货-服务器未发货
                        TbLog.e("---线上支付----出货------");
                        orderInfo.setPayStatus(backPay.isPay());
                        PushOpenTrackNoUtils.shipmentLine(mActivity, orderInfo);
                        this.interrupt();
                    } else if (!backPay.isPay() && null != isOpenTrack) {
                        //没有支付-查询 && 没有出货 TODO 递归线上支付状态
                        SystemClock.sleep(Config.PAY_INTERVAL);
                    } else {
                        this.interrupt();
                        TbLog.i("--------关闭递归-------");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                findCount = 0;
            } else {
                // 请求失败
                findCount++;
                if (null != isOpenTrack && findCount < Config.HTTP_ERROR_REQUEST_COUNT) {
                    //没有支付-查询 && 没有出货 TODO 递归线上支付状态
                } else if (null == isOpenTrack) {
                    TbLog.i("已经出货-停止轮询- " + orderInfo.getOrderNo());
                    this.interrupt();
                } else {
                    this.interrupt();
                }
            }
            SystemClock.sleep(Config.PAY_INTERVAL);
        }
    }
}
