package org.caojun.salmagundi.passwordstore.greendao;

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

    @Generated(hash = 66582281)
    public Password(Long id, String company, String url, byte type, byte length, String account, String password) {
        this.id = id;
        this.company = company;
        this.url = url;
        this.type = type;
        this.length = length;
        this.account = account;
        this.password = password;
    }

    @Generated(hash = 565943725)
    public Password() {
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
}
