package org.caojun.salmagundi.today.ormlite;

import android.os.Parcel;
import android.os.Parcelable;
import com.j256.ormlite.field.DatabaseField;
import java.io.Serializable;

/**
 * Created by CaoJun on 2017/7/10.
 */

public class History implements Serializable, Parcelable {

    @DatabaseField(generatedId = true)
    private Integer id;
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

    public History(Parcel in) {
        id = in.readInt();
        title = in.readString();
        year = in.readString();
        month = in.readString();
        day = in.readString();
        content = in.readString();
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
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(year);
        dest.writeString(month);
        dest.writeString(day);
        dest.writeString(content);
    }
}
