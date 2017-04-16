package com.dai.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/16 0016.
 */

public class ChatDataObserved implements ChatDataSubject {


    private static ChatDataObserved chatDataObserved = null;

    private List<ChatDataObserver> list = new ArrayList<>();

    public static ChatDataObserved getInstance() {
        if (null == chatDataObserved) {
            synchronized (ChatDataObserved.class) {
                if (null == chatDataObserved) {
                    chatDataObserved = new ChatDataObserved();
                }
            }
        }
        return chatDataObserved;
    }

    @Override
    public void add(ChatDataObserver chatDataObserver) {
        list.add(chatDataObserver);

    }

    @Override
    public void remove(ChatDataObserver chatDataObserver) {
        if (list.contains(chatDataObserver)) {
            list.remove(chatDataObserver);
        }
    }

    @Override
    public void notifyText(ChatData chatData) {
        System.out.println("chatData = " + chatData);
        for (ChatDataObserver chatDataObserver : list) {
            chatDataObserver.onTextMessage(chatData.getText());
        }
    }

    @Override
    public void notifyByte(ChatData chatData) {
        for (ChatDataObserver chatDataObserver : list) {
            chatDataObserver.onByteMessage(chatData.getAbyte());
        }
    }

}
