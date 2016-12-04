package chat.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {

	/** 
	 * 加密 
	 *  
	 * @param content 需要加密的内容 
	 * @param password  加密密码 
	 * @return 
	 */  
	public static byte[] encrypt(String content, String password) {  
	        try {             
	                KeyGenerator kgen = KeyGenerator.getInstance("AES");  
	                kgen.init(128, new SecureRandom(password.getBytes()));  
	                SecretKey secretKey = kgen.generateKey();  
	                byte[] enCodeFormat = secretKey.getEncoded();  
	                SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");  
	                Cipher cipher = Cipher.getInstance("AES");// 创建密码器  
	                byte[] byteContent = content.getBytes("utf-8");  
	                cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化  
	                byte[] result = cipher.doFinal(byteContent);  
	                return result; // 加密  
	        } catch (NoSuchAlgorithmException e) {  
	                e.printStackTrace();  
	        } catch (NoSuchPaddingException e) {  
	                e.printStackTrace();  
	        } catch (InvalidKeyException e) {  
	                e.printStackTrace();  
	        } catch (UnsupportedEncodingException e) {  
	                e.printStackTrace();  
	        } catch (IllegalBlockSizeException e) {  
	                e.printStackTrace();  
	        } catch (BadPaddingException e) {  
	                e.printStackTrace();  
	        }  
	        return null;  
	}  
	
	
	
	/**解密 
	 * @param content  待解密内容 
	 * @param password 解密密钥 
	 * @return 
	 */  
	public static byte[] decrypt(byte[] content, String password) {  
	        try {  
	                 KeyGenerator kgen = KeyGenerator.getInstance("AES");  
	                 kgen.init(128, new SecureRandom(password.getBytes()));  
	                 SecretKey secretKey = kgen.generateKey();  
	                 byte[] enCodeFormat = secretKey.getEncoded();  
	                 SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");              
	                 Cipher cipher = Cipher.getInstance("AES");// 创建密码器  
	                cipher.init(Cipher.DECRYPT_MODE, key);// 初始化  
	                byte[] result = cipher.doFinal(content);  
	                return result; // 加密  
	        } catch (NoSuchAlgorithmException e) {  
	                e.printStackTrace();  
	        } catch (NoSuchPaddingException e) {  
	                e.printStackTrace();  
	        } catch (InvalidKeyException e) {  
	                e.printStackTrace();  
	        } catch (IllegalBlockSizeException e) {  
	                e.printStackTrace();  
	        } catch (BadPaddingException e) {  
	                e.printStackTrace();  
	        }  
	        return null;  
	}  
	
	
	/**将二进制转换成16进制 
	 * @param buf 
	 * @return 
	 */  
	public static String parseByte2HexStr(byte buf[]) {  
	        StringBuffer sb = new StringBuffer();  
	        for (int i = 0; i < buf.length; i++) {  
	                String hex = Integer.toHexString(buf[i] & 0xFF);  
	                if (hex.length() == 1) {  
	                        hex = '0' + hex;  
	                }  
	                sb.append(hex.toUpperCase());  
	        }  
	        return sb.toString();  
	}  
	
	
	/**将16进制转换为二进制 
	 * @param hexStr 
	 * @return 
	 */  
	public static byte[] parseHexStr2Byte(String hexStr) {  
	        if (hexStr.length() < 1)  
	                return null;  
	        byte[] result = new byte[hexStr.length()/2];  
	        for (int i = 0;i< hexStr.length()/2; i++) {  
	                int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);  
	                int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);  
	                result[i] = (byte) (high * 16 + low);  
	        }  
	        return result;  
	}  
	
	

	/** 
     * 初始化 AES Cipher 
     * @param sKey 
     * @param cipherMode 
     * @return 
     */  
    public static Cipher initAESCipher(String sKey, int cipherMode) {  
        //创建Key gen  
        KeyGenerator keyGenerator = null;  
        Cipher cipher = null;  
        try {  
            keyGenerator = KeyGenerator.getInstance("AES");  
            keyGenerator.init(128, new SecureRandom(sKey.getBytes()));  
            SecretKey secretKey = keyGenerator.generateKey();  
            byte[] codeFormat = secretKey.getEncoded();  
            SecretKeySpec key = new SecretKeySpec(codeFormat, "AES");  
            cipher = Cipher.getInstance("AES");  
            //初始化  
            cipher.init(cipherMode, key);  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.  
        } catch (NoSuchPaddingException e) {  
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.  
        } catch (InvalidKeyException e) {  
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.  
        }  
        return cipher;  
    }  
	
	
	public static void main(String[] args) {
//		test2();
		test3();
	}
	
	
	public static void test1(){
		String content = "test";  
		String password = "12345678";  
		//加密  
		System.out.println("加密前：" + content);  
		byte[] encryptResult = encrypt(content, password);  
		//解密  
		byte[] decryptResult = decrypt(encryptResult,password);  
		System.out.println("解密后：" + new String(decryptResult));  
	}
	
	public static void test2(){
		String content = "test";  
		String password = "12345678";  
		//加密  
		System.out.println("加密前：" + content);  
		byte[] encryptResult = encrypt(content, password);  
		try {  
		        String encryptResultStr = new String(encryptResult,"utf-8");  
		        //解密  
		        byte[] decryptResult = decrypt(encryptResultStr.getBytes("utf-8"),password);  
		        System.out.println("解密后：" + new String(decryptResult));  
		} catch (UnsupportedEncodingException e) {  
		        e.printStackTrace();  
		}  
	}
	public static void test3(){
		String content = "test";  
		String password = "12345678";  
		//加密  
		System.out.println("加密前：" + content);  
		byte[] encryptResult = encrypt(content, password);  
		String encryptResultStr = parseByte2HexStr(encryptResult);  
		System.out.println("加密后：" + encryptResultStr);  
		//解密  
		byte[] decryptFrom = parseHexStr2Byte(encryptResultStr);  
		byte[] decryptResult = decrypt(decryptFrom,password);  
		System.out.println("解密后：" + new String(decryptResult));  
	}
}
