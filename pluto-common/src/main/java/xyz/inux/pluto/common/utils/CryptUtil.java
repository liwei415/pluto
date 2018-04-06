package xyz.inux.pluto.common.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;


/**
 * <ul>
 * <li>BASE64的加密解密是双向的，可以求反解。</li>
 * <li>MD5、SHA以及HMAC是单向加密，任何数据加密后只会产生唯一的一个加密串，通常用来校验数据在传输过程中是否被修改。</li>
 * <li>HMAC算法有一个密钥，增强了数据传输过程中的安全性，强化了算法外的不可控因素。</li>
 * <li>DES DES-Data Encryption Standard,即数据加密算法。 DES算法的入口参数有三个:Key、Data、Mode。
 * <ul>
 * <li>Key:8个字节共64位,是DES算法的工作密钥;</li>
 * <li>Data:8个字节64位,是要被加密或被解密的数据;</li>
 * <li>Mode:DES的工作方式,有两种:加密或解密。</li>
 * </ul>
 * </li>
 * <ul>
 */
public class CryptUtil {
	private static final String KEY_MD5 = "MD5";
	private static final String KEY_SHA = "SHA";
	/**
	 * MAC算法可选以下多种算法
	 * 
	 * <pre>
	 * 
	 * HmacMD5  
	 * HmacSHA1  
	 * HmacSHA256  
	 * HmacSHA384  
	 * HmacSHA512
	 * </pre>
	 */
	public static final String KEY_MAC = "HmacMD5";

	/**
	 * BASE64解密
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptBASE64(String key) throws Exception {
		return Base64.decodeBase64(key);
	}

	/**
	 * BASE64 加密
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encryptBASE64(byte[] key) throws Exception {
		return Base64.encodeBase64URLSafeString(key);
	}

	/**
	 * MD5加密
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptMD5(byte[] data) throws Exception {
		MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
		md5.update(data);

		return md5.digest();
	}
	private static char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', 
		                            '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	public static String encryptMD5(String data) throws Exception {
		MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
		md5.update(data.getBytes("UTF-8"));

		byte[] bs = md5.digest();
		char[] str = new char[16 * 2];
		int k = 0;// 转换结果中对应的字符位置
		for (int i = 0; i < 16; i++) {// 对MD5的每一个字节转换成16进制字符
			byte byte0 = bs[i];
			str[k++] = Digit[byte0 >>> 4 & 0xf];// 对字节高4位进行16进制转换
			str[k++] = Digit[byte0 & 0xf]; // 对字节低4位进行16进制转换
		}
		
		return new String(str);
	}

	/**
	 * SHA加密
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptSHA(byte[] data) throws Exception {
		MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
		sha.update(data);

		return sha.digest();
	}

	/**
	 * 初始化HMAC密钥
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String initMacKey() throws Exception {
		KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_MAC);
		SecretKey secretKey = keyGenerator.generateKey();
		return encryptBASE64(secretKey.getEncoded());
	}

	/**
	 * HMAC 加密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptHMAC(byte[] data, String key) throws Exception {
		SecretKey secretKey = new SecretKeySpec(decryptBASE64(key), KEY_MAC);
		Mac mac = Mac.getInstance(secretKey.getAlgorithm());
		mac.init(secretKey);
		return mac.doFinal(data);
	}

	/**
	 * DES 算法 <br>
	 * 可替换为以下任意一种算法，同时key值的size相应改变。
	 * 
	 * <pre>
	 * DES                  key size must be equal to 56 
	 * DESede(TripleDES)    key size must be equal to 112 or 168 
	 * AES                  key size must be equal to 128, 192 or 256,but 192 and 256 bits may not be available 
	 * Blowfish             key size must be multiple of 8, and can only range from 32 to 448 (inclusive) 
	 * RC2                  key size must be between 40 and 1024 bits 
	 * RC4(ARCFOUR)         key size must be between 40 and 1024 bits
	 * </pre>
	 */
	public static final String ALGORITHM = "DES";

	/**
	 * DES 算法转换密钥<br>
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static Key toKey(byte[] key) throws Exception {
		SecretKey secretKey = null;
		if (ALGORITHM.equals("DES") || ALGORITHM.equals("DESede")) {
			DESKeySpec dks = new DESKeySpec(key);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
			secretKey = keyFactory.generateSecret(dks);
		} else {
			// 当使用其他对称加密算法时，如AES、Blowfish等算法时，用下述代码替换上述三行代码
			secretKey = new SecretKeySpec(key, ALGORITHM);
		}
		return secretKey;
	}

	/**
	 * DES 算法解密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data, String key) throws Exception {
		Key k = toKey(decryptBASE64(key));
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, k);
		return cipher.doFinal(data);
	}

	/**
	 * DES 算法加密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data, String key) throws Exception {
		Key k = toKey(decryptBASE64(key));
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, k);
		return cipher.doFinal(data);
	}

	/**
	 * DES 算法生成密钥
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String initKey() throws Exception {
		return initKey(null);
	}

	/**
	 * DES 算法生成密钥
	 * 
	 * @param seed
	 * @return
	 * @throws Exception
	 */
	public static String initKey(String seed) throws Exception {
		SecureRandom secureRandom = null;
		if (seed != null) {
			secureRandom = new SecureRandom(decryptBASE64(seed));
		} else {
			secureRandom = new SecureRandom();
		}
		KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM);
		kg.init(secureRandom);
		SecretKey secretKey = kg.generateKey();
		return encryptBASE64(secretKey.getEncoded());
	}
	//des加密
	public static byte[] desCrypto(byte[] datasource, String password) {              
		try{  
			SecureRandom random = new SecureRandom();  
			DESKeySpec desKey = new DESKeySpec(password.getBytes());  
			//创建一个密匙工厂，然后用它把DESKeySpec转换成  
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");  
			SecretKey securekey = keyFactory.generateSecret(desKey);  
			//Cipher对象实际完成加密操作  
			Cipher cipher = Cipher.getInstance("DES");  
			//用密匙初始化Cipher对象  
			cipher.init(Cipher.ENCRYPT_MODE, securekey, random);  
			//现在，获取数据并加密  
			//正式执行加密操作  
			return cipher.doFinal(datasource);  
		}catch(Throwable e){  
			e.printStackTrace();  
		}  
		return null;  
	}
	//des解密
    public static byte[] desDecrypt(byte[] src, String password) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom random = new SecureRandom();
        // 创建一个DESKeySpec对象
        DESKeySpec desKey = new DESKeySpec(password.getBytes());
        // 创建一个密匙工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        // 将DESKeySpec对象转换成SecretKey对象
        SecretKey securekey = keyFactory.generateSecret(desKey);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance("DES");
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, random);
        // 真正开始解密操作
        return cipher.doFinal(src);
    }
    public static void testDes(){
        //待加密内容
        String str = "测试内容";
        //密码，长度要是8的倍数
        String password = "12345678";
        byte[] result = desCrypto(str.getBytes(),password);
        System.out.println("加密后内容为："+new String(result));
        
        //直接将如上内容解密
        try {
                byte[] decryResult = desDecrypt(result, password);
                System.out.println("加密后内容为："+new String(decryResult));
        } catch (Exception e1) {
                e1.printStackTrace();
        }
    }
    //aes加密算法
    /** 
     * 加密 aes-256-cbc ,aes
     * @param content 需要加密的内容 
     * @param password  加密密码 
     * @return 
     * 
     */
    public static byte[] aesEncrypt(String algorithm, String content, String password) {
    	try {
    		KeyGenerator kgen = KeyGenerator.getInstance("AES");  
    		kgen.init(128, new SecureRandom(password.getBytes()));  
    		SecretKey secretKey = kgen.generateKey();  
    		byte[] enCodeFormat = secretKey.getEncoded();
    		SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");  
    		Cipher cipher = Cipher.getInstance(algorithm);// 创建密码器   
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
    /**
     * AES解密 aes-256-cbc ,aes
     * @param content  待解密内容 
     * @param password 解密密钥 
     * @return
     */
    public static byte[] aesDecrypt(String algorithm, byte[] content, String password) {
    	try {
    		KeyGenerator kgen = KeyGenerator.getInstance("AES");
    		kgen.init(128, new SecureRandom(password.getBytes()));
    		SecretKey secretKey = kgen.generateKey();  
    		byte[] enCodeFormat = secretKey.getEncoded(); 
    		SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");              
    		Cipher cipher = Cipher.getInstance(algorithm);// 创建密码器   
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
    public static void aesTest() throws Exception{
    	String content = "21.2ccfe5658eafb2aa3bca3856d4b1dad5.2592000.1403315977.1447461198-2406673";
    	String password = "7f386d0503477dc82e5be5cae2bfc990";
    	String algorithm = "AES";//AES, AES-256-CBC, AES/CBC/PKCS5Padding
    	
    	//加密   
    	System.out.println("加密前：" + content);  
    	byte[] encryptResult = aesEncrypt(algorithm, content, password); 
    	//加密值
    	System.out.println("加密值BASE64:" + encryptBASE64(encryptResult));
    	String tmpString = parseByte2HexStr(encryptResult);
    	System.out.println("加密值:" + tmpString);
    	//解密   
    	byte[] tmpArray = parseHexStr2Byte(tmpString);
    	byte[] decryptResult = aesDecrypt(algorithm, tmpArray, password);  
    	System.out.println("解密后：" + new String(decryptResult));  
    }
    
    /**
     * 将二进制转换成16进制 
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
    /**
     * 将16进制转换为二进制 
     * @param hexStr 
     * @return 
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
    	if (hexStr.length() < 1){
    		return null;
    	}
    	byte[] result = new byte[hexStr.length()/2];  
    	for (int i = 0;i< hexStr.length()/2; i++) {  
    		int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);  
    		int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);  
    		result[i] = (byte) (high * 16 + low);  
    	}  
    	return result;  
    }  
	
	public static void main(String[] args) throws Exception{
		//aesTest();
		//testAESCSC();
		//zCc5xk_vsZk
		String result = CryptUtil.encryptMD5("20140420085120764000");
		System.out.println(result);
		//byte[] bs = CryptUtil.aesEncrypt(algorithm, content, password)
		//System.out.println(new String(bs));
		long L1 = 0x5e366e280191ebebL;
		long L2 = 0x316cde785d56a38fL;
		long L3 = 0x826b7418434cf76aL;
		System.out.println(L1-L2);
		System.out.println(L3-L1);
		System.out.println(2*L1 - L3 - L2);
	}
	
	public static void testAESCSC() throws Exception {
        SecureRandom rnd = new SecureRandom();

        String text = "Hello, my dear! ... " + System.getProperty("user.home");
        byte[] textData = text.getBytes();

        IvParameterSpec iv = new IvParameterSpec(rnd.generateSeed(16));

        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128);
        SecretKey k = generator.generateKey();

        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, k, iv);
        byte[] someData = c.update(textData);
        byte[] data = c.doFinal();

        System.out.println("E: " + someData.length + "," + data.length);

        c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, k, iv);
        byte[] someDecrypted = c.update(someData);
        byte[] moreDecrypted = c.doFinal(data);

        System.out.println("R: " + (someDecrypted.length + "," + moreDecrypted.length));
    }
}
