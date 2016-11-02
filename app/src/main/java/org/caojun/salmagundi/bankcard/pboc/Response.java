package org.caojun.salmagundi.bankcard.pboc;

/**
 * 物理传输层应答报文对象
 * @author CaoJun
 *
 */
public class Response
{

    // 处理状态，4字符编码
    private String status;
    // 返回数据对象
    private BelTLV data;
    // 返回二进制数据
    private byte[] binary;
    private Record record;
    public byte[] getBinary()
    {
        return binary;
    }
    public void setBinary(byte[] binary)
    {
        this.binary = binary;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public BelTLV getData() {
        return data;
    }
    public void setData(BelTLV data)
    {
        this.data = data;
    }
    public void setRecord(Record record)
    {
        this.record=record;
    }
    public Record getRecord()
    {
        return this.record;
    }
}
