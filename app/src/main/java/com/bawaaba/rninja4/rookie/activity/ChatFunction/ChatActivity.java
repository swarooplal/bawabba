package com.bawaaba.rninja4.rookie.activity.ChatFunction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
//import com.ashokvarma.bottomnavigation.bottomMenuHelper.getTextBadgeItem();
import com.bawaaba.rninja4.rookie.BaseBottomMenuHelper;
import com.bawaaba.rninja4.rookie.MainActivity;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.ChatFunction.views.CustomViewPager;
import com.bawaaba.rninja4.rookie.activity.ProfileView;
import com.bawaaba.rninja4.rookie.activity.SearchActivity;
import com.bawaaba.rninja4.rookie.activity.adapters.ChatviewPagerAdapter;
import com.bawaaba.rninja4.rookie.helper.SQLiteHandler;
import com.bawaaba.rninja4.rookie.helper.SessionManager;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.utils.BaseChatActivity;
import com.bawaaba.rninja4.rookie.utils.Utils;
import com.quickblox.chat.model.QBChatDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;


public class ChatActivity extends BaseChatActivity  {

    private CustomViewPager viewPager;
    private TabLayout tabLayout;
    private SQLiteHandler db;
    private SessionManager session;
    private String inbox_count;
//    private bottomMenuHelper.getTextBadgeItem() bottomMenuHelper.getTextBadgeItem();
    String inbox_incount="";
    private BaseBottomMenuHelper bottomMenuHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().hide();
        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());
//        bottomMenuHelper.getTextBadgeItem() = Utils.getTextBadge();
        HashMap<String, String> user = db.getUserDetails();
        final String db_id = user.get("uid");
        unread_notification();
        ObjectFactory.getInstance().getAppPreference(getApplicationContext()).saveNewMessageArrived(false);
        ObjectFactory.getInstance().getAppPreference(getApplicationContext()).saveCurrentActivity("ChatActivity");
        bottomMenuHelper=new BaseBottomMenuHelper(this);
        /*BottomNavigationBar bottomNavigationView = (BottomNavigationBar)
                findViewById(R.id.bottom_bar);

        bottomNavigationView
                .addItem(new BottomNavigationItem(R.drawable.ic_home1, "Home").setActiveColorResource(R.color.bottomnavigation))
                .addItem(new BottomNavigationItem(R.drawable.ic_search1, "Search").setActiveColorResource(R.color.bottomnavigation))
                .addItem(new BottomNavigationItem(R.drawable.ic_inbox1, "Inbox").setBadgeItem(bottomMenuHelper.getTextBadgeItem()).setActiveColorResource(R.color.bottomnavigation))
                .addItem(new BottomNavigationItem(R.drawable.ic_profile, "Profile").setActiveColorResource(R.color.bottomnavigation))
                .setFirstSelectedPosition(2)
                .initialise();

        bottomNavigationView.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                switch (position) {
                    case 0:
                        Intent to_main = new Intent(ChatActivity.this, MainActivity.class);
                        startActivity(to_main);
                        finish();
                        break;
                    case 1:
                        Intent to_search = new Intent(ChatActivity.this, SearchActivity.class);
                        startActivity(to_search);
                        finish();
                        break;
                    case 2:
                        Intent to_inbox = new Intent(ChatActivity.this, ChatActivity.class);
                        startActivity(to_inbox);
                        finish();
                        break;
                    case 3:
                        Intent to_profile = new Intent(ChatActivity.this, ProfileView.class);
                        startActivity(to_profile);
                        finish();
                        break;
                    default:
                        break;

                }
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });*/
/*
        allDialogsMessagesListener = new AllDialogsMessageListener();
        systemMessagesListener = new SystemMessagesListener();
        dialogsManager = new DialogsManager();
        registerQbChatListeners();
*/
        initialize();
    }


    private int getUnreadMsgCount(QBChatDialog chatDialog){
        Integer unreadMessageCount = chatDialog.getUnreadMessageCount();
        if (unreadMessageCount == null) {
            return 0;
        } else {
            return unreadMessageCount;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bottomMenuHelper.onActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }

    private void initialize() {

        viewPager = findViewById(R.id.tc_viewpager);
        tabLayout = findViewById(R.id.tablayout_tc);
        ChatviewPagerAdapter pagerAdapter = new ChatviewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFrag(new ChatFragment(), "Chat");
        pagerAdapter.addFrag(new InboxFragment(), "Inbox");
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setTabMode(TabLayout.GRAVITY_CENTER);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.bg_main));
        viewPager.setPagingEnabled(false);
    }

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ChatActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            ObjectFactory.getInstance().getAppPreference(getApplicationContext()).saveNewMessageArrived(false);
            //unregisterQbChatListeners();
        } catch (Exception e) {
        }
        bottomMenuHelper.unbind();
    }
    @Override
    public void onResume() {
        unread_notification();
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        unread_notification();
    }


    private void unread_notification() {
        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(getApplicationContext()).getApiService().notification("app-client",
                "123321",
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getLoginToken(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId());

        responseBodyCall.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        if (responseString != null) {
                            JSONObject jsonObject = null;
                            try {

                                jsonObject = new JSONObject(responseString);
                                inbox_incount = jsonObject.getString("count");
                                int count_not= Integer.parseInt(inbox_incount);
                                if (count_not!=0) {
                                    bottomMenuHelper.getTextBadgeItem().setText(count_not+"");
                                    bottomMenuHelper.getTextBadgeItem().show(true);
                                }
                                else
                                {
                                    bottomMenuHelper.getTextBadgeItem().setText("0");
                                    bottomMenuHelper.getTextBadgeItem().setBackgroundColor(Color.TRANSPARENT);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    dialog.dismiss();
//                    Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            }
        });
    }
/*
    private QBChatDialogMessageListener allDialogsMessagesListener;
    private SystemMessagesListener systemMessagesListener;
    private QBSystemMessagesManager systemMessagesManager;
    private QBIncomingMessagesManager incomingMessagesManager;
    private DialogsManager dialogsManager;

    private void registerQbChatListeners() {
        incomingMessagesManager = QBChatService.getInstance().getIncomingMessagesManager();
        systemMessagesManager = QBChatService.getInstance().getSystemMessagesManager();

        if (incomingMessagesManager != null) {
            incomingMessagesManager.addDialogMessageListener(allDialogsMessagesListener != null  ? allDialogsMessagesListener : new AllDialogsMessageListener());
        }

        if (systemMessagesManager != null) {
            systemMessagesManager.addSystemMessageListener(systemMessagesListener != null ? systemMessagesListener : new SystemMessagesListener());
        }

        dialogsManager.addManagingDialogsCallbackListener(this);
    }

    private void unregisterQbChatListeners() {
        if (incomingMessagesManager != null) {
            incomingMessagesManager.removeDialogMessageListrener(allDialogsMessagesListener);
        }

        if (systemMessagesManager != null) {
            systemMessagesManager.removeSystemMessageListener(systemMessagesListener);
        }

        dialogsManager.removeManagingDialogsCallbackListener(this);
    }

    private class SystemMessagesListener implements QBSystemMessageListener {
        @Override
        public void processMessage(final QBChatMessage qbChatMessage) {
            dialogsManager.onSystemMessageReceived(qbChatMessage);
        }

        @Override
        public void processError(QBChatException e, QBChatMessage qbChatMessage) {}
    }

    private class AllDialogsMessageListener extends QbChatDialogMessageListenerImp {
        @Override
        public void processMessage(final String dialogId, final QBChatMessage qbChatMessage, Integer senderId) {
            if (!senderId.equals(ChatHelper.getCurrentUser().getId())) {
                dialogsManager.onGlobalMessageReceived(dialogId, qbChatMessage);
            }
        }
    }

    @Override
    public void onDialogCreated(QBChatDialog chatDialog) {}

    @Override
    public void onDialogUpdated(String chatDialog) {}

    @Override
    public void onNewDialogLoaded(QBChatDialog chatDialog) {}
*/
}
