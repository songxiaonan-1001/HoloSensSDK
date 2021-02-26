package com.huawei.holosens;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.huawei.holobase.Consts;
import com.huawei.holobasic.consts.MySharedPreferenceKey;
import com.huawei.holobasic.utils.MySharedPreference;
import com.huawei.holosens.adapter.DeviceListAdapter;
import com.huawei.holosens.adapter.DeviceResponseBean;
import com.huawei.holosens.base.BaseFragment;
import com.huawei.holosens.bean.TokenBean;
import com.huawei.holosens.utils.ToastUtils;
import com.huawei.net.retrofit.impl.AppImpl;
import com.huawei.net.retrofit.impl.ResponseListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.view.View.GONE;
import static com.huawei.holobase.Consts.TAG;

public class DeviceListFragment extends BaseFragment {
//    private String TAG = "DeviceListFragment";

    private Button mGetToken;
    private RecyclerView recyclerView ;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private List<DeviceResponseBean.DevicesBean> deviceLists = new ArrayList<>();
    private DeviceListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new01_device_list, null);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new DeviceListAdapter(R.layout.item_device_list, deviceLists);

        mGetToken = view.findViewById(R.id.get_token);
        mGetToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadToken();
            }
        });

        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                DeviceResponseBean.DevicesBean bean = (DeviceResponseBean.DevicesBean) adapter.getItem(position);
                String deviceId = bean.getDevice_id();
                String deviceType = bean.getDevice_type();
                Intent intent = new Intent(getActivity(), ChannelListActivity.class);
                intent.putExtra("deviceId", deviceId);
                intent.putExtra("deviceType", deviceType);
                getActivity().startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData(MySharedPreference.getString(MySharedPreferenceKey.LoginKey.TOKEN));
    }

    /**
     * 获取token
     */
    private void loadToken() {

        ResponseListener listener = new ResponseListener() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "loadToken result:" + result);
                if (!TextUtils.isEmpty(result)) {
                    TokenBean token = new Gson().fromJson(result, TokenBean.class);
                    MySharedPreference.putString(MySharedPreferenceKey.LoginKey.TOKEN, token.getAccess_token());
                    loadData(token.getAccess_token());
                    mGetToken.setVisibility(GONE);
                }else{
                    ToastUtils.show(mActivity, "获取token失败");
                }
            }

            @Override
            public void onFailed(Throwable throwable) {

            }
        };
        HashMap<String, Object> params = new HashMap<>();
        params.put("ak", Consts.ak);
        params.put("sk", Consts.sk);
        String url = Consts.gainToken.replace("{user_id}", Consts.userId);
        AppImpl.getInstance(getActivity()).getTokenByPostMethod(url, params, listener);

    }

    /**
     * 获取设备列表
     * @param token
     */
    private void loadData(String token) {
        Log.i(TAG, "loadData token:" + token);
        //获取设备列表的数据
        ResponseListener listener = new ResponseListener() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "loadData result:" + result);
                if(null != result) {
                    mGetToken.setVisibility(GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    Gson gson = new Gson();
                    DeviceResponseBean bean = gson.fromJson(result, DeviceResponseBean.class);
                    deviceLists = bean.getDevices();
                    adapter.setNewData(deviceLists);
                    adapter.notifyDataSetChanged();
                }else{
                    mGetToken.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(GONE);
                }
            }

            @Override
            public void onFailed(Throwable throwable) {
                loadToken();
            }
        };

        String path = Consts.deviceList.replace("{user_id}", Consts.userId);
        String url = path + "?limit=1000&&offset=0";
        AppImpl.getInstance(getActivity()).getData(url, listener, token);
    }
}
