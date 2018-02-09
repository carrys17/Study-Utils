package com.example.admin.testget;

import com.google.zxing.WriterException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by admin on 2018/2/7.
 */

public class OutboardHttpUtils {



    public static List<String> doPostLogin() throws IOException {
//        POST /Center/login.html?flag=hidden HTTP/1.1
//        Host: vip.zaza666.com
//        Connection: keep-alive
//        Content-Length: 54
//        Accept: application/json, text/javascript, */*; q=0.01
//Origin: http://vip.zaza666.com
//X-Requested-With: XMLHttpRequest
//User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36
//Content-Type: application/x-www-form-urlencoded; charset=UTF-8
//Referer: http://vip.zaza666.com/Center/login.html?flag=hidden
//Accept-Encoding: gzip, deflate
//Accept-Language: zh-CN,zh;q=0.9
//Cookie: PHPSESSID=h0fsg1q9ku2hjkg4mk355t2693; __root_domain_v=.zaza666.com; _qddaz=QD.dbc22d.fnqdzy.jdcf70ai; Hm_lvt_4e9fea0594abd91080de592dbeb2afff=1517968403,1517986954; _qdda=3-1.2ju1k1; _qddab=3-1ci9vb.jdcweneg; _qddamta_2852155138=3-0; Hm_lpvt_4e9fea0594abd91080de592dbeb2afff=1517997317
//
//telephone=yanghaovip&password=KIfj15%40gg&action=login

        List<String> list = new ArrayList<>();
        String  url = "http://vip.zaza666.com/Center/login.html?flag=hidden";
        String params = "telephone=yanghaovip&password=KIfj15%40gg&action=login";
        String res = "";

        URL realUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
        connection.setRequestProperty("Connection","Keep-Alive");
        connection.setRequestProperty("Content-Length","54");
        connection.setRequestProperty("Accept","application/json, text/javascript, */*; q=0.01");
        connection.setRequestProperty("Origin","http://vip.zaza666.com");
        connection.setRequestProperty("X-Requested-With","XMLHttpRequest");
        connection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36 Edge/14.14393");
        connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
        connection.setRequestProperty("Referer","http://vip.zaza666.com/Center/login.html?flag=hidden");
        connection.setRequestProperty("Accept-Encoding","gzip,deflate");
        connection.setRequestProperty("Accept-Language","zh-CN,zh;q=0.9");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        PrintWriter pw = new PrintWriter(connection.getOutputStream());
        pw.write(params);
        pw.flush();


        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));
        String line;
        while ((line = br.readLine())!= null){
            res += "\n"+line;
        }

        String s = connection.getHeaderField("Set-Cookie");
        list.add(res);
        list.add(s);

        return list;
    }

    public static String doPostPicFirst(String cookie) throws IOException {

//        POST /Center/tuoguan?vtype=2 HTTP/1.1
//        Host: vip.come666.com
//        Connection: keep-alive
//        Content-Length: 26
//        Accept: application/json, text/javascript, */*; q=0.01
//Origin: http://vip.come666.com
//X-Requested-With: XMLHttpRequest
//User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36
//Content-Type: application/x-www-form-urlencoded; charset=UTF-8
//Referer: http://vip.come666.com/Center/tuoguan
//Accept-Encoding: gzip, deflate
//Accept-Language: zh-CN,zh;q=0.9
//Cookie: PHPSESSID=0n3djas770pdf8f2mipspf6e23; __root_domain_v=.come666.com; _qddaz=QD.l5nxdj.z63gbw.jdbe5ash; Hm_lvt_4e9fea0594abd91080de592dbeb2afff=1517906178,1517966718; _qdda=3-1.2vko6y; _qddab=3-roy5ty.jdcoojyo; _qddamta_2852155138=3-0; Hm_lpvt_4e9fea0594abd91080de592dbeb2afff=1517985396
//
//action=qrcode&xiaohao_id=0

        String  url = "http://vip.zaza666.com/Center/tuoguan?vtype=2";
        String params = "action=qrcode&xiaohao_id=0";
        String res = "";

        URL realUrl = new URL(url);
        URLConnection connection = realUrl.openConnection();
        connection.setRequestProperty("Connection","Keep-Alive");
        connection.setRequestProperty("Content-Length","26");
        connection.setRequestProperty("Accept","application/json, text/javascript, */*; q=0.01");
        connection.setRequestProperty("Origin","http://vip.zaza666.com");
        connection.setRequestProperty("X-Requested-With","XMLHttpRequest");
        connection.setRequestProperty("Cookie",cookie);
        connection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36 Edge/14.14393");
        connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
        connection.setRequestProperty("Referer","http://vip.zaza666.com/Center/tuoguan");
        connection.setRequestProperty("Accept-Encoding","gzip,deflate");
        connection.setRequestProperty("Accept-Language","zh-CN,zh;q=0.9");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        PrintWriter pw = new PrintWriter(connection.getOutputStream());
        pw.write(params);
        pw.flush();

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));
        String line;
        while ((line = br.readLine())!= null){
            res += "\n"+line;
        }


        return res;
    }

    public static String doPostPicSecond(String cookie,int id) throws IOException {

//        POST /Center/tuoguan?vtype=2 HTTP/1.1
//        Host: vip.come666.com
//        Connection: keep-alive
//        Content-Length: 31
//        Accept: application/json, text/javascript, */*; q=0.01
//Origin: http://vip.come666.com
//X-Requested-With: XMLHttpRequest
//User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36
//Content-Type: application/x-www-form-urlencoded; charset=UTF-8
//Referer: http://vip.come666.com/Center/tuoguan
//Accept-Encoding: gzip, deflate
//Accept-Language: zh-CN,zh;q=0.9
//Cookie: PHPSESSID=0n3djas770pdf8f2mipspf6e23; __root_domain_v=.come666.com; _qddaz=QD.l5nxdj.z63gbw.jdbe5ash; Hm_lvt_4e9fea0594abd91080de592dbeb2afff=1517906178,1517966718; _qdda=3-1.2vko6y; _qddab=3-roy5ty.jdcoojyo; _qddamta_2852155138=3-0; Hm_lpvt_4e9fea0594abd91080de592dbeb2afff=1517985396
//
//action=qrcode&xiaohao_id=768009

        String  url = "http://vip.zaza666.com/Center/tuoguan?vtype=2";
        String params = "action=qrcode&xiaohao_id="+id;
        String res = "";

        URL realUrl = new URL(url);
        URLConnection connection = realUrl.openConnection();
        connection.setRequestProperty("Connection","Keep-Alive");
        connection.setRequestProperty("Content-Length","31");
        connection.setRequestProperty("Accept","application/json, text/javascript, */*; q=0.01");
        connection.setRequestProperty("Origin","http://vip.zaza666.com");
        connection.setRequestProperty("X-Requested-With","XMLHttpRequest");
        connection.setRequestProperty("Cookie",cookie);
        connection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36 Edge/14.14393");
        connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
        connection.setRequestProperty("Referer","http://vip.zaza666.com/Center/tuoguan");
        connection.setRequestProperty("Accept-Encoding","gzip,deflate");
        connection.setRequestProperty("Accept-Language","zh-CN,zh;q=0.9");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        PrintWriter pw = new PrintWriter(connection.getOutputStream());
        pw.write(params);
        pw.flush();

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));
        String line;
        while ((line = br.readLine())!= null){
            res += "\n"+line;
        }


        return res;
    }

    public static String  doPostTuoguan(String cookie,int id) throws IOException {
//        Center/tuoguan?vtype=2 HTTP/1.1
//        Host: vip.zaza666.com
//        Connection: keep-alive
//        Content-Length: 32
//        Accept: application/json, text/javascript, */*; q=0.01
//Origin: http://vip.zaza666.com
//X-Requested-With: XMLHttpRequest
//User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36
//Content-Type: application/x-www-form-urlencoded; charset=UTF-8
//Referer: http://vip.zaza666.com/Center/tuoguan
//Accept-Encoding: gzip, deflate
//Accept-Language: zh-CN,zh;q=0.9
//Cookie: PHPSESSID=3lrg5ho87jrh68afbfm2nsqrp4; Hm_lvt_4e9fea0594abd91080de592dbeb2afff=1517968374; __root_domain_v=.zaza666.com; _qddaz=QD.13t3gc.7m2rw3.jdcf6dv5; _qdda=3-1.1; _qddab=3-ckk4x1.jdcsml10; _qddamta_2852155138=3-0; Hm_lpvt_4e9fea0594abd91080de592dbeb2afff=1517991126
//
//xiaohao_id=769239&action=tuoguan


        String  url = "http://vip.zaza666.com/Center/tuoguan?vtype=2";
        String params = "action=tuoguan&xiaohao_id="+id;
        String res = "";

        URL realUrl = new URL(url);
        URLConnection connection = realUrl.openConnection();
        connection.setRequestProperty("Connection","Keep-Alive");
        connection.setRequestProperty("Content-Length","32");
        connection.setRequestProperty("Accept","application/json, text/javascript, */*; q=0.01");
        connection.setRequestProperty("Origin","http://vip.zaza666.com");
        connection.setRequestProperty("X-Requested-With","XMLHttpRequest");
        connection.setRequestProperty("Cookie",cookie);
        connection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36 Edge/14.14393");
        connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
        connection.setRequestProperty("Referer","http://vip.zaza666.com/Center/tuoguan");
        connection.setRequestProperty("Accept-Encoding","gzip,deflate");
        connection.setRequestProperty("Accept-Language","zh-CN,zh;q=0.9");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        PrintWriter pw = new PrintWriter(connection.getOutputStream());
        pw.write(params);
        pw.flush();

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));
        String line;
        while ((line = br.readLine())!= null){
            res += "\n"+line;
        }


        return res;

    }

}
