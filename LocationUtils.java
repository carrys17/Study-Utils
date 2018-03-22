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
     * 根据ip通过百度地图api去获取城市
     * @param ip
     * @return
     */
    public static String Ip2LocationByBaiduApi(String ip) {
        try {
            URL url = new URL("http://api.map.baidu.com/location/ip?ak=F454f8a5efe5e577997931cc01de3974&ip=" + ip);
            URLConnection conn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line = null;
            StringBuffer result = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
            String ipAddr = result.toString();
            try {
                JSONObject obj1 = new JSONObject(ipAddr);
                if ("0".equals(obj1.get("status").toString())) {
                    JSONObject obj2 = new JSONObject(obj1.get("content").toString());
                    JSONObject obj3 = new JSONObject(obj2.get("address_detail").toString());
                    return obj3.get("city").toString();
                } else {
                    return "读取失败";
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return "读取失败";
            }

        } catch (IOException e) {
            return "读取失败";
        }
    }


    //http://pv.sohu.com/cityjson?ie=utf-8&ip=113.111.245.219
    // var returnCitySN = {"cip": "113.111.245.219", "cid": "440100", "cname": "广东省广州市"};
    public static String Ip2LocationBySohu(String ip) {
        try {
            URL url = new URL("http://pv.sohu.com/cityjson?ie=utf-8&ip=" + ip);
            URLConnection conn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line = null;
            StringBuffer result = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
            String ipAddr = result.toString();
            int left = ipAddr.indexOf("省");
            int right = ipAddr.indexOf("市");
            String city = ipAddr.substring(left + 1, right+1);
            return city;

        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            return "读取失败";
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
            return "读取失败";
        } catch (IOException e1) {
            e1.printStackTrace();
            return "读取失败";
        }
    }

    // /**/jQuery110206955700272228569_1433922418817({"status":"0","t":"1433922612109","set_cache_time":"","data":[{"location":"广东省广州市 联通", "titlecont":"IP地址查询", "origip":"112.96.199.156", "origipquery":"112.96.199.156", "showlamp":"1", "showLikeShare":1, "shareImage":1, "ExtendedLocation":"", "OriginQuery":"112.96.199.156", "tplt":"ip", "resourceid":"6006", "fetchkey":"112.96.199.156", "appinfo":"", "role_id":0, "disp_type":0}]});
    public static String Ip2LocationByBaiduWeb(String ip) {
        try {
            // https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php?query=112.96.199.156&co=&resource_id=6006&t=1433922612109&ie=utf8&oe=gbk&cb=op_aladdin_callback&format=json&tn=baidu&cb=jQuery110206955700272228569_1433922418817&_=1433922418822

            // 简化后的https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php?query=123.123.192.255&co=&resource_id=6006&t=1433922612109&ie=utf8&oe=utf-8&format=json&tn=baidu
            // 需要ip和时间戳
            long time = System.currentTimeMillis();
            URL url = new URL("https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php?query=" + ip
            +"&co=&resource_id=6006&t="+time+"&ie=utf8&oe=utf-8&format=json&tn=baidu");
            URLConnection conn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line = null;
            StringBuffer result = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            reader.close();
            String ipAddr = result.toString();
            ipAddr = new String(ipAddr.getBytes(),"utf-8");
            Log.i("location", "Ip2LocationByBaiduWeb: result -- "+ipAddr);
            int left = ipAddr.indexOf("省");
            int right = ipAddr.indexOf("市");
            String city = ipAddr.substring(left + 1, right+1);// 广州市
            return city;

        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            return "读取失败";
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
            return "读取失败";
        } catch (IOException e1) {
            e1.printStackTrace();
            return "读取失败";
        }
    }




}