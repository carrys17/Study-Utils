package com.example.admin.locationdemo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

/**
 * Created by admin on 2018/3/22.
 */
    public class AppStatusUtils {

        private static final String TAG = "AppStatusUtils";

        /**
         * 方法描述：判断某一应用是否正在运行
         *
         * @param context
         *            上下文
         * @param packageName
         *            应用的包名
         * @return true 表示正在运行，false表示没有运行
         */
        public static boolean isAppRunning(Context context, String packageName) {
            ActivityManager am = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
            if (list.size() <= 0) {
                return false;
            }
            for (ActivityManager.RunningTaskInfo info : list) {
                if (info.baseActivity.getPackageName().equals(packageName)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * 方法描述：判断某一Service是否正在运行
         *
         * @param context
         *            上下文
         * @param serviceName
         *            Service的全路径： 包名 + service的类名
         * @return true 表示正在运行，false 表示没有运行
         */
        public static boolean isServiceRunning(Context context, String serviceName) {
            ActivityManager am = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningServiceInfo> runningServiceInfos = am
                    .getRunningServices(200);
            if (runningServiceInfos.size() <= 0) {
                return false;
            }
            for (ActivityManager.RunningServiceInfo serviceInfo : runningServiceInfos) {
                if (serviceInfo.service.getClassName().equals(serviceName)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * 返回app运行状态 1:程序在前台运行 2:程序在后台运行 3:程序未启动 注意：需要配置权限<uses-permission
         * android:name="android.permission.GET_TASKS" />
         */
        public static int getAppSatus(Context context, String pageName) {

            ActivityManager am = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(20);

            // 判断程序是否在栈顶
            if (list.get(0).topActivity.getPackageName().equals(pageName)) {
                return 1;
            } else {
                // 判断程序是否在栈里
                for (ActivityManager.RunningTaskInfo info : list) {
                    if (info.topActivity.getPackageName().equals(pageName)) {
                        return 2;
                    }
                }
                return 3;// 栈里找不到，返回3
            }
        }

        // 是否是前台进程
        public static boolean isForeground(Context context, String packageName) {
            if (context != null) {
                ActivityManager activityManager = (ActivityManager) context
                        .getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningAppProcessInfo> processes = activityManager
                        .getRunningAppProcesses();
                for (ActivityManager.RunningAppProcessInfo processInfo : processes) {
                    if (processInfo.processName.equals(packageName)) {
                        if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        // 通过adb ps |grep来查看某个包名的运行状态 ,这个才是最好用的，前面的都判断不准,而且在android5.0之后就不能申请到GET_TASKS权限
        public static boolean isRunningByadb(String packageName) throws IOException {

            List<String> list = ShellUtil.getShellOut2(ShellUtil
                    .shell("ps |grep "+packageName));

            for (int i = 0; i < list.size(); i++) {
                String hang = list.get(i);
                String[] hangStr = hang.split(" ");
                int size = hangStr.length;
                // 判断是目标包名的那一行
                if (hangStr[size - 1].equals(packageName)) {
                    String status = hangStr[size - 2];
                    Log.i(TAG, "run: status -- " + status);
                    if ("R".equals(status)) {
                        Log.i(TAG, "run: 当前是运行状态");
                        return true;
                    } else if ("S".equals(status)) {
                        Log.i(TAG, "run: 当前是停止状态");
                        return false;
                    } else {
                        Log.i(TAG, "run: 当前状态未知 -- " + status);
                        return false;
                    }
                }
            }
            return false;
        }

    }


