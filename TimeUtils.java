package com.example.shang.study_utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by admin on 2017/12/13.
 */

public class TimeUtils{

    public static void main(String []args){

//      2017.11.11
        String s ="1510353204638";
        Date date = new Date();
        System.out.println(s);

        String res = stampToDate(s);

//        String res= null;
//        try {
//            res = dateToStamp(s);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        System.out.println(res);



//        String source = "&lt;?xml version='1.0' encoding='utf-8' standalone='yes' ?&gt;\n" +
//                "&lt;map&gt;\n" +
//                "    &lt;string name=&quot;com.tencent.mm&quot;&gt;{&amp;quot;buildManufacturer&amp;quot;:&amp;quot;OnePlus&amp;quot;,&amp;quot;buildModel&amp;quot;:&amp;quot;A1001&amp;quot;,&amp;quot;buildSerial&amp;quot;:&amp;quot;wql8sm0tn383&amp;quot;,&amp;quot;empty&amp;quot;:false,&amp;quot;settingsSecureAndroidId&amp;quot;:&amp;quot;6iset1lvocevt3j&amp;quot;,&amp;quot;telephonyGetDeviceId&amp;quot;:&amp;quot;865209034938374&amp;quot;,&amp;quot;telephonyGetLine1Number&amp;quot;:&amp;quot;13700511132&amp;quot;,&amp;quot;telephonyGetNetworkType&amp;quot;:&amp;quot;9&amp;quot;,&amp;quot;telephonyGetSimSerialNumber&amp;quot;:&amp;quot;89154542370167379630&amp;quot;,&amp;quot;wifiInfoGetMacAddress&amp;quot;:&amp;quot;00:1F:5D:AC:5F:1B&amp;quot;,&amp;quot;wifiInfoGetSSID&amp;quot;:&amp;quot;TP-LINK_I7DDPO&amp;quot;}&lt;/string&gt;\n" +
//                "&lt;/map&gt;";
//         String regex = "telephonyGetDeviceId&amp;quot;:&amp;quot;(\\d+)&amp;quot;";
////        telephonyGetDeviceId&amp;quot;:&amp;quot;865209034938374&amp
//        System.out.println(getMatcher(regex,source));

    }

//    public static String getMatcher(String regex, String source){
//        String res = "";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(source);
//        while (matcher.find()){
//            res = matcher.group(1);
//        }
//        return res;
//        String s = "大司法_解释2017.2323";
//        String ss = "2017.";
//        boolean b = isContains(s,ss);
//        System.out.println(" xxx b == "+b);
//
//    }
//
//    private static boolean isContains(String conRemark, String target) {
//        if (conRemark.indexOf(target)!=-1){
//            return true;
//        }else {
//            return false;
//        }
//
//    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String time){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        long lt = new Long(time);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String time) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(time);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }




}
