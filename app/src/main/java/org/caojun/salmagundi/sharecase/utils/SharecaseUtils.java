package org.caojun.salmagundi.sharecase.utils;

import android.content.Context;
import org.caojun.salmagundi.sharecase.ormlite.Order;
import org.caojun.salmagundi.sharecase.ormlite.Sharecase;
import org.caojun.salmagundi.sharecase.ormlite.SharecaseDatabase;
import org.caojun.salmagundi.sharecase.ormlite.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CaoJun on 2017/6/15.
 */

public class SharecaseUtils {

    /**
     * 只能由共享箱所有人新建箱子，并设定服务费
     * @param context
     * @param user
     * @param commission
     * @return
     */
    public int insert(Context context, User user, float commission) {
        if (context == null || user == null || user.getType() != User.Type_Admin || commission < 0) {
            return -1;
        }
        Sharecase sharecase = new Sharecase(commission);
        return SharecaseDatabase.getInstance(context).insert(sharecase);
    }

    public static Sharecase getSharecase(Context context, int id) {
        List<Sharecase> list = SharecaseDatabase.getInstance(context).query("id", id);
        if (list == null || list.isEmpty() || list.size() > 1) {
            return null;
        }
        return list.get(0);
    }

    private static int addIdOrders(Context context, Sharecase sharecase, int idOrder) {
        if (context == null || sharecase == null || idOrder < 0) {
            return -1;
        }
        List<Integer> idOrders = sharecase.getIdOrders();
        if (idOrders == null || idOrders.isEmpty()) {
            idOrders = new ArrayList<>();
        }
        idOrders.add(idOrder);
        sharecase.setIdOrders(idOrders);
        return SharecaseDatabase.getInstance(context).update(sharecase);
    }

    public static int loan(Context context, Sharecase sharecase, Order order) {
        if (context == null || sharecase == null || !sharecase.isEmpty() || order == null) {
            return -1;
        }
        sharecase.setName(order.getName());
        sharecase.setRent(order.getRent());
        sharecase.setDeposit(order.getDeposit());
        sharecase.setIdHost(order.getIdHost());
        return addIdOrders(context, sharecase, order.getId());
    }

    public static int borrow(Context context, Sharecase sharecase) {
        if (context == null || sharecase == null || sharecase.isEmpty()) {
            return -1;
        }
        sharecase.setName(null);
        sharecase.setRent(0);
        sharecase.setDeposit(0);
        sharecase.setIdHost(-1);
        sharecase.setIdOrder(-1);
        return SharecaseDatabase.getInstance(context).update(sharecase);
    }

    public static int recycle(Context context, Order order) {
        if (context == null || order == null || order.isBorrowing()) {
            return -1;
        }
        Sharecase sharecase = SharecaseUtils.getSharecase(context, order.getIdSharecase());
        if (sharecase == null || sharecase.isEmpty()) {
            return -1;
        }
        return borrow(context, sharecase);
    }
}
