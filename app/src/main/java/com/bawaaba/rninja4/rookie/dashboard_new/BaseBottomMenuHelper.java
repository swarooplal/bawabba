package com.bawaaba.rninja4.rookie.dashboard_new;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ashokvarma.bottomnavigation.TextBadgeItem;
import com.bawaaba.rninja4.rookie.MainActivity;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.ChatFunction.ChatActivity;
import com.bawaaba.rninja4.rookie.activity.LoginActivity;
import com.bawaaba.rninja4.rookie.activity.ProfileView;
import com.bawaaba.rninja4.rookie.activity.SearchActivity;
import com.bawaaba.rninja4.rookie.helper.SQLiteHandler;
import com.bawaaba.rninja4.rookie.helper.SessionManager;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.utils.AppPreference;
import com.bawaaba.rninja4.rookie.utils.Utils;

import java.util.HashMap;

public class BaseBottomMenuHelper {
    private TextBadgeItem textBadgeItem;
    private SQLiteHandler db;
    private SessionManager session;
//    private String className;
    private BaseBottomHelperActivity act;
    private BottomNavigationBar bottomNavigationView;
//    private String db_id;
    private int selectedPosition=-1;
    private final int tabCount=4;

    private static final int REQUEST_LOGIN = 5200;

    public BaseBottomMenuHelper(@NonNull BaseBottomHelperActivity activity,int currentPosition) {
        this.act = activity;
//        className = activity.getClass().getName();
        db = new SQLiteHandler(activity.getApplicationContext());
        session = new SessionManager(activity.getApplicationContext());
        textBadgeItem = Utils.getTextBadge();

        bottomNavigationView = activity.findViewById(R.id.bottomMenu);

        changePosition(currentPosition);
    }

    public int getTabCount() {
        return tabCount;
    }


    //Please call this after calling changeFragment  Otherwise backstack will not work properly
    public void changePosition(int position) {
        /*this.className=fragmentName;
        if (HomeFragment.class.getName().equals(className)) {
            selectedPosition = 0;
        } else if (SearchActivity.class.getName().equals(className)) {
            selectedPosition = 1;
        } else if (ChatActivity.class.getName().equals(className)) {
            selectedPosition = 2;
        } else if (ProfileView.class.getName().equals(className)) {
            selectedPosition = 3;
        }*/
        if(position>-1 && position<tabCount) {
            selectedPosition = position;
            initializeTabs();
        }


    }

    private void initializeTabs() {
        if (bottomNavigationView != null) {
            bottomNavigationView.clearAll();
            bottomNavigationView.setFirstSelectedPosition(selectedPosition);
            ObjectFactory.getInstance().getAppPreference(act.getApplicationContext()).saveCurrentActivity(act.getClass().getSimpleName());
            bottomNavigationView
                    .addItem(new BottomNavigationItem(R.drawable.ic_home1, "Home").setActiveColorResource(R.color.bottomnavigation))
                    .addItem(new BottomNavigationItem(R.drawable.ic_search1, "Search").setActiveColorResource(R.color.bottomnavigation))
                    .addItem(new BottomNavigationItem(R.drawable.ic_inbox1, "Inbox").setBadgeItem(textBadgeItem).setActiveColorResource(R.color.bottomnavigation))
                    .addItem(new BottomNavigationItem(R.drawable.ic_profile, "Profile").setActiveColorResource(R.color.bottomnavigation))
                    .setFirstSelectedPosition(selectedPosition)
                    .initialise();
//            setFirstSelectedPosition();

            bottomNavigationView.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
                @Override
                public void onTabSelected(int position) {
                    BaseBottomMenuHelper.this.onTabSelected(position);
                }

                @Override
                public void onTabUnselected(int position) {

                }

                @Override
                public void onTabReselected(int position) {

                }
            });
        }
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void onTabSelected(int position) {
        try {

            HashMap<String, String> user = db.getUserDetails();
            String db_id = user.get("uid");
            switch (position) {
                case 0: if(selectedPosition!=0) {
                    act.changeFragment(new HomeFragment(),true);
                    changePosition(position);
                }

                    break;
                case 1:
                    if(selectedPosition!=1) {
                        act.changeFragment(new SearchFragment(),true);
                        changePosition(position);
                    }
                    break;

                case 2:
                    if(selectedPosition!=2) {
                        if (session.isLoggedIn() && db_id != null) {
                            act.changeFragment(new ChatFragment(), true);
                            changePosition(position);
                        } else {
                            changePosition(selectedPosition);
                            Intent intent=new Intent(act.getApplicationContext(),LoginActivity.class).putExtra(LoginActivity.EXTRA_ONLY_NEED_RESULT,true);
                            act.startActivityForResult(intent,REQUEST_LOGIN+position);
                        }
                    }
                    break;

                case 3:
                    if(selectedPosition!=3) {
                        if (session.isLoggedIn() && db_id != null) {
                            AppPreference appPreference=ObjectFactory.getInstance().getAppPreference(act);
                            act.changeFragment(ProfileViewFragment.newInstance(appPreference.getUserId(),appPreference.getUserName()), true);
                            changePosition(position);
                        } else {
                            changePosition(selectedPosition);
                            Intent intent=new Intent(act.getApplicationContext(),LoginActivity.class).putExtra(LoginActivity.EXTRA_ONLY_NEED_RESULT,true);
                            act.startActivityForResult(intent,REQUEST_LOGIN+position);
                        }
                    }
                    break;
//                    Toast.makeText(act, "Not Implemented", Toast.LENGTH_SHORT).show();
//                    changePosition(selectedPosition);
//                    return;
                   /* if (!ProfileView.class.getName().equals(className)) {
                        if (session.isLoggedIn() && db_id != null) {
                            cls = ProfileView.class;
//                                    Intent to_profile = new Intent(getApplicationContext(), ProfileView.class);
//                                    startActivity(to_profile);
//                                    finish();
                        } else {
                            requestCode = REQUEST_LOGIN + position;
                            finish = false;
                            cls = LoginActivity.class;
                            intent=new Intent(act.getApplicationContext(),LoginActivity.class).putExtra(LoginActivity.EXTRA_ONLY_NEED_RESULT,true);
                                    *//*Intent to_login = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(to_login);
                                    finish();*//*
                        }
                    }*/
                            /*if (session.isLoggedIn() && db_id != null) {
                                Intent to_profile = new Intent(act.getApplicationContext(), ProfileView.class);
                                act.startActivity(to_profile);
                                //finish();
                            } else {
                                Intent to_login = new Intent(act.getApplicationContext(), LoginActivity.class);
                                act.startActivityForResult(to_login,REQUEST_LOGIN+position);
                              //  finish();
                            }
                    break;*/
                default:
                    break;

            }
           /* if (cls != null) {
                if (intent == null) {
                    intent = new Intent(act.getApplicationContext(), cls);
                }
                intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP);
                if (requestCode < 1)
                    act.startActivity(intent);
                else
                    act.startActivityForResult(intent, requestCode);
                if (finish) {
                    act.finish();
                    unbind();
                }
            }*/
        } catch (Exception ignored) {
        }
    }

    public SessionManager getSession() {
        return session;
    }

    public SQLiteHandler getDb() {
        return db;
    }

    public TextBadgeItem getTextBadgeItem() {
        return textBadgeItem;
    }

    public void unbind() {
        act = null;
        if(bottomNavigationView!=null)
            bottomNavigationView.clearAll();
        bottomNavigationView = null;
//        session=null;
//        textBadgeItem=null;
//        db=null;
    }

    public void isMessageArrived() {
        try {
            if(act==null)
                return;
            boolean isMessageArrived = ObjectFactory.getInstance().getAppPreference(act).isNewMessageArrived();
            if (isMessageArrived) {
                //showUnreadMessages();
                int total = ObjectFactory.getInstance().getAppPreference(act).getUnreadMessage();
                if (total > 0) {
                    textBadgeItem.setText("" + total);
                    textBadgeItem.show(false);
                } else {
                    hideText();
                }
            } else {
                hideText();
            }
        } catch (Exception e) {
        }
    }

    private void hideText() {
        try {
            textBadgeItem.setText("");
            textBadgeItem.hide();
        } catch (Exception e) {
        }
    }

    public void setFirstSelectedPosition() {
        if (bottomNavigationView != null) {
            initializeTabs();
        }
    }

    public void setFirstSelectedPosition(int position) {
        if (bottomNavigationView != null) {
            changePosition(position);
        }
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("Request","="+requestCode+"  "+(requestCode-REQUEST_LOGIN)+"   "+bottomNavigationView.getChildCount());
        if (act != null && (requestCode >= REQUEST_LOGIN || requestCode < tabCount+ REQUEST_LOGIN)) {
            HashMap<String, String> user = db.getUserDetails();
            String db_id = user.get("uid");
            if (session.isLoggedIn() && db_id != null)
            onTabSelected(requestCode - REQUEST_LOGIN);
            else
                setFirstSelectedPosition(selectedPosition);
            return true;
        }
        return false;
    }
}
