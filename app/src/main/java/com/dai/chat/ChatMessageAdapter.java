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
    private List<Message> messages;

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
                convertView = inflater.inflate(R.layout.item_chat_from_msg,parent,false);

            }
        }
        return convertView;
    }

    class ViewHolder {
        TextView time;
        TextView name;
        TextView message;
        ImageView headIcon;
    }
}
