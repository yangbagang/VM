package com.ybg.rp.vm.net;

import android.content.Context;
import android.text.TextUtils;

import com.cnpay.tigerbalm.net.HttpPrompt;
import com.cnpay.tigerbalm.utils.TbLog;
import com.ybg.rp.vm.util.Config;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.OnResponseListener;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.RequestQueue;
import com.yolanda.nohttp.Response;
import com.yolanda.nohttp.download.DownloadQueue;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * 网络访问工具
 *
 * @author zenghonghua
 * @包名: com.cnpay.vending.yifeng.net
 * @修改记录:
 * @公司:
 * @date 2016/4/7 0007
 */
public class NetWorkUtil {

    public static NetWorkUtil netWorkUtil;

    /**
     * 请求队列
     */
    private RequestQueue requestQueue;

    /**
     * 下载队列
     */
    private static DownloadQueue downloadQueue;

    private NetWorkUtil() {
        requestQueue = NoHttp.newRequestQueue();
    }

    /**
     * 单一实例
     *
     * @return NetWorkUtil
     */
    public synchronized static NetWorkUtil getInstance() {
        if (netWorkUtil == null)
            netWorkUtil = new NetWorkUtil();
        return netWorkUtil;
    }

    /**
     * 下载队列
     */
    public static DownloadQueue getDownloadInstance() {
        if (downloadQueue == null)
            downloadQueue = NoHttp.newDownloadQueue();
        return downloadQueue;
    }

    /**
     * post 请求
     *
     * @param url url
     * @return
     */
    public Request<String> post(String url) {
        return NoHttp.createStringRequest(Config.URL_COMM + url, RequestMethod.POST);
    }

  /*  public Request<JSONObject> postJson(String url) {
        return NoHttp.createJsonObjectRequest(Config.URL_COMM + url, RequestMethod.POST);
    }*/

    /**
     * get 请求
     *
     * @return
     */
    public Request<String> get(String url) {
        return NoHttp.createStringRequest(Config.URL_COMM + url, RequestMethod.GET);
    }

    /**
     * 天到请求到队列
     *
     * @param context  上下文
     * @param what     用来标志请求, 在回调方法中会返回这个what
     * @param request  请求参数
     * @param callback 结果回调
     */
    public void add(Context context, int what, Request<String> request, final YFHttpListener callback) {
        requestQueue.add(what, request, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                try {
                    TbLog.i("[ 返回的网络数据----" + response.get());
                    String result = response.get();
                    JSONObject json = new JSONObject(result);
                    if (Config.TRUE.equals(json.getString(Config.SUCCESS))) {
                        callback.onSuccess(what, response);
                    } else {
                        callback.onFailed(what, json.getString(Config.ERROR));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailed(what, e.getMessage());
                }
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception e, int responseCode, long networkMillis) {
                String msg;
                if (e instanceof ConnectException) {
                    msg = HttpPrompt.CONNECTEXCEPTION;
                } else if (e instanceof ConnectTimeoutException) {
                    msg = HttpPrompt.CONNECTEXCEPTION;
                } else if (e instanceof UnknownHostException) {
                    msg = HttpPrompt.UNKNOWNHOSTEXCEPTION;
                } else if (e instanceof SocketException) {
                    msg = HttpPrompt.SOCKETEXCEPTION;
                } else if (e instanceof SocketTimeoutException) {
                    msg = HttpPrompt.SOCKETTIMEOUTEXCEPTION;
                } else if (e instanceof NullPointerException) {
                    msg = HttpPrompt.NULLPOINTEREXCEPTION;
                } else {
                    if (e == null || TextUtils.isEmpty(e.getMessage())) {
                        msg = HttpPrompt.NULLMESSAGEEXCEPTION;
                    } else {
                        msg = e.getMessage();
                    }
                }
                if (msg.equals("The target server failed to respond")) {
                    msg = HttpPrompt.SOCKETEXCEPTION;
                } else if (msg.contains("Request time out")) {
                    msg = HttpPrompt.SOCKETEXCEPTION;
                }
//                e = new Exception(msg);
                callback.onFailed(what, msg);
            }

            @Override
            public void onFinish(int what) {

            }
        });
    }

    /**
     * 取消这个sign标记的所有请求
     */
    public void cancelBySign(Object sign) {
        requestQueue.cancelBySign(sign);
    }

    /**
     * 取消队列中所有请求
     */
    public void cancelAll() {
        requestQueue.cancelAll();
    }

    /**
     * 退出app时停止所有请求
     */
    public void stopAll() {
        requestQueue.stop();
    }
}
