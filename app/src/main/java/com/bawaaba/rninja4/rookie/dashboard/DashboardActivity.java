package com.bawaaba.rninja4.rookie.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;

import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.LoginActivity;
import com.bawaaba.rninja4.rookie.dashboard.fragments.FragmentHome;
import com.bawaaba.rninja4.rookie.dashboard.fragments.FragmentInbox;
import com.bawaaba.rninja4.rookie.dashboard.fragments.FragmentProfile;
import com.bawaaba.rninja4.rookie.dashboard.fragments.FragmentSearch;
import com.bawaaba.rninja4.rookie.helper.SQLiteHandler;
import com.bawaaba.rninja4.rookie.helper.SessionManager;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.model.MessageEvent;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

   public ViewPager vpMain;
    String temp = "";
    public static BottomBar bottomBar;
    private SQLiteHandler db;
    private SessionManager session;
    public ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dashboard);
        vpMain = (ViewPager) findViewById(R.id.vpMain);
        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        HashMap<String, String> user = db.getUserDetails();
        final String db_id = user.get("uid");
        if (session.isLoggedIn() && db_id !=null) {
            isMessageArrived();
            EventBus.getDefault().register(this);
        }

        // set up adaper for the bottom navigation menu
        setupViewPager(vpMain);

        vpMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomBar.setVerticalScrollbarPosition(position);
                bottomBar.selectTabAtPosition(position, false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(int tabId) {
                if (tabId == R.id.tab_home) {
                    vpMain.setCurrentItem(0);
                    temp = temp.replace("-0","") + "-0";
                } else if (tabId == R.id.tab_search) {
                    vpMain.setCurrentItem(1);
                    temp = temp.replace("-1","") + "-1";
                } else if (tabId == R.id.tab_inbox) {
                    vpMain.setCurrentItem(2);
                    temp = temp.replace("-2","") + "-2";
                    Log.d("selectedRe","inbox");
                }  else if (tabId == R.id.tab_account) {
                    vpMain.setCurrentItem(3);
                    temp = temp.replace("-3","") + "-3";
                    Log.d("selectedRe","profile");
                }
            }
        });

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_home) {
                    vpMain.setCurrentItem(0);
                    temp = temp.replace("-0","") + "-0";
                } else if (tabId == R.id.tab_search) {
                    vpMain.setCurrentItem(1);
                    temp = temp.replace("-1","") + "-1";
                } else if (tabId == R.id.tab_inbox) {
                    vpMain.setCurrentItem(2);
                    temp = temp.replace("-2","") + "-2";
                    Log.d("selected","inbo");
                    if (!session.isLoggedIn() && db_id == null) {
                        Intent to_login = new Intent(DashboardActivity.this, LoginActivity.class);
                        to_login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(to_login);
                        finish();
                    }
                } else if (tabId == R.id.tab_account) {
                    vpMain.setCurrentItem(3);
                    temp = temp.replace("-3","") + "-3";
                    Log.d("selected","prof");
                    if (!session.isLoggedIn() && db_id == null) {
                        Intent to_login = new Intent(DashboardActivity.this, LoginActivity.class);
                        to_login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(to_login);
                        finish();
                    }
                }
            }
        });

        vpMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }
    public void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new FragmentHome(), "home");
        adapter.addFragment(new FragmentSearch(), "search");
        adapter.addFragment(new FragmentInbox(), "inbox");
        adapter.addFragment(new FragmentProfile(), "profile");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed() {

        String[] words = temp.split("-");
        if(words.length<=2)
            if (vpMain.getCurrentItem() != 0) {
                temp = "";
                vpMain.setCurrentItem(0);
            }else
                finish();
        else {
            temp = temp.replace("-" + words[words.length - 1], "");
            int pos = vpMain.getCurrentItem();
            if (vpMain.getCurrentItem() == pos)
                pos = 2;
            else
                pos = 1;
            vpMain.setCurrentItem(Integer.parseInt(words[words.length - pos]));
        }
//        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
//            finish();
//        }
    }

    public int setNottificationCount(int count)
    {
        BottomBarTab nearby = bottomBar.getTabWithId(R.id.tab_inbox);
        nearby.setBadgeCount(count);
        return count;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    // quick blox count
    public void isMessageArrived() {
        try {
            boolean isMessageArrived = ObjectFactory.getInstance().getAppPreference(getApplicationContext()).isNewMessageArrived();
            if (isMessageArrived) {
                //showUnreadMessages();
                int total = ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUnreadMessage();
                if (total > 0) {
                    //  ((DashboardActivity)getActivity()).setNottificationCount(total);
                    setNottificationCount(total);
                } else {

                }
            } else {

            }
        } catch (Exception e) {
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event)
    {
        isMessageArrived();
    }

//    public void showFragment(Fragment fragment) {
//
//        String TAG = fragment.getClass().getSimpleName();
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//      //  fragmentTransaction.replace(R.id.realtabcontent, fragment, TAG);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commitAllowingStateLoss();
//    }

//    protected void backstackFragment() {
//        Log.d("Stack count", getSupportFragmentManager().getBackStackEntryCount() + "");
//        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
//            finish();
//        }
//        getSupportFragmentManager().popBackStack();
//        removeCurrentFragment();
//    }

//    private void removeCurrentFragment() {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        Fragment currentFrag = getSupportFragmentManager()
//                .findFragmentById(R.id.realtabcontent);
//
//        if (currentFrag != null) {
//            transaction.remove(currentFrag);
//        }
//        transaction.commitAllowingStateLoss();
//    }
}
