package org.caojun.salmagundi.bankcard.pboc;

import org.caojun.salmagundi.bankcard.nfc.Transfer;
import java.io.IOException;

/**
 * Read Record命令
 * Created by CaoJun on 2016/8/26.
 */
public class Reader {
    /**
     * 读取文件的一行记录
     *
     * @param transfer
     * @param sfi
     * @param index
     * @return
     */
    public BelTLV readRecord(Transfer transfer, byte sfi, int index)
            throws Exception {
        // 构造C-APDU
        byte[] cmd = new byte[5];
        cmd[0] = 0x00;
        cmd[1] = (byte) 0xB2;
        cmd[2] = (byte) index;
        cmd[3] = (byte) (sfi << 3 | 0x04);
        cmd[4] = 0x00;
        Response res = null;
        try {
            res = transfer.transceive(cmd);
        } catch (IOException ex) {
            throw new Exception("transfer error", ex);
        }
        if ("9000".equals(res.getStatus())) {
            return res.getData();
        } else {
            if ("6A83".equals(res.getStatus())) {
                // 读到最后一条记录
                return null;
            } else {
                // 其他错误
                throw new Exception("readRecord unknown error:"
                        + res.getStatus());
            }
        }
    }
}
