package org.caojun.salmagundi.bankcard.pboc;

/**
 * 应用初始化
 * Created by CaoJun on 2016/8/26.
 */
public class TradeOption {
    // 卡片支持SDA，静态脱机认证
    private boolean supportSDA;
    // 卡片支持DDA，动态脱机认证
    private boolean supportDDA;
    // 卡片支持持卡人认证
    private boolean supportCVM;
    // 卡片支持终端风险管理
    private boolean supportTRM;
    // 卡片支持发卡行认证
    private boolean supportBV;
    // 卡片支持CDA，复合脱机认证
    private boolean supportCDA;
    // AIP
    private byte AIP;
    private byte[] AFL;

    /**
     * 返回AEF文件的大小
     *
     * @return
     */
    public int getAEFNum() {
        return this.AFL.length >>> 2;
    }

    /**
     * 返回第index个AEF的SFI
     *
     * @param index
     * @return
     */
    public byte getSFI(int index) {
        return (byte) (this.AFL[(index << 2)] >>> 3);
    }

    /**
     * 返回第index个AEF的第一条记录序号
     *
     * @param index
     * @return
     */
    public int getStartRecord(int index) {
        return this.AFL[(index << 2) + 1];
    }

    /**
     * 返回第index个AEF的最后一条记录序号
     *
     * @param index
     * @return
     */
    public int getEndRecord(int index) {
        return this.AFL[(index << 2) + 2];
    }

    public void setAIP(byte[] aIP) {
        byte flag = aIP[0];
        this.AIP = flag;
        this.supportSDA = ((flag & 0x40) == 0x40);
        this.supportDDA = ((flag & 0x20) == 0x20);
        this.supportCVM = ((flag & 0x10) == 0x10);
        this.supportTRM = ((flag & 0x08) == 0x08);
        this.supportBV = ((flag & 0x04) == 0x04);
        this.supportCDA = ((flag & 0x01) == 0x01);
    }

    public byte getAIP() {
        return AIP;
    }

    public boolean isSupportSDA() {
        return supportSDA;
    }

    public boolean isSupportDDA() {
        return supportDDA;
    }

    public boolean isSupportCVM() {
        return supportCVM;
    }

    public boolean isSupportTRM() {
        return supportTRM;
    }

    public boolean isSupportBV() {
        return supportBV;
    }

    public boolean isSupportCDA() {
        return supportCDA;
    }

    public void setAFL(byte[] aFL) {
        AFL = aFL;
    }
}
