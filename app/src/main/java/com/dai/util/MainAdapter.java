package com.dai.util;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dai.R;
import com.dai.bean.ChatMessage;
import com.dai.chat.ChatActivity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/16 0016.
 */

public class MainAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ChatMessage> chatMessages;
    private LayoutInflater inflater;

    public MainAdapter(Context context, ArrayList<ChatMessage> chatMessages) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.chatMessages = chatMessages;
    }

    @Override
    public int getCount() {
        return chatMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return chatMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_main, parent, false);
            vh.head = (ImageView) convertView.findViewById(R.id.item_room_head);
            vh.id = (TextView) convertView.findViewById(R.id.item_room_id);
            vh.content = (TextView) convertView.findViewById(R.id.item_room_content);
            vh.time = (TextView) convertView.findViewById(R.id.item_room_time);
            vh.number = (TextView) convertView.findViewById(R.id.item_room_number);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.id.setText(chatMessages.get(position).getRoomId());
        vh.content.setText(chatMessages.get(position).getContent());
        vh.time.setText(chatMessages.get(position).getTimeStamp());
        vh.number.setText(String.valueOf(chatMessages.get(position)));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewWorkflowDetail(chatMessages.get(position).getRoomId(), chatMessages.get(position).getContent());
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView head;
        TextView id;
        TextView content;
        TextView time;
        TextView number;
    }

    protected void viewWorkflowDetail(String roomId, String content) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("roomId", roomId);
        intent.putExtra("content", content);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

}
