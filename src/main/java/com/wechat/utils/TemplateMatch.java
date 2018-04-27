package com.wechat.utils;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.opencv_core;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.cvLoadImage;
import static org.bytedeco.javacpp.opencv_imgproc.CV_TM_SQDIFF;
import static org.bytedeco.javacpp.opencv_imgproc.cvMatchTemplate;

/**
 * @Author: huangzz
 * @Description:
 * @Date :2018/4/27
 */
public class TemplateMatch {

    private opencv_core.IplImage image;

    public void load(String filename) {
        image = cvLoadImage(filename);
    }
    public boolean matchTemplate(opencv_core.IplImage source) {
        boolean matchRes=false;
//        opencv_core.IplImage result = cvCreateImage(opencv_core.cvSize(
//                source.width() - this.image.width() + 1,
//                source.height() - this.image.height() + 1),
//                opencv_core.IPL_DEPTH_32F, 1);
//
//        opencv_core.cvZero(result);
//        cvMatchTemplate(source, this.image, result, CV_TM_SQDIFF);
//        opencv_core.CvPoint maxLoc = new opencv_core.CvPoint();
//        opencv_core.CvPoint minLoc = new opencv_core.CvPoint();
//        DoublePointer minVal=new DoublePointer();
//        DoublePointer maxVal=new DoublePointer();
//        cvMinMaxLoc(result,minVal,maxVal,minLoc,maxLoc,null);
//        System.out.println(result.width());
//        System.out.println(minLoc.x());
//        System.out.println(minVal);

//        double[] minVal = new double[2];
//        double[] maxVal = new double[2];
//        cvMinMaxLoc(result,minVal,maxVal);
//        matchRes = maxVal[0] > 0.60f ? true : false;
//        System.out.println(maxVal[0]);
//        cvReleaseImage(result);
        return matchRes;
    }

    public static void main(String[] args) {
        System.out.println("START...");
        TemplateMatch tm = new TemplateMatch();//实例化TemplateMatch对象
        tm.load("F://wechat//test.png");//加载带比对图片，注此图片必须小于源图
        boolean result = tm.matchTemplate(cvLoadImage("F://wechat//1.png"));//校验585.png是否包含于原图58home.png
        if (result){//打印匹配结果，boolean
            System.out.println("match");
        }else{
            System.out.println("un-match");
        }
        System.out.println("END...");
    }

}
