package com.dai.bean;

import com.google.gson.Gson;

/**
 * Created by dai on 2017/4/24.
 */

public class SendMessage2 {

    private String roomId;   //房间Id
    private String userId;   //用户Id(username)
    private long timeStamp; //时间戳 可根据时间戳 进行查看排序 以及推送,未收到的数据
    private String message;  //消息

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTimeStamp() {
        return String.valueOf(timeStamp);
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        SendMessage2 sendMessage = new SendMessage2();
        sendMessage.setMessage(message);
        sendMessage.setTimeStamp(timeStamp);
        sendMessage.setRoomId(roomId);
        sendMessage.setUserId(userId);
        System.out.println("gson.toJson(sendMessage, SendMessage.class) = " + gson.toJson(sendMessage, SendMessage2.class));
        return gson.toJson(sendMessage, SendMessage2.class);
    }
}
