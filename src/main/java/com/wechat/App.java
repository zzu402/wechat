package com.wechat;

import com.wechat.utils.JsonMapper;
import com.wechat.utils.LogUtils;
import com.wechat.utils.SleepUtils;
import com.wechat.websocket.WebSocketClientImpl;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: huangzz
 * @Description:
 * @Date :2018/5/3
 */
public class App {

    public  static void main(String args[]){
        String userName="test";
        String secretKey="1172109637e0a084dc82a0fe2decf44ed52e1da42cd08eec648b54749bdabde793dd644b3d1fadefd65227f6336697b2";
        String uri=String.format("ws://localhost:8080/websocket/%s",userName);
        try {
            WebSocketClientImpl client=WebSocketClientImpl.getAvailableSocketClient(uri);
            WebSocketClientImpl.connect(client);
            WebSocketClientImpl.keepClientAlive(client,secretKey);
        } catch (URISyntaxException e) {
            LogUtils.error(App.class,"程序异常",e);
        }


    }


}
