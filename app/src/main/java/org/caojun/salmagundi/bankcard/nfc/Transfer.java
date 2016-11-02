package org.caojun.salmagundi.bankcard.nfc;

import org.caojun.salmagundi.bankcard.pboc.Response;

import java.io.IOException;

/**
 * 传输层协议接口
 * @author CaoJun
 *
 */
public interface Transfer {

    /**
     * 发送命令，接受以TLV格式的数据返回
     * @param cmd
     * @return
     * @throws IOException
     */
    public Response transceive(byte[] cmd) throws IOException;

    /**
     * 是否连接物理链路
     * @return
     */
    public boolean isConnect();

    /**
     * 与物理设备建立连接
     * @throws IOException
     */
    public void connect() throws IOException;

    /**
     * 与物理设备关闭连接
     * @throws IOException
     */
    public void close() throws IOException;
}
