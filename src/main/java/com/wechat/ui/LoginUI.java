package com.wechat.ui;
import com.wechat.App;
import com.wechat.utils.LogUtils;
import com.wechat.utils.PropertiesUtils;
import com.wechat.websocket.WebSocketClientImpl;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URISyntaxException;

public class LoginUI extends AbstractUI{
	private JTextField textField;
	private JTextArea textArea;

	public LoginUI(int closeOperation) {
		initialize(closeOperation);
	}
	protected void initialize(int closeOperation) {
		frame = new JFrame();
		frame.setTitle("登陆");
		frame.getContentPane().setLayout(null);
		frame.setBounds(0,0,460,300);
		JLabel lblSecretkey = new JLabel("用户名:");
		lblSecretkey.setBounds(40, 40, 65, 30);
		frame.getContentPane().add(lblSecretkey);

		JLabel lblApikey = new JLabel("密钥:");
		lblApikey.setBounds(40, 80, 65, 30);
		frame.getContentPane().add(lblApikey);

		textField = new JTextField();
		textField.setBounds(100, 40, 300, 30);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		textArea = new JTextArea();
		textArea.setBounds(100, 80, 300, 80);
		textArea.setColumns(5);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		frame.getContentPane().add(textArea);

		init();

		JButton button = new JButton("登陆");
		button.setBounds(100, 200, 70, 30);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String userName=textField.getText().trim();
				String secretKey=textArea.getText().trim();

				File userFile= PropertiesUtils.getUserDir();
				PropertiesUtils.updateProperty(userFile,"userName",userName);
				PropertiesUtils.updateProperty(userFile,"secretKey",secretKey);

				PropertiesUtils.loadProps(userFile);
				String url=PropertiesUtils.getString("serverUrl","ws://121.54.168.163:8080/websocket");
//				url="ws://121.54.168.163:8080/websocket";

				String uri=String.format("%s/%s",url,userName);
				try {
					WebSocketClientImpl client=WebSocketClientImpl.getAvailableSocketClient(uri);
					if(!client.isConnecting()) {//如果没有连接，则去连接
						WebSocketClientImpl.connect(client);
						WebSocketClientImpl.keepClientAlive(client);
					}
					WebSocketClientImpl.login(client,secretKey);
				} catch (URISyntaxException ex) {
					LogUtils.error(App.class, "程序异常", ex);
				}

			}
		});
		frame.getContentPane().add(button);
		JButton button_1 = new JButton("取消");
		button_1.setBounds(300, 200, 70, 30);
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		frame.getContentPane().add(button_1);
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
	}

	public void init(){
		PropertiesUtils.loadProps(PropertiesUtils.getUserDir());
		String userName=PropertiesUtils.getString("userName","");
		String secretKey=PropertiesUtils.getString("secretKey","");
		textField.setText(userName);
		textArea.setText(secretKey);
	}

//	public static void main(String[]args){
//		AbstractUI ui=new LoginUI(WindowConstants.EXIT_ON_CLOSE);
//		ui.frame.setVisible(true);
//	}

}
