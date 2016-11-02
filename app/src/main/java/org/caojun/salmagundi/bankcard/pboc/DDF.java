package org.caojun.salmagundi.bankcard.pboc;

import java.util.List;

/**
 * 根目录DDF定义
 * Created by CaoJun on 2016/8/26.
 */
public class DDF {
    // DF名称
    private String name;
    // 目录基本文件的SFI
    private byte SFI;

    public DDF(BelTLV tlv) {
        this._parseDDF(tlv);
    }

    /**
     * 解析DDF对象
     *
     * @param data
     * @return
     */
    private void _parseDDF(BelTLV data) {

        for (BelTLV tlv : (List<BelTLV>) data.getValues()) {
            String flag = tlv.getFlag();
            if ("A5".equals(flag)) {
                // FCI数据专用模板
                List<BelTLV> FCIs = (List<BelTLV>) tlv.getValues();
                for (BelTLV sub : FCIs) {
                    flag = sub.getFlag();
                    if ("88".equals(flag)) {
                        // 目录基本文件的SFI
                        this.SFI = ((byte[]) sub.getValues())[0];
                    }
                }
            } else if ("84".equals(flag)) {
                // DF名称
                this.name = new String((byte[]) tlv.getValues());
            }
        }
    }

    public byte getSFI() {
        return SFI;
    }
}
