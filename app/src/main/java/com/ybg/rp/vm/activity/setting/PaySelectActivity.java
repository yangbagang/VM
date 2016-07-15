package com.ybg.rp.vm.activity.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

import com.ybg.rp.vm.R;
import com.ybg.rp.vm.activity.base.BaseActivity;

/**
 * 支付方式选择
 *
 * 包            名:      com.ybg.rp.vm.activity.setting
 * 类            名:      PaySelectActivity
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公            司:
 *
 * @author yuyucheng
 * @version V1.0
 * @date 2016/6/23 0023
 */
public class PaySelectActivity extends BaseActivity{
    private ToggleButton tb_baidu,tb_QQPay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_manage_pay);
        initTitle("支付方式", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findLayout();
        tb_baidu.setClickable(false);
        tb_baidu.setChecked(false);
        tb_QQPay.setClickable(false);
        tb_QQPay.setChecked(false);
    }

    private void findLayout(){
        tb_baidu=(ToggleButton)findViewById(R.id.paySelect_sv_baidu);
        tb_QQPay=(ToggleButton)findViewById(R.id.paySelect_sv_QQPay);
    }
}
