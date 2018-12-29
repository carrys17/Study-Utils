package com.example.shang.dagger2demo;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
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

    public static BufferedReader shellOut(Process ps) {
        BufferedInputStream in = new BufferedInputStream(ps.getInputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        return br;
    }

    public static String getShellOut(Process ps) {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = shellOut(ps);
        String line;

        try {
            while ((line = br.readLine()) != null) {
                // sb.append(line);
                sb.append(line + System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString().trim();
    }

    // 返回的是List类型
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

    /**
     * 执行su命令，最多重复执行5次
     */
    public static String performSuCommandAndGetRes(String cmd)
            throws InterruptedException {
        int repeatTimes = 5;
        for (int i = 0; i < repeatTimes; i++) {
            String result = "";
            DataOutputStream dos = null;
            DataInputStream dis = null;
            Process p = null;
            try {
                p = Runtime.getRuntime().exec("su");
                dos = new DataOutputStream(p.getOutputStream());
                dis = new DataInputStream(p.getInputStream());
                dos.writeBytes(cmd + "; echo \"suCmdRes=\"$?\n");
                dos.flush();
                dos.writeBytes("exit\n");
                dos.flush();

                String line = "";
                BufferedReader din = new BufferedReader(new InputStreamReader(
                        dis));
                while ((line = din.readLine()) != null) {
                    result += line;
                }
                p.waitFor();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (dos != null) {
                    try {
                        dos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (dis != null) {
                    try {
                        dis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (p != null) {
                    try {
                        p.destroy();
                    } catch (Exception e2) {
                    }
                }
            }

            Log.i("sudo", cmd + " = " + result);
            if (result != null && result.contains("suCmdRes") == true) {
                for (int j = -5; j < 5; j++) {
                    result = result.replace("suCmdRes=" + j, "");
                }
                return result;
            } else {
                if (i == repeatTimes - 1)
                    break;
                Thread.sleep(1500);
            }
        }

        return null;
    }


    /**
     * 执行su命令，最多重复执行5次
     */
    public static boolean performSuCommand(String cmd)
            throws InterruptedException {
        int repeatTimes = 5;
        for (int i = 0; i < repeatTimes; i++) {
            String result = "";
            DataOutputStream dos = null;
            DataInputStream dis = null;
            Process p = null;
            try {
                p = Runtime.getRuntime().exec("su");
                dos = new DataOutputStream(p.getOutputStream());
                dis = new DataInputStream(p.getInputStream());
                dos.writeBytes(cmd + "; echo \"suCmdRes=\"$?\n");
                dos.flush();
                dos.writeBytes("exit\n");
                dos.flush();

                String line = "";
                BufferedReader din = new BufferedReader(new InputStreamReader(
                        dis));
                while ((line = din.readLine()) != null) {
                    result += line;
                }
                p.waitFor();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (dos != null) {
                    try {
                        dos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (dis != null) {
                    try {
                        dis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (p != null) {
                    try {
                        p.destroy();
                    } catch (Exception e2) {
                    }
                }
            }

            Log.i("sudo", cmd + " = " + result);
            if (result != null && result.contains("suCmdRes=0") == true) {
                return true;
            } else {
                if (i == repeatTimes - 1)
                    break;
                Thread.sleep(1500);
            }
        }

        return false;
    }
}
