package com.ybg.rp.vm.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.cnpay.tigerbalm.utils.StrUtil;
import com.cnpay.tigerbalm.utils.TbLog;
import com.cnpay.tigerbalm.utils.ToastUtil;
import com.cnpay.tigerbalm.utils.dialog.DialogUtil;
import com.cnpay.tigerbalm.utils.dialog.fragment.AlertDialogFragment;
import com.ybg.rp.vm.R;
import com.ybg.rp.vm.activity.base.BaseActivity;
import com.ybg.rp.vm.adapter.SelectCabinetAdapter;
import com.ybg.rp.vm.entity.TrackError;
import com.ybg.rp.vm.entity.db.LayerBean;
import com.ybg.rp.vm.entity.db.TrackBean;
import com.ybg.rp.vm.listener.ResultCallback;
import com.ybg.rp.vm.util.helper.DbSetHelper;
import com.ybg.rp.vm.util.helper.EntityDBUtil;
import com.ybg.rp.vm.util.dialog.BaseProgressUtils;

import java.util.ArrayList;

/**
 * 包            名:      com.ybg.rp.vm.activity.setting
 * 类            名:      SelectCabinetActivity
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公            司:
 *
 * @author yuyucheng
 * @version V1.0
 * @date 2016/6/28 0028
 */
public class SelectCabinetActivity extends BaseActivity implements View.OnClickListener {
    private GridView gv_no, gv_door, gv_max;
    private TextView tv_ok, tv_cancel;
    /**
     * 编号
     */
    private ArrayList<TrackError> noList;
    /**
     * 柜门数量
     */
    private ArrayList<TrackError> doorList;
    /**
     * 排放量
     */
    private ArrayList<TrackError> maxList;

    private DbSetHelper dHelper;
    private EntityDBUtil dbUtil;
    /**
     * 操作方式 0:新增 1:修改
     */
    private int opType = 0;
    private int noIndex = 0;//传进来新增编号
    private boolean isAdd = false;//是否新增格子柜
    private String baseNo;//传进来的原本编号

    private String selectNo;
    private String selectNum;
    private String selectMax;

    private SelectCabinetAdapter noAdapter;
    private SelectCabinetAdapter doorAdapter;
    private SelectCabinetAdapter maxAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.view_select_cabinet);

        initTitle("格子柜编辑", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**取消*/
                cancelSelect(false);
            }
        });

        dbUtil = EntityDBUtil.getInstance();
        dHelper = DbSetHelper.getInstance(this);
        findLayout();
        initData();
        setListener();


        Intent intent = getIntent();
        opType = intent.getIntExtra("op", 0);

        if (opType == 0) {
            /**新增格子柜*/
            noIndex = intent.getIntExtra("index", 0);

            selectNo = noList.get(noIndex).getLayerNo();
            selectNum = "64";
            selectMax = "1";
            isAdd = true;
            setBaseSelect();
        } else {
            /**编辑格子柜*/
            LayerBean cabinet = (LayerBean) intent.getSerializableExtra("cabinet");
            isAdd = false;
            try {
                if (cabinet != null) {
                    baseNo = cabinet.getLayerNo();//保存原本编号
                    selectNo = cabinet.getLayerNo();
                    selectNum = String.valueOf(cabinet.getTrackNum());
                    /**读取轨道样例 获取统一的排放量*/
                    TrackBean tb = dbUtil.getDb().selector(TrackBean.class).where("LAYER_NO", "=", selectNo).findFirst();
                    if (tb != null) {
                        selectMax = String.valueOf(tb.getMaxInventory());
                    } else {
                        selectMax = "1";
                    }
                    setBaseSelect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void findLayout() {
        gv_no = (GridView) findViewById(R.id.select_gv_no);
        gv_door = (GridView) findViewById(R.id.select_gv_num);
        gv_max = (GridView) findViewById(R.id.select_gv_max);
        tv_ok = (TextView) findViewById(R.id.select_tv_ok);
        tv_cancel = (TextView) findViewById(R.id.select_tv_cancel);
    }

    private void setListener() {
        tv_ok.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);

        noAdapter = new SelectCabinetAdapter(SelectCabinetActivity.this, noList);
        gv_no.setAdapter(noAdapter);
        gv_no.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectNo = noList.get(position).getLayerNo();
                refreshChoose(noList, position);
                noAdapter.notifyDataSetChanged();
            }
        });

        doorAdapter = new SelectCabinetAdapter(SelectCabinetActivity.this, doorList);
        gv_door.setAdapter(doorAdapter);
        gv_door.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectNum = doorList.get(position).getLayerNo();
                refreshChoose(doorList, position);
                //ArrayList<TrackError> list = refreshChoose(doorList, position);
                //doorList.clear();
                //doorList.addAll(list);
                doorAdapter.notifyDataSetChanged();
            }
        });

        maxAdapter = new SelectCabinetAdapter(SelectCabinetActivity.this, maxList);
        gv_max.setAdapter(maxAdapter);
        gv_max.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectMax = maxList.get(position).getLayerNo();
                refreshChoose(maxList, position);
                maxAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_tv_ok:
                if (isAdd) {
                    /**新增*/
                    if (canChoose()) {
                        saveCabinet();
                    } else {
                        ToastUtil.showCenterToast(SelectCabinetActivity.this, "该编号已存在");
                    }
                } else {
                    /**修改*/
                    if (selectNo.equals(baseNo)) {
                        /**编号未改变*/
                        saveCabinet();
                    } else {
                        /**改变编号*/
                        showWarn();
                    }
                }

                break;
            case R.id.select_tv_cancel:
                cancelSelect(false);
                break;
        }
    }

    /*************************************************************
     *                  数据设置
     * ************************************************************/

    /**
     * 初始化设置数据
     */
    private void initData() {
        noList = new ArrayList<>();
        doorList = new ArrayList<>();
        maxList = new ArrayList<>();
        /**编号 1~6*/
        for (int i = 1; i < 7; i++) {
            TrackError no = new TrackError();
            no.setLayerNo(String.valueOf(i));
            no.setIsSelect(false);
            noList.add(no);
        }

        /**柜门*/
        TrackError door01 = new TrackError();
        door01.setLayerNo("1");
        door01.setIsSelect(false);
        doorList.add(door01);

        TrackError door02 = new TrackError();
        door02.setLayerNo("50");
        door02.setIsSelect(false);
        doorList.add(door02);

        TrackError door03 = new TrackError();
        door03.setLayerNo("64");
        door03.setIsSelect(false);
        doorList.add(door03);

        TrackError door04 = new TrackError();
        door04.setLayerNo("88");
        door04.setIsSelect(false);
        doorList.add(door04);

        TrackError door05 = new TrackError();
        door05.setLayerNo("99");
        door05.setIsSelect(false);
        doorList.add(door05);

        /**排放量 1~15*/
        for (int i = 1; i < 16; i++) {
            TrackError max = new TrackError();
            max.setLayerNo(String.valueOf(i));
            max.setIsSelect(false);
            maxList.add(max);
        }
    }

    /**
     * 设置初始化选中
     */
    private void setBaseSelect() {
        /*编号*/
        for (int i = 0; i < noList.size(); i++) {
            if (noList.get(i).getLayerNo().equals(selectNo)) {
                noList.get(i).setIsSelect(true);
            }
        }
        /*柜门*/
        for (int i = 0; i < doorList.size(); i++) {
            if (doorList.get(i).getLayerNo().equals(selectNum)) {
                doorList.get(i).setIsSelect(true);
            }
        }
        /*排放量*/
        for (int i = 0; i < maxList.size(); i++) {
            if (maxList.get(i).getLayerNo().equals(selectMax)) {
                maxList.get(i).setIsSelect(true);
            }
        }

        noAdapter.notifyDataSetChanged();
        doorAdapter.notifyDataSetChanged();
        maxAdapter.notifyDataSetChanged();
    }


    /**
     * 刷新选择
     */
    private void refreshChoose(ArrayList<TrackError> datas, int position) {
        for (int i = 0; i < datas.size(); i++) {
            datas.get(i).setIsSelect(false);
        }
        datas.get(position).setIsSelect(true);
    }

    /**
     * 提示是否新增
     */
    private void showWarn() {
        DialogUtil.showAlertDialog(SelectCabinetActivity.this,
                "编号已改变", "是否新增格子柜", new AlertDialogFragment.DialogOnClickListener() {
                    @Override
                    public void onPositiveClick() {
                        TbLog.i("-------ok-------------");
                        DialogUtil.removeDialog(SelectCabinetActivity.this);
                        if (canChoose()) {
                            saveCabinet();
                        } else {
                            ToastUtil.showCenterToast(SelectCabinetActivity.this, "该编号已存在");
                        }
                    }

                    @Override
                    public void onNegativeClick() {
                        TbLog.i("-------ocancel-------------");
                        DialogUtil.removeDialog(SelectCabinetActivity.this);
                    }
                });

    }

    /**
     * 判断格子柜编号是否可用
     */
    private boolean canChoose() {
        try {
            /**判断*/
            ArrayList<LayerBean> list = (ArrayList<LayerBean>) dbUtil.getDb().selector(LayerBean.class)
                    .where("GRID_MARK", "=", "1").orderBy("LAYER_NO").findAll();
            if (list != null && list.size() > 0) {
                for (LayerBean bean : list) {
                    if (bean.getLayerNo().equals(selectNo)) {
                        return false;
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 保存设置
     */
    private void saveCabinet() {
        if (!StrUtil.isEmpty(selectNo) && !StrUtil.isEmpty(selectNum) && !StrUtil.isEmpty(selectMax)) {
            /**保存*/
            dHelper.setCabinet(new ResultCallback.ResultListener() {
                @Override
                public void startFunction() {
                    BaseProgressUtils.showBaseDialog(SelectCabinetActivity.this, "");
                }

                @Override
                public void isResultOK(Boolean ok) {
                    BaseProgressUtils.cancelBaseDialog();
                    cancelSelect(true);
                }
            }, selectNo, Integer.parseInt(selectNum), Integer.parseInt(selectMax));
        }
    }

    /**
     * 取消选择
     */
    private void cancelSelect(Boolean isSelect) {
        Intent intent = new Intent();
        intent.putExtra("select", isSelect);
        setResult(1001, intent);
        finish();
    }
}
