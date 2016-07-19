package com.ybg.rp.vm.service;

import android.app.IntentService;
import android.content.Intent;

import com.cnpay.tigerbalm.utils.TaskQueue;
import com.cnpay.tigerbalm.utils.WifiUtil;
import com.cnpay.tigerbalm.utils.task.TaskItem;
import com.cnpay.tigerbalm.utils.task.TaskObjectListener;
import com.ybg.rp.vm.entity.db.ErrorTranData;
import com.ybg.rp.vm.entity.db.TranDataUpdate;
import com.ybg.rp.vm.entity.db.TranOnlineData;
import com.ybg.rp.vm.entity.db.util.DbUtils;
import com.ybg.rp.vm.net.VMRequest;

import java.util.ArrayList;
import java.util.List;

import xutils.db.DbManager;

/**
 * 数据补传
 * 包            名:      com.ybg.rp.vm.service
 * 类            名:      TtimingDataUpService
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公             司:
 *
 * @author liyuanming
 * @version V1.0
 * @date 2016/6/14
 */
public class TtimingDataUpService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public TtimingDataUpService() {
        super("TtimingDataUpService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        TaskQueue mTaskQueue = TaskQueue.getInstance();
        /*查询未上传数据，上传*/
        TaskItem taskItem5 = new TaskItem(new TaskObjectListener() {
            @Override
            public <T> void update(T obj) {
            }

            @Override
            public <T> T getObject() {
                try {
                    boolean isNet = WifiUtil.isConnectivity(TtimingDataUpService.this);
                    //有网络就进行上传,没有网络就不进行
                    if (isNet) {
                        DbManager db = DbUtils.getInstance().getDb();
                        VMRequest instance = VMRequest.getInstance(TtimingDataUpService.this);
                        List<TranDataUpdate> tranUpd = db.selector(TranDataUpdate.class).where("CARD_UPD", "=", TranDataUpdate.UPD_FULT)
                                .or("SERVICE_UPD", "=", TranDataUpdate.UPD_FULT).findAll();
                        if (null != tranUpd) {
                            for (int i = 0; i < tranUpd.size(); i++) {
                                TranDataUpdate dataUpdate = tranUpd.get(i);
                                if (dataUpdate.getServiceUpd() == TranDataUpdate.UPD_FULT) {
                                    //服务器数据上传
                                    TranOnlineData data = db.selector(TranOnlineData.class).where("ORDER_NO", "=", dataUpdate.getOrderNo()).findFirst();
                                    instance.sendSettleData(data, null, true, 0);
                                }
                            }
                        }
                        //上传未上传的订单详情
                        List<ErrorTranData> errorTranDatas = db.selector(ErrorTranData.class).where("IS_UPD", "=", "0").findAll();
                        instance.sendErrorData((ArrayList<ErrorTranData>) errorTranDatas);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }
        });
        mTaskQueue.execute(taskItem5);
    }
}
