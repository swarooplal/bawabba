package com.bawaaba.rninja4.rookie.activity.ChatFunction;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.ChatFunction.listener.QbChatDialogMessageListenerImp;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.managers.DialogsManager;
import com.bawaaba.rninja4.rookie.managers.DialogsManager.ManagingDialogsCallbacks;
import com.bawaaba.rninja4.rookie.model.MessageEvent;
import com.bawaaba.rninja4.rookie.ui.activity.SelectUsersActivity;
import com.bawaaba.rninja4.rookie.ui.adapter.DialogsAdapter;
import com.bawaaba.rninja4.rookie.utils.IConsts;
import com.bawaaba.rninja4.rookie.utils.chat.ChatHelper;
import com.bawaaba.rninja4.rookie.utils.qb.callback.QbDialogHolder;
import com.bawaaba.rninja4.rookie.utils.qb.callback.QbEntityCallbackImpl;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.QBSystemMessagesManager;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.listeners.QBSystemMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.sample.core.gcm.GooglePlayServicesHelper;
import com.quickblox.sample.core.utils.constant.GcmConsts;
import com.quickblox.users.model.QBUser;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Created by rninja4 on 10/17/17.
 */

public class ChatFragment extends Fragment implements
//        DialogsManager.ManagingDialogsCallbacks,
        IConsts {
    private static final String TAG = ChatFragment.class.getSimpleName();
    private View rootView;
    private Activity mActivity;

    private static final int REQUEST_SELECT_PEOPLE = 174;
    private static final int REQUEST_DIALOG_ID_FOR_UPDATE = 165;

    private ProgressBar progressBar;
    private SwipyRefreshLayout setOnRefreshListener;
    private QBRequestGetBuilder requestBuilder;
    private Menu menu;
    private int skipRecords = 0;
    private boolean isProcessingResultInProgress;

    private BroadcastReceiver pushBroadcastReceiver;
    private GooglePlayServicesHelper googlePlayServicesHelper;
    private DialogsAdapter dialogsAdapter;
    private QBChatDialogMessageListener allDialogsMessagesListener;
    private SystemMessagesListener systemMessagesListener;
    private QBSystemMessagesManager systemMessagesManager;
    private QBIncomingMessagesManager incomingMessagesManager;
    private DialogsManager dialogsManager;
    private LinearLayout emptyHintLayout;
//    private ListView dialogsListView;
    private SwipeMenuListView dialogsListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        mActivity = getActivity();

        googlePlayServicesHelper = new GooglePlayServicesHelper();

        pushBroadcastReceiver = new PushBroadcastReceiver();

        allDialogsMessagesListener = new AllDialogsMessageListener();
        systemMessagesListener = new SystemMessagesListener();

        dialogsManager = new DialogsManager();

        emptyHintLayout = rootView.findViewById(R.id.layout_chat_empty);
        dialogsListView = rootView.findViewById(R.id.list_dialogs_chats);
        progressBar = rootView.findViewById(R.id.progress_dialogs);
        setOnRefreshListener = rootView.findViewById(R.id.swipy_refresh_layout);

        //registerForContextMenu(dialogsListView);
        dialogsAdapter = new DialogsAdapter(getContext(), new ArrayList<>(QbDialogHolder.getInstance().getDialogs().values()));
        ObjectFactory.getInstance().getAppPreference(mActivity).saveNewMessageArrived(false);
//        TextView listHeader = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.include_list_hint_header, dialogsListView, false);
//        listHeader.setText(R.string.dialogs_list_hint);
//        dialogsListView.setEmptyView(emptyHintLayout);
//        dialogsListView.addHeaderView(listHeader, null, false);

        dialogsListView.setAdapter(dialogsAdapter);

        dialogsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QBChatDialog selectedDialog = (QBChatDialog) parent.getItemAtPosition(position);
                Intent intent = new Intent(mActivity, ChatNewActivity.class);
                intent.putExtra(ChatNewActivity.EXTRA_DIALOG_ID, selectedDialog);
                startActivityForResult(intent, REQUEST_DIALOG_ID_FOR_UPDATE);
            }
        });

        dialogsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                QBChatDialog selectedDialog = (QBChatDialog) parent.getItemAtPosition(position);
                //startSupportActionMode(new DeleteActionModeCallback());
                dialogsAdapter.selectItem(selectedDialog);
                return true;
            }
        });

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(mActivity);
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                deleteItem.setTitle("Delete");
                deleteItem.setTitleColor(Color.WHITE);
                deleteItem.setTitleSize(20);
                deleteItem.setWidth(dp2px(80));
//                deleteItem.setIcon(R.drawable.ic_delete_white_24dp);
                menu.addMenuItem(deleteItem);
            }
        };
        dialogsListView.setCloseInterpolator(new BounceInterpolator());
//        dialogsListView.setOpenInterpolator(new BounceInterpolator());
        dialogsListView.setMenuCreator(creator);

        dialogsListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        QBChatDialog selectedDialog = dialogsAdapter.getItem(position);
                        deleteChat(selectedDialog);
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        requestBuilder = new QBRequestGetBuilder();

        setOnRefreshListener.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if (direction == SwipyRefreshLayoutDirection.TOP) {
                    requestBuilder.setSkip(skipRecords = 0);
                } else {
                    requestBuilder.setSkip(skipRecords += ChatHelper.DIALOG_ITEMS_PER_PAGE);
                }
                loadDialogsFromQb(true, false);
            }
        });


//        registerQbChatListeners();
        createChatSesion();
//        final boolean enable = QBSettings.getInstance().isEnablePushNotification();
        return rootView;
    }

    private void deleteChat(QBChatDialog qbChatDialog) {
        try {
            showProgress();
            ChatHelper.getInstance().deleteDialog(qbChatDialog, new QBEntityCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid, Bundle bundle) {
                    hideProgress();
                    updateDialogsList();
                }

                @Override
                public void onError(QBResponseException e) {

                }
            });
        } catch (Exception e) {

        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private ProgressDialog mDialogo;
    private void showProgress(){
        try {
            try {
                if (mDialogo != null) {
                    if (mDialogo.isShowing()) {
                        mDialogo.dismiss();
                        mDialogo = null;
                    }
                }
            } catch (Exception e) {}
            mDialogo = new ProgressDialog(mActivity);
            mDialogo.setMessage("Loading...");
            mDialogo.setCanceledOnTouchOutside(false);
            mDialogo.show();
        } catch (Exception e) {}
    }

    private void hideProgress(){
        try {
            mDialogo.dismiss();
        } catch (Exception e) {

        }
    }
    private void createChatSesion() {
        progressBar.setVisibility(View.VISIBLE);
        String user = ObjectFactory.getInstance().getAppPreference(mActivity).getUserId();
        String password = QB_PASSWORD;
        try {
            final QBUser qbUser = new QBUser(user, password);
            QBAuth.createSession(qbUser).performAsync(new QBEntityCallback<QBSession>() {
                @Override
                public void onSuccess(QBSession qbSession, Bundle bundle) {
                    try {
                        loadChatService(qbUser, qbSession);
                    } catch (Exception e) {
                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onError(QBResponseException e) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void loadChatService(QBUser qbUser, QBSession qbSession) {
        if (QBChatService.getInstance().isLoggedIn()) {
            loadSystemMessageAndDialogs();
        } else {
            qbUser.setId(qbSession.getUserId());
            qbUser.setPassword(qbSession.getToken());
            QBChatService.getInstance().login(qbUser, new QBEntityCallback() {
                @Override
                public void onSuccess(Object o, Bundle bundle) {
                    loadSystemMessageAndDialogs();
                }

                @Override
                public void onError(QBResponseException e) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    private QBSystemMessagesManager qbSystemMessagesManager;
    private QBIncomingMessagesManager qbIncomingMessagesManager;
    private Set<String> dialogsIds;
    private void loadSystemMessageAndDialogs(){
        try {
            registerQbChatListeners();

            Map<String, QBChatDialog> dialogMap = QbDialogHolder.getInstance().getDialogs();
            if (dialogMap.size() > 0) {
                loadDialogsFromQb(true, true);
            } else {
                loadDialogsFromQb(false, true);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        googlePlayServicesHelper.checkPlayServicesAvailable(mActivity);
        LocalBroadcastManager.getInstance(mActivity).registerReceiver(pushBroadcastReceiver, new IntentFilter(GcmConsts.ACTION_NEW_GCM_EVENT));
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(pushBroadcastReceiver);
        unregisterQbChatListeners();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == mActivity.RESULT_OK) {
            isProcessingResultInProgress = true;
            if (requestCode == REQUEST_SELECT_PEOPLE) {
                ArrayList<QBUser> selectedUsers = (ArrayList<QBUser>) data.getSerializableExtra(SelectUsersActivity.EXTRA_QB_USERS);

                if (isPrivateDialogExist(selectedUsers)) {
                    selectedUsers.remove(ChatHelper.getCurrentUser());
                    QBChatDialog existingPrivateDialog = QbDialogHolder.getInstance().getPrivateDialogWithUser(selectedUsers.get(0));
                    isProcessingResultInProgress = false;

                    Intent intent = new Intent(mActivity, ChatNewActivity.class);
                    intent.putExtra(ChatNewActivity.EXTRA_DIALOG_ID, existingPrivateDialog);
                    startActivityForResult(intent, REQUEST_DIALOG_ID_FOR_UPDATE);
                } else {
                    //ProgressDialogFragment.show(getSupportFragmentManager(), R.string.create_chat);
                    createDialog(selectedUsers);
                }
            } else if (requestCode == REQUEST_DIALOG_ID_FOR_UPDATE) {
                if (data != null) {
                    String dialogId = data.getStringExtra(ChatNewActivity.EXTRA_DIALOG_ID);
                    loadUpdatedDialog(dialogId);
                } else {
                    isProcessingResultInProgress = false;
                    updateDialogsList();
                }
            }
        } else {
            updateDialogsAdapter();
        }
    }

    private boolean isPrivateDialogExist(ArrayList<QBUser> allSelectedUsers) {
        ArrayList<QBUser> selectedUsers = new ArrayList<>();
        selectedUsers.addAll(allSelectedUsers);
        selectedUsers.remove(ChatHelper.getCurrentUser());
        String count_selecteduser= String.valueOf(selectedUsers.size());
        Log.e("count_selecteduser",count_selecteduser);
        return selectedUsers.size() == 1 && QbDialogHolder.getInstance().hasPrivateDialogWithUser(selectedUsers.get(0));
    }

    private void loadUpdatedDialog(String dialogId) {
        ChatHelper.getInstance().getDialogById(dialogId, new QbEntityCallbackImpl<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog result, Bundle bundle) {
                isProcessingResultInProgress = false;
                QbDialogHolder.getInstance().addDialog(result);
                updateDialogsAdapter();
            }

            @Override
            public void onError(QBResponseException e) {
                isProcessingResultInProgress = false;
            }
        });
    }

    private void updateDialogsList() {
        requestBuilder.setSkip(skipRecords = 0);
        loadDialogsFromQb(true, true);
    }

    private void registerQbChatListeners() {
//        incomingMessagesManager = QBChatService.getInstance().getIncomingMessagesManager();
//        systemMessagesManager = QBChatService.getInstance().getSystemMessagesManager();
//
//        if (incomingMessagesManager != null) {
//            incomingMessagesManager.addDialogMessageListener(allDialogsMessagesListener != null
//                    ? allDialogsMessagesListener : new AllDialogsMessageListener());
//        }
//
//        if (systemMessagesManager != null) {
//            systemMessagesManager.addSystemMessageListener(systemMessagesListener != null
//                    ? systemMessagesListener : new SystemMessagesListener());
//        }
        //dialogsManager.addManagingDialogsCallbackListener(mActivity);

        qbIncomingMessagesManager = QBChatService.getInstance().getIncomingMessagesManager();
        qbSystemMessagesManager = QBChatService.getInstance().getSystemMessagesManager();

        if (qbIncomingMessagesManager != null) {
            qbIncomingMessagesManager.addDialogMessageListener(allDialogsMessagesListener != null ? allDialogsMessagesListener : new AllDialogsMessageListener());
        }

        if (qbSystemMessagesManager  != null) {
            qbSystemMessagesManager.addSystemMessageListener(systemMessagesListener != null ? systemMessagesListener : new SystemMessagesListener());
        }
        dialogsManager.addManagingDialogsCallbackListener(callbacks);
    }

    private void unregisterQbChatListeners() {
        if (qbIncomingMessagesManager != null) {
            qbIncomingMessagesManager.removeDialogMessageListrener(allDialogsMessagesListener);
        }

        if (qbSystemMessagesManager != null) {
            qbSystemMessagesManager.removeSystemMessageListener(systemMessagesListener);
        }

//        if (incomingMessagesManager != null) {
//            incomingMessagesManager.removeDialogMessageListrener(allDialogsMessagesListener);
//        }
//
//        if (systemMessagesManager != null) {
//            systemMessagesManager.removeSystemMessageListener(systemMessagesListener);
//        }

        dialogsManager.removeManagingDialogsCallbackListener(callbacks);

    }
    ManagingDialogsCallbacks callbacks = new ManagingDialogsCallbacks() {
        @Override
        public void onDialogCreated(QBChatDialog chatDialog) {
            updateDialogsAdapter();
        }

        @Override
        public void onDialogUpdated(String chatDialog) {
            updateDialogsAdapter();
        }

        @Override
        public void onNewDialogLoaded(QBChatDialog chatDialog) {
            updateDialogsAdapter();
        }
    };

    private void createDialog(final ArrayList<QBUser> selectedUsers) {
        ChatHelper.getInstance().createDialogWithSelectedUsers(selectedUsers,
                new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog dialog, Bundle args) {
                        isProcessingResultInProgress = false;
                        dialogsManager.sendSystemMessageAboutCreatingDialog(systemMessagesManager, dialog);
                        Intent intent = new Intent(mActivity, ChatNewActivity.class);
                        intent.putExtra(ChatNewActivity.EXTRA_DIALOG_ID, dialog);
                        startActivityForResult(intent, REQUEST_DIALOG_ID_FOR_UPDATE);
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        isProcessingResultInProgress = false;
                    }
                }
        );
    }

    private void loadDialogsFromQb(final boolean silentUpdate, final boolean clearDialogHolder) {
        isProcessingResultInProgress = true;
        if (!silentUpdate) {
            progressBar.setVisibility(View.VISIBLE);
        }

        requestBuilder.sortAsc("last_message_date_sent");

        ChatHelper.getInstance().getDialogs(requestBuilder, new QBEntityCallback<ArrayList<QBChatDialog>>() {
            @Override
            public void onSuccess(ArrayList<QBChatDialog> dialogs, Bundle bundle) {
                isProcessingResultInProgress = false;
                progressBar.setVisibility(View.GONE);
                setOnRefreshListener.setRefreshing(false);

                if (clearDialogHolder) {
                    QbDialogHolder.getInstance().clear();
                }
                QbDialogHolder.getInstance().addDialogs(dialogs);
                updateDialogsAdapter();
            }
            @Override
            public void onError(QBResponseException e) {
                isProcessingResultInProgress = false;
                progressBar.setVisibility(View.GONE);
                setOnRefreshListener.setRefreshing(false);
                Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDialogsAdapter() {
        try {
            dialogsAdapter.updateList(new ArrayList<>(QbDialogHolder.getInstance().getDialogs().values()));
            dialogsListView.setEmptyView(emptyHintLayout);
        } catch (Exception e) {

        }
    }

    private class PushBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra(GcmConsts.EXTRA_GCM_MESSAGE);
            requestBuilder.setSkip(skipRecords = 0);
            loadDialogsFromQb(true, true);
            ObjectFactory.getInstance().getAppPreference(mActivity).saveNewMessageArrived(true);
            EventBus.getDefault().post(new MessageEvent("Comes from ChatFragment"));
//            NotificationUtils.showNotification(mActivity, MainActivity.class, ResourceUtils.getString(R.string.notification_title_new), message, R.mipmap.ic_notification, 1);
        }
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
                String activityName = ObjectFactory.getInstance().getAppPreference(mActivity).getCurrentActivity();
                if (activityName.equalsIgnoreCase("ChatActivity")) {
                    ObjectFactory.getInstance().getAppPreference(mActivity).saveNewMessageArrived(false);
                } else {
                    ObjectFactory.getInstance().getAppPreference(mActivity).saveNewMessageArrived(true);
                    EventBus.getDefault().post(new MessageEvent("Comes from AllDialogsMessageListener!"));
                }
            }
        }
    }
}
