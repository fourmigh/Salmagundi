package org.caojun.salmagundi.bankcard.pboc;

import org.caojun.salmagundi.bankcard.nfc.Transfer;
import org.caojun.salmagundi.utils.FormatUtils;
import java.io.IOException;

/**
 * Get Data命令
 * Created by CaoJun on 2016/8/26.
 */
public class GetData {
    /**
     * 获得标签对应的值
     * @param transfer
     * @param Record
     * @return
     * @throws Exception
     */
    public Record getTradeReadRecord(Transfer transfer, String Record)
            throws Exception {
        // 构造C-APDU
        byte[] cmd = FormatUtils.hexStringToByteArray("00B2" + Record + "5C00");
        Response res = null;
        try {
            res = transfer.transceive(cmd);
        } catch (IOException ex) {
            throw new Exception(ex.getMessage(), ex);
        }

        if ("9000".equals(res.getStatus())) {
            return res.getRecord(); // (byte[]) res.getData().getValues();
        } else {
            return null;
            /**
             * if ("6A83".equals(res.getStatus())) { // 命令中请求的数据是专有数据不能返回
             * throw new Exception("no data found"); } else { // 其他错误 throw
             * new Exception("getTradeReadRecord unknown error:" +
             * res.getStatus()); }
             **/
        }
    }

    public byte[] getData(Transfer transfer, String p1p2) throws Exception {
        // 构造C-APDU
        byte[] cmd = FormatUtils.hexStringToByteArray("80CA" + p1p2 + "00");

        Response res = null;
        try {
            res = transfer.transceive(cmd);
        } catch (IOException ex) {
            throw new Exception(ex.getMessage(), ex);
        }

        if ("9000".equals(res.getStatus())) {
            return (byte[]) res.getData().getValues();
        } else {
            if ("6A88".equals(res.getStatus())) {
                // 命令中请求的数据是专有数据不能返回
                throw new Exception("GetData.getData: no data found");
            } else {
                // CommonUtils.OutputLog("错误信息getData：", res.getStatus());
                // 其他错误
                throw new Exception(
                        "GetData.getData: getData unknown error:"
                                + res.getStatus());
            }
        }
    }
}
