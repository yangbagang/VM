package com.ybg.rp.vm.entity.db;


import java.io.Serializable;

import xutils.db.annotation.Column;
import xutils.db.annotation.Table;

/**
 * 轨道或格子柜子柜对象
 * <p/>
 * 包            名:      com.cnpay.vending.yifeng.entity
 * 类            名:      TrackBean
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公            司:
 *
 * @author yuyucheng
 * @version V1.0
 * @date 2016/4/6 0006
 */
@Table(name = "TRACK_VENDING")
public class TrackBean implements Serializable {
    private static final long serialVersionUID = -4499406945914405276L;
    /**
     * 轨道-良好
     */
    public static final int FAULT_O = 0;
    /**
     * 轨道-故障
     */
    public static final int FAULT_E = 1;
//
//    @Column(name = "id")
//    private long id;

    /**
     * 0：良好,1：故障
     */
    @Column(name = "FAULT")
    private Integer fault;

    /**
     * 1：格子柜,0：不是格子柜
     */
    @Column(name = "GRID_MARK")
    private Integer gridMark;

    /**
     * 所属机器编号
     */
    @Column(name = "LAYER_NO")
    public String layerNo;
    /**
     * 轨道编号
     */
    @Column(name = "TRACK_NO", isId = true)
    private String trackNo;
    /**
     * 最大库存
     */
    @Column(name = "MAX_INVENTORY")
    private Integer maxInventory;


//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }

    public Integer getFault() {
        return fault;
    }

    public void setFault(Integer fault) {
        this.fault = fault;
    }

    public Integer getGridMark() {
        return gridMark;
    }

    public void setGridMark(Integer gridMark) {
        this.gridMark = gridMark;
    }

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

    public Integer getMaxInventory() {
        return maxInventory;
    }

    public void setMaxInventory(Integer maxInventory) {
        this.maxInventory = maxInventory;
    }

    @Override
    public String toString() {
        return "TrackBean{" +
//                "id=" + id +
                ", fault=" + fault +
                ", gridMark=" + gridMark +
                ", layerNo='" + layerNo + '\'' +
                ", trackNo='" + trackNo + '\'' +
                ", maxInventory=" + maxInventory +
                '}';
    }
}
