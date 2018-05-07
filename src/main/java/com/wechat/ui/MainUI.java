package com.wechat.ui;
import com.wechat.utils.PropertiesUtils;
import com.wechat.utils.SleepUtils;
import com.wechat.websocket.WebSocketClientImpl;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class MainUI extends AbstractUI{

	private static Long startTime=System.currentTimeMillis();
	private JLabel timer;
	public MainUI(int closeOperation) {
		initialize(closeOperation);
	}
	protected void initialize(int closeOperation) {
		frame = new JFrame();
		frame.setTitle("程序正在运行...");
		frame.getContentPane().setLayout(null);
		frame.setBounds(0,0,350,100);
		timer=new JLabel();
		timer.setBounds(10,10,330,40);
		timer.setFont(new   java.awt.Font("Dialog",   1,   20));
		timer.setText("程序已经运行：0秒");
		frame.getContentPane().add(timer);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(closeOperation);// 设置主窗体关闭按钮样式
		frame.setResizable(false);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				//去关闭socekt 连接
				WebSocketClientImpl client=WebSocketClientImpl.getSocketClient();
				if(client!=null&&client.isConnecting()){
					client.close();
				}
				super.windowClosing(e);
			}
		});
		new Thread(new Runnable() {
			@Override
			public void run() {
				Long now;
				Long time;
				Integer minute;
				Integer second;
				Integer hour;
				Integer day;
				while(true){
					minute=0;
					second=0;
					hour=0;
					day=0;
					now=System.currentTimeMillis();
					time=(now-startTime)/1000;
					day= Math.toIntExact(time / (24 * 60 * 60));
					hour= Math.toIntExact(time / (60 * 60));
					minute= Math.toIntExact(time / 60);
					second= Math.toIntExact(time % 60);
					StringBuilder sb=new StringBuilder();
					if(day>0){
						sb.append(day+"天");
					}
					if(hour>0){
						sb.append(hour+"时");
					}
					if(minute>0){
						sb.append(minute+"分");
					}
					sb.append(second+"秒");
					String text=String.format("程序已经运行了:%s",sb.toString());
					timer.setText(text);
					frame.getContentPane().repaint();
					SleepUtils.sleep(1000L);
				}
			}
		}).start();
	}
	public static void main(String[]args){
		AbstractUI ui=new MainUI(WindowConstants.EXIT_ON_CLOSE);
		ui.frame.setVisible(true);
	}

}
