package chat.utils;

import java.io.IOException;
import java.util.UUID;

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
	public static void decryptMessage(String filePrivateKeyName,Message message) throws IOException{
		/*
		 * 服务器端解密客户端发送过来的消息步骤:
		 * 	1.获取发送过来消息里的KEY
		 * 		1.1. 用服务器的私钥对KEY进行RSA解密,得到AES解密的KEY
		 * 	2.获取加密的内容(使用的AES加密)
		 * 		2.2. 用解密后的KEY对发送过来的消息内容进行AES解密得到最后的明文消息
		 * 
		 */
		//1.用私钥解密message中的key
		byte[] key = decryptByPrivateKey(filePrivateKeyName, message.getKey());
		try {
			//2.解密出来的key作为AES的key去解密message中的消息		
			String content = new String(AESUtils.decrypt(AESUtils.parseHexStr2Byte(message.getContent()), new String(key)));
			//3.把解密出来的消息设置会给message
			message.setContent(content);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 服务器解密发送文件消息的KEY
	 * @param message 对message设置加密后的KEY
	 * @return 返回KEY
	 */
	public static void decryptFileKey(Message message){
		//对Key,用私钥解密
		byte[] encryptByPublicKey = decryptByPrivateKey("privateKey.key", message.getKey());
		//设置消息中的key
		message.setKey(encryptByPublicKey);
	}
	
	
	
	
	/**
	 * 验证加密后的数字签名
	 * @param decryptKey 客户端发送过来的已解密好的公钥
	 * @param verifyKey	需要验证的加密数字签名
	 * @return
	 */
	public static boolean verifyEncryptKeySign(byte[] decryptKey,byte[] verifyKey){
		boolean b = true;
		/*
		 * 验证加密后的数字签名的步骤：
		 * 	1.用解密好的公钥去校验数字签名
		 * 			如果校验不成功，说明不是客户端发来的
		 * 	2.用已经解密好的公钥去解密数字签名
		 * 			如果解密不成功说明不是客户端发来的
		 * 	3.对解密好的公钥，进行加盐在进行md5加密,和解密好的数字签名匹配
		 * 			如果匹配不成功，说明不是客户端发送来的
		 */
		//1.用解密好的公钥去校验数字签
		//待研究......
		
		
		
		
		return b;
	}
	
	
	
}
