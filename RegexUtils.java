public class RegexUtils {



    //  判断是否为手机号（问号前面的为区号）

    public static boolean isPhoneNumber(String phoneNumber){

        // ^ 一行的起始  $一行的结束

        String regex =  "^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(phoneNumber);

        return matcher.matches();

    }



    // 判断是否为用户名（大写或小写字母或数字组成的6-12位）\w词字符[a-zA-Z0-9]

    public static boolean isUsername(String usrname){

        String regex = "^\\w{6,12}$";

        // 可以改进一下，就是以字母开头的6到12位词字符

        // String regex = "^[a-zA-Z]\\w{5,11}$";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(usrname);

        return matcher.matches();

    }



    // 判断是否为邮箱

    public static boolean isMail(String mail){

        String regex = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(mail);

        return matcher.matches();

    }



    //  判断是否为身份证（15位数字或者17位数加上1个数字或者X）

    public static boolean isIDcard(String idcard){

        String regex = "(^\\d{15}$)|^\\d{17}([0-9]|X)$";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(idcard);

        return matcher.matches();

    }



    //  判断是否为URL

    public static boolean isUrl(String url){

        String regex = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(url);

        return matcher.matches();

    }



    //  判断是否为IP地址

    public static boolean isIP(String ip){

        String regex = "(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(ip);

        return matcher.matches();

    }



    // 判断是否为中文字符

    public static boolean isChinese(String chinese){

        String regex = "^[\u4e00-\u9fa5]{1,9}$"; // 加{1,9}表示1到9个中文符，可以去掉的

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(chinese);

        return matcher.matches();



    }



}