package chat.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.naming.ConfigurationException;


public class IOUtils {

	
	
	public static void SaveKeyFile(String fileName,byte[] key){
		try {
			FileOutputStream fileOut = new FileOutputStream(fileName);
			fileOut.write(key);
			fileOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	
	public static void SaveKeyFile(String fileName,String skey){
		try {
			byte[] key = skey.getBytes();
			FileOutputStream fileOut = new FileOutputStream(fileName);
			fileOut.write(key);
			fileOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public static String ReadKeyFile(String fileName){
		try {
			FileInputStream fileIn = new FileInputStream(fileName);
			byte[] keys = new byte[fileIn.available()]; 
			fileIn.read(keys); 
			fileIn.close(); 
			return new String(keys);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null; 
	}
	
	
	
}
