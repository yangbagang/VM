package com.ybg.rp.vm.activity.test;

import android.os.Bundle;

import com.ybg.rp.vm.R;
import com.ybg.rp.vm.activity.base.BaseActivity;
import com.ybg.rp.vm.util.helper.EntityDBUtil;

/**
 * 曾宏华测试
 *
 * Created by zenghonghua on 2016/6/22 0022.
 */
public class HongHuaTestActivity extends BaseActivity {
    private EntityDBUtil dbUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_honghua_test);
        initTitle("测试页面");

        dbUtil=EntityDBUtil.getInstance();

    }

}
