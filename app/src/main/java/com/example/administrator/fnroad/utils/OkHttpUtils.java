package com.example.administrator.fnroad.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by hornkyin on 16/6/21.
 * Pvt词根为Private缩写，为内部方法
 * 封装了①Get同步、异步请求，包含请求Header标头的设置；②Post同步、异步请求，包含请求Header标头的设置；
 * 注：HTTP同步请求需开启子线程执行，更新UI需要Handler协助;HTTP异步请求已封装了回调结果置于UI线程，可直接更新UI
 */
public class OkHttpUtils {
    private static OkHttpUtils mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;
    private Gson mGson;

    private OkHttpUtils() {
        mOkHttpClient = new OkHttpClient();
        mDelivery = new Handler(Looper.getMainLooper());
        mGson = new Gson();
        //设置网络连接、读、写的超时时间
        mOkHttpClient.newBuilder().connectTimeout(10, TimeUnit.SECONDS);
        mOkHttpClient.newBuilder().readTimeout(10, TimeUnit.SECONDS);
        mOkHttpClient.newBuilder().writeTimeout(10, TimeUnit.SECONDS);
    }

    /**
     * 单例模式，获取OkHttpUtils单例对象
     *
     * @return
     */
    public static OkHttpUtils getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpUtils();
                }
            }
        }
        return mInstance;
    }

    /**
     * 内部类，构建参数对象
     */
    public static class Param {
        String key;
        String value;

        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    /**
     * 内部抽象类，用于成功与失败的结果回调。T可以是自定义的Model类，会自行的将服务器请求到的JSON字符串解析成Model类对象
     *
     * @param <T>
     */
    public static abstract class ResultCallback<T> {
        Type mType;

        public ResultCallback() {
            mType = getSuperclassTypeParameter(getClass());
        }

        static Type getSuperclassTypeParameter(Class<?> subclass) {
            Type superclass = subclass.getGenericSuperclass();
            if (superclass instanceof Class) {
                throw new RuntimeException("Missing type parameter.");
            }
            ParameterizedType parameterized = (ParameterizedType) superclass;
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }

        //Handler将结果Post至UI线程中，可直接更新UI
        public abstract void onError(Request request, Exception e);

        //Handler将结果Post至UI线程中，可直接更新UI
        public abstract void onResponse(T response);
    }

    /**
     * 同步的Get请求
     *
     * @param url
     * @param headers
     * @return
     * @throws IOException
     */
    private Response getSyncPvt(String url, Map<String, String> headers) throws IOException {
        final Request request = buildGetRequest(url, headers);
        Call call = mOkHttpClient.newCall(request);
        Response execute = call.execute();
        return execute;
    }

    /**
     * 同步Get请求，获取字符串
     *
     * @param url
     * @param headers
     * @return
     * @throws IOException
     */
    private String getSyncStringPvt(String url, Map<String, String> headers) throws IOException {
        Response execute = getSyncPvt(url, headers);
        return execute.body().string();
    }

    /**
     * 异步的Get请求
     *
     * @param url
     * @param headers
     * @param callback
     */
    private void getAsynPvt(String url, Map<String, String> headers, final ResultCallback callback) {
        final Request request = buildGetRequest(url, headers);
        deliveryResult(callback, request);
    }

    /**
     * 同步的Post请求
     *
     * @param url
     * @param headers
     * @param params
     * @return
     * @throws IOException
     */
    private Response postSyncPvt(String url, Map<String, String> headers, Param[] params) throws IOException {
        Request request = buildPostRequest(url, headers, params);
        Response response = mOkHttpClient.newCall(request).execute();
        return response;
    }

    /**
     * 同步的Post请求，获取字符串
     *
     * @param url
     * @param headers
     * @param params
     * @return
     * @throws IOException
     */
    private String postSyncStringPvt(String url, Map<String, String> headers, Param[] params) throws IOException {
        Response response = postSyncPvt(url, headers, params);
        return response.body().string();
    }

    /**
     * 异步的Post请求
     *
     * @param url
     * @param headers
     * @param params
     * @param callback
     */
    private void postAsynPvt(String url, Map<String, String> headers, Param[] params, final ResultCallback callback) {
        Request request = buildPostRequest(url, headers, params);
        deliveryResult(callback, request);
    }

    /**
     * 异步的Post请求
     *
     * @param url
     * @param headers
     * @param params
     * @param callback
     */
    private void postAsynPvt(String url, Map<String, String> headers, Map<String, String> params, final ResultCallback callback) {
        Param[] paramsArr = map2Params(params);
        Request request = buildPostRequest(url, headers, paramsArr);
        deliveryResult(callback, request);
    }

    /**
     * 异步的Post请求
     * 针对图片上传
     *
     * @param url      服务器接口的URL
     * @param filesDir 图片路径
     * @param params   访问接口必需的参数
     * @param callback
     */
    private void postAsynPvtOfImage(String url, List<String> filesDir, Map<String, String> params, int scale, final ResultCallback callback) {
        Request request = buildPostRequest(url, filesDir, params, scale);
        deliveryResult(callback, request);
    }

    //*****************************************公开方法**********************************************

    /**
     * 公开的Get同步请求方法，需另行开启Thread执行Get网络请求
     *
     * @param url
     * @param resultCallback
     * @return
     * @throws IOException
     */
    public static Response getSync(String url, ResultCallback<String> resultCallback) throws IOException {
        return getInstance().getSyncPvt(url, null);
    }

    /**
     * 公开的Get同步请求方法，需另行开启Thread执行Get网络请求
     *
     * @param url
     * @param headers
     * @return
     * @throws IOException
     */
    public static Response getSync(String url, Map<String, String> headers) throws IOException {
        return getInstance().getSyncPvt(url, headers);
    }

    /**
     * 公开的Get同步请求方法，获取字符串，需另行开启Thread执行Get网络请求
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String getSyncString(String url) throws IOException {
        return getInstance().getSyncStringPvt(url, null);
    }

    /**
     * 公开的Get同步请求方法，获取字符串，需另行开启Thread执行Get网络请求
     *
     * @param url
     * @param headers
     * @return
     * @throws IOException
     */
    public static String getSyncString(String url, Map<String, String> headers) throws IOException {
        return getInstance().getSyncStringPvt(url, headers);
    }

    /**
     * 公开的Get异步请求方法，ResultCallback回调，回调结果在UI线程中执行
     *
     * @param url
     * @param callback
     */
    public static void getAsyn(String url, ResultCallback callback) {
        getInstance().getAsynPvt(url, null, callback);
    }

    /**
     * 公开的Get异步请求方法，ResultCallback回调，回调结果在UI线程中执行
     *
     * @param url
     * @param headers
     * @param callback
     */
    public static void getAsyn(String url, Map<String, String> headers, ResultCallback callback) {
        getInstance().getAsynPvt(url, headers, callback);
    }

    /**
     * 公开的Post同步请求方法，需另行开启Thread执行Post网络请求
     *
     * @param url
     * @param params
     * @return
     * @throws IOException
     */
    public static Response postSync(String url, Param[] params) throws IOException {
        return getInstance().postSyncPvt(url, null, params);
    }

    /**
     * 公开的Post同步请求方法，需另行开启Thread执行Post网络请求
     *
     * @param url
     * @param headers
     * @param params
     * @return
     * @throws IOException
     */
    public static Response postSync(String url, Map<String, String> headers, Param[] params) throws IOException {
        return getInstance().postSyncPvt(url, headers, params);
    }

    /**
     * 公开的Post同步请求方法，获取字符串，需另行开启Thread执行Post网络请求
     *
     * @param url
     * @param params
     * @return
     * @throws IOException
     */
    public static String postSyncString(String url, Param[] params) throws IOException {
        return getInstance().postSyncStringPvt(url, null, params);
    }

    /**
     * 公开的Post同步请求方法，获取字符串，需另行开启Thread执行Post网络请求
     *
     * @param url
     * @param headers
     * @param params
     * @return
     * @throws IOException
     */
    public static String postSyncString(String url, Map<String, String> headers, Param[] params) throws IOException {
        return getInstance().postSyncStringPvt(url, headers, params);
    }

    /**
     * 公开的Post异步请求方法，ResultCallback回调，回调结果在UI线程中执行
     *
     * @param url
     * @param params
     * @param callback
     */
    public static void postAsyn(String url, Param[] params, final ResultCallback callback) {
        getInstance().postAsynPvt(url, null, params, callback);
    }

    /**
     * 公开的Post异步请求方法，ResultCallback回调，回调结果在UI线程中执行
     *
     * @param url
     * @param headers
     * @param params
     * @param callback
     */
    public static void postAsyn(String url, Map<String, String> headers, Param[] params, final ResultCallback callback) {
        getInstance().postAsynPvt(url, headers, params, callback);
    }

    /**
     * 公开的Post异步请求方法，ResultCallback回调，回调结果在UI线程中执行
     *
     * @param url
     * @param params
     * @param callback
     */
    public static void postAsyn(String url, Map<String, String> params, final ResultCallback callback) {
        getInstance().postAsynPvt(url, null, params, callback);
    }

    /**
     * 公开的Post异步请求方法，ResultCallback回调，回调结果在UI线程中执行
     *
     * @param url
     * @param headers
     * @param params
     * @param callback
     */
    public static void postAsyn(String url, Map<String, String> headers, Map<String, String> params, final ResultCallback callback) {
        getInstance().postAsynPvt(url, headers, params, callback);
    }

    /**
     * 公开的Post异步请求方法，ResultCallback回调，回调结果在UI线程中执行
     * 针对图片上传
     *
     * @param url      服务器接口的URL
     * @param filesDir 图片路径
     * @param params   访问接口的参数
     * @param callback
     */
    public static void postAsyn(String url, List<String> filesDir, Map<String, String> params, int scale, final ResultCallback callback) {
        getInstance().postAsynPvtOfImage(url, filesDir, params, scale, callback);
    }


    //*****************************************公共方法**********************************************

    /**
     * 传递回调结果
     *
     * @param callback
     * @param request
     */
    private void deliveryResult(final ResultCallback callback, Request request) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailedStringCallback(call.request(), e, callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final String string = response.body().string();
                    if (callback.mType == String.class) {
                        sendSuccessResultCallback(string, callback);
                    } else {
                        Object o = mGson.fromJson(string, callback.mType);
                        sendSuccessResultCallback(o, callback);
                    }
                } catch (IOException | com.google.gson.JsonParseException e) {
                    sendFailedStringCallback(response.request(), e, callback);
                }
            }
        });
    }

    /**
     * 回调成功后的结果
     *
     * @param object
     * @param callback
     */
    private void sendSuccessResultCallback(final Object object, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onResponse(object);
                }
            }
        });
    }

    /**
     * 回调失败后的结果
     *
     * @param request
     * @param e
     * @param callback
     */
    private void sendFailedStringCallback(final Request request, final Exception e, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null)
                    callback.onError(request, e);
            }
        });
    }

    /**
     * 生成Post的请求，返回Request对象
     *
     * @param url
     * @param headers
     * @param params
     * @return
     */
    private Request buildPostRequest(String url, Map<String, String> headers, Param[] params) {
        if (params == null) {
            params = new Param[0];
        }
        FormBody.Builder builder = new FormBody.Builder();
        for (Param param : params) {
            builder.add(param.key, param.value);
        }
        RequestBody requestBody = builder.build();
        return new Request.Builder()
                .url(url)
                .headers(buildHeaders(headers))
                .post(requestBody)
                .build();
    }

    /**
     * 生成Post的请求，返回Request对象。
     * 针对图片上传
     *
     * @param url      服务器接口的URL
     * @param filesDir 图片的路径
     * @param params   访问接口需要的参数
     * @return
     */
    private Request buildPostRequest(String url, List<String> filesDir, Map<String, String> params, int scale) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (int i = 0; i < filesDir.size(); i++) {
            File file = new File(filesDir.get(i));
            if (file != null) {

                String path = file.getPath();
                Bitmap bitmap = getImage(path, scale);
                String newPath = file.getParent() + "/temp";
                File newFile = null;
                try {
                    newFile = saveFile(bitmap, newPath, file.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                builder.addFormDataPart("PROBLEM_IMAGE" + i, file.getName(), RequestBody.create(MediaType.parse("image/png"), newFile));
            }
        }
        //添加其它信息
        for (String key : params.keySet()) {
            builder.addFormDataPart(key, params.get(key));
        }

        MultipartBody requestBody = builder.build();
        //构建请求
        return new Request.Builder()
                .url(url)//地址
                .post(requestBody)//添加请求体
                .build();

    }

    /**
     * 生成Get请求时的Request对象
     *
     * @param url
     * @param headers
     * @return
     */
    private Request buildGetRequest(String url, Map<String, String> headers) {
        return new Request.Builder()
                .url(url)
                .headers(buildHeaders(headers))
                .build();
    }

    /**
     * 生成请求的Headers，比如放入Cookie信息
     *
     * @param headers
     * @return
     */
    private Headers buildHeaders(Map<String, String> headers) {
        Headers.Builder headerBuilder = new Headers.Builder();
        if (headers != null && !headers.isEmpty())
            for (String key : headers.keySet()) {
                headerBuilder.add(key, headers.get(key));
            }
        return headerBuilder.build();
    }

    /**
     * 将Map对象转换成内部类Params对象
     *
     * @param params
     * @return
     */
    private Param[] map2Params(Map<String, String> params) {
        if (params == null) return new Param[0];
        int size = params.size();
        Param[] res = new Param[size];
        Set<Map.Entry<String, String>> entries = params.entrySet();
        int i = 0;
        for (Map.Entry<String, String> entry : entries) {
            res[i++] = new Param(entry.getKey(), entry.getValue());
        }
        return res;
    }

    /************************************************************************************************/

    private Bitmap getImage(String srcPath, int scale) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        if (scale <= 0)
            scale = 1;
        newOpts.inSampleSize = scale;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

    private Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 将Bitmap转换成文件
     * 保存文件
     *
     * @param bitmap
     * @param fileName
     * @throws IOException
     */
    public static File saveFile(Bitmap bitmap, String path, String fileName) throws IOException {
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path, fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
        return myCaptureFile;

    }
}
