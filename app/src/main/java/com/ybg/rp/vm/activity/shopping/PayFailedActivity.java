package com.ybg.rp.vm.activity.shopping;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.ybg.rp.vm.R;
import com.ybg.rp.vm.activity.base.BaseActivity;

/**
 * 支付失败界面
 *
 * Created by zenghonghua on 2016/6/23 0023.
 */
public class PayFailedActivity extends BaseActivity {

    private TextView tv_tint;
    private TextView tv_time;

    private int time = 10;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_pay_failed);

        initTitle("");

        tv_time = (TextView) findViewById(R.id.payFailed_tv_time);
        tv_tint = (TextView) findViewById(R.id.payFailed_tv_tint);

        mHandler.postDelayed(runnable, 1000);
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
