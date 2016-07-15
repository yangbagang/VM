package com.ybg.rp.vm.activity.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnpay.tigerbalm.utils.TbLog;
import com.ybg.rp.vm.R;

/**
 * 基础Activity
 * 包            名:      com.ybg.rp.vm.activity.base
 * 类            名:      BaseActivity
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公             司:
 *
 * @author liyuanming
 * @version V1.0
 * @date 2016/6/3
 */
public class BaseActivity extends AppCompatActivity {

    private TextView tv_Title;      //中间标题
    private RelativeLayout rl_TitleBar;   // 整个标题bar
    private LinearLayout ll_back;       //返回
    private LinearLayout ll_right;      //右侧区域

    private ImageView iv_right;     //右侧图标
    private TextView tv_right;      //右侧文字
    public LinearLayout ll_rootView;
    private LinearLayout ll_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.topbar_layout);


        tv_Title = (TextView) findViewById(R.id.top_tv_title);
        rl_TitleBar = (RelativeLayout) findViewById(R.id.rl_title_Tobar);

        ll_back = (LinearLayout) findViewById(R.id.base_ll_back);
        ll_right = (LinearLayout) findViewById(R.id.base_ll_right);

        iv_right = (ImageView) findViewById(R.id.base_iv_right);
        tv_right = (TextView) findViewById(R.id.base_tv_tight);

        ll_rootView = (LinearLayout) findViewById(R.id.ll_rootView);
        ll_content = (LinearLayout) findViewById(R.id.ll_content);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TbLog.i("[ base onDestroy ]");
        tv_Title = null;
        rl_TitleBar = null;
        ll_back = null;
        ll_right = null;
        ll_content = null;
        iv_right = null;
        tv_right = null;

        System.gc();
    }

    @Override
    public void setContentView(int layoutResID) {
//        super.setContentView(layoutResID);
        setContentLayout(layoutResID);
    }

    /***
     * 设置内容区域
     *
     * @param resId 资源文件ID
     */
    public void setContentLayout(int resId) {
        //        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = LayoutInflater.from(this).inflate(resId, null);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        contentView.setLayoutParams(layoutParams);
        //        contentView.setBackgroundDrawable(null);
        if (null != ll_content) {
            ll_content.addView(contentView);
        }

    }

    /**
     * 设置标题
     * 默认事件返回
     *
     * @param title 标题
     */
    public void initTitle(String title) {
        tv_Title.setText(title);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 设置标题
     *
     * @param title         标题
     * @param clickListener 点击事件
     */
    public void initTitle(String title, View.OnClickListener clickListener) {
        tv_Title.setText(title);
        ll_back.setOnClickListener(clickListener);
    }


    /**
     * 是否显示右侧区域
     *
     * @param title
     * @param haveRightTitle
     */
    public void initTitle(String title, boolean haveRightTitle) {
        tv_Title.setText(title);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (haveRightTitle) {
            ll_right.setVisibility(View.VISIBLE);
        } else {
            ll_right.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 右侧区域点击事件
     *
     * @param clickListener
     */
    public void setRightClick(View.OnClickListener clickListener) {
        ll_right.setOnClickListener(clickListener);
    }

    /**
     * 设置标题
     * 默认事件返回
     */
    public void initNoVisibleTitle() {
        rl_TitleBar.setVisibility(View.GONE);
    }

    /**
     * 设置右侧图标和文字
     *
     * @param resId
     * @param title
     */
    public void setRightContent(int resId, String title) {
        iv_right.setImageResource(resId);
        tv_right.setText(title);
    }
}
