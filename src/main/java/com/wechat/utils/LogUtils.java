package com.wechat.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: huangzz
 * @Description:
 * @Date :2018/4/27
 */
public class LogUtils {

    public static Map<Class,Logger> loggerMap=new HashMap<Class, Logger>();
    public  static void info(Class clazz,String log){
        Logger logger=getLogger(clazz);
        logger.info(log);
    }
    private static Logger getLogger(Class clazz){
        Logger logger=loggerMap.get(clazz);
        if(logger==null) {
            synchronized (LogUtils.class) {
                if(logger==null) {
                    logger = LoggerFactory.getLogger(clazz);
                    loggerMap.put(clazz, logger);
                }
            }
        }
        return  logger;
    }

    public static void error(Class clazz,String log){
        error(clazz,log,null);
    }

    public static void debug(Class clazz,String log){
        Logger logger=getLogger(clazz);
        logger.debug(log);
    }

    public static void warn(Class clazz,String log){
        Logger logger=getLogger(clazz);
        logger.warn(log);
    }

    public static void error(Class clazz,String log,Exception e){
        Logger logger=getLogger(clazz);
        if(e!=null)
            logger.error(log,e);
        else
            logger.error(log);
    }
}
