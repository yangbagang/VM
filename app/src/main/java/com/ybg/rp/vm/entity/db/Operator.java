package com.ybg.rp.vm.entity.db;

import com.ybg.rp.vm.entity.PushBase;

/**
 * 操作员 对象
 * 包            名:      com.cnpay.vending.yifeng.entity
 * 类            名:      Operater
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公            司:
 *
 * @author yuyucheng
 * @version V1.0
 * @date 2016/4/14 0014
 */
public class Operator extends PushBase {
    private static final long serialVersionUID = -8308480293219071976L;
    //操作员ID
    private Long operId;
    //操作员名称
    private String operName;
    public Long getOperId() {
        return operId;
    }
    public void setOperId(Long operId) {
        this.operId = operId;
    }
    public String getOperName() {
        return operName;
    }
    public void setOperName(String operName) {
        this.operName = operName;
    }

    @Override
    public String toString() {
        return "Operator{" +
                "operId=" + operId +
                ", operName='" + operName + '\'' +
                '}';
    }
}
