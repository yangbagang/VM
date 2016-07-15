package com.ybg.rp.vm.entity;

import java.io.Serializable;

/**
 * 推送基类
 * 包            名:      com.cnpay.vending.yifeng.entity.push
 * 类            名:      PushBase
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公             司:
 *
 * @author liyuanming
 * @version V1.0
 * @date 2016/4/14
 */
public class PushBase implements Serializable  {
    private static final long serialVersionUID = 6585836853554422043L;

    /** 打开柜门*/
    public static final String TYPE_OPEN = "1";
    /** 出货*/
    public static final String TYPE_SHIP = "2";

    /** 出货 21寸屏幕使用*/
    public static final String TYPE_SHIP_NEW = "10002";
    /** 推送登录*/
    public static final String TYPE_SIGN_IN = "10003";
    /** 扫描成功*/
    public static final String TYPE_SCAN_SUCCESS = "10004";



    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
