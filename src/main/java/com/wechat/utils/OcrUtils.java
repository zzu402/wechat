package com.wechat.utils;

import com.baidu.aip.ocr.AipOcr;
import com.wechat.constant.GlobalConstant;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * @Author: huangzz
 * @Description:
 * @Date :2018/4/27
 */
public class OcrUtils {
   //https://console.bce.baidu.com/ai/#/ai/ocr/app/list
    public static final String APP_ID = "11164162";
    public static final String API_KEY = "QsXNTssGCtucKWtaSQa8fHwv";
    public static final String SECRET_KEY = "z5KVwRWuwWYYhTVeGDB4jWDeosYxb66G";

    /*
        给定一张图片的本地地址，识别图片所包含的文字
     */
    public static JSONObject iDText(String imagePath){
      try {
          AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
          // 可选：设置网络连接参数
          client.setConnectionTimeoutInMillis(2000);
          client.setSocketTimeoutInMillis(60000);
          // 调用接口
          JSONObject res = client.basicGeneral(imagePath, new HashMap<String, String>());
          return res;
      }catch (Exception e){
          LogUtils.error(OcrUtils.class,"获取识别文字出错",e);
          return  null;
      }
    }
    public static boolean imageContainText(String imagePath,String text){
        boolean contain=false;
        JSONObject res=iDText(imagePath);
        if(res==null)
            return false;
        String resStr=res.toString(2);
        if(resStr.contains(text))
            contain=true;
        return  contain;
    }

    public static void  main(String[] args){
        System.out.println(imageContainText(GlobalConstant.SCREENSHOT_LOCATION,"联系人"));
    }






}
