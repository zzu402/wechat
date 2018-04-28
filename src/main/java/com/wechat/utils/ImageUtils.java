package com.wechat.utils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * @Author: huangzz
 * @Description:
 * @Date :2018/4/28
 */
public class ImageUtils {

   /* 图片裁切
  * @param x1 选择区域左上角的x坐标
  * @param y1 选择区域左上角的y坐标
  * @param width 选择区域的宽度
  * @param height 选择区域的高度
  * @param sourcePath 源图片路径
  * @param descpath 裁切后图片的保存路径
  */
    public static void cron(int x1, int y1, int width, int height,
                           String sourcePath, String descpath) {
        FileInputStream is = null;
        ImageInputStream iis = null;
        try {
            is = new FileInputStream(sourcePath);
            String fileSuffix = sourcePath.substring(sourcePath
                    .lastIndexOf(".") + 1);
            Iterator<ImageReader> it = ImageIO
                    .getImageReadersByFormatName(fileSuffix);
            ImageReader reader = it.next();
            iis = ImageIO.createImageInputStream(is);
            reader.setInput(iis, true);
            ImageReadParam param = reader.getDefaultReadParam();
            Rectangle rect = new Rectangle(x1, y1, width, height);
            param.setSourceRegion(rect);
            BufferedImage bi = reader.read(0, param);
            ImageIO.write(bi, fileSuffix, new File(descpath));
        } catch (Exception ex) {
            LogUtils.error(ImageUtils.class,"裁剪图片出错",ex);
        } finally {
            closeInputStream(is);
            if (iis != null) {
                try {
                    iis.close();
                } catch (IOException e) {
                    LogUtils.error(ImageUtils.class,"裁剪图片出错",e);
                }
                iis = null;
            }
        }
    }

    private static void closeInputStream(InputStream is){
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                LogUtils.error(ImageUtils.class,"关闭输入流异常",e);
            }
            is = null;
        }
    }

    public static void main(String[]args){
        cron(20,100,400,80,"F://wechat//12.png","F://wechat//cron.png");
    }





}
