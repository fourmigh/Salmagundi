package org.caojun.salmagundi.sharecase.utils;

import android.content.Context;
import android.text.TextUtils;
import org.caojun.salmagundi.sharecase.ormlite.Order;
import org.caojun.salmagundi.sharecase.ormlite.Sharecase;
import org.caojun.salmagundi.sharecase.ormlite.User;
import org.caojun.salmagundi.sharecase.ormlite.UserDatabase;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CaoJun on 2017/6/15.
 */

public class UserUtils {

    /**
     * 新增的第一个用户为共享箱所有人
     * @param context
     * @param name
     * @param gesturePassword
     * @return
     */
    public static int insertAdmin(Context context, String name, byte[] gesturePassword) {
        User user = new User(User.Type_Admin, name, gesturePassword);
        return UserDatabase.getInstance(context).insert(user);
    }

    /**
     * 新增用户
     * @param context
     * @param name
     * @param gesturePassword
     * @return
     */
    public static int insertUser(Context context, String name, byte[] gesturePassword) {
        User user = new User(User.Type_User, name, gesturePassword);
        return UserDatabase.getInstance(context).insert(user);
    }

    /**
     * 更新收入
     * @param context
     * @param user
     * @param income 增减值
     * @return
     */
    public static int updateIncome(Context context, User user, float income) {
        if (context == null || user == null || income == 0) {
            return -1;
        }
        float oldIncome = user.getIncome();
        float newIncom = oldIncome + income;
        user.setIncome(newIncom);
        return UserDatabase.getInstance(context).update(user);
    }

    /**
     * 更新支出
     * @param context
     * @param user
     * @param expend
     * @return
     */
    public static int updateExpend(Context context, User user, float expend) {
        if (context == null || user == null || expend == 0) {
            return -1;
        }
        float oldExpend = user.getExpend();
        float newExpend = oldExpend + expend;
        user.setExpend(newExpend);
        return UserDatabase.getInstance(context).update(user);
    }

    /**
     * 更新账号
     * @param context
     * @param user
     * @param name
     * @return
     */
    public static int updateName(Context context, User user, String name) {
        if (context == null || user == null || TextUtils.isEmpty(name)) {
            return -1;
        }
        user.setName(name);
        return UserDatabase.getInstance(context).update(user);
    }

    /**
     * 更新手势密码
     * @param context
     * @param user
     * @param gesturePassword
     * @return
     */
    public static int updateGesturePassword(Context context, User user, byte[] gesturePassword) {
        if (context == null || user == null || gesturePassword == null) {
            return -1;
        }
        user.setGesturePassword(gesturePassword);
        return UserDatabase.getInstance(context).update(user);
    }

    public static User getUser(Context context, int id) {
        List<User> list = UserDatabase.getInstance(context).query("id", id);
        if (list == null || list.isEmpty() || list.size() > 1) {
            return null;
        }
        return list.get(0);
    }

    public static int loan(Context context, User user, Sharecase sharecase, Order order) {
        if (context == null || user == null || sharecase == null || order == null) {
            return -1;
        }
        List<Integer> idSharecases = user.getIdSharecases();
        if (idSharecases == null || idSharecases.isEmpty()) {
            idSharecases = new ArrayList<>();
        }
        idSharecases.add(sharecase.getId());
        user.setIdSharecases(idSharecases);

        List<Integer> idOrders = user.getIdOrders();
        if (idOrders == null || idOrders.isEmpty()) {
            idOrders = new ArrayList<>();
        }
        idOrders.add(order.getId());
        user.setIdOrders(idOrders);
        return UserDatabase.getInstance(context).update(user);
    }

    public static int borrowHost(Context context, User host, Order order) {
        if (context == null || host == null || order == null) {
            return -1;
        }
        List<Integer> idSharecases = host.getIdSharecases();
        if (idSharecases == null || idSharecases.isEmpty()) {
            return -1;
        }
        idSharecases.remove(order.getIdSharecase());
        host.setIdSharecases(idSharecases);
        return UserDatabase.getInstance(context).update(host);
    }

    public static int borrowUser(Context context, User user, Order order) {
        if (context == null || user == null || order == null) {
            return -1;
        }
        List<Integer> idSharecases = user.getIdSharecases();
        if (idSharecases == null || idSharecases.isEmpty()) {
            idSharecases = new ArrayList<>();
        }
        idSharecases.add(order.getIdSharecase());
        user.setIdSharecases(idSharecases);
        return UserDatabase.getInstance(context).update(user);
    }

    public static int recycle(Context context, Order order) {
        if (context == null || order == null || order.isBorrowing()) {
            return -1;
        }
        Sharecase sharecase = SharecaseUtils.getSharecase(context, order.getIdSharecase());
        if (sharecase == null) {
            return -1;
        }
        User host = UserUtils.getUser(context, order.getIdHost());
        if (host == null) {
            return -1;
        }
        List<Integer> idSharecases = host.getIdSharecases();
        if (idSharecases == null || idSharecases.isEmpty()) {
            return -1;
        }
        List<Integer> idOrders = host.getIdOrders();
        if (idOrders == null || idOrders.isEmpty()) {
            return -1;
        }
        idSharecases.remove(sharecase.getId());
        host.setIdSharecases(idSharecases);
        idOrders.remove(order.getId());
        host.setIdOrders(idOrders);
        return UserDatabase.getInstance(context).update(host);
    }

    /**
     * 归还物品时计算物品所有人收支
     * @param context
     * @param host
     * @param order
     * @return
     */
    public static int restoreHost(Context context, User host, Order order) {

    }

    /**
     * 归还物品时计算物品使用人收支
     * @param context
     * @param user
     * @param order
     * @return
     */
    public static int restoreUser(Context context, User user, Order order) {

    }

    /**
     * 归还物品时计算共享箱所有人收支
     * @param context
     * @param admin
     * @param order
     * @return
     */
    public static int restoreAdmin(Context context, User admin, Order order) {

    }
}
