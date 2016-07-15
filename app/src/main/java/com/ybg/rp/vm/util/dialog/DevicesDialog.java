package com.ybg.rp.vm.util.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.cnpay.tigerbalm.utils.DateUtil;
import com.cnpay.tigerbalm.utils.SimpleUtil;
import com.cnpay.tigerbalm.utils.ToastUtil;
import com.ybg.rp.vm.R;
import com.ybg.rp.vm.app.OPApplication;
import com.ybg.rp.vm.util.AppPreferences;
import com.ybg.rp.vm.util.Config;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 包            名:      com.cnpay.vending.yifeng.util.dialog
 * 类            名:      DevicesDialog
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公             司:
 *
 * @author liyuanming
 * @version V1.0
 * @date 2016/4/22
 */
public class DevicesDialog extends DialogFragment {

    public static DevicesDialog newInstance(int theme) {
        DevicesDialog dialogFragment = new DevicesDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("theme", theme);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int theme = getArguments().getInt("theme", 0);
        setStyle(DialogFragment.STYLE_NO_TITLE, theme);//设置样式
    }

    private int pos = 0;
    private ArrayList<String> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);//标题
        getDialog().setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失

        View view = inflater.inflate(R.layout.dialog_devices, container);
        ListView lv = (ListView) view.findViewById(R.id.lv_item_devices);
        Button cancel = (Button) view.findViewById(R.id.btn_item_cancel);
        Button saveOk = (Button) view.findViewById(R.id.btn_item_save);

        Window dialogWindow = getDialog().getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.5); // 高度设置为屏幕的0.8
        lp.height = (int) (d.heightPixels * 0.5);
//        lp.width = d.widthPixels; // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YFDialogUtil.removeDialog(getActivity());
            }
        });
        saveOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String basePath = list.get(pos);
                if (basePath.contains("sdcard")) {
                    ToastUtil.showToast(getActivity(), "不能选中当前SD卡路径");
                    return;
                }
                String targetDir = basePath + "/cnpay_vm_" + DateUtil.getCurrentDate(DateUtil.dateFormatYMD) + "_"
                        + AppPreferences.getInstance().getVMId();

                SimpleUtil.copyFolder(Config.BASE_PATH, targetDir);
                YFDialogUtil.removeDialog(getActivity());

            }
        });

        list = SimpleUtil.getStorageDirectory();
        ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < list.size(); i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("deviceName", list.get(i));
            data.add(map);
        }
        lv.setAdapter(new SimpleAdapter(getActivity(), data, R.layout.item_device
                , new String[]{"deviceName"}, new int[]{R.id.tv_item_deivceName}));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                view.setSelected(true);
                pos = position;
            }
        });

        return view;
    }

}
