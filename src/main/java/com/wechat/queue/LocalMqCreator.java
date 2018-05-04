package com.wechat.queue;

/**
 * Created by hongshuiqiao on 2017/10/13.
 */
public class LocalMqCreator implements MessageQueueCreator {
    @Override
    public MessageQueue createMq(String name) {
        LocalMessageQueue queue = new LocalMessageQueue();
        queue.setQueueName(name);
        return queue;
    }
}
