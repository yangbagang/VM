package com.ybg.rp.vm.activity.login;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ybg.rp.vm.R;
import com.ybg.rp.vm.activity.base.BaseActivity;

/**
 * 扫描成功
 */
public class ScanSuccessActivity extends BaseActivity {

    private TextView tv_back;
    public static ScanSuccessActivity scanSuccessActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        scanSuccessActivity = this;

        initNoVisibleTitle();

        tv_back = (TextView) findViewById(R.id.scanSuccess_tv_back);


        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
