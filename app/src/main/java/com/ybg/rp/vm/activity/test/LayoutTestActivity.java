package com.ybg.rp.vm.activity.test;

import android.os.Bundle;

import com.ybg.rp.vm.R;
import com.ybg.rp.vm.activity.base.BaseActivity;

/**
 * 布局测试
 *
 * 包   名:     com.ybg.rp.vm.activity.test
 * 类   名:     LayoutTestActivity
 * 版权所有:     版权所有(C)2010-2016
 * 公   司:
 * 版   本:          V1.0
 * 时   间:     2016/7/7 0007 10:33
 * 作   者:     yuyucheng
 */
public class LayoutTestActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_test);
        initTitle("测试页面");
    }
}
