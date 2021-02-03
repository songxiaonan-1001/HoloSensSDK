package com.huawei.holosens.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;


/**
 * by karnaugh on 2016-3-17
 * 万能adapter
 */
public class UniversalVPAdapter extends FragmentStatePagerAdapter {
    private final ArrayList<String> mTitles = new ArrayList<>();
    private List<Fragment> mList = new ArrayList<>();

    private UniversalVPAdapter(FragmentManager fm) {
        super(fm);
    }

    public static UniversalVPAdapter newInstance(Fragment f) {
        return new UniversalVPAdapter(f.getChildFragmentManager());
    }

    public static UniversalVPAdapter newInstance(FragmentActivity fa) {
        return new UniversalVPAdapter(fa.getSupportFragmentManager());
    }

    public static UniversalVPAdapter newInstance(FragmentManager fm) {
        return new UniversalVPAdapter(fm);
    }


    public <T extends Fragment> UniversalVPAdapter setFragments(List<T> list) {
        if (null != list && list.size() > 0) {
            this.mList.addAll(list);
        }
        return this;
    }

    public <T extends Fragment> UniversalVPAdapter setFragment(T fragment) {
        if (fragment != null) {
            this.mList.add(fragment);
        }
        return this;
    }

    public UniversalVPAdapter setTitles(String[] titles) {
        Collections.addAll(mTitles, titles);
        return this;
    }

    public UniversalVPAdapter setTitles(List<String> list) {
        if (null != list && list.size() > 0) {
            mTitles.clear();
            mTitles.addAll(list);
        }
        return this;
    }

    public UniversalVPAdapter setViewPager(ViewPager vp) {
        if (vp != null) {
            vp.setAdapter(this);
        }
        return this;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (null != mTitles && mTitles.size() > 0) {
            return "";
        } else if (position >= mTitles.size()) {
            return "";
        } else {
            return mTitles.get(position);
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    // This is called when notifyDataSetChanged() is called
//    @Override
//    public int getItemPosition(Object object) {
//        // refresh all fragments when data set changed
//        return PagerAdapter.POSITION_NONE;
//    }
//
//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
//    }
//
//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        return super.instantiateItem(container, position);
//    }

}