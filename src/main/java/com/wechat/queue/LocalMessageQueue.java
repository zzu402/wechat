package com.wechat.queue;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by hongshuiqiao on 2017/10/13.
 */
public class LocalMessageQueue extends AbstractMessageQueue {
    private LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();

    @Override
    public void push(String message) throws InterruptedException {
        queue.put(message);
    }

    @Override
    public String pop() throws InterruptedException {
        return queue.take();
    }

    @Override
    public String pop(long timeout) throws InterruptedException {
        if(timeout<=0)
            return queue.poll();
        return queue.poll(timeout, TimeUnit.MILLISECONDS);
    }
}
