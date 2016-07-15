package com.ybg.rp.vm.net;


import com.yolanda.nohttp.Response;

/**
 * 结果回调接口
 *
 * @author zenghonghua
 * @包名: com.cnpay.vending.yifeng.net
 * @修改记录:
 * @公司:
 * @date 2016/4/7 0007
 */
public interface YFHttpListener<T> {

    void onSuccess(int what, Response<T> response);

    void onFailed(int what, String msg);
}
