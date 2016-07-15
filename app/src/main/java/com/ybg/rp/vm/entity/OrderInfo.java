package com.ybg.rp.vm.entity;

import java.util.ArrayList;

/**
 * 包            名:      com.cnpay.vending.yifeng.entity.pay
 * 类            名:      OrderInfo
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公             司:
 *
 * @author liyuanming
 * @version V1.0
 * @date 2016/4/14
 */
public class OrderInfo extends PushBase {
    private static final long serialVersionUID = 1664388262712103216L;

    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 订单总额
     */
    private Double orderMoney;
    /**
     * 商品信息
     */
    private ArrayList<GoodsInfo> orderInfos;

    /**
     * 支付类型
     */
    private String payWay;

    /**
     * 是否支付成功(本地使用)
     */
    private boolean payStatus;


    @Override
    public String toString() {
        return "OrderInfo{" +
                "orderNo='" + orderNo + '\'' +
                ", orderMoney=" + orderMoney +
                ", orderInfos=" + orderInfos +
                ", payWay=" + payWay +
                ", payStatus=" + payStatus +
                '}';
    }

    public boolean isPayStatus() {
        return payStatus;
    }

    public void setPayStatus(boolean payStatus) {
        this.payStatus = payStatus;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Double getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(Double orderMoney) {
        this.orderMoney = orderMoney;
    }

    public ArrayList<GoodsInfo> getOrderInfos() {
        return orderInfos;
    }

    public void setOrderInfos(ArrayList<GoodsInfo> orderInfos) {
        this.orderInfos = orderInfos;
    }
}
