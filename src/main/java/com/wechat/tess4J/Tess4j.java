package com.wechat.tess4J;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;

import java.io.File;

/**
 * @Author: huangzz
 * @Description:
 * @Date :2018/5/7
 */
public class Tess4j {

    public static void main(String[]args){

        //将验证码图片的内容识别为字符串
        try {
            File imageFile = new File("F:\\wechat\\image\\message.png");
            File tessDataFolder = LoadLibs.extractTessResources("tessdata");
            Tesseract instance = new Tesseract();
            instance.setLanguage("eng");//英文库识别数字比较准确
            instance.setDatapath(tessDataFolder.getAbsolutePath());
            String result = instance.doOCR(imageFile);
            System.out.println(result);
        } catch (TesseractException e) {
            e.printStackTrace();
        }
    }
}
