package org.caojun.salmagundi.sharecase.utils;

import android.content.Context;
import android.text.TextUtils;

import com.socks.library.KLog;

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
     * @return
     */
    public static Order loan(Context context, Sharecase sharecase, int idHost, String name, float rent, float deposit, int idUser) {
        if (context == null || sharecase == null || idHost <= 0 || TextUtils.isEmpty(name) || rent < 0 || deposit < 0 && idUser <= 0) {
            KLog.d("loan", "1");
            return null;
        }
        Order order = new Order(sharecase.getId(), idHost, name, rent, deposit, sharecase.getCommission(), idUser);
        KLog.d("loan", "2");
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
    public Order borrow(Context context, Order order, Sharecase sharecase, User user, int timeStart) {
        if (context == null || order == null || sharecase == null || user == null || timeStart <= 0) {
            return null;
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
    public Order restore(Context context, Order order, Sharecase sharecase, long timeEnd) {
        if (context == null || order == null || sharecase == null || timeEnd <= 0) {
            return null;
        }
        User host = UserUtils.getUser(context, order.getIdHost());
        if (host == null) {
            return null;
        }
        User user = UserUtils.getUser(context, order.getIdUser());
        if (user == null) {
            return null;
        }
        Sharecase oldSharecase = SharecaseUtils.getSharecase(context, order.getIdSharecase());
        if (oldSharecase == null) {
            return null;
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
    public static Order reloan(Context context, Order order) {
        if (context == null || order == null) {
            return null;
        }
        Sharecase sharecase = SharecaseUtils.getSharecase(context, order.getIdSharecase());
        if (sharecase == null) {
            return null;
        }
        User host = UserUtils.getUser(context, order.getIdHost());
        if (host == null) {
            return null;
        }
        String name = order.getName();
        float rent = order.getRent();
        float deposit = order.getDeposit();
        return loan(context, sharecase, host.getId(), name, rent, deposit, 0);
    }

    public static boolean recycle(Context context, Order order) {
        return OrderDatabase.getInstance(context).delete(order);
    }
}
