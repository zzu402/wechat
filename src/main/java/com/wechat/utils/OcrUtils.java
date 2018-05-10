package com.wechat.utils;

import com.baidu.aip.ocr.AipOcr;
import com.wechat.constant.GlobalConstant;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * @Author: huangzz
 * @Description: 识别图片文字工具，一种利用百度api，一种利用tess4j
 * @Date :2018/4/27
 */
public class OcrUtils {
    //https://console.bce.baidu.com/ai/#/ai/ocr/app/list
    private static String APP_ID = "11164162";
    private static String API_KEY = "QsXNTssGCtucKWtaSQa8fHwv";
    private static String SECRET_KEY = "z5KVwRWuwWYYhTVeGDB4jWDeosYxb66G";

    public static final Integer BAIDU_MODEL=1;
    public static final Integer TESS4J_MODEL=2;


    public static void setAppId(String appId) {
        APP_ID = appId;
    }

    public static void setApiKey(String apiKey) {
        API_KEY = apiKey;
    }

    public static void setSecretKey(String secretKey) {
        SECRET_KEY = secretKey;
    }

    /*
    给定一张图片的本地地址，识别图片所包含的文字
    */
    private static String baiduOcr(String imagePath) {
        try {
            AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
            // 可选：设置网络连接参数
            client.setConnectionTimeoutInMillis(2000);
            client.setSocketTimeoutInMillis(60000);
            // 调用接口
            JSONObject res = client.basicGeneral(imagePath, new HashMap<String, String>());
            return res.toString(2);
        } catch (Exception e) {
            LogUtils.error(OcrUtils.class, "获取识别文字出错", e);
            return "";
        }
    }

    public static String ocr(String imagePath,Integer model){
//        if(model==BAIDU_MODEL){
//            return baiduOcr(imagePath);
//        }
//        if(model==TESS4J_MODEL){
//            return tess4jOcr(imagePath);
//        }
        return baiduOcr(imagePath);
    }

    private static Tesseract instance = null;

    private static Tesseract getInstance() {
        if (instance == null) {
            synchronized (OcrUtils.class) {
                if (instance == null)
                    instance = new Tesseract();
                File tessDataFolder = LoadLibs.extractTessResources("tessdata");
                instance.setLanguage("chi_sim");//英文库识别数字比较准确
                instance.setDatapath(tessDataFolder.getAbsolutePath());
            }
        }
        return instance;
    }

    private static String tess4jOcr(String imagePath) {
        File imageFile = new File(imagePath);
        if (!imageFile.exists())
            return "";
        try {
            String result = getInstance().doOCR(imageFile);
            return result.replace(" ", "");
        } catch (TesseractException e) {
            LogUtils.error(OcrUtils.class, "Tess4j do ocr error", e);
            return baiduOcr(imagePath);
        }
    }
}
