package com.bawaaba.rninja4.rookie.dashboard_new;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.bawaaba.rninja4.rookie.BioTab.Description;
import com.bawaaba.rninja4.rookie.BioTab.EditDetails;
import com.bawaaba.rninja4.rookie.BioTab.Services;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.ChatFunction.ChatNewActivity;
import com.bawaaba.rninja4.rookie.activity.ChatFunction.listener.QbChatDialogMessageListenerImp;
import com.bawaaba.rninja4.rookie.activity.CircleTransform;
import com.bawaaba.rninja4.rookie.activity.ContactActivity;
import com.bawaaba.rninja4.rookie.activity.ForgetPassword;
import com.bawaaba.rninja4.rookie.activity.MyDialogFragment;
import com.bawaaba.rninja4.rookie.activity.ProfileTab.Edit_Review_Postedbyme;
import com.bawaaba.rninja4.rookie.activity.ProfileTab.Edit_reviewpostedbyhirer;
import com.bawaaba.rninja4.rookie.activity.ProfileTab.LanguageTab;
import com.bawaaba.rninja4.rookie.activity.ProfileTab.Profile_settings;
import com.bawaaba.rninja4.rookie.activity.ProfileTab.SkillTab;
import com.bawaaba.rninja4.rookie.activity.ProfileTab.SocialMedia;
import com.bawaaba.rninja4.rookie.activity.RegisterActivity;
import com.bawaaba.rninja4.rookie.activity.portfolioTab.OtherFile;
import com.bawaaba.rninja4.rookie.activity.portfolioTab.PortfolioAudio;
import com.bawaaba.rninja4.rookie.activity.portfolioTab.PortfolioFile;
import com.bawaaba.rninja4.rookie.activity.portfolioTab.PortfolioImage;
import com.bawaaba.rninja4.rookie.activity.portfolioTab.PortfolioReview;
import com.bawaaba.rninja4.rookie.activity.portfolioTab.PortfolioVideo;
import com.bawaaba.rninja4.rookie.firbase.Config;
import com.bawaaba.rninja4.rookie.helper.SQLiteHandler;
import com.bawaaba.rninja4.rookie.helper.SessionManager;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.managers.DialogsManager;
import com.bawaaba.rninja4.rookie.model.profile.Profileresponse;
import com.bawaaba.rninja4.rookie.utils.IConsts;
import com.bawaaba.rninja4.rookie.utils.TinyDB;
import com.bawaaba.rninja4.rookie.utils.chat.ChatHelper;
import com.bawaaba.rninja4.rookie.utils.qb.callback.QbDialogHolder;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.QBPrivacyListsManager;
import com.quickblox.chat.QBSystemMessagesManager;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.listeners.QBPrivacyListListener;
import com.quickblox.chat.listeners.QBSystemMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBPrivacyList;
import com.quickblox.chat.model.QBPrivacyListItem;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class ProfileViewFragment extends Fragment implements IConsts, DialogsManager.ManagingDialogsCallbacks, MyDialogFragment.InterfaceCommunicator {


    MyDialogFragment dialogFrag;
    QBPrivacyList qbPrivacyList;
    private static final String service_spec = "Services";
    private static final String description_spec = "Description";
    //   Tabs for skill,image,social media
    private static final String SKILL_SPEC = "Skills";
    private static final String LANGUAGE_SPEC = "Language";
    private static final String SOCIALMEDIA_SPEC = "Social Media";
    private static final String IMAGES_SPEC = "Images";
    private static final String VIDEOS_SPEC = "Videos";
    private static final String AUDIO_SPEC = "Audios";
    private static final String FILE_SPEC = "Pdf";
    private static final String OTHER_SPEC = "Other";
    private static final String REVIEW_SPEC = "Reviews";
    private static final String REVIEWBY_ME_SPEC = "";

    RelativeLayout family;
    String userRegID = "";
    private TextView txtName;
    private TextView txtLocation;
    private TextView editdetails;
    private TextView noPortfolio;
    private TextView editReview;
    private TextView editReview_by_me;
    //private TextView review_count;
    private ImageView fab_review;
    private ImageView txtImage;
    private ImageView user_button;
    private TabHost tabHost;
    private ImageButton report_profile;
    private TabHost tabHost1;
    private TabHost tabHost2;
    private TabHost tabHost3;
    private TabHost tabHost4;
    private TextView review_count;
    private TabWidget tw;
    private SQLiteHandler db;
    private SessionManager session;
    private LocalActivityManager mlam;
    //  Tabs for Portfolio sections
    private FloatingActionButton fab, fabMessage, fabSetting;
    private String profileId = "";
    private String profileName = "";
    private String db_id;
    private SwipeRefreshLayout swipe;
    private QBChatDialogMessageListener allDialogsMessagesListener;
    private SystemMessagesListener systemMessagesListener;
    private QBSystemMessagesManager systemMessagesManager;
    private QBIncomingMessagesManager incomingMessagesManager;
    private DialogsManager dialogsManager;
    Button btn_reportuser, btn_blockuser, btn_cancel;
    BottomSheetDialog dialog;
    boolean isOtherUserBlocked = false;
    String strOtherUserId = "";
    public String reg_id;
    QBPrivacyListsManager privacyListsManager;
    TinyDB tinyDB;

    public static ProfileViewFragment newInstance(String regId, String name) {

        Bundle args = new Bundle();
        args.putString("regId", regId);
        args.putString("name", name);
        ProfileViewFragment fragment = new ProfileViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_profile_view, container, false);
        init(v, savedInstanceState);

        return v;
    }

    private void initViews(View v) {
        fabMessage = (FloatingActionButton) v.findViewById(R.id.fab1);
        ImageButton home = (ImageButton) v.findViewById(R.id.home);
        txtName = (TextView) v.findViewById(R.id.name);
//        txtEmail = (TextView) findViewById(R.id.email);
        user_button = (ImageButton) v.findViewById(R.id.verify_button);
        txtLocation = (TextView) v.findViewById(R.id.location);
        editdetails = (TextView) v.findViewById(R.id.edit_details);
        noPortfolio = (TextView) v.findViewById(R.id.no_portfolio);
        // txtBio=(TextView)findViewById(R.id.desc);
        txtImage = (ImageView) v.findViewById(R.id.android);
        family = (RelativeLayout) v.findViewById(R.id.fab_family);

        editReview = (TextView) v.findViewById(R.id.edit_review);
        fab_review = (ImageView) v.findViewById(R.id.fab_review);

        fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fabMessage = (FloatingActionButton) v.findViewById(R.id.fab1);
        fabSetting = (FloatingActionButton) v.findViewById(R.id.fab2);
        report_profile = (ImageButton) v.findViewById(R.id.report_profile);
        review_count = (TextView) v.findViewById(R.id.review_count);
        editReview_by_me = (TextView) v.findViewById(R.id.edit_review_by_me);
        swipe = (SwipeRefreshLayout) v.findViewById(R.id.swipe);
        tw = (TabWidget) v.findViewById(android.R.id.tabs);
//tabhost for aboutme,description
        tabHost = (TabHost) v.findViewById(android.R.id.tabhost);
        tabHost1 = (TabHost) v.findViewById(R.id.mySecondTabhost);
        tabHost2 = (TabHost) v.findViewById(R.id.myThirdTabhost);
        tabHost3 = (TabHost) v.findViewById(R.id.myFourthTabhost);
        tabHost4 = (TabHost) v.findViewById(R.id.myFifthTabhost);
    }

    private void init(View v, @Nullable Bundle savedInstanceState) {
        profileId = getArguments().getString("regId");
        profileName = getArguments().getString("name");
        initViews(v);


        db = new SQLiteHandler(getContext());
        session = new SessionManager(getContext());
        allDialogsMessagesListener = new AllDialogsMessageListener();
        systemMessagesListener = new SystemMessagesListener();
        dialogsManager = new DialogsManager();
        tinyDB = new TinyDB(getContext());
        createChatSesion();
        mlam = new LocalActivityManager(getActivity(), true);
        mlam.dispatchCreate(savedInstanceState);

        HashMap<String, String> user = db.getUserDetails();
        db_id = user.get("uid"); // value from db when logged in


        if (!session.isLoggedIn()) {
            if (profileId != null) {
                String reg_id = profileId;
                userRegID = reg_id;
                profileuser(reg_id);
            } else {
                session.setLogin(false);
                logoutUser();
            }
        } else {
            if (profileId != null) {
                String reg_id = profileId;
                userRegID = reg_id;
                profileuser(reg_id);
            } else {
                String reg_id = user.get("uid");
                userRegID = reg_id;
                profileuser(reg_id);
            }

        }
        setListeners();
    }

    private void setListeners() {
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    swipe.setRefreshing(true);
                    ((BaseBottomHelperActivity) getActivity()).changeFragment(ProfileViewFragment.newInstance(profileId, profileName), true);
                } catch (Exception e) {
                }
            }
        });
        fabMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (session.isLoggedIn() && db_id != null) {

//                    Intent to_chat = new Intent(ProfileView.this, ChatActivity.class);
//                    startActivity(to_chat);
                    //first freelancer implemented                 //createChatSesion();
                    showQbUserInformation();
                } else {
                    if (profileId != null) {


                        Intent to_contacts = new Intent(getContext(), ContactActivity.class);
                        to_contacts.putExtra("profile_id", profileId);
                        startActivity(to_contacts);
                    }
                }
            }
        });


        if (session.isLoggedIn() && (db_id.equals(profileId) || (profileId == null))) {

            //btnLogout.setVisibility(View.VISIBLE);
            editdetails.setVisibility(View.VISIBLE);
            fabMessage.setVisibility(View.VISIBLE);
            editReview.setVisibility(View.VISIBLE);
            fab_review.setVisibility(View.GONE);
            report_profile.setVisibility(View.GONE);

        } else {
            // btnLogout.setVisibility(View.GONE);
            editdetails.setVisibility(View.GONE);
            fabSetting.setVisibility(View.GONE);
            editReview.setVisibility(View.GONE);
            fab_review.setVisibility(View.VISIBLE);
            report_profile.setVisibility(View.VISIBLE);

        }

        if (session.isLoggedIn() && db_id != null) {
            fab_review.setOnClickListener(new View.OnClickListener() {

                private RatingBar ratingBar;
                private AppCompatTextView choose_file;
                private ImageView pic_file;
                private EditText review_text;
                private Button Submit;

                @Override
                public void onClick(View v) {

                    if (getActivity() != null) {
                        dialogFrag = new MyDialogFragment();
                        dialogFrag.setFragmentContainerId(R.id.mainFrame);
//                    FragmentManager fm = getFragmentManager();
                        dialogFrag.show(getActivity().getFragmentManager(), "dialog");

                    }
                }

            });

        } else {
            fab_review.setOnClickListener(new View.OnClickListener() {
                private Button btnLogin;
                private TextView forgotPassword;
                private TextView Signup;

                @Override
                public void onClick(View v) {

                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                    View mView = getLayoutInflater().inflate(R.layout.dialogue_login, null);

                    final EditText inputEmail = (EditText) mView.findViewById(R.id.email);
                    final EditText inputPassword = (EditText) mView.findViewById(R.id.password);

                    forgotPassword = (TextView) mView.findViewById(R.id.forgot);
                    Signup = (TextView) mView.findViewById(R.id.signup);

                    forgotPassword.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent to_forget = new Intent(getContext(), ForgetPassword.class);
                            startActivity(to_forget);
                        }
                    });

                    Signup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(getContext(),
                                    RegisterActivity.class);
                            startActivity(i);
                        }
                    });
                    btnLogin = (Button) mView.findViewById(R.id.btnLogin);
                    btnLogin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String email = inputEmail.getText().toString().trim();
                            String password = inputPassword.getText().toString().trim();

                            if (!email.isEmpty() || !password.isEmpty()) {
                                if (!isValidEmail(email)) {
                                    inputEmail.setError("Invalid Email");
                                } else if (!isValidPassword(password)) {
                                    inputPassword.setError("Invalid Password");
                                } else {

                                    check_login(email, password);

                                }
                            } else {
                                Toast.makeText(getContext(),
                                        "Please enter the credentials!", Toast.LENGTH_LONG)
                                        .show();
                            }
                        }

                        private boolean isValidEmail(String email) {

                            String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                            Pattern pattern = Pattern.compile(EMAIL_PATTERN);
                            Matcher matcher = pattern.matcher(email);
                            return matcher.matches();
                        }

                        private boolean isValidPassword(String password) {
                            if (password != null && password.length() >= 6) {
                                return true;
                            }
                            return false;
                        }

                    });

                    mBuilder.setView(mView);
                    AlertDialog dialog = mBuilder.create();
                    dialog.show();

                }
            });
        }

        if (session.isLoggedIn() && db_id != null) {

            report_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    init_modal_bottomsheet();
                }
            });
        } else {
            report_profile.setOnClickListener(new View.OnClickListener() {

                private Button btnLogin;
                private TextView forgotPassword;
                private TextView Signup;

                @Override
                public void onClick(View v) {

                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                    View mView = getLayoutInflater().inflate(R.layout.dialogue_login, null);

                    final EditText inputEmail = (EditText) mView.findViewById(R.id.email);
                    final EditText inputPassword = (EditText) mView.findViewById(R.id.password);

                    forgotPassword = (TextView) mView.findViewById(R.id.forgot);
                    Signup = (TextView) mView.findViewById(R.id.signup);

                    forgotPassword.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent to_forget = new Intent(getContext(), ForgetPassword.class);
                            startActivity(to_forget);
                        }
                    });

                    Signup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(getContext(),
                                    RegisterActivity.class);
                            startActivity(i);
                        }
                    });
                    btnLogin = (Button) mView.findViewById(R.id.btnLogin);
                    btnLogin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String email = inputEmail.getText().toString().trim();
                            String password = inputPassword.getText().toString().trim();

                            if (!email.isEmpty() || !password.isEmpty()) {
                                if (!isValidEmail(email)) {
                                    inputEmail.setError("Invalid Email");
                                } else if (!isValidPassword(password)) {
                                    inputPassword.setError("Invalid Password");
                                } else {

                                    check_login(email, password);

                                }
                            } else {
                                Toast.makeText(getContext(),
                                        "Please enter the credentials!", Toast.LENGTH_LONG)
                                        .show();
                            }
                        }

                        private boolean isValidEmail(String email) {

                            String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                            Pattern pattern = Pattern.compile(EMAIL_PATTERN);
                            Matcher matcher = pattern.matcher(email);
                            return matcher.matches();
                        }

                        private boolean isValidPassword(String password) {
                            if (password != null && password.length() >= 6) {
                                return true;
                            }
                            return false;
                        }

                    });

                    mBuilder.setView(mView);
                    AlertDialog dialog = mBuilder.create();
                    dialog.show();

                }
            });
        }


        tabHost.setup(mlam);
        tabHost1.setup(mlam);
        tabHost2.setup(mlam);
        tabHost3.setup(mlam);
        tabHost4.setup(mlam);


//        TabHost.TabSpec ReviewSpec = tabHost3.newTabSpec(REVIEW_SPEC);
//        Intent reviewIntent = new Intent(ProfileView.this, PortfolioReview.class);


        registerQbChatListeners();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterQbChatListeners();
    }

    private void registerQbChatListeners() {
        try {
            incomingMessagesManager = QBChatService.getInstance().getIncomingMessagesManager();
            systemMessagesManager = QBChatService.getInstance().getSystemMessagesManager();

            if (incomingMessagesManager != null) {
                incomingMessagesManager.addDialogMessageListener(allDialogsMessagesListener != null
                        ? allDialogsMessagesListener : new AllDialogsMessageListener());
            }

            if (systemMessagesManager != null) {
                systemMessagesManager.addSystemMessageListener(systemMessagesListener != null
                        ? systemMessagesListener : new SystemMessagesListener());
            }

            //dialogsManager.addManagingDialogsCallbackListener(mActivity);
            dialogsManager.addManagingDialogsCallbackListener(this);
        } catch (Exception e) {
            Log.e("Register", "=", e);
        }

    }

    private void unregisterQbChatListeners() {
        try {
            if (incomingMessagesManager != null) {
                incomingMessagesManager.removeDialogMessageListrener(allDialogsMessagesListener);
            }

            if (systemMessagesManager != null) {
                systemMessagesManager.removeSystemMessageListener(systemMessagesListener);
            }

            dialogsManager.removeManagingDialogsCallbackListener(this);
        } catch (Exception e) {
        }

    }

    private void check_login(final String email, final String password) {
        SharedPreferences pref = getContext().getSharedPreferences(Config.SHARED_PREF, 0);
        reg_id = pref.getString("regId", null);
        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(getContext()).getApiService().login("app-client",
                "123321", email, password, reg_id, "android");
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jObj = new JSONObject(responseString);
                        boolean error = jObj.getBoolean("error");
                        if (!error) {
                            session.setLogin(true);
                            JSONObject user = jObj.getJSONObject("user");
                            String name = user.getString("name");
                            String uid = user.getString("uid");
                            String email = user.getString("email");
                            String token = user.getString("token");
                            String verify_code = user.getString("verify_code");
                            boolean result = db.addUser(name, email, uid, token, verify_code, password);
                            if (result) {
                                ((BaseBottomHelperActivity) getActivity()).changeFragment(ProfileViewFragment.newInstance(profileId, profileName), true);
                            }

                        } else {
                            String errorMsg = jObj.getString("error_msg");
                            Toast.makeText(getContext(),
                                    errorMsg, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        // JSON error
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void createChatSesion() {

        ((BaseBottomHelperActivity) getActivity()).showProgress("Loading...", false);

        // mDialogo.show();
        String user, password;
        user = ObjectFactory.getInstance().getAppPreference(getContext()).getUserId();
        password = QB_PASSWORD;

        final QBUser qbUser = new QBUser(user, password);
        QBAuth.createSession(qbUser).performAsync(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {
                try {
                    qbUser.setId(qbSession.getUserId());
                    qbUser.setPassword(qbSession.getToken());

                    if (QBChatService.getInstance().isLoggedIn()) {
                        //  showQbUserInformation();
                    } else {
                        QBChatService.getInstance().login(qbUser, new QBEntityCallback() {
                            @Override
                            public void onSuccess(Object o, Bundle bundle) {
                                try {
                                    privacyListsManager = QBChatService.getInstance().getPrivacyListsManager();
                                    ((BaseBottomHelperActivity) getActivity()).dismissProgress();
                                } catch (Exception e) {
                                }

                            }

                            @Override
                            public void onError(QBResponseException e) {
                                try {
                                    ((BaseBottomHelperActivity) getActivity()).dismissProgress();
                                } catch (Exception e1) {
                                }

                            }
                        });
                    }
                } catch (Exception e) {
                    try {
                        ((BaseBottomHelperActivity) getActivity()).dismissProgress();
                    } catch (Exception e1) {
                    }
                }
            }

            @Override
            public void onError(QBResponseException e) {
                try {
                    ((BaseBottomHelperActivity) getActivity()).dismissProgress();
                } catch (Exception e1) {
                }
            }
        });

    }

    private void showQbUserInformation() {
        try {
            QBSystemMessagesManager qbSystemMessagesManager = QBChatService.getInstance().getSystemMessagesManager();
            qbSystemMessagesManager.addSystemMessageListener(new SystemMessagesListener());
            QBIncomingMessagesManager qbIncomingMessagesManager = QBChatService.getInstance().getIncomingMessagesManager();
            qbIncomingMessagesManager.addDialogMessageListener(new AllDialogsMessageListener());

            QBPagedRequestBuilder pagedRequestBuilder = new QBPagedRequestBuilder();
            pagedRequestBuilder.setPage(1);
            pagedRequestBuilder.setPerPage(50);

            ArrayList<String> usersLogins = new ArrayList<String>();
//            usersLogins.add("Ga5d8c66779da8fc79721028629e859ad6bnyg");// User4 - TestUser
            usersLogins.add(userRegID);//User2 - Abhishek

            QBUsers.getUsersByLogins(usersLogins, pagedRequestBuilder).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
                //            QBUsers.getUsers(pagedRequestBuilder).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
                @Override
                public void onSuccess(ArrayList<QBUser> users, Bundle params) {
                    try {
                        if (!users.isEmpty() && users.size() > 0) {
                            openPrivateChatWindow(users);
                        } else {
                            ((BaseBottomHelperActivity) getActivity()).showSnackBar("User must install new app, than logout and login again.", Color.YELLOW);

                        }
                    } catch (Exception e) {
                    }

                }

                @Override
                public void onError(QBResponseException errors) {
                    try {
                        ((BaseBottomHelperActivity) getActivity()).dismissProgress();
                    } catch (Exception e) {
                    }
                }
            });
        } catch (Exception e) {
            try {
                ((BaseBottomHelperActivity) getActivity()).dismissProgress();
            } catch (Exception e1) {
            }
        }
    }

    private void openPrivateChatWindow(ArrayList<QBUser> selectedUsers) {

        try {
            if (isPrivateDialogExist(selectedUsers)) {
                ((BaseBottomHelperActivity) getActivity()).dismissProgress();
                selectedUsers.remove(ChatHelper.getCurrentUser());
                QBChatDialog existingPrivateDialog = QbDialogHolder.getInstance().getPrivateDialogWithUser(selectedUsers.get(0));
                //           isProcessingResultInProgress = false;
                Intent intent = new Intent(getContext(), ChatNewActivity.class);
                intent.putExtra(ChatNewActivity.EXTRA_DIALOG_ID, existingPrivateDialog);
                intent.putExtra("from", "profile");
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);


            } else {
                // ProgressDialogFragment.show(getSupportFragmentManager(), R.string.create_chat);
                createDialog(selectedUsers);
            }
        } catch (Exception e) {
            try {
                ((BaseBottomHelperActivity) getActivity()).dismissProgress();
            } catch (Exception e1) {
            }
        }
    }

    private void createDialog(final ArrayList<QBUser> selectedUsers) {
        try {
            ChatHelper.getInstance().createDialogWithSelectedUsers(selectedUsers,
                    new QBEntityCallback<QBChatDialog>() {
                        @Override
                        public void onSuccess(QBChatDialog dialog, Bundle args) {
                            try {
                                ((BaseBottomHelperActivity) getActivity()).dismissProgress();
                                //isProcessingResultInProgress = false;
                                dialogsManager.sendSystemMessageAboutCreatingDialog(systemMessagesManager, dialog);
                                Intent intent = new Intent(getContext(), ChatNewActivity.class);
                                intent.putExtra(ChatNewActivity.EXTRA_DIALOG_ID, dialog);
                                intent.putExtra("from", "profile");
                                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            } catch (Exception e) {
                            }

                        }

                        @Override
                        public void onError(QBResponseException e) {
                            try {
                                ((BaseBottomHelperActivity) getActivity()).dismissProgress();
                            } catch (Exception e1) {
                            }
                            // isProcessingResultInProgress = false;
                            //ProgressDialogFragment.hide(getSupportFragmentManager());
                            //showErrorSnackbar(R.string.dialogs_creation_error, null, null);
                        }
                    }
            );
        } catch (Exception e) {
            try {
                ((BaseBottomHelperActivity) getActivity()).dismissProgress();
            } catch (Exception e1) {
            }
        }
    }

    private boolean isPrivateDialogExist(ArrayList<QBUser> allSelectedUsers) {
        ArrayList<QBUser> selectedUsers = new ArrayList<>();
        selectedUsers.addAll(allSelectedUsers);
        selectedUsers.remove(ChatHelper.getCurrentUser());
        return selectedUsers.size() == 1 && QbDialogHolder.getInstance().hasPrivateDialogWithUser(selectedUsers.get(0));
    }

    private void setReviewApi(String rating, float rate, String review, String imageRating) {

        System.out.println("setReviewApi profileId" + profileId);

        Log.e("RevieImagechecking", imageRating);
        System.out.println("setReviewApi prof" + ObjectFactory.getInstance().getAppPreference(getContext()).getUserId());
//        final Dialog dialog = ObjectFactory.getInstance().getUtils(getContext()).showLoadingDialog(getContext());
//        dialog.show();
        ((BaseBottomHelperActivity) getActivity()).showProgress("Loading", false);
        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(getContext()).getApiService().setRating(
                "app-client",
                "123321",
                ObjectFactory.getInstance().getAppPreference(getContext()).getUserId(),
                ObjectFactory.getInstance().getAppPreference(getContext()).getLoginToken(),
                ObjectFactory.getInstance().getAppPreference(getContext()).getUserId(),
                profileId,
                review,
                rating,
                imageRating,
                ObjectFactory.getInstance().getAppPreference(getContext()).getUserName()
        );
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                ((BaseBottomHelperActivity) getActivity()).dismissProgress();
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        if (responseString != null) {
                            Log.e("Profile_review", responseString);
                            JSONObject jsonObject = new JSONObject(responseString);
                            if (jsonObject != null) {
                                System.out.println("onResponse " + responseString);
                                if (!jsonObject.getBoolean("error")) {
                                    //dialogParent.dismiss();
                                    Toast.makeText(getContext(), "Successfully reviewed", Toast.LENGTH_SHORT).show();
                                    ((BaseBottomHelperActivity) getActivity()).changeFragment(ProfileViewFragment.newInstance(profileId, profileName), true);
                                    // apiCallToUpdateProfileDatas();

                                } else {
                                    Toast.makeText(getContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();
                ((BaseBottomHelperActivity) getActivity()).dismissProgress();
            }
        });

    }


    private void profileuser(final String uid) {
        HashMap<String, String> user = db.getUserDetails();
        final String db_id = (user.get("uid") == null) ? "null" : user.get("uid");
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", uid);
        params.put("db_id", (db_id != null) ? db_id : "no");
//        final Dialog dialog = ObjectFactory.getInstance().getUtils(getContext()).showLoadingDialog(getContext());
//        dialog.show();
        ((BaseBottomHelperActivity) getActivity()).showProgress("Loading", false);
        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(getContext()).getApiService().profileDetails("app-client",
                "123321", params);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
//                    dialog.dismiss();
                    ((BaseBottomHelperActivity) getActivity()).dismissProgress();
                    swipe.setRefreshing(false);

                } catch (Exception e) {
                }
                if (response.body() != null) {

                    try {
                        String responseString = new String(response.body().bytes());
                        Profileresponse profileresponse = new Gson().fromJson(responseString, Profileresponse.class);
                        ObjectFactory.getInstance().getAppPreference(getContext()).setProfileResponse(responseString);
                        JSONObject jObj = new JSONObject(responseString);
                        boolean error = jObj.getBoolean("error");
                        if (!error) {
                            JSONObject user = jObj.getJSONObject("UserData");
                            //  Log.e("profile image", String.valueOf(user));
                            String profile_image = user.getString("profile_img");
                            String name = user.getString("name");
                            String dob = (user.getString("dob") != "null") ? user.getString("dob") : "";
                            String contactnumber = (user.getString("phone") != "null") ? user.getString("phone") : "";
                            String location = user.getString("location");
                            String profile_url = user.getString("profile_url");
                            final String role = user.getString("role");
                            final String category = user.getString("category");
                            final String gender = user.getString("gender");
                            final String verify = user.getString("verify");
                            final String dp_exist = user.getString("dp_exist");

                            if (verify.matches("0")) {
                                user_button.setVisibility(View.VISIBLE);
                            } else {
                                user_button.setVisibility(View.GONE);
                            }
                            Log.e("check ver", verify);
                            final String finalProfile_image1 = profile_image;
                            fabSetting.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent to_prof_settings = new Intent(getContext(), Profile_settings.class);
                                    to_prof_settings.putExtra("verify", verify);
                                    to_prof_settings.putExtra("profile_img", finalProfile_image1);
                                    startActivity(to_prof_settings);
//                                 finish();
                                }
                            });
                            Log.e("role", role);
                            final String finalProfile_image = profile_image;
                            final String finalName = name;
                            final String finalDob = dob;
                            final String finalContactnumber = contactnumber;
                            final String finalLocation = location;
                            final String finalProfile_url = profile_url;

                            editReview.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent to_edit_review = new Intent(getContext(), Edit_Review_Postedbyme.class);
                                    startActivity(to_edit_review);
                                }
                            });

                            editdetails.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent to_editdetails = new Intent(getContext(), EditDetails.class);
                                    to_editdetails.putExtra("user_id", uid);
                                    to_editdetails.putExtra("profile_img", finalProfile_image);
                                    to_editdetails.putExtra("name", finalName);
                                    to_editdetails.putExtra("dob", finalDob);
                                    to_editdetails.putExtra("phone", finalContactnumber);
                                    to_editdetails.putExtra("location", finalLocation);
                                    to_editdetails.putExtra("profile_url", finalProfile_url);
                                    to_editdetails.putExtra("gender", gender);
                                    to_editdetails.putExtra("dp_exist", dp_exist);
                                    startActivity(to_editdetails);
                                    //   finish();
                                }
                            });

                            if (role.equals("Hirer")) {

                                profile_image = user.getString("profile_img");
                                name = user.getString("name");
                                dob = (user.getString("dob") != "null") ? user.getString("dob") : "";
                                contactnumber = (user.getString("phone") != "null") ? user.getString("phone") : "";
                                location = user.getString("location");
                                profile_url = user.getString("profile_url");
                                Log.e("role", "hirercheck");
                                tabHost.setVisibility(View.GONE);
                                tabHost1.setVisibility(View.GONE);
                                tabHost2.setVisibility(View.GONE);
                                tabHost3.setVisibility(View.GONE);
                                tabHost4.setVisibility(View.VISIBLE);
                                editReview_by_me.setVisibility(View.GONE);
                                noPortfolio.setVisibility(View.GONE);
                                fabMessage.setVisibility(View.GONE);
                                fab.setVisibility(View.GONE);
                                String reviewbyme = user.getString("review_by_me");
                                TabHost.TabSpec review_by_me = tabHost4.newTabSpec(REVIEWBY_ME_SPEC);
                                Intent review_by_me_Intent = new Intent(getContext(), Edit_reviewpostedbyhirer.class);
                                review_by_me_Intent.putExtra("review_by_me", reviewbyme);
                                review_by_me_Intent.putExtra("user_id", uid);
                                review_by_me.setIndicator(REVIEWBY_ME_SPEC);
                                review_by_me.setContent(review_by_me_Intent);
                                tabHost4.addTab(review_by_me);
                                noPortfolio.setVisibility(View.GONE);


                            } else {

                                //skills
                                TabWidget widget = tabHost1.getTabWidget();
                                String skills = user.getString("skills");
                                TabHost.TabSpec skillSpec = tabHost1.newTabSpec(SKILL_SPEC);
                                Intent skillIntent = new Intent(getContext(), SkillTab.class);
                                skillIntent.putExtra("skills", skills);
                                skillIntent.putExtra("user_id", uid);
                                skillIntent.putExtra("category", category);
                                skillSpec.setIndicator(SKILL_SPEC);
                                skillSpec.setContent(skillIntent);
                                tabHost1.addTab(skillSpec);

                                String language = user.getString("language");
                                TabHost.TabSpec languageSpec = tabHost1.newTabSpec(LANGUAGE_SPEC);
                                Intent languageIntent = new Intent(getContext(), LanguageTab.class);
                                languageIntent.putExtra("language", language);
                                languageIntent.putExtra("user_id", uid);
                                languageSpec.setIndicator(LANGUAGE_SPEC);
                                languageSpec.setContent(languageIntent);
                                tabHost1.addTab(languageSpec);
                                //tabHost1.getTabWidget().getChildAt(tabHost1.getCurrentTab()).getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);

                                //    social media

                                String socialmedia = user.getString("socialmedia");
                                TabHost.TabSpec socialmediaSpec = tabHost1.newTabSpec(SOCIALMEDIA_SPEC);
                                Intent socialmediaIntent = new Intent(getContext(), SocialMedia.class);
                                socialmediaIntent.putExtra("socialmedia", socialmedia);
                                socialmediaIntent.putExtra("user_id", uid);
                                socialmediaSpec.setIndicator(SOCIALMEDIA_SPEC);
                                socialmediaSpec.setContent(socialmediaIntent);
                                tabHost1.addTab(socialmediaSpec);
                                try {
                                    final TabWidget tw = (TabWidget) tabHost1.findViewById(android.R.id.tabs);
                                    for (int i = 0; i < tw.getChildCount(); ++i) {
                                        final View tabView = tw.getChildTabViewAt(i);
                                        final TextView tv = (TextView) tabView.findViewById(android.R.id.title);
                                        tv.setSingleLine(true);
                                        tv.setAllCaps(false);
                                        tv.setTextSize(14);
                                    }
                                } catch (Exception e) {
                                }
//                            tabHost1.getTabWidget().getChildAt(tabHost1.getCurrentTab()).getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);


                                // portfolio image
                                JSONArray portfolio_image = user.getJSONArray("portfolio_image");
                                int image_count = portfolio_image.length();
                                TabHost.TabSpec ImagesSpec = tabHost2.newTabSpec(IMAGES_SPEC);
                                Intent imagesIntent = new Intent(getContext(), PortfolioImage.class);
                                imagesIntent.putExtra("user_id", uid);
                                imagesIntent.putExtra("portfolio_image", String.valueOf(portfolio_image));

                                if (image_count > 0 || db_id.equals(uid)) {
//                                    ImagesSpec.setIndicator("", getResources().getDrawable(R.drawable.phototab));
                                    ImagesSpec.setIndicator("Images", null);
                                    ImagesSpec.setContent(imagesIntent);
                                    tabHost2.addTab(ImagesSpec);
                                }

                                // portfolio video
                                JSONArray portfolio_video = user.getJSONArray("portfolio_video");
                                int video_count = portfolio_video.length();
                                TabHost.TabSpec VideoSpec = tabHost2.newTabSpec(VIDEOS_SPEC);
                                Intent VideoIntent = new Intent(getContext(), PortfolioVideo.class);
                                VideoIntent.putExtra("user_id", uid);
                                VideoIntent.putExtra("portfolio_video", String.valueOf(portfolio_video));

                                if (video_count > 0 || db_id.equals(uid)) {
                                    VideoSpec.setContent(VideoIntent);
//                                    VideoSpec.setIndicator("", getResources().getDrawable(R.drawable.videotab));
                                    VideoSpec.setIndicator("Videos", null);
                                    tabHost2.addTab(VideoSpec);
                                }

                                // portfolio audio

                                JSONArray portfolio_audio = user.getJSONArray("portfolio_audio");
                                int audio_count = portfolio_audio.length();
                                TabHost.TabSpec AudioSpec = tabHost2.newTabSpec(AUDIO_SPEC);
                                Intent AudioIntent = new Intent(getContext(), PortfolioAudio.class);
                                AudioIntent.putExtra("user_id", uid);
                                AudioIntent.putExtra("portfolio_audio", String.valueOf(portfolio_audio));

                                if (audio_count > 0 || db_id.equals(uid)) {
                                    AudioSpec.setContent(AudioIntent);
//                                    AudioSpec.setIndicator("", getResources().getDrawable(R.drawable.audiotab));
                                    AudioSpec.setIndicator("Audios", null);
                                    tabHost2.addTab(AudioSpec);
                                }
                                // portfolio file
                                JSONArray portfolio_doc = user.getJSONArray("portfolio_doc");
                                int document_count = portfolio_doc.length();
                                TabHost.TabSpec FileSpec = tabHost2.newTabSpec(FILE_SPEC);
                                Intent FileIntent = new Intent(getContext(), PortfolioFile.class);
                                FileIntent.putExtra("user_id", uid);
                                FileIntent.putExtra("portfolio_doc", String.valueOf(portfolio_doc));

                                if (document_count > 0 || db_id.equals(uid)) {
                                    FileSpec.setContent(FileIntent);
//                                    FileSpec.setIndicator("", getResources().getDrawable(R.drawable.texttab));
                                    FileSpec.setIndicator("PDF's", null);
                                    tabHost2.addTab(FileSpec);
                                }
                                // portfolio link
                                JSONArray portfolio_link = user.getJSONArray("portfolio_link");
                                int link_count = portfolio_link.length();
                                TabHost.TabSpec OtherSpec = tabHost2.newTabSpec(OTHER_SPEC);
                                Intent OtherIntent = new Intent(getContext(), OtherFile.class);
                                OtherIntent.putExtra("user_id", uid);
                                OtherIntent.putExtra("portfolio_link", String.valueOf(portfolio_link));


                                if (link_count > 0 || db_id.equals(uid)) {
                                    OtherSpec.setContent(OtherIntent);
//                                    OtherSpec.setIndicator("", getResources().getDrawable(R.drawable.linktab));
                                    OtherSpec.setIndicator("Others", null);
                                    tabHost2.addTab(OtherSpec);
                                }

                                if (image_count == 0 && video_count == 0 && audio_count == 0 && document_count == 0 && link_count == 0 && !db_id.equals(uid)) {
                                    noPortfolio.setVisibility(View.VISIBLE);
//                                    ImagesSpec.setIndicator("", getResources().getDrawable(R.drawable.phototab));
                                    ImagesSpec.setIndicator("Images", null);
                                    ImagesSpec.setContent(imagesIntent);
                                    tabHost2.addTab(ImagesSpec);
                                } else {
                                    noPortfolio.setVisibility(View.GONE);
                                }

                                try {
                                    final TabWidget tw = (TabWidget) tabHost2.findViewById(android.R.id.tabs);
                                    for (int i = 0; i < tw.getChildCount(); ++i) {
                                        final View tabView = tw.getChildTabViewAt(i);
                                        final TextView tv = (TextView) tabView.findViewById(android.R.id.title);
                                        tv.setSingleLine(true);
                                        tv.setAllCaps(false);
                                        tv.setTextSize(14);
                                    }
                                } catch (Exception e) {
                                }

                            }

                            if (db_id.equals(uid)) {
                                fab.setVisibility(View.VISIBLE);
                            }

                            String review = user.getString("review");
                            TabHost.TabSpec reviewSpec = tabHost3.newTabSpec(REVIEW_SPEC);
                            Intent reviewIntent = new Intent(getContext(), PortfolioReview.class);
                            reviewIntent.putExtra("review", review);
                            reviewIntent.putExtra("user_id", uid);
                            reviewSpec.setIndicator(REVIEW_SPEC);
                            reviewSpec.setContent(reviewIntent);
                            tabHost3.addTab(reviewSpec);
                            //  tabHost3.getTabWidget().getChildAt(tabHost1.getCurrentTab()).getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);

                            JSONArray jsonArray = null;
                            try {
                                jsonArray = new JSONArray(review);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            int count = jsonArray.length();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = jsonArray.getJSONObject(i);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                TextView tv = new TextView(getContext());

                                try {
                                    tv.setText(jsonObject.getString("review"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            Log.e("review_count", String.valueOf(count));


                            review_count.setText("(" + count + " Reviews)");

                            String description = user.getString("description");
                            TabHost.TabSpec descriptionSpec = tabHost.newTabSpec(description_spec);
                            Intent descrptionIntent = new Intent(getContext(), Description.class);
                            descrptionIntent.putExtra("description", description);
                            descrptionIntent.putExtra("user_id", uid);
                            descriptionSpec.setIndicator(description_spec);
                            descriptionSpec.setContent(descrptionIntent);
                            tabHost.addTab(descriptionSpec);

                            String services = user.getString("services");
                            TabHost.TabSpec servicesSpec = tabHost.newTabSpec(service_spec);
                            Intent serviceIntent = new Intent(getContext(), Services.class);
                            serviceIntent.putExtra("services", services);
                            serviceIntent.putExtra("user_id", uid);
                            servicesSpec.setIndicator(service_spec);
                            servicesSpec.setContent(serviceIntent);
                            tabHost.addTab(servicesSpec);

                            final String finalProfile_url1 = profile_url;

                            final String finalName1 = name;

                            fab.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent myintent = new Intent(Intent.ACTION_SEND);
                                    myintent.setType("text/plain");
                                    String shareBody = "https://test378.bawabba.com/" + finalProfile_url1;
                                    String shareSub = finalName1;
                                    myintent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                                    myintent.putExtra(Intent.EXTRA_TEXT, shareBody);
                                    startActivity(Intent.createChooser(myintent, "Share Using"));
                                }
                            });

                            txtName.setText(name);
                            txtLocation.setText(location);
                            txtName.requestFocusFromTouch();

                            Glide.with(getContext())   // pass Context
                                    .load(profile_image)    // pass the image url
                                    .transform(new CircleTransform(getContext()))
                                    .into(txtImage);
                        } else {
                            String errorMsg = jObj.getString("error_msg");
                            Toast.makeText(getContext(),
                                    errorMsg, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {

                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {

                    ((BaseBottomHelperActivity) getActivity()).dismissProgress();
                } catch (Exception e) {
                }
            }
        });
    }

    public void onResume() {
        super.onResume();
        try {
            mlam.dispatchResume();
        } catch (Exception e) {
        }
    }

   /* @Override
    public void onRestart() {
        super.onRestart();
        android.app.Fragment prev = getFragmentManager().findFragmentByTag("dialog");

        if (prev == null) {
            recreate();
        }

    }*/

    public void onPause() {
        super.onPause();
        try {
            mlam.dispatchPause(getActivity().isFinishing());
        } catch (Exception e) {
        }
    }

    private void logoutUser() {

        try {
            db.deleteUsers();
            session.setLogin(false);
            ((BaseBottomHelperActivity) getActivity()).setTabPosition(0);
        } catch (Exception e) {
        }
        /*Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();*/
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDialogCreated(QBChatDialog chatDialog) {
    }

    @Override
    public void onDialogUpdated(String chatDialog) {

    }

    @Override
    public void onNewDialogLoaded(QBChatDialog chatDialog) {

    }

    @Override
    public void sendRequestCode(String rating, float rate, String review, String imageRating) {
        setReviewApi(rating, rate, review, imageRating);

    }

    private class SystemMessagesListener implements QBSystemMessageListener {
        @Override
        public void processMessage(final QBChatMessage qbChatMessage) {
            dialogsManager.onSystemMessageReceived(qbChatMessage);
        }

        @Override
        public void processError(QBChatException e, QBChatMessage qbChatMessage) {

        }
    }

    private class AllDialogsMessageListener extends QbChatDialogMessageListenerImp {
        @Override
        public void processMessage(final String dialogId, final QBChatMessage qbChatMessage, Integer senderId) {
            if (!senderId.equals(ChatHelper.getCurrentUser().getId())) {
                dialogsManager.onGlobalMessageReceived(dialogId, qbChatMessage);
            }
        }
    }

    public void init_modal_bottomsheet() {
        View modalbottomsheet = getLayoutInflater().inflate(R.layout.modal_bottomsheet, null);
        dialog = new BottomSheetDialog(getContext());
        dialog.setContentView(modalbottomsheet);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        btn_reportuser = (Button) modalbottomsheet.findViewById(R.id.btn_reportuser);
        btn_reportuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText report_message;
                Button save;
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = getLayoutInflater().inflate(R.layout.dialogue_report, null);
                report_message = (EditText) mView.findViewById(R.id.report_text);


                save = (Button) mView.findViewById(R.id.report_save);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String report_text = report_message.getText().toString();

                        if (report_text != null && report_text.length() < 50) {
                            report_message.setBackgroundResource(R.drawable.red_alert_round);
                            Toast.makeText(getContext(), "Minimum 50 characters required for message.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (!report_text.isEmpty()) {
                            report_abuse(report_text);
                        } else {
                            dialog.dismiss();
                            Toast.makeText(getContext(),
                                    "Please enter the text", Toast.LENGTH_LONG)
                                    .show();

                        }
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();

            }
        });
        btn_blockuser = (Button) modalbottomsheet.findViewById(R.id.btn_blockuser);
        btn_blockuser.setText("");
        if (QBChatService.getInstance().getPrivacyListsManager() == null) {
            btn_blockuser.setVisibility(View.GONE);
        } else {

            db = new SQLiteHandler(getContext());
            HashMap<String, String> user = db.getUserDetails();
            String user_id = user.get("uid");
            String token = user.get("token");
            String profile_id = profileId;

            final String name = profileName;
            QBPagedRequestBuilder pagedRequestBuilder = new QBPagedRequestBuilder();
            pagedRequestBuilder.setPage(1);
            pagedRequestBuilder.setPerPage(10);

            QBUsers.getUsersByFullName(name, pagedRequestBuilder).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
                @Override
                public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                    for (int i = 0; i < qbUsers.size(); i++) {
                        if (TextUtils.equals(qbUsers.get(i).getFullName(), name)) {
                            strOtherUserId = String.valueOf(qbUsers.get(0).getId());
                        }
                    }
                }

                @Override
                public void onError(QBResponseException e) {

                }
            });
            btn_blockuser.setVisibility(View.VISIBLE);
            QBPrivacyList list = null;
            try {
                privacyListsManager = QBChatService.getInstance().getPrivacyListsManager();

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
                String id = "";

                if (item.getType() == QBPrivacyListItem.Type.USER_ID &&
                        item.getValueForType().contains(id)) {
                    if (item.isAllow()) {
                        isOtherUserBlocked = false;
                        btn_blockuser.setText("Block User");
                    } else {

                        btn_blockuser.setText("UnBlock User");
                        isOtherUserBlocked = true;
                    }

                }
            }
        }
        btn_blockuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToBlockList(String.valueOf(strOtherUserId));

            }
        });

        btn_cancel = (Button) modalbottomsheet.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void report_abuse(final String report_text) {
        final String profile_id = profileId;
        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(getContext()).getApiService().reportAbuse("app-client",
                "123321", ObjectFactory.getInstance().getAppPreference(getContext()).getLoginToken(), ObjectFactory.getInstance().getAppPreference(getContext()).getUserId(),
                ObjectFactory.getInstance().getAppPreference(getContext()).getUserId(), profile_id, report_text);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.body() != null) {

                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jObj = new JSONObject(responseString);
                        boolean error = jObj.getBoolean("error");
                        if (!error) {

                            Toast.makeText(getContext(),
                                    "report submit successfully", Toast.LENGTH_LONG).show();
                            ((BaseBottomHelperActivity) getActivity()).changeFragment(ProfileViewFragment.newInstance(profileId, profileName), true);

                        } else {
                            String errorMsg = jObj.getString("error_msg");
                            Toast.makeText(getContext(),
                                    errorMsg, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void addToBlockList(String userID) {
        qbPrivacyList = new QBPrivacyList();
        qbPrivacyList.setName("public");
        QBPrivacyListItem item1 = new QBPrivacyListItem();
        if (isOtherUserBlocked) {
            item1.setType(QBPrivacyListItem.Type.USER_ID);

            item1.setAllow(true);
            //item1.setMutualBlock(false);
            tinyDB.putString("blockedid", "");
        } else {
            tinyDB.putString("blockedid", userID);

            item1.setAllow(false);

            // item1.setMutualBlock(true);
        }
        item1.setType(QBPrivacyListItem.Type.USER_ID);
        item1.setValueForType(String.valueOf(userID));
        //
        ArrayList<QBPrivacyListItem> items = new ArrayList<QBPrivacyListItem>();

        items.add(item1);
        qbPrivacyList.setItems(items);

        try {
            privacyListsManager = QBChatService.getInstance().getPrivacyListsManager();

            privacyListsManager.setPrivacyList(qbPrivacyList);

            privacyListsManager.setPrivacyListAsDefault("public");
            privacyListsManager.setPrivacyListAsActive("public");
            privacyListsManager.addPrivacyListListener(new QBPrivacyListListener() {
                @Override
                public void setPrivacyList(String s, List<QBPrivacyListItem> list) {

                }

                @Override
                public void updatedPrivacyList(String s) {

                }
            });
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
            Log.e("profile", "SmackException.NotConnectedException while setting privacy list :- " + e.getMessage());
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
            Log.e("profile", "XMPPException.XMPPErrorException while setting privacy list :- " + e.getMessage());
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
            Log.e("profile", "SmackException.NoResponseException while setting privacy list :- " + e.getMessage());
        }

        if (!isOtherUserBlocked) {
            Toast.makeText(getContext(), "User is Blocked now", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getContext(), "User is UnBlocked now", Toast.LENGTH_LONG).show();
        }
        dialog.dismiss();
    }


}
