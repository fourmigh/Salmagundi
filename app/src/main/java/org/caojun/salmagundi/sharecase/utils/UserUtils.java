package org.caojun.salmagundi.sharecase.utils;

import android.content.Context;
import android.text.TextUtils;
import org.caojun.salmagundi.sharecase.ormlite.Order;
import org.caojun.salmagundi.sharecase.ormlite.SerializedList;
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
        SerializedList<Integer> idSharecases = user.getIdSharecases();
        if (idSharecases == null || idSharecases.isEmpty()) {
            idSharecases = new SerializedList<>();
        }
        idSharecases.add(sharecase.getId());
        user.setIdSharecases(idSharecases);

        SerializedList<Integer> idOrders = user.getIdOrders();
        if (idOrders == null || idOrders.isEmpty()) {
            idOrders = new SerializedList<>();
        }
        idOrders.add(order.getId());
        user.setIdOrders(idOrders);
        return UserDatabase.getInstance(context).update(user);
    }

    public static boolean borrow(Context context, User user, Order order) {
        if (context == null || user == null || order == null) {
            return false;
        }
        User host = UserUtils.getUser(context, order.getIdHost());
        if (host == null) {
            return false;
        }
        SerializedList<Integer> idSharecasesHost = host.getIdSharecases();
        if (idSharecasesHost == null || idSharecasesHost.isEmpty()) {
            return false;
        }
        Sharecase sharecase = SharecaseUtils.getSharecase(context, order.getIdSharecase());
        if (sharecase == null) {
            return false;
        }
        User admin = UserUtils.getUser(context, sharecase.getIdAdmin());
        if (admin == null) {
            return false;
        }
        idSharecasesHost.remove(order.getIdSharecase());
        host.setIdSharecases(idSharecasesHost);

        SerializedList<Integer> idSharecasesUser = user.getIdSharecases();
        if (idSharecasesUser == null || idSharecasesUser.isEmpty()) {
            idSharecasesUser = new SerializedList<>();
        }
        idSharecasesUser.add(order.getIdSharecase());
        user.setIdSharecases(idSharecasesUser);
        float expendUser = user.getExpend() + order.getDeposit();
        user.setExpend(expendUser);

        float incomeAdmin = admin.getIncome() + order.getDeposit();
        admin.setIncome(incomeAdmin);

        int resHost = UserDatabase.getInstance(context).update(host);
        int resUser = UserDatabase.getInstance(context).update(user);
        int resAdmin = UserDatabase.getInstance(context).update(admin);
        if (resHost > 0 && resUser > 0 && resAdmin > 0) {
            return true;
        }
        return false;
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
        SerializedList<Integer> idSharecases = host.getIdSharecases();
        if (idSharecases == null || idSharecases.isEmpty()) {
            return -1;
        }
        SerializedList<Integer> idOrders = host.getIdOrders();
        if (idOrders == null || idOrders.isEmpty()) {
            return -1;
        }
        idSharecases.remove(sharecase.getId());
        host.setIdSharecases(idSharecases);
        idOrders.remove(order.getId());
        host.setIdOrders(idOrders);
        return UserDatabase.getInstance(context).update(host);
    }

    private static long getDay(long timeEnd, long timeStart) {
        long time = timeEnd - timeStart;
        return time / (1000 * 60 * 60 * 24);
    }

    /**
     * 归还物品时计算所有人的收支
     * @param context
     * @param order
     * @return
     */
    public static boolean restore(Context context, Order order) {
        if (context == null || order == null) {
            return false;
        }
        User host = UserUtils.getUser(context, order.getIdHost());
        if (host == null) {
            return false;
        }
        User user = UserUtils.getUser(context, order.getIdUser());
        if (user == null) {
            return false;
        }
        Sharecase sharecase = SharecaseUtils.getSharecase(context, order.getIdSharecase());
        if (sharecase == null) {
            return false;
        }
        User admin = UserUtils.getUser(context, sharecase.getIdAdmin());
        if (admin == null) {
            return false;
        }
        long day = getDay(order.getTimeEnd(), order.getTimeStart());
        float rent = order.getRent() * day;//总收入
        float commission = order.getCommission() * rent;//服务费
        float incomeHost = host.getIncome() + (rent - commission);//物品所有人收入
        float expendHost = host.getExpend() + commission;//物品所有人支出
        host.setIncome(incomeHost);
        host.setExpend(expendHost);
        float deposit = order.getDeposit();//押金
        float expendUser = user.getExpend() + rent;//物品使用人支出
        float incomeUser = user.getIncome() + deposit;//物品使用人收入
        user.setExpend(expendUser);
        user.setIncome(incomeUser);
        float expendAdmin = admin.getExpend() + deposit;//共享箱所有人支出
        float incomeAdmin = admin.getIncome() + commission;//共享箱所有人收入
        admin.setExpend(expendAdmin);
        admin.setIncome(incomeAdmin);
        int resHost = UserDatabase.getInstance(context).update(host);
        int resUser = UserDatabase.getInstance(context).update(user);
        int resAdmin = UserDatabase.getInstance(context).update(admin);
        if (resHost > 0 && resUser > 0 && resAdmin > 0) {
            return true;
        }
        return false;
    }
}
