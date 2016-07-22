package com.ybg.rp.vm.entity;

/**
 * 商品大类信息
 *
 * @author yangbagang
 * @包名: com.ybg.rp.vm.entity
 * @修改记录:
 * @公司:
 * @date 2016/4/14 0014
 */
public class BigGoodsInfo {

    private Long id;

    private String name;

    private Short status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BigGoodsInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                '}';
    }
}
