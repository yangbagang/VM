package com.ybg.rp.vm.entity.db;

import android.text.TextUtils;

import com.cnpay.tigerbalm.utils.DateUtil;

import java.io.Serializable;
import java.util.Date;

import xutils.db.annotation.Column;
import xutils.db.annotation.Table;

/**
 * 消费记录
 * <p/>
 * 包            名:      com.cnpay.ppvending.entity.db
 * 类            名:      TranOnlineData
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公            司:
 *
 * @author yuyucheng
 * @version V1.0
 * @date 2016/1/25 0025
 */
@Table(name = "TRAN_ONLINE")
public class TranOnlineData implements Serializable /*BaseCardTranData*/{
    private static final long serialVersionUID = 3361168792395926972L;
    //    @Column(name = "id", isId = true)
//    private long id;
    @Column(name = "CREATE_DATE")
    private String createDate;
    // = DateUtil.getCurrentDate(DateUtil.dateFormatYMDHMS)

    /*******************************************
     * 以下参数，为售卖机服务所用
     *******************************************/
    /**
     * 交易订单号
     */
    @Column(name = "ORDER_NO", isId = true)
    private String orderNo;

    /**
     * 交易时间
     */
    @Column(name = "TRAN_DATE")
    private String tranDate;

    /**
     * 支付方式：0：com.ybg.rp.vm，1：支付宝，2：微信支付
     */
    @Column(name = "PAY_TYPE")
    private String payType;

    /**
     * 交易结果 0 取消 1 成功 2 失败 3(卡交易不确定)
     */
    @Column(name = "SALE_RESULT")
    private String saleResult;

    /**
     * 订单总额
     */
    @Column(name = "ORDER_PRICE")
    private Double orderPrice;

    /**
     * 轨道 s
     */
    @Column(name = "TRACK_NOS")
    private String trackNos;


    /**
     * 商品ID
     *//*
    @Column(name = "GOODS_ID")
    private String goodId;

    *//**
     * 商品名称
     *//*
    @Column(name = "GOODS_NAME")
    private String goodsName;

    *//**
     * 商品价格
     *//*
    @Column(name = "GOODS_PRICE")
    private Double goodsPrice;
    *//**
     * 轨道编号
     *//*
    @Column(name = "TRACK_NO")
    private String trackNo;

    */

    /**
     * 唯一标识 -- 作为数据生成判断
     *//*
    @Column(name = "UUID")
    private String uuId;*/
    public String getCreateDate() {
        if (TextUtils.isEmpty(createDate)) {
            createDate = DateUtil.getCurrentDate(DateUtil.dateFormatYMDHMS);
        }
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }


    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getTranDate() {
        return tranDate;
    }

    public void setTranDate(Date tranDate) {
        String dateStr= DateUtil.getStringByFormat(tranDate, DateUtil.dateFormatYMDHMS);
        this.tranDate = dateStr;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getSaleResult() {
        return saleResult;
    }

    public void setSaleResult(String saleResult) {
        this.saleResult = saleResult;
    }

    public Double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(Double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getTrackNos() {
        return trackNos;
    }

    public void setTrackNos(String trackNos) {
        this.trackNos = trackNos;
    }


    @Override
    public String toString() {
        return "TranOnlineData{" +
                "createDate='" + createDate + '\'' +
                /*", cardLogicType='" + getCardLogicType() + '\'' +
                ", cardNo='" + getCardNo() + '\'' +
                ", cardTrxCount='" + getCardTrxCount() + '\'' +
                ", csnNo='" + getCsnNo() + '\'' +
                ", preCardBalance='" + getPreCardBalance() + '\'' +
                ", psamNo='" + getPsamNo() + '\'' +
                ", trxType='" + getTrxType() + '\'' +
                ", terminalTrxCount='" + getTerminalTrxCount() + '\'' +
                ", cardType='" + getCardType() + '\'' +
                ", trxMoney='" + getTrxMoney() + '\'' +
                ", trxDate='" + getTrxDate() + '\'' +
                ", trxTime='" + getTrxTime() + '\'' +
                ", tac='" + getTac() + '\'' +
                ", prevTrxSeq='" + getPrevTrxSeq() + '\'' +
                ", overdraftMoney='" + getOverdraftMoney() + '\'' +
                ", prevTrxMoney='" + getPrevTrxMoney() + '\'' +
                ", prevTrxType='" + getPrevTrxType() + '\'' +
                ", prevPsamNo='" + getPrevPsamNo() + '\'' +
                ", prevTrxDate='" + getPrevTrxDate() + '\'' +
                ", prevTrxTime='" + getPrevTrxTime() + '\'' +
                ", saleNo='" + getSaleNo() + '\'' +*/
                ", orderNo='" + orderNo + '\'' +
                ", tranDate='" + tranDate + '\'' +
                ", payType='" + payType + '\'' +
                ", saleResult='" + saleResult + '\'' +
                ", orderPrice=" + orderPrice +
                ", trackNos='" + trackNos + '\'' +
                '}';
    }
}
