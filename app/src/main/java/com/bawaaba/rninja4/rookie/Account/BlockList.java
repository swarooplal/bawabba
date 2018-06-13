package com.bawaaba.rninja4.rookie.Account;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.model.BlockModel;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBPrivacyListsManager;
import com.quickblox.chat.model.QBPrivacyList;
import com.quickblox.chat.model.QBPrivacyListItem;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.util.ArrayList;
import java.util.List;

import static com.bawaaba.rninja4.rookie.utils.IConsts.QB_PASSWORD;

public class BlockList extends AppCompatActivity {

    private List<BlockModel> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private BlockListAdapter mAdapter;
    private ProgressDialog mDialogo;
    QBPrivacyListsManager privacyListsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_list);
        getSupportActionBar().hide();

        recyclerView = (RecyclerView) findViewById(R.id.rvblock);

        mAdapter = new BlockListAdapter(BlockList.this,movieList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        createChatSesion();
    }

    private void PopulateBlockUsers() {
        QBPrivacyList list = null;
        try {
            privacyListsManager= QBChatService.getInstance().getPrivacyListsManager();

            list = privacyListsManager.getPrivacyList("public");
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        }

        List<QBPrivacyListItem> items = list.getItems();
        for (QBPrivacyListItem item : items) {

            if (item.getType() == QBPrivacyListItem.Type.USER_ID ) {
                if (!item.isAllow()){
                    String str=item.getValueForType();
                    String[] split =str.split("-");


                    QBUsers.getUser(Integer.parseInt(split[0])).performAsync(new QBEntityCallback<QBUser>() {
                        @Override
                        public void onSuccess(QBUser qbUser, Bundle bundle) {
                            QBPrivacyList list = null;

                            BlockModel blockModelObj=new BlockModel();
                            blockModelObj.setNameOfUser(""+qbUser.getFullName());
                            blockModelObj.setUserId(""+qbUser.getId());
                            blockModelObj.setImagpath(qbUser.getCustomData());
                            movieList.add(blockModelObj);
                            mAdapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onError(QBResponseException e) {

                        }
                    });

                }

            }
        }
    }

    private void createChatSesion() {
        mDialogo = new ProgressDialog(this);
        mDialogo.setMessage("Loading...");
        mDialogo.setCanceledOnTouchOutside(false);
        //  mDialogo.show();

        String user, password;

        user = ObjectFactory.getInstance().getAppPreference(this).getUserId();
        password = QB_PASSWORD;

        final QBUser qbUser = new QBUser(user, password);
        QBAuth.createSession(qbUser).performAsync(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {
                try {
                    qbUser.setId(qbSession.getUserId());
                    qbUser.setPassword(qbSession.getToken());

                    if (QBChatService.getInstance().isLoggedIn()) {
                        privacyListsManager=QBChatService.getInstance().getPrivacyListsManager();
                        if (privacyListsManager!=null){
                            PopulateBlockUsers();
                        }
                    } else {
                        QBChatService.getInstance().login(qbUser, new QBEntityCallback() {
                            @Override
                            public void onSuccess(Object o, Bundle bundle) {
                                mDialogo.dismiss();
                            }

                            @Override
                            public void onError(QBResponseException e) {
                                mDialogo.dismiss();

                            }
                        });
                    }
                }catch (Exception e) {
                    mDialogo.dismiss();
                }

            }

            @Override
            public void onError(QBResponseException e) {
                mDialogo.dismiss();
            }
        });
    }
}
