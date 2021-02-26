package com.huawei.net.retrofit.impl;


import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.huawei.holobase.Consts;
import com.huawei.holobase.bean.EventMsg;
import com.huawei.holobase.bean.LoginBean;
import com.huawei.holobase.bean.PtzToken;
import com.huawei.holobase.bean.bean;
import com.huawei.holobase.view.TipDialog;
import com.huawei.holobasic.consts.MySharedPreferenceKey;
import com.huawei.holobasic.utils.MySharedPreference;
import com.huawei.holobasic.utils.TUtils;
import com.huawei.net.R;
import com.huawei.net.retrofit.request.BaseRequestParam;
import com.huawei.net.retrofit.request.ResponseCode;
import com.huawei.net.retrofit.request.ResponseData;
import com.huawei.net.retrofit.request.RetrofitClient;
import com.huawei.net.retrofit.utils.DebugUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.huawei.holobase.Consts.TAG;
import static com.huawei.net.retrofit.request.ResponseCode.SUCCESS;


public class AppImpl {
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private final AppService appService;
//
//    private Dialog mProDialog;

    @SuppressLint("StaticFieldLeak")
    private static AppImpl appImpl;

//    private static boolean isLoading = false;

    public static AppImpl getInstance(Context context) {
        if (appImpl == null) {
            return new AppImpl(context);
        } else {
            return appImpl;
        }
    }

    private AppImpl(Context context) {
        AppImpl.context = context;
        appService = RetrofitClient.getInstance(context).create(AppService.class);

    }

    /**
     * 命令透传(云台控制)
     *
     * @param param
     * @param enterpriseId
     * @param deviceId
     * @param channelId
     * @return
     */
    public Observable<ResponseData<Object>> ptzDevSendCmd(BaseRequestParam param, String enterpriseId,
                                                          String deviceId, String channelId, String controlToken) {
        String url = Consts.ptzDevSendCmd.replace("enterprise_id", enterpriseId).replace("device_id", deviceId)
                .replace("channel_id", channelId);
        url = url + "/" + controlToken;
        return loadData(url, param.buildHeader(), param.build(), new TypeToken<Object>() {
        }.getType());
    }


    /**
     * 获取ptz token
     *
     * @param param
     * @return
     */
    public Observable<ResponseData<PtzToken>> getPtzControlToken(BaseRequestParam param) {
        return loadData(Consts.getPtzControlToken, param.buildHeader(), param.build(), new TypeToken<PtzToken>() {
        }.getType());
    }

    /**
     * 释放ptz的token
     *
     * @param param
     * @return
     */
    public Observable<ResponseData<bean>> releasePtzControlToken(BaseRequestParam param) {
        return loadData(Consts.releasePtzControlToken, param.buildHeader(), param.build(), new TypeToken<bean>() {
        }.getType());
    }


    /**
     * 基础数据请求
     *
     * @param url    访问地址
     * @param params 请求内容
     * @return 返回值
     */
    @SuppressWarnings("unchecked")
    private <T> Observable<ResponseData<T>> loadData(String url, HashMap<String, Object> params, final Type typeToken) {
        if (isConnected(context)) {

            //转换为json字符串
            String jsonParam = new Gson().toJson(params);
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonParam);
            if (DebugUtil.isApkInDebug(context)) {
                Log.e("jsonParam>>>>>>>>>>>", body.toString() + "=====" + jsonParam);
            }

            //Rx被观测者
            Observable<String> observable = appService.loadData(url, body);

            return observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap(new Func1<String, Observable<ResponseData<T>>>() {
                        @Override
                        public Observable<ResponseData<T>> call(String strResponse) {
                            if (DebugUtil.isApkInDebug(context)) {
                                Log.e("strResponse>>>>>>>>>>>", strResponse);
                            }
                            //创建对象,承载数据.
                            ResponseData<T> resultResponseData = new ResponseData();
                            //解密返回结果
                            if (null != strResponse) {
                                try {
                                    JSONObject object = new JSONObject(strResponse);
                                    if (object.has("code")) {
                                        resultResponseData.setCode(object.getInt("code"));
                                    }
                                    if (object.has("msg")) {
                                        resultResponseData.setMsg(object.getString("msg"));
                                    }
                                    if (object.has("data")) {
                                        String str = object.getString("data");
                                        T t = new Gson().fromJson(str, typeToken);
                                        resultResponseData.setData(t);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
//                            Log.e(typeToken + ">>>>>>>>>>>", new Gson().toJson(resultResponseData));
//
//                            if (resultResponseData.getCode() != ResponseCode.SUCCESS) {
//                                TUtils.show(context, resultResponseData.getMsg());
//                            }
                            return Observable.just(resultResponseData);
                        }
                    })
                    //错误处理，防止错误返回导致数据链断裂
                    .onErrorReturn(new Func1<Throwable, ResponseData<T>>() {
                                       @Override
                                       public ResponseData<T> call(Throwable throwable) {
                                           throwable.printStackTrace();
                                           ResponseData<T> errorData = new ResponseData<>();
                                           if (throwable.getMessage().contains("after 10000ms")) {
                                               errorData.setCode(ResponseCode.REQUEST_ERROR_OVERSION);
                                           } else {
                                               errorData.setCode(ResponseCode.REQUEST_ERROR);
                                           }
                                           errorData.setData(null);
                                           return errorData;
                                       }
                                   }
                    );
        } else {
            showNetToast(context);
            ResponseData<T> errorData = new ResponseData<>();
            errorData.setCode(ResponseCode.REQUEST_ERROR);
            errorData.setMsg("请检查网络设置");
            errorData.setData(null);
            return Observable.just(errorData);
        }
    }


    /**
     * 基础数据请求
     *
     * @param url    访问地址
     * @param params 请求内容
     * @return 返回值
     */
    @SuppressWarnings({"unchecked", "ConstantConditions"})
    private <T> Observable<ResponseData<T>> loadData(final String url, final HashMap<String, Object> header, final HashMap<String, Object> params, final Type typeToken) {

        if (isConnected(context)) {
            //转换为json字符串
//            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
//            String jsonParam = gson.toJson(params);
//            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonParam);
//            Log.e("jsonParam>>>>>>>>>>>", body.toString() + "=====" + jsonParam);
//            //Rx被观测者
//            final Observable<String> observable = appService.loadData(url,
//                    header.get("Authorization").toString(),
//                    body);


//            if (checkTokenTimeout()) {
//                LinkedHashMap<String, Object> data = new LinkedHashMap<>();
//                data.put("tiken", MySharedPreference.getString(MySharedPreferenceKey.LoginKey.TIKEN));
//
//                Gson gsonToken = new GsonBuilder().disableHtmlEscaping().create();
//                String jsonParamToken = gsonToken.toJson(data);
//                RequestBody bodyToken = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonParamToken);
//                //Rx被观测者
//                Observable<String> observableToken = appService.loadData(Consts.renewalToken, bodyToken);
//
//                return observableToken.subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .flatMap(new Func1<String, Observable<ResponseData<T>>>() {
//                            @Override
//                            public Observable<ResponseData<T>> call(String strResponse) {
//                                Log.e("strResponse>>>>>>>>>>>", strResponse);
//                                //创建对象,承载数据.
//                                ResponseData<T> resultResponseData = new ResponseData();
//                                //解密返回结果
//                                if (null != strResponse) {
//                                    try {
//                                        JSONObject object = new JSONObject(strResponse);
//                                        if (object.getInt("code") == SUCCESS) {
//                                            if (object.has("data")) {
//                                                String str = object.getString("data");
//                                                LoginBean token = new Gson().fromJson(str, LoginBean.class);
//                                                MySharedPreference.putString(MySharedPreferenceKey.LoginKey.TOKEN, token.getToken());
//                                                MySharedPreference.putLong(MySharedPreferenceKey.LoginKey.TOKEN_TIME, System.currentTimeMillis());
//                                                MySharedPreference.putInt(MySharedPreferenceKey.LoginKey.TOKEN_EXPIRES_TIME, token.getToken_expires_in());
//                                                MySharedPreference.putInt(MySharedPreferenceKey.LoginKey.TIKEN_EXPIRES_TIME, token.getTiken_expires_in());
//
//                                                //重新请求原来的接口
//                                                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
//                                                String jsonParam = gson.toJson(params);
//                                                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonParam);
//                                                Log.e("jsonParam>>>>>>>>>>>", body.toString() + "=====" + jsonParam);
//                                                String Authorization = "Bearer "+MySharedPreference.getString(MySharedPreferenceKey.LoginKey.TOKEN);
//                                                //Rx被观测者
//                                                final Observable<String> observable = appService.loadData(url,Authorization, body);
//                                                return observable.subscribeOn(Schedulers.io())
//                                                        .observeOn(AndroidSchedulers.mainThread())
//                                                        .flatMap(new Func1<String, Observable<ResponseData<T>>>() {
//                                                            @Override
//                                                            public Observable<ResponseData<T>> call(String strResponse) {
//
//                                                                Log.e("strResponse>>>>>>>>>>>", strResponse);
//                                                                //创建对象,承载数据.
//                                                                ResponseData<T> resultResponseData = new ResponseData();
//                                                                //解密返回结果
//                                                                if (null != strResponse) {
//                                                                    try {
//                                                                        JSONObject object = new JSONObject(strResponse);
//                                                                        if (object.has("code")) {
//                                                                            resultResponseData.setCode(object.getInt("code"));
//                                                                        }
//                                                                        if (resultResponseData.getCode() == ResponseCode.INVALID_TOKEN) {
////                                                                            showQuitDialog(context);
//                                                                        }
//                                                                        if (object.has("msg")) {
//                                                                            resultResponseData.setMsg(object.getString("msg"));
//                                                                        }
//                                                                        if (object.has("data")) {
//                                                                            String str = object.getString("data");
//                                                                            T t = new Gson().fromJson(str, typeToken);
//                                                                            resultResponseData.setData(t);
//                                                                        }
//                                                                    } catch (JSONException e) {
//                                                                        e.printStackTrace();
//                                                                    }
//                                                                }
//
//                                                                Log.e(typeToken + ">>>>>>>>>>>", new Gson().toJson(resultResponseData));
//                                                                return Observable.just(resultResponseData);
//                                                            }
//                                                        })
//                                                        //错误处理，防止错误返回导致数据链断裂
//                                                        .onErrorReturn(new Func1<Throwable, ResponseData<T>>() {
//                                                                           @Override
//                                                                           public ResponseData<T> call(Throwable throwable) {
//                                                                               throwable.printStackTrace();
//                                                                               ResponseData<T> errorData = new ResponseData<>();
//                                                                               if (throwable.getMessage().contains("after 10000ms"))
//                                                                                   errorData.setCode(ResponseCode.REQUEST_ERROR_OVERSION);
//                                                                               else
//                                                                                   errorData.setCode(ResponseCode.REQUEST_ERROR);
//                                                                               errorData.setData(null);
//                                                                               errorData.setMsg(throwable.getMessage());
//                                                                               return errorData;
//                                                                           }
//                                                                       }
//                                                        );
//                                            }
//                                        }else{  //续约令牌失败，弹出被提退框
//                                            showQuitDialog(context);
//                                        }
//
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                                return Observable.just(resultResponseData);
//                            }
//                        })//错误处理，防止错误返回导致数据链断裂
//                        .onErrorReturn(new Func1<Throwable, ResponseData<T>>() {
//                                           @Override
//                                           public ResponseData<T> call(Throwable throwable) {
//                                               throwable.printStackTrace();
//                                               ResponseData<T> errorData = new ResponseData<>();
//                                               if (throwable.getMessage().contains("after 10000ms"))
//                                                   errorData.setCode(ResponseCode.REQUEST_ERROR_OVERSION);
//                                               else
//                                                   errorData.setCode(ResponseCode.REQUEST_ERROR);
//                                               errorData.setData(null);
//                                               errorData.setMsg(throwable.getMessage());
//                                               return errorData;
//                                           }
//                                       }
//                        );
//            } else {
            //转换为json字符串
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            String jsonParam = gson.toJson(params);
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonParam);
            Log.e("jsonParam>>>>>>>>>>>", body.toString() + "=====" + jsonParam);
            //Rx被观测者
            final Observable<String> observable = appService.loadData(url,
                    header.get("Authorization").toString(),
                    body);

            return observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap(new Func1<String, Observable<ResponseData<T>>>() {
                        @Override
                        public Observable<ResponseData<T>> call(String strResponse) {

                            Log.e("strResponse>>>>>>>>>>>", strResponse);
                            //创建对象,承载数据.
                            ResponseData<T> resultResponseData = new ResponseData();
                            //解密返回结果
                            if (null != strResponse) {
                                try {
                                    JSONObject object = new JSONObject(strResponse);
                                    if (object.has("code")) {
                                        resultResponseData.setCode(object.getInt("code"));
                                    }
                                    if (resultResponseData.getCode() == ResponseCode.ACCOUNT_QUIT) {
                                        showQuitDialog(context, R.string.quit_info);
                                    }
                                    if (resultResponseData.getCode() == ResponseCode.INVALID_TOKEN ||
                                            resultResponseData.getCode() == ResponseCode.INVALID_TIKEN) {
                                        //续约token
                                        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
                                        data.put("tiken", MySharedPreference.getString(MySharedPreferenceKey.LoginKey.TIKEN));
                                        Gson gsonToken = new GsonBuilder().disableHtmlEscaping().create();
                                        String jsonParamToken = gsonToken.toJson(data);
                                        RequestBody bodyToken = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonParamToken);
                                        //Rx被观测者
                                        Observable<String> observableToken = appService.loadData(Consts.renewalToken, bodyToken);

                                        return observableToken.subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .flatMap(new Func1<String, Observable<ResponseData<T>>>() {
                                                    @Override
                                                    public Observable<ResponseData<T>> call(String strResponse) {
                                                        if (DebugUtil.isApkInDebug(context)) {
                                                            Log.e("strResponse>>>>>>>>>>>", strResponse);
                                                        }
                                                        //创建对象,承载数据.
                                                        ResponseData<T> resultResponseData = new ResponseData();
                                                        //解密返回结果
                                                        if (null != strResponse) {
                                                            try {
                                                                JSONObject object = new JSONObject(strResponse);
                                                                if (object.getInt("code") == SUCCESS) {
                                                                    if (object.has("data")) {
                                                                        String str = object.getString("data");
                                                                        LoginBean token = new Gson().fromJson(str, LoginBean.class);
                                                                        MySharedPreference.putString(MySharedPreferenceKey.LoginKey.TOKEN, token.getToken());
                                                                        MySharedPreference.putLong(MySharedPreferenceKey.LoginKey.TOKEN_TIME, System.currentTimeMillis());
                                                                        MySharedPreference.putInt(MySharedPreferenceKey.LoginKey.TOKEN_EXPIRES_TIME, token.getToken_expires_in());
                                                                        MySharedPreference.putInt(MySharedPreferenceKey.LoginKey.TIKEN_EXPIRES_TIME, token.getTiken_expires_in());

                                                                        //重新请求原来的接口
                                                                        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                                                                        String jsonParam = gson.toJson(params);
                                                                        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonParam);
                                                                        if (DebugUtil.isApkInDebug(context)) {
                                                                            Log.e("jsonParam>>>>>>>>>>>", body.toString() + "=====" + jsonParam);
                                                                        }
                                                                        String Authorization = "Bearer " + MySharedPreference.getString(MySharedPreferenceKey.LoginKey.TOKEN);
                                                                        //Rx被观测者
                                                                        final Observable<String> observable = appService.loadData(url, Authorization, body);
                                                                        return observable.subscribeOn(Schedulers.io())
                                                                                .observeOn(AndroidSchedulers.mainThread())
                                                                                .flatMap(new Func1<String, Observable<ResponseData<T>>>() {
                                                                                    @Override
                                                                                    public Observable<ResponseData<T>> call(String strResponse) {

                                                                                        Log.e("strResponse>>>>>>>>>>>", strResponse);
                                                                                        //创建对象,承载数据.
                                                                                        ResponseData<T> resultResponseData = new ResponseData();
                                                                                        //解密返回结果
                                                                                        if (null != strResponse) {
                                                                                            try {
                                                                                                JSONObject object = new JSONObject(strResponse);
                                                                                                if (object.has("code")) {
                                                                                                    resultResponseData.setCode(object.getInt("code"));
                                                                                                }
//                                                                                                    if (resultResponseData.getCode() == ResponseCode.INVALID_TOKEN) {
//                                                                                                    }
                                                                                                if (object.has("msg")) {
                                                                                                    resultResponseData.setMsg(object.getString("msg"));
                                                                                                }
                                                                                                if (object.has("data")) {
                                                                                                    String str = object.getString("data");
                                                                                                    T t = new Gson().fromJson(str, typeToken);
                                                                                                    resultResponseData.setData(t);
                                                                                                }
                                                                                            } catch (JSONException e) {
                                                                                                e.printStackTrace();
                                                                                            }
                                                                                        }

//                                                                                        Log.e(typeToken + ">>>>>>>>>>>", new Gson().toJson(resultResponseData));
                                                                                        return Observable.just(resultResponseData);
                                                                                    }
                                                                                })
                                                                                //错误处理，防止错误返回导致数据链断裂
                                                                                .onErrorReturn(new Func1<Throwable, ResponseData<T>>() {
                                                                                                   @Override
                                                                                                   public ResponseData<T> call(Throwable throwable) {
                                                                                                       throwable.printStackTrace();
                                                                                                       ResponseData<T> errorData = new ResponseData<>();
                                                                                                       if (throwable.getMessage().contains("after 10000ms"))
                                                                                                           errorData.setCode(ResponseCode.REQUEST_ERROR_OVERSION);
                                                                                                       else
                                                                                                           errorData.setCode(ResponseCode.REQUEST_ERROR);
                                                                                                       errorData.setData(null);
                                                                                                       errorData.setMsg(throwable.getMessage());
                                                                                                       return errorData;
                                                                                                   }
                                                                                               }
                                                                                );

                                                                    }
                                                                } else {  //续约令牌失败，弹出被提退框
                                                                    showTikenDialog(context, R.string.alert_info);
                                                                }

                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                        return Observable.just(resultResponseData);
                                                    }
                                                })//错误处理，防止错误返回导致数据链断裂
                                                .onErrorReturn(new Func1<Throwable, ResponseData<T>>() {
                                                                   @Override
                                                                   public ResponseData<T> call(Throwable throwable) {
                                                                       throwable.printStackTrace();
                                                                       ResponseData<T> errorData = new ResponseData<>();
                                                                       if (throwable.getMessage().contains("after 10000ms"))
                                                                           errorData.setCode(ResponseCode.REQUEST_ERROR_OVERSION);
                                                                       else
                                                                           errorData.setCode(ResponseCode.REQUEST_ERROR);
                                                                       errorData.setData(null);
                                                                       errorData.setMsg(throwable.getMessage());
                                                                       return errorData;
                                                                   }
                                                               }
                                                );
                                    }
                                    if (object.has("msg")) {

                                        resultResponseData.setMsg(object.getString("msg"));
                                    }
                                    if (object.has("data")) {
                                        String str = object.getString("data");
                                        T t = new Gson().fromJson(str, typeToken);
                                        resultResponseData.setData(t);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

//                            Log.e(typeToken + ">>>>>>>>>>>", new Gson().toJson(resultResponseData));
                            return Observable.just(resultResponseData);
                        }
                    })
                    //错误处理，防止错误返回导致数据链断裂
                    .onErrorReturn(new Func1<Throwable, ResponseData<T>>() {
                                       @Override
                                       public ResponseData<T> call(Throwable throwable) {
                                           throwable.printStackTrace();
                                           ResponseData<T> errorData = new ResponseData<>();
                                           if (throwable.getMessage().contains("after 10000ms"))
                                               errorData.setCode(ResponseCode.REQUEST_ERROR_OVERSION);
                                           else
                                               errorData.setCode(ResponseCode.REQUEST_ERROR);
                                           errorData.setData(null);
                                           errorData.setMsg(throwable.getMessage());
                                           return errorData;
                                       }
                                   }
                    );
//            }


        } else {
            showNetToast(context);
            ResponseData<T> errorData = new ResponseData<>();
            errorData.setCode(ResponseCode.REQUEST_ERROR);
            errorData.setMsg("请检查网络设置");
            errorData.setData(null);
            return Observable.just(errorData);
        }

    }

    private void showNetToast(Context context) {
        TUtils.show(context, "请检查网络设置");
    }


    /**
     * 判断网络是否连接
     *
     * @param context 上下文
     * @return 返回是否网络连接
     */
    private boolean isConnected(Context context) {
        boolean bisConnFlag = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo network = connectivityManager.getActiveNetworkInfo();
        if (network != null) {
            bisConnFlag = connectivityManager.getActiveNetworkInfo()
                    .isAvailable();
        }
        return bisConnFlag;
    }

    TipDialog quitDialog;

    private void showQuitDialog(Context context, int infoRes) {

        if (quitDialog == null) {
            quitDialog = new TipDialog(context);
            quitDialog.setMessage(context.getResources().getString(infoRes))
                    .setTitle(context.getResources().getString(R.string.alert))
                    .setSingle(true).setOnClickBottomListener(new TipDialog.OnClickBottomListener() {
                @Override
                public void onPositiveClick() {
                    quitDialog.dismiss();
                    EventMsg event = new EventMsg();
                    event.setMsgTag(EventMsg.MSG_EVENT_QUIT);
                    EventBus.getDefault().post(event);
                }

                @Override
                public void onNegtiveClick() {
                    quitDialog.dismiss();
                }
            });
        }
        MySharedPreference.putBoolean(MySharedPreferenceKey.LoginKey.LOGOUT, true);
        quitDialog.show();
    }

    /**
     * tiken失效
     *
     * @param context
     * @param infoRes
     */
    private void showTikenDialog(Context context, int infoRes) {

        final TipDialog dialog = new TipDialog(context);
        dialog.setMessage(context.getResources().getString(infoRes))
                .setTitle(context.getResources().getString(R.string.alert))
                .setSingle(true).setOnClickBottomListener(new TipDialog.OnClickBottomListener() {
            @Override
            public void onPositiveClick() {
                dialog.dismiss();
                EventMsg event = new EventMsg();
                event.setMsgTag(EventMsg.MSG_EVENT_QUIT);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onNegtiveClick() {
                dialog.dismiss();
            }
        }).show();
    }

    /**
     * @return
     */
    private boolean checkTokenTimeout() {
        long tokenTime = MySharedPreference.getLong(MySharedPreferenceKey.LoginKey.TOKEN_TIME, 0);
        int tokenExpiresTime = MySharedPreference.getInt(MySharedPreferenceKey.LoginKey.TOKEN_EXPIRES_TIME, 0);

        long time = System.currentTimeMillis() - tokenTime;
        if (time < (tokenExpiresTime == 0 ? 5 * 60 * 1000 : tokenExpiresTime * 1000)) {
            return false;
        }
        return true;
    }


    /**
     * 获取设备列表
     *
     * @param url
     * @param listener
     * @param token
     */
    public void getData(String url, final ResponseListener listener, String token) {
        Call<String> call = appService.getDeviceList(url, token);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i(TAG, "成功:" + response.body());
                if (response.code() == 200) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onSuccess(null);
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i(TAG, "失败");
                listener.onFailed(t);
            }
        });
    }

    public void getDataByPostMethod(String url, String token, HashMap<String, Object> params, final ResponseListener listener) {

        JSONArray array = (JSONArray) params.get("channels");
        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("channels", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "url:" + url);
        Log.i(TAG, "参数:" + jsonParam);
        Log.i(TAG, "token:" + token);


        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonParam));
        Call<String> call = appService.getDataByPostMethod(url, token, body);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {


                Log.i(TAG, "post result:" + response.body() + " code:" + response.code() + " message:" + response.message());

                if (response.code() == 200) {
                    listener.onSuccess(response.body());
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                listener.onFailed(t);
                Log.i(TAG, "post failed:" + t.toString());
            }
        });

    }

    /**
     * 获取Token
     * @param url
     * @param params 请求体参数(获取token要用到的ak和sk)
     * @param listener
     */
    public void getTokenByPostMethod(String url, HashMap<String, Object> params, final ResponseListener listener) {

        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String jsonParam = gson.toJson(params);
        Log.i(TAG, "jsonParam:" + jsonParam);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonParam);

        Call<String> call = appService.getTokenByPostMethod(url, body);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                Log.i(TAG, "post token result:" + response.body() + " code:" + response.code() + " message:" + response.message());
                if (response.code() == 200) {
                    //response.body()即为请求到的数据(里面含token信息)
                    listener.onSuccess(response.body());
                } else {
                    listener.onSuccess(null);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                listener.onFailed(t);
                Log.i(TAG, "post failed:" + t.toString());
            }
        });

    }

    /**
     * 获取国标设备的录像列表
     *
     * @param url
     * @param token
     * @param start
     * @param end
     * @param limit
     * @param offset
     * @param type
     * @param listener
     */
    public void getGB28181RecordList(String url, String token, String start, String end, int limit, int offset, String type, boolean cloud, final ResponseListener listener) {

        HashMap<String, String> map = new HashMap<>();
        map.put("start_time", start);
        map.put("end_time", end);
        map.put("offset", String.valueOf(offset));
        map.put("limit", String.valueOf(limit));
        if (cloud) {
            map.put("record_type", type);
        } else {
            map.put("stream_type", type);
        }

        Call<String> call = appService.GetData(url, token, map);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i(TAG, "成功:" + response.body());
                if (response.code() == 200) {
                    listener.onSuccess(response.body());
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i(TAG, "失败");
                listener.onFailed(t);
            }
        });
    }


    /**
     * 获取国标的回放地址
     *
     * @param url
     * @param token
     * @param start
     * @param end
     * @param playback_protocol
     * @param listener
     */
    public void getGb2881VodUrl(String url, String token, String start, String end, String playback_protocol, final ResponseListener listener) {

        HashMap<String, String> map = new HashMap<>();
        map.put("start_time", start);
        map.put("end_time", end);
        map.put("playback_protocol", playback_protocol);

        Call<String> call = appService.GetData(url, token, map);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i(TAG, "成功:" + response.body());
                if (response.code() == 200) {
                    listener.onSuccess(response.body());
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i(TAG, "失败");
                listener.onFailed(t);
            }
        });
    }

}
