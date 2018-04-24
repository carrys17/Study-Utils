package com.example.shang.adminmanagedemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.shang.adminmanagedemo.Constant.LT_ADD_FACE;
import static com.example.shang.adminmanagedemo.Constant.LT_BANNER_IMAGE_PREFIX;
import static com.example.shang.adminmanagedemo.Constant.LT_BRAND_LIST;
import static com.example.shang.adminmanagedemo.Constant.LT_DEL_BANNER;
import static com.example.shang.adminmanagedemo.Constant.LT_GET_CODE;
import static com.example.shang.adminmanagedemo.Constant.LT_LOGIN;
import static com.example.shang.adminmanagedemo.Constant.LT_PICLIST;
import static com.example.shang.adminmanagedemo.Constant.LT_UPLOAD_IMAGE;


public class OkhttpUtils {

    private static final String TAG = "OkhttpUtils";


    private static volatile OkHttpClient client = null;


    public static OkHttpClient getClient() {
        if (client == null){
            synchronized (OkhttpUtils.class){
                if (client == null){
                    client = new OkHttpClient.Builder().cookieJar(new CookieJar() {

                        private final HashMap<String,List<Cookie>> cookieStore = new HashMap<>();

                        @Override
                        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                            Log.i(TAG, "saveFromResponse: url.host() -- "+url.host()+ ", cookies -- "+cookies);
                            cookieStore.put(url.host(),cookies);
                        }

                        @Override
                        public List<Cookie> loadForRequest(HttpUrl url) {
                            if (cookieStore.get(url.host()) != null){
                                Log.i(TAG, "loadForRequest: url.host() -- "+url.host());
                                Log.i(TAG, "loadForRequest: "+cookieStore.get(url.host()));
                            }
                            return cookieStore.get(url.host()) != null ? cookieStore.get(url.host()): new ArrayList<Cookie>();
                        }
                    }).connectTimeout(10, TimeUnit.SECONDS).readTimeout(20,TimeUnit.SECONDS).build();
                }
            }
        }
        return client;
    }

    // 获取验证码
    public static HashMap getCode() throws IOException {

        OkHttpClient client = getClient();
        Log.i(TAG, "getCode: client -- "+client.toString());
        HashMap map = new HashMap();
        Bitmap bitmap;

        RequestBody requestBody = new FormBody.Builder()
                .add("size","24")  // 字体大小
                .add("length","4") // 文字长度
                .build();

        Request request = new Request.Builder()
                .url(LT_GET_CODE)
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();

        Log.i(TAG, "getCode: response -- "+response);

        int resCode = response.code();
        String resMessage = response.message();

        Log.i(TAG, "getCode: resCode -- "+resCode);
        Log.i(TAG, "getCode: resMessage -- "+resMessage);

        // 获取成功
        if (resCode == 200){
            InputStream inputStream = response.body().byteStream();
            Log.i(TAG, "getCode: inputStream -- "+inputStream);
            bitmap = BitmapFactory.decodeStream(inputStream);
        }else {
            bitmap = null;
        }
        map.put("bitmap",bitmap);
        map.put("message",resMessage);
        return map;
    }


//    /**
//     *  用户注册
//     * @param username       账号
//     * @param password       密码
//     * @param repassword     确认密码
//     * @param phone           手机号
//     * @return
//     * @throws IOException
//     */
//    public static JSONObject userRegister(String username,String password,String repassword,String phone) throws IOException, JSONException {
//        JSONObject object = null;
//
//        OkHttpClient client = getClient();
//        Log.i(TAG, "userRegister: client -- "+client.toString());
//
//        RequestBody requestBody = new FormBody.Builder()
//                .add("username",username)
//                .add("password",password)
//                .add("repassword",repassword)
//                .add("phone",phone)
//                .build();
//
//        Request request = new Request.Builder()
//                .url(LT_REGISTER)
//                .post(requestBody)
//                .build();
//
//        Response response = client.newCall(request).execute();
//        Log.i(TAG, "userRegister: response -- "+response);
//        String body = response.body().string();
//        body = StringUtils.UnicodeDecode(body);
//        Log.i(TAG, "userRegister: body -- "+body);
//        if (StringUtils.isJSONString(body)){
//            object = new JSONObject(body);
//        }
//        return object;
//
//    }

    /**
     *  后台管理员登录
     * @param username    账号
     * @param password    密码
     * @param code         验证码
     */
    public static JSONObject userLogin(String username,String password,String code) throws IOException, JSONException {
        JSONObject object = null;
        RequestBody requestBody = new FormBody.Builder()
//                .add("username",username)
                .add("adminname",username)
                .add("password",password)
                .add("code",code)
                .build();

        OkHttpClient client = getClient();
        Log.i(TAG, "userLogin: client -- "+client.toString());

        // cookie就是客户端的会话id，而session就是服务器端的会话id，根据这个id号可以查询到你的会话内容。
        Request request = new Request.Builder()
                .url(LT_LOGIN)
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();

        Log.i(TAG, "userLogin: response -- "+response);
        Headers headers = response.headers();
        Log.i(TAG, "userLogin: headers -- "+headers);

        String body = response.body().string();
        body = StringUtils.UnicodeDecode(body);
        Log.i(TAG, "userLogin: body -- "+body);
        if (StringUtils.isJSONString(body)){
            object = new JSONObject(body);
        }
        return object;
    }

    // 获取轮播图
    public static HashMap<String,String> getPicList() throws IOException, JSONException {
        OkHttpClient client = getClient();
        Log.i(TAG, "getPicList: client -- "+client.toString());
        Request request = new Request.Builder()
                .url(LT_PICLIST)
                .get()
                .build();

        Response response = client.newCall(request).execute();
        Log.i(TAG, "getPicList: response -- "+response);
        String res = response.body().string();
        res = StringUtils.UnicodeDecode(res);
        Log.i(TAG, "getPicList: res -- "+res);

        if (StringUtils.isJSONString(res)){
            JSONObject object = new JSONObject(res);
            if ("1000".equals(object.getString("err_code"))){
                Log.i(TAG, "getPicList: 获取图片成功");
                JSONArray array = object.getJSONArray("result");
                HashMap<String,String> images = new HashMap();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object1 = (JSONObject) array.get(i);
                    String id = object1.getString("id");
                    String image = object1.getString("image");
                    image = LT_BANNER_IMAGE_PREFIX + image;
                    images.put(id,image);
                }
                return images;
            }else {
                Log.i(TAG, "getPicList: 获取图片失败, "+ object.getString("err_msg"));
            }
        }

        // {"err_code":"1000","result":[{"id":"15","image":"5923d373dc647.jpg"},{"id":"16",
        // "image":"5923d373e5ae2.jpg"},{"id":"17","image":"5923d373efee1.jpg"},{"id":"19","image":"5923d37426b31.jpg"}]}

        return null;
    }



    // 获取品牌图
    public static HashMap<String,String>  getBrandList() throws IOException, JSONException {
        OkHttpClient client = getClient();
        Log.i(TAG, "getBrandList: client -- "+client.toString());
        Request request = new Request.Builder()
                .url(LT_BRAND_LIST)
                .get()
                .build();

        Response response = client.newCall(request).execute();
        Log.i(TAG, "getBrandList: response -- "+response);
        String res = response.body().string();
        res = StringUtils.UnicodeDecode(res);
        Log.i(TAG, "getBrandList: res -- "+res);

        if (StringUtils.isJSONString(res)){
            JSONObject object = new JSONObject(res);
            if ("1000".equals(object.getString("err_code"))){
                Log.i(TAG, "getBrandList: 获取图片成功");
                JSONArray array = object.getJSONArray("result");
                HashMap<String,String>  images = new HashMap<>();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object1 = (JSONObject) array.get(i);
                    String id = object1.getString("id");
                    String image = object1.getString("image");
                    image = LT_BANNER_IMAGE_PREFIX + image;
                    images.put(id,image);
                }
                return images;
            }else {
                Log.i(TAG, "getBrandList: 获取图片失败, "+ object.getString("err_msg"));
            }
        }

        // {"err_code":"1000","result":[{"id":"15","image":"5923d373dc647.jpg"},{"id":"16",
        // "image":"5923d373e5ae2.jpg"},{"id":"17","image":"5923d373efee1.jpg"},{"id":"19","image":"5923d37426b31.jpg"}]}

        return null;
    }

    // 注册人脸信息
    public static boolean addFace(String adminId) throws IOException, JSONException {

        OkHttpClient client = getClient();
        Log.i(TAG, "addFace: client -- "+client.toString());

        RequestBody requestBody = new FormBody.Builder()
                .add("id",adminId)
                .build();


        Request request = new Request.Builder()
                .url(LT_ADD_FACE)
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        Log.i(TAG, "addFace: response -- "+response);
        String res = response.body().string();
        res = StringUtils.UnicodeDecode(res);
        Log.i(TAG, "addFace: res -- "+res);



        if (StringUtils.isJSONString(res)){
            JSONObject object = new JSONObject(res);
            if ("1000".equals(object.getString("err_code"))){
                Log.i(TAG, "addFace: 添加面部识别成功");
                return true;
            }else {
                Log.i(TAG, "addFace: 添加面部识别失败, "+ object.getString("err_msg"));
                return false;
            }
        }
        return false;
    }


    // 删除轮播图
    public static boolean delBannerPic(String bannerId) throws IOException, JSONException {
        OkHttpClient client = getClient();
        Log.i(TAG, "delBannerPic: client -- "+client.toString());

        RequestBody requestBody = new FormBody.Builder()
                .add("id",bannerId)
                .build();

        Request request = new Request.Builder()
                .url(LT_DEL_BANNER)
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        Log.i(TAG, "delBannerPic: response -- " + response);
        String res = response.body().string();
        res = StringUtils.UnicodeDecode(res);
        Log.i(TAG, "delBannerPic: res -- " + res);

        if (StringUtils.isJSONString(res)) {
            JSONObject object = new JSONObject(res);
            if ("1000".equals(object.getString("err_code"))) {
                Log.i(TAG, "delBannerPic: 删除轮播图成功");
                return true;
            } else {
                Log.i(TAG, "delBannerPic: 删除轮播图失败, " + object.getString("err_msg"));
                return false;
            }
        }
        return false;
    }


    //将图片发送到服务器
    public static boolean uploadFile(File file,String prefix) throws IOException, JSONException {
        OkHttpClient client = getClient();

        long time = System.currentTimeMillis()/1000L;

        //   RequestBody.create(MediaType.parse("image/png")   jpeg, file)); application/octet-stream
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);



        Log.i(TAG, "uploadFile: fileBody -- "+fileBody);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", prefix+"_"+time+".jpg", fileBody)
                .build();
        Request request = new Request.Builder()
                .url(LT_UPLOAD_IMAGE)
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        Log.i(TAG, "uploadFile: response -- " + response);
        String res = response.body().string();
        res = StringUtils.UnicodeDecode(res);
        Log.i(TAG, "uploadFile: res -- " + res);

        if (StringUtils.isJSONString(res)) {
            JSONObject object = new JSONObject(res);
            if ("1000".equals(object.getString("err_code"))) {
                Log.i(TAG, "uploadFile: 上传图片成功");
                return true;
            } else {
                Log.i(TAG, "uploadFile: 上传图片失败, " + object.getString("err_msg"));
                return false;
            }
        }
        return false;

    }


    // 获取热门推荐
//    public static ArrayList<GoodsJson.GoodJson>  getRecommend() throws IOException, JSONException {
//
//        OkHttpClient client = getClient();
//        Log.i(TAG, "getRecommend: client -- "+client.toString());
//        Request request = new Request.Builder()
//                .url(LT_RECOMMEND)
//                .get()
//                .build();
//
//        Response response = client.newCall(request).execute();
//        Log.i(TAG, "getRecommend: response -- "+response);
//        String res = response.body().string();
//        res = StringUtils.UnicodeDecode(res);
//        if (StringUtils.isJSONString(res)){
////            JSONObject object = new JSONObject(res);
////            String result = object.getString("result");
////            Log.i(TAG, "getRecommend: result -- "+result);
//            Gson gson = new Gson();
//            GoodsJson goodsJson = gson.fromJson(res,GoodsJson.class);
//            if ("1000".equals(goodsJson.err_code)){
//                ArrayList<GoodsJson.GoodJson> goodJsonArrayList  = goodsJson.result;
//                return goodJsonArrayList;
//
//            }else {
//                Log.i(TAG, "getRecommend: 获取热门推荐失败 "+ new JSONObject(res).getString("err_msg"));
//                return null;
//            }
//        }else {
//            return null;
//        }
//    }


    // 获取新品商品
//    public static ArrayList<GoodsJson.GoodJson>  getNewGoods() throws IOException, JSONException {
//
//        OkHttpClient client = getClient();
//        Log.i(TAG, "getNewGoods: client -- "+client.toString());
//        Request request = new Request.Builder()
//                .url(LT_NEWGOODS)
//                .get()
//                .build();
//
//        Response response = client.newCall(request).execute();
//        Log.i(TAG, "getNewGoods: response -- "+response);
//        String res = response.body().string();
//        res = StringUtils.UnicodeDecode(res);
//        if (StringUtils.isJSONString(res)){
////            JSONObject object = new JSONObject(res);
////            String result = object.getString("result");
////            Log.i(TAG, "getNewGoods: result -- "+result);
//            Gson gson = new Gson();
//            GoodsJson goodsJson = gson.fromJson(res,GoodsJson.class);
//            if ("1000".equals(goodsJson.err_code)){
//                ArrayList<GoodsJson.GoodJson> goodJsonArrayList  = goodsJson.result;
//                return goodJsonArrayList;
//            }else {
//                Log.i(TAG, "getNewGoods: 获取热门推荐失败"+ new JSONObject(res).getString("err_msg"));
//                return null;
//            }
//        }else {
//            return null;
//        }
//    }

//    // 获取热销商品
//    public static ArrayList<GoodsJson.GoodJson>  getHotGoods() throws IOException, JSONException {
//
//        OkHttpClient client = getClient();
//        Log.i(TAG, "getHotGoods: client -- "+client.toString());
//        Request request = new Request.Builder()
//                .url(LT_HOTGOODS)
//                .get()
//                .build();
//
//        Response response = client.newCall(request).execute();
//        Log.i(TAG, "getHotGoods: response -- "+response);
//        String res = response.body().string();
//        res = StringUtils.UnicodeDecode(res);
//        if (StringUtils.isJSONString(res)){
//
//            JSONObject object = new JSONObject(res);
//            String result = object.getString("result");
//            Log.i(TAG, "getHotGoods: result -- "+result);
//            Gson gson = new Gson();
//            GoodsJson goodsJson = gson.fromJson(res,GoodsJson.class);
//            if ("1000".equals(goodsJson.err_code)){
//                ArrayList<GoodsJson.GoodJson> goodJsonArrayList  = goodsJson.result;
//                return goodJsonArrayList;
//            }else {
//                Log.i(TAG, "getHotGoods: 获取热门推荐失败"+ new JSONObject(res).getString("err_msg"));
//                return null;
//            }
//        }else {
//            return null;
//        }
//    }

}
