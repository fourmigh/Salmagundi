package org.caojun.salmagundi.passwordstore.greendao;

import org.caojun.salmagundi.secure.DESede;
import org.caojun.salmagundi.string.ConvertUtils;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import java.io.Serializable;

/**
 * 密码仓库实体类
 * Created by CaoJun on 2017/2/15.
 */

@Entity
public class Password implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id(autoincrement = true)
    private Long id;
    private String company;//网站/银行
    private String url;//网址
    private byte type;//密码类型：0数字，1字母+数字+符号
    private byte length;//密码长度
    private String account;//账号
    private String password;//密码
    private byte realLength;//密码实际长度

    /**
     * 获取加密密钥
     * @return
     */
    private static String getKey(String company, String url, byte type, byte length, String account) {
        String key = company + url + type + account;
        StringBuffer k = new StringBuffer();
        for (int i = 0;i < key.length();i ++) {
            char c = key.charAt(i);
            if ((c >= 0 && c <= 9) || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                k.append(c);
            }
        }
        StringBuffer sb = new StringBuffer(24);
        int index = 0;
        while (sb.length() != 24) {
            if (index >= k.length()) {
                index %= k.length();
            }
            char c = k.charAt(index);
            sb.append(c);
            index += length;
        }
        return sb.toString();
    }

    /**
     * 加密密码
     */
    public static String getEncodePassword(String company, String url, byte type, byte length, String account, String password) {
        byte[] input = password.getBytes();
        byte[] output = DESede.encrypt(getKey(company, url, type, length, account), input);
        return ConvertUtils.toBase64(output);
    }

    /**
     * 获取明文密码
     */
    public String getDecodePassword() {
        byte[] input = ConvertUtils.base64ToBytes(password);
        byte[] output = DESede.decrypt(getKey(company, url, type, length, account), input);
        return new String(output);
    }

    public Password() {
    }

    @Generated(hash = 735068223)
    public Password(Long id, String company, String url, byte type, byte length, String account, String password,
            byte realLength) {
        this.id = id;
        this.company = company;
        this.url = url;
        this.type = type;
        this.length = length;
        this.account = account;
        this.password = password;
        this.realLength = realLength;
    }

    public byte getRealLength() {
        return realLength;
    }

    public Long getId() {
        return id;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public void setLength(byte length) {
        this.length = length;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCompany() {
        return company;
    }

    public String getUrl() {
        return url;
    }

    public byte getType() {
        return type;
    }

    public byte getLength() {
        return length;
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRealLength(byte realLength) {
        this.realLength = realLength;
    }
}
