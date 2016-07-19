package com.ybg.rp.vm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.cnpay.tigerbalm.utils.GsonUtils;
import com.cnpay.tigerbalm.utils.TbLog;
import com.cnpay.tigerbalm.utils.ToastUtil;
import com.igexin.sdk.PushConsts;
import com.ybg.rp.vm.activity.login.LoginActivity;
import com.ybg.rp.vm.activity.login.ScanSuccessActivity;
import com.ybg.rp.vm.activity.setting.ManageSetActivity;
import com.ybg.rp.vm.app.OPApplication;
import com.ybg.rp.vm.entity.OrderInfo;
import com.ybg.rp.vm.entity.PushBase;
import com.ybg.rp.vm.entity.PushOpenDoor;
import com.ybg.rp.vm.entity.db.Operator;
import com.ybg.rp.vm.entity.db.util.DbUtils;
import com.ybg.rp.vm.serial.PushOpenTrackNoUtils;
import com.ybg.rp.vm.util.helper.EntityDBUtil;

import org.apache.log4j.Logger;
import org.json.JSONObject;

/**
 * 个推-透传返回数据
 * 包            名:      com.ybg.rp.vm.receiver
 * 类            名:      PushNoticeReceiver
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公             司:
 *
 * @author liyuanming
 * @version V1.0
 * @date 2016/6/14
 */
public class PushNoticeReceiver extends BroadcastReceiver {
    private Logger log = Logger.getLogger(PushNoticeReceiver.class);

    @Override
    public void onReceive(final Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        try {
            switch (bundle.getInt(PushConsts.CMD_ACTION)) {
                case PushConsts.GET_MSG_DATA:
                    /**获取透传数据*/
                    // String appid = bundle.getString("appid");
                    byte[] payload = bundle.getByteArray("payload");
                    //String taskid = bundle.getString("taskid");
                    //String messageid = bundle.getString("messageid");
                    if (payload != null) {
                        String data = new String(payload);
                        TbLog.i("[--Receive:" + data);
                        JSONObject json = new JSONObject(data);
                        String type = json.getString("type");
                        switch (type) {
                            case PushBase.TYPE_OPEN:
                                /**打开柜门*/
                                final PushOpenDoor pushOpenDoor = GsonUtils.createGson().fromJson(data, PushOpenDoor.class);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        TbLog.i("----" + pushOpenDoor.toString());
                                        /**打开柜门*/
                                        String trackNos = pushOpenDoor.getData();
                                        try {
                                            PushOpenTrackNoUtils.operTrackNo(context, trackNos);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        EntityDBUtil.getInstance().saveLog("推送-开柜门:" + trackNos);
                                    }
                                }).start();
                                break;
                            case PushBase.TYPE_SHIP_NEW:
                                /** 出货 21寸屏幕使用*/
                                final OrderInfo orderInfo = GsonUtils.createGson().fromJson(data, OrderInfo.class);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // 数据不为NULL，说明柜门未打开
                                        String isOpenTrack = OPApplication.getInstance().getIsOpenTrack(orderInfo.getOrderNo());
                                        if (null != isOpenTrack && isOpenTrack.equals(orderInfo.getOrderNo())) {
                                            // 柜门打开后，进行数据初始化
                                            OPApplication.getInstance().isOpenTrackRemove(orderInfo.getOrderNo());
                                            TbLog.e("----------线上支付--------PUSH");
                                            PushOpenTrackNoUtils.shipmentLine(context, orderInfo);
                                        } else {
                                            log.info("------柜门已打开/没有改订单数据---  " + orderInfo.getOrderNo());
                                        }
                                    }
                                }).start();
                                break;
                            case PushBase.TYPE_SIGN_IN:
                                /** 推送登录*/
                                Operator operatorIn = GsonUtils.createGson().fromJson(data, Operator.class);
                                Operator yfOper = OPApplication.getInstance().getOper();
                                if (operatorIn.getOperatorId().longValue() != yfOper.getOperatorId().longValue
                                        ()) {
                                    log.info("登录用户不一致 -推送的用户：" + operatorIn.toString() + "----当前：" + yfOper.toString());
                                    ToastUtil.showCenterToast(context, "请在'" + yfOper.getOperatorName() +
                                            "'的用户点击登录");
                                    return;
                                }
                                DbUtils.getInstance().saveLog("登录");

                                Intent optIntent = new Intent();
                                optIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                optIntent.setClass(context, ManageSetActivity.class);
                                context.startActivity(optIntent);
                                /** 关闭二维码扫描页面*/
                                if (null != LoginActivity.loginActivity && !LoginActivity.loginActivity.isFinishing()) {
                                    LoginActivity.loginActivity.finish();
                                }
                                if (null != ScanSuccessActivity.scanSuccessActivity && !ScanSuccessActivity.scanSuccessActivity.isFinishing()) {
                                    ScanSuccessActivity.scanSuccessActivity.finish();
                                }
                                break;
                            case PushBase.TYPE_SCAN_SUCCESS:
                                Operator operator = GsonUtils.createGson().fromJson(data, Operator.class);
                                OPApplication.getInstance().setOper(operator);//保存到application
                                /** 获得推送 进入扫描成功页面*/
                                Intent scanIntent = new Intent();
                                scanIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                scanIntent.setClass(context, ScanSuccessActivity.class);
                                context.startActivity(scanIntent);
                                break;
                        }
                    }
                    break;
               /* case PushConsts.GET_CLIENTID:
                    // 获取ClientID(CID)
                    // 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
                    String cid = bundle.getString("clientid");
                    TbLog.e("PushDemo 获取到CID:" + cid);
                    break;*/
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
