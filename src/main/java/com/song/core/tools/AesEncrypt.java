package com.song.core.tools;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

/**
 * AES加密
 * @author songhj
 *
 */
public class AesEncrypt {

	private static final String AESTYPE = "AES/ECB/PKCS5Padding";
	
	/**
	 * 加密
	 *
	 * @param salt 盐【必须是16位字符】
	 * @param content 待加密的内容
	 * @return
	 */
	public static String encrypt(String salt, String content) {
		byte[] encrypt = null;
		try {
			Key key = generateKey(salt);
			Cipher cipher = Cipher.getInstance(AESTYPE);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			encrypt = cipher.doFinal(content.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String(Base64.encodeBase64(encrypt));
	}

	/**
	 * 解密
	 *
	 * @param salt 盐【必须是16位字符】
	 * @param encryptData 待解密的密⽂
	 * @return
	 */
	public static String decrypt(String salt, String encryptData) {
		byte[] decrypt = null;
		try {
			Key key = generateKey(salt);
			Cipher cipher = Cipher.getInstance(AESTYPE);
			cipher.init(Cipher.DECRYPT_MODE, key);
			decrypt = cipher.doFinal(Base64.decodeBase64(encryptData));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String(decrypt).trim();
	}
	
	public static Key generateKey(String key) throws Exception {
		SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
		return keySpec;
	}
	
	public static void main(String args[]){
		String salt = "Song123Hai456Jun";
		String password = "123456";
		//加密
		String encryptFile = AesEncrypt.encrypt(salt, password);
		System.out.println(encryptFile);
		//解密
		System.out.println(AesEncrypt.decrypt(salt, encryptFile));
	}
}
