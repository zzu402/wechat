package com.wechat.queue;

/**
 * Created by hongshuiqiao on 2017/10/13.
 */
public interface MessageQueue {
    /**
     * 返回消息队列的名称
     * @return
     */
    String getQueueName();

    /**
     * 往消息队列的尾部添加一条消息
     * @param message
     */
    void push(String message) throws InterruptedException;

    /**
     * 获取并删除消息队列头部的一条消息，如果没有消息则阻塞
     * @return
     */
    String pop() throws InterruptedException;

    /**
     * 获取并删除消息队列头部的一条消息，如果没有消息则最多等待指定的毫秒数，如果依然没有消息则返回空
     * @param timeout
     * @return
     */
    String pop(long timeout) throws InterruptedException;
}
