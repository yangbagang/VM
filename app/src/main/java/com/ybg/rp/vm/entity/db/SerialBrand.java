package com.ybg.rp.vm.entity.db;

import java.io.Serializable;

import xutils.db.annotation.Column;
import xutils.db.annotation.Table;

/**
 * 主机格子柜所使用得串口
 * <p/>
 * 包            名:      com.ybg.rp.vm.entity.db
 * 类            名:      SerialBrand
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公            司:
 *
 * @author yuyucheng
 * @version V1.0
 * @date 2016/6/23 0023
 */
@Table(name = "SERIAL")
public class SerialBrand implements Serializable {
    private static final long serialVersionUID = -3083419201209733012L;

    /************************************************
     * 机器品牌 标识
     ************************************************/

    public static final String yifeng = "yifeng";
    public static final String yile = "yile";
    public static final String fushi = "fushi";


    @Column(name = "id", isId = true)
    private long id;

    /**
     * 1：格子柜,0：不是格子柜
     */
    @Column(name = "GRID_MARK")
    private Integer gridMark;
    /**
     * 使用品牌
     */
    @Column(name = "USBRAND")
    private String brand;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getGridMark() {
        return gridMark;
    }

    public void setGridMark(Integer gridMark) {
        this.gridMark = gridMark;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public String toString() {
        return "SerialBrand{" +
                "id=" + id +
                ", gridMark=" + gridMark +
                ", brand='" + brand + '\'' +
                '}';
    }
}
