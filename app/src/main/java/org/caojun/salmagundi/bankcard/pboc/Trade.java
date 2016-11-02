package org.caojun.salmagundi.bankcard.pboc;

import java.math.BigDecimal;

/**
 * 交易定义
 * Created by CaoJun on 2016/8/26.
 */
public class Trade {
    // 交易类型
    private byte type;
    // 交易金额
    private BigDecimal amount;
    // 交易日期，格式yyMMdd
    private String date;
    // 交易时间，格式HHmmss
    private String time;
    // 交易随机数，用来防止伪造数据
    private byte[] magic;
    // 授权码
    private String authorizeCode;
    // 应用密文类型
    private String ACtype;
    // 商户名称
    private String merchant;

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getAuthorizeCode() {
        return authorizeCode;
    }

    public void setAuthorizeCode(String authorizeCode) {
        this.authorizeCode = authorizeCode;
    }

    public String getACtype() {
        return ACtype;
    }

    public void setACtype(String aCtype) {
        ACtype = aCtype;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public byte[] getMagic() {
        return magic;
    }

    public void setMagic(byte[] magic) {
        this.magic = magic;
    }
}
