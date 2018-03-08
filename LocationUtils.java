package com.example.admin.locationdemo;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;


/**
 * Created by admin on 2018/3/5.
 */

public class LocationUtils {

    private static final String TAG = "LocationUtils";

    /**
     * http://ip-api.com/json/58.192.32.1?fields=520191&lang=en
     * 根据ip获取位置信息
     *
     * @param ip
     * @return {"accuracy":50,"as":"AS4538 China Education and Research Network Center",
     * "city":"Nanjing","country":"China","countryCode":"CN","isp":
     * "China Education and Research Network Center","lat":32.0617,"lon":118.7778,"mobile":false,
     * "org":"China Education and Research Network Center","proxy":false,"query":"58.192.32.1",
     * "region":"JS","regionName":"Jiangsu","status":"success","timezone":"Asia/Shanghai","zip":""}
     */
    public static JSONObject Ip2Location(String ip) {
        JSONObject jsonObject = null;

        String urlStr = "http://ip-api.com/json/" + ip + "?fields=520191&lang=en";
        try {
            URL url = new URL(urlStr);
            HttpURLConnection  urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(5000);//读取超时
            urlConnection.setConnectTimeout(5000); // 连接超时
            urlConnection.setDoInput(true);
            urlConnection.setUseCaches(false);
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                //找到服务器的情况下,可能还会找到别的网站返回html格式的数据
                InputStream is = urlConnection.getInputStream();

                BufferedReader buff = new BufferedReader(new InputStreamReader(is, "UTF-8"));//注意编码，会出现乱码
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ((line = buff.readLine()) != null) {
                    builder.append(line);
                }
                buff.close();//内部会关闭InputStream
                urlConnection.disconnect();

                String res = builder.toString();

                Log.i(TAG, "Ip2Location: res -- "+res);
                if (StringUtils.isJSONString(res)){
                    jsonObject = new JSONObject(res);

                }
            }


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    /**
     * 根据ip通过百度api去获取城市
     * @param ip
     * @return
     */
    public static String Ip2LocationByBaiduApi(String ip){
        try {
            URL url = new URL("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=" + ip);
            URLConnection connection = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));
            String line = null;
            StringBuffer res = new StringBuffer();
            while ((line = reader.readLine())!=null){
                res.append(line);
            }
            reader.close();
            String ipAddr = res.toString();
            if (StringUtils.isJSONString(ipAddr)){
                JSONObject jsonObject = new JSONObject(ipAddr);
                if ("1".equals(jsonObject.get("ret").toString())){
                    return jsonObject.get("city").toString();
                }else {
                    return "读取失败";
                }

            }else {
                return "访问后得到的不是json数据, res -- "+ipAddr;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "读取失败 e -- "+e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return "读取失败 e -- "+e.getMessage();
        } catch (JSONException e) {
            e.printStackTrace();
            return "读取失败 e -- "+e.getMessage();
        }
    }


 	/**
         * 计算两个坐标点之间的距离
         *
         * @param firstLatitude   第一个坐标的纬度
         * @param firstLongitude  第一个坐标的经度
         * @param secondLatitude  第二个坐标的纬度
         * @param secondLongitude 第二个坐标的经度
         * @return 返回两点之间的距离，单位：m
         */
        public static double getDistance(double firstLatitude, double firstLongitude,
                                         double secondLatitude, double secondLongitude) {
            double firstRadLat = rad(firstLatitude);
            double firstRadLng = rad(firstLongitude);
            double secondRadLat = rad(secondLatitude);
            double secondRadLng = rad(secondLongitude);

            double a = firstRadLat - secondRadLat;
            double b = firstRadLng - secondRadLng;
            double cal = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(firstRadLat)
                    * Math.cos(secondRadLat) * Math.pow(Math.sin(b / 2), 2))) * EarthRadius;
            double result = Math.round(cal * 10000d) * 1000d / 10000d ;
            return result;
        }
}