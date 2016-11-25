package chat.utils;

import java.util.UUID;

import org.junit.Test;

import chat.common.Message;
import chat.common.MessageType;

/*
                       ::
                      :;J7, :,                        ::;7:
                      ,ivYi, ,                       ;LLLFS:
                      :iv7Yi                       :7ri;j5PL
                     ,:ivYLvr                    ,ivrrirrY2X,
                     :;r@Wwz.7r:                :ivu@kexianli.
                    :iL7::,:::iiirii:ii;::::,,irvF7rvvLujL7ur
                   ri::,:,::i:iiiiiii:i:irrv177JX7rYXqZEkvv17
                ;i:, , ::::iirrririi:i:::iiir2XXvii;L8OGJr71i
              :,, ,,:   ,::ir@mingyi.irii:i:::j1jri7ZBOS7ivv,
                 ,::,    ::rv77iiiriii:iii:i::,rvLq@huhao.Li
             ,,      ,, ,:ir7ir::,:::i;ir:::i:i::rSGGYri712:
           :::  ,v7r:: ::rrv77:, ,, ,:i7rrii:::::, ir7ri7Lri
          ,     2OBBOi,iiir;r::        ,irriiii::,, ,iv7Luur:
        ,,     i78MBBi,:,:::,:,  :7FSL: ,iriii:::i::,,:rLqXv::
        :      iuMMP: :,:::,:ii;2GY7OBB0viiii:i:iii:i:::iJqL;::
       ,     ::::i   ,,,,, ::LuBBu BBBBBErii:i:i:i:i:i:i:r77ii
      ,       :       , ,,:::rruBZ1MBBqi, :,,,:::,::::::iiriri:
     ,               ,,,,::::i:  @arqiao.       ,:,, ,:::ii;i7:
    :,       rjujLYLi   ,,:::::,:::::::::,,   ,:i,:,,,,,::i:iii
    ::      BBBBBBBBB0,    ,,::: , ,:::::: ,      ,,,, ,,:::::::
    i,  ,  ,8BMMBBBBBBi     ,,:,,     ,,, , ,   , , , :,::ii::i::
    :      iZMOMOMBBM2::::::::::,,,,     ,,,,,,:,,,::::i:irr:i:::,
    i   ,,:;u0MBMOG1L:::i::::::  ,,,::,   ,,, ::::::i:i:iirii:i:i:
    :    ,iuUuuXUkFu7i:iii:i:::, :,:,: ::::::::i:i:::::iirr7iiri::
    :     :rk@Yizero.i:::::, ,:ii:::::::i:::::i::,::::iirrriiiri::,
     :      5BMBBBBBBSr:,::rv2kuii:::iii::,:i:,, , ,,:,:i@petermu.,
          , :r50EZ8MBBBBGOBBBZP7::::i::,:::::,: :,:,::i;rrririiii::
              :jujYY7LS0ujJL7r::,::i::,::::::::::::::iirirrrrrrr:ii:
           ,:  :@kevensun.:,:,,,::::i:i:::::,,::::::iir;ii;7v77;ii;i,
           ,,,     ,,:,::::::i:iiiii:i::::,, ::::iiiir@xingjief.r;7:i,
        , , ,,,:,,::::::::iiiiiiiiii:,:,:::::::::iiir;ri7vL77rrirri::
         :,, , ::::::::i:::i:::i:i::,,,,,:,::i:i:::iir;@Secbone.ii:::
		 
*/



/**
 * 加密工具
 * @author c
 *
 */
public class EncryptionUtils {

	/**
	 * RSA公钥加密
	 * @param filePublicKeyName 要使用的公钥的文件路径
	 * @param data	要加密的数据
	 * @return 返回加密后的数据
	 */
	public static  byte[] encryptByPublicKey(String filePublicKeyName,String data){
		//1.读取文件名为:filePublicKeyName的密钥文件
		String key = IOUtils.ReadKeyFile(filePublicKeyName);
        try {
        	//2.用读取出来的密钥对数据进行加密
			byte[] encodedData = RSAUtils.encryptByPublicKey(data.getBytes(), key);
			return encodedData;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        return  null;
	}
	
	
	/**
	 * RSA私钥加密
	 * @param filePrivateKeyName 要使用的私钥的文件路径
	 * @param data 要加密的数据
	 * @return 返回加密后的数据
	 */
	public static byte[] encryptByPrivateKey(String filePrivateKeyName,String data){
		//1.读取文件名为:filePrivateKeyName的密钥文件
		String key = IOUtils.ReadKeyFile(filePrivateKeyName);
		 try {
        	//2.用读取出来的密钥对数据进行加密
			byte[] encodedData = RSAUtils.encryptByPrivateKey(data.getBytes(), key);
			return encodedData;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	       return  null;
	}
	
	/**
	 * 客户端加密消息数据
	 * @param filePublicKeyName 要使用的公钥的文件路径
	 * @param message 发送的消息
	 */
	public static void encryptMessage(String filePublicKeyName,Message message){
		/*
		 * 客户端加密消息数据的步骤:
		 * 1.DES加密发送的消息内容.
		 * 		1.1 生成KEY,生成唯一的UUID作为KEY
		 * 		1.2用这个KEY来进行DES加密发送的内容
		 * 		1.3设置加密内容
		 * 2.加密发送能够解密DES的key.
		 * 		2.1对KEY用服务器派发的公钥进行RSA加密(只有服务器的私钥可以解密)
		 *		2.2设置key
		 */		
				
		//1.生成唯一的UUID
		String key = UUID.randomUUID().toString();
		//2.用这个UUID作为key来加密发送消息
		//判断发送的类型
		//如果是发送文件
		if(message.getMessageType().equals(MessageType.Common_Message_ToPerson)||message.getMessageType().equals(MessageType.Common_Message_ToAll)){
			try {
				//设置DES加密后的消息
				message.setContent(new String(DesUtil.encrypt(message.getContent(), key)));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			//2.对Key,用公钥加密
			byte[] encryptKey = encryptByPublicKey("publicKey.key", key);
			//3.设置消息中的key
			message.setKey(encryptKey);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	
	
	
}
