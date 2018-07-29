package com.bawaaba.rninja4.rookie.dashboard_new;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class PagerAdapter extends FragmentPagerAdapter {
    private ArrayList<String> list=new ArrayList<>();
    public PagerAdapter(FragmentManager fm,List<String> list) {
        super(fm);
        if(list!=null)
            this.list.addAll(list);
    }

    @Override
    public Fragment getItem(int position) {
        return SkillViewFragment.newInstance(list.get(position));
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public ArrayList<String> getList() {
        return list;
    }
}
