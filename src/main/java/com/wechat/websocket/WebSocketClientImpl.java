package com.wechat.websocket;

import com.wechat.App;
import com.wechat.queue.MqManager;
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
        LogUtils.info(WebSocketClientImpl.class, "onOpen:");
    }

    @Override
    public void onMessage(String s) {
        LogUtils.info(WebSocketClientImpl.class, "onMessage:" + s);
        Map<String, Object> verifyMap = JsonMapper.nonEmptyMapper().fromJson(s, Map.class);
        String phone = (String) verifyMap.get("verifyPhone");
        String verifyCode = (String) verifyMap.get("verifyCode");
        String code = (String) verifyMap.get("code");
        if (!StringUtil.isBlank(code)) {
            if (code.equals("success")) {
                App.loginSuccess();
            } else {
                App.loginFailure((String) verifyMap.get("errorMsg"));
            }
        }
        if (!StringUtil.isBlank(phone) && !StringUtil.isBlank(verifyCode)) {
            LogUtils.info(WebSocketClientImpl.class, "do add friend and send code...");
            try {
                MqManager.getMq(String.format("VERIFY_FRIEND")).push(JsonMapper.nonEmptyMapper().toJson(verifyMap));
            } catch (InterruptedException e) {
                LogUtils.error(WebSocketClientImpl.class, "操作进入消息队列异常", e);
            }
        }
    }

    public void doWith() throws InterruptedException {
        while (true) {
            String json = MqManager.getMq(String.format("VERIFY_FRIEND")).pop();
            Map<String, Object> verifyMap = JsonMapper.nonEmptyMapper().fromJson(json, Map.class);
            Long userId = (Integer) verifyMap.get("userId") * 1L;
            Long verifyInfoId = (Integer) verifyMap.get("verifyInfoId") * 1L;
            String phone = (String) verifyMap.get("verifyPhone");
            String verifyCode = (String) verifyMap.get("verifyCode");
            WechatUtils.goWechatHome();
            WechatUtils.addFriendAndSendMsg(phone, verifyCode);
            if(!WechatUtils.addFriendSuccess){//如果没有添加成功
                verifyMap.put("resultCode", "error");
                verifyMap.put("errorMsg","add friend failure");
                sendMessage(socketClient, JsonMapper.nonEmptyMapper().toJson(verifyMap));
                LogUtils.info(WebSocketClientImpl.class,"do with error:add friend failure");
            }else if (WechatUtils.addFriendSuccess&&WechatUtils.isNeedToRequestAddFriend){
                verifyMap.put("resultCode", "error");
                verifyMap.put("errorMsg","the friend need request verify");
                sendMessage(socketClient, JsonMapper.nonEmptyMapper().toJson(verifyMap));
                LogUtils.info(WebSocketClientImpl.class,"do with error:the friend need request verify");
            }else if(WechatUtils.addFriendSuccess&&WechatUtils.msgIsNoEqualCode){
                verifyMap.put("resultCode", "error");
                verifyMap.put("errorMsg","send msg no equals code");
                sendMessage(socketClient, JsonMapper.nonEmptyMapper().toJson(verifyMap));
                LogUtils.info(WebSocketClientImpl.class,"do with error:send msg no equals code");
            }else if (WechatUtils.addFriendSuccess&&WechatUtils.isNeedAutoSendMsg) {
                boolean autoSuccess = WechatUtils.autoSendMsg(phone, verifyCode);
                if (autoSuccess) {
                    verifyMap.put("resultCode", "success");
                    sendMessage(socketClient, JsonMapper.nonEmptyMapper().toJson(verifyMap));
                    LogUtils.info(WebSocketClientImpl.class,"do with success");
                } else {
                    verifyMap.put("resultCode", "error");
                    verifyMap.put("errorMsg","auto send msg error");
                    sendMessage(socketClient, JsonMapper.nonEmptyMapper().toJson(verifyMap));
                    LogUtils.info(WebSocketClientImpl.class,"do with error:auto send msg error");

                }
            }else if(WechatUtils.addFriendSuccess){
                verifyMap.put("resultCode", "success");
                sendMessage(socketClient, JsonMapper.nonEmptyMapper().toJson(verifyMap));
                LogUtils.info(WebSocketClientImpl.class,"auto send msg success");
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
        if (socketClient == null) {
            synchronized (WebSocketClientImpl.class) {
                if (socketClient == null)
                    socketClient = new WebSocketClientImpl(new URI(uri));
            }
        }
        return socketClient;
    }

    public static WebSocketClientImpl getSocketClient() {
        return socketClient;
    }


    public static void connect(WebSocketClientImpl client) {
        client.connect();
        while (!client.getReadyState().equals(READYSTATE.OPEN)) {
            LogUtils.info(WebSocketClientImpl.class, "web socket is connect");
            SleepUtils.sleep(300L);
        }
        LogUtils.info(WebSocketClientImpl.class, "web socket connected");
    }

    public static void login(WebSocketClientImpl client, String secretKey) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> verifyMap = new HashMap<>();
                verifyMap.put("secretkey", secretKey);
                WebSocketClientImpl.sendMessage(client, JsonMapper.nonEmptyMapper().toJson(verifyMap));
                LogUtils.info(WebSocketClientImpl.class, "login message send success");
            }
        }).start();
    }


    public static void keepClientAlive(WebSocketClientImpl client) {

        new Thread(new Runnable() {//处理任务
            @Override
            public void run() {
                try {
                    client.doWith();
                } catch (InterruptedException e) {
                    LogUtils.error(WebSocketClientImpl.class, "消息出队列异常", e);
                }
            }
        }).start();
    }

    public static void sendMessage(WebSocketClientImpl client, String message) {
        client.send(message);
        LogUtils.info(WebSocketClientImpl.class, "message send success");
    }

}
