package com.ybg.rp.vm.entity.db.util;

import com.cnpay.tigerbalm.utils.DateUtil;
import com.ybg.rp.vm.app.OPApplication;
import com.ybg.rp.vm.entity.db.LogInfo;
import com.ybg.rp.vm.entity.db.Operator;
import com.ybg.rp.vm.entity.db.TrackBean;
import com.ybg.rp.vm.entity.db.TranDataUpdate;

/**
 * 包            名:      com.ybg.rp.vm.entity.db.util
 * 类            名:      DbUtils
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公             司:
 *
 * @author liyuanming
 * @version V1.0
 * @date 2016/6/14
 */
public class DbUtils extends BaseDBCommon {

    private static DbUtils dbUtil;

    public static DbUtils getInstance() {
        if (null == dbUtil) {
            dbUtil = new DbUtils();
        }
        return dbUtil;
    }

    private DbUtils() {
        super();
    }

    /**
     * 保存LOG日志
     *
     * @param content 日志
     */
    public void saveLog(String content) {
        try {
            LogInfo log = new LogInfo();
            log.setContent(content);
            log.setCreateDate(DateUtil.getCurrentDate(DateUtil.dateFormatYMDHMS));
            Operator operator = OPApplication.getInstance().getOper();
            if (null != operator) {
                log.setOperId(operator.getOperatorId());
                log.setOperName(operator.getOperatorName());
            }
            getDb().save(log);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 记录错误轨道信息
     *
     * @param trackNo 错误轨道
     */
    public void saveFaultTrackNo(String trackNo) {
        try {
            TrackBean tv = getDb().selector(TrackBean.class).where("TRACK_NO", "=", trackNo).findFirst();
            if (null == tv) {
                return;
            }
            tv.setFault(TrackBean.FAULT_E);
            dbUtil.saveOrUpdate(tv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 服务器上传
     *
     * @param orderNo
     * @param isUpd
     */
    public void saveForTranUpdate_Service(String orderNo, boolean isUpd) {
        try {
            TranDataUpdate tran = (TranDataUpdate) findFirst(TranDataUpdate.class, "ORDER_NO", "=", orderNo);
            if (null == tran) {
                tran = new TranDataUpdate();
                tran.setOrderNo(orderNo);
            }
            if (isUpd)
                tran.setServiceUpd(TranDataUpdate.UPD_SUCCESS);
            else
                tran.setServiceUpd(TranDataUpdate.UPD_FULT);
            getDb().saveOrUpdate(tran);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
