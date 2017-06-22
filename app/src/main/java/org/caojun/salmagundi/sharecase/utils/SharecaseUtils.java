package org.caojun.salmagundi.sharecase.utils;

import android.content.Context;
import org.caojun.salmagundi.sharecase.ormlite.Sharecase;
import org.caojun.salmagundi.sharecase.ormlite.SharecaseDatabase;
import org.caojun.salmagundi.sharecase.ormlite.User;
import java.util.List;

/**
 * Created by CaoJun on 2017/6/15.
 */

public class SharecaseUtils {

    /**
     * 只能由共享箱所有人新建箱子，并设定服务费
     * @param context
     * @param admin
     * @param commission
     * @return
     */
    public Sharecase insert(Context context, User admin, float commission) {
        if (context == null || admin == null || admin.getType() != User.Type_Admin || commission < 0) {
            return null;
        }
        Sharecase sharecase = new Sharecase(admin.getId(), commission);
        return SharecaseDatabase.getInstance(context).insert(sharecase);
    }

    public static Sharecase getSharecase(Context context, int id) {
        List<Sharecase> list = SharecaseDatabase.getInstance(context).query("id", id);
        if (list == null || list.isEmpty() || list.size() > 1) {
            return null;
        }
        return list.get(0);
    }

    public static Sharecase recycle(Context context, Sharecase sharecase) {
        if (context == null || sharecase == null) {
            return null;
        }
        sharecase.setName(null);
        sharecase.setRent(0);
        sharecase.setDeposit(0);
        sharecase.setIdHost(0);
        return SharecaseDatabase.getInstance(context).update(sharecase);
    }
}
