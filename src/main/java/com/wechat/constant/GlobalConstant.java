package com.wechat.constant;

import com.wechat.utils.PropertiesUtils;

import java.io.File;
import java.util.Properties;

/**
 * @Author: huangzz
 * @Description:
 * @Date :2018/4/27
 */
public class GlobalConstant {


    public static String ADB_PATH = "adb";//ADB 路径，电脑设置了环境变量直接写adb，否则要加上文件路径

    public static String IMAGE_LOCATION= System.getProperty("user.dir")+"//image";//图片目录

    public static String SCREENSHOT_LOCATION = IMAGE_LOCATION+"//screenshot.png";//截屏位置

    public static String HOME_LOCATION=IMAGE_LOCATION+"//home.png";

    public static String ADD_LOCATION=IMAGE_LOCATION+"//add.png";

    public static String MESSAGE_LOCATION=IMAGE_LOCATION+"//message.png";

    public static String INPUT_LOCATION=IMAGE_LOCATION+"//input.png";


    public static String CONTACT_LOCATION=IMAGE_LOCATION+"//contact.png";

    public static String VERIFY_LOCATION=IMAGE_LOCATION+"//verify.png";
    static {
        File file=new File(IMAGE_LOCATION);
        if(!file.exists())
            file.mkdirs();

    }



}
