package com.wechat.queue;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hongshuiqiao on 2017/10/13.
 */
public class MqManager {
    private static Map<String,MessageQueue> queueMap = new ConcurrentHashMap<>();
    private static final MessageQueueCreator localMqCreator = new LocalMqCreator();
    private static MessageQueueCreator mqCreator;

    public static void setMqCreator(MessageQueueCreator mqCreator) {
        MqManager.mqCreator = mqCreator;
    }

    public static MessageQueueCreator getMqCreator() {
        if(null == mqCreator)
            return localMqCreator;
        return mqCreator;
    }

    public static MessageQueue getMq(String name){
        MessageQueue queue = queueMap.get(name);
        if(null != queue)
            return queue;
        synchronized (MqManager.class) {
            queue = queueMap.get(name);
            if(null != queue)
                return queue;
            queue = getMqCreator().createMq(name);
            queueMap.put(name,queue);
        }
        return queue;
    }
}
