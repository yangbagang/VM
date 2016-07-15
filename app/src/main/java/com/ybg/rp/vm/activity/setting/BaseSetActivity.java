package com.ybg.rp.vm.activity.setting;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.ybg.rp.vm.listener.KeyboardTouchListener;
import com.ybg.rp.vm.util.AppPreferences;
import com.ybg.rp.vm.util.KeyboardUtil;
import com.cnpay.tigerbalm.utils.GsonUtils;
import com.cnpay.tigerbalm.utils.SimpleUtil;
import com.cnpay.tigerbalm.utils.StrUtil;
import com.cnpay.tigerbalm.utils.TbLog;
import com.cnpay.tigerbalm.utils.ToastUtil;
import com.cnpay.tigerbalm.utils.dialog.DialogUtil;
import com.cnpay.tigerbalm.utils.dialog.fragment.AlertDialogFragment;
import com.ybg.rp.vm.R;
import com.ybg.rp.vm.activity.base.BaseActivity;
import com.ybg.rp.vm.adapter.CabinetListAdapter;
import com.ybg.rp.vm.app.OPApplication;
import com.ybg.rp.vm.entity.db.LayerBean;
import com.ybg.rp.vm.entity.db.Operator;
import com.ybg.rp.vm.entity.db.TrackBean;
import com.ybg.rp.vm.listener.KeyBoardStateListener;
import com.ybg.rp.vm.listener.TrackSetListener;
import com.ybg.rp.vm.net.NetWorkUtil;
import com.ybg.rp.vm.net.WHAT;
import com.ybg.rp.vm.net.YFHttpListener;
import com.ybg.rp.vm.util.helper.DbSetHelper;
import com.ybg.rp.vm.util.helper.EntityDBUtil;
import com.ybg.rp.vm.util.dialog.BaseProgressUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;

import java.util.ArrayList;

/**
 * 基础设置页面
 */
public class BaseSetActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private ScrollView scrollView;
    /**
     * 开关 主机
     */
    private ToggleButton tg_main_0, tg_main_7, tg_main_8;
    private TextView tv_00, tv_07, tv_08;
    /**
     * 层级数量
     */
    private EditText edt_main_track_0, edt_main_track_1, edt_main_track_2, edt_main_track_3, edt_main_track_4,
            edt_main_track_5, edt_main_track_6, edt_main_track_7, edt_main_track_8;
    /**
     * 排放量设置
     */
    private TextView tv_main_emission_0, tv_main_emission_1, tv_main_emission_2, tv_main_emission_3,
            tv_main_emission_4, tv_main_emission_5, tv_main_emission_6, tv_main_emission_7,
            tv_main_emission_8;

    /**
     * 格子柜设置
     */
    private TextView tv_addCabinet;
    private ListView lv_cabinet;
    private CabinetListAdapter listAdapter;

    private NetWorkUtil netWorkUtil;
    private DbSetHelper helper;

    private ArrayList<LayerBean> lyList;
    private ArrayList<LayerBean> cabinetList;
    private KeyboardUtil keyboardUtil;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String str = (String) msg.obj;
                    ToastUtil.showToast(BaseSetActivity.this, str);
                    break;
                case 2:
                    /** 主机设置*/
                    LayerBean layerBean = (LayerBean) msg.obj;
                    setMainLayout(layerBean.getLayerNo(), layerBean.getTrackNum());
                    break;
                case 3:
                    /** 格子柜设置*/
                    SimpleUtil.setListViewHeightBasedOnChildren(lv_cabinet);
                    listAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_manage_base);
        initTitle("基础设置", true);
        setRightContent(R.mipmap.icon_upd, "上传");
        setRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击上传
                updBaseVMData();
            }
        });

        netWorkUtil = NetWorkUtil.getInstance();
        helper = DbSetHelper.getInstance(this);

        lyList = new ArrayList<>();
        cabinetList = new ArrayList<>();
        findLayout();
        initListener();
    }

    /**
     * 初始化控件
     */
    private void findLayout() {
        scrollView = (ScrollView) findViewById(R.id.sv_layout);
        tg_main_0 = (ToggleButton) findViewById(R.id.container_sv_main_0);
        tg_main_7 = (ToggleButton) findViewById(R.id.container_sv_main_7);
        tg_main_8 = (ToggleButton) findViewById(R.id.container_sv_main_8);
        tv_00 = (TextView) findViewById(R.id.container_tv_main_track_0);
        tv_07 = (TextView) findViewById(R.id.container_tv_main_track_7);
        tv_08 = (TextView) findViewById(R.id.container_tv_main_track_8);

        edt_main_track_0 = (EditText) findViewById(R.id.container_edt_main_track_0);
        edt_main_track_1 = (EditText) findViewById(R.id.container_edt_main_track_1);
        edt_main_track_2 = (EditText) findViewById(R.id.container_edt_main_track_2);
        edt_main_track_3 = (EditText) findViewById(R.id.container_edt_main_track_3);
        edt_main_track_4 = (EditText) findViewById(R.id.container_edt_main_track_4);
        edt_main_track_5 = (EditText) findViewById(R.id.container_edt_main_track_5);
        edt_main_track_6 = (EditText) findViewById(R.id.container_edt_main_track_6);
        edt_main_track_7 = (EditText) findViewById(R.id.container_edt_main_track_7);
        edt_main_track_8 = (EditText) findViewById(R.id.container_edt_main_track_8);

        tv_main_emission_0 = (TextView) findViewById(R.id.container_tv_main_emission_0);
        tv_main_emission_1 = (TextView) findViewById(R.id.container_tv_main_emission_1);
        tv_main_emission_2 = (TextView) findViewById(R.id.container_tv_main_emission_2);
        tv_main_emission_3 = (TextView) findViewById(R.id.container_tv_main_emission_3);
        tv_main_emission_4 = (TextView) findViewById(R.id.container_tv_main_emission_4);
        tv_main_emission_5 = (TextView) findViewById(R.id.container_tv_main_emission_5);
        tv_main_emission_6 = (TextView) findViewById(R.id.container_tv_main_emission_6);
        tv_main_emission_7 = (TextView) findViewById(R.id.container_tv_main_emission_7);
        tv_main_emission_8 = (TextView) findViewById(R.id.container_tv_main_emission_8);

        tv_addCabinet = (TextView) findViewById(R.id.container_tv_addCabinet);
        lv_cabinet = (ListView) findViewById(R.id.container_lv_cabinet);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(BaseSetActivity.this, MaxGoodSetActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        switch (v.getId()) {
            case R.id.container_tv_main_emission_0:
                intent.putExtra("layer", "00");
                break;
            case R.id.container_tv_main_emission_1:
                intent.putExtra("layer", "01");
                break;
            case R.id.container_tv_main_emission_2:
                intent.putExtra("layer", "02");
                break;
            case R.id.container_tv_main_emission_3:
                intent.putExtra("layer", "03");
                break;
            case R.id.container_tv_main_emission_4:
                intent.putExtra("layer", "04");
                break;
            case R.id.container_tv_main_emission_5:
                intent.putExtra("layer", "05");
                break;
            case R.id.container_tv_main_emission_6:
                intent.putExtra("layer", "06");
                break;
            case R.id.container_tv_main_emission_7:
                intent.putExtra("layer", "07");
                break;
            case R.id.container_tv_main_emission_8:
                intent.putExtra("layer", "08");
                break;
        }

        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new AsyncTask<String, Integer, Boolean>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                /**开始*/
                //YFDialogUtil.showLoadding(BaseSetActivity.this);
                BaseProgressUtils.showBaseDialog(BaseSetActivity.this, "");
                setMainSelect(null, false);
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                /**结果返回*/
                //YFDialogUtil.removeDialog(BaseSetActivity.this);
                BaseProgressUtils.cancelBaseDialog();
            }

            @Override
            protected Boolean doInBackground(String... params) {
                /**
                 * 获取主机信息
                 */
                lyList.clear();
                lyList.addAll(helper.getMainLayer());
                /**
                 * 获取当前保存格子柜信息
                 */
                cabinetList.clear();
                cabinetList.addAll(helper.getCabinetList());

                if (lyList.size() > 0) {
                    for (LayerBean lb : lyList) {
                        Message msg = new Message();
                        msg.what = 2;
                        msg.obj = lb;
                        handler.sendMessage(msg);
                    }
                }

                if (cabinetList.size() > 0) {
                    Message msg = new Message();
                    msg.what = 3;
                    //msg.obj = "";
                    handler.sendMessage(msg);
                }
                return true;
            }
        }.execute();
    }


    /**
     * 根据读取的数据设置布局
     * 设置主机
     */
    private void setMainLayout(String layer, int selectNum) {
        if (layer.equals("01")) {
            edt_main_track_1.setText(String.valueOf(selectNum));
            edt_main_track_1.setSelection(String.valueOf(selectNum).length());
        } else if (layer.equals("02")) {
            edt_main_track_2.setText(String.valueOf(selectNum));
            edt_main_track_2.setSelection(String.valueOf(selectNum).length());
        } else if (layer.equals("03")) {
            edt_main_track_3.setText(String.valueOf(selectNum));
            edt_main_track_3.setSelection(String.valueOf(selectNum).length());
        } else if (layer.equals("04")) {
            edt_main_track_4.setText(String.valueOf(selectNum));
            edt_main_track_4.setSelection(String.valueOf(selectNum).length());
        } else if (layer.equals("05")) {
            edt_main_track_5.setText(String.valueOf(selectNum));
            edt_main_track_5.setSelection(String.valueOf(selectNum).length());
        } else if (layer.equals("06")) {
            edt_main_track_6.setText(String.valueOf(selectNum));
            edt_main_track_6.setSelection(String.valueOf(selectNum).length());
        } else if (layer.equals("07")) {
            tg_main_7.setChecked(true);
            edt_main_track_7.setText(String.valueOf(selectNum));
            edt_main_track_7.setSelection(String.valueOf(selectNum).length());
        } else if (layer.equals("08")) {
            tg_main_8.setChecked(true);
            edt_main_track_8.setText(String.valueOf(selectNum));
            edt_main_track_8.setSelection(String.valueOf(selectNum).length());
        } else if (layer.equals("00")) {
            tg_main_0.setChecked(true);
            edt_main_track_0.setText(String.valueOf(selectNum));
            edt_main_track_0.setSelection(String.valueOf(selectNum).length());
        }
    }


    /**
     * 设置监听
     */
    private void initListener() {
        tv_main_emission_0.setOnClickListener(this);
        tv_main_emission_1.setOnClickListener(this);
        tv_main_emission_2.setOnClickListener(this);
        tv_main_emission_3.setOnClickListener(this);
        tv_main_emission_4.setOnClickListener(this);
        tv_main_emission_5.setOnClickListener(this);
        tv_main_emission_6.setOnClickListener(this);
        tv_main_emission_7.setOnClickListener(this);
        tv_main_emission_8.setOnClickListener(this);

        tg_main_0.setOnCheckedChangeListener(this);
        tg_main_7.setOnCheckedChangeListener(this);
        tg_main_8.setOnCheckedChangeListener(this);

        /**配置安全输入*/
        keyboardUtil = new KeyboardUtil(this, ll_rootView, scrollView);
        // 控制 KeyBord state
        keyboardUtil.setKeyBoardStateChangeListener(new KeyBoardStateListener(BaseSetActivity.this));
        // 控制 finish or next Key
        keyboardUtil.setInputOverListener(new TrackSetListener());

        KeyboardTouchListener touchListener = new KeyboardTouchListener(keyboardUtil, KeyboardUtil.INPUTTYPE_NUM_FINISH, -1);
        edt_main_track_0.setOnTouchListener(touchListener);
        edt_main_track_1.setOnTouchListener(touchListener);
        edt_main_track_2.setOnTouchListener(touchListener);
        edt_main_track_3.setOnTouchListener(touchListener);
        edt_main_track_4.setOnTouchListener(touchListener);
        edt_main_track_5.setOnTouchListener(touchListener);
        edt_main_track_6.setOnTouchListener(touchListener);
        edt_main_track_7.setOnTouchListener(touchListener);
        edt_main_track_8.setOnTouchListener(touchListener);


        listAdapter = new CabinetListAdapter(new CabinetListAdapter.CabinetListener() {
            @Override
            public void modifyCabinet(int position) {
                /**修改*/
                LayerBean cabinet = cabinetList.get(position);
                if (cabinet != null) {
                    fixCabinet(cabinet);
                }
            }

            @Override
            public void deleteCabinet(int position) {
                /**删除*/
                LayerBean cabinet = cabinetList.get(position);
                if (cabinet != null) {
                    showWarn(cabinet.getLayerNo(), position);
                }
            }
        }, BaseSetActivity.this, cabinetList);
        lv_cabinet.setAdapter(listAdapter);

        tv_addCabinet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCabinet();
            }
        });
    }


    /*********************************************************************
     * 主机操作部分
     ******************************************************************/

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        /**可选择启用与否*/
        switch (buttonView.getId()) {
            case R.id.container_sv_main_0:
                setMainSelect("00", isChecked);
                break;
            case R.id.container_sv_main_7:
                setMainSelect("07", isChecked);
                break;
            case R.id.container_sv_main_8:
                setMainSelect("08", isChecked);
                break;
        }
    }

    /**
     * 根据设置,设置主机轨道数不可输入
     */
    private void setMainSelect(String cabinetNo, boolean isSelect) {
        if (isSelect) {
            TbLog.i("[-选中 添加主机 :" + cabinetNo + " 层");
        } else {
            TbLog.i("[-不选中 删除主机 :" + cabinetNo + " 层");
        }
        if (!StrUtil.isEmpty(cabinetNo)) {
            if (cabinetNo.equals("00")) {
                if (isSelect) {
                    edt_main_track_0.setVisibility(View.VISIBLE);
                    tv_00.setVisibility(View.VISIBLE);
                } else {
                    edt_main_track_0.setVisibility(View.GONE);
                    tv_00.setVisibility(View.GONE);
                    deleteMain("00");
                }

            } else if (cabinetNo.equals("07")) {
                if (isSelect) {
                    edt_main_track_7.setVisibility(View.VISIBLE);
                    tv_07.setVisibility(View.VISIBLE);
                } else {
                    edt_main_track_7.setVisibility(View.GONE);
                    tv_07.setVisibility(View.GONE);
                    deleteMain("07");
                }
            } else if (cabinetNo.equals("08")) {
                if (isSelect) {
                    edt_main_track_8.setVisibility(View.VISIBLE);
                    tv_08.setVisibility(View.VISIBLE);
                } else {
                    edt_main_track_8.setVisibility(View.GONE);
                    tv_08.setVisibility(View.GONE);
                    deleteMain("08");
                }
            }
        } else {
            edt_main_track_0.setVisibility(View.GONE);
            tv_00.setVisibility(View.GONE);
            edt_main_track_7.setVisibility(View.GONE);
            tv_07.setVisibility(View.GONE);
            edt_main_track_8.setVisibility(View.GONE);
            tv_08.setVisibility(View.GONE);
        }
    }

    /**
     * 删除主机信息
     * @param layerNo 指定层级轨道
     */
    private void deleteMain(final String layerNo) {
        new AsyncTask<String, Integer, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {
                synchronized (this) {
                    try {
                        helper.delLayer(layerNo);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        }.execute();
    }
    /****************************************************
     *                  格子柜操作
     * ***********************************************/
    /**
     * 提示
     */
    private void showWarn(final String cabinetNo, final int position) {
        DialogUtil.showAlertDialog(BaseSetActivity.this,
                "是否删除格子柜", "删除后无法进行购买", new AlertDialogFragment.DialogOnClickListener() {
                    @Override
                    public void onPositiveClick() {
                        TbLog.i("-------ok-------------");
                        DialogUtil.removeDialog(BaseSetActivity.this);

                        cutCabinet(cabinetNo);
                        /**刷新页面*/
                        cabinetList.remove(position);
                        Message msg = new Message();
                        msg.what = 3;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onNegativeClick() {
                        TbLog.i("-------ocancel-------------");
                        DialogUtil.removeDialog(BaseSetActivity.this);
                    }
                });
    }

    /**
     * 删除格子柜
     */
    private void cutCabinet(final String cabinetNo) {
        new AsyncTask<String, Integer, Boolean>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                BaseProgressUtils.showBaseDialog(BaseSetActivity.this, "");
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                BaseProgressUtils.cancelBaseDialog();
            }

            @Override
            protected Boolean doInBackground(String... params) {
                helper.delLayer(cabinetNo);
                return null;
            }
        }.execute();

    }

    /**
     * 跳转修改格子柜信息
     *
     * @param cabinet 指定格子柜
     */
    private void fixCabinet(LayerBean cabinet) {
        Intent intent = new Intent(BaseSetActivity.this, SelectCabinetActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("op", 1);
        Bundle bundle = new Bundle();
        bundle.putSerializable("cabinet", cabinet);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1000);
    }

    /**
     * 新增格子柜
     */
    private void addCabinet() {
        if (cabinetList.size() >= 6) {
            ToastUtil.showCenterToast(BaseSetActivity.this, "格子柜数量已满");
            return;
        }

        Intent intent = new Intent(BaseSetActivity.this, SelectCabinetActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("op", 0);
        intent.putExtra("index", 0);
        startActivityForResult(intent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == 1001) {
            TbLog.i("[-格子柜操作成功=" + data.getBooleanExtra("select", true) + "]");
        }
    }

    /********************************************************
     *                  数据上传
     * *****************************************************/
    /**
     * 层级、轨道数保存
     */
    public void updBaseVMData() {
        //YFDialogUtil.showLoadding(ContainerManyActivity.this);
        Operator operator = OPApplication.getInstance().getOper();
        if (null == operator) {
            ToastUtil.showToast(BaseSetActivity.this, "操作员错误，请重新登录");
            return;
        }
        final long operId = operator.getOperId();
        final ArrayList<LayerBean> lvList = EntityDBUtil.getInstance().findAll(LayerBean.class);
        Request<String> request = netWorkUtil.post("updFloorsTrackSet1");
        // 添加请求参数
        request.add("machineId", appPreferences.getVMId());//机器ID
        request.add("operId", operId);//轨道编号
        String gsons = GsonUtils.toJsonProperties(lvList, "layerNo", "trackNum");
        TbLog.i("---上传的轨道信息:" + gsons);
        request.add("gsons", gsons);
        netWorkUtil.add(BaseSetActivity.this, WHAT.VM_UPD_BASE_LEYAR, request, new YFHttpListener<String>() {
            @Override
            public void onSuccess(int what, Response<String> response) {
                updTrack(operId, lvList);
            }

            @Override
            public void onFailed(int what, String msg) {
                TbLog.e(msg);
                ToastUtil.showToast(BaseSetActivity.this, msg);
                //YFDialogUtil.removeDialog(ContainerManyActivity.this);
            }
        });
    }

    /**
     * 最大库存数据
     *
     * @param operId
     * @param lvList
     */
    private void updTrack(final long operId, final ArrayList<LayerBean> lvList) {
        new AsyncTask<String, Integer, Boolean>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                //YFDialogUtil.removeDialog(ContainerManyActivity.this);
            }

            @Override
            protected Boolean doInBackground(String... params) {
                try {
                    Request<String> request = netWorkUtil.post("maxEmissionSet1");
                    for (int i = 0; i < lvList.size(); i++) {
                        LayerBean layerBean = lvList.get(i);
                        ArrayList<TrackBean> itemList = (ArrayList<TrackBean>) EntityDBUtil.getInstance().getDb().selector(TrackBean.class)
                                .where("LAYER_NO", "=", layerBean.getLayerNo()).findAll();
                        // 添加请求参数
                        request.add("machineId", appPreferences.getVMId());//机器ID
                        request.add("operId", operId);

                        request.add("layerNo", layerBean.getLayerNo());//当前层

                        String gsons = GsonUtils.toJsonProperties(itemList, "trackNo", "maxInventory");
                        TbLog.i("机器id" + appPreferences.getVMId()
                                +"-----------gsons:"+gsons);
                        request.add("gsons", gsons);//数据
                        //同步请求
                        Response<String> response = NoHttp.startRequestSync(request);
                        if (response.isSucceed()) {
                            TbLog.e("数据上传成功" + i + "-----" + lvList.size());
                            if (lvList.size() == (i + 1)) {
                                TbLog.i("数据上传成功--- ");
                                sendMsg("数据上传成功");
                            }
                        } else {
                            TbLog.e("上传失败--- ");
                            sendMsg("上传失败");
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        }.execute();
    }

    public void sendMsg(String obj) {
        Message msg = new Message();
        msg.what = 1;
        msg.obj = obj;
        handler.sendMessage(msg);
    }


    /**********************************************************
     *                  键盘控制
     ****************************************************/


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (keyboardUtil.isShow) {
                keyboardUtil.hideSystemKeyBoard();
                keyboardUtil.hideAllKeyBoard();
                keyboardUtil.hideKeyboardLayout();
            } else {
                return super.onKeyDown(keyCode, event);
            }

            return false;
        } else
            return super.onKeyDown(keyCode, event);
    }

    private AppPreferences appPreferences = AppPreferences.getInstance();
}
