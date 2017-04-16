package com.dai.util;

/**
 * Created by Administrator on 2017/4/16 0016.
 */

public interface ChatDataObserver {
    void onTextMessage(String string);

    void onByteMessage(Byte abyte);
}
