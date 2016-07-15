package com.ybg.rp.vm.activity.shopping;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cnpay.tigerbalm.utils.StrUtil;
import com.cnpay.tigerbalm.utils.TbLog;
import com.ybg.rp.vm.R;
import com.ybg.rp.vm.activity.base.BaseActivity;
import com.ybg.rp.vm.entity.GoodsInfo;
import com.ybg.rp.vm.entity.OrderInfo;
import com.ybg.rp.vm.entity.db.TranOnlineData;
import com.ybg.rp.vm.listener.ShoppingSelectListener;
import com.ybg.rp.vm.net.VMRequest;

import org.apache.log4j.Logger;

import java.util.Date;

/**
 * 支付单个商品
 *
 * @author zenghonghua
 * @包名: com.cnpay.vending.yifeng.activity.shopping
 * @修改记录:
 * @公司:
 * @date 2016/4/12 0012
 */
public class PayShoppingSingleActivity extends BaseActivity implements View.OnClickListener {

    private final Logger log = Logger.getLogger(PayShoppingSingleActivity.class);

    private RelativeLayout ll_code_bg;  //二维码背景
    private ImageView iv_read;      //刷卡提示
    private TextView tv_Title;      //中间标题  倒计时
    private LinearLayout ll_back;       //返回
    private ImageView iv_iamge;     //商品图片
    private TextView tv_name;       //商品名字
    private TextView tv_standard;   //规格
    private TextView tv_rail;       //轨道
    private TextView tv_price;      //价格z
    private TextView tv_orderno;    //订单号
    public TextView tv_hint;    //卡支付成功提示

    private ImageView iv_code;      //二维码

    private RadioGroup rg_pay_all;
    private RadioButton rb_weixin;      //微信支付
    private RadioButton rb_zhifubao;        //支付宝支付

    private int time = 120;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    private ShoppingSelectListener radioListener;

    /**
     * 商品和订单信息
     */
    private OrderInfo orderInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** 硬件加速*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        setContentLayout(R.layout.activity_pay_single);
        findId();
        ExitShoppingActivity.getInstance().addActivity(this);
        initNoVisibleTitle();

        /**传进的数据*/
        orderInfo = (OrderInfo) getIntent().getSerializableExtra("orderInfo");
        TbLog.i("[order:" + orderInfo.toString() + "]");
        //支付方式：默认 0：离线，1：支付宝，2：微信支付
        String type = getIntent().getStringExtra("type");
        setDataLayout();
        setListener();

        mHandler.postDelayed(runnable, 1000);

        /** 设置默认支付方式*/
        if (type.equals(ShoppingSelectListener.AL)) {
            rb_zhifubao.setChecked(true);
        } else {
            rb_weixin.setChecked(true);
        }
    }

    /**
     * 设置显示数据
     */
    private void setDataLayout() {
        if (null == orderInfo || StrUtil.isEmpty(orderInfo.getOrderNo()) ||
                null == orderInfo.getOrderInfos()
                || 0 >= orderInfo.getOrderInfos().size()) {
            return;
        }

        String orderNo = orderInfo.getOrderNo();
        tv_orderno.setText(orderNo);

        GoodsInfo goodsInfo = orderInfo.getOrderInfos().get(0);
        /*名称*/
        tv_name.setText(goodsInfo.getGoodsName());
        /*规格描述 450ml/瓶*/
        tv_standard.setText(StrUtil.isEmpty(goodsInfo.getGoodsDesc()) ? "" : goodsInfo.getGoodsDesc());
        /*轨道*/
        tv_rail.setText(goodsInfo.getTrackNo() + "轨道");
        /*金额*/
        tv_price.setText("￥" + StrUtil.doubleFormatTow(goodsInfo.getPrice()));

        /*图片*/
        String goodsPic = goodsInfo.getGoodsPic();
        //ImageUtils.getInstance(this).getImage(goodsPic, R.mipmap.icon_default_pic).into(iv_iamge);
        Glide.with(this)
                .load(goodsPic)
                .crossFade()
                .placeholder(R.mipmap.icon_default_pic)
                .error(R.mipmap.icon_default_pic)
                .into(iv_iamge);

    }


    /**
     * 设置监听
     */
    private void setListener() {
        ll_back.setOnClickListener(this);
        radioListener = new ShoppingSelectListener(this, orderInfo, iv_code, ll_code_bg, mHandler, rb_weixin, rb_zhifubao, tv_hint);
        rg_pay_all.setOnCheckedChangeListener(radioListener);
    }


    private void findId() {
        ll_code_bg = (RelativeLayout) findViewById(R.id.payWay_ll_bg);
        tv_orderno = (TextView) findViewById(R.id.payWay_tv_orderNo);
        tv_Title = (TextView) findViewById(R.id.paySingle_tv_title);
        ll_back = (LinearLayout) findViewById(R.id.paySingle_ll_back);

        iv_iamge = (ImageView) findViewById(R.id.paySingle_iv_image);
        tv_name = (TextView) findViewById(R.id.paySingle_tv_name);
        tv_standard = (TextView) findViewById(R.id.paySingle_tv_standard);
        tv_rail = (TextView) findViewById(R.id.paySingle_tv_rail);
        tv_price = (TextView) findViewById(R.id.paySingle_tv_price);

        iv_code = (ImageView) findViewById(R.id.payWay_iv_code);
        rg_pay_all = (RadioGroup) findViewById(R.id.payWay_rg_pay_all);
        rb_weixin = (RadioButton) findViewById(R.id.payWay_rb_weixin);
        rb_zhifubao = (RadioButton) findViewById(R.id.payWay_rb_zhifubao);
        tv_hint = (TextView) findViewById(R.id.payWay_tv_hint);
    }

    /**
     * 倒计时
     */
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            time--;
            if (time == 0) {
                /**取消交易*/
                cancelOrderNo(2);
                finish();
            } else {
                tv_Title.setText(time + "S");
            }
            mHandler.postDelayed(this, 1000);
        }
    };

    /**
     * 点击事件 返回
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.paySingle_ll_back:
                /**取消倒计时*/
                mHandler.removeCallbacks(runnable);
                /**取消交易*/
                cancelOrderNo(1);

                finish();
                break;

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        TbLog.i("[-ShoppingSingle onPause-]");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TbLog.i("[-ShoppingSingle onDestroy-]");
        mHandler.removeCallbacks(runnable);
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
        if (null != radioListener) {
            radioListener.recycleCache();
            radioListener.closeLine();
            radioListener.cloasCache();
            radioListener = null;
        }

        //Utils.onClearMemory(this);

        ll_code_bg.setBackgroundColor(0);  //二维码背景
        iv_read = null;      //刷卡提示
        iv_iamge = null;
        tv_Title = null;      //中间标题  倒计时
        ll_back = null;       //返回
        tv_name = null;       //商品名字
        tv_standard = null;   //规格
        tv_rail = null;       //轨道
        tv_price = null;      //价格z
        tv_orderno = null;    //订单号
        tv_hint = null;    //卡支付成功提示

        iv_code = null;      //二维码
        rg_pay_all = null;
        rb_weixin = null;      //微信支付
        rb_zhifubao = null;        //支付宝支付
        //orderInfo = null;
    }

    @Override
    public void onTrimMemory(int level) {
        switch (level) {
            case TRIM_MEMORY_UI_HIDDEN:
                /** 内存不足，并且该进程的UI已经不可见了 */
                if (null != radioListener) {
                    radioListener.closeLine();
                    radioListener.recycleCache();
                }
                break;
        }
        super.onTrimMemory(level);
    }

    /**
     * 取消订单
     */
    public void cancelOrderNo(int cancelType) {
        TbLog.e("交易取消/交易超时");
        //LinePayThread.isStartCount = false;
        /**启动服务上传交易结果 - 不需要取消订单操作*/
        TranOnlineData data = new TranOnlineData();
        Date date = new Date();
        data.setTranDate(date);
        data.setOrderNo(orderInfo.getOrderNo());
        data.setPayType(orderInfo.getPayWay() + "");
        String trackNos = "";
        for (int i = 0; i < orderInfo.getOrderInfos().size(); i++) {
            trackNos += orderInfo.getOrderInfos().get(i).getTrackNo() + ",";
        }
        data.setTrackNos(trackNos);
        data.setSaleResult("0");//0 取消 1 成功 2 失败
        data.setOrderPrice(orderInfo.getOrderMoney());
        VMRequest.getInstance(this).startSendSale(data, null, cancelType);
        TbLog.i("---取消订单---" + data.toString());
        log.info("---取消订单---" + data.toString());
    }

}
