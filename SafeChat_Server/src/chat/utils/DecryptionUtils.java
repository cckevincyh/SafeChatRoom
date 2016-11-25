package chat.utils;

import java.io.IOException;

import chat.common.Message;

public class DecryptionUtils {

	/**
	 * RSA解密
	 * @param filePrivateKeyName 要使用的私钥的文件路径
	 * @param data 要解密的数据
	 * @return 返回解密后的数据
	 */
	public static byte[] decryptByPrivateKey(String filePrivateKeyName,byte[] data){
		//1.读取文件名为:filePrivateKeyName的密钥文件
		String key = IOUtils.ReadKeyFile(filePrivateKeyName);
		  try {
			//2.用读取出来的密钥对数据进行解密
			byte[] decodedData = RSAUtils.decryptByPrivateKey(data, key);
			return decodedData;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		return null;
	}
	
	/**
	 * 服务器解密消息
	 * @param filePrivateKeyName 要使用的私钥的文件路径
	 * @param message 接受的消息
	 */
	public static void decryptMessage(String filePrivateKeyName,Message message){
		/*
		 * 服务器端解密客户端发送过来的消息步骤:
		 * 	1.获取发送过来消息里的KEY
		 * 		1.1. 用服务器的私钥对KEY进行RSA解密,得到DES解密的KEY
		 * 	2.获取加密的内容(使用的DES加密)
		 * 		2.2. 用解密后的KEY对发送过来的消息内容进行DES解密得到最后的明文消息
		 * 
		 */
		//1.用私钥解密message中的key
		byte[] key = decryptByPrivateKey(filePrivateKeyName, message.getKey());
		try {
			//2.解密出来的key作为DES的key去解密message中的消息
			String content = DesUtil.decrypt(message.getContent(),key);
			//3.把解密出来的消息设置会给message
			message.setContent(content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
