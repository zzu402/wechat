package com.wechat.utils;

import com.wechat.constant.GlobalConstant;

/**
 * @Author: huangzz
 * @Description:
 * 这些位置参数都是基于雷电模拟器调制：分辨率 手机版：1080*1920（dpi 480）
 * @Date :2018/4/28
 */
public class WechatUtils {

    private static void clickSearch(){
        AdbUtils.touch(780,120);
    }
    private static void searchFriend(String wechatId){
        AdbUtils.inputText(wechatId);
    }
    private static void clickContact(){
        AdbUtils.touch(200,450);
    }
    private static void clickChatInput(){
        AdbUtils.touch(350,1800);
    }
    private static void inputChatContent(String text){
        AdbUtils.inputText(text);
    }
    private static void sendText(){
        AdbUtils.touch(940,1820);
    }

    public static boolean queryFriend(String friend){
        clickSearch();
        searchFriend(friend);
        AdbUtils.printScreen();
        ImageUtils.cron(40,200,400,200, GlobalConstant.SCREENSHOT_LOCATION,GlobalConstant.CONTACT_LOCATION);
        return OcrUtils.imageContainText(GlobalConstant.CONTACT_LOCATION,"联系人")||OcrUtils.imageContainText(GlobalConstant.CONTACT_LOCATION,"最常使用");
    }

    public static boolean sendMessage(String friend,String text){
        if(queryFriend(friend)) {
            clickContact();
            clickChatInput();
            inputChatContent(text);
            sendText();
            return  true;
        }
        return  false;
    }

    public static void goWechatHome(){
        AdbUtils.printScreen();
        ImageUtils.cron(20,100,400,80, GlobalConstant.SCREENSHOT_LOCATION,GlobalConstant.HOME_LOCATION);
        while(!OcrUtils.imageContainText(GlobalConstant.HOME_LOCATION,"微信")){
            AdbUtils.goBack();
            AdbUtils.printScreen();
            ImageUtils.cron(20,100,400,80, GlobalConstant.SCREENSHOT_LOCATION,GlobalConstant.HOME_LOCATION);
        }
    }



    public static void clickAddFriend(String friend){
        AdbUtils.touch(970,120);
        AdbUtils.touch(700,410);
        AdbUtils.touch(200,330);
        searchFriend(friend);
        AdbUtils.touch(200,250);
        int y=0;
        SleepUtils.sleep(500L);
        AdbUtils.printScreen();
        ImageUtils.cron(200,1050+y,400,80, GlobalConstant.SCREENSHOT_LOCATION,GlobalConstant.ADD_LOCATION);
        while(!OcrUtils.imageContainText(GlobalConstant.ADD_LOCATION,"添加")){
            y+=150;
            ImageUtils.cron(200,1050+y,400,80, GlobalConstant.SCREENSHOT_LOCATION,GlobalConstant.ADD_LOCATION);
            if(y>700)
                return;
        }
        AdbUtils.touch(200,1050+y);
        AdbUtils.printScreen();
        ImageUtils.cron(100,100,400,80, GlobalConstant.SCREENSHOT_LOCATION,GlobalConstant.VERIFY_LOCATION);
        if(OcrUtils.imageContainText(GlobalConstant.VERIFY_LOCATION,"验证申请")){
            AdbUtils.touch(900,150);
        }
    }

    public static void main(String[]args){
        goWechatHome();
        clickAddFriend("15959340993");
        goWechatHome();
        int time=0;
        while(!sendMessage("15959340993","hello")){
            time++;
            if(time>10)
                break;
        }
    }


}
