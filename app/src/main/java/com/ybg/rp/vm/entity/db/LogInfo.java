package com.ybg.rp.vm.entity.db;


import com.cnpay.tigerbalm.utils.DateUtil;
import com.cnpay.tigerbalm.utils.StrUtil;

import java.io.Serializable;

import xutils.db.annotation.Column;
import xutils.db.annotation.Table;

/**
 * 包            名:      com.cnpay.vending.yifeng.entity.db
 * 类            名:      LogInfo
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公             司:
 *
 * @author liyuanming
 * @version V1.0
 * @date 2016/4/19
 */
@Table(name = "LOG_INFO")
public class LogInfo implements Serializable {
    private static final long serialVersionUID = -1516558409007492906L;

    @Column(name = "id", isId = true)
    private long id;

    @Column(name = "OPER_NAME")
    private String operName;

    @Column(name = "OPER_ID")
    private long operId;

    @Column(name = "CREATE_DATE")
    private String createDate;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "TYPE")
    private int type;


    @Override
    public String toString() {
        return "LogInfo{" +
                "id=" + id +
                ", operName='" + operName + '\'' +
                ", operId=" + operId +
                ", createDate='" + createDate + '\'' +
                ", content='" + content + '\'' +
                ", type=" + type +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOperName() {
        return operName;
    }

    public void setOperName(String operName) {
        this.operName = operName;
    }

    public long getOperId() {
        return operId;
    }

    public void setOperId(long operId) {
        this.operId = operId;
    }

    public String getCreateDate() {
        if (StrUtil.isEmpty(createDate)){
            createDate = DateUtil.getCurrentDate(DateUtil.dateFormatYMDHMS);
        }
        return createDate;
    }

    public void setCreateDate(String createDate) {
        if (StrUtil.isEmpty(createDate)){
            createDate = DateUtil.getCurrentDate(DateUtil.dateFormatYMDHMS);
        }
        this.createDate = createDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
