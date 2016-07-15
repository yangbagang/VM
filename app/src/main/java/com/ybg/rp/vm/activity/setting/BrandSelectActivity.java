package com.ybg.rp.vm.activity.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.cnpay.tigerbalm.utils.TbLog;
import com.ybg.rp.vm.R;
import com.ybg.rp.vm.activity.base.BaseActivity;
import com.ybg.rp.vm.entity.db.SerialBrand;
import com.ybg.rp.vm.util.helper.EntityDBUtil;

/**
 * 机器品牌设置,选择调用的串口
 * <p>
 * 包            名:      com.ybg.rp.vm.activity.setting
 * 类            名:      BrandSelectActivity
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公            司:
 *
 * @author yuyucheng
 * @version V1.0
 * @date 2016/6/23 0023
 */
public class BrandSelectActivity extends BaseActivity implements View.OnClickListener {
    private CheckBox cb_main_yifeng, cb_main_yile, cb_main_fushi;
    private CheckBox cb_cabinet_yifeng, cb_cabinet_yile, cb_cabinet_fushi;

    private EntityDBUtil dbUtil;
    private String yifeng = "yifeng";
    private String yile = "yile";
    private String fushi = "fushi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_manage_serial);
        initTitle("品牌选择", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dbUtil = EntityDBUtil.getInstance();
        findLayout();
        setListener();
        loadData();
    }

    private void findLayout() {
        cb_main_yifeng = (CheckBox) findViewById(R.id.serial_main_yifeng);
        cb_main_yile = (CheckBox) findViewById(R.id.serial_main_yile);
        cb_main_fushi = (CheckBox) findViewById(R.id.serial_main_fushi);

        cb_cabinet_yifeng = (CheckBox) findViewById(R.id.serial_cabinet_yifeng);
        cb_cabinet_yile = (CheckBox) findViewById(R.id.serial_cabinet_yile);
        cb_cabinet_fushi = (CheckBox) findViewById(R.id.serial_cabinet_fushi);
    }

    private void loadData() {
        try {
            /**1：格子柜,0：不是格子柜*/
            /**获取主机选择的品牌*/
            SerialBrand main_b = dbUtil.getDb().selector(SerialBrand.class).where("GRID_MARK", "=", "0").findFirst();
            if (main_b == null) {
                main_b = new SerialBrand();
                main_b.setGridMark(0);
                main_b.setBrand(yifeng);
            }
            setMainCheck(main_b);
            /**获取格子柜的选择品牌*/
            SerialBrand cabinet_b = dbUtil.getDb().selector(SerialBrand.class).where("GRID_MARK", "=", "1").findFirst();
            if (cabinet_b == null) {
                cabinet_b = new SerialBrand();
                cabinet_b.setGridMark(1);
                cabinet_b.setBrand(yifeng);
            }
            setCabinetCheck(cabinet_b);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取主机串口
     *
     * @param brand 点击选择的品牌
     */
    private void getMainCheck(String brand) {
        TbLog.i("[ getMainCheck " + brand + "]");
        try {
            SerialBrand main_b = dbUtil.getDb().selector(SerialBrand.class).where("GRID_MARK", "=", "0").findFirst();
            if (main_b != null) {
                if (!main_b.getBrand().equals(brand)) {
                    main_b.setBrand(brand);//更新品牌
                    setMainCheck(main_b);//更新数据库与UI
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置主机串口
     *
     * @param m 被选中品牌
     */
    private void setMainCheck(SerialBrand m) {
        TbLog.i("[ setMainCheck " + m.toString() + "]");
        cb_main_yifeng.setChecked(false);
        cb_main_yile.setChecked(false);
        cb_main_fushi.setChecked(false);

        String brand = m.getBrand();
        if (brand.equals(yifeng)) {
            cb_main_yifeng.setChecked(true);
        } else if (brand.equals(yile)) {
            cb_main_yile.setChecked(true);
        } else if (brand.equals(fushi)) {
            cb_main_fushi.setChecked(true);
        }

        dbUtil.saveOrUpdate(m);
    }

    /**
     * 获取格子柜串口
     *
     * @param brand 点击选择的品牌
     */
    private void getCabinetCheck(String brand) {
        try {
            SerialBrand cabinet_b = dbUtil.getDb().selector(SerialBrand.class).where("GRID_MARK", "=", "1").findFirst();

            if (cabinet_b != null) {
                if (!cabinet_b.getBrand().equals(brand)) {
                    cabinet_b.setBrand(brand);
                    setCabinetCheck(cabinet_b);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置格子柜串口
     *
     * @param c 被选中品牌
     */
    private void setCabinetCheck(SerialBrand c) {
        cb_cabinet_yifeng.setChecked(false);
        cb_cabinet_yile.setChecked(false);
        cb_cabinet_fushi.setChecked(false);

        String brand = c.getBrand();
        if (brand.equals(yifeng)) {
            cb_cabinet_yifeng.setChecked(true);
        } else if (brand.equals(yile)) {
            cb_cabinet_yile.setChecked(true);
        } else if (brand.equals(fushi)) {
            cb_cabinet_fushi.setChecked(true);
        }

        dbUtil.saveOrUpdate(c);
    }

    private void setListener() {
        cb_main_yifeng.setOnClickListener(this);
        cb_main_yile.setOnClickListener(this);
        cb_main_fushi.setOnClickListener(this);

        cb_cabinet_yifeng.setOnClickListener(this);
        cb_cabinet_yile.setOnClickListener(this);
        cb_cabinet_fushi.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.serial_main_yifeng:
                getMainCheck(yifeng);
                break;
            case R.id.serial_main_yile:
                getMainCheck(yile);
                break;
            case R.id.serial_main_fushi:
                getMainCheck(fushi);
                break;

            case R.id.serial_cabinet_yifeng:
                getCabinetCheck(yifeng);
                break;
            case R.id.serial_cabinet_yile:
                getCabinetCheck(yile);
                break;
            case R.id.serial_cabinet_fushi:
                getCabinetCheck(fushi);
                break;
        }
    }
}
