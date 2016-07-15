package com.ybg.rp.vm.entity;

/**
 * 商品大类信息
 *
 * @author zenghonghua
 * @包名: com.cnpay.vending.yifeng.entity
 * @修改记录:
 * @公司:
 * @date 2016/4/14 0014
 */
public class BigGoodsInfo {


    /**
     * id : 1                   //大类别id
     * category : 默认大类      //名称
     * categoryType : 1
     * createDate : 1454223736000
     * createUser : wangju
     * des : 系统默认
     * keyword : 大类
     * sort : 1
     * status : 1
     * updateDate : 1454223739000
     * updateUser : wangju
     */

    private int id;
    private String category;
    private int categoryType;
    private long createDate;
    private String createUser;
    private String des;
    private String keyword;
    private int sort;
    private int status;
    private long updateDate;
    private String updateUser;

    public void setId(int id) {
        this.id = id;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCategoryType(int categoryType) {
        this.categoryType = categoryType;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public int getCategoryType() {
        return categoryType;
    }

    public long getCreateDate() {
        return createDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public String getDes() {
        return des;
    }

    public String getKeyword() {
        return keyword;
    }

    public int getSort() {
        return sort;
    }

    public int getStatus() {
        return status;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public String getUpdateUser() {
        return updateUser;
    }
}
