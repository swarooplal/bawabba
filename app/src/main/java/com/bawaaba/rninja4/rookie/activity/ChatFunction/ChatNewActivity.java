package com.bawaaba.rninja4.rookie.activity.ChatFunction;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.ChatFunction.listener.QbChatDialogMessageListenerImp;
import com.bawaaba.rninja4.rookie.activity.CircleTransform;
import com.bawaaba.rninja4.rookie.activity.ProfileView;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.ui.activity.ChatInfoActivity;
import com.bawaaba.rninja4.rookie.ui.activity.SelectUsersActivity;
import com.bawaaba.rninja4.rookie.ui.adapter.AttachmentPreviewAdapter;
import com.bawaaba.rninja4.rookie.ui.adapter.ChatAdapter;
import com.bawaaba.rninja4.rookie.ui.widget.AttachmentPreviewAdapterView;
import com.bawaaba.rninja4.rookie.utils.IConsts;
import com.bawaaba.rninja4.rookie.utils.chat.ChatHelper;
import com.bawaaba.rninja4.rookie.utils.qb.callback.PaginationHistoryListener;
import com.bawaaba.rninja4.rookie.utils.qb.callback.QbDialogHolder;
import com.bawaaba.rninja4.rookie.utils.qb.callback.QbDialogUtils;
import com.bawaaba.rninja4.rookie.utils.qb.callback.QbEntityCallbackTwoTypeWrapper;
import com.bawaaba.rninja4.rookie.utils.qb.callback.VerboseQbChatConnectionListener;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.listeners.QBChatDialogTypingListener;
import com.quickblox.chat.model.QBAttachment;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.messages.QBPushNotifications;
import com.quickblox.messages.model.QBEnvironment;
import com.quickblox.messages.model.QBEvent;
import com.quickblox.messages.model.QBNotificationType;
import com.quickblox.messages.model.QBPushType;
import com.quickblox.sample.core.gcm.GooglePlayServicesHelper;
import com.quickblox.sample.core.ui.dialog.ProgressDialogFragment;
import com.quickblox.sample.core.utils.KeyboardUtils;
import com.quickblox.sample.core.utils.Toaster;
import com.quickblox.sample.core.utils.constant.GcmConsts;
import com.quickblox.sample.core.utils.imagepick.ImagePickHelper;
import com.quickblox.sample.core.utils.imagepick.OnImagePickedListener;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class ChatNewActivity extends BaseActivity implements OnImagePickedListener, IConsts {
    private static final String TAG = ChatNewActivity.class.getSimpleName();
    private static final int REQUEST_CODE_ATTACHMENT = 721;
    private static final int REQUEST_CODE_SELECT_PEOPLE = 752;

    private static final String PROPERTY_SAVE_TO_HISTORY = "save_to_history";
    public static final String EXTRA_DIALOG_ID = "dialogId";

    private ProgressBar progressBar;
    private StickyListHeadersListView messagesListView;
    private EditText messageEditText;
    private TextView button_chat_send;
    private ImageView avatarView, backArrow;
    private TextView txtAuthorName;
    private TextView txtTyping;
//    private AppCompatTextView block;

    private boolean typingStarted = false;
    //    private boolean blockedSuccess = false;
    private LinearLayout attachmentPreviewContainerLayout;
//    private Snackbar snackbar;

    private ChatAdapter chatAdapter;
    private AttachmentPreviewAdapter attachmentPreviewAdapter;
    private ConnectionListener chatConnectionListener;
    private QBChatDialog qbChatDialog;
    private ArrayList<QBChatMessage> unShownMessages;
    private int skipPagination = 0;
    private ChatMessageListener chatMessageListener;
    private GooglePlayServicesHelper googlePlayServicesHelper;
    private BroadcastReceiver pushBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra(GcmConsts.EXTRA_GCM_MESSAGE);
            Log.i(TAG, "Receiving event " + GcmConsts.ACTION_NEW_GCM_EVENT + " with data: " + message);
            //retrieveMessage(message);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_new);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        googlePlayServicesHelper = new GooglePlayServicesHelper();
        registerReceiver();

        qbChatDialog = (QBChatDialog) getIntent().getSerializableExtra(EXTRA_DIALOG_ID);

        qbChatDialog.initForChat(QBChatService.getInstance());

        chatMessageListener = new ChatMessageListener();

        qbChatDialog.addMessageListener(chatMessageListener);

        ObjectFactory.getInstance().getAppPreference(getApplicationContext()).saveNewMessageArrived(false);
        ObjectFactory.getInstance().getAppPreference(getApplicationContext()).saveCurrentActivity("ChatActivity");
        initChatConnectionListener();

        initViews();
        initChat();
        KeyboardUtils.hideKeyboard(messageEditText);
//        Recipient ID can't be null or empty
        txtTyping.setVisibility(View.GONE);
        try {
            List<Integer> userIds = new ArrayList<>();
            userIds.addAll(qbChatDialog.getOccupants());
            userIds.remove(ChatHelper.getCurrentUser().getId());
            //userIds.add(dialog.getLastMessageUserId());
            QBPagedRequestBuilder requestBuilder = new QBPagedRequestBuilder(userIds.size(), 1);
            final String[] photo = {""};

            QBUsers.getUsersByIDs(userIds, requestBuilder).performAsync(
                    new QbEntityCallbackTwoTypeWrapper<ArrayList<QBUser>, ArrayList<QBChatDialog>>(null) {
                        @Override
                        public void onSuccess(ArrayList<QBUser> users, Bundle params) {
                            try {
                                qbUser = users.get(0);
                                photo[0] = qbUser.getCustomData();
                                txtAuthorName.setText(qbUser.getFullName());
                                final String user_loginid=qbUser.getLogin();
                                Log.e("qbvaluecheck",user_loginid);




                                txtAuthorName.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent to_profile = new Intent(getApplicationContext(),ProfileView.class);
                                        to_profile.putExtra("reg_id",user_loginid);
                                        getApplicationContext().startActivity(to_profile);
                                    }
                                });
                                try {
                                    if (qbUser.getCustomData().startsWith("http")) {
                                        photo[0] = qbUser.getCustomData();
                                    } else {
                                        photo[0] = "http://test378.bawabba.com/assets/profilepic/" + qbUser.getCustomData();
                                    }
                                } catch (Exception e) {
                                    photo[0] = "http://test378.bawabba.com/assets/profilepic/girl-profile.jpg";
                                }
                                Glide.with(getApplicationContext()).load(photo[0].toString().trim())
                                        .transform(new CircleTransform(getApplicationContext()))
                                        .placeholder(R.drawable.user)
                                        .error(R.drawable.user)
                                        .into(avatarView);
                            } catch (Exception e) {
                                Glide.with(getApplicationContext()).load(photo[0].toString().trim())
                                        .transform(new CircleTransform(getApplicationContext()))
                                        .placeholder(R.drawable.user)
                                        .error(R.drawable.user)
                                        .into(avatarView);

                            }
                        }
                    });

        } catch (Exception e) {

        }

    }
    QBUser qbUser;
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        if (qbChatDialog != null) {
            outState.putString(EXTRA_DIALOG_ID, qbChatDialog.getDialogId());
        }
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (qbChatDialog == null) {
            qbChatDialog = QbDialogHolder.getInstance().getChatDialogById(savedInstanceState.getString(EXTRA_DIALOG_ID));
        }
    }
    private void registerReceiver() {
        googlePlayServicesHelper.checkPlayServicesAvailable(this);

        LocalBroadcastManager.getInstance(this).registerReceiver(pushBroadcastReceiver,
                new IntentFilter(GcmConsts.ACTION_NEW_GCM_EVENT));
    }
    @Override
    protected void onResume() {
        registerReceiver();
        super.onResume();
        ChatHelper.getInstance().addConnectionListener(chatConnectionListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ChatHelper.getInstance().removeConnectionListener(chatConnectionListener);
    }

    @Override
    public void onBackPressed() {
        releaseChat();
        sendDialogId();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(pushBroadcastReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_chat, menu);

        MenuItem menuItemLeave = menu.findItem(R.id.menu_chat_action_leave);
        MenuItem menuItemDelete = menu.findItem(R.id.menu_chat_action_delete);
        if (qbChatDialog.getType() == QBDialogType.PRIVATE) {
            menuItemLeave.setVisible(false);
        } else {
            menuItemDelete.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_chat_action_info:
                ChatInfoActivity.start(this, qbChatDialog);
                return true;
            case R.id.menu_chat_action_leave:
                leaveGroupChat();
                return true;
            case R.id.menu_chat_action_delete:
                deleteChat();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sendDialogId() {
        Intent result = new Intent();
        result.putExtra(EXTRA_DIALOG_ID, qbChatDialog.getDialogId());
        setResult(RESULT_OK, result);
    }

    private void leaveGroupChat() {
        ProgressDialogFragment.show(getSupportFragmentManager());
        try {
            ChatHelper.getInstance().exitFromDialog(qbChatDialog, new QBEntityCallback<QBChatDialog>() {
                @Override
                public void onSuccess(QBChatDialog qbDialog, Bundle bundle) {
                    ProgressDialogFragment.hide(getSupportFragmentManager());
                    QbDialogHolder.getInstance().deleteDialog(qbDialog);
                    finish();
                }

                @Override
                public void onError(QBResponseException e) {
                    ProgressDialogFragment.hide(getSupportFragmentManager());
//                    showErrorSnackbar(R.string.error_leave_chat, e, new OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            leaveGroupChat();
//                        }
//                    });
                }
            });
        } catch (Exception e) {
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_SELECT_PEOPLE) {
                ArrayList<QBUser> selectedUsers = (ArrayList<QBUser>) data.getSerializableExtra(
                        SelectUsersActivity.EXTRA_QB_USERS);

                updateDialog(selectedUsers);
            }
        }
    }

    @Override
    public void onImagePicked(int requestCode, File file) {
        switch (requestCode) {
            case REQUEST_CODE_ATTACHMENT:
                attachmentPreviewAdapter.add(file);
                break;
        }
    }

    @Override
    public void onImagePickError(int requestCode, Exception e) {
        showErrorSnackbar(0, e, null);
    }

    @Override
    public void onImagePickClosed(int requestCode) {
        // ignore
    }

    @Override
    protected View getSnackbarAnchorView() {
        return findViewById(R.id.list_chat_messages);
    }

    public void onSendChatClick() {
        int totalAttachmentsCount = attachmentPreviewAdapter.getCount();
        Collection<QBAttachment> uploadedAttachments = attachmentPreviewAdapter.getUploadedAttachments();
        if (!uploadedAttachments.isEmpty()) {
            if (uploadedAttachments.size() == totalAttachmentsCount) {
                for (QBAttachment attachment : uploadedAttachments) {
                    sendChatMessage(null, attachment);
                }
            } else {
                Toaster.shortToast(R.string.chat_wait_for_attachments_to_upload);
            }
        }

        String text = messageEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(text)) {
            sendChatMessage(text, null);
        }
    }

    public void onAttachmentsClick(View view) {
        fragment = new ImagePickHelper().pickAnImage1(this, REQUEST_CODE_ATTACHMENT);
    }
    private Fragment fragment;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (fragment != null) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void showMessage(QBChatMessage message) {
        if (chatAdapter != null) {
            chatAdapter.add(message);
            scrollMessageListDown();
        } else {
            if (unShownMessages == null) {
                unShownMessages = new ArrayList<>();
            }
            unShownMessages.add(message);
        }
    }

    private void initViews() {
        actionBar.setDisplayHomeAsUpEnabled(true);
        messagesListView = _findViewById(R.id.list_chat_messages);
        messageEditText = _findViewById(R.id.edit_chat_message);
        progressBar = _findViewById(R.id.progress_chat);
        attachmentPreviewContainerLayout = _findViewById(R.id.layout_attachment_preview_container);
        button_chat_send = _findViewById(R.id.button_chat_send);
        backArrow  = _findViewById(R.id.imageBack);
        avatarView = _findViewById(R.id.imageView);
        txtAuthorName = _findViewById(R.id.txtAuthorName);
        txtTyping = _findViewById(R.id.txtTyping);
        //block=_findViewById(R.id.block);

        backArrow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


//        block.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                blockUser();
//            }
//        });

        attachmentPreviewAdapter = new AttachmentPreviewAdapter(this,
                new AttachmentPreviewAdapter.OnAttachmentCountChangedListener() {
                    @Override
                    public void onAttachmentCountChanged(int count) {
                        attachmentPreviewContainerLayout.setVisibility(count == 0 ? View.GONE : View.VISIBLE);
                    }
                },
                new AttachmentPreviewAdapter.OnAttachmentUploadErrorListener() {
                    @Override
                    public void onAttachmentUploadError(QBResponseException e) {
                        showErrorSnackbar(0, e, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onAttachmentsClick(v);
                            }
                        });
                    }
                });
        AttachmentPreviewAdapterView previewAdapterView = _findViewById(R.id.adapter_view_attachment_preview);
        previewAdapterView.setAdapter(attachmentPreviewAdapter);

        button_chat_send.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onSendChatClick();
            }
        });

        messageEditText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString()) && s.toString().trim().length() >= 1) {
                    typingStarted = true;
                    try {
                        button_chat_send.setTextColor(getResources().getColor(R.color.bg_main));
                        qbChatDialog.sendIsTypingNotification();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (s.toString().trim().length() == 0 && typingStarted) {
                    typingStarted = false;
                    try {
                        button_chat_send.setTextColor(getResources().getColor(R.color.input_login_hint));
                        qbChatDialog.sendStopTypingNotification();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        QBChatDialogTypingListener typingListener = new QBChatDialogTypingListener() {
            @Override
            public void processUserIsTyping(String dialogId, Integer senderId) {
                setTypingStatus(true);
            }

            @Override
            public void processUserStopTyping(String dialogId, Integer senderId) {
                setTypingStatus(false);
            }
        };

        qbChatDialog.addIsTypingListener(typingListener);
    }


    private void sendChatMessage(String text, QBAttachment attachment) {
        QBChatMessage chatMessage = new QBChatMessage();
        if (attachment != null) {
            ArrayList<QBAttachment> attachments = new ArrayList<>();
            chatMessage.addAttachment(attachment);
            attachments.add(attachment);
            chatMessage.setAttachments(attachments);
        } else {
            chatMessage.setBody(text);
        }
        chatMessage.setProperty(PROPERTY_SAVE_TO_HISTORY, "1");
        chatMessage.setSenderId(QBChatService.getInstance().getUser().getId());
        chatMessage.setDateSent(System.currentTimeMillis() / 1000);
        chatMessage.setSaveToHistory(true);
        chatMessage.setMarkable(true);

        try {
            qbChatDialog.sendMessage(chatMessage);
            try {
                if (attachment == null)
                    sendPushNotification(text);
                else{
                    sendPushNotification("Attachment image");
                }
            } catch (Exception e) {

            }
            if (QBDialogType.PRIVATE.equals(qbChatDialog.getType())) {
                showMessage(chatMessage);
            }

            if (attachment != null) {
                KeyboardUtils.hideKeyboard(messageEditText);
                attachmentPreviewAdapter.remove(attachment);
            } else {
                messageEditText.setText("");
            }
            scrollMessageListDown();
        } catch (Exception e) {
            Log.w(TAG, e);
            //Toaster.shortToast("Can't send a message, You are not connected to chat");
        }

    }

    private void sendPushNotification(String message) {
        try {
            StringifyArrayList<Integer> userIds = new StringifyArrayList<Integer>();
            for (int i = 0; i < qbChatDialog.getOccupants().size(); i++) {
                userIds.add(qbChatDialog.getOccupants().get(i));
            }

            userIds.remove(QBChatService.getInstance().getUser().getId());
            QBEvent event = new QBEvent();
            event.setUserIds(userIds);
            event.setEnvironment(QBEnvironment.DEVELOPMENT);
            event.setNotificationType(QBNotificationType.PUSH);
            event.setPushType(QBPushType.GCM);
            final HashMap<String, Object> data = new HashMap();
            data.put("data.message", message);
            data.put("data.senderName", ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserName());
            data.put("data.profileimg", ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getProfileImg());
            data.put("data.shortprofileimg", ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getShortProfileImg());
            data.put("data.user_id", ChatHelper.getCurrentUser().getId());
            data.put("data.recipientid",qbChatDialog.getRecipientId());
            event.setMessage(data);

            QBPushNotifications.createEvent(event).performAsync(new QBEntityCallback<QBEvent>() {
                @Override
                public void onSuccess(QBEvent qbEvent, Bundle args) {

                    Log.d("haiiiiiiilog","jsjsjjdj");
                }

                @Override
                public void onError(QBResponseException errors) {
                }
            });
        } catch (Exception e) {
        }
    }

    private void initChat() {
        switch (qbChatDialog.getType()) {
            case GROUP:
            case PUBLIC_GROUP:
                joinGroupChat();
                break;

            case PRIVATE:
                loadDialogUsers();
                break;

            default:
                Toaster.shortToast(String.format("%s %s", getString(R.string.chat_unsupported_type), qbChatDialog.getType().name()));
                finish();
                break;
        }
    }

    private void joinGroupChat() {
        progressBar.setVisibility(View.VISIBLE);
        ChatHelper.getInstance().join(qbChatDialog, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void result, Bundle b) {
//                if (snackbar != null) {
//                    snackbar.dismiss();
//                }
                loadDialogUsers();
            }

            @Override
            public void onError(QBResponseException e) {
                progressBar.setVisibility(View.GONE);
//                snackbar = showErrorSnackbar(R.string.connection_error, e, null);
            }
        });
    }

    private void leaveGroupDialog() {
        try {
            ChatHelper.getInstance().leaveChatDialog(qbChatDialog);
        } catch (XMPPException | SmackException.NotConnectedException e) {
            Log.w(TAG, e);
        }
    }

    private void releaseChat() {
        qbChatDialog.removeMessageListrener(chatMessageListener);
        if (!QBDialogType.PRIVATE.equals(qbChatDialog.getType())) {
            leaveGroupDialog();
        }
    }
    private void updateDialog(final ArrayList<QBUser> selectedUsers) {
        ChatHelper.getInstance().updateDialogUsers(qbChatDialog, selectedUsers,
                new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog dialog, Bundle args) {
                        qbChatDialog = dialog;
                        loadDialogUsers();
                    }

                    @Override
                    public void onError(QBResponseException e) {
//                        showErrorSnackbar(R.string.chat_info_add_people_error, e,
//                                new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        updateDialog(selectedUsers);
//                                    }
//                                });
                    }
                }
        );
    }

    private void loadDialogUsers() {
        ChatHelper.getInstance().getUsersFromDialog(qbChatDialog, new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> users, Bundle bundle) {
                setChatNameToActionBar();
                loadChatHistory();
            }

            @Override
            public void onError(QBResponseException e) {
//                showErrorSnackbar(R.string.chat_load_users_error, e,
//                        new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                loadDialogUsers();
//                            }
//                        });
            }
        });
    }

    private void setChatNameToActionBar() {
        String chatName = QbDialogUtils.getDialogName(qbChatDialog);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(chatName);
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeButtonEnabled(true);
        }
    }

    private void setTypingStatus(boolean isTyping) {
        try {
            if (isTyping) {
                txtAuthorName.setGravity(Gravity.TOP);
                txtTyping.setVisibility(View.VISIBLE);
            } else {
                txtAuthorName.setGravity(Gravity.CENTER_VERTICAL);
                txtTyping.setVisibility(View.GONE);
            }
        } catch (Exception e) {
        }

    }


    private void loadChatHistory() {
        ChatHelper.getInstance().loadChatHistory(qbChatDialog, skipPagination, new QBEntityCallback<ArrayList<QBChatMessage>>() {
            @Override
            public void onSuccess(ArrayList<QBChatMessage> messages, Bundle args) {
                // The newest messages should be in the end of list,
                // so we need to reverse list to show messages in the right order
                Collections.reverse(messages);
                if (chatAdapter == null) {
                    chatAdapter = new ChatAdapter(ChatNewActivity.this, qbChatDialog, messages);
                    chatAdapter.setPaginationHistoryListener(new PaginationHistoryListener() {
                        @Override
                        public void downloadMore() {
                            loadChatHistory();
                        }
                    });
                    chatAdapter.setOnItemInfoExpandedListener(new ChatAdapter.OnItemInfoExpandedListener() {
                        @Override
                        public void onItemInfoExpanded(final int position) {
                            if (isLastItem(position)) {
                                // HACK need to allow info textview visibility change so posting it via handler
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        messagesListView.setSelection(position);
                                    }
                                });
                            } else {
                                messagesListView.smoothScrollToPosition(position);
                            }
                        }

                        private boolean isLastItem(int position) {
                            return position == chatAdapter.getCount() - 1;
                        }
                    });
                    if (unShownMessages != null && !unShownMessages.isEmpty()) {
                        List<QBChatMessage> chatList = chatAdapter.getList();
                        for (QBChatMessage message : unShownMessages) {
                            if (!chatList.contains(message)) {
                                chatAdapter.add(message);
                            }
                        }
                    }
                    messagesListView.setAdapter(chatAdapter);
                    messagesListView.setAreHeadersSticky(false);
                    messagesListView.setDivider(null);
                } else {
                    chatAdapter.addList(messages);
                    messagesListView.setSelection(messages.size());
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(QBResponseException e) {
                progressBar.setVisibility(View.GONE);
                skipPagination -= ChatHelper.CHAT_HISTORY_ITEMS_PER_PAGE;
//                snackbar = showErrorSnackbar(R.string.connection_error, e, null);
            }
        });
        skipPagination += ChatHelper.CHAT_HISTORY_ITEMS_PER_PAGE;
    }

    private void scrollMessageListDown() {
        try {
            messagesListView.setSelection(messagesListView.getCount() - 1);
            messagesListView.smoothScrollToPosition(messagesListView.getCount() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteChat() {
        ChatHelper.getInstance().deleteDialog(qbChatDialog, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onError(QBResponseException e) {
                showErrorSnackbar(R.string.dialogs_deletion_error, e,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteChat();
                            }
                        });
            }
        });
    }

    private void initChatConnectionListener() {
        chatConnectionListener = new VerboseQbChatConnectionListener(getSnackbarAnchorView()) {
            @Override
            public void reconnectionSuccessful() {
                super.reconnectionSuccessful();
                skipPagination = 0;
                switch (qbChatDialog.getType()) {
                    case GROUP:
                        chatAdapter = null;
                        // Join active room if we're in Group Chat
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                joinGroupChat();
                            }
                        });
                        break;
                }
            }
        };
    }

    public class ChatMessageListener extends QbChatDialogMessageListenerImp {
        @Override
        public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {
            showMessage(qbChatMessage);
        }
    }
}
