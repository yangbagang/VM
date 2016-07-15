package com.ybg.rp.vm.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ybg.rp.vm.R;
import com.ybg.rp.vm.activity.base.BaseActivity;
import com.ybg.rp.vm.net.VMRequest;
import com.ybg.rp.vm.util.AppPreferences;
import com.ybg.rp.vm.util.Config;

/**
 * 初始化显示页面
 */
public class InitActivity extends BaseActivity {

    private AppPreferences appPreferences = AppPreferences.getInstance();
    private LinearLayout ll_welcome;
    private ImageView init_img;
    private EditText vm_code;
    private Button btn_update_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_init);

        initNoVisibleTitle();

        if (!appPreferences.hasInit()) {
            appPreferences.init(getSharedPreferences(Config.PREFERENCES, Activity.MODE_PRIVATE));
        }

        ll_welcome = (LinearLayout) findViewById(R.id.init_ll_welcome);
        init_img = (ImageView) findViewById(R.id.init_img);
        vm_code = (EditText) findViewById(R.id.vm_code);
        btn_update_code = (Button) findViewById(R.id.btn_update_code);
        btn_update_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vmCode = vm_code.getText().toString();
                if (vmCode != null && !"".equals(vmCode)) {
                    appPreferences.setVmCode(vmCode);

                    new MyThread(true).start();
                }
            }
        });

        if (appPreferences.isFirstUse()) {
            init_img.setVisibility(View.GONE);
            vm_code.setVisibility(View.VISIBLE);
            btn_update_code.setVisibility(View.VISIBLE);
        } else {
            startAnima();
        }

        // 注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("update_client_id");
        registerReceiver(updateBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(updateBroadcastReceiver);
    }

    private void startAnima() {
        vm_code.setVisibility(View.GONE);
        btn_update_code.setVisibility(View.GONE);
        init_img.setVisibility(View.VISIBLE);

        new MyThread(false).start();

        //做动画初始化信息
        AlphaAnimation anima = new AlphaAnimation(0.3f, 1.0f);
        anima.setDuration(3000);
        ll_welcome.startAnimation(anima);
        anima.setAnimationListener(new InitAnimatiom(this));
    }

    /**
     * 获取售卖机ID
     */
    private class MyThread extends Thread {

        private boolean needBroadcast;

        public MyThread(boolean needBroadcast) {
            this.needBroadcast = needBroadcast;
        }

        @Override
        public void run() {
            super.run();
            VMRequest request = VMRequest.getInstance(InitActivity.this.getApplicationContext());
            request.updateClientId(needBroadcast);
        }
    }

    private class InitAnimatiom implements Animation.AnimationListener {

        private Activity mActivity;

        public InitAnimatiom(Activity mActivity) {
            this.mActivity = mActivity;
        }

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            Intent intent = new Intent();
            intent.setClass(mActivity, HomeActivity.class);
            mActivity.startActivity(intent);
            mActivity.finish();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private BroadcastReceiver updateBroadcastReceiver = new BroadcastReceiver() {

        public String ACTION = "update_client_id";
        public String MSG = "update_client_msg";
        public String IS_SUCCESS = "update_client_is_success";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION.equals(action)) {
                boolean isSuccess = intent.getExtras().getBoolean(IS_SUCCESS);
                String msg = intent.getExtras().getString(MSG);
                if (isSuccess) {
                    startAnima();
                } else {
                    Toast.makeText(InitActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
}
