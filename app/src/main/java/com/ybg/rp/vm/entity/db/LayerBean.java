package com.ybg.rp.vm.entity.db;


import java.io.Serializable;

import xutils.db.annotation.Column;
import xutils.db.annotation.Table;

/**
 * 主机轨道层级 或格子柜
 * 包            名:      com.cnpay.vending.yifeng.entity
 * 类            名:      LayerBean
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公            司:
 *
 * @author yuyucheng
 * @version V1.0
 * @date 2016/4/6 0006
 */
@Table(name = "LAYER_VENDING")
public class LayerBean implements Serializable{
    private static final long serialVersionUID = -4319005870122938096L;

//    @Column(name = "id",isId = true)
//    private long id;
    /**
     * 机器编号
     */
    @Column(name = "LAYER_NO", isId = true)
    private String layerNo;
    /**
     * 轨道数,格子数
     */
    @Column(name = "TRACK_NUM")
    private Integer trackNum;
    /**
     * 1：格子柜,0：不是格子柜
     */
    @Column(name = "GRID_MARK")
    private Integer gridMark;

 /*   public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
*/
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

    public Integer getGridMark() {
        return gridMark;
    }

    public void setGridMark(Integer gridMark) {
        this.gridMark = gridMark;
    }

    @Override
    public String toString() {
        return "LayerBean{" +
//                "id=" + id +
                ", layerNo='" + layerNo + '\'' +
                ", trackNum=" + trackNum +
                ", gridMark=" + gridMark +
                '}';
    }
}
