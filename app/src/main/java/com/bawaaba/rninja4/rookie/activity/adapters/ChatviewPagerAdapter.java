package com.bawaaba.rninja4.rookie.activity.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bawaaba.rninja4.rookie.activity.ChatFunction.ChatFragment;
import com.bawaaba.rninja4.rookie.activity.ChatFunction.InboxFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rninja4 on 10/17/17.
 */

public class ChatviewPagerAdapter  extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ChatviewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ChatFragment();
            case 1:
                return new InboxFragment();

            default:
                return new ChatFragment();
        }
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFrag(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    public void updateTitle(int position){

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
