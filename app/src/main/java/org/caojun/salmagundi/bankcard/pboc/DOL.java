package org.caojun.salmagundi.bankcard.pboc;

import org.caojun.salmagundi.utils.FormatUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * DOL对象定义，表示卡片需要的属性和对应的长度
 * Created by CaoJun on 2016/8/26.
 */
public class DOL {
    private List<String> flags = new ArrayList<String>();
    private List<Integer> lens = new ArrayList<Integer>();

    public DOL(byte[] data) {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        while (true) {
            String flag = this._parseDOLTag(in);
            if (flag == null) {
                break;
            }
            int len = in.read();
            if (len == -1) {
                break;
            }
            this.flags.add(flag);
            this.lens.add(len);

        }
    }

    public List<String> getFlags() {
        return flags;
    }

    public List<Integer> getLens() {
        return lens;
    }

    /**
     * 根据flag标签查找对应的数据字段长度
     *
     * @param flag
     * @return
     */
    public int getLen(String flag) {
        for (int i = 0; i < this.flags.size(); i++) {
            if (this.flags.get(i).equals(flag)) {
                return this.lens.get(i);
            }
        }
        return 0;
    }

    /**
     * 查找所有字段的长度
     *
     * @return
     */
    public int getTotalLen() {
        int len = 0;
        for (Integer l : this.lens) {
            len += l;
        }
        return len;
    }

    /**
     * 解析tag
     *
     * @param in
     * @return
     */
    private String _parseDOLTag(ByteArrayInputStream in) {
        int data = in.read();
        if (data == -1) {
            return null;
        }
        if ((data & 0x1F) == 0x1F) {
            // 多字节tag
            ByteArrayOutputStream tmpOut = new ByteArrayOutputStream();
            tmpOut.write(data);
            while (true) {
                data = in.read();
                if (data == -1) {
                    return null;
                }
                tmpOut.write(data);
                if ((data & 0x80) == 0) {
                    // 最后一个flag的bit8是0
                    break;
                }
            }
            return FormatUtils.byteArrayToHexString(tmpOut.toByteArray());
        } else {
            // 单字节tag
            return FormatUtils.byteArrayToHexString(new byte[] { (byte) data });
        }
    }
}
