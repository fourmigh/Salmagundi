package org.caojun.salmagundi.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Package相关的工具类
 * Created by CaoJun on 2016/10/27.
 */

public class PackageUtils {

    public static PackageInfo getPackageInfo(Context context, String packageName)
    {
        if(context == null)
        {
            return null;
        }
        PackageManager pManager = context.getPackageManager();
        try {
            return pManager.getPackageInfo(packageName, 0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    /**
//     * 会报android.os.TransactionTooLargeException异常，以下方法均不建议使用
//     * @param context
//     * @param flags
//     * @return
//     */
//    public static List<PackageInfo> getInstalledPackages(Context context, int flags)
//    {
//        if(context == null)
//        {
//            return null;
//        }
//        PackageManager pManager = context.getPackageManager();
//        return pManager.getInstalledPackages(flags);
//    }
//
//    public static List<PackageInfo> getInstalledActivities(Context context)
//    {
//        return getInstalledPackages(context, PackageManager.GET_ACTIVITIES);
//    }
//
//    public static List<PackageInfo> getInstalledSystemActivities(Context context)
//    {
//        List<PackageInfo> packageInfos = getInstalledActivities(context);
//        if(packageInfos == null || packageInfos.isEmpty())
//        {
//            return null;
//        }
//        List<PackageInfo> list = new ArrayList<>();
//        for(int i = 0;i < packageInfos.size();i ++)
//        {
//            PackageInfo info = packageInfos.get(i);
//            if((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0)
//            {
//                list.add(info);
//            }
//        }
//        return list;
//    }
//
//    public static PackageInfo getInstalledSystemActivityByPackageName(Context context, String packageName)
//    {
//        if(TextUtils.isEmpty(packageName))
//        {
//            return null;
//        }
//        List<PackageInfo> packageInfos = getInstalledActivities(context);
//        if(packageInfos == null || packageInfos.isEmpty())
//        {
//            return null;
//        }
//        for(int i = 0;i < packageInfos.size();i ++)
//        {
//            PackageInfo info = packageInfos.get(i);
//            if(packageName.equals(info.applicationInfo.packageName))
//            {
//                return info;
//            }
//        }
//        return null;
//    }
//
//    public static PackageInfo getInstalledSystemActivityByLabel(Context context, String label)
//    {
//        if(TextUtils.isEmpty(label))
//        {
//            return null;
//        }
//        List<PackageInfo> packageInfos = getInstalledSystemActivities(context);
//        if(packageInfos == null || packageInfos.isEmpty())
//        {
//            return null;
//        }
//        for(int i = 0;i < packageInfos.size();i ++)
//        {
//            PackageInfo info = packageInfos.get(i);
//            String infoLabel = context.getPackageManager().getApplicationLabel(info.applicationInfo).toString();
//            LogUtils.d("infoLabel", i + " : " + infoLabel);
//            if(label.equals(infoLabel))
//            {
//                return info;
//            }
//        }
//        return null;
//    }
}
