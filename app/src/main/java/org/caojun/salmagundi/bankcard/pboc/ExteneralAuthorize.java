package org.caojun.salmagundi.bankcard.pboc;

import org.caojun.salmagundi.bankcard.nfc.Transfer;
import org.caojun.salmagundi.utils.FormatUtils;
import java.io.IOException;

/**
 * 外部认证命令
 * Created by CaoJun on 2016/8/26.
 */
public class ExteneralAuthorize {
    private String header = "00820000";

    /**
     * 外部数据验证
     *
     * @param transfer
     * @param datas
     * @return
     */
    public boolean authorize(Transfer transfer, byte[] datas) {
        // 构造C-APDU
        byte[] cmd = new byte[5 + datas.length];
        System.arraycopy(FormatUtils.hexStringToByteArray(this.header), 0, cmd,
                0, 4);
        cmd[4] = (byte) datas.length;
        System.arraycopy(datas, 0, cmd, 5, datas.length);
        Response res = null;
        try {
            res = transfer.transceive(cmd);
        } catch (IOException ex) {
            return false;
        }

        if ("9000".equals(res.getStatus())) {
            return true;
        } else {
            if ("6300".equals(res.getStatus())) {
                // 验证失败
                return false;
            } else if ("6985".equals(res.getStatus())) {
                // 已经收到过验证数据
                return false;
            } else {
                // 其他错误
                return false;
            }
        }
    }
}
