package org.caojun.salmagundi.bankcard.pboc;

import android.text.TextUtils;

import org.caojun.salmagundi.utils.FormatUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * 卡片信息
 * Created by CaoJun on 2016/8/26.
 */
public class Card {
    //当前卡的历史记录
    private List<Record> records;
    // 交易选项
    private TradeOption option;
    // 磁条2信息
    private String track2;
    // 应用主账号序列号
    private String panSeq;
    // 发卡行公钥证书
    private byte[] bankPubKey;
    // 发卡行公钥余数
    private byte[] bankPubKeyLeft;
    // 发卡行公钥指数
    private byte[] bankPubKeyPower;
    // IC卡公钥证书
    private byte[] icPubKey;
    // IC卡公钥余数
    private byte[] icPubKeyLeft;
    // IC卡公钥指数
    private byte[] icPubKeyPower;
    // 产品标识信息
    private byte[] cardIDInfos;
    // CA公钥索引
    private byte PKI;
    // 动态数据认证数据对象列表（DDOL）
    private byte[] ddol;
    // 服务代码
    private String serviceCode;
    // 签名的静态应用数据（SAD）
    private byte[] SAD;
    // 持卡人验证方法（CVM）列表
    private byte[] CVM;
    // 发卡行行为代码（IAC）-缺省
    private byte[] defaultIAC;
    // 发卡行行为代码（IAC）-拒绝
    private byte[] rejectIAC;
    // 发卡行行为代码（IAC）-联机
    private byte[] onlineIAC;
    // 应用生效日期, 格式yyMMdd
    private String createDate;
    // 应用失效日期, 格式yyMMdd
    private String expireDate;
    // 主Pan
    private String pan;
    // 应用版本号
    private String version;
    // 应用用途控制
    private byte[] AUC;
    // 卡片风险管理数据对象列表1（CDOL1）
    private DOL cdol1;
    // 卡片风险管理数据对象列表2 （CDOL2）
    private DOL cdol2;
    // 连续脱机交易下限
    private int offlineTradeDown = -1;
    // 连续脱机交易上限
    private int offlineTradeUp = -1;
    // 电子现金
    private double EcashBalance;
    // 电子现金上限
    private double EcashBalanceLimit;

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof Card)) {
            return false;
        }
        if (TextUtils.isEmpty(pan) || TextUtils.isEmpty(((Card) o).pan)) {
            return false;
        }
        if (pan.compareTo(((Card) o).pan) != 0) {
            return false;
        }
        if (TextUtils.isEmpty(track2)
                || TextUtils.isEmpty(((Card) o).track2)) {
            return false;
        }
        if (track2.compareTo(((Card) o).track2) != 0) {
            return false;
        }
        if (EcashBalanceLimit != ((Card) o).EcashBalanceLimit) {
            return false;
        }
        if (EcashBalance != ((Card) o).EcashBalance) {
            return false;
        }
        if (TextUtils.isEmpty(createDate)
                || TextUtils.isEmpty(((Card) o).createDate)) {
            return false;
        }
        if (createDate.compareTo(((Card) o).createDate) != 0) {
            return false;
        }
        if (TextUtils.isEmpty(expireDate)
                || TextUtils.isEmpty(((Card) o).expireDate)) {
            return false;
        }
        if (expireDate.compareTo(((Card) o).expireDate) != 0) {
            return false;
        }
        return true;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();

        buf.append("卡号：").append(this.pan).append("\r\n");
        buf.append("二磁道：").append(this.track2).append("\r\n");
        buf.append("应用版本号：").append(this.version).append("\r\n");
        buf.append("有效期（YYMMDD）：").append(this.expireDate).append("\r\n");
        buf.append("服务代码：").append(this.serviceCode).append("\r\n");
        return buf.toString();
    }

    /**
     * 设置卡属性
     *
     * @param record
     */
    public void setCardInfo(BelTLV record) {
        String flag = record.getFlag();
        if ("57".equals(flag)) {
            // 磁道2
            String track2 = FormatUtils.byteArrayToHexString((byte[]) record
                    .getValues());
            // 过滤一下右填充的F
            int index = track2.indexOf("F");
            if (index >= 0) {
                track2 = track2.substring(0, index);
            }
            this.setTrack2(track2);
        } else if ("5F34".equals(flag)) {
            // Pan序号
            String seq = FormatUtils.byteArrayToHexString((byte[]) record
                    .getValues());
            this.setPanSeq(seq);
        } else if ("90".equals(flag)) {
            // 发卡行公钥
            this.setBankPubKey((byte[]) record.getValues());
        } else if ("92".equals(flag)) {
            // 发卡行公钥余数
            this.setBankPubKeyLeft((byte[]) record.getValues());
        } else if ("9F32".equals(flag)) {
            // 发卡行公钥指数
            this.setBankPubKeyPower((byte[]) record.getValues());
        } else if ("9F46".equals(flag)) {
            // IC卡公钥
            this.setIcPubKey((byte[]) record.getValues());
        } else if ("9F48".equals(flag)) {
            // IC卡公钥余数
            this.setIcPubKeyLeft((byte[]) record.getValues());
        } else if ("9F47".equals(flag)) {
            // IC卡公钥指数
            this.setIcPubKeyPower((byte[]) record.getValues());
        } else if ("9F63".equals(flag)) {
            // 发卡行信息
            this.setCardIDInfos(((byte[]) record.getValues()));
        } else if ("8F".equals(flag)) {
            // CA公钥序号
            this.setPKI(((byte[]) record.getValues())[0]);
        } else if ("9F49".equals(flag)) {
            // DDOL数据
            this.setDdol((byte[]) record.getValues());
        } else if ("5F30".equals(flag)) {
            // 服务代码
            this.setServiceCode(FormatUtils
                    .byteArrayToHexString((byte[]) record.getValues()));
        } else if ("93".equals(flag)) {
            // 脱机数据认证SAD签名数据
            this.setSAD((byte[]) record.getValues());
        } else if ("8E".equals(flag)) {
            // CVM列表
            this.setCVM((byte[]) record.getValues());
        } else if ("9F0D".equals(flag)) {
            // 发卡行行为-默认
            this.setDefaultIAC((byte[]) record.getValues());
        } else if ("9F0E".equals(flag)) {
            // 发卡行行为-拒绝
            this.setRejectIAC((byte[]) record.getValues());
        } else if ("9F0F".equals(flag)) {
            // 发卡行行为-联机
            this.setOnlineIAC((byte[]) record.getValues());
        } else if ("5F25".equals(flag)) {
            // 生效日期
            this.setCreateDate(FormatUtils
                    .byteArrayToHexString((byte[]) record.getValues()));
        } else if ("5F24".equals(flag)) {
            // 过期日期
            this.setExpireDate(FormatUtils
                    .byteArrayToHexString((byte[]) record.getValues()));
        } else if ("5A".equals(flag)) {
            // PAN
            String pan = FormatUtils.byteArrayToHexString((byte[]) record
                    .getValues());
            // 过滤右填充的F
            int index = pan.indexOf("F");
            if (index >= 0) {
                pan = pan.substring(0, index);
            }
            this.setPan(pan);
        } else if ("9F08".equals(flag)) {
            // 版本
            this.setVersion(FormatUtils.byteArrayToHexString((byte[]) record
                    .getValues()));
        } else if ("9F07".equals(flag)) {
            // AUC
            this.setAUC((byte[]) record.getValues());
        } else if ("8C".equals(flag)) {
            // CDOL1数据
            this.cdol1 = new DOL((byte[]) record.getValues());
        } else if ("8D".equals(flag)) {
            // CDOL2数据
            this.cdol2 = new DOL((byte[]) record.getValues());
        } else if ("9F14".equals(flag)) {
            // 脱机交易下限
            this.setOfflineTradeDown((int) ((byte[]) record.getValues())[0]);
        } else if ("9F23".equals(flag)) {
            // 脱机交易上限
            this.setOfflineTradeUp((int) ((byte[]) record.getValues())[0]);
        } else if ("84".equals(flag)) {
            // CommonUtils.OutputLog("84:",
            // record.getValues().toString());
        }
    }
    public void setCardInfos(BelTLV tlv) {

        for (BelTLV record : (List<BelTLV>) tlv.getValues()) {
            setCardInfo(record);
        }
    }

    public TradeOption getOption() {
        return option;
    }

    public void setOption(TradeOption option) {
        this.option = option;
    }

    public String getTrack2() {
        return track2;
    }

    public void setTrack2(String track2) {
        this.track2 = track2;
    }

    public String getPanSeq() {
        if (TextUtils.isEmpty(panSeq) )
        {
            panSeq = "000";
        }
        else
        {
            panSeq = "000" + panSeq;
            panSeq = panSeq.substring(panSeq.length() - 3, panSeq.length());
        }
        return panSeq;
    }

    public void setPanSeq(String panSeq) {
        this.panSeq = panSeq;
    }

    public byte[] getBankPubKey() {
        return bankPubKey;
    }

    public void setBankPubKey(byte[] bankPubKey) {
        this.bankPubKey = bankPubKey;
    }

    public byte[] getBankPubKeyLeft() {
        return bankPubKeyLeft;
    }

    public void setBankPubKeyLeft(byte[] bankPubKeyLeft) {
        this.bankPubKeyLeft = bankPubKeyLeft;
    }

    public byte[] getBankPubKeyPower() {
        return bankPubKeyPower;
    }

    public void setBankPubKeyPower(byte[] bankPubKeyPower) {
        this.bankPubKeyPower = bankPubKeyPower;
    }

    public byte[] getIcPubKey() {
        return icPubKey;
    }

    public void setIcPubKey(byte[] icPubKey) {
        this.icPubKey = icPubKey;
    }

    public byte[] getIcPubKeyLeft() {
        return icPubKeyLeft;
    }

    public void setIcPubKeyLeft(byte[] icPubKeyLeft) {
        this.icPubKeyLeft = icPubKeyLeft;
    }

    public byte[] getIcPubKeyPower() {
        return icPubKeyPower;
    }

    public void setIcPubKeyPower(byte[] icPubKeyPower) {
        this.icPubKeyPower = icPubKeyPower;
    }

    public byte[] getCardIDInfos() {
        return cardIDInfos;
    }

    public void setCardIDInfos(byte[] cardIDInfos) {
        this.cardIDInfos = cardIDInfos;
    }

    public byte getPKI() {
        return PKI;
    }

    public void setPKI(byte pKI) {
        PKI = pKI;
    }

    public byte[] getDdol() {
        return ddol;
    }

    public void setDdol(byte[] ddol) {
        this.ddol = ddol;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public byte[] getSAD() {
        return SAD;
    }

    public void setSAD(byte[] sAD) {
        SAD = sAD;
    }

    public byte[] getCVM() {
        return CVM;
    }

    public void setCVM(byte[] cVM) {
        CVM = cVM;
    }

    public byte[] getDefaultIAC() {
        return defaultIAC;
    }

    public void setDefaultIAC(byte[] defaultIAC) {
        this.defaultIAC = defaultIAC;
    }

    public byte[] getRejectIAC() {
        return rejectIAC;
    }

    public void setRejectIAC(byte[] rejectIAC) {
        this.rejectIAC = rejectIAC;
    }

    public byte[] getOnlineIAC() {
        return onlineIAC;
    }

    public void setOnlineIAC(byte[] onlineIAC) {
        this.onlineIAC = onlineIAC;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public byte[] getAUC() {
        return AUC;
    }

    public void setAUC(byte[] aUC) {
        AUC = aUC;
    }

    public DOL getCdol1() {
        return cdol1;
    }

    public DOL getCdol2() {
        return cdol2;
    }

    public int getOfflineTradeDown() {
        return offlineTradeDown;
    }

    public void setOfflineTradeDown(int offlineTradeDown) {
        this.offlineTradeDown = offlineTradeDown;
    }

    public int getOfflineTradeUp() {
        return offlineTradeUp;
    }

    public void setOfflineTradeUp(int offlineTradeUp) {
        this.offlineTradeUp = offlineTradeUp;
    }

    public void setEcashBalance(BigDecimal ecashBalance) {
        this.EcashBalance = ecashBalance.doubleValue();
    }

    public double getEcashBalance() {
        return this.EcashBalance;
    }

    public void setEcashBalanceLimit(BigDecimal ecashBalanceLimit) {
        this.EcashBalanceLimit = ecashBalanceLimit.doubleValue();
    }

    public double getEcashBalanceLimit() {
        return this.EcashBalanceLimit;
    }
}
