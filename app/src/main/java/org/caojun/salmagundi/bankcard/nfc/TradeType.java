package org.caojun.salmagundi.bankcard.nfc;

/**
 * 交易类型常量定义
 * @author CaoJun
 *
 */
public class TradeType
{
    // 电子现金余额
    public static final byte NFCBAL = 0x00;
    // 查询余额交易
    public static final byte QUERY = 0x08;
    // 转账类交易
    public static final byte TRANSFER = 0x04;
    // 圈存交易
    public static final byte DEBIT = 0x02;
    // 圈存记录
    public static final byte RECORD = 0xA;
}
