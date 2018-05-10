package com.wechat.utils;

import com.wechat.constant.GlobalConstant;

import java.util.Random;

/**
 * @Author: huangzz
 * @Description: 脚本
 * @Date :2018/5/10
 */
public class AutoScript {

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


    public static void goHome() {//返回微信主页面
        AdbUtils.printScreen();
        ImageUtils.cron(20, 100, 400, 80, GlobalConstant.SCREENSHOT_LOCATION, GlobalConstant.HOME_LOCATION);
        String text= OcrUtils.ocr(GlobalConstant.HOME_LOCATION,OcrUtils.TESS4J_MODEL);
        while (!text.contains("微信")) {//
            AdbUtils.touch(50,130);
            AdbUtils.printScreen();
            ImageUtils.cron(20, 100, 400, 80, GlobalConstant.SCREENSHOT_LOCATION, GlobalConstant.HOME_LOCATION);
            text= OcrUtils.ocr(GlobalConstant.HOME_LOCATION,OcrUtils.TESS4J_MODEL);
        }
    }


    //进入添加好友的界面
    public static void enterAddSearchView(){
        AdbUtils.touch(970, 120);//点击左上角"+"
        AdbUtils.touch(700, 410);//点击"添加朋友"
        AdbUtils.touch(200, 330);//点击"搜索 微信号/QQ号//手机号"
    }
    //输入微信id进行微信号搜索
    public static void doAddSearchAcion(String wechatId){
        Random random=new Random();
        AdbUtils.inputText(wechatId);//在输入框输入好友 微信号/手机号/qq
        //这边输入可能输入不全
        AdbUtils.printScreen();
        ImageUtils.cron(250,100,500,100, GlobalConstant.SCREENSHOT_LOCATION,GlobalConstant.INPUT_LOCATION);
        String inputPhone=OcrUtils.ocr(GlobalConstant.INPUT_LOCATION,OcrUtils.TESS4J_MODEL);
        if(!inputPhone.contains(wechatId)){
            AdbUtils.touch(1040, 120);//输入不全，再次输入
            AdbUtils.inputText(wechatId);//再输入一次
        }
        //这边随便输入一位数字
        AdbUtils.inputText(String.valueOf(random.nextInt(9)));
        //然后删除
        AdbUtils.del();

        //截取页面作为特征值
        AdbUtils.printScreen();
        ImageUtils.cron(0,0,1080,1920,GlobalConstant.SCREENSHOT_LOCATION,GlobalConstant.LAST_PAGE_LOCATION);

        //在搜索栏目上面搜索到对象后 点击
        AdbUtils.touch(200, 250);

    }

    //进入到添加界面
    public static boolean  enterAddView(){
        //从上一个页面进来，这时候可能会卡住，先延时1.5秒
        SleepUtils.sleep(1500L);//
        AdbUtils.printScreen();
        int time=0;
        while(ImageUtils.isSimilarity(GlobalConstant.SCREENSHOT_LOCATION,GlobalConstant.LAST_PAGE_LOCATION)){
            //表示当前页面还停留
            SleepUtils.sleep(1000L);//休眠一秒钟
            AdbUtils.printScreen();
            time++;
            if(time>10){//如果两个页面一直相似，超过10次退出
                return false;
            }
        }
        ImageUtils.cron(0,0,1080,1920,GlobalConstant.SCREENSHOT_LOCATION,GlobalConstant.LAST_PAGE_LOCATION);
        AdbUtils.printScreen();
        time=0;
        while(!ImageUtils.isSimilarity(GlobalConstant.SCREENSHOT_LOCATION,GlobalConstant.LAST_PAGE_LOCATION)){
            //表示当前页面还停留
            SleepUtils.sleep(1000L);//休眠一秒钟
            AdbUtils.printScreen();
            time++;
            if(time>10){
                return false;
            }
        }
        return true;
    }

    //1 表示未添加，这时候要进行点击添加按钮 2表示已经添加了，这时候点击发送消息，0 表示什么都没找到
    public static int doAddFriendAction(String verifyCode){//这个时候该做添加朋友的操作了
        AdbUtils.printScreen();//截屏幕
        int y = 0;
        ImageUtils.cron(200, 750 + y, 600, 150, GlobalConstant.SCREENSHOT_LOCATION, GlobalConstant.ADD_LOCATION);//裁剪部分区域
        String text = OcrUtils.ocr(GlobalConstant.ADD_LOCATION,OcrUtils.TESS4J_MODEL);//获得裁剪部分区域的文字
        while (y < 900) {
            if (text.contains("添加")) {//该好友并没有添加到通讯录
                AdbUtils.touch(200, 750 + y+70);//点击添加按钮
                AdbUtils.printScreen();
                ImageUtils.cron(0,0,1080,1920,GlobalConstant.SCREENSHOT_LOCATION,GlobalConstant.LAST_PAGE_LOCATION);
                return 1;
            } else if (text.contains("发消息")) {//该好友已经添加到通讯录，可以直接发消息
                sendMsgAfterAddAction(750+y,verifyCode);
                return  2;
            } else {//什么都没有找到，继续找
                SleepUtils.sleep(100L);
                y += 100;
                ImageUtils.cron(200, 750 + y, 600, 150, GlobalConstant.SCREENSHOT_LOCATION, GlobalConstant.ADD_LOCATION);
                text = OcrUtils.ocr(GlobalConstant.ADD_LOCATION,OcrUtils.TESS4J_MODEL);;
            }
        }
        return 0;
    }

    //点完添加后，发送消息
    public static boolean sendMsgAfterAddFriend(String verifyCode){
        //这个地方容易卡住，休眠3秒
        SleepUtils.sleep(3000L);
        AdbUtils.printScreen();
        //截图之后比较下相似
        ImageUtils.cron(100, 100, 400, 80, GlobalConstant.SCREENSHOT_LOCATION, GlobalConstant.VERIFY_LOCATION);
        String text=OcrUtils.ocr(GlobalConstant.VERIFY_LOCATION,OcrUtils.TESS4J_MODEL);
        if(text.contains("验证申请")){
            AdbUtils.touch(900, 150);
            return  false;
        }
        int y=0;
        ImageUtils.cron(200, 750 + y, 600, 150, GlobalConstant.SCREENSHOT_LOCATION, GlobalConstant.ADD_LOCATION);//裁剪部分区域
        text = OcrUtils.ocr(GlobalConstant.ADD_LOCATION,OcrUtils.TESS4J_MODEL);//获得裁剪部分区域的文字
        while(y<900){
            if(text.contains("发消息")){
                sendMsgAfterAddAction(750+y,verifyCode);
                return true;
            }else{
                SleepUtils.sleep(100L);
                y += 100;
                ImageUtils.cron(200, 750 + y, 600, 150, GlobalConstant.SCREENSHOT_LOCATION, GlobalConstant.ADD_LOCATION);
                text = OcrUtils.ocr(GlobalConstant.ADD_LOCATION,OcrUtils.TESS4J_MODEL);//获得裁剪部分区域的文字
            }
        }
       return false;
    }

    private static void sendMsgAfterAddAction(int y,String verifyCode){
        SleepUtils.sleep(1000L);//给模拟器一个反应时间
        AdbUtils.touch(200, y+100);//点击屏幕上面的发消息
        clickChatInput();//点击使得可以输入
        inputChatContent(verifyCode);//输入消息
        AdbUtils.printScreen();
        ImageUtils.cron(135,1800,500,100, GlobalConstant.SCREENSHOT_LOCATION,GlobalConstant.MESSAGE_LOCATION);
        String text=OcrUtils.ocr(GlobalConstant.MESSAGE_LOCATION,OcrUtils.TESS4J_MODEL);
        if(!text.contains(verifyCode)){
            for(int i=0;i<verifyCode.length();i++)
                AdbUtils.del();
            inputChatContent(verifyCode);//输入消息
        }
        sendText();//点击发送
    }

    private static void clickSearch() {
        AdbUtils.touch(780, 120);
    }
    private static void searchFriend(String wechatId) {
        AdbUtils.inputText(wechatId);
    }

    private static boolean queryFriend(String friend) {
        clickSearch();
        searchFriend(friend);
        AdbUtils.printScreen();
        ImageUtils.cron(40, 200, 400, 200, GlobalConstant.SCREENSHOT_LOCATION, GlobalConstant.CONTACT_LOCATION);
        String text=OcrUtils.ocr(GlobalConstant.CONTACT_LOCATION,OcrUtils.TESS4J_MODEL);
        return text.equals("联系人") || text.equals( "最常使用");
    }

    public static boolean searchFriendAndSendMessage(String friend, String text) {
        goHome();
        int times=0;
        while(!queryFriend(friend)){
            AdbUtils.touch(1040, 120);
            times++;
            if(times>5)
                return false;
        }
        clickContact();
        clickChatInput();
        inputChatContent(text);
        sendText();
        return true;
    }

    //1-成功 2-表示进入到添加界失败 3-没有找到添加或发送消息按钮,4-发送失败
    public static int autoRun(String wechatId,String verifyCode){
        //1-先返回到微信主页面
        goHome();
        //2-进入微信添加好友界面
        enterAddSearchView();
        //3-搜索微信号
        doAddSearchAcion(wechatId);
        //4-进入到添加界面
        if(!enterAddView()){
            return 2;
        }
        //5-添加
        int result=doAddFriendAction(verifyCode);
        if(result==1) {
            if(!sendMsgAfterAddFriend(verifyCode)) {
                if(searchFriendAndSendMessage(wechatId, verifyCode))
                    return 4;
            }
        }
        else if(result==0)
            return 3;
        return 1;
    }

    public static void main(String args[]){
        String wechatId="282501549";
        String code="123456";
        System.out.println(autoRun(wechatId,code));


    }












}
