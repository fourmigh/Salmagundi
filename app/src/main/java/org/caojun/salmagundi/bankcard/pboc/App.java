package org.caojun.salmagundi.bankcard.pboc;

import java.util.List;

/**
 * 应用定义
 * Created by CaoJun on 2016/8/26.
 */
public class App {
    // 应用的AID
    private String aid;
    // 应用名称
    private String label;
    // 优先级，1最高
    private int priority;
    // 语言
    private String lan;
    // 发卡行代码索引
    private byte bankCode;
    // PDOL
    private DOL pdol;
    // 发卡行自定义数据
    private List<BelTLV> bankData;

    public String toString() {
        StringBuilder buf = new StringBuilder();

        buf.append("aid:").append(this.getAid()).append("\r\n");
        buf.append("label:").append(this.getLabel()).append("\r\n");
        buf.append("priority:").append(this.getPriority()).append("\r\n");
        buf.append("lan:").append(this.getLan()).append("\r\n");
        buf.append("bankCode:").append(this.getBankCode()).append("\r\n");
        buf.append("pdol:").append(this.getPdol().toString())
                .append("\r\n");

        return buf.toString();
    }

    public DOL getDOL()
    {
        return pdol;
    }

    /**
     * 选择应用
     *
     * @param data
     */
    public void select(BelTLV data) {
        for (BelTLV tlv : (List<BelTLV>) data.getValues()) {
            if ("A5".equals(tlv.getFlag())) {
                // 获得应用的应用模板
                for (BelTLV sub : (List<BelTLV>) tlv.getValues()) {
                    String flag = sub.getFlag();
                    if ("9F38".equals(flag)) {
                        // 获取IC卡支持的PDOL列表
                        this.pdol = new DOL((byte[]) sub.getValues());
                    } else if ("BF0C".equals(flag)) {
                        // 发卡行自定义数据
                        this.bankData = (List<BelTLV>) tlv.getValues();
                    } else if ("5F2D".equals(flag)) {
                        // 首选语言
                        this.lan = new String((byte[]) sub.getValues());
                    } else if ("9F11".equals(flag)) {
                        // 发卡行代码表索引
                        this.bankCode = ((byte[]) sub.getValues())[0];
                    }
                }
            }
        }
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getAid() {
        return aid;
    }

    public String getLabel() {
        return label;
    }

    public int getPriority() {
        return priority;
    }

    public DOL getPdol() {
        return pdol;
    }

    public String getLan() {
        return lan;
    }

    public byte getBankCode() {
        return bankCode;
    }
}
