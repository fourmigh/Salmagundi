package org.caojun.salmagundi.bankcard.pboc;

/**
 * 应用密文对象，是GenerateAC命令的产物
 * Created by CaoJun on 2016/8/26.
 */
public class AC {
    // 密文信息数据
    private byte CID;
    // 应用交易计数器（ATC）
    private int atc;
    // 应用密文（AC）
    private byte[] ac;
    // 发卡行应用数据
    private byte[] bankDatas;

    public String getACtype() {
        byte flag = (byte) (this.CID & 0xC0);
        if (flag == 0x00) {
            return "AAC";
        } else if (flag == 0x40) {
            return "TC";
        } else if (flag == (byte) 0x80) {
            return "ARQC";
        } else {
            return "";
        }
    }

    public byte getCID() {
        return CID;
    }

    public void setCID(byte cID) {
        CID = cID;
    }

    public int getAtc() {
        return atc;
    }

    public void setAtc(int atc) {
        this.atc = atc;
    }

    public byte[] getAc() {
        return ac;
    }

    public void setAc(byte[] ac) {
        this.ac = ac;
    }

    public byte[] getBankDatas() {
        return bankDatas;
    }

    public void setBankDatas(byte[] bankDatas) {
        this.bankDatas = bankDatas;
    }
}
