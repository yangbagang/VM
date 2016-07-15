package com.ybg.rp.vm.net;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.cnpay.tigerbalm.utils.DateUtil;
import com.cnpay.tigerbalm.utils.TbLog;
import com.igexin.sdk.PushManager;
import com.ybg.rp.vm.entity.db.ErrorTrackNo;
import com.ybg.rp.vm.entity.db.ErrorTranData;
import com.ybg.rp.vm.entity.db.TranOnlineData;
import com.ybg.rp.vm.entity.db.util.DbUtils;
import com.ybg.rp.vm.util.AppPreferences;
import com.ybg.rp.vm.util.Config;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 通用请求数据上传
 *
 */
public class VMRequest {

    private Context mContext;
    private NetWorkUtil netWorkUtil;
    private Logger log = Logger.getLogger(VMRequest.class);
    private static VMRequest commVMResquest;
    private AppPreferences appPreferences = AppPreferences.getInstance();

    public static VMRequest getInstance(Context mContext) {
        if (null == commVMResquest) {
            commVMResquest = new VMRequest(mContext);
        }
        return commVMResquest;
    }

    private VMRequest(Context mContext) {
        this.mContext = mContext;
        netWorkUtil = NetWorkUtil.getInstance();
    }

    /**
     * 更新clientId
     */
    public void updateClientId(final boolean needBroadcast) {
        String vmCode = appPreferences.getVMCode();

        TbLog.i("-------上传的机器ID:" + vmCode);
        String clientId = PushManager.getInstance().getClientid(mContext);
        Request<String> request = netWorkUtil.post("vendMachineInfo/updateClientIdByVmCode");
        // 添加请求参数
        request.add("vmCode", vmCode);
        request.add("clientId", clientId);
        TbLog.i("--请求参数--  vmCode = " + vmCode + " , clientId = " + clientId);
        netWorkUtil.add(mContext, WHAT.VM_REG, request, new YFHttpListener<String>() {
            @Override
            public void onSuccess(int what, Response<String> response) {
                try {
                    JSONObject json = new JSONObject(response.get());
                    String success = json.getString("success");
                    String machineId = json.getString("machineId");
                    String msg = json.getString("msg");

                    if ("true".equals(success)) {
                        appPreferences.setVmId(machineId);
                    } else {
                        appPreferences.setVmCode("");
                    }
                    if (needBroadcast) {
                        Intent intent = new Intent("update_client_id");
                        intent.putExtra("update_client_is_success", success);
                        intent.putExtra("update_client_msg", msg);
                        mContext.sendBroadcast(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int what, String msg) {
                TbLog.e(msg);
                if (needBroadcast) {
                    appPreferences.setVmCode("");
                    Intent intent = new Intent("update_client_id");
                    intent.putExtra("update_client_is_success", false);
                    intent.putExtra("update_client_msg", msg);
                    mContext.sendBroadcast(intent);
                }
            }
        });
    }

    /**
     * 上传交易结果
     *
     * @param data       数据( 类型 0:取消，1卡交易，2线上交易)
     * @param errorGoods 错误的轨道信息
     */
    public void startSendSale(final TranOnlineData data, final ArrayList<ErrorTranData>
            errorGoods, final int cancelType) {
        TbLog.i("[--启动-上传交易结果 (上传数据到VEM) 一直--]");
        new Thread(new Runnable() {
            @Override
            public void run() {
                /**把交易记录保存到文件 0 取消 1 card 2 online*/
                TbLog.i("[-save:" + data.toString() + "-]");
                //TODO --保存消费记录
                DbUtils.getInstance().saveOrUpdate(data);
                /**上传*/
                sendSettleData(data, errorGoods, false, cancelType);
            }
        }).start();
    }

    /**
     * 上传交易数据到-售卖机后台
     * 同步请求到服务器
     *
     * @param data       交易记录 支付方式：0：离线，1：支付宝，2：微信支付
     * @param errorGoods 错误轨道信息
     * @param isDataUp   是否进行库存扣减？true 不扣库存，false 扣取库存
     */
    public void sendSettleData(final TranOnlineData data, final ArrayList<ErrorTranData> errorGoods,
                               boolean isDataUp, int cancelType) {
        log.info("保存与上传交易结果--   VEM  --- TranOnlineData=" + data.toString());
        Request<String> request = netWorkUtil.post("orderInfo/updateOrderStatus");
        // 添加请求参数
        request.add("orderNo", data.getOrderNo());
        request.add("payType", data.getPayType());
        request.add("result", data.getSaleResult());
        request.add("transDate", DateUtil.getStringByFormat(data.getTranDate(), DateUtil.dateFormatYMDHMS));
        request.add("isDataUp", isDataUp);
        request.add("cancelType", cancelType);

        for (int i = 1; i <= Config.HTTP_ERROR_REQUEST_COUNT; i++) {
            //同步请求
            Response<String> response = NoHttp.startRequestSync(request);
            if (response.isSucceed()) {
                //上传交易错误的轨道
                sendErrorData(errorGoods);
                DbUtils.getInstance().saveForTranUpdate_Service(data.getOrderNo(), true);
                break;
            } else if (i == Config.HTTP_ERROR_REQUEST_COUNT) {
                //保存未上传的数据
                DbUtils.getInstance().saveForTranUpdate_Service(data.getOrderNo(), false);
            }
            SystemClock.sleep(Config.UO_DATA_INTERVAL);
        }
    }

    /**
     * 订单-部分轨道错误后，进行数据上传
     * 同步请求到服务器
     *
     * @param goodsInfos 商品数据/轨道编号
     */
    public void sendErrorData(final ArrayList<ErrorTranData> goodsInfos) {
        if (null == goodsInfos || goodsInfos.size() <= 0) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Request<String> request = netWorkUtil.post("orderInfo/deliveryGoodsFail");
                for (int i = 1; i < goodsInfos.size(); i++) {
                    for (int count = 0; count <= Config.HTTP_ERROR_REQUEST_COUNT; count++) {
                        ErrorTranData tranData = goodsInfos.get(i);
                        // 添加请求参数
                        request.add("orderSn", tranData.getOrderNo());
                        request.add("trackNo", tranData.getTrackNo());
                        request.add("gid", tranData.getGid());
                        //同步请求
                        Response<String> response = NoHttp.startRequestSync(request);
                        if (response.isSucceed()) {
                            tranData.setIsUpd(1);
                            DbUtils.getInstance().saveOrUpdate(tranData);
                            break;
                        } else if (count == Config.HTTP_ERROR_REQUEST_COUNT) {
                            //上传失败-保存到本地
                            DbUtils.getInstance().saveOrUpdate(tranData);
                        }
                        SystemClock.sleep(Config.UO_DATA_INTERVAL);
                    }
                }
            }
        }).start();
    }

    /**
     * 售卖机错误信息上送
     * 以及把数据保存到本地数据库
     *
     * @param errorTrackNo    轨道错误信息
     */
    private int addFaultCount = 0;

    public void addFaultInfo(final ErrorTrackNo errorTrackNo) {
        /**  轨道错误信息 记录错误信息到数据库 */
        DbUtils.getInstance().saveFaultTrackNo(errorTrackNo.getTrackNo());
        DbUtils.getInstance().saveLog("轨道：" + errorTrackNo.getTrackNo() + "-" + errorTrackNo.getErrMsg());

        Request<String> request = netWorkUtil.post("vendMachineInfo/addErrorInfo");
        // 添加请求参数
        request.add("machineId", appPreferences.getVMId());//机器ID
        request.add("orbitalNo", errorTrackNo.getTrackNo());//轨道编号
        request.add("errorMsg", errorTrackNo.getErrMsg());//错误信息

        netWorkUtil.add(mContext, WHAT.VM_ERROR_TRACK, request, new YFHttpListener<String>() {
            @Override
            public void onSuccess(int what, Response<String> response) {
                addFaultCount = 0;
                TbLog.i("--售卖机错误信息上送完成--");
            }

            @Override
            public void onFailed(int what, String msg) {
                TbLog.e(msg);
                addFaultCount++;
                if (addFaultCount <= Config.HTTP_ERROR_REQUEST_COUNT) {
                    addFaultInfo(errorTrackNo);
                } else {
                    // 添加-轨道的错误数据 - 记录未上传数据
                    DbUtils.getInstance().saveOrUpdate(errorTrackNo);

                    addFaultCount = 0;
                    TbLog.i("保存未上传的轨道错误数据");
                }
            }
        });
    }

}
