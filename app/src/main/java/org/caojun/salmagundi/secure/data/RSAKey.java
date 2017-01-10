package org.caojun.salmagundi.secure.data;

import android.provider.BaseColumns;

/**
 * RSA公私钥数据表
 * Created by CaoJun on 2017/1/6.
 */

public class RSAKey implements BaseColumns {
    public static final String TABLE_NAME = "rsakey_table";
    public static final String Private_Key = "Private_Key";
    public static final String Public_Key = "Public_Key";
}
