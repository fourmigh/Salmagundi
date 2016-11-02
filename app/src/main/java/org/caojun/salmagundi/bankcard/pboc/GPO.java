package org.caojun.salmagundi.bankcard.pboc;

import org.caojun.salmagundi.bankcard.nfc.Transfer;
import org.caojun.salmagundi.utils.FormatUtils;
import java.io.IOException;
import java.util.List;

/**
 * Get Processing Options命令
 * Created by CaoJun on 2016/8/26.
 */
public class GPO {
    private byte[] header = FormatUtils.hexStringToByteArray("80A80000");

    /**
     * 获取交易特性
     *
     * @param datas
     */
    public Object getOption(Transfer transfer, byte[] datas)
            throws Exception {

        byte[] cmd = new byte[4 + 1 + 2 + datas.length + 1];
        System.arraycopy(header, 0, cmd, 0, 4);
        cmd[4] = (byte) (2 + datas.length);
        cmd[5] = (byte) 0x83;
        cmd[6] = (byte) datas.length;
        System.arraycopy(datas, 0, cmd, 7, datas.length);
        cmd[cmd.length - 1] = 0x00;
        Response res = null;
        try {
            res = transfer.transceive(cmd);
        } catch (IOException ex) {
            throw new Exception("GPO.getOption: transfer error", ex);
        }
        if ("9000".equals(res.getStatus())) {
            TradeOption opt = new TradeOption();

            Object values = res.getData().getValues();
            if(!(values instanceof byte[]))
            {
                List<BelTLV> listBelTLV = (List<BelTLV>) values;

                Card card = new Card();
                for(int i = 0;i < listBelTLV.size();i ++)
                {
                    BelTLV beltlv = listBelTLV.get(i);
                    card.setCardInfo(beltlv);

                    if ("57".equals(beltlv.getFlag())) {
                        // 磁道2
                        String track2 = FormatUtils.byteArrayToHexString((byte[]) beltlv.getValues());
                        // 过滤一下右填充的F
                        int index = track2.indexOf("D");
                        if (index >= 0) {
                            track2 = track2.substring(0, index);
                        }
                        card.setPan(track2);
                    }
                }
                return new Object[]{card, listBelTLV};
            }
            else
            {
                // 返回的数据2字节的AIP+4字节一组的AFL
                byte[] d = (byte[]) values;
                byte[] aip = new byte[] { d[0], d[1] };
                opt.setAIP(aip);
                byte[] afl = new byte[d.length - 2];
                System.arraycopy(d, 2, afl, 0, afl.length);
                opt.setAFL(afl);
                return opt;
            }
        } else {
            if ("6985".equals(res.getStatus())) {
                // 交易不被支持
                throw new Exception("GPO.getOption: trade not support");
            } else {
                // 其他错误
                throw new Exception(
                        "GPO.getOption: getOption unknown error:"
                                + res.getStatus());
            }
        }
    }
}
