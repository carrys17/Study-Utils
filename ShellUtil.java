package com.example.admin.locationdemo;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/3/22.
 */

public class ShellUtil {

    public static Process shell(String command) {
        return process("adb shell " + command);
    }

    private static Process process(String command) {
        Process ps = null;
        try {
            Log.i("adb", command);
            ps = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ps;
    }

    public static BufferedReader shellOut(Process ps) {
        BufferedInputStream in = new BufferedInputStream(ps.getInputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        return br;
    }

    public static List<String> getShellOut2(Process ps) {
        List<String> list = new ArrayList<>();
        BufferedReader br = shellOut(ps);
        String line;

        try {
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }
}
