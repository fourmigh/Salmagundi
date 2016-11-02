package org.caojun.salmagundi.bankcard.nfc;

import android.nfc.tech.IsoDep;

import org.caojun.salmagundi.bankcard.pboc.BelTLV;
import org.caojun.salmagundi.bankcard.pboc.Record;
import org.caojun.salmagundi.bankcard.pboc.Response;
import org.caojun.salmagundi.utils.FormatUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * ISO 14443-4传输协议层实现类
 * @author CaoJun
 *
 */
public class IsoTransfer implements Transfer {
    private IsoDep iso;
    public static int cmdIndex = 1;//指令计数器

    public IsoTransfer(IsoDep iso)
    {
        this.iso = iso;
        this.iso.setTimeout(2000);
    }

    @Override
    public Response transceive(byte[] cmd) throws IOException
    {
        if (!this.isConnect())
        {
            throw new IOException("tag out of range");
        }
        try
        {
            byte[] response = this.iso.transceive(cmd);
            Response res = new Response();
            //提取应答码，BCD码
            byte[] status = new byte[] {response[response.length - 2],response[response.length - 1]};
            res.setStatus(FormatUtils.byteArrayToHexString(status));
            if (response.length > 2)
            {
                //如果返回结果有数据部分，则将数据部分解析为BelTLV对象
                byte[] data = new byte[response.length - 2];
                System.arraycopy(response, 0, data, 0, data.length);
                Record record = new Record();
                if(record.IsStartsWith(cmd))
                {
                    res.setRecord(record.parse(data));
                }
                else
                {
                    ByteArrayInputStream in = new ByteArrayInputStream(data);
                    BelTLV tlv = BelTLV.parse(in);
                    res.setData(tlv);
                }
            }
            return res;
        }
        catch (IOException ex)
        {
            throw ex;
        }
    }

    @Override
    public void connect() throws IOException
    {
        this.iso.connect();
    }

    @Override
    public void close() throws IOException {
        this.iso.close();
    }

    @Override
    public boolean isConnect() {
        return this.iso.isConnected();
    }

}
