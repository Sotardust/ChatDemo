package com.dai.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dai.R;
import com.dai.bean.Message;

import java.util.List;

/**
 * Created by dai on 2017/4/19.
 */

public class ChatMessageAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private static List<Message> messages;

    public ChatMessageAdapter(Context context, List<Message> messages) {
        inflater = LayoutInflater.from(context);
        this.messages = messages;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        Message message = messages.get(position);
        if (convertView == null) {
            vh = new ViewHolder();
            if (message.isComing()) {
                convertView = inflater.inflate(R.layout.item_chat_from_msg, parent, false);
                vh.headIcon = (ImageView) convertView.findViewById(R.id.item_chat_from_headIcon);
                vh.name = (TextView) convertView.findViewById(R.id.item_chat_from_name);
                vh.message = (TextView) convertView.findViewById(R.id.item_chat_from_message);
                vh.time = (TextView) convertView.findViewById(R.id.item_chat_from_time);
            } else {
                convertView = inflater.inflate(R.layout.item_chat_send_msg, parent, false);
                vh.headIcon = (ImageView) convertView.findViewById(R.id.item_chat_send_headIcon);
                vh.name = (TextView) convertView.findViewById(R.id.item_chat_send_name);
                vh.message = (TextView) convertView.findViewById(R.id.item_chat_send_message);
                vh.time = (TextView) convertView.findViewById(R.id.item_chat_send_time);
            }
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.headIcon.setBackgroundResource(message.getHeadIcon());
        vh.name.setText(message.getName());
        vh.message.setText(message.getMessage());
        vh.time.setText(message.getDate());

        return convertView;
    }

    private class ViewHolder {
        ImageView headIcon;
        TextView name;
        TextView message;
        TextView time;
    }

    public void setMessages(Message message) {
        this.messages.add(message);
        notifyDataSetChanged();
    }
}
