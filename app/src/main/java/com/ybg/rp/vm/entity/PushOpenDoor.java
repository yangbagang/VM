package com.ybg.rp.vm.entity;

/**
 * 包   名:     com.ybg.rp.vm.entity
 * 类   名:     PushOpenDoor
 * 版权所有:     版权所有(C)2010-2016
 * 公   司:
 * 版   本:          V1.0
 * 时   间:     2016/7/4 0004 16:12
 * 作   者:     yuyucheng
 */
public class PushOpenDoor extends PushBase{
    private static final long serialVersionUID = -7723066180224054643L;

    /**
     * title : D03
     * data : D03
     * type : 5
     */

    private String title;
    private String data;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
