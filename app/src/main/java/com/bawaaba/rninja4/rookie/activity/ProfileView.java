package com.bawaaba.rninja4.rookie.activity;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.LocalActivityManager;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ashokvarma.bottomnavigation.TextBadgeItem;
import com.bumptech.glide.Glide;
import com.bawaaba.rninja4.rookie.App.AppConfig;
import com.bawaaba.rninja4.rookie.App.AppController;
import com.bawaaba.rninja4.rookie.BioTab.Description;
import com.bawaaba.rninja4.rookie.BioTab.EditDetails;
import com.bawaaba.rninja4.rookie.BioTab.Services;
import com.bawaaba.rninja4.rookie.JSONParser;
import com.bawaaba.rninja4.rookie.MainActivity;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.ChatFunction.ChatActivity;
import com.bawaaba.rninja4.rookie.activity.ChatFunction.ChatNewActivity;
import com.bawaaba.rninja4.rookie.activity.ChatFunction.listener.QbChatDialogMessageListenerImp;
import com.bawaaba.rninja4.rookie.activity.ProfileTab.Edit_Review_Postedbyme;
import com.bawaaba.rninja4.rookie.activity.ProfileTab.Edit_reviewpostedbyhirer;
import com.bawaaba.rninja4.rookie.activity.ProfileTab.LanguageTab;
import com.bawaaba.rninja4.rookie.activity.ProfileTab.Profile_settings;
import com.bawaaba.rninja4.rookie.activity.ProfileTab.SkillTab;
import com.bawaaba.rninja4.rookie.activity.ProfileTab.SocialMedia;
import com.bawaaba.rninja4.rookie.activity.portfolioTab.OtherFile;
import com.bawaaba.rninja4.rookie.activity.portfolioTab.PortfolioAudio;
import com.bawaaba.rninja4.rookie.activity.portfolioTab.PortfolioFile;
import com.bawaaba.rninja4.rookie.activity.portfolioTab.PortfolioImage;
import com.bawaaba.rninja4.rookie.activity.portfolioTab.PortfolioReview;
import com.bawaaba.rninja4.rookie.activity.portfolioTab.PortfolioVideo;
import com.bawaaba.rninja4.rookie.helper.SQLiteHandler;
import com.bawaaba.rninja4.rookie.helper.SessionManager;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.managers.DialogsManager;
import com.bawaaba.rninja4.rookie.managers.DialogsManager.ManagingDialogsCallbacks;
import com.bawaaba.rninja4.rookie.model.MessageEvent;
import com.bawaaba.rninja4.rookie.model.profile.Profileresponse;
import com.bawaaba.rninja4.rookie.utils.IConsts;
import com.bawaaba.rninja4.rookie.utils.TinyDB;
import com.bawaaba.rninja4.rookie.utils.Utils;
import com.bawaaba.rninja4.rookie.utils.chat.ChatHelper;
import com.bawaaba.rninja4.rookie.utils.qb.callback.QbDialogHolder;
import com.google.gson.Gson;
import com.kbeanie.multipicker.api.ImagePicker;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
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

import static com.bawaaba.rninja4.rookie.App.AppConfig.URL_REPORT_ABUSE;

//import com.example.rninja4.rookie.activity.ProfileTab.LanguageTab;

public class ProfileView extends TabActivity implements  IConsts, ManagingDialogsCallbacks,MyDialogFragment.InterfaceCommunicator {
    MyDialogFragment dialogFrag;
    QBPrivacyList qbPrivacyList;
    private static final String TAG = ProfileView.class.getSimpleName();
    private static final String service_spec ="Services";
    private static final String description_spec ="Description";
    //   Tabs for skill,image,social media
    private static final String SKILL_SPEC ="Skills";
    private static final String LANGUAGE_SPEC ="Language";
    private static final String SOCIALMEDIA_SPEC ="Social Media";
    private static final String IMAGES_SPEC ="Images";
    private static final String VIDEOS_SPEC ="Videos";
    private static final String AUDIO_SPEC ="Audios";
    private static final String FILE_SPEC ="Pdf";
    private static final String OTHER_SPEC ="OtherFiles";
    private static final String REVIEW_SPEC ="Reviews";
    private static final String REVIEWBY_ME_SPEC ="";
    JSONParser jsonParser = new JSONParser();
    RelativeLayout family;
    NestedScrollView mainScroll;
    String userRegID="";
    private TextView txtName;
    private TextView txtLocation;
    private TextView editdetails;
    private TextView noPortfolio;
    private TextView editReview;
    private TextView editReview_by_me;
    //private TextView review_count;
    private ImageView fab_review;
    private ImageView txtImage;
    private ImageView user_image;
    private ImageView user_button;
    private Button btnLogout;
    private Toolbar mToolbar;
    private SQLiteHandler db;
    private SessionManager session;
    private TabWidget tw;
    private TabHost tabHost;
    private ImageButton report_profile;
    private ImageView Back_Profile;
    // Tabs for Aboutme,description,services
    private TabWidget tw1;
    private TabHost tabHost1;
    private TabHost tabHost2;
    private TabHost tabHost3;
    private TabHost tabHost4;
    private TextView review_post;
    private TextView review_count;
    private LinearLayout post_review;
    private RecyclerView rvReviews_hire;
    private View underline;
    private LocalActivityManager mlam;
    //  Tabs for Portfolio sections
    private FloatingActionButton fab, fabMessage, fabSetting;
    private BottomSheetBehavior bottomSheetBehavior;
    private TextView bottomSheetHeading;
    private DrawerLayout mDrawer;
    private NavigationView pDrawer;
    // Tabs for portfolio Reviews
    private ActionBarDrawerToggle drawerToggle;
    private String imageRating = "no";
    private ImagePicker imagePicker;
    private String profileId = "";
    private static final int REQUEST_WRITE_PERMISSION = 2;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private static final int CAMERA_REQUEST_PROOF = 100;
    private static final int CAMERA_REQUEST_KEY_CODE = 110;
    private static int GALLERY_REQUEST_PROOF = 101;
    private static int GALLERY_REQUEST_KEY_CODE = 111;
    final CharSequence[] items = {"Take Photo",
            "Choose from Library",
            "Cancel"};
    private Bitmap bitmap;
    private RatingBar ratingBar;
    private AppCompatTextView choose_file;
    private ImageView pic_file;
    private EditText review_text;
    private Button Submit;
    private SwipeRefreshLayout swipe;
    public static final String EXTRA_DIALOG_ID = "dialogId";

    private QBChatDialogMessageListener allDialogsMessagesListener;
    private SystemMessagesListener systemMessagesListener;
    private QBSystemMessagesManager systemMessagesManager;
    private QBIncomingMessagesManager incomingMessagesManager;
    private DialogsManager dialogsManager;
    private TextBadgeItem textBadgeItem;
    private QBChatDialog qbChatDialog;
    private boolean isBlocked;

    // for hirer profile
    private RecyclerView rvReviews;
    private AppCompatTextView review_count_hirer;
    private AppCompatTextView review_posted;
    private String imagebase = "";
    Button btn_reportuser,btn_blockuser,btn_cancel;
    BottomSheetDialog dialog;
    boolean isOtherUserBlocked=false;
    String strOtherUserId="";


    public void isMessageArrived() {
        try {
            boolean isMessageArrived = ObjectFactory.getInstance().getAppPreference(getApplicationContext()).isNewMessageArrived();
            if (isMessageArrived) {
                int total = ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUnreadMessage();
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

    private void hideText(){
        textBadgeItem.setText("");
        textBadgeItem.hide();
    }
    QBPrivacyListsManager privacyListsManager;
    TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile);
        allDialogsMessagesListener = new AllDialogsMessageListener();
        systemMessagesListener = new SystemMessagesListener();
        dialogsManager = new DialogsManager();
        tinyDB =new TinyDB(ProfileView.this);
        createChatSesion();

//        mainScroll = (NestedScrollView) findViewById(R.id.nested);
//        mainScroll.post(new Runnable() {
//
//            public void run() {
//
//                mainScroll.scrollTo(0, 0);
//            }
//        });
        //  mDrawer = (DrawerLayout)findViewById(R.id.drawer_layout2);
        fabMessage = (FloatingActionButton) findViewById(R.id.fab1);

        mlam = new LocalActivityManager(this, true);
        mlam.dispatchCreate(savedInstanceState);

        ImageButton home = (ImageButton) findViewById(R.id.home);


        txtName = (TextView) findViewById(R.id.name);
//        txtEmail = (TextView) findViewById(R.id.email);
        user_button = (ImageButton) findViewById(R.id.verify_button);
        txtLocation = (TextView) findViewById(R.id.location);
        editdetails = (TextView) findViewById(R.id.edit_details);
        noPortfolio = (TextView) findViewById(R.id.no_portfolio);
        // txtBio=(TextView)findViewById(R.id.desc);
        txtImage = (ImageView) findViewById(R.id.android);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        family = (RelativeLayout) findViewById(R.id.fab_family);

        editReview = (TextView) findViewById(R.id.edit_review);
        fab_review = (ImageView) findViewById(R.id.fab_review);
//        review_post=(TextView)findViewById(R.id.edit_review_post);
//        post_review=(LinearLayout)findViewById(R.id.review_linear);
        underline=(View)findViewById(R.id.UnderLine);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fabMessage = (FloatingActionButton) findViewById(R.id.fab1);
        fabSetting = (FloatingActionButton) findViewById(R.id.fab2);
        report_profile=(ImageButton) findViewById(R.id.report_profile);
        review_count=(TextView) findViewById(R.id.review_count);
        editReview_by_me=(TextView)findViewById(R.id.edit_review_by_me);
        rvReviews_hire = (RecyclerView) findViewById(R.id.rvReviews);
        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        final String db_id = user.get("uid"); // value from db when logged in
        final Intent get_id = getIntent();
        final String search_user_id = get_id.getStringExtra("reg_id"); // intent from search
        profileId = search_user_id;
        BottomNavigationBar bottomNavigationView = (BottomNavigationBar)
                findViewById(R.id.bottom_bar);
        if(!session.isLoggedIn()){
            bottomNavigationView.setFirstSelectedPosition(1);
        }else if(search_user_id == null || db_id.equals(search_user_id)) {
            bottomNavigationView.setFirstSelectedPosition(3);
        }else{
            bottomNavigationView.setFirstSelectedPosition(1);
        }
        textBadgeItem = Utils.getTextBadge();
        unread_notification();
        ObjectFactory.getInstance().getAppPreference(getApplicationContext()).saveCurrentActivity("ProfileView");
        isMessageArrived();
        bottomNavigationView
                .addItem(new BottomNavigationItem(R.drawable.ic_home1, "Home").setActiveColorResource(R.color.bottomnavigation))
                .addItem(new BottomNavigationItem(R.drawable.ic_search1, "Search").setActiveColorResource(R.color.bottomnavigation))
                .addItem(new BottomNavigationItem(R.drawable.ic_inbox1, "Inbox").setBadgeItem(textBadgeItem).setActiveColorResource(R.color.bottomnavigation))
                .addItem(new BottomNavigationItem(R.drawable.ic_profile, "Profile").setActiveColorResource(R.color.bottomnavigation))
                // .setFirstSelectedPosition(3)
                .initialise();
        isMessageArrived();
        bottomNavigationView.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                switch (position) {
                    case 0:
                        Intent to_main = new Intent(ProfileView.this, MainActivity.class);
                        startActivity(to_main);
//                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_in_left);
                        finish();
                        break;
                    case 1:
                        Intent to_search = new Intent(ProfileView.this, SearchActivity.class);
                        startActivity(to_search);
//                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_in_left);
                        finish();
                        break;
                    case 2:
                        if (session.isLoggedIn() && db_id != null) {
                            ObjectFactory.getInstance().getAppPreference(getApplicationContext()).saveNewMessageArrived(false);
                            Intent to_inbox = new Intent(ProfileView.this, ChatActivity.class);
                            startActivity(to_inbox);
//                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_in_left);
                            finish();
                        }else {
                            Intent to_login = new Intent(ProfileView.this, LoginActivity.class);
                            startActivity(to_login);
                            finish();
                        }
                        break;
                    case 3:
                        Intent to_profile = new Intent(ProfileView.this, ProfileView.class);
                        startActivity(to_profile);
//                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_in_left);
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
        });

        if (!session.isLoggedIn()) {
            if (get_id.getStringExtra("reg_id") != null) {
                String reg_id = get_id.getStringExtra("reg_id");
                userRegID = reg_id;
                profileuser(reg_id);
            } else {
                session.setLogin(false);
                logoutUser();
            }
        } else {
            if (get_id.getStringExtra("reg_id") != null) {
                String reg_id = get_id.getStringExtra("reg_id");
                userRegID = reg_id;
                profileuser(reg_id);
            } else {
                String reg_id = user.get("uid");
                userRegID = reg_id;
                profileuser(reg_id);
            }

        }

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(true);

//                Intent to_profile = new Intent(ProfileView.this,ProfileView.class);
//                to_profile.putExtra("reg_id",userRegID);
//                startActivity(to_profile);
                recreate();

            }
        });

//  CHANGE BECAUSE OF UI NEEDS
        Log.e(TAG, db_id + "= " + search_user_id);

        fabMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (session.isLoggedIn() && db_id != null) {

//                    Intent to_chat = new Intent(ProfileView.this, ChatActivity.class);
//                    startActivity(to_chat);
                    //first freelancer implemented                 //createChatSesion();
                    showQbUserInformation();
                } else {
                    Intent get_id = getIntent();
                    if (get_id.getStringExtra("reg_id") != null) {
                        String profile_id = get_id.getStringExtra("reg_id");

                        Intent to_contacts = new Intent(ProfileView.this, ContactActivity.class);
                        to_contacts.putExtra("profile_id", profile_id);
                        startActivity(to_contacts);
                    }
                }
            }
        });


        if (session.isLoggedIn() && (db_id.equals(search_user_id) || (search_user_id == null))) {

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
            Log.e(TAG, "fab_review");
            fab_review.setOnClickListener(new View.OnClickListener() {

                private RatingBar ratingBar;
                private AppCompatTextView choose_file;
                private ImageView pic_file;
                private EditText review_text;
                private Button Submit;

                @Override
                public void onClick(View v) {

//                    final Dialog dialog = new Dialog(ProfileView.this);
//                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    dialog.setContentView(R.layout.add_review);
//                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                    dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
//                            WindowManager.LayoutParams.WRAP_CONTENT);
//
//                    choose_file = (AppCompatTextView) dialog.findViewById(R.id.choose_file);
//                    ratingBar = (RatingBar) dialog.findViewById(R.id.rating);
//                    review_text = (EditText) dialog.findViewById(R.id.review);
//                    pic_file = (ImageView) dialog.findViewById(R.id.pick_file);
//                    Submit = (Button) dialog.findViewById(R.id.btnreview);
//
//
//
//                    pic_file.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            //pickImageSingle();
//                            int result = ContextCompat.checkSelfPermission(ProfileView.this, Manifest.permission.CAMERA);
//                            int result1 = ContextCompat.checkSelfPermission(ProfileView.this, Manifest.permission.READ_EXTERNAL_STORAGE);
//                            if ( result == PackageManager.PERMISSION_GRANTED) {
//                                if (result1 == PackageManager.PERMISSION_GRANTED) {
//                                    takePhoto();
//                                } else {
//                                    alert();
//                                }
//                            } else {
//                                buildAlertMessageNoCamera();
//                            }
//                        }
//                        private void takePhoto() {
//                            try {
//                                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileView.this);
//                                builder.setTitle(Constants.ProfileFragment.ADD_PHOTO);
//                                builder.setItems(items, new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int item) {
//
//                                        if (items[item].equals(Constants.ProfileFragment.TAKE_PHOTO)) {
//
//                                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                            //    buildAlertMessageNoCamera();
//                                            startActivityForResult(cameraIntent, CAMERA_REQUEST_PROOF);
//                                            //Glide.with(getActivity()).load(ObjectFactory.getInstance().getLoginManager(getContext()).getUserPhoto()).fitCenter().into(mProfilePic);
//
//                                        } else if (items[item].equals(Constants.ProfileFragment.FROM_LIBRARY)) {
//
//                                            Intent galleryintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                                            startActivityForResult(galleryintent, GALLERY_REQUEST_PROOF);
//
//                                        } else if (items[item].equals(Constants.ProfileFragment.CANCEL)) {
//                                            dialog.dismiss();
//                                        }
//                                    }
//                                });
//                                builder.show();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        private void alert() {
//                            ActivityCompat.requestPermissions(ProfileView.this,
//                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                                    REQUEST_WRITE_PERMISSION);
//                        }
//
//                        private void buildAlertMessageNoCamera() {
//                            ActivityCompat.requestPermissions(ProfileView.this,
//                                    new String[]{Manifest.permission.CAMERA},
//                                    MY_PERMISSIONS_REQUEST_CAMERA);
//
//                        }
//                    });
//
//
//                    ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//                        @Override
//                        public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
//                            float rate = ratingBar.getRating();
//                            System.out.println("ProfileView.onRatingChanged " + rate);
//                        }
//                    });
//
//                    Submit.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            String review = review_text.getText().toString().trim();
//                            float rate = ratingBar.getRating();
//                            String rating = String.valueOf((int) rate);
//
//                            if (review != null && review.length() < 10) {
//                                review_text.setBackgroundResource(R.drawable.red_alert);
//                                Toast.makeText(ProfileView.this, "Minimum 10 characters required for review", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//
//                            if (!review.isEmpty() && !rating.isEmpty()) {
//                                setReviewApi(rating, rate, review, dialog);
//                            } else {
//                                Toast.makeText(getApplicationContext(),
//                                        "Please enter the credentials!", Toast.LENGTH_LONG)
//                                        .show();
//                            }
//                        }
//                    });

//                    dialog.show();
//                }
                    dialogFrag = new MyDialogFragment();
                    FragmentManager fm = getFragmentManager();
                    dialogFrag.show(fm, "dialog");
                }

            });

        } else {
            fab_review.setOnClickListener(new View.OnClickListener() {
                private Button btnLogin;
                private TextView forgotPassword;
                private TextView Signup;

                @Override
                public void onClick(View v) {

                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(ProfileView.this);
                    View mView = getLayoutInflater().inflate(R.layout.dialogue_login, null);

                    final EditText inputEmail = (EditText) mView.findViewById(R.id.email);
                    final EditText inputPassword = (EditText) mView.findViewById(R.id.password);

                    forgotPassword = (TextView) mView.findViewById(R.id.forgot);
                    Signup = (TextView) mView.findViewById(R.id.signup);

                    forgotPassword.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent to_forget = new Intent(getApplicationContext(), ForgetPassword.class);
                            startActivity(to_forget);
                        }
                    });

                    Signup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(getApplicationContext(),
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

                                    checkLogin(email, password);

                                }
                            } else {
                                Toast.makeText(getApplicationContext(),
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

                        // Login form for Add Review
                        private void checkLogin(final String email, final String password) {

                            String tag_string_req = "req_login";
                            //pDialog.setMessage("Logging in ...");
                            //  showDialog();

                            StringRequest strReq = new StringRequest(Request.Method.POST,
                                    AppConfig.URL_LOGIN, new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    Log.e(TAG, "Login Response: " + response.toString());
                                    // hideDialog();

                                    try {
                                        JSONObject jObj = new JSONObject(response);
                                        boolean error = jObj.getBoolean("error");
                                        if (!error) {
                                            session.setLogin(true);

                                            JSONObject user = jObj.getJSONObject("user");
                                            String name = user.getString("name");
                                            String uid = user.getString("uid");
                                            String email = user.getString("email");
                                            String token = user.getString("token");
                                            String verify_code = user.getString("verify_code");
                                            boolean result = db.addUser(name, email, uid, token,verify_code,password);
                                            if (result) {
                                                Intent get_id = getIntent();
                                                String profile_id = get_id.getStringExtra("reg_id");
                                                Intent to_profile = new Intent(ProfileView.this, ProfileView.class);
                                                to_profile.putExtra("reg_id", profile_id);
                                                startActivity(to_profile);
                                                finish();
                                            }
                                        } else {
                                            String errorMsg = jObj.getString("error_msg");
                                            Toast.makeText(getApplicationContext(),
                                                    errorMsg, Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        // JSON error
                                        e.printStackTrace();
                                        Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    }

                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Intent i3 = new Intent(ProfileView.this, Internetconnection.class);
                                    i3.putExtra("current_class", "Loginactivity");
                                    Log.e(TAG, "Login Error: " + error.getMessage());
                                    Toast.makeText(getApplicationContext(),
                                            "Please connect to internet", Toast.LENGTH_LONG).show();
                                    startActivity(i3);
                                    // hideDialog();
                                    finish();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() {
                                    // Posting parameters to login url
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("email", email);
                                    params.put("password", password);
                                    return params;
                                }

                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    Map headers = new HashMap();
                                    headers.put("Client-Service", "app-client");
                                    headers.put("Auth-Key", "123321");
                                    return headers;
                                }
                            };
                            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
                        }
                    });

                    mBuilder.setView(mView);
                    AlertDialog dialog = mBuilder.create();
                    dialog.show();

                }
            });
        }

        if (session.isLoggedIn() && db_id != null){
//            report_profile.setOnClickListener(new View.OnClickListener() {
//
//                private EditText report_message;
//                private Button save;
//
//                @Override
//                public void onClick(View v) {
//
//                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(ProfileView.this);
//                    View mView = getLayoutInflater().inflate(R.layout.dialogue_report, null);
//                    report_message = (EditText) mView.findViewById(R.id.report_text);
//
//
//
//
//
//                    save = (Button) mView.findViewById(R.id.report_save);
//
//                    save.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            String report_text =report_message.getText().toString();
//
//                            if(report_text!=null && report_text.length()<50) {
//                                report_message.setError("Minimum 50 characters required for message.");
//                                return;
//                            }
//
//                            if (!report_text.isEmpty()) {
//                                report_abuse(report_text);
//                            } else {
//
//                                Toast.makeText(getApplicationContext(),
//                                        "Please enter the credentials!", Toast.LENGTH_LONG)
//                                        .show();
//
//                            }
//
//                        }
//
//                        private void report_abuse(final String report_text) {
//
//                            db = new SQLiteHandler(getApplicationContext());
//                            HashMap<String, String> user = db.getUserDetails();
//                            final String user_id = user.get("uid");
//                            final String token = user.get("token");
//                            Intent get_id = getIntent();
//                            final String profile_id = get_id.getStringExtra("reg_id");
//
//                            String tag_string_req = "req_register";
//
//                            StringRequest strReq = new StringRequest(Request.Method.POST,
//                                    URL_REPORT_ABUSE, new Response.Listener<String>() {
//                                @Override
//                                public void onResponse(String response) {
//
//                                    try {
//                                        JSONObject jObj = new JSONObject(response);
//                                        boolean error = jObj.getBoolean("error");
//                                        if (!error) {
//                                            Intent to_profile = new Intent(ProfileView.this, ProfileView.class);
//                                            startActivity(to_profile);
//                                            finish();
//                                            Toast.makeText(getApplicationContext(),
//                                                    "report submit successfully", Toast.LENGTH_LONG).show();
//
//                                        } else {
//                                            String errorMsg = jObj.getString("error_msg");
//                                            Toast.makeText(getApplicationContext(),
//                                                    errorMsg, Toast.LENGTH_LONG).show();
//                                        }
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//
//                            }, new Response.ErrorListener() {
//
//                                @Override
//                                public void onErrorResponse(VolleyError error) {
//
//                                    Log.e(TAG, "Registration Error: " + error.getMessage());
//                                    Toast.makeText(getApplicationContext(),
//                                            error.getMessage(), Toast.LENGTH_LONG).show();
//                                    //hideDialog();
//                                }
//                            }) {
//
//                                @Override
//                                protected Map<String, String> getParams() {
//                                    Map<String, String> params = new HashMap<String, String>();
//                                    params.put("user_id", user_id);
//                                    params.put("profile_id", profile_id);
//                                    params.put("message", report_text);
//                                    return params;
//                                }
//
//                                @Override
//                                public Map<String, String> getHeaders() throws AuthFailureError {
//                                    Map headers = new HashMap();
//                                    headers.put("Client-Service", "app-client");
//                                    headers.put("Auth-Key", "123321");
//                                    headers.put("Token", token);
//                                    headers.put("User-Id", user_id);
//                                    return headers;
//                                }
//                            };
//                            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
//                        }
//                    });
//
//                    mBuilder.setView(mView);
//                    AlertDialog dialog = mBuilder.create();
//                    dialog.show();
//                }
//
//
//            });
            report_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    init_modal_bottomsheet();
                }
            });
        }else{
            report_profile.setOnClickListener(new View.OnClickListener() {

                private Button btnLogin;
                private TextView forgotPassword;
                private TextView Signup;
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(ProfileView.this);
                    View mView = getLayoutInflater().inflate(R.layout.dialogue_login, null);

                    final EditText inputEmail = (EditText) mView.findViewById(R.id.email);
                    final EditText inputPassword = (EditText) mView.findViewById(R.id.password);

                    forgotPassword = (TextView) mView.findViewById(R.id.forgot);
                    Signup = (TextView) mView.findViewById(R.id.signup);

                    forgotPassword.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent to_forget = new Intent(getApplicationContext(), ForgetPassword.class);
                            startActivity(to_forget);
                        }
                    });

                    Signup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(getApplicationContext(),
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

                                    checkLogin(email, password);

                                }
                            } else {
                                Toast.makeText(getApplicationContext(),
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

                        // Login form for Add Review
                        private void checkLogin(final String email, final String password) {

                            String tag_string_req = "req_login";
                            //pDialog.setMessage("Logging in ...");
                            //  showDialog();

                            StringRequest strReq = new StringRequest(Request.Method.POST,
                                    AppConfig.URL_LOGIN, new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    Log.e(TAG, "Login Response: " + response.toString());
                                    // hideDialog();

                                    try {
                                        JSONObject jObj = new JSONObject(response);
                                        boolean error = jObj.getBoolean("error");
                                        if (!error) {
                                            session.setLogin(true);

                                            JSONObject user = jObj.getJSONObject("user");
                                            String name = user.getString("name");
                                            String uid = user.getString("uid");
                                            String email = user.getString("email");
                                            String token = user.getString("token");
                                            String verify_code = user.getString("verify_code");
                                            boolean result = db.addUser(name, email, uid, token,verify_code,password);
                                            if (result) {
                                                Intent get_id = getIntent();
                                                String profile_id = get_id.getStringExtra("reg_id");
                                                Intent to_profile = new Intent(ProfileView.this, ProfileView.class);
                                                to_profile.putExtra("reg_id", profile_id);
                                                startActivity(to_profile);
                                                finish();
                                            }

                                        } else {
                                            String errorMsg = jObj.getString("error_msg");
                                            Toast.makeText(getApplicationContext(),
                                                    errorMsg, Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        // JSON error
                                        e.printStackTrace();
                                        Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    }

                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Intent i3 = new Intent(ProfileView.this, Internetconnection.class);
                                    i3.putExtra("current_class", "Loginactivity");
                                    Log.e(TAG, "Login Error: " + error.getMessage());
                                    Toast.makeText(getApplicationContext(),
                                            "Please connect to internet", Toast.LENGTH_LONG).show();
                                    startActivity(i3);
                                    // hideDialog();
                                    finish();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() {
                                    // Posting parameters to login url
                                    Map<String, String> params = new HashMap<String, String>();

                                    params.put("email", email);
                                    params.put("password", password);
                                    return params;
                                }

                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    Map headers = new HashMap();
                                    headers.put("Client-Service", "app-client");
                                    headers.put("Auth-Key", "123321");
                                    return headers;
                                }
                            };
                            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
                        }


                    });

                    mBuilder.setView(mView);
                    AlertDialog dialog = mBuilder.create();
                    dialog.show();

                }
            });
        }

        tw = (TabWidget) findViewById(android.R.id.tabs);
//tabhost for aboutme,description
        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(mlam);

        tabHost1 = (TabHost) findViewById(R.id.mySecondTabhost);
        tabHost1.setup(mlam);


        tabHost2 = (TabHost) findViewById(R.id.myThirdTabhost);
        tabHost2.setup(mlam);


        tabHost3 = (TabHost) findViewById(R.id.myFourthTabhost);
        tabHost3.setup(mlam);

        tabHost4 = (TabHost) findViewById(R.id.myFifthTabhost);
        tabHost4.setup(mlam);



//        TabHost.TabSpec ReviewSpec = tabHost3.newTabSpec(REVIEW_SPEC);
//        Intent reviewIntent = new Intent(ProfileView.this, PortfolioReview.class);


        if (get_id.getStringExtra("reg_id") != null) {
            String user_id = get_id.getStringExtra("reg_id");


        } else {
            String user_id = user.get("uid");
        }

        if (!session.isLoggedIn()) {
            if (get_id.getStringExtra("reg_id") != null) {
                String user_id = get_id.getStringExtra("reg_id");
            }
        } else {
            if (get_id.getStringExtra("reg_id") != null) {
                String user_id = get_id.getStringExtra("reg_id");
            } else {
                String user_id = user.get("uid");
            }
        }

        registerQbChatListeners();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unregisterQbChatListeners();
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        isMessageArrived();
    }
    private void registerQbChatListeners() {
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

    private ProgressDialog mDialogo;

    private void createChatSesion() {
        mDialogo = new ProgressDialog(this);
        mDialogo.setMessage("Loading...");
        mDialogo.setCanceledOnTouchOutside(false);
       // mDialogo.show();

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
                        //  showQbUserInformation();
                    } else {
                        QBChatService.getInstance().login(qbUser, new QBEntityCallback() {
                            @Override
                            public void onSuccess(Object o, Bundle bundle) {
                                privacyListsManager=QBChatService.getInstance().getPrivacyListsManager();
                                mDialogo.dismiss();
                            }

                            @Override
                            public void onError(QBResponseException e) {
                                mDialogo.dismiss();

                            }
                        });
                    }
                }catch (Exception e)
                {
                    mDialogo.dismiss();
                }
            }

            @Override
            public void onError(QBResponseException e) {
                mDialogo.dismiss();
            }
        });

    }

    private void showQbUserInformation(){
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
                    if (!users.isEmpty() && users.size() > 0) {
                        openPrivateChatWindow(users);
                    } else {

                     //   mDialogo.dismiss();
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "User must install new app, than logout and login again.", Snackbar.LENGTH_LONG);
                        View sbView = snackbar.getView();
                        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.YELLOW);
                        snackbar.show();
                    }
                }
                @Override
                public void onError(QBResponseException errors) {
                    mDialogo.dismiss();
                }
            });
        } catch (Exception e) {
            mDialogo.dismiss();
        }
    }
    private void openPrivateChatWindow(ArrayList<QBUser> selectedUsers){

        try {
            if (isPrivateDialogExist(selectedUsers)) {
                mDialogo.dismiss();
                selectedUsers.remove(ChatHelper.getCurrentUser());
                QBChatDialog existingPrivateDialog = QbDialogHolder.getInstance().getPrivateDialogWithUser(selectedUsers.get(0));
          //           isProcessingResultInProgress = false;
                Intent intent = new Intent(ProfileView.this, ChatNewActivity.class);
                intent.putExtra(ChatNewActivity.EXTRA_DIALOG_ID, existingPrivateDialog);
                intent.putExtra("from","profile");
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);


            } else {
               // ProgressDialogFragment.show(getSupportFragmentManager(), R.string.create_chat);
                createDialog(selectedUsers);
            }
        } catch (Exception e) {
            mDialogo.dismiss();
        }
    }

    private void createDialog(final ArrayList<QBUser> selectedUsers) {
        try {
            ChatHelper.getInstance().createDialogWithSelectedUsers(selectedUsers,
                    new QBEntityCallback<QBChatDialog>() {
                        @Override
                        public void onSuccess(QBChatDialog dialog, Bundle args) {
                            mDialogo.dismiss();
                            //isProcessingResultInProgress = false;
                           dialogsManager.sendSystemMessageAboutCreatingDialog(systemMessagesManager, dialog);
                            Intent intent = new Intent(ProfileView.this, ChatNewActivity.class);
                            intent.putExtra(ChatNewActivity.EXTRA_DIALOG_ID, dialog);
                            intent.putExtra("from","profile");
                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }

                        @Override
                        public void onError(QBResponseException e) {
                            mDialogo.dismiss();
                            // isProcessingResultInProgress = false;
                            //ProgressDialogFragment.hide(getSupportFragmentManager());
                            //showErrorSnackbar(R.string.dialogs_creation_error, null, null);
                        }
                    }
            );
        } catch (Exception e) {
            mDialogo.dismiss();
        }
    }

    private boolean isPrivateDialogExist(ArrayList<QBUser> allSelectedUsers) {
        ArrayList<QBUser> selectedUsers = new ArrayList<>();
        selectedUsers.addAll(allSelectedUsers);
        selectedUsers.remove(ChatHelper.getCurrentUser());
        return selectedUsers.size() == 1 && QbDialogHolder.getInstance().hasPrivateDialogWithUser(selectedUsers.get(0));
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == CAMERA_REQUEST_PROOF) {
//            final Bitmap photo;
//            try {
//                photo = (Bitmap) data.getExtras().get("data");
//                bitmap = photo;
//                imageRating = getStringImage(bitmap);
//                File photopath = null;
//                photopath = saveToInternalStorage(photo);
//                //  choose_file.setText("1 item selected");
//                try {
//                    choose_file.setText("1 item selected");
//                } catch (Exception e) {
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else if (requestCode == GALLERY_REQUEST_PROOF && (data != null)) {
//            try {
//                Uri selected_image = data.getData();
//                String[] filepath = {MediaStore.Images.Media.DATA};
//                Cursor cursor = getApplicationContext().getContentResolver().query(selected_image, filepath, null, null, null);
//                cursor.moveToFirst();
//                int columIntex = cursor.getColumnIndex(filepath[0]);
//                String filePath = cursor.getString(columIntex);
//                cursor.close();
//                Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
//                bitmap = yourSelectedImage;
//                imageRating = getStringImage(bitmap);
//
////                if (getStringImage(bitmap).isEmpty()) {
////
////                  imageRating = "no";
////
////                    Log.e("reviewimage",imageRating);
////                } else {
////                    imageRating = getStringImage(bitmap);
////                    Log.e("reviewimagecheckingggg",imageRating);
////              }
//                //  choose_file.setText("1 item selected");
//                try {
//                    choose_file.setText("1 item selected");
//                } catch (Exception e) {
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

//    private File saveToInternalStorage(Bitmap bitmap) throws IOException {
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//            }
//        }
//        ContextWrapper cw = new ContextWrapper(this);
//        File directory = cw.getDir("patientphoto", Context.MODE_PRIVATE);
//        Random rand = new Random();
//        int num = rand.nextInt(5000) + 1;
//        File path = new File(directory, "" + num + ".jpg");
//        FileOutputStream stream = null;
//
//        try {
//            stream = new FileOutputStream(path);
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            stream.close();
//        }
//        return path;
//    }
//
//    private String getStringImage(Bitmap bmp) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] imageBytes = baos.toByteArray();
//        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
//        return encodedImage;
//    }

    private void setReviewApi(String rating, float rate, String review,String imageRating ) {

        System.out.println("ProfileView.setReviewApi profileId" + profileId);

        Log.e("RevieImagechecking",imageRating);
        System.out.println("ProfileView.setReviewApi prof" + ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId());
        final Dialog dialog = ObjectFactory.getInstance().getUtils(ProfileView.this).showLoadingDialog(ProfileView.this);
        dialog.show();
        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(ProfileView.this).getApiService().setRating(
                "app-client",
                "123321",
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getLoginToken(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                profileId,
                review,
                rating,
                imageRating,
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserName()
        );
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                dialog.dismiss();
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        if (responseString != null) {
                            Log.e("Profile_review", responseString);
                            JSONObject jsonObject = new JSONObject(responseString);
                            if (jsonObject != null) {
                                System.out.println("ProfileView.onResponse " + responseString);
                                if (!jsonObject.getBoolean("error")) {
                                    //dialogParent.dismiss();
                                    Toast.makeText(ProfileView.this, "Successfully reviewed", Toast.LENGTH_SHORT).show();
                                    Intent to_profile = new Intent(ProfileView.this, ProfileView.class);
                                    to_profile.putExtra("reg_id",profileId);
                                    startActivity(to_profile);
                                    finish();
                                    // apiCallToUpdateProfileDatas();

                                } else {
                                    Toast.makeText(ProfileView.this, "Some error occurred", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ProfileView.this, "Network Error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

    }

    //For maintaining the lifecycle without breaking when loacl activity manger is adding

    private void profileuser(final String uid) {
        String tag_string_req = "req_profile";
        Log.e(TAG, "Profile Responsescheck: " + uid);
        HashMap<String, String> user = db.getUserDetails();
        final String db_id = (user.get("uid") == null) ? "null" : user.get("uid");
        Log.e("uidcheck", db_id);
        final Dialog dialog = ObjectFactory.getInstance().getUtils(ProfileView.this).showLoadingDialog(ProfileView.this);
        dialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    swipe.setRefreshing(false);

                } catch (Exception e) {
                }
                Log.e(TAG, "Profile Responses: " + response.toString());
                Profileresponse profileresponse = new Gson().fromJson(response, Profileresponse.class);
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).setProfileResponse(response);

                try {
                    JSONObject jObj = new JSONObject(response);
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

                        if (verify.matches("0")) {
                            user_button.setVisibility(View.VISIBLE);
                        } else {
                            user_button.setVisibility(View.GONE);
                        }

                        Log.e("check ver",verify);

                        final String finalProfile_image1 = profile_image;
                        fabSetting.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent to_prof_settings = new Intent(ProfileView.this, Profile_settings.class);
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
                                Intent to_edit_review = new Intent(ProfileView.this, Edit_Review_Postedbyme.class);
                                startActivity(to_edit_review);
                            }
                        });

                        editdetails.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent to_editdetails = new Intent(ProfileView.this, EditDetails.class);
                                to_editdetails.putExtra("user_id", uid);
                                to_editdetails.putExtra("profile_img", finalProfile_image);
                                to_editdetails.putExtra("name", finalName);
                                to_editdetails.putExtra("dob", finalDob);
                                to_editdetails.putExtra("phone", finalContactnumber);
                                to_editdetails.putExtra("location", finalLocation);
                                to_editdetails.putExtra("profile_url", finalProfile_url);
                                to_editdetails.putExtra("gender", gender);
                                startActivity(to_editdetails);
                                //   finish();
                            }
                        });

                        if (role.equals("Hirer")) {

                            profile_image = user.getString("profile_img");
                            name = user.getString("name");
                            dob = (user.getString("dob")!= "null") ? user.getString("dob") : "";
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
                            Intent review_by_me_Intent = new Intent(ProfileView.this, Edit_reviewpostedbyhirer.class);
                            review_by_me_Intent.putExtra("review_by_me", reviewbyme);
                            review_by_me_Intent.putExtra("user_id", uid);
                            Log.e(TAG, "reviewCheck: " + reviewbyme);
                            review_by_me.setIndicator(REVIEWBY_ME_SPEC);
                            review_by_me.setContent(review_by_me_Intent);
                            tabHost4.addTab(review_by_me);
                            noPortfolio.setVisibility(View.GONE);


                        } else {
                            // tabHost4.setVisibility(View.GONE);
                            //skills
                            TabWidget widget = tabHost1.getTabWidget();
                            String skills = user.getString("skills");
                            TabHost.TabSpec skillSpec = tabHost1.newTabSpec(SKILL_SPEC);
                            Intent skillIntent = new Intent(ProfileView.this, SkillTab.class);
                            skillIntent.putExtra("skills", skills);
                            skillIntent.putExtra("user_id", uid);
                            skillIntent.putExtra("category", category);
                            skillSpec.setIndicator(SKILL_SPEC);
                            skillSpec.setContent(skillIntent);
                            tabHost1.addTab(skillSpec);
//                            review_post.setVisibility(View.GONE);
//                            post_review.setVisibility(View.GONE);
//                            underline.setVisibility(View.GONE);
                            //  rvReviews_hire.setVisibility(View.GONE);


//                            for(int i = 0; i < widget.getChildCount(); i++) {
//                                View v = widget.getChildAt(i);
//
//                                // Look for the title view to ensure this is an indicator and not a divider.
//                                TextView tv = (TextView)v.findViewById(android.R.id.title);
//                                if(tv == null) {
//                                    continue;
//                                }
//                                v.setBackgroundResource(R.drawable.tab_selected_holo);
//                            }



//
//                            for (int i = 0; i < widget.getChildCount(); i++){
//
//
//
//                                tabHost1.getTabWidget().getChildAt(tabHost1.getCurrentTab()).getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);
//
//                            }

                            // language

                            String language = user.getString("language");
                            TabHost.TabSpec languageSpec = tabHost1.newTabSpec(LANGUAGE_SPEC);
                            Intent languageIntent = new Intent(ProfileView.this, LanguageTab.class);
                            languageIntent.putExtra("language", language);
                            languageIntent.putExtra("user_id", uid);
                            languageSpec.setIndicator(LANGUAGE_SPEC);
                            languageSpec.setContent(languageIntent);
                            tabHost1.addTab(languageSpec);
                            //tabHost1.getTabWidget().getChildAt(tabHost1.getCurrentTab()).getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);

                            //    social media

                            String socialmedia = user.getString("socialmedia");
                            TabHost.TabSpec socialmediaSpec = tabHost1.newTabSpec(SOCIALMEDIA_SPEC);
                            Intent socialmediaIntent = new Intent(ProfileView.this, SocialMedia.class);
                            socialmediaIntent.putExtra("socialmedia", socialmedia);
                            socialmediaIntent.putExtra("user_id", uid);
                            Log.e(TAG, "mediaCheck: " + socialmedia);
                            socialmediaSpec.setIndicator(SOCIALMEDIA_SPEC);
                            socialmediaSpec.setContent(socialmediaIntent);
                            tabHost1.addTab(socialmediaSpec);
//                            tabHost1.getTabWidget().getChildAt(tabHost1.getCurrentTab()).getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);


                            // portfolio image
                            JSONArray portfolio_image = user.getJSONArray("portfolio_image");
                            int image_count = portfolio_image.length();
                            TabHost.TabSpec ImagesSpec = tabHost2.newTabSpec(IMAGES_SPEC);
                            Intent imagesIntent = new Intent(ProfileView.this, PortfolioImage.class);
                            imagesIntent.putExtra("user_id", uid);
                            imagesIntent.putExtra("portfolio_image", String.valueOf(portfolio_image));

                            if (image_count > 0 || db_id.equals(uid)) {
                                ImagesSpec.setIndicator("", getResources().getDrawable(R.drawable.phototab));
                                ImagesSpec.setContent(imagesIntent);
                                tabHost2.addTab(ImagesSpec);
                            }

                            // portfolio video
                            JSONArray portfolio_video = user.getJSONArray("portfolio_video");
                            int video_count = portfolio_video.length();
                            Log.e(TAG, "videoCheck1: " + portfolio_video);
                            TabHost.TabSpec VideoSpec = tabHost2.newTabSpec(VIDEOS_SPEC);
                            Intent VideoIntent = new Intent(ProfileView.this, PortfolioVideo.class);
                            VideoIntent.putExtra("user_id", uid);
                            VideoIntent.putExtra("portfolio_video", String.valueOf(portfolio_video));

                            if (video_count > 0 || db_id.equals(uid)) {
                                VideoSpec.setContent(VideoIntent);
                                VideoSpec.setIndicator("", getResources().getDrawable(R.drawable.videotab));
                                tabHost2.addTab(VideoSpec);
                            }

                            // portfolio audio

                            JSONArray portfolio_audio = user.getJSONArray("portfolio_audio");
                            int audio_count = portfolio_audio.length();
                            TabHost.TabSpec AudioSpec = tabHost2.newTabSpec(AUDIO_SPEC);
                            Intent AudioIntent = new Intent(ProfileView.this, PortfolioAudio.class);
                            AudioIntent.putExtra("user_id", uid);
                            AudioIntent.putExtra("portfolio_audio", String.valueOf(portfolio_audio));

                            if (audio_count > 0 || db_id.equals(uid)) {
                                AudioSpec.setContent(AudioIntent);
                                AudioSpec.setIndicator("", getResources().getDrawable(R.drawable.audiotab));
                                tabHost2.addTab(AudioSpec);
                            }
                            // portfolio file
                            JSONArray portfolio_doc = user.getJSONArray("portfolio_doc");
                            int document_count = portfolio_doc.length();
                            Log.e(TAG, "portdoccount: " + document_count);
                            TabHost.TabSpec FileSpec = tabHost2.newTabSpec(FILE_SPEC);
                            Intent FileIntent = new Intent(ProfileView.this, PortfolioFile.class);
                            FileIntent.putExtra("user_id", uid);
                            FileIntent.putExtra("portfolio_doc", String.valueOf(portfolio_doc));
                            Log.e(TAG, "docCheck1: " + portfolio_doc);

                            if (document_count > 0 || db_id.equals(uid)) {
                                FileSpec.setContent(FileIntent);
                                FileSpec.setIndicator("", getResources().getDrawable(R.drawable.texttab));
                                tabHost2.addTab(FileSpec);
                            }
                            // portfolio link
                            JSONArray portfolio_link = user.getJSONArray("portfolio_link");
                            int link_count = portfolio_link.length();
                            TabHost.TabSpec OtherSpec = tabHost2.newTabSpec(OTHER_SPEC);
                            Intent OtherIntent = new Intent(ProfileView.this, OtherFile.class);
                            OtherIntent.putExtra("user_id", uid);
                            OtherIntent.putExtra("portfolio_link", String.valueOf(portfolio_link));
                            Log.e(TAG, "linkCheck1: " + portfolio_link);


                            if (link_count > 0 || db_id.equals(uid)) {
                                OtherSpec.setContent(OtherIntent);
                                OtherSpec.setIndicator("", getResources().getDrawable(R.drawable.linktab));
                                tabHost2.addTab(OtherSpec);
                            }

                            if (image_count == 0 && video_count == 0 && audio_count == 0 && document_count == 0 && link_count == 0 && !db_id.equals(uid)) {
                                noPortfolio.setVisibility(View.VISIBLE);
                                Log.e(TAG, "tabempty ");
                                ImagesSpec.setIndicator("", getResources().getDrawable(R.drawable.phototab));
                                ImagesSpec.setContent(imagesIntent);
                                tabHost2.addTab(ImagesSpec);
                            } else {
                                noPortfolio.setVisibility(View.GONE);
                            }
                        }

                        if (db_id.equals(uid)) {
                            fab.setVisibility(View.VISIBLE);
                        }

                        String review = user.getString("review");
                        TabHost.TabSpec reviewSpec = tabHost3.newTabSpec(REVIEW_SPEC);
                        Intent reviewIntent = new Intent(ProfileView.this, PortfolioReview.class);
                        reviewIntent.putExtra("review", review);
                        reviewIntent.putExtra("user_id", uid);
                        Log.e(TAG, "reviewCheck: " + review);
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

                            TextView tv = new TextView(getApplicationContext());

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
                        Intent descrptionIntent = new Intent(ProfileView.this, Description.class);
                        descrptionIntent.putExtra("description", description);
                        descrptionIntent.putExtra("user_id", uid);
                        descriptionSpec.setIndicator(description_spec);
                        descriptionSpec.setContent(descrptionIntent);
                        tabHost.addTab(descriptionSpec);

                        String services = user.getString("services");
                        TabHost.TabSpec servicesSpec = tabHost.newTabSpec(service_spec);
                        Intent serviceIntent = new Intent(ProfileView.this, Services.class);
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
                                String shareBody = "http://www.bawabba.com/" + finalProfile_url1;
                                String shareSub = finalName1;
                                myintent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                                myintent.putExtra(Intent.EXTRA_TEXT, shareBody);
                                startActivity(Intent.createChooser(myintent, "Share Using"));
                            }
                        });

                        txtName.setText(name);
                        txtLocation.setText(location);
                        txtName.requestFocusFromTouch();

                        Glide.with(getApplicationContext())   // pass Context
                                .load(profile_image)    // pass the image url
                                .transform(new CircleTransform(getApplicationContext()))
                                .into(txtImage);
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

//                Intent i3=new Intent(ProfileView.this,Internetconnection.class);
//                i3.putExtra("current_class", "Loginactivity");
//                Log.e(TAG, "Login Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(),
//                        "Please connect to internet", Toast.LENGTH_LONG).show();
//                startActivity(i3);
//                finish();

            }
        })

        {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to profile url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", uid);
                params.put("db_id", (db_id != null) ? db_id : "no");
                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map headers = new HashMap();
                headers.put("Client-Service", "app-client");
                headers.put("Auth-Key", "123321");
                return headers;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    protected void onResume() {
        super.onResume();
        mlam.dispatchResume();
        unread_notification();
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");

        if(prev == null){
            recreate();
        }

    }

    protected void onPause() {
        super.onPause();
        mlam.dispatchPause(isFinishing());
    }
    private void logoutUser() {
        db.deleteUsers();
        session.setLogin(false);
        Intent intent = new Intent(ProfileView.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        HashMap<String, String> user = db.getUserDetails();
        final String db_id = (user.get("uid") == null) ? "null" : user.get("uid");

        if(session.isLoggedIn()&&db_id.equals(userRegID))  {
            Intent intent = new Intent(ProfileView.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finishAffinity();
        }else{
            super.onBackPressed();
        }

    }

//    public void pickImageSingle() {
//        imagePicker = new ImagePicker(this);
//        imagePicker.setFolderName("Random");
//        imagePicker.setRequestId(1234);
//        imagePicker.ensureMaxSize(500, 500);
//        imagePicker.shouldGenerateMetadata(true);
//        imagePicker.shouldGenerateThumbnails(true);
//        imagePicker.setImagePickerCallback(this);
//        Bundle bundle = new Bundle();
//        bundle.putInt("android.intent.extras.CAMERA_FACING", 1);
//        imagePicker.pickImage();
//    }

//    @Override
//    public void onImagesChosen(List<ChosenImage> list) {
//        imageRating = list.get(0).getOriginalPath();
//    }
//
//    @Override
//    public void onError(String s) {
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == 1) {
//            if (requestCode == Picker.PICK_IMAGE_DEVICE) {
//                if (imagePicker == null) {
//                    imagePicker = new ImagePicker(this);
//                    imagePicker.setImagePickerCallback(this);
//                }
//                imagePicker.submit(data);
//            }
//        }
//    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
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
        setReviewApi(rating,rate,review,imageRating);

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
        dialog = new BottomSheetDialog(this);
        dialog.setContentView(modalbottomsheet);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        btn_reportuser = (Button) modalbottomsheet.findViewById(R.id.btn_reportuser);
        btn_reportuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText report_message;
                Button save;
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ProfileView.this);
                View mView = getLayoutInflater().inflate(R.layout.dialogue_report, null);
                report_message = (EditText) mView.findViewById(R.id.report_text);


                save = (Button) mView.findViewById(R.id.report_save);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String report_text =report_message.getText().toString();

                        if(report_text!=null && report_text.length()<50) {
                            report_message.setBackgroundResource(R.drawable.red_alert_round);
                            Toast.makeText(ProfileView.this, "Minimum 50 characters required for message.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (!report_text.isEmpty()) {
                            report_abuse(report_text);
                        } else {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(),
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
        if (QBChatService.getInstance().getPrivacyListsManager()==null){
            btn_blockuser.setVisibility(View.GONE);
        }else {

            db = new SQLiteHandler(getApplicationContext());
            HashMap<String, String> user = db.getUserDetails();
            String user_id = user.get("uid");
            String token = user.get("token");
            Intent get_id = getIntent();
            String profile_id = get_id.getStringExtra("reg_id");

            final String name = get_id.getStringExtra("fullname");
            QBPagedRequestBuilder pagedRequestBuilder = new QBPagedRequestBuilder();
            pagedRequestBuilder.setPage(1);
            pagedRequestBuilder.setPerPage(10);

            QBUsers.getUsersByFullName(name, pagedRequestBuilder).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
                @Override
                public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                    for (int i=0;i<qbUsers.size();i++){
                        if (TextUtils.equals(qbUsers.get(i).getFullName(),name)){
                            strOtherUserId=String.valueOf(qbUsers.get(0).getId());
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
                privacyListsManager=QBChatService.getInstance().getPrivacyListsManager();

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
                String id ="";

                if (item.getType() == QBPrivacyListItem.Type.USER_ID &&
                        item.getValueForType().contains(id)) {
                    if (item.isAllow()){
                        isOtherUserBlocked=false;
                        btn_blockuser.setText("Block User");
                    }else{

                        btn_blockuser.setText("UnBlock User");
                        isOtherUserBlocked=true;
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

        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        final String user_id = user.get("uid");
        final String token = user.get("token");
        Intent get_id = getIntent();
        final String profile_id = get_id.getStringExtra("reg_id");

        String tag_string_req = "req_register";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_REPORT_ABUSE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Intent to_profile = new Intent(ProfileView.this, ProfileView.class);
                        startActivity(to_profile);
                        finish();
                        Toast.makeText(getApplicationContext(),
                                "report submit successfully", Toast.LENGTH_LONG).show();

                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);
                params.put("profile_id", profile_id);
                params.put("message", report_text);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map headers = new HashMap();
                headers.put("Client-Service", "app-client");
                headers.put("Auth-Key", "123321");
                headers.put("Token", token);
                headers.put("User-Id", user_id);
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    public void addToBlockList(String userID) {
        qbPrivacyList=new QBPrivacyList();
        qbPrivacyList.setName("public");
        QBPrivacyListItem item1 = new QBPrivacyListItem();
        if (isOtherUserBlocked){
            item1.setType(QBPrivacyListItem.Type.USER_ID);

            item1.setAllow(true);
            //item1.setMutualBlock(false);
            tinyDB.putString("blockedid","");
        }else{
            tinyDB.putString("blockedid",userID);

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
            privacyListsManager=QBChatService.getInstance().getPrivacyListsManager();

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
            Log.e(TAG, "SmackException.NotConnectedException while setting privacy list :- " + e.getMessage());
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
            Log.e(TAG, "XMPPException.XMPPErrorException while setting privacy list :- " + e.getMessage());
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
            Log.e(TAG, "SmackException.NoResponseException while setting privacy list :- " + e.getMessage());
        }

        if (!isOtherUserBlocked){
            Toast.makeText(ProfileView.this,"User is Blocked now",Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(ProfileView.this,"User is UnBlocked now",Toast.LENGTH_LONG).show();
        }
        dialog.dismiss();
    }
    private void unread_notification() {
        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(getApplicationContext()).getApiService().notification("app-client",
                "123321",
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getLoginToken(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId());

        responseBodyCall.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response)
            {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        if (responseString != null) {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(responseString);
                                String count=jsonObject.getString("count");
                                if(!count.equals("0")){
                                    textBadgeItem.setText(count);
                                    textBadgeItem.show(false);
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

}