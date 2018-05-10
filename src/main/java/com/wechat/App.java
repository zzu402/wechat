package com.wechat;

import com.wechat.ui.LoginUI;
import com.wechat.ui.MainUI;
import com.wechat.utils.*;
import com.wechat.websocket.WebSocketClientImpl;

import javax.swing.*;
import java.io.File;
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

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                loginUI.frame.setVisible(false);
                MainUI mainUI = new MainUI(WindowConstants.EXIT_ON_CLOSE);
                mainUI.frame.setVisible(true);
            }
        });

    }

    public static void loginFailure(String message) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JOptionPane.showMessageDialog(loginUI.frame, message);
            }
        });


    }


    public static void main(String args[]) {
        PropertiesUtils.loadProps(PropertiesUtils.getUserDir());
        String url=PropertiesUtils.getString("serverUrl","");
        if(StringUtil.isBlank(url)){
            File userFile= PropertiesUtils.getUserDir();
            PropertiesUtils.updateProperty(userFile,"serverUrl","ws://121.54.168.163:8080/websocket");
        }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                loginUI = new LoginUI(WindowConstants.EXIT_ON_CLOSE);
                loginUI.frame.setVisible(true);
            }
        });

    }
}
