package com.ybg.rp.vm.entity;

import java.io.Serializable;

/**
 * 查询 线上支付返回数据
 * 包            名:      com.cnpay.ppvending.entity
 * 类            名:      BackPay
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公             司:
 *
 * @author liyuanming
 * @version V1.0
 * @date 2016/3/7
 */
public class BackPay implements Serializable {
    private static final long serialVersionUID = -1369701031038409505L;


    /**
     * 支付状态
     */
    private boolean isPay;

    /**
     * 订单状态0：下单中 1：成功 2：完成  3 取消
     */

    private String orderStatus;

    /**
     * 发货状态 0未发货 1已发货
     */
    private String deliveryStatus;

    public boolean success;

    private String payWay;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isPay() {
        return isPay;
    }

    public void setIsPay(boolean isPay) {
        this.isPay = isPay;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    @Override
    public String toString() {
        return "BackPay{" +
                "isPay=" + isPay +
                ", orderStatus='" + orderStatus + '\'' +
                ", deliveryStatus='" + deliveryStatus + '\'' +
                '}';
    }
}
