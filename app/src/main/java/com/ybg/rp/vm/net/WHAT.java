package com.ybg.rp.vm.net;

/**
 * nohttp 请求序号
 * 包            名:      com.cnpay.vending.yifeng.net
 * 类            名:      WHAT
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公             司:
 *
 * @author liyuanming
 * @version V1.0
 * @date 2016/4/13
 */
public interface WHAT {

    /**
     * 公共请求 - VEM 10000 开始
     */
    /*获取商户信息和售卖机ID*/
    int VM_REG = 10001;
    /*数据上传*/
    int VM_UP_DATA = 10002;

    /*数据上传 - 错误上传-继续上传 delay*/
    int VM_UP_DATA_DELAY = 10003;

    /*错误轨道上传*/
    int VM_ERROR_TRACK = 10004;

    /*输入轨道编号查询*/
    int VM_UPD_BASE_LEYAR = 10005;

    /*订单-部分轨道失败*/
    int VM_UP_ERROR_ORDER_TRACK = 10006;


    /*输入轨道编号查询*/
    int VM_INP_TRACKNO = 20001;

    /* 生成订单*/
    int VM_ORDER = 20002;

    /*获取商品信息大类*/
    int VM_GOODSINFO_BIG = 30001;
    /* 获取商品信息详情*/
    int VM_GOODSINFO_DETAIL = 30002;

}
