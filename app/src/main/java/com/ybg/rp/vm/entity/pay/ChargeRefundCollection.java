package com.ybg.rp.vm.entity.pay;

import java.io.Serializable;
import java.util.List;

/**
 * 包            名:      com.cnpay.ppvending.entity.pay
 * 类            名:      ChargeRefundCollection
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公            司:
 *
 * @author yuyucheng
 * @version V1.0
 * @date 2016/1/18 0018
 */
public class ChargeRefundCollection implements Serializable{
    private static final long serialVersionUID = -8404434755192215762L;
    private String object;
    private String url;
    private Boolean hasMore;
    private List<Refund> data;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getHasMore() {
        return hasMore;
    }

    public void setHasMore(Boolean hasMore) {
        this.hasMore = hasMore;
    }

    public List<Refund> getData() {
        return data;
    }

    public void setData(List<Refund> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ChargeRefundCollection{" +
                "object='" + object + '\'' +
                ", url='" + url + '\'' +
                ", hasMore=" + hasMore +
                ", data=" + data +
                '}';
    }
}
