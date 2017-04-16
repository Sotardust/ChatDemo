package com.dai.util;

/**
 * Created by Administrator on 2017/4/16 0016.
 */

public interface ChatDataSubject {
    void add(ChatDataObserver chatDataObserver);

    void remove(ChatDataObserver chatDataObserver);

    void notifyText(ChatData chatData);

    void notifyByte(ChatData chatData);
}
