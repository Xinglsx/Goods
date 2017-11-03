package com.mingshu.goods.views;

/**
 * Created by Lisx on 2017-10-30.
 * 使用EventBus3.0三部曲
 * 1.定义事件
 */

public class MessageEvent {
    public final String message;

    public MessageEvent(String message) {
        this.message = message;
    }
}

