package org.caojun.salmagundi.sharecase.utils;

import android.content.Context;
import android.text.TextUtils;

import org.caojun.salmagundi.sharecase.ormlite.Order;
import org.caojun.salmagundi.sharecase.ormlite.OrderDatabase;
import org.caojun.salmagundi.sharecase.ormlite.Sharecase;
import org.caojun.salmagundi.sharecase.ormlite.User;

import java.util.List;

/**
 * Created by CaoJun on 2017/6/15.
 */

public class OrderUtils {

    /**
     * 共享箱空闲时才能放入物品，生成订单
     * @param context
     * @param sharecase
     * @param host
     * @param name
     * @param rent
     * @param deposit
     * @return
     */
    public static int loan(Context context, Sharecase sharecase, User host, String name, float rent, float deposit) {
        if (context == null || sharecase == null || !sharecase.isEmpty() || host == null || TextUtils.isEmpty(name) || rent < 0 || deposit < 0) {
            return -1;
        }
        Order order = new Order(sharecase.getId(), host.getId(), name, rent, deposit, sharecase.getCommission());
        return OrderDatabase.getInstance(context).insert(order);
    }

    /**
     * 共享箱有物品时，租借物品并更新订单
     * @param context
     * @param order
     * @param sharecase
     * @param user
     * @param timeStart
     * @return
     */
    public int borrow(Context context, Order order, Sharecase sharecase, User user, int timeStart) {
        if (context == null || order == null || sharecase == null || sharecase.isEmpty() || user == null || timeStart <= 0) {
            return -1;
        }
        order.setIdUser(user.getId());
        order.setTimeStart(timeStart);
        return OrderDatabase.getInstance(context).update(order);
    }

    /**
     * 共享箱空闲时才能放入物品，归还物品时更新订单
     * @param context
     * @param order
     * @param sharecase
     * @param timeEnd
     * @return
     */
    public int restore(Context context, Order order, Sharecase sharecase, long timeEnd) {
        if (context == null || order == null || sharecase == null || !sharecase.isEmpty() || timeEnd <= 0) {
            return -1;
        }
        User host = UserUtils.getUser(context, order.getIdHost());
        if (host == null) {
            return -1;
        }
        User user = UserUtils.getUser(context, order.getIdUser());
        if (user == null) {
            return -1;
        }
        Sharecase oldSharecase = SharecaseUtils.getSharecase(context, order.getIdSharecase());
        if (oldSharecase == null) {
            return -1;
        }
        order.setIdSharecase(sharecase.getId());//将物品放入共享箱sharecase
        order.setTimeEnd(timeEnd);//记录归还时间
        return OrderDatabase.getInstance(context).update(order);
    }

    public static Order getOrder(Context context, int id) {
        List<Order> list = OrderDatabase.getInstance(context).query("id", id);
        if (list == null || list.isEmpty() || list.size() > 1) {
            return null;
        }
        return list.get(0);
    }

    /**
     * 归还物品后，重新开始出租
     * @param context
     * @param order
     * @return
     */
    public static int reloan(Context context, Order order) {
        if (context == null || order == null) {
            return -1;
        }
        Sharecase sharecase = SharecaseUtils.getSharecase(context, order.getIdSharecase());
        if (sharecase == null || !sharecase.isEmpty()) {
            return -1;
        }
        User host = UserUtils.getUser(context, order.getIdHost());
        if (host == null) {
            return -1;
        }
        String name = order.getName();
        float rent = order.getRent();
        float deposit = order.getDeposit();
        return loan(context, sharecase, host, name, rent, deposit);
    }

    public static int recycle(Context context, Order order) {
        return OrderDatabase.getInstance(context).delete(order);
    }
}
