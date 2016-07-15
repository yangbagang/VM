package com.ybg.rp.vm.entity;

/**
 * 错误轨道 适配用
 *
 * 包            名:      com.ybg.rp.vm.entity
 * 类            名:      TrackError
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公            司:
 *
 * @author yuyucheng
 * @version V1.0
 * @date 2016/6/23 0023
 */
public class TrackError {
    private String layerNo;
    private String trackNo;
    private boolean isSelect;

    public String getLayerNo() {
        return layerNo;
    }

    public void setLayerNo(String layerNo) {
        this.layerNo = layerNo;
    }

    public String getTrackNo() {
        return trackNo;
    }

    public void setTrackNo(String trackNo) {
        this.trackNo = trackNo;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    @Override
    public String toString() {
        return "TrackError{" +
                "layerNo='" + layerNo + '\'' +
                ", trackNo='" + trackNo + '\'' +
                ", isSelect=" + isSelect +
                '}';
    }
}
