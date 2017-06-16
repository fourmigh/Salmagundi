package org.caojun.salmagundi.sharecase.ormlite;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;
import java.util.List;

/**
 * 用户（包括共享箱所有人、使用人）
 * Created by CaoJun on 2017/6/14.
 */

public class User implements Serializable, Parcelable {
    public static final byte Type_Admin = 0;//共享箱所有人
    public static final byte Type_User = 1;//共享箱使用人（包括物品主人、租借物品人）

    public User(byte type, String name, byte[] gesturePassword) {
        this.setType(type);
        this.setName(name);
        this.setGesturePassword(gesturePassword);
    }

    @DatabaseField(generatedId = true)
    private Integer id;
    @DatabaseField
    private byte type;//类型（共享箱所有人、共享箱使用人）
    @DatabaseField
    private float income;//收入
    @DatabaseField
    private float expend;//支出
    @DatabaseField
    private String name;//账号
    @DatabaseField
    private byte[] gesturePassword;
    @DatabaseField
    private List<Integer> idSharecases;//相关共享箱ID
    @DatabaseField
    private List<Integer> idOrders;//相关订单ID

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public float getIncome() {
        return income;
    }

    public void setIncome(float income) {
        this.income = income;
    }

    public float getExpend() {
        return expend;
    }

    public void setExpend(float expend) {
        this.expend = expend;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getGesturePassword() {
        return gesturePassword;
    }

    public void setGesturePassword(byte[] gesturePassword) {
        this.gesturePassword = gesturePassword;
    }

    public List<Integer> getIdSharecases() {
        return idSharecases;
    }

    public void setIdSharecases(List<Integer> idSharecases) {
        this.idSharecases = idSharecases;
    }

    public List<Integer> getIdOrders() {
        return idOrders;
    }

    public void setIdOrders(List<Integer> idOrders) {
        this.idOrders = idOrders;
    }

    public User(Parcel in) {
        id = in.readInt();
        type = in.readByte();
        income = in.readFloat();
        expend = in.readFloat();
        name = in.readString();
        int lengthGesturePassword = in.readInt();
        if (lengthGesturePassword > 0) {
            gesturePassword = new byte[lengthGesturePassword];
            in.readByteArray(gesturePassword);
        }
        idSharecases = in.readArrayList(Integer.class.getClassLoader());
        idOrders = in.readArrayList(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeByte(type);
        dest.writeFloat(income);
        dest.writeFloat(expend);
        dest.writeString(name);
        int lengthGesturePassword = gesturePassword == null?0:gesturePassword.length;
        dest.writeInt(lengthGesturePassword);
        if (lengthGesturePassword > 0) {
            dest.writeByteArray(gesturePassword);
        }
        dest.writeList(idSharecases);
        dest.writeList(idOrders);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
