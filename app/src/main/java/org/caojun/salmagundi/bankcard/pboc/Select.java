package org.caojun.salmagundi.bankcard.pboc;

import org.caojun.salmagundi.bankcard.nfc.Transfer;
import org.caojun.salmagundi.utils.FormatUtils;
import java.io.IOException;

/**
 * Select命令
 * Created by CaoJun on 2016/8/26.
 */
public class Select {
    private byte[] header = FormatUtils.hexStringToByteArray("00A40400");

    /**
     * 选择DDF，根目录文件
     *
     * @param transfer
     * @return
     */
    public DDF selectDDF(Transfer transfer) throws Exception {
        // 构造C-APDU
        byte[] data = "1PAY.SYS.DDF01".getBytes();
        byte len = (byte) data.length;
        byte[] cmd = new byte[4 + 1 + len + 1];
        System.arraycopy(header, 0, cmd, 0, 4);
        cmd[4] = len;
        System.arraycopy(data, 0, cmd, 5, len);

        cmd[cmd.length - 1] = 0x00;
        // 如果传输发生错误直接终端交易流程
        Response res = null;
        try {
            res = transfer.transceive(cmd);
        } catch (IOException ex) {
            throw new Exception("TAG + "
                    + ".Select.selectDDF: transfer error", ex);
        }
        if ("9000".equals(res.getStatus())) {
            return new DDF(res.getData());
        } else {
            if ("6A81".equals(res.getStatus())) {
                // 卡被锁定或者选择（SELECT）命令不支持
                throw new Exception(
                        "卡被锁定或者选择（SELECT）命令不支持 4-card locked or command not support");
            } else if ("6A82".equals(res.getStatus())) {
                // IC卡上没有PSE
                // throw new Exception("文件没有找到PSE  no PSE in IC card");
                return null;
            } else if ("6A83".equals(res.getStatus())) {
                // PSE被锁定
                throw new Exception("PSE被锁定 5-PSE locked");
            } else {
                // 其他错误
                throw new Exception("其他错误 unknown error:" + res.getStatus());
            }
        }
    }

    /**
     * 选择应用ADF
     *
     * @param transfer
     * @param app
     * @return
     */
    public App selectADF(Transfer transfer, App app) throws Exception {
        // 构造C-APDU
        byte[] data = FormatUtils.hexStringToByteArray(app.getAid());
        byte len = (byte) data.length;
        byte[] cmd = new byte[4 + 1 + len + 1];
        System.arraycopy(header, 0, cmd, 0, 4);
        cmd[4] = len;
        System.arraycopy(data, 0, cmd, 5, len);
        cmd[cmd.length - 1] = 0x00;

        Response res = null;
        try {
            res = transfer.transceive(cmd);
        } catch (IOException ex) {
            throw new Exception("transfer error", ex);
        }
        if ("9000".equals(res.getStatus())) {
            app.select(res.getData());
            return app;
        } else {
            if ("6A81".equals(res.getStatus())) {
                // 应用被锁定
                throw new Exception("6-app locked");
            } else if ("6283".equals(res.getStatus())) {
                // 应用文件没有找到
                throw new Exception("app not found");
            } else if ("6A82".equals(res.getStatus())) {
                // throw new Exception("unknown error:" + res.getStatus());
                return null;
            } else {
                throw new Exception("unknown error:" + res.getStatus());
            }
        }
    }
}
