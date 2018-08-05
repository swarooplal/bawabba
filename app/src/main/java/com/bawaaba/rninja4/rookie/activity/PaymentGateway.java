//package com.example.rninja4.rookie.activity;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.View;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ProgressBar;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.example.rninja4.rookie.App.AppController;
//import com.example.rninja4.rookie.R;
//import com.example.rninja4.rookie.helper.SQLiteHandler;
//import com.example.rninja4.rookie.helper.SessionManager;
//import com.payfort.start.Card;
//import com.payfort.start.Start;
//import com.payfort.start.Token;
//import com.payfort.start.TokenCallback;
//import com.payfort.start.error.CardVerificationException;
//import com.payfort.start.error.StartApiException;
//
//import org.json.JSONObject;
//
//import java.util.EnumSet;
//import java.util.HashMap;
//import java.util.Map;
//
//import static com.example.rninja4.rookie.App.AppConfig.URL_PAYMENT;
//
//public class PaymentGateway extends AppCompatActivity implements TokenCallback {
//
//    private static final String API_OPEN_KEY = "test_open_k_02b2df032090e31cf4ee";
//    private static final String TAG = PaymentGateway.class.getSimpleName();
//
//    private EditText numberEditText;
//    private EditText monthEditText;
//    private EditText yearEditText;
//    private EditText cvcEditText;
//    private EditText ownerEditText;
//    private ProgressBar progressBar;
//    private TextView errorTextView;
//    private Button payButton;
//    private RadioGroup Subscription;
//
//    private RadioButton Subscription_1;
//
//
//    private SQLiteHandler db;
//    private SessionManager session;
//    Start start = new Start(API_OPEN_KEY);
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_payment_gateway);
//
//
//
//        numberEditText = (EditText) findViewById(R.id.numberEditText);
//        monthEditText = (EditText) findViewById(R.id.monthEditText);
//        yearEditText = (EditText) findViewById(R.id.yearEditText);
//        cvcEditText = (EditText) findViewById(R.id.cvcEditText);
//        ownerEditText = (EditText) findViewById(R.id.ownerEditText);
//        progressBar = (ProgressBar) findViewById(R.id.progressBar);
//        errorTextView = (TextView) findViewById(R.id.errorTextView);
//        payButton = (Button) findViewById(R.id.payButton);
//
//        Subscription = (RadioGroup) findViewById(R.id.subscription);
//    }
//    public void pay(View view) {
//        try {
//            Card card = unbindCard();
//
//            errorTextView.setText(null);
//            hideKeyboard();
//            showProgress(true);
//            start.createToken(this, card, this, 10 * 100, "USD");
//        } catch (CardVerificationException e) {
//            setErrors(e.getErrorFields());
//        }
//    }
//    private Card unbindCard() throws CardVerificationException {
//        clearErrors();
//        String number = unbindString(numberEditText);
//        int year = unbindInteger(yearEditText);
//        int month = unbindInteger(monthEditText);
//        String cvc = unbindString(cvcEditText);
//        String owner = unbindString(ownerEditText);
//        return new Card(number, cvc, month, year, owner);
//    }
//
//    private void clearErrors() {
//        numberEditText.setError(null);
//        monthEditText.setError(null);
//        yearEditText.setError(null);
//        cvcEditText.setError(null);
//        ownerEditText.setError(null);
//    }
//
//    private void setErrors(EnumSet<Card.Field> errors) {
//        String error = getString(R.string.edit_text_invalid);
//
//        if (errors.contains(Card.Field.NUMBER)) {
//            numberEditText.setError(error);
//        }
//        if (errors.contains(Card.Field.EXPIRATION_YEAR)) {
//            yearEditText.setError(error);
//        }
//        if (errors.contains(Card.Field.EXPIRATION_MONTH)) {
//            monthEditText.setError(error);
//        }
//        if (errors.contains(Card.Field.CVC)) {
//            cvcEditText.setError(error);
//        }
//        if (errors.contains(Card.Field.OWNER)) {
//            ownerEditText.setError(error);
//        }
//    }
//    private String unbindString(EditText editText) {
//        return editText.getText().toString().trim();
//    }
//
//    private int unbindInteger(EditText editText) {
//        try {
//            String text = unbindString(editText);
//            return Integer.parseInt(text);
//        } catch (NumberFormatException e) {
//            return -1;
//        }
//    }
//
//    private void hideKeyboard() {
//        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        View view = getCurrentFocus();
//        if (view != null) {
//            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }
//    }
//    private void showProgress(boolean progressVisible) {
//        payButton.setEnabled(!progressVisible);
//        progressBar.setVisibility(progressVisible ? View.VISIBLE : View.GONE);
//    }
//
//    @Override
//    public void onSuccess(Token token) {
//
//        String token_id = token.getId();
//        Log.d(TAG, "Token is received:" + token);
//        showProgress(false);
//        paymentprocess(token_id);
//
//    }
//
//    private void paymentprocess(final String token) {
//
//        db = new SQLiteHandler(getApplicationContext());
//        session = new SessionManager(getApplicationContext());
//
//        HashMap<String, String> user = db.getUserDetails();
//        final String user_id = user.get("uid");
//        final String email_id = user.get("email");
//
//        Subscription_1 = (RadioButton) findViewById(R.id.subscription1);
//
//        final int subcription_value = (Subscription_1.isChecked() ? 2 : 1 );
//
//        Log.e(TAG, "Subsscription:" + subcription_value);
//
//         String tag_string_req = "req_register";
//
//        StringRequest strReq = new StringRequest(Request.Method.POST,
//                URL_PAYMENT, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.e(TAG, "Register Response: " + response.toString());
//
//
//
//                try {
//                    JSONObject jObj = new JSONObject(response);
//                    boolean error = jObj.getBoolean("error");
//                    if (!error) {
//
//                        Log.e(TAG, "Register Response: " + response.toString());
//                        Toast.makeText(getApplicationContext(), "your payment was  successfull!!!", Toast.LENGTH_LONG).show();
//                        Intent to_profile = new Intent(getApplicationContext(), ProfileView.class);
//                        startActivity(to_profile);
//                        finish();
//
//                    }else{
//
//                        String errorMsg = jObj.getString("error_msg");
//                        Toast.makeText(getApplicationContext(),
//                                errorMsg, Toast.LENGTH_LONG).show();
//                    }
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                Log.e(TAG, "Registration Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
//
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//
//
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("startToken", String.valueOf(token));
//                params.put("startEmail", email_id);
//                params.put("user_id", user_id);
//               params.put("sub_plan", String.valueOf(subcription_value));
//
//
//                return params;
//            }
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map headers = new HashMap();
//                headers.put("Client-Service", "app-client");
//                headers.put("Auth-Key","123321");
//                return headers;
//            }
//        };
//        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
//    }
//
//    @Override
//    public void onError(StartApiException error) {
//        Log.e(TAG, "Error getting token", error);
//        errorTextView.setText(R.string.error);
//        showProgress(false);
//    }
//
//    @Override
//    public void onCancel() {
//
//        Log.e(TAG, "Getting token is canceled by user");
//        showProgress(false);
//
//    }
//}
