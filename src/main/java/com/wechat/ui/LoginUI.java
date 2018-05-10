package com.wechat.ui;
import com.wechat.App;
import com.wechat.utils.AutoScript;
import com.wechat.utils.LogUtils;
import com.wechat.utils.OcrUtils;
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
	private JRadioButton jb1 = new JRadioButton("Tess4jOcr",true);// 定义一个单选按钮
	private JRadioButton jb2 = new JRadioButton("BaiduOcr");// 定义一个单选按钮
	public LoginUI(int closeOperation) {
		initialize(closeOperation);
	}
	protected void initialize(int closeOperation) {
		frame = new JFrame();
		frame.setTitle("登陆");
		frame.getContentPane().setLayout(null);
		frame.setBounds(0,0,460,300);

		JLabel comment = new JLabel("说明:识别模式Tess4j为本地识别，准确率无法保证。");
		comment.setBounds(40, 0, 420, 30);
		frame.getContentPane().add(comment);

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

		JLabel lbModel = new JLabel("识别模式:");
		lbModel.setBounds(40, 180, 65, 30);
		frame.getContentPane().add(lbModel);

		jb1.setBounds(110,180,120,30);
		jb2.setBounds(240,180,120,30);
		frame.getContentPane().add(jb1);
		frame.getContentPane().add(jb2);
		ButtonGroup group = new ButtonGroup();
		group.add(this.jb1);
		group.add(this.jb2);
		init();

		JButton button = new JButton("登陆");
		button.setBounds(100, 230, 70, 30);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String userName=textField.getText().trim();
				String secretKey=textArea.getText().trim();
				int model=jb1.isSelected()? OcrUtils.TESS4J_MODEL:OcrUtils.BAIDU_MODEL;
				AutoScript.setModel(model);
				File userFile= PropertiesUtils.getUserDir();
				PropertiesUtils.updateProperty(userFile,"userName",userName);
				PropertiesUtils.updateProperty(userFile,"secretKey",secretKey);
				PropertiesUtils.updateProperty(userFile,"ocrModel",model+"");
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
		button_1.setBounds(300, 230, 70, 30);
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
		String model=PropertiesUtils.getString("ocrModel","2");
		if(model.equals("1")){
			jb2.setSelected(true);
		}else{
			jb1.setSelected(true);
		}
		textField.setText(userName);
		textArea.setText(secretKey);
	}

	public static void main(String args[]){
		LoginUI loginUI=new LoginUI(WindowConstants.EXIT_ON_CLOSE);
		loginUI.frame.setVisible(true);
	}


}
