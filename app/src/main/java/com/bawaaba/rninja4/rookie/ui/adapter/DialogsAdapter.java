package com.bawaaba.rninja4.rookie.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bawaaba.rninja4.rookie.App.AppController;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.CircleTransform;
import com.bawaaba.rninja4.rookie.utils.chat.ChatHelper;
import com.bawaaba.rninja4.rookie.utils.qb.callback.QbDialogUtils;
import com.bawaaba.rninja4.rookie.utils.qb.callback.QbEntityCallbackTwoTypeWrapper;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.sample.core.ui.adapter.BaseSelectableListAdapter;
import com.quickblox.sample.core.utils.ResourceUtils;
import com.quickblox.sample.core.utils.UiUtils;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rninja4 on 12/11/17.
 */

public class DialogsAdapter extends BaseSelectableListAdapter<QBChatDialog> {

    private static final String EMPTY_STRING = "";
    private Context mContext;
    public DialogsAdapter(Context context, List<QBChatDialog> dialogs) {
        super(context, dialogs);
        mContext = context;
    }

    public DialogsAdapter(Context context, List<QBChatDialog> dialogs, QBUser qbUser) {
        super(context, dialogs);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_dialog, parent, false);

            holder = new ViewHolder();
            holder.rootLayout = convertView.findViewById(R.id.root);
            holder.nameTextView = convertView.findViewById(R.id.text_dialog_name);
            holder.lastMessageTextView = convertView.findViewById(R.id.text_dialog_last_message);
            holder.dialogImageView = convertView.findViewById(R.id.image_dialog_icon);
//            holder.txtOnlineOffline= convertView.findViewById(R.id.txtOnlineOffline);
            holder.unreadCounterTextView = convertView.findViewById(R.id.text_dialog_unread_count);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final QBChatDialog dialog = getItem(position);

        holder.nameTextView.setText(QbDialogUtils.getDialogName(dialog));
        holder.lastMessageTextView.setText(prepareTextLastMessage(dialog));

        if (dialog.getType().equals(QBDialogType.GROUP)) {
            holder.dialogImageView.setBackgroundDrawable(UiUtils.getGreyCircleDrawable());
            holder.dialogImageView.setImageResource(R.drawable.icon_user);
        } else {
            List<Integer> userIds = new ArrayList<>();
            userIds.addAll(dialog.getOccupants());
            userIds.remove(ChatHelper.getCurrentUser().getId());
                //userIds.add(dialog.getLastMessageUserId());
            QBPagedRequestBuilder requestBuilder = new QBPagedRequestBuilder(userIds.size(), 1);
            final String[] photo = {""};
            QBUsers.getUsersByIDs(userIds, requestBuilder).performAsync(
                    new QbEntityCallbackTwoTypeWrapper<ArrayList<QBUser>, ArrayList<QBChatDialog>>(null) {
                        @Override
                        public void onSuccess(ArrayList<QBUser> users, Bundle params) {
                            try {
                                QBUser user = users.get(0);
                                try {
                                    photo[0] = user.getCustomData();
                                    try {
                                        if (user.getCustomData().startsWith("http")) {
                                            photo[0] = user.getCustomData();
                                        } else {
                                            photo[0] = "http://test378.bawabba.com/assets/profilepic/" + user.getCustomData();
                                        }
                                    } catch (Exception e) {
                                      //  photo[0] = "http://test378.bawabba.com/assets/profilepic/girl-profile.jpg";
                                    }

                                    Glide.with(AppController.getInstance()).load(photo[0].toString().trim())
                                            .transform(new CircleTransform(AppController.getInstance()))
                                            .placeholder(R.drawable.user)
                                            .error(R.drawable.user)
                                            .into(holder.dialogImageView);
                                } catch (Exception e) {
                                    Glide.with(AppController.getInstance()).load(photo[0].toString().trim())
                                            .transform(new CircleTransform(mContext))
                                            .placeholder(R.drawable.user)
                                            .error(R.drawable.user)
                                            .into(holder.dialogImageView);
                                    e.printStackTrace();
                                }

//                                try {
//                                    long currentTime = System.currentTimeMillis();
//                                    long userLastRequestAtTime = user.getLastRequestAt().getTime();
//                                    if ((currentTime - userLastRequestAtTime) > 5 * 60 * 1000) {// if user didn't do anything last 5 minutes (5*60*1000 milliseconds)
//                                        // user is offline now
//                                        holder.txtOnlineOffline.setBackgroundResource(R.drawable.shape_rectangle_offline);
//                                    }else{
//                                        holder.txtOnlineOffline.setBackgroundResource(R.drawable.shape_rectangle_small);
//                                    }
//                                } catch (Exception e) {}

                            } catch (Exception e) {
                            }

                        }
                    });


        }

        int unreadMessagesCount = getUnreadMsgCount(dialog);
        if (unreadMessagesCount == 0) {
            holder.unreadCounterTextView.setVisibility(View.GONE);
        } else {
            holder.unreadCounterTextView.setVisibility(View.VISIBLE);
            holder.unreadCounterTextView.setText(String.valueOf(unreadMessagesCount > 99 ? 99 : unreadMessagesCount));
        }

        holder.rootLayout.setBackgroundColor(isItemSelected(position) ? ResourceUtils.getColor(R.color.selected_list_item_color) : ResourceUtils.getColor(android.R.color.transparent));

        return convertView;
    }

    private int getUnreadMsgCount(QBChatDialog chatDialog){
        Integer unreadMessageCount = chatDialog.getUnreadMessageCount();
        if (unreadMessageCount == null) {
            return 0;
        } else {
            return unreadMessageCount;
        }
    }

    private boolean isLastMessageAttachment(QBChatDialog dialog) {
        String lastMessage = dialog.getLastMessage();
        Integer lastMessageSenderId = dialog.getLastMessageUserId();
        return TextUtils.isEmpty(lastMessage) && lastMessageSenderId != null;
    }

    private String prepareTextLastMessage(QBChatDialog chatDialog){
        if (isLastMessageAttachment(chatDialog)){
            return context.getString(R.string.chat_attachment);
        } else {
            return chatDialog.getLastMessage();
        }
    }

    private static class ViewHolder {
        ViewGroup rootLayout;
        ImageView dialogImageView;
//        TextView txtOnlineOffline;
        TextView nameTextView;
        TextView lastMessageTextView;
        TextView unreadCounterTextView;
    }
}
