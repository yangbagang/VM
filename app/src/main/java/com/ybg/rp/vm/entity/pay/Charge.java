package com.ybg.rp.vm.entity.pay;

import java.io.Serializable;
import java.util.Map;

/**
 * 包            名:      com.cnpay.ppvending.entity.pay
 * 类            名:      Charge
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公            司:
 *
 * @author yuyucheng
 * @version V1.0
 * @date 2016/1/18 0018
 */
public class Charge implements Serializable{
    private static final long serialVersionUID = -4133780318630531914L;
    private String id;
    private String object;
    private Long created;
    private Boolean livemode;
    private Boolean paid;
    private Boolean refunded;
    private Object app;
    private String channel;
    private String orderNo;
    private String clientIp;
    private Integer amount;
    private Integer amountSettle;
    private String currency;
    private String subject;
    private String body;
    private Long timePaid;
    private Long timeSettle;
    private String transactionNo;
    private ChargeRefundCollection refunds;
    private Integer amountRefunded;
    private String failureCode;
    private String failureMsg;
    private Map<String, String> metadata;
    private Map<String, Object> credential;
    private Map<String, String> extra;
    private String description;

    /** 记录网上重新失败的错误信息 --- 保存错误信息*/
    private String errorInfo ;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public Boolean getLivemode() {
        return livemode;
    }

    public void setLivemode(Boolean livemode) {
        this.livemode = livemode;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Boolean getRefunded() {
        return refunded;
    }

    public void setRefunded(Boolean refunded) {
        this.refunded = refunded;
    }

    public Object getApp() {
        return app;
    }

    public void setApp(Object app) {
        this.app = app;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getAmountSettle() {
        return amountSettle;
    }

    public void setAmountSettle(Integer amountSettle) {
        this.amountSettle = amountSettle;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getTimePaid() {
        return timePaid;
    }

    public void setTimePaid(Long timePaid) {
        this.timePaid = timePaid;
    }

    public Long getTimeSettle() {
        return timeSettle;
    }

    public void setTimeSettle(Long timeSettle) {
        this.timeSettle = timeSettle;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public ChargeRefundCollection getRefunds() {
        return refunds;
    }

    public void setRefunds(ChargeRefundCollection refunds) {
        this.refunds = refunds;
    }

    public Integer getAmountRefunded() {
        return amountRefunded;
    }

    public void setAmountRefunded(Integer amountRefunded) {
        this.amountRefunded = amountRefunded;
    }

    public String getFailureCode() {
        return failureCode;
    }

    public void setFailureCode(String failureCode) {
        this.failureCode = failureCode;
    }

    public String getFailureMsg() {
        return failureMsg;
    }

    public void setFailureMsg(String failureMsg) {
        this.failureMsg = failureMsg;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public Map<String, Object> getCredential() {
        return credential;
    }

    public void setCredential(Map<String, Object> credential) {
        this.credential = credential;
    }

    public Map<String, String> getExtra() {
        return extra;
    }

    public void setExtra(Map<String, String> extra) {
        this.extra = extra;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    @Override
    public String toString() {
        return "Charge{" +
                "id='" + id + '\'' +
                ", object='" + object + '\'' +
                ", created=" + created +
                ", livemode=" + livemode +
                ", paid=" + paid +
                ", refunded=" + refunded +
                ", app=" + app +
                ", channel='" + channel + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", clientIp='" + clientIp + '\'' +
                ", amount=" + amount +
                ", amountSettle=" + amountSettle +
                ", currency='" + currency + '\'' +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                ", timePaid=" + timePaid +
                ", timeSettle=" + timeSettle +
                ", transactionNo='" + transactionNo + '\'' +
                ", refunds=" + refunds +
                ", amountRefunded=" + amountRefunded +
                ", failureCode='" + failureCode + '\'' +
                ", failureMsg='" + failureMsg + '\'' +
                ", metadata=" + metadata +
                ", credential=" + credential +
                ", extra=" + extra +
                ", description='" + description + '\'' +
                ", errorInfo='" + errorInfo + '\'' +
                '}';
    }
}
