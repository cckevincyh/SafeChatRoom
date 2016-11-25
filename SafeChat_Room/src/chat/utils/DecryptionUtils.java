package chat.utils;

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
	
	
	
}
