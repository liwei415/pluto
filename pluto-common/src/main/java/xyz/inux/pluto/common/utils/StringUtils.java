package xyz.inux.pluto.common.utils;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtils {
	private static final Pattern KVP_PATTERN = Pattern.compile("([_.a-zA-Z0-9][-_.a-zA-Z0-9]*)[=](.*)"); //key value pair pattern.
	
	/**
	 * 判断一个字符串是否空串,其中null,""," "都算作空串
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(final String str){
		if(str == null){
			return true;
		}
		
		String tmpStr = str.trim();
		if(tmpStr.equals("")){
			return true;
		}
		
		return false;
	}
	/**
	 * 判断一个字符串是否是数字
	 * @param str
	 * @param type 1-不允许带符号,2-允许带符号(-\+)
	 * @return
	 */
	public static boolean isNumeric(final String str, final int type){
		if(isEmpty(str)){
			return false;
		}
		if(type < 1 || type > 2){
			return false;
		}
		String tmpString = str.trim();
		int length = tmpString.length();
		if(type == 1 || length == 1){// 不包含符号位
			return org.apache.commons.lang3.StringUtils.isNumeric(tmpString);
		}
		//判断符号位
		String firstStr = tmpString.substring(0, 1);
		if(firstStr.equals("+") || firstStr.equals("-")){
			return org.apache.commons.lang3.StringUtils.isNumeric(tmpString.substring(1));
		}else{
			return org.apache.commons.lang3.StringUtils.isNumeric(tmpString);
		}
	}
	public static boolean isUnsignedNumeric(final String str){
		return isNumeric(str, 1);
	}
	public static boolean isSignedNumeric(final String str){
		return isNumeric(str, 2);
	}
	/**
	 * 根据pattern拼字符串
	 * @param src
	 * @param posSign
	 * @return
	 */
	public static String makeStringByParam(final String src, final String posSign){
		if(StringUtils.isEmpty(posSign)){
			return src;
		}
		return makeStringByParams(src, new String[]{posSign.trim()});
	}
	/**
	 * 根据pattern拼字符串
	 * @param src
	 * @param posSign
	 * @return
	 */
	public static String makeStringByParams(final String src, final String[] posSigns){
		if(StringUtils.isEmpty(src)){
			return "";
		}
		if(posSigns == null || posSigns.length == 0){
			return src;
		}
		Object[] objs = new Object[posSigns.length];
		System.arraycopy(posSigns, 0, objs, 0, posSigns.length);
		
		return MessageFormat.format(src, objs);
	}
	/**
	 * 字符串转化为int,失败的话返回默认值
	 * @param str
	 * @param defaultValue
	 * @return
	 */
	public static int parseInt(final String str, final int defaultValue){
		if(!isSignedNumeric(str)){
			return defaultValue;
		}
		int intValue = defaultValue;
		try{
			intValue = Integer.parseInt(str.trim());
		}catch(Exception e){
			e.printStackTrace();
		}
		return intValue;
	}
	/**
	 * 字符串转化为Long,失败的话返回默认值
	 * @param str
	 * @param defaultValue
	 * @return
	 */
	public static long parseLong(final String str, final long defaultValue){
		if(!isSignedNumeric(str)){
			return defaultValue;
		}
		long longValue = defaultValue;
		try{
			longValue = Long.parseLong(str.trim());
		}catch(Exception e){
			e.printStackTrace();
		}
		return longValue;
	}
	/**
	 * 字符串转化为Long,失败的话返回默认值
	 * @param str
	 * @param defaultValue
	 * @return
	 */
	public static byte parseByte(final String str, final byte defaultValue){
		if(!isSignedNumeric(str)){
			return defaultValue;
		}
		byte byteValue = defaultValue;
		try{
			byteValue = Byte.parseByte(str.trim());
		}catch(Exception e){
			e.printStackTrace();
		}
		return byteValue;
	}
	/**
     * parse query string to Parameters.
     * 
     * @param qs query string.
     * @return Parameters instance.
     */
	public static Map<String, String> parseQueryString(String qs) {
	    if( qs == null || qs.length() == 0 )
            return new HashMap<String, String>();
	    return parseKeyValuePair(qs, "\\&");
	}
	
	/**
	 * 主要用来在解析json时遇到的"null"串
	 * @param src
	 * @param defaultString
	 * @return
	 */
	public static String parserString(String src, String defaultString){
		if(src == null){
			return defaultString;
		}
		src = src.trim();
		if(src.equalsIgnoreCase("null")){
			return defaultString;
		}
		return src;
	}
	/**
	 * parse key-value pair.
	 * 
	 * @param str string.
	 * @param itemSeparator item separator.
	 * @return key-value map;
	 */
	private static Map<String, String> parseKeyValuePair(String str, String itemSeparator) {
		String[] tmp = str.split(itemSeparator);
		Map<String, String> map = new HashMap<String, String>(tmp.length);
		for(int i=0;i<tmp.length;i++)
		{
			Matcher matcher = KVP_PATTERN.matcher(tmp[i]);
			if( matcher.matches() == false )
				continue;
			map.put(matcher.group(1), matcher.group(2));
		}
		return map;
	}
	/**
	 * 把数字num拼成长度是length的字符串,
	 * 长度不足时在前面补filler
	 * 超长时返回num整串
	 * 如果num小于0,负号变为1
	 * 
	 * @param num
	 * @param length
	 * @param filler
	 * @return
	 */
	public static String makeStringFromNumber(int num, int length,  char filler){
		StringBuilder sb = new StringBuilder();
		if(num < 0){
			sb.append("1");
			sb.append(0-num);
		}else{
			sb.append(num);
		}
		String tmpString = sb.toString();
		if(length <= 0 || tmpString.length() >= length){
			return tmpString;
		}
		StringBuilder sb1 = new StringBuilder();
		for(int i = 0; i < length - tmpString.length(); i ++){
			sb1.append(filler);
		}
		sb1.append(tmpString);
		
		
		return sb1.toString();
	}
	
	/**
	 * 检查是否合法用户名
	 * @param loginName
	 * @return
	 */
	public static boolean checkLoginName(final String loginName){
		if(loginName == null){
			return false;
		}
		
		return loginName.matches("^[a-zA-Z]\\w{5,24}$"); // _a-zA-Z0-9,6至25长度,第一位必须是字母
	}
	/**
	 * 检查是否合法手机号
	 * @param loginName
	 * @return
	 */
	public static boolean checkMobile(final String mobile){
		if(mobile == null){
			return false;
		}
		return mobile.matches("^1\\d{10}$");
	}
	/**
	 * 检查是否合法邮箱地址
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(final String email){
		if(email == null){
			return false;
		}
		
		return email.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
	}
}
