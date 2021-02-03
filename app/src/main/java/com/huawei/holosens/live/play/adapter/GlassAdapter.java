package com.huawei.holosens.live.play.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.huawei.holosens.R;
import com.huawei.holosens.live.play.bean.Glass;
import com.huawei.holosens.live.play.bean.GlassType;
import com.huawei.holosens.live.play.glass.AddGlass;
import com.huawei.holosens.live.play.glass.C2GlassIpc;
import com.huawei.holosens.live.play.ui.WindowFragment;

import java.util.List;


/**
 * 玻璃Adapter
 *
 */

public class GlassAdapter extends RecyclerView.Adapter<RecyclerView
        .ViewHolder> {

    private List<Glass> mGlassList;
    private int mSelectedGlassNo;
    private Glass.Size mGlassSize;
    // 当前玻璃所在的窗户是否可以被用户看到
    private volatile boolean isVisibleToUser;
    private WindowFragment mWindow;
    private boolean mIsEdit;

    /**
     * @param window          窗户
     * @param glassSize       玻璃尺寸
     * @param selectedGlassNo 当前选中的玻璃号
     */
    public GlassAdapter(WindowFragment window, Glass.Size glassSize, int selectedGlassNo) {
        mWindow = window;
        mGlassSize = glassSize;
        mSelectedGlassNo = selectedGlassNo;
    }

    public void setEdit(boolean isEdit) {
        this.mIsEdit = isEdit;
    }

    public boolean isEdit() {
        return mIsEdit;
    }

    /**
     * 玻璃列表
     *
     * @param list 玻璃列表
     */
    public void updateGlassList(List<Glass> list) {
        mGlassList = list;
    }

    /**
     * 更新玻璃尺寸
     *
     * @param glassSize
     */
    public void updateGlassSize(Glass.Size glassSize) {
        mGlassSize = glassSize;
    }

    /**
     * 对用户可见
     *
     * @param visibleToUser
     */
    public void setVisibleToUser(boolean visibleToUser) {
        isVisibleToUser = visibleToUser;
    }

    public boolean isVisibleToUser() {
        return isVisibleToUser;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case GlassType.TYPE_GLASS_CLOUDSEE_V2_IPC:// 2.0协议_摄像机
                view = layoutInflater.inflate(R.layout.item_glass_play, parent, false);
                viewHolder = new C2GlassIpc(mWindow, view, mGlassSize, mSelectedGlassNo, isVisibleToUser(), isEdit());
                break;
            case GlassType.TYPE_EMPTY:// 空白页

                break;
            case GlassType.TYPE_PLUS:// "+"号玻璃
                view = layoutInflater.inflate(R.layout.item_glass_add, parent, false);
                viewHolder = new AddGlass(mWindow, view, mGlassSize);
                break;
            default:
                break;
        }

        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        int type = GlassType.TYPE_GLASS_CLOUDSEE_V1_IPC;
        //不判空容易抛空指针异常
        if(null != mGlassList
                && mGlassList.size() > 0
                && position < mGlassList.size()){
            if(null != mGlassList.get(position)){
                type = mGlassList.get(position).getType();
            }
        }
        return type;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        Glass glass = mGlassList.get(position);
        switch (viewType) {
            case GlassType.TYPE_GLASS_CLOUDSEE_V2_IPC:// 2.0协议_摄像机
                ((C2GlassIpc) holder).bindGlass(glass);
                break;
            case GlassType.TYPE_EMPTY:// 白纸
                // Empty
                break;
            case GlassType.TYPE_PLUS:// "+"号玻璃
                // Empty
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mGlassList == null ? 0 : mGlassList.size();
    }
}