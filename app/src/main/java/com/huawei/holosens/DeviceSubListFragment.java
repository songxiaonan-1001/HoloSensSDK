package com.huawei.holosens;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.huawei.holobase.Consts;
import com.huawei.holobase.bean.PlayBean;
import com.huawei.holobasic.consts.MySharedPreferenceKey;
import com.huawei.holobasic.utils.MySharedPreference;
import com.huawei.holosens.adapter.ChannelResponseBean;
import com.huawei.holosens.adapter.ChannelSubListAdapter;
import com.huawei.holosens.adapter.DeviceListEditAdapter;
import com.huawei.holosens.adapter.DeviceResponseBean;
import com.huawei.holosens.base.BaseActivity;
import com.huawei.holosens.live.play.ui.JVMultiPlayActivity;
import com.huawei.holosens.view.TopBarLayout;
import com.huawei.net.retrofit.impl.AppImpl;
import com.huawei.net.retrofit.impl.ResponseListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DeviceSubListFragment extends Fragment implements DeviceListEditAdapter.DeviceSelectedListener, ChannelSubListAdapter.NvrDeviceChannelSelectedListener, View.OnClickListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private BaseActivity mActivity;
    private TopBarLayout topBar;
    private List<DeviceResponseBean.DevicesBean> deviceLists = new ArrayList<>();
    private DeviceListEditAdapter adapter;
    private TextView btn_live_play;
    private RecyclerView recyclerView;

    private List<ChannelResponseBean.ChannelsBean> mSelectChannels = new ArrayList<>();
    private List<ChannelResponseBean.ChannelsBean> alreadyAddChannels = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseActivity) context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new01_device_sub_list, null);

        topBar = view.findViewById(R.id.topBar);
        topBar.setTitle("可选设备列表");

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new DeviceListEditAdapter(R.layout.item_device_list_edit, deviceLists);
        adapter.setDeviceSelectedListener(this);
        recyclerView.setAdapter(adapter);
        btn_live_play = view.findViewById(R.id.btn_live_play);
        btn_live_play.setOnClickListener(this);
        btn_live_play.setEnabled(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_live_play: {
                goTOPlay();
                break;
            }
            case R.id.right_btn: {

                break;
            }
            default:
            break;
        }
    }

    private void loadData() {
        //获取设备列表的数据
        ResponseListener listener = new ResponseListener() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                DeviceResponseBean bean = gson.fromJson(result, DeviceResponseBean.class);
                deviceLists = bean.getDevices();
                adapter.setNewData(deviceLists);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(Throwable throwable) {

            }
        };

        String path = Consts.deviceList.replace("{user_id}", Consts.userId);
        String url = path + "?limit=1000&&offset=0";
        AppImpl.getInstance(getActivity()).getData(url, listener, MySharedPreference.getString(MySharedPreferenceKey.LoginKey.TOKEN));

    }

    boolean updated = false;
    public void update(){
        if(!updated){
            List<PlayBean> playList = ((JVMultiPlayActivity)mActivity).getPlayList();
            for(PlayBean bean :playList){
                ChannelResponseBean.ChannelsBean channel = new ChannelResponseBean.ChannelsBean();
                channel.setDevice_id(bean.getDeviceId());
                channel.setChannel_id(bean.getChannelID());
                alreadyAddChannels.add(channel);
            }
            updated = true;
        }

    }

    @Override
    public void onDeviceSelected(DeviceResponseBean.DevicesBean item) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.left_btn) {
                    recyclerView.removeAllViews();
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(adapter);
                    topBar.setTitle("可选设备列表");
                    topBar.getmLeftImage().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((JVMultiPlayActivity) mActivity).hideFragment();
                        }
                    });
                }

            }
        };
        topBar.setTopBar(R.mipmap.ic_login_back_normal, -1, "可选通道列表", listener);

        loadNvrChannel(item.getDevice_id());

        recyclerView.removeAllViews();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (channelListAdapter == null) {
            channelListAdapter = new ChannelSubListAdapter(R.layout.item_device_list_edit, channelsBeanList);

        }
        channelListAdapter.setNvrDeviceChannelSelectedListener(this);
        recyclerView.setAdapter(channelListAdapter);

    }


    List<ChannelResponseBean.ChannelsBean> channelsBeanList = new ArrayList<>();
    private ChannelSubListAdapter channelListAdapter;

    private void loadNvrChannel(String deviceId) {
        //获取通道列表
        ResponseListener listener = new ResponseListener() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                ChannelResponseBean bean = gson.fromJson(result, ChannelResponseBean.class);
                channelsBeanList = bean.getChannels();
                for(ChannelResponseBean.ChannelsBean channel : channelsBeanList){
                    if(checkChannelAdded(channel)) {
                        channel.setEnable(false);
                        channel.setSelected(true);
                    }
                    else {
                        channel.setEnable(true);
                        channel.setSelected(false);
                    }

                }
                channelListAdapter.setNewData(channelsBeanList);
                channelListAdapter.notifyDataSetChanged();
                //转为PlayBeanList
            }

            @Override
            public void onFailed(Throwable throwable) {

            }
        };
        String path = Consts.channelList.replace("{user_id}", Consts.userId);
        String url = path + "?device_id=" + deviceId+ "&&limit=1000&&offset=0";
        AppImpl.getInstance(getActivity()).getData(url, listener, MySharedPreference.getString(MySharedPreferenceKey.LoginKey.TOKEN));
    }

    @Override
    public void onNVRChannelSelected(ChannelResponseBean.ChannelsBean bean, boolean checked) {
        //nvr通道被选中的回调
        if (checked) {
            mSelectChannels.add(bean);
        } else {
            mSelectChannels.remove(bean);
        }
    }

    /**
     * 检查通道有没有被添加
     * @param cb
     * @return
     */
    private boolean checkChannelAdded(ChannelResponseBean.ChannelsBean cb){
        for(ChannelResponseBean.ChannelsBean channel : alreadyAddChannels){
            if(cb.getDevice_id().equals(channel.getDevice_id()) && cb.getChannel_id().equals(channel.getChannel_id())){
                return true;
            }
        }
        return false;
    }

    private void goTOPlay() {
        List<PlayBean> list = new ArrayList<>();
        for (ChannelResponseBean.ChannelsBean bean : mSelectChannels) {
            if(!checkChannelAdded(bean)) {
                PlayBean playBean = new PlayBean(1, bean.getDevice_id(), bean.getChannel_id(), bean.getChannel_name(), bean.getAccess_protocol(),!bean.getChannel_state().equals("ONLINE")?0:1);
                list.add(playBean);
                alreadyAddChannels.add(bean);
            }
        }
//        ((JVMultiPlayActivity) mActivity).getPlayList().addAll(list);
        ((JVMultiPlayActivity) mActivity).updateAddDeviceList(list);
    }
}
