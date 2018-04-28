package com.wechat.utils;

/**
 * @Author: huangzz
 * @Description:
 * @Date :2018/4/28
 */
public class SleepUtils {

    public static void sleep(Long time){
        try{
            Thread.sleep(time);
        }catch (Exception e){
            LogUtils.error(SleepUtils.class,"休眠出错",e);
        }
    }
}
