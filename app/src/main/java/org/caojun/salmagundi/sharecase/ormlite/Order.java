package org.caojun.salmagundi.sharecase.ormlite;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * 订单
 * Created by CaoJun on 2017/6/14.
 */

public class Order implements Serializable, Parcelable {

    public Order(int idSharecase, int idHost, String name, float rent, float deposit, float commission) {
        this.setIdSharecase(idSharecase);
        this.setIdHost(idHost);
        this.setName(name);
        this.setRent(rent);
        this.setDeposit(deposit);
        this.setCommission(commission);
        this.setIdUser(-1);
        this.setTimeStart(0);
        this.setTimeEnd(0);
    }

    /**
     * 物品正在租借中
     * @return
     */
    public boolean isBorrowing() {
        if (timeStart == 0 && timeEnd == 0 && idUser == -1 && idSharecase >= 0 && idHost >= 0) {
            return true;
        }
        return false;
    }

    @DatabaseField(generatedId = true)
    private Integer id;
    @DatabaseField
    private Integer idSharecase;//共享箱ID
    @DatabaseField
    private Integer idHost;//物品所有人ID
    @DatabaseField
    private Integer idUser;//物品使用人ID
    @DatabaseField
    private String name;//箱内物品名称（物品主人设置）
    @DatabaseField
    private float rent;//箱内物品租金（归物品主人所有，按天计算）
    @DatabaseField
    private float deposit;//箱内物品押金（归共享箱平台所有，归还物品时返还）
    @DatabaseField
    private float commission;//服务费（归共享箱平台所有，向物品主人收取，相当于收入所得税，收入的百分比）
    @DatabaseField
    private long timeStart;//物品租用起始时间
    @DatabaseField
    private long timeEnd;//物品租用结束时间（物品归还时，计算租金、服务费，归还押金）

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdSharecase() {
        return idSharecase;
    }

    public void setIdSharecase(Integer idSharecase) {
        this.idSharecase = idSharecase;
    }

    public Integer getIdHost() {
        return idHost;
    }

    public void setIdHost(Integer idHost) {
        this.idHost = idHost;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRent() {
        return rent;
    }

    public void setRent(float rent) {
        this.rent = rent;
    }

    public float getDeposit() {
        return deposit;
    }

    public void setDeposit(float deposit) {
        this.deposit = deposit;
    }

    public float getCommission() {
        return commission;
    }

    public void setCommission(float commission) {
        this.commission = commission;
    }

    public long getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(long timeStart) {
        this.timeStart = timeStart;
    }

    public long getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(long timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Order(Parcel in) {
        id = in.readInt();
        idSharecase = in.readInt();
        idHost = in.readInt();
        idUser = in.readInt();
        name = in.readString();
        rent = in.readFloat();
        deposit = in.readFloat();
        commission = in.readFloat();
        timeStart = in.readLong();
        timeEnd = in.readLong();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(idSharecase);
        dest.writeInt(idHost);
        dest.writeInt(idUser);
        dest.writeString(name);
        dest.writeFloat(rent);
        dest.writeFloat(deposit);
        dest.writeFloat(commission);
        dest.writeLong(timeStart);
        dest.writeLong(timeEnd);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
