package org.caojun.bloodpressure.ormlite;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * Created by CaoJun on 2017/7/10.
 */

public class History implements Serializable, Parcelable {

    @DatabaseField(id = true)
    private String id;
    @DatabaseField
    private String title;
    @DatabaseField
    private String year;
    @DatabaseField
    private String month;
    @DatabaseField
    private String day;
    @DatabaseField
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public History(Parcel in) {
        id = in.readString();
        title = in.readString();
        year = in.readString();
        month = in.readString();
        day = in.readString();
        content = in.readString();
    }

    public History() {}

    public History(String title, String year, String month, String day, String content) {
        setId(title + year + month + day);
        setTitle(title);
        setYear(year);
        setMonth(month);
        setDay(day);
        setContent(content);
    }

    public static final Creator<History> CREATOR = new Creator<History>() {
        @Override
        public History createFromParcel(Parcel in) {
            return new History(in);
        }

        @Override
        public History[] newArray(int size) {
            return new History[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(year);
        dest.writeString(month);
        dest.writeString(day);
        dest.writeString(content);
    }
}
