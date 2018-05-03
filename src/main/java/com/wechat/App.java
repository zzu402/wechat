package com.wechat;

import com.wechat.ui.LoginUI;
import com.wechat.ui.MainUI;
import com.wechat.utils.JsonMapper;
import com.wechat.utils.LogUtils;
import com.wechat.utils.SleepUtils;
import com.wechat.websocket.WebSocketClientImpl;

import javax.swing.*;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: huangzz
 * @Description:
 * @Date :2018/5/3
 */
public class App {

    private static LoginUI loginUI = null;

    public static void loginSuccess() {
        loginUI.frame.setVisible(false);
        MainUI mainUI = new MainUI(WindowConstants.EXIT_ON_CLOSE);
        mainUI.frame.setVisible(true);
    }

    public static void loginFailure(String message) {
        JOptionPane.showMessageDialog(null, message);
    }


    public static void main(String args[]) {
        loginUI = new LoginUI(WindowConstants.EXIT_ON_CLOSE);
        loginUI.frame.setVisible(true);
//        String userName="test";
//        String secretKey="1172109637e0a084dc82a0fe2decf44ed52e1da42cd08eec648b54749bdabde793dd644b3d1fadefd65227f6336697b2";
//        String uri=String.format("ws://localhost:8080/websocket/%s",userName);
//        try {
//            WebSocketClientImpl client=WebSocketClientImpl.getAvailableSocketClient(uri);
//            WebSocketClientImpl.connect(client);
//            WebSocketClientImpl.keepClientAlive(client,secretKey);
//        } catch (URISyntaxException e) {
//            LogUtils.error(App.class,"程序异常",e);
//        }
    }


}
