package com.ybg.rp.vm.activity.shopping;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnpay.tigerbalm.utils.Arith;
import com.cnpay.tigerbalm.utils.TbLog;
import com.ybg.rp.vm.R;
import com.ybg.rp.vm.activity.base.BaseActivity;
import com.ybg.rp.vm.adapter.PayGoodsAdapter;
import com.ybg.rp.vm.entity.GoodsInfo;
import com.ybg.rp.vm.entity.OrderInfo;
import com.ybg.rp.vm.entity.db.TranOnlineData;
import com.ybg.rp.vm.listener.ShoppingSelectListener;
import com.ybg.rp.vm.net.VMRequest;

import org.apache.log4j.Logger;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 支付 购物车
 *
 * @author zenghonghua
 * @包名: com.cnpay.vending.yifeng.activity.shopping
 * @修改记录:
 * @公司:
 * @date 2016/4/11 0011
 */
public class PayShoppingCartActivity extends BaseActivity implements View.OnClickListener {

    private final Logger log = Logger.getLogger(PayShoppingCartActivity.class);

    private RelativeLayout ll_code_bg;  //二维码白色背景
    private TextView tv_Title;      //中间标题  倒计时
    private LinearLayout ll_back;       //返回
    private ListView listView;
    private TextView tv_total_money;    //总金额
    private ImageView iv_code;      //二维码
    private TextView tv_orderno;    //订单号
    public TextView tv_hint;    //卡支付成功提示


    private RadioGroup rg_pay_all;
    private RadioButton rb_weixin;   //微信支付
    private RadioButton rb_zhifubao;   //支付宝支付


    private int time = 120;//倒计时

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    private ShoppingSelectListener radioListener;

    /**
     * 商品和订单信息 -
     */
    private OrderInfo orderInfo;

    /**
     * 支付方式：默认 0：com.ybg.rp.vm，1：支付宝，2：微信支付
     */
    private String type;
    private ArrayList<GoodsInfo> orderInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** 硬件加速*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        setContentLayout(R.layout.activity_pay_cart);
        findId();

        ExitShoppingActivity.getInstance().addActivity(this);
        initNoVisibleTitle();

        orderInfo = (OrderInfo) getIntent().getSerializableExtra("orderInfo");
        type = getIntent().getStringExtra("type");
        TbLog.i("[ type=" + type + " ,order=" + orderInfo.toString());

        mHandler.postDelayed(runnable, 1000);
        initData();
        setListener();

        /** 设置默认支付方式*/
        if (type.equals(ShoppingSelectListener.AL)) {
            rb_zhifubao.setChecked(true);
        } else {
            rb_weixin.setChecked(true);
        }

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
//        tv_Title = (TextView) findViewById(R.id.cartPay_tv_title);
//        ll_back = (LinearLayout) findViewById(R.id.cartPay_ll_back);
        tv_Title = (TextView) findViewById(R.id.paySingle_tv_title);
        ll_back = (LinearLayout) findViewById(R.id.paySingle_ll_back);

        listView = (ListView) findViewById(R.id.cartPay_listview);
        tv_total_money = (TextView) findViewById(R.id.cartPay_tv_total_money);

        iv_code = (ImageView) findViewById(R.id.payWay_iv_code);
        rg_pay_all = (RadioGroup) findViewById(R.id.payWay_rg_pay_all);
        rb_weixin = (RadioButton) findViewById(R.id.payWay_rb_weixin);
        rb_zhifubao = (RadioButton) findViewById(R.id.payWay_rb_zhifubao);
        tv_orderno = (TextView) findViewById(R.id.payWay_tv_orderNo);
        tv_hint = (TextView) findViewById(R.id.payWay_tv_hint);
    }

    private void initData() {
        orderInfos = orderInfo.getOrderInfos();
        PayGoodsAdapter adapter = new PayGoodsAdapter(this, orderInfos);
        listView.setAdapter(adapter);

        double totalPrice = 0;
        for (int i = 0; i < orderInfos.size(); ++i) {
            GoodsInfo goodsInfo = orderInfos.get(i);
            Double price = goodsInfo.getPrice();
            int num = goodsInfo.getNum();

            totalPrice = totalPrice + (Arith.mul(price, num));
            DecimalFormat df = new DecimalFormat("######0.00");
            String format = df.format(totalPrice);
            tv_total_money.setText("¥ " + format);
        }

        String orderNo = orderInfo.getOrderNo();
        tv_orderno.setText(orderNo);
    }

    /**
     * 倒计时
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            time--;
            if (time == 0) {
                cancelOrderNo(2);
                finish();
            } else {
                tv_Title.setText(time + "S");
            }
            mHandler.postDelayed(this, 1000);
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.paySingle_ll_back:
                mHandler.removeCallbacks(runnable);
                cancelOrderNo(1);
                finish();
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(runnable);
        mHandler.removeCallbacksAndMessages(null);
        mHandler=null;
        if (null != radioListener) {
            radioListener.recycleCache();
            radioListener.closeLine();
            radioListener.cloasCache();
            radioListener = null;
        }

        //Utils.onClearMemory(this);

        listView = null;
        ll_code_bg = null;
        tv_Title = null;      //中间标题  倒计时
        ll_back = null;       //返回
        tv_orderno = null;    //订单号
        tv_hint = null;    //卡支付成功提示
        tv_total_money = null;

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
