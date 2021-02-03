package com.huawei.holosens;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.huawei.holobase.Consts;
import com.huawei.holobase.bean.PlayBean;
import com.huawei.holobasic.consts.MySharedPreferenceKey;
import com.huawei.holobasic.utils.MySharedPreference;
import com.huawei.holosens.adapter.ChannelListAdapter;
import com.huawei.holosens.adapter.ChannelResponseBean;
import com.huawei.holosens.base.BaseFragment;
import com.huawei.holosens.commons.BundleKey;
import com.huawei.holosens.live.play.ui.JVMultiPlayActivity;
import com.huawei.net.retrofit.impl.AppImpl;
import com.huawei.net.retrofit.impl.ResponseListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChannelListFragment extends BaseFragment {

    private String deviceId;
    private String deviceType;
    private String play_bean_list;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        deviceId = bundle.getString("deviceId");
        deviceType = bundle.getString("deviceType");
    }

    private List<ChannelResponseBean.ChannelsBean> channelsBeanList = new ArrayList<>();
    private List<PlayBean> playBeans = new ArrayList<>();
    private ChannelListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new01_device_list, null);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ChannelListAdapter(R.layout.item_device_list, channelsBeanList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                playBeans.clear();
                //启动该通道的播放页面
                ChannelResponseBean.ChannelsBean bean = (ChannelResponseBean.ChannelsBean) adapter.getData().get(position);
                PlayBean playBean = new PlayBean(1, bean.getDevice_id(), bean.getChannel_id(), bean.getChannel_name(), bean.getAccess_protocol(), !bean.getChannel_state().equals("ONLINE")?0:1);
                playBeans.add(playBean);
                play_bean_list = new Gson().toJson(playBeans);
                Intent intent = new Intent(getActivity(), JVMultiPlayActivity.class);
                intent.putExtra(BundleKey.PLAY_BEAN_LIST, play_bean_list);
                intent.putExtra(BundleKey.SELECT_NO, position);
//
//                intent.putExtra("protocol", bean.getAccess_protocol());
//                intent.putExtra("deviceId", deviceId);
                getActivity().startActivity(intent);

            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
    }

    private void loadData() {
        //获取通道列表
        ResponseListener listener = new ResponseListener() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                ChannelResponseBean bean = gson.fromJson(result, ChannelResponseBean.class);
                channelsBeanList = bean.getChannels();
                adapter.setNewData(channelsBeanList);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailed(Throwable throwable) {

            }
        };
        String path = Consts.channelList.replace("{user_id}", Consts.userId);
        String url = path + "?device_id=" + deviceId+ "&&limit=1000&&offset=0";
        AppImpl.getInstance(getActivity()).getData(url, listener, MySharedPreference.getString(MySharedPreferenceKey.LoginKey.TOKEN));
    }
}
