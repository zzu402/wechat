package com.wechat.utils;

import com.wechat.constant.GlobalConstant;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

/**
 * @Author: huangzz
 * @Description: 这些位置参数都是基于雷电模拟器调制：分辨率 手机版：1080*1920（dpi 480）
 * @Date :2018/4/28
 */
public class WechatUtils {

    private static void clickSearch() {
        AdbUtils.touch(780, 120);
    }

    private static void searchFriend(String wechatId) {
        AdbUtils.inputText(wechatId);
    }

    private static void clickContact() {
        AdbUtils.touch(200, 450);
    }

    private static void clickChatInput() {
        AdbUtils.touch(350, 1800);
    }

    private static void inputChatContent(String text) {
        AdbUtils.inputText(text);
    }

    private static void sendText() {
        AdbUtils.touch(940, 1820);
    }

    public static boolean isNeedAutoSendMsg=false;
    public static boolean isNeedToRequestAddFriend=false;
    public static boolean addFriendSuccess=true;
    public static boolean msgIsNoEqualCode=false;

    private static boolean queryFriend(String friend) {
        clickSearch();
        searchFriend(friend);
        AdbUtils.printScreen();
        ImageUtils.cron(40, 200, 400, 200, GlobalConstant.SCREENSHOT_LOCATION, GlobalConstant.CONTACT_LOCATION);
        return OcrUtils.imageContainText(GlobalConstant.CONTACT_LOCATION, "联系人") || OcrUtils.imageContainText(GlobalConstant.CONTACT_LOCATION, "最常使用");
    }

    private static boolean sendMessage(String friend, String text) {
        if (queryFriend(friend)) {//这种情况可能是Adb输入不全
            clickContact();
            clickChatInput();
            inputChatContent(text);
            sendText();
            return true;
        } else {//输入不全的时候要先去点击X去除输入内容
            AdbUtils.touch(1040, 120);
        }
        return false;
    }

    public static void goWechatHome() {//返回主页面
        int time=0;
        AdbUtils.printScreen();
        ImageUtils.cron(20, 100, 400, 80, GlobalConstant.SCREENSHOT_LOCATION, GlobalConstant.HOME_LOCATION);
        while (!OcrUtils.imageContainText(GlobalConstant.HOME_LOCATION, "微信")) {
            AdbUtils.goBack();
            SleepUtils.sleep(500L);//休眠0.5秒
            AdbUtils.printScreen();
            ImageUtils.cron(20, 100, 400, 80, GlobalConstant.SCREENSHOT_LOCATION, GlobalConstant.HOME_LOCATION);
            time++;
            if(time>10)
                break;
        }
    }


    public static void addFriendAndSendMsg(String friend,String verifyCode) {
        isNeedToRequestAddFriend=false;
        addFriendSuccess=true;
        isNeedAutoSendMsg=false;
        AdbUtils.touch(970, 120);//点击左上角"+"
        AdbUtils.touch(700, 410);//点击"添加朋友"
        AdbUtils.touch(200, 330);//点击"搜索 微信号/QQ号//手机号"
        searchFriend(friend);//在输入框输入好友 微信号/手机号/qq
        AdbUtils.touch(200, 250);//在搜索栏目上面搜索到对象后 点击
        SleepUtils.sleep(1000L);//休眠,防止网络延时没有进入
        AdbUtils.printScreen();//截屏幕
        int y = 0;
        ImageUtils.cron(200, 1050 + y, 600, 100, GlobalConstant.SCREENSHOT_LOCATION, GlobalConstant.ADD_LOCATION);//裁剪部分区域
        String text = OcrUtils.iDText(GlobalConstant.ADD_LOCATION).toString(2);//获得裁剪部分区域的文字
        while (y < 700) {
            if (text.contains("添加")) {//该好友并没有添加到通讯录
                AdbUtils.touch(200, 1050 + y);//点击添加按钮
                SleepUtils.sleep(1000L);//这里容易卡住，设定休眠1秒
                AdbUtils.printScreen();//
                ImageUtils.cron(100, 100, 400, 80, GlobalConstant.SCREENSHOT_LOCATION, GlobalConstant.VERIFY_LOCATION);
                String verifyText=OcrUtils.iDText(GlobalConstant.VERIFY_LOCATION).toString(2);
                if (verifyText.contains("验证申请")) {//如果是验证申请，就点击
                    AdbUtils.touch(900, 150);
                    isNeedToRequestAddFriend=true;
                }else if(verifyText.contains("详细资料")){//这种情况下有两种可能，第一种就是界面卡住，第二种是免验证添加
                    SleepUtils.sleep(1000L);//先休眠1秒钟，再截屏
                    AdbUtils.printScreen();//再次截屏
                    //重复上个动作
                    ImageUtils.cron(100, 100, 400, 80, GlobalConstant.SCREENSHOT_LOCATION, GlobalConstant.VERIFY_LOCATION);
                    verifyText=OcrUtils.iDText(GlobalConstant.VERIFY_LOCATION).toString(2);
                    if (verifyText.contains("验证申请")) {//如果是验证申请，就点击
                        AdbUtils.touch(900, 150);
                        isNeedToRequestAddFriend=true;
                    }else  if(verifyText.contains("详细资料")){//这时候尝试两次了，默认是第二种情况
                        int y2=0;
                        ImageUtils.cron(200, 1050 + y2, 600, 100, GlobalConstant.SCREENSHOT_LOCATION, GlobalConstant.ADD_LOCATION);//裁剪部分区域
                        text = OcrUtils.iDText(GlobalConstant.ADD_LOCATION).toString(2);//获得裁剪部分区域的文字
                        while(y2<700){
                            if(text.contains("发消息")){
                                clickAndSendMsg(1050+y2,verifyCode);
                                break;
                            }else{
                                y2 += 150;
                                ImageUtils.cron(200, 1050 + y2, 600, 100, GlobalConstant.SCREENSHOT_LOCATION, GlobalConstant.ADD_LOCATION);
                                text = OcrUtils.iDText(GlobalConstant.ADD_LOCATION).toString(2);//获得裁剪部分区域的文字
                            }
                        }
                        if(y2>700){
                            isNeedAutoSendMsg=true;
                        }
                    }
                }else{//如果什么都没有，就默认添加成功
                    isNeedAutoSendMsg=true;
                }
                break;
            } else if (text.contains("发消息")) {//该好友已经添加到通讯录，可以直接发消息
                clickAndSendMsg(1050+y,verifyCode);
                break;
            } else {//什么都没有找到，继续找
                y += 150;
                ImageUtils.cron(200, 1050 + y, 600, 100, GlobalConstant.SCREENSHOT_LOCATION, GlobalConstant.ADD_LOCATION);
                text = OcrUtils.iDText(GlobalConstant.ADD_LOCATION).toString(2);//获得裁剪部分区域的文字
            }
        }
        if(y>700){
            addFriendSuccess=false;
        }
    }
    private static void clickAndSendMsg(int y,String verifyCode){
        msgIsNoEqualCode=false;
        AdbUtils.touch(200, y);//点击屏幕上面的发消息
        clickChatInput();//点击使得可以输入
        inputChatContent(verifyCode);//输入消息
        AdbUtils.printScreen();
        ImageUtils.cron(135,1800,500,100, GlobalConstant.SCREENSHOT_LOCATION,GlobalConstant.MESSAGE_LOCATION);
        String text=OcrUtils.iDText(GlobalConstant.MESSAGE_LOCATION).toString(2);
        if(!text.contains(verifyCode)){
            msgIsNoEqualCode=true;
        }
        sendText();//点击发送
        goWechatHome();//发送完毕后回到微信页面
    }

    public static boolean autoSendMsg(String phone,String verifyCode){
        WechatUtils.goWechatHome();
        int time = 0;
        while (!WechatUtils.sendMessage(phone, verifyCode)) {
            time++;
            if (time > 5)
                break;
        }
        if (time <= 5) {
            return true;
        } else {
           return  false;
        }
    }



    public static void main(String[] args) {
        String phone = "zzu_402";
        String verifyCode = "6845";
        WechatUtils.goWechatHome();
        WechatUtils.addFriendAndSendMsg(phone, verifyCode);
        if(!WechatUtils.addFriendSuccess){//如果没有添加成功
          System.out.println("add friend failure");
        }else if (WechatUtils.addFriendSuccess&&WechatUtils.isNeedToRequestAddFriend){
            System.out.println("the friend need request verify");
        }else if(WechatUtils.addFriendSuccess&&WechatUtils.msgIsNoEqualCode){
            System.out.println("send msg no equals code");
        }else if (WechatUtils.addFriendSuccess&&WechatUtils.isNeedAutoSendMsg) {

            boolean autoSuccess = WechatUtils.autoSendMsg(phone, verifyCode);
            if (autoSuccess) {
                System.out.println("success");

            } else {
                System.out.println("auto message error");
            }
        }else if(WechatUtils.addFriendSuccess){
            System.out.println("success");
        }



    }
}
