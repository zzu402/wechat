package com.wechat.queue;

/**
 * Created by hongshuiqiao on 2017/10/13.
 */
public interface MessageQueueCreator {
    /**
     * 创建MQ
     * @param name
     * @return
     */
    MessageQueue createMq(String name);
}
