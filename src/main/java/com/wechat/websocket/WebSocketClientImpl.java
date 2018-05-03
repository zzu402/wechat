package com.wechat.websocket;

import com.wechat.utils.*;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: huangzz
 * @Description:
 * @Date :2018/5/3
 */
public class WebSocketClientImpl extends WebSocketClient {

    private static WebSocketClientImpl socketClient = null;

    private WebSocketClientImpl(URI serverUri) {
        super(serverUri, new Draft_6455());
    }

    private WebSocketClientImpl(URI serverUri, Draft protocolDraft) {
        super(serverUri, protocolDraft);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        LogUtils.info(WebSocketClientImpl.class,"onOpen:");
    }

    @Override
    public void onMessage(String s) {
        LogUtils.info(WebSocketClientImpl.class,"onMessage:"+s);
        Map<String, Object> verifyMap = JsonMapper.nonEmptyMapper().fromJson(s, Map.class);
        String phone = (String) verifyMap.get("verifyPhone");
        String code = (String) verifyMap.get("verifyCode");
        if(!StringUtil.isBlank(phone)&&!StringUtil.isBlank(code)){
            LogUtils.info(WebSocketClientImpl.class,"do add friend and send code...");
            WechatUtils.goWechatHome();
            WechatUtils.clickAddFriend(phone);
            WechatUtils.goWechatHome();
            int time=0;
            while(! WechatUtils.sendMessage(phone,code)){
                time++;
                if(time>10)
                    break;
            }
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {

    }

    @Override
    public void onError(Exception e) {


    }

    public static WebSocketClientImpl getAvailableSocketClient(String uri) throws URISyntaxException {
        if(socketClient==null){
            synchronized (WebSocketClientImpl.class){
                if(socketClient==null)
                    socketClient=new WebSocketClientImpl(new URI(uri));
            }
        }
        return  socketClient;
    }



    public static void connect(WebSocketClientImpl client){
        client.connect();
        while(!client.getReadyState().equals(READYSTATE.OPEN)){
            LogUtils.info(WebSocketClientImpl.class,"web socket is connect");
            SleepUtils.sleep(300L);
        }
        LogUtils.info(WebSocketClientImpl.class,"web socket connected");
    }

    public static void keepClientAlive(WebSocketClientImpl client,String secretKey){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> verifyMap = new HashMap<>();
                verifyMap.put("secretkey", secretKey);
                WebSocketClientImpl.sendMessage(client, JsonMapper.nonEmptyMapper().toJson(verifyMap));
               while (!client.isClosed()){
               }
               LogUtils.info(WebSocketClientImpl.class,"connect is closed");
            }
        }).start();

    }

    public static void sendMessage(WebSocketClientImpl client,String message){
        client.send(message);
        LogUtils.info(WebSocketClientImpl.class,"message send success");
    }

}
