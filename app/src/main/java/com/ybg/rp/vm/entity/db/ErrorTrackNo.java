package com.ybg.rp.vm.entity.db;


import java.io.Serializable;
import java.util.Date;

import xutils.db.annotation.Column;
import xutils.db.annotation.Table;

/**
 * 错误轨道信息，未上传
 * <p/>
 * 包            名:      com.cnpay.ppvending.entity
 * 类            名:      ErrorTrackNo
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公            司:
 *
 * @author yuyucheng
 * @version V1.0
 * @date 2016/1/18 0018
 */
@Table(name = "ERROR_TRACK_NO")
public class ErrorTrackNo implements Serializable {
    private static final long serialVersionUID = 5270966742525912330L;
    @Column(name = "id", isId = true)
    private long id;

    /**
     * 轨道
     */
    @Column(name = "TRACK_NO")
    private String trackNo;

    /**
     * 错误信息
     */
    @Column(name = "ERR_MSG")
    private String errMsg;

    /**
     * 是否上传 0:未 ,1 :已
     */
    @Column(name = "IS_UPD")
    private int isUpd = 0;


    /**
     * 类型： 0 ： 测试 ， 1： 订单
     */
    @Column(name = "TYPE")
    private int type;

    /**
     * 订单号
     */
    @Column(name = "TYPE")
    private String orderNo;

    @Column(name = "CREATE_TIME")
    private Date date = new Date();


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTrackNo() {
        return trackNo;
    }

    public void setTrackNo(String trackNo) {
        this.trackNo = trackNo;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public int getIsUpd() {
        return isUpd;
    }

    public void setIsUpd(int isUpd) {
        this.isUpd = isUpd;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @Override
    public String toString() {
        return "ErrorTrackNo{" +
                "id=" + id +
                ", trackNo='" + trackNo + '\'' +
                ", errMsg='" + errMsg + '\'' +
                ", isUpd=" + isUpd +
                ", type='" + type + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", date=" + date +
                '}';
    }
}
