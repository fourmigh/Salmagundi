package org.caojun.salmagundi.bankcard.pboc;

import org.caojun.salmagundi.utils.FormatUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * BEL-TLV数据对象
 * @author CaoJun
 *
 */
public class BelTLV {

    // 控制域
    private String flag;
    // 是否为复合对象
    private boolean constructed;
    // 数据对象
    private Object values;
    private boolean valid = false;

    private static final byte CONSTRUCT_FLG = 0x20;
    private static final byte MUTIPLE_FLAG = 0x1F;
    private static final byte MUTIPLE_FLAG_END_FLAG = (byte) 0x80;
    private static final byte MUTIPLE_LEN_FLAG = (byte) 0x80;
    private static final byte MUTIPLE_SIZE_FLAG = 0x0F;

    public BelTLV()
    {

    }

    public BelTLV(String flag, boolean constructed, Object values)
    {
        this.constructed = constructed;
        this.flag = flag;
        this.values = values;
    }

    public byte[] toBytes()
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            out.write(FormatUtils.hexStringToByteArray(this.flag));
            if (this.constructed) {
                ByteArrayOutputStream out2 = new ByteArrayOutputStream();
                List<BelTLV> subs = (List<BelTLV>) this.values;
                for (BelTLV tlv : subs) {
                    out2.write(tlv.toBytes());
                }
                out2.close();
                byte[] datas = out2.toByteArray();
                out.write(datas.length);
                out.write(datas);
                return out.toByteArray();
            } else {
                byte[] datas = (byte[]) this.values;
                out.write(datas.length);
                out.write(datas);
                return out.toByteArray();
            }
        } catch (Exception ex) {
            return null;
        } finally{
            try{
                out.close();
            }catch(Exception ex){}
        }

    }

    /**
     * 解析BEL-TLV
     *
     * @param in
     * @return
     */
    public static BelTLV parse(ByteArrayInputStream in)
    {
        int d = in.read();
        if (d == -1)
        {
            // 没有正常读取完整就结束
            return null;
        }
        BelTLV tlv = new BelTLV();
        // 1.读取flag
        byte flag = (byte) d;

        if ((flag & CONSTRUCT_FLG) == CONSTRUCT_FLG)
        {
            // 数据对象为{{T,L,V},{T,L,V}...}结构
            tlv.setConstructed(true);
        } else
        {
            // 普通的数据对象
            tlv.setConstructed(false);
        }

        if ((flag & MUTIPLE_FLAG) == MUTIPLE_FLAG)
        {
            //多字节flag域
            ByteArrayOutputStream tmpOut = new ByteArrayOutputStream();
            try
            {
                tmpOut.write(new byte[] { flag });
            } catch (IOException ex)
            {
                return null;
            }
            while (true)
            {
                d = in.read();
                if (d == -1)
                {
                    // 没有正常读取完整就结束
                    return null;
                }
                byte flagSequence = (byte) d;
                try
                {
                    tmpOut.write(new byte[] { flagSequence });
                } catch (IOException ex)
                {
                    return null;
                }
                if ((flagSequence & MUTIPLE_FLAG_END_FLAG) == 0)
                {
                    // 最后一个flag的bit8是0
                    break;
                }
            }
            tlv.setFlag(FormatUtils.byteArrayToHexString(tmpOut.toByteArray()));
            //CommonUtils.OutputLog("FLAG-1", Utils.byteArrayToHexString(tmpOut.toByteArray()));
        }
        else
        {
            // 单字节flag域
            tlv.setFlag(FormatUtils.byteArrayToHexString(new byte[] { flag }));
            //CommonUtils.OutputLog("FLAG-2",Utils.byteArrayToHexString(new byte[] { flag }));
        }

        // 2.读取length
        d = in.read();
        if (d == -1)
        {
            return null;
        }
        byte l = (byte) d;
        int length = 0;
        if ((l & MUTIPLE_LEN_FLAG) == 0)
        {
            // 单字节长度域
            length = (int) l;
        }
        else
        {
            // 多字节长度域
            int size = l & MUTIPLE_SIZE_FLAG;
            for (int i = 0; i < size; i++)
            {
                d = in.read();
                if (d == -1)
                {
                    return null;
                }
                length = (length << 8) + d;
            }
        }
        // 3.读取value
        byte[] buf = new byte[length];
        try
        {
            int re = in.read(buf);
            if (re == -1)
            {
                return null;
            }
        } catch (IOException ex)
        {
            return null;
        }

        if (!tlv.isConstructed())
        {
            // 简单tlv的值保存为字节流
            tlv.setValues(buf);
        }
        else
        {
            // 复合tlv递归调用解析
            ByteArrayInputStream subIn = new ByteArrayInputStream(buf);
            List<BelTLV> subTlvs = new ArrayList<BelTLV>();
            while (true)
            {
                BelTLV subTlv = parse(subIn);
                if (subTlv == null)
                {
                    break;
                }
                subTlvs.add(subTlv);
            }
            tlv.setValues(subTlvs);
        }
        tlv.setValid(true);
        return tlv;
    }
    public String getFlag()
    {
        return flag;
    }

    public void setFlag(String flag)
    {
        if(flag.equals("9C"))
        {
            this.flag = flag;
        }
        this.flag = flag;
    }
    public boolean isConstructed()
    {
        return constructed;
    }
    public void setConstructed(boolean constructed)
    {
        this.constructed = constructed;
    }
    public Object getValues()
    {
        return values;
    }
    public void setValues(Object values)
    {
        this.values = values;
    }
    public boolean isValid()
    {
        return valid;
    }
    public void setValid(boolean valid)
    {
        this.valid = valid;
    }
    public String toString()
    {
        StringBuilder buf = new StringBuilder();
        buf.append("flag:").append(this.flag).append("\r\n");
        if (!this.constructed) {
            buf.append("value:")
                    .append(FormatUtils.byteArrayToHexString((byte[]) this.values))
                    .append("\r\n");
        } else {
            List<BelTLV> subs = (List<BelTLV>) this.values;
            for (int i = 0; i < subs.size(); i++) {
                buf.append(subs.get(i));
            }
        }
        return buf.toString();
    }

}
