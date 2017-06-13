package org.caojun.salmagundi.passwordstore.ormlite;

import android.os.Parcel;
import android.os.Parcelable;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.io.Serializable;

/**
 * 密码仓库实体类
 * Created by CaoJun on 2017/5/2.
 */

@DatabaseTable
public class Password implements Serializable, Parcelable {

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

    public Password(Parcel in) {
        id = in.readInt();
        company = in.readString();
        url = in.readString();
        type = in.readByte();
        length = in.readByte();
        account = in.readString();
        password = in.readString();
        realLength = in.readByte();
    }

    public static final Creator<Password> CREATOR = new Creator<Password>() {
        @Override
        public Password createFromParcel(Parcel in) {
            return new Password(in);
        }

        @Override
        public Password[] newArray(int size) {
            return new Password[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(company);
        dest.writeString(url);
        dest.writeByte(type);
        dest.writeByte(length);
        dest.writeString(account);
        dest.writeString(password);
        dest.writeByte(realLength);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
