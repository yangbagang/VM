package com.ybg.rp.vm.entity;

import java.io.Serializable;

/**
 * 商品信息
 *
 * @author zenghonghua
 * @包名: com.cnpay.vending.yifeng.entity
 * @修改记录:
 * @公司:
 * @date 2016/4/11 0011
 */
public class GoodsInfo implements Serializable {

    private static final long serialVersionUID = -6488486654503211765L;
    /**
     * ID
     */
    private String gid;
    /** 图片地址*/
    private String goodsPic;
    /** 名字*/
    private String goodsName;
    /** 价格*/
    private Double price;
    /**规格*/
    private String goodsDesc;
    /**库存*/
    private int kucun;

    /**
     * 商品数量,本地使用
     */
    private int num;

    /** 轨道*/
    private String trackNo;


    @Override
    public String toString() {
        return "GoodsInfo{" +
                "gid='" + gid + '\'' +
                ", goodsPic='" + goodsPic + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", price=" + price +
                ", goodsDesc='" + goodsDesc + '\'' +
                ", kucun=" + kucun +
                ", num=" + num +
                ", trackNo='" + trackNo + '\'' +
                '}';
    }

    public String getgId() {
        return gid;
    }

    public void setgId(String gId) {
        this.gid = gId;
    }

    public String getGoodsPic() {
        return goodsPic;
    }

    public void setGoodsPic(String goodsPic) {
        this.goodsPic = goodsPic;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public int getKucun() {
        return kucun;
    }

    public void setKucun(int kucun) {
        this.kucun = kucun;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getTrackNo() {
        return trackNo;
    }

    public void setTrackNo(String trackNo) {
        this.trackNo = trackNo;
    }
}
