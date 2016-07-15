package com.ybg.rp.vm.entity.db;

import com.cnpay.tigerbalm.utils.StrUtil;

import java.io.Serializable;

import xutils.db.annotation.Column;
import xutils.db.annotation.Table;

/**
 * 支付方式数据库保存对象
 *
 * 包            名:      com.cnpay.vending.yifeng.entity.db
 * 类            名:      PayBean
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公            司:
 *
 * @author yuyucheng
 * @version V1.0
 * @date 2016/5/20 0020
 */
@Table(name = "PAYWAY")
public class PayBean implements Serializable{

    private static final long serialVersionUID = -110840385576381009L;

    @Column(name = "id", isId = true)
    private long id;
    /** 1 启用 0 不起用*/
    @Column(name = "ALIPAY")
    private String alipay;

    @Column(name = "WX")
    private String wx;

    @Column(name = "CARD")
    private String card;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAlipay() {
        return alipay;
    }

    public void setAlipay(String alipay) {
        if (StrUtil.isEmpty(alipay) || alipay.equals("0")){
            alipay="1";
        }
        this.alipay = alipay;
    }

    public String getWx() {
        return wx;
    }

    public void setWx(String wx) {
        if (StrUtil.isEmpty(wx) || wx.equals("0")){
            wx="1";
        }
        this.wx = wx;
    }

    public String getCard() {
        if (StrUtil.isEmpty(card)){
            card="0";
        }
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }
}
