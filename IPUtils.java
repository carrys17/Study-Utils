package com.example.admin.locationdemo;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.Context.WIFI_SERVICE;


/**
 * Created by admin on 2018/3/2.
 */

public class IPUtils {
    private static final String TAG = "IPUtils";

    private static String[] platforms = {
            "http://pv.sohu.com/cityjson?ie=utf-8",
            "http://ip.chinaz.com/getip.aspx",
            "http://pv.sohu.com/cityjson"
    };

    public static String getOutNetIP(Context context, int index) {
        if (index < platforms.length) {
            BufferedReader buff = null;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(platforms[index]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(5000);//读取超时
                urlConnection.setConnectTimeout(5000); // 连接超时
                urlConnection.setDoInput(true);
                urlConnection.setUseCaches(false);
                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    //找到服务器的情况下,可能还会找到别的网站返回html格式的数据
                    InputStream is = urlConnection.getInputStream();
                    Log.i(TAG, "getOutNetIP: index -- "+index);


                    buff = new BufferedReader(new InputStreamReader(is, "UTF-8"));//注意编码，会出现乱码
                    StringBuilder builder = new StringBuilder();
                    String line = null;
                    while ((line = buff.readLine()) != null) {
                        builder.append(line);
                    }
                    buff.close();//内部会关闭InputStream
                    urlConnection.disconnect();
                    Log.i(TAG, builder.toString());
                    if (index == 0 || index == 1) {
                        //截取字符串
                        int startIndex = builder.indexOf("{");//包含{
                        int endIndex = builder.indexOf("}");//包含}
                        String json = builder.substring(startIndex, endIndex + 1);//包含[startIndex,endIndex)
                        Log.i(TAG, "getOutNetIP: json -- "+json);
                        JSONObject jo = new JSONObject(json);
                        String ip = jo.getString("cip");
                        Log.i(TAG, "getOutNetIP: 外网ip -- "+ip);
                        return ip;
                    } else if (index == 2) {
                        JSONObject jo = new JSONObject(builder.toString());
                        Log.i(TAG, "getOutNetIP: 外网ip -- "+jo.getString("ip"));
                        return jo.getString("ip");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(TAG,"出错 e -- "+e.getMessage());
            }
        } else {
            return getInNetIp(context);
        }
        return getOutNetIP(context, ++index);
    }

    private static String getInNetIp(Context context) {
        // 这种拿到的只是局域网ip
        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = formatIpAddress(ipAddress);
        Log.i(TAG, "局域网 ip -- "+ip);
        return null;
    }

    private static String formatIpAddress(int ipAddress) {
        return (ipAddress & 0xFF) + "."+
                ((ipAddress>>8) & 0xFF) + "."+
                ((ipAddress>>16) & 0xFF) + "."+
                ((ipAddress>>24) & 0xFF) ;
    }

}