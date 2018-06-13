package com.bawaaba.rninja4.rookie.activity.ChatFunction;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.adapters.InboxrecyclerviewAdapter;
import com.bawaaba.rninja4.rookie.helper.SQLiteHandler;
import com.bawaaba.rninja4.rookie.helper.SessionManager;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.model.inbox.Email;
import com.bawaaba.rninja4.rookie.model.inbox.InboxResponse;
import com.bawaaba.rninja4.rookie.utils.BaseFragment;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rninja4 on 10/17/17.
 */

public class InboxFragment extends BaseFragment {

;
    private RecyclerView rvInbox;
    private SQLiteHandler db;
    private SessionManager session;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inbox, container, false);
        rvInbox = (RecyclerView) rootView.findViewById(R.id.rvInbox);
        LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.VERTICAL, true);
        rvInbox.setLayoutManager(layoutManager);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiCall();
    }

    private void apiCall() {

        db = new SQLiteHandler(getActivity());
        session = new SessionManager(getActivity());

        HashMap<String, String> user = db.getUserDetails();
        final String db_id = user.get("uid");

        System.out.println("SkillEditActivity.apiCallToSaveData");

        if (session.isLoggedIn() && db_id != null) {

            Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(getActivity()).getApiService().getInbox("app-client",
                    "123321",
                    ObjectFactory.getInstance().getAppPreference(getActivity()).getUserId(),
                    ObjectFactory.getInstance().getAppPreference(getActivity()).getLoginToken(),
                    ObjectFactory.getInstance().getAppPreference(getActivity()).getUserId());
//            responseBodyCall.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    if (response.body() != null) {
//                        try {
//                            String responseString = new String(response.body().bytes());
//                            System.out.println("InboxFragment.onResponse" +responseString);
//                            Log.e("inbox reponse",responseString);
//
//                            if (responseString != null) {
//                                JSONObject jsonObject = new JSONObject(responseString);
//                                if (jsonObject != null) {
//                                    InboxResponse inboxResponse = new Gson().fromJson(responseString, InboxResponse.class);
////                                    String inbox_count= String.valueOf(inboxResponse.getEmails().size());
////                                    Log.e("Inbox_count",inbox_count);
//                                    if (!inboxResponse.getError()) {
//                                        setAdapter(inboxResponse.getEmails());
//                                    }
//                                }
//                            }
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    //Toast.makeText(getActivity(), "failed to load..", Toast.LENGTH_SHORT).show();
//                }
//            });

            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.body() != null) {
                        try {
                            String responseString = new String(response.body().bytes());
                            System.out.println("InboxFragment.onResponse" +responseString);
                            Log.e("inbox repons",responseString);
                            if (responseString != null) {
                                JSONObject jsonObject = new JSONObject(responseString);
                                if (jsonObject != null) {
                                    InboxResponse inboxResponse = new Gson().fromJson(responseString, InboxResponse.class);
//                                    String inbox_count= String.valueOf(inboxResponse.getEmails().size());
//                                    Log.e("Inbox_count",inbox_count);
                                    if (!inboxResponse.getError()) {
                                        setAdapter(inboxResponse.getEmails());
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
                    Toast.makeText(getActivity(), "failed to load..", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(),
                    "No Inbox Data!", Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void setAdapter(final List<Email> emails) {

        InboxrecyclerviewAdapter inboxrecyclerviewAdapter = new InboxrecyclerviewAdapter(getContext(), getActivity(), emails);
        rvInbox.setAdapter(inboxrecyclerviewAdapter);
        inboxrecyclerviewAdapter.setOnClickListener(new InboxrecyclerviewAdapter.AdvertisementsRecyclerViewClickListener() {
            @Override
            public void onClicked(int position, View v) {
                Intent intent = new Intent(getActivity(), InboxDetailsActivity.class);
                intent.putExtra("FROM", emails.get(position).getEmail());
                intent.putExtra("CONTACT", emails.get(position).getPhone());
                intent.putExtra("MESSAGE", emails.get(position).getMessage());
                intent.putExtra("DATE", emails.get(position).getDate());
                intent.putExtra("NAME", emails.get(position).getName());
                startActivity(intent);
            }
        });
    }
    private void initViews() {

    }
    }

