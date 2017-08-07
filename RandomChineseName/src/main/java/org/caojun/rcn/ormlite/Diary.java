package org.caojun.rcn.ormlite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by CaoJun on 2017/8/7.
 */

@DatabaseTable
public class Diary implements Serializable {

    @DatabaseField(id = true)
    private String day;//yyyyMMdd
    @DatabaseField
    private byte cntName;//取名次数

    public Diary() {}

    public Diary(String day, byte cntName) {
        setDay(day);
        setCntName(cntName);
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public byte getCntName() {
        return cntName;
    }

    public void setCntName(byte cntName) {
        this.cntName = cntName;
    }
}
