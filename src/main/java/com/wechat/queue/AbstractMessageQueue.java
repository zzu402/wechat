package com.wechat.queue;

/**
 * Created by hongshuiqiao on 2017/10/13.
 */
public abstract class AbstractMessageQueue implements MessageQueue {
    private String queueName;

    public String getQueueName() {
        return queueName;
    }

    void setQueueName(String queueName) {
        this.queueName = queueName;
    }
}
