package com.ybg.rp.vm.entity.db;

import java.io.Serializable;

import xutils.db.annotation.Column;
import xutils.db.annotation.Table;

/**
 * 包            名:      com.cnpay.vending.park.entity.db
 * 类            名:      TranDataUpdate
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公             司:
 *
 * @author liyuanming
 * @version V1.0
 * @date 2016/5/20
 */
@Table(name = "TRAN_DATA_UPDATE")
public class TranDataUpdate implements Serializable {
    private static final long serialVersionUID = 1201644629203103749L;

    /**
     * 已上传
     */
    public static int UPD_SUCCESS = 1;
    /**
     * 未上传
     */
    public static int UPD_FULT = 0;

    @Column(name = "ORDER_NO", isId = true)
    private String orderNo;


    @Column(name = "CARD_UPD")
    private int cardUpd = 0;

    @Column(name = "SERVICE_UPD")
    private int serviceUpd = 0;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getCardUpd() {
        return cardUpd;
    }

    public void setCardUpd(int cardUpd) {
        this.cardUpd = cardUpd;
    }

    public int getServiceUpd() {
        return serviceUpd;
    }

    public void setServiceUpd(int serviceUpd) {
        this.serviceUpd = serviceUpd;
    }

    @Override
    public String toString() {
        return "TranDataUpdate{" +
                "orderNo='" + orderNo + '\'' +
                ", cardUpd=" + cardUpd +
                ", serviceUpd=" + serviceUpd +
                '}';
    }
}
