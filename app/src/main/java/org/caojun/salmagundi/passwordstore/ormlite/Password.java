package org.caojun.salmagundi.passwordstore.ormlite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.caojun.salmagundi.secure.DESede;
import org.caojun.salmagundi.string.ConvertUtils;
import java.io.Serializable;

/**
 * 密码仓库实体类
 * Created by CaoJun on 2017/5/2.
 */

@DatabaseTable
public class Password implements Serializable {

    @DatabaseField(generatedId = true)
    private Integer id;
    @DatabaseField
    private String company;//网站/银行
    @DatabaseField
    private String url;//网址
    @DatabaseField
    private byte type;//密码类型：0数字，1字母+数字+符号
    @DatabaseField
    private byte length;//密码长度
    @DatabaseField
    private String account;//账号
    @DatabaseField
    private String password;//密码
    @DatabaseField
    private byte realLength;//密码实际长度

    public Password() {}

    public Password(Integer id, String company, String url, byte type, byte length, String account, String password,
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

    public Integer getId() {
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

    public void setId(Integer id) {
        this.id = id;
    }

    public void setRealLength(byte realLength) {
        this.realLength = realLength;
    }
}
