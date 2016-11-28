package chat.utils;

import java.io.IOException;

import chat.common.Message;
import chat.common.MessageType;

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
	 * 客户端解密接受到的消息
	 * @param message	接受到的消息
	 * @return
	 */
	public static void decryptMessage(Message message) throws IOException{
		/*
		 * 客户端解密接受到的消息的步骤：
		 * 	1.RSA解密消息中的KEY
		 * 		1.1.取得消息中的KEY
		 * 		1.2.用客户端上的私钥解密KEY,得到AES加密的KEY
		 * 2.AES机密消息中的内容
		 * 		2.1.得到消息中的内容
		 * 		2.2.用解密出来的KEY,对内容进行AES解密操作得到消息
		 *		2.3.设置消息内容
		 */
		
		
		//1.用客户端上的私钥解密KEY,得到DES加密的KEY
		byte[] AESKey = decryptByPrivateKey(message.getGetter()+"_privateKey.key",message.getKey());
		
		try {
			//2.用解密出来的KEY,对内容进行DES解密操作得到消息
			String decrypt = new String(AESUtils.decrypt(AESUtils.parseHexStr2Byte(message.getContent()), new String(AESKey)));
			//3.设置消息内容
			message.setContent(decrypt);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	
	
	
}
