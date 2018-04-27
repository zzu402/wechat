package com.wechat.utils;
import com.wechat.constant.GlobalConstant;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class AdbUtils {
    private static Boolean error = null;


    /*
        点击
     */
    public static void touch(int x,int y){
        String command=GlobalConstant.ADB_PATH+" shell input tap "+x+" "+y;
        doAdbCommand(command);
    }

    /*
        滑动
     */
    public static void swipe(int sx,int sy,int tx,int ty){
        String command=GlobalConstant.ADB_PATH+" shell input swipe "+sx+" "+sy+" "+tx+" "+ty;
        doAdbCommand(command);
    }

    /*
        输入文本
     */
    public static void inputText(String text){
        String command=GlobalConstant.ADB_PATH+" shell input text "+text;
        doAdbCommand(command);
    }
    /*
        返回上一级
     */
    public static void goBack(){
        String command=GlobalConstant.ADB_PATH+" shell input keyevent 4";
        doAdbCommand(command);
    }

    /**
     * 改进的截图方法<br>
     * 感谢 hxzqlh
     * 当改进的截图方法不能正常执行时降级为常规方法
     */
    public static void printScreen() {
        LogUtils.info(AdbUtils.class,"截屏开始");
        if (error != null && error) {
            printScreenWithOld();
        } else {
            try {
                String[] args = new String[]{"bash", "-c", GlobalConstant.ADB_PATH + " exec-out screencap -p > " + GlobalConstant.SCREENSHOT_LOCATION};
                String os = System.getProperty("os.name");
                if (os.toLowerCase().startsWith("win")) {
                    args[0] = "cmd";
                    args[1] = "/c";
                }
                Process p1 = Runtime.getRuntime().exec(args);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(p1.getErrorStream()));
                String s=null;
                while ((s = bufferedReader.readLine()) != null)
                    LogUtils.error(AdbUtils.class,s);
                p1.waitFor();
                checkScreenSuccess();
            } catch (IOException e) {
                LogUtils.error(AdbUtils.class,"截屏失败",e);
                error = true;
                printScreenWithOld();
            } catch (InterruptedException e) {
                LogUtils.error(AdbUtils.class,"截屏失败",e);
            }
        }
        LogUtils.info(AdbUtils.class,"截屏结束");
    }

    private static void checkScreenSuccess() throws IOException {
        if (error == null) {
            BufferedImage image = ImageIO.read(new File(GlobalConstant.SCREENSHOT_LOCATION));
            if (image == null) {
                throw new IOException("cann't read file \"" + GlobalConstant.SCREENSHOT_LOCATION + "\" into image object");
            }
        }
    }

    private static boolean doAdbCommand(String command){
        try {
            Runtime.getRuntime().exec(command);
            return true;
        } catch (IOException e) {
            LogUtils.error(AdbUtils.class,"Adb命令执行失败",e);
            return  false;
        }
    }

    public static void printScreenWithOld() {
        try {
            Process p1 = Runtime.getRuntime().exec(GlobalConstant.ADB_PATH + " shell screencap -p /sdcard/screenshot.png");
            p1.waitFor();
            Process p2 = Runtime.getRuntime().exec(GlobalConstant.ADB_PATH + " pull /sdcard/screenshot.png " + GlobalConstant.SCREENSHOT_LOCATION);
            p2.waitFor();
        } catch (IOException e) {
            LogUtils.error(AdbUtils.class,"截屏失败",e);
        } catch (InterruptedException e) {
            LogUtils.error(AdbUtils.class,"截屏失败",e);
        }
    }

    public static void main(String[] args) {
        inputText("jianglong");
    }
}
