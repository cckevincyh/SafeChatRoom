package chat_room.client.backstage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;

import chat.common.Message;
import chat.common.MessageType;
import chat.common.User;
import chat.utils.AESUtils;
import chat.utils.DecryptionUtils;
import chat.utils.DesUtil;
import chat.utils.EncryptionUtils;


/**
 * 客户端后台的处理类
 * @author Administrator
 *
 */
public class ClienManage {
	private ObjectOutputStream os;
	private ObjectInputStream ois;
	private Socket s;
	private boolean isConnect = true;
	private ServerSocket ss;
	
	private BufferedOutputStream bos;
	private BufferedInputStream bis;
	private CipherInputStream cipherInputStream;
	public ClienManage(){
		try {
			 s = new Socket("127.0.0.1", 9999);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(ConnectException e){
			isConnect = false;
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 用于注册用户的方法 
	 * @param Name 用户名
	 * @param PassWord 密码
	 * @return 返回是否注册成功
	 */
	public boolean Register(User user){
		boolean b = false;
		try {
			if(s!=null){
				os = new ObjectOutputStream(s.getOutputStream());
				user.setType(MessageType.UserRegister);
				os.writeObject(user);
				ois = new ObjectInputStream(s.getInputStream());
				Message mes = (Message)ois.readObject();
				if(mes.getMessageType().equals(MessageType.Register_Success)){
					b = true;
				}
			}
		}catch (IOException e) {
			// TODO Auto-generated catch block
			b = false;
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			b = false;
			e.printStackTrace();
		}
		return b;
	}
	
	/**
	 * 用于用户登陆的方法
	 * @param user 用户对象
	 * @return 返回用户是否登陆成功
	 */
	public boolean Login(){
		boolean b = false;
		try {
			if(s!=null){
				ois = new ObjectInputStream(s.getInputStream());
				Message mess = (Message)ois.readObject();
				if(mess.getMessageType().equals(MessageType.Login_Success)){
					b = true;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			b = false;
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			b = false;
			e.printStackTrace();
		}
		return b;
	}
	
	/**
	 * 检查是否登陆过了的方法
	 * @return 是否登陆过
	 */
	public boolean Check_isLogin(User user){
		boolean b = false;
		try {
			if(s!=null){
				os = new ObjectOutputStream(s.getOutputStream());
				user.setType(MessageType.UserLogin);
				os.writeObject(user);
				ois = new ObjectInputStream(s.getInputStream());
				Message mess = (Message)ois.readObject();
				if(mess.getMessageType().equals(MessageType.Login)){
					b = true;
				}else if(mess.getMessageType().equals(MessageType.NoLogin)){
					b = false;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			b = true;
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			b = true;
			e.printStackTrace();
		}
		return b;
	}
	
	/**
	 * 接收信息的方法
	 * @return 返回接收的信息
	 */
	public Message ReciveMessage(){
		Message mess = null;
		if(s!=null){
			try {
				ois = new ObjectInputStream(s.getInputStream());
				 mess = (Message)ois.readObject();	
			}catch(SocketException e){
				isConnect = false;
				e.printStackTrace();
			}catch(EOFException e){
				isConnect = false;
				e.printStackTrace();
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return mess;
		
	}
	
	
	/**
	 * 发送信息的方法
	 * @param mess 信息
	 */
	public void SendMessage(Message mess){
		if(s!=null){
			try {
				/************加密****************/
				//发送消息之前需要加密
				EncryptionUtils.encryptMessage("publicKey.key", mess);
				/************加密****************/
				os = new ObjectOutputStream(s.getOutputStream());
				os.writeObject(mess);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * 关闭资源的方法
	 */
	public void CloseResource(){
			try {
				if(s!=null){
					s.close();
				}if(os!=null){
					os.close();
				}
				if(ois!=null){
					ois.close();
				}
				if(cipherInputStream!=null){
					cipherInputStream.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
	/**
	 * 发送文件的方法
	 * @param path 路径 
	 * @param key AES加密需要的key
	 */
	public void SendFile(String path,String key){
		Socket s1 = null;
		try {
			//新创建一个TCP协议
			s1 = new Socket("127.0.0.1", 8888);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			bos = new BufferedOutputStream(s1.getOutputStream());
			
		//	bis = new BufferedInputStream(new FileInputStream(path));
			/************AES加密文件数据*******************/
			Cipher cipher = AESUtils.initAESCipher(key,Cipher.ENCRYPT_MODE);   
             cipherInputStream = new CipherInputStream(new FileInputStream(path), cipher);  
        	/************AES加密文件数据*******************/
            byte[] bys = new byte[1024];
			int len = 0;
			while ((len = cipherInputStream.read(bys)) != -1) {
				
				bys = AESUtils.parseHexStr2Byte(AESUtils.parseByte2HexStr(bys));
				bos.write(bys, 0, len);
				bos.flush();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(s1!=null){
				try {
					s1.shutdownOutput();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	
	
	/**
	 * 判断是否连接到服务器
	 * @return 是否连接到服务器
	 */
	public boolean IsConnect(){
		return isConnect;
	}
	
	/**
	 * 接受文件的方法
	 */
	public void ReciveFile(Message mess){
		Socket s1 = null;
		try {
			ss = new ServerSocket(7777);
			s1 = ss.accept();
			/***************RAS解密服务器端发送过来的key*******************/
			DecryptionUtils.decryptFileKey(mess);
			/***************RAS解密服务器端发送过来的key*******************/
			//bis = new BufferedInputStream(s1.getInputStream());
			/**********************进行AES解密*****************************/
			Cipher cipher = AESUtils.initAESCipher(new String(mess.getKey()),Cipher.DECRYPT_MODE);  
            //以加密流写入文件  
             cipherInputStream = new CipherInputStream(s1.getInputStream(), cipher);  
            /**********************进行AES解密*****************************/
			bos = new BufferedOutputStream(new FileOutputStream(mess.getContent()));
			byte[] bys = new byte[1024];
			int len = 0;
			while ((len = cipherInputStream.read(bys)) != -1) {
				bos.write(bys, 0, len);
				bos.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if(ss!=null){
					ss.close();
				}
				if(bis!=null){
					bis.close();
				}if(s1!=null){
					s1.close();
				}if(bos!=null){
					bos.close();
				}
				if(cipherInputStream!=null){
					cipherInputStream.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
