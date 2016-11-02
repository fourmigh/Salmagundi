package org.caojun.salmagundi.bankcard.pboc;

import org.caojun.salmagundi.bankcard.nfc.Transfer;
import java.io.IOException;

/**
 * Generate AC命令
 * Created by CaoJun on 2016/8/26.
 */
public class GenerateAC {
    /**
     * 获得应用明文
     *
     * @param transfer
     * @param ACtype
     * @param datas
     */
    public AC getAC(Transfer transfer, String ACtype, byte[] datas)
            throws Exception {
        if(datas == null)
        {
            return null;
        }
        // 构造C-APDU
        byte[] cmd = new byte[6 + datas.length];
        cmd[0] = (byte) 0x80;
        cmd[1] = (byte) 0xAE;
        if ("AAC".equals(ACtype)) {
            cmd[2] = 0x00;
        } else if ("TC".equals(ACtype)) {
            cmd[2] = 0x40;
        } else if ("ARQC".equals(ACtype)) {
            cmd[2] = (byte) 0x80;
        }
        cmd[3] = 0x00;
        cmd[4] = (byte) datas.length;
        System.arraycopy(datas, 0, cmd, 5, datas.length);
        cmd[cmd.length - 1] = 0x00;
        Response res = null;
        try {
            res = transfer.transceive(cmd);
        } catch (IOException ex) {
            throw new Exception("getAC transfer error", ex);
        }
        if ("9000".equals(res.getStatus())) {
            AC a = new AC();
            BelTLV tlv = res.getData();
            byte[] response = (byte[]) tlv.getValues();
            // 设置密文数据
            a.setCID(response[0]);
            // 设置atc
            a.setAtc(((response[1] & 0x000000FF) << 8)
                    | (response[2] & 0x000000FF));
            // 8字节的应用密文
            byte[] ac = new byte[8];
            System.arraycopy(response, 3, ac, 0, 8);
            // 设置密文
            a.setAc(ac);
            if (response.length > 11) {
                byte[] bank = new byte[response.length - 11];
                System.arraycopy(response, 11, bank, 0,
                        response.length - 11);
                // 设置发卡行应用数据
                a.setBankDatas(bank);
            }
            return a;
        } else {
            // 其他错误
            throw new Exception("getAC unknown error:" + res.getStatus());
        }
    }
}
