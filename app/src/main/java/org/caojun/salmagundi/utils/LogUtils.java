package org.caojun.salmagundi.utils;

import com.socks.library.KLog;

/**
 * 日志输出类
 * Created by CaoJun on 2016/10/27.
 */

public class LogUtils {
    public static enum LogType
    {
        System,//System.out.println
        Log,//Log
        None;//关闭日志输出
    }
    private static LogType Type = LogType.Log;

    public static void setLogType(LogType logType)
    {
        Type = logType;
    }

    public static void i(String tag, String msg) {
        switch(Type)
        {
            case System:
                System.out.println("i." + tag + " : " + msg);
                break;
            case Log:
                KLog.i(tag, msg);
                break;
            default:
                break;
        }
    }

    public static void d(String tag, String msg) {
        switch(Type)
        {
            case System:
                System.out.println("d." + tag + " : " + msg);
                break;
            case Log:
                KLog.d(tag, msg);
                break;
            default:
                break;
        }
    }

    public static void e(String tag, String msg) {
        switch(Type)
        {
            case System:
                System.out.println("e." + tag + " : " + msg);
                break;
            case Log:
                KLog.e(tag, msg);
                break;
            default:
                break;
        }
    }

    public static void v(String tag, String msg) {
        switch(Type)
        {
            case System:
                System.out.println("v." + tag + " : " + msg);
                break;
            case Log:
                KLog.v(tag, msg);
                break;
            default:
                break;
        }
    }

    public static void w(String tag, String msg) {
        switch(Type)
        {
            case System:
                System.out.println("w." + tag + " : " + msg);
                break;
            case Log:
                KLog.w(tag, msg);
                break;
            default:
                break;
        }
    }

    public static void i(String tag, Object msg) {
        i(tag, msg.toString());
    }

    public static void d(String tag, Object msg) {
        d(tag, msg.toString());
    }

    public static void e(String tag, Object msg) {
        e(tag, msg.toString());
    }

    public static void v(String tag, Object msg) {
        v(tag, msg.toString());
    }

    public static void w(String tag, Object msg) {
        w(tag, msg.toString());
    }
}
