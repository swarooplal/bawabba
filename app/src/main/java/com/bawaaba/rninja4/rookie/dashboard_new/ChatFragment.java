package com.bawaaba.rninja4.rookie.dashboard_new;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.ChatFunction.InboxFragment;
import com.bawaaba.rninja4.rookie.activity.ChatFunction.views.CustomViewPager;
import com.bawaaba.rninja4.rookie.activity.adapters.ChatviewPagerAdapter;
import com.bawaaba.rninja4.rookie.helper.SQLiteHandler;
import com.bawaaba.rninja4.rookie.helper.SessionManager;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;

public class ChatFragment extends Fragment {

    private CustomViewPager viewPager;
    private TabLayout tabLayout;
    private SQLiteHandler db;
    private SessionManager session;
    String inbox_incount="";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.frag_dash_chat,container,false);
        initViews(v);
        init();
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            ObjectFactory.getInstance().getAppPreference(getContext()).saveNewMessageArrived(false);
            //unregisterQbChatListeners();
        } catch (Exception e) {
        }
    }

    private void initViews(View v) {
        viewPager = v.findViewById(R.id.tc_viewpager);
        tabLayout = v.findViewById(R.id.tablayout_tc);
    }


    private void init() {
        ObjectFactory.getInstance().getAppPreference(getContext()).saveNewMessageArrived(false);
        ChatviewPagerAdapter pagerAdapter = new ChatviewPagerAdapter(getChildFragmentManager());
        pagerAdapter.addFrag(new com.bawaaba.rninja4.rookie.activity.ChatFunction.ChatFragment(), "Chat");
        pagerAdapter.addFrag(new InboxFragment(), "Inbox");
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setTabMode(TabLayout.GRAVITY_CENTER);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.bg_main));
        viewPager.setPagingEnabled(false);
    }
}
