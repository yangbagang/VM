package com.ybg.rp.vm.listener;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnpay.tigerbalm.utils.ACache;
import com.cnpay.tigerbalm.utils.DateUtil;
import com.cnpay.tigerbalm.utils.GsonUtils;
import com.cnpay.tigerbalm.utils.TbLog;
import com.cnpay.tigerbalm.utils.ToastUtil;
import com.igexin.sdk.PushManager;
import com.ybg.rp.vm.R;
import com.ybg.rp.vm.app.OPApplication;
import com.ybg.rp.vm.entity.OrderInfo;
import com.ybg.rp.vm.entity.db.TranOnlineData;
import com.ybg.rp.vm.entity.pay.Charge;
import com.ybg.rp.vm.net.NetWorkUtil;
import com.ybg.rp.vm.net.WHAT;
import com.ybg.rp.vm.net.YFHttpListener;
import com.ybg.rp.vm.thread.LinePayThread;
import com.ybg.rp.vm.util.AppPreferences;
import com.ybg.rp.vm.util.Config;
import com.ybg.rp.vm.util.Utils;
import com.ybg.rp.vm.util.dialog.YFDialogUtil;
import com.ybg.rp.vm.util.helper.EntityDBUtil;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;

import org.json.JSONObject;

/**
 * 支付方式选择
 * 包            名:      com.cnpay.vending.yifeng
 * 类            名:      ShoppingSelectListener
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公             司:
 *
 * @author liyuanming
 * @version V1.0
 * @date 2016/4/13
 */
public class ShoppingSelectListener implements RadioGroup.OnCheckedChangeListener {

    /**
     * 支付方式：默认 0：，1：支付宝，2：微信支付
     */
    public static String payType;
    public static final String AL = "1";
    public static final String WX = "2";


    private Activity mActivity;

    private OrderInfo orderInfo;

    private RelativeLayout ll_code_bg;//二维码白色背景
    private ImageView iv_code;      //二维码

    private RadioGroup radioGroupPayAll;    //3种支付方式组
    private RadioButton rb_weixin;          //微信支付
    private RadioButton rb_zhifubao;        //支付宝支付
    private TextView tv_hint;   //交易提示

    private LinePayThread linePayThread;
    private Handler mHandler;


    private NetWorkUtil netWorkUtil;


    /*缓存二维码*/
    private ACache cache;

    /*二维码显示*/
    private Bitmap bitmap;
    //private Map<String, WeakReference<Bitmap>> imageCache = new HashMap<String, WeakReference<Bitmap>>();

    public ShoppingSelectListener(Activity mActivity, OrderInfo orderInfo, ImageView iv_code, RelativeLayout ll_bg,
                                  Handler handler, RadioButton rb_weixin, RadioButton rb_zhifubao, TextView tv_hint) {
        this.mActivity = mActivity;
        this.orderInfo = orderInfo;
        this.mHandler = handler;
        this.iv_code = iv_code;
        this.ll_code_bg = ll_bg;
        this.rb_weixin = rb_weixin;
        this.rb_zhifubao = rb_zhifubao;
        this.tv_hint = tv_hint;
        this.cache = ACache.get(mActivity, Config.CACHE_FILENAME);
        this.netWorkUtil = NetWorkUtil.getInstance();

        /** 添加销售数据 - 初始化*/

        TranOnlineData data = new TranOnlineData();
        data.setOrderNo(orderInfo.getOrderNo());
        EntityDBUtil.getInstance().addObject(data);

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            /**微信*/
            case R.id.payWay_rb_weixin:
                payType = WX;
                tv_hint.setVisibility(View.INVISIBLE);
                closeLine();
                selectPayLine();
                break;
            /**支付宝*/
            case R.id.payWay_rb_zhifubao:
                payType = AL;
                tv_hint.setVisibility(View.INVISIBLE);
                closeLine();
                selectPayLine();
                break;
        }
    }

    /**
     * 选择支付方式
     */
    private void selectPayLine() {
        ll_code_bg.setBackgroundColor(mActivity.getResources().getColor(R.color.white));
        iv_code.setVisibility(View.VISIBLE);
        bitmap = cache.getAsBitmap(orderInfo.getOrderNo() + payType);
        if (bitmap != null) {
            ll_code_bg.setVisibility(View.VISIBLE);

            iv_code.setImageBitmap(bitmap);
            if (null == linePayThread) {
                linePayThread = new LinePayThread(mActivity, orderInfo);
                linePayThread.start();
            }
        } else {
            ll_code_bg.setVisibility(View.INVISIBLE);
            payAlAndWx(orderInfo, payType);
        }
    }

    /**
     * 支付宝  微信  支付
     *
     * @param good    选择的商品
     * @param payType 1:支付宝 , 2:微信
     */
    private synchronized void payAlAndWx(final OrderInfo good, final String payType) {
        if (!PushManager.getInstance().isPushTurnedOn(mActivity)) {
            TbLog.e("重新启动-个推链接");
            PushManager.getInstance().initialize(mActivity);
        }

        /** 将显示二维码的地方 - 设置未NULL*/
        iv_code.setImageBitmap(null);

        Request<String> request = netWorkUtil.post("orderInfo/createPingPlusCharge");
        // 添加请求参数
        String machineId = AppPreferences.getInstance().getVMId();
        String orderNo = good.getOrderNo();
        request.add("machineId", machineId);//机器ID
        request.add("orderNo", orderNo);
//        request.add("goodId", good.getGoodId());
//        request.add("goodPrice", String.valueOf(good.getPrice()));
        request.add("payType", payType);//1:支付宝 , 2:微信
        TbLog.i("----machineId = " + machineId + "-- orderNo = " + orderNo + "-  payType = " + payType);
        YFDialogUtil.showLoadding(mActivity);
        netWorkUtil.add(mActivity, WHAT.VM_ERROR_TRACK, request, new YFHttpListener<String>() {
            @Override
            public void onSuccess(int what, Response<String> response) {
                YFDialogUtil.removeDialog(mActivity);
                try {
                    String result = response.get();
                    JSONObject json = new JSONObject(result);
                    TbLog.i("-----------strCharge------" + json.toString());

                    Charge charge = GsonUtils.createGson(DateUtil.dateFormatYMDHMS).fromJson(json.getString("charge"), Charge.class);
                    String qr = "";
                    if (payType.equals(AL)) {
                        TbLog.i("-------------支付宝----支付");
                        /** 支付宝 二维码*/
                        qr = charge.getCredential().get("alipay_qr").toString();
                    } else if (payType.equals(WX)) {
                        TbLog.i("-------------微信----支付");
                        /** 微信 二维码*/
                        qr = charge.getCredential().get("wx_pub_qr").toString();
                    }
                    /**  生成二维码 */
                    bitmap = Utils.Create2DCode(qr);
                    iv_code.setImageBitmap(bitmap);

//                    WeakReference<Bitmap> softBitmap = new WeakReference<Bitmap>(bitmap);
//                    imageCache.put("path", softBitmap);
//                    Bitmap bitmapTwo = softBitmap.get();
//                    if (bitmapTwo != null) {
//                        iv_code.setImageBitmap(bitmapTwo);
//                    } else {
//                        ToastUtil.showToast(mActivity.getApplicationContext(), "生成二维码故障,请重新购买!");
//                    }

                    ll_code_bg.setVisibility(View.VISIBLE);
                    ll_code_bg.setBackgroundColor(mActivity.getResources().getColor(R.color.white));
                    cache.put(good.getOrderNo() + payType, ShoppingSelectListener.this.bitmap, 120);//缓存保存120秒  ACache.TIME_SECOND

                    orderInfo.setPayWay(payType);

                    if (null == linePayThread) {
                        linePayThread = new LinePayThread(mActivity, orderInfo);
                        linePayThread.start();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int what, String msg) {
                YFDialogUtil.removeDialog(mActivity);
                TbLog.e(msg);
                ToastUtil.showToast(mActivity, "网络连接故障,请重试");
            }
        });

    }

    /**
     * 回收缓存
     */
    public void recycleCache() {
        cache.remove(orderInfo.getOrderNo() + AL);
        cache.remove(orderInfo.getOrderNo() + WX);
        Utils.recycleImageView(iv_code);
        if (bitmap != null) {
            bitmap.recycle();
        }
        System.gc();
    }

    /**
     * 关闭 支付宝/微信
     */
    public void closeLine() {
        /** 关闭图片缓存*/
        if (bitmap != null && !bitmap.isRecycled()) {
            iv_code.setImageBitmap(null);
            bitmap.recycle();
            bitmap = null;
            TbLog.i("-关闭图片缓存-");
        }
        if (null != linePayThread) {
            linePayThread.interrupt();
            linePayThread = null;
            TbLog.i("-关闭线上支付-");
        }

        // 提醒系统回收图片
        System.gc();
    }

    public void cloasCache() {
        if (cache != null) {
            cache.clear();
        }
    }

    /**
     * 关闭卡交易
     */
//    public synchronized void closeCard() {
//        if (null != tranCardThread) {
//            tranCardThread.close();
//            tranCardThread.interrupt();
//            tranCardThread = null;
//            TbLog.i("-关闭卡交易-");
//        }
//        if (null != reader) {
//            reader.closeSerialPort();
//        }
//    }
}
