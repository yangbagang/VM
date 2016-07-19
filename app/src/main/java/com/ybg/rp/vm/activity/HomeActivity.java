package com.ybg.rp.vm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnpay.tigerbalm.utils.ToastUtil;
import com.ybg.rp.vm.R;
import com.ybg.rp.vm.activity.base.BaseActivity;
import com.ybg.rp.vm.activity.login.LoginActivity;
import com.ybg.rp.vm.activity.setting.ManageSetActivity;
import com.ybg.rp.vm.activity.shopping.ShoppingActivity;
import com.ybg.rp.vm.entity.db.TrackBean;
import com.ybg.rp.vm.listener.NumInputChangedListener;
import com.ybg.rp.vm.listener.NumTouchListener;
import com.ybg.rp.vm.util.helper.EntityDBUtil;

import java.util.ArrayList;

/**
 * 主页
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_logo_click;    //进入操作员登录界面
    private LinearLayout ll_shopping_cart;  //进入购物车界面

    /**
     * 数字按钮与输入
     */
    private EditText edit_trackNum;
    private TextView tv_inputInfo;
    private TextView bt_delete, bt_deleteAll;
    private TextView bt_num_01, bt_num_02, bt_num_03, bt_num_04;
    private TextView bt_num_05, bt_num_06, bt_num_07, bt_num_08;
    private TextView bt_num_09, bt_num_0;

    private ArrayList<TrackBean> allTrackList;
    private EntityDBUtil dbUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        dbUtil = EntityDBUtil.getInstance();
        allTrackList = dbUtil.findAll(TrackBean.class);

        initNoVisibleTitle();
        findView();
        setListener();
    }

    private void findView() {
        ll_shopping_cart = (LinearLayout) findViewById(R.id.home_ll_shopping_cart);
        iv_logo_click = (ImageView) findViewById(R.id.home_iv_logo_click);

        tv_inputInfo = (TextView) findViewById(R.id.home_tv_input);
        edit_trackNum = (EditText) findViewById(R.id.home_edit_trackNum);

        bt_num_01 = (TextView) findViewById(R.id.home_bt_num_01);
        bt_num_02 = (TextView) findViewById(R.id.home_bt_num_02);
        bt_num_03 = (TextView) findViewById(R.id.home_bt_num_03);
        bt_num_04 = (TextView) findViewById(R.id.home_bt_num_04);
        bt_num_05 = (TextView) findViewById(R.id.home_bt_num_05);
        bt_num_06 = (TextView) findViewById(R.id.home_bt_num_06);
        bt_num_07 = (TextView) findViewById(R.id.home_bt_num_07);
        bt_num_08 = (TextView) findViewById(R.id.home_bt_num_08);
        bt_num_09 = (TextView) findViewById(R.id.home_bt_num_09);
        bt_num_0 = (TextView) findViewById(R.id.home_bt_num_0);
        bt_delete = (TextView) findViewById(R.id.home_bt_delete);
        bt_deleteAll = (TextView) findViewById(R.id.home_bt_deleteAll);
    }

    /**
     * 点击事件
     */
    private void setListener() {
        ll_shopping_cart.setOnClickListener(this);

        iv_logo_click.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if ("78".equals(edit_trackNum.getText().toString())) {
                    /** 跳转到登录*/
                    Intent login = new Intent();
                    login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    login.setClass(HomeActivity.this, LoginActivity.class);
                    startActivity(login);
                }
                return true;
            }
        });


        /**键盘点击*/
        NumTouchListener touchListener = new NumTouchListener(edit_trackNum, tv_inputInfo);
        bt_delete.setOnTouchListener(touchListener);
        bt_delete.setLongClickable(true);
        bt_delete.setClickable(true);

        bt_deleteAll.setOnTouchListener(touchListener);
        bt_deleteAll.setLongClickable(true);
        bt_deleteAll.setClickable(true);

        bt_num_0.setOnTouchListener(touchListener);
        bt_num_0.setLongClickable(true);
        bt_num_0.setClickable(true);

        bt_num_01.setOnTouchListener(touchListener);
        bt_num_01.setLongClickable(true);
        bt_num_01.setClickable(true);

        bt_num_02.setOnTouchListener(touchListener);
        bt_num_02.setLongClickable(true);
        bt_num_02.setClickable(true);

        bt_num_03.setOnTouchListener(touchListener);
        bt_num_03.setLongClickable(true);
        bt_num_03.setClickable(true);

        bt_num_04.setOnTouchListener(touchListener);
        bt_num_04.setLongClickable(true);
        bt_num_04.setClickable(true);

        bt_num_05.setOnTouchListener(touchListener);
        bt_num_05.setLongClickable(true);
        bt_num_05.setClickable(true);

        bt_num_06.setOnTouchListener(touchListener);
        bt_num_06.setLongClickable(true);
        bt_num_06.setClickable(true);

        bt_num_07.setOnTouchListener(touchListener);
        bt_num_07.setLongClickable(true);
        bt_num_07.setClickable(true);

        bt_num_08.setOnTouchListener(touchListener);
        bt_num_08.setLongClickable(true);
        bt_num_08.setClickable(true);

        bt_num_09.setOnTouchListener(touchListener);
        bt_num_09.setLongClickable(true);
        bt_num_09.setClickable(true);

        /**数字输入变化*/
        NumInputChangedListener textChangedListener = new NumInputChangedListener(HomeActivity.this,
                edit_trackNum, tv_inputInfo, allTrackList);
        edit_trackNum.addTextChangedListener(textChangedListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_ll_shopping_cart:
                /** 跳转到购物车*/
                Intent shopCart = new Intent();
                shopCart.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                shopCart.setClass(HomeActivity.this, ShoppingActivity.class);
                startActivity(shopCart);
                break;

        }
    }


    /**
     * 双击退出程序
     */
    private boolean is2CallBack = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (!is2CallBack) {
                ToastUtil.showToast(this, "再按一次退出");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        is2CallBack = false;
                    }
                }, 1000);
                is2CallBack = true;
            } else {
                android.os.Process.killProcess(android.os.Process.myPid()); //杀进程方式
            }
        }
        return true;
    }
}
