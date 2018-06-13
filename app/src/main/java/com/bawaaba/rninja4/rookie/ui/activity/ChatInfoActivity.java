package com.bawaaba.rninja4.rookie.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.ui.adapter.UsersAdapter;
import com.bawaaba.rninja4.rookie.utils.qb.callback.QbUsersHolder;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.users.model.QBUser;

import java.util.List;

public class ChatInfoActivity extends BaseActivity {
    private static final String EXTRA_DIALOG = "dialog";

    private ListView usersListView;
    private QBChatDialog qbDialog;

    public static void start(Context context, QBChatDialog qbDialog) {
        Intent intent = new Intent(context, ChatInfoActivity.class);
        intent.putExtra(EXTRA_DIALOG, qbDialog);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_info_new);

        qbDialog = (QBChatDialog) getIntent().getSerializableExtra(EXTRA_DIALOG);
        usersListView = _findViewById(R.id.list_login_users);

        actionBar.setDisplayHomeAsUpEnabled(true);

        buildUserList();
    }

    @Override
    protected View getSnackbarAnchorView() {
        return usersListView;
    }

    private void buildUserList() {
        List<Integer> userIds = qbDialog.getOccupants();
        List<QBUser> users = QbUsersHolder.getInstance().getUsersByIds(userIds);

        UsersAdapter adapter = new UsersAdapter(this, users);
        usersListView.setAdapter(adapter);
    }
}
