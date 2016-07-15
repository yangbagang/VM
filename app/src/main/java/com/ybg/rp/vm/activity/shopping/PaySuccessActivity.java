package com.ybg.rp.vm.activity.shopping;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ybg.rp.vm.R;
import com.ybg.rp.vm.activity.base.BaseActivity;

/**
 * 推送 支付成功页面
 *
 * Created by zenghonghua on 2016/6/23 0023.
 */
public class PaySuccessActivity extends BaseActivity {

    private TextView tv_time;

    private int time = 5;
    private Handler mHandler = new Handler();

    private LinearLayout vending;
    private LinearLayout cabinet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_pay_success);
        initTitle("");

        tv_time = (TextView) findViewById(R.id.paySuccess_tv_time);
        vending = (LinearLayout) findViewById(R.id.ll_vending);
        cabinet = (LinearLayout) findViewById(R.id.ll_cabinet);

        mHandler.postDelayed(runnable, 1000);

        //1：弹簧柜，2：格子柜，3：弹簧柜 + 格子柜
        String type = getIntent().getStringExtra("type");
        if (!TextUtils.isEmpty(type)) {
            if (type.equals("1")) {
                vending.setVisibility(View.VISIBLE);
            } else if (type.equals("2")) {
                cabinet.setVisibility(View.VISIBLE);
            } else {
                vending.setVisibility(View.VISIBLE);
                cabinet.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 倒计时
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            time--;
            if (time == 0) {
                ExitShoppingActivity.getInstance().exit();
                finish();
            } else {
                tv_time.setText(time + "S返回主页");
            }
            mHandler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(runnable);
        mHandler = null;
        tv_time = null;
        //System.gc();
    }

}
