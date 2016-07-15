package com.ybg.rp.vm.activity.login;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnpay.tigerbalm.utils.StrUtil;
import com.cnpay.tigerbalm.utils.ToastUtil;
import com.google.zxing.WriterException;
import com.ybg.rp.vm.R;
import com.ybg.rp.vm.activity.base.BaseActivity;
import com.ybg.rp.vm.util.AppPreferences;
import com.ybg.rp.vm.util.Utils;

/**
 * 操作员登录
 */
public class LoginActivity extends BaseActivity {

    private ImageView iv_code;  //二维码
    private TextView tv_sn;     //机器MAC
    private Bitmap bitmap;

    public static LoginActivity loginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginActivity= this;

        iv_code = (ImageView) findViewById(R.id.login_iv_code);
        tv_sn = (TextView) findViewById(R.id.login_tv_sn);

        initTitle("");
        createCode2();
    }

    /**
     * 生成二维码
     */
    private void createCode2() {
        String vmCode = AppPreferences.getInstance().getVMCode();
        try {
            if (!StrUtil.isEmpty(vmCode)) {
                tv_sn.setText("设备编号 :    " + vmCode);
                bitmap = Utils.Create2DCode(vmCode);
                iv_code.setImageBitmap(bitmap);
            } else {
                ToastUtil.showToast(this, "生成登录二维码故障");
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmap != null && !bitmap.isRecycled()) {
            iv_code.setImageBitmap(null);
            bitmap.recycle();
            bitmap = null;
        }
    }
}
