package com.ybg.rp.vm.activity.setting.baseload;

/**
 * 包   名:     com.ybg.rp.vm.activity.setting.baseload
 * 类   名:     DBLayerBean
 * 版权所有:     版权所有(C)2010-2016
 * 公   司:
 * 版   本:          V1.0
 * 时   间:     2016/7/6 0006 10:06
 * 作   者:     yuyucheng
 */
public class DBLayerBean {
    /**
     * 层级编号
     */
    private String layerNo;
    /**
     * 轨道数量
     */
    private Integer trackNum;
    /**
     * 最大排放量
     */
    private Integer trackMax;
    /**
     * 1：格子柜,0：不是格子柜
     */
    private Integer gridMark;

    public String getLayerNo() {
        return layerNo;
    }

    public void setLayerNo(String layerNo) {
        this.layerNo = layerNo;
    }

    public Integer getTrackNum() {
        return trackNum;
    }

    public void setTrackNum(Integer trackNum) {
        this.trackNum = trackNum;
    }

    public Integer getTrackMax() {
        return trackMax;
    }

    public void setTrackMax(Integer trackMax) {
        this.trackMax = trackMax;
    }

    public Integer getGridMark() {
        return gridMark;
    }

    public void setGridMark(Integer gridMark) {
        this.gridMark = gridMark;
    }

    @Override
    public String toString() {
        return "DBLayerBean{" +
                "layerNo='" + layerNo + '\'' +
                ", trackNum=" + trackNum +
                ", trackMax=" + trackMax +
                ", gridMark=" + gridMark +
                '}';
    }
}
