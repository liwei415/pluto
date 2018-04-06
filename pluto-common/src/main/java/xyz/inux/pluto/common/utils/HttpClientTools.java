package xyz.inux.pluto.common.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.AbstractContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.springframework.util.Assert;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClientTools {
	/** 请求服务器返回的Http状态码 */
	private int iGetResultCode;
	/** 请求服务器返回的Http回应内容 */
	private String strGetResponseBody;
	/** 执行方法后返回的错误提示 */
	private String errorInfo;
	/** 文件下载字节流 */
	private byte[] content = null;
	/** 文件数据流 */
	private InputStream inStream = null;
	/** 返回数据长度 */
	private long responseLength = 0L;
	
	private static final String U_A = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:37.0) Gecko/20100101 Firefox/37.0";
	
	/** HttpClient对象 */
	private final CloseableHttpClient httpClient;
	
	private boolean isHttps;
	private boolean useProxy;
	private String  proxyHost;
	private int     proxyPort;
	
	private int     connectionRequestTimeoutMills;
	private int     connectionTimeoutMills;
	private int     soTimeoutMills;
	
	/**
	 * 代理与协议设置
	 * @param isHttps ture-https,false-http协议
	 * @param useProxy true-启用代理,此时proxyHost和proxyPort必填,false-不启用代理
	 * @param proxyHost 代理服务器IP
	 * @param proxyPort 代理服务器端口号
	 */
	private HttpClientTools(int connectionRequestTimeoutMills, 
			               int connectionTimeoutMills, 
			               int soTimeoutMills,
			               boolean isHttps,
			               boolean useProxy,
			               String proxyHost,
			               int proxyPort) {
		RequestConfig requestConfig = RequestConfig.custom()  
				.setConnectionRequestTimeout(connectionRequestTimeoutMills)
				.setConnectTimeout(connectionTimeoutMills)
				.setSocketTimeout(soTimeoutMills)
				.build();
		
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create()
			    .disableAutomaticRetries()
			    .setMaxConnPerRoute(20)
			    .setMaxConnTotal(200)
			    .setDefaultRequestConfig(requestConfig)
			    .setUserAgent(U_A);
		if(isHttps){
			httpClientBuilder.setSSLSocketFactory(createSSLConnSocketFactory());
		}
		if(useProxy){
			Assert.hasText(proxyHost, "代理地址空");
			Assert.state(proxyPort > 0, "代理端口空");
			httpClientBuilder.setProxy(new HttpHost(proxyHost, proxyPort));
		}
		httpClient = httpClientBuilder.build();
	}
	
	public static HttpClientTools.HttpClientToolsBuilder custom(){
		return new HttpClientToolsBuilder();
	}
	
	public static class HttpClientToolsBuilder {
		private boolean isHttps;
		private boolean useProxy;
		private String  proxyHost;
		private int     proxyPort;
		
		private int     connectionRequestTimeoutMills;
		private int     connectionTimeoutMills;
		private int     soTimeoutMills;
		
		HttpClientToolsBuilder() {
            super();
            
            isHttps = false;
            useProxy = false;
            proxyHost = null;
            proxyPort = 0;
            
            connectionRequestTimeoutMills = 60000;
            connectionTimeoutMills = 60000;
            soTimeoutMills = 60000;
		}
		
		public HttpClientToolsBuilder setHttps(final boolean isHttps) {
			this.isHttps = isHttps;
			
			return this;
		}

		public HttpClientToolsBuilder setUseProxy(final boolean useProxy) {
			this.useProxy = useProxy;
			
			return this;
		}

		public HttpClientToolsBuilder setProxyHost(final String proxyHost) {
			this.proxyHost = proxyHost;
			
			return this;
		}

		public HttpClientToolsBuilder setProxyPort(final int proxyPort) {
			this.proxyPort = proxyPort;
			
			return this;
		}

		public HttpClientToolsBuilder setConnectionRequestTimeoutMills(final int connectionRequestTimeoutMills) {
			this.connectionRequestTimeoutMills = connectionRequestTimeoutMills;
			
			return this;
		}

		public HttpClientToolsBuilder setConnectionTimeoutMills(final int connectionTimeoutMills) {
			this.connectionTimeoutMills = connectionTimeoutMills;
			
			return this;
		}

		public HttpClientToolsBuilder setSoTimeoutMills(final int soTimeoutMills) {
			this.soTimeoutMills = soTimeoutMills;
			
			return this;
		}
		
		public HttpClientTools builder(){
			return new HttpClientTools(
					connectionRequestTimeoutMills, 
		            connectionTimeoutMills, 
		            soTimeoutMills,
		            isHttps,
		            useProxy,
		            proxyHost,
		            proxyPort);
		}
	}
	
	public boolean isHttps() {
		return isHttps;
	}
	public boolean isUseProxy() {
		return useProxy;
	}
	public String getProxyHost() {
		return proxyHost;
	}
	public int getProxyPort() {
		return proxyPort;
	}
	public int getConnectionRequestTimeoutMills() {
		return connectionRequestTimeoutMills;
	}
	public int getConnectionTimeoutMills() {
		return connectionTimeoutMills;
	}
	public int getSoTimeoutMills() {
		return soTimeoutMills;
	}
	/**
	 * 根据给定的URL地址和参数字符串，以Get方法调用，如果成功返回true，如果失败返回false
	 * @param url String url地址，可以带参数
	 * @param param String 参数字符串，例如：a=1&b=2&c=3
	 * @return boolean true－成功，false失败，如果返回成功可以getStrGetResponseBody()
	 * 获取返回内容字符串，如果失败，则可访问getErrorInfo()获取错误提示。
	 */
	public boolean executeGetMethod(String url, String param, String charset) {
		if (url == null || url.length() <= 0) {
			errorInfo = "无效url地址";
			return false;
		}
		StringBuffer serverURL = new StringBuffer(url);
		if (param != null && param.length() > 0) {
			if(serverURL.indexOf("?") > -1){
				serverURL.append("&");
			}else{
				serverURL.append("?");
			}
			serverURL.append(param);
		}
		HttpGet httpget = new HttpGet(serverURL.toString());
		try {
			CloseableHttpResponse response = null;
			try{
				response = httpClient.execute(httpget);
				iGetResultCode = response.getStatusLine().getStatusCode();
				HttpEntity httpEntity = response.getEntity();
				responseLength = httpEntity.getContentLength();
				strGetResponseBody = EntityUtils.toString(httpEntity, charset);
				EntityUtils.consume(httpEntity);
			}finally{
				if(response != null){
					response.close();
				}
			}
			if (iGetResultCode >= 200 && iGetResultCode < 303) {
				return true;
			} else if (iGetResultCode >= 400 && iGetResultCode < 500) {
				httpget.abort();
				errorInfo = "请求的目标地址不存在：" + iGetResultCode;
			} else {
				httpget.abort();
				errorInfo = "请求错误：" + iGetResultCode;
			}
		} catch (Exception ex) {
			errorInfo = ex.getMessage();
		} finally {
			if(httpget != null){
				httpget.releaseConnection();
			}
		}
		return false;
	}
	/**
	 * 根据给定的URL地址和参数字符串，以Get方法调用,下载文件，如果成功返回true，如果失败返回false
	 * @param url String url地址，可以带参数
	 * @param param String 参数字符串，例如：a=1&b=2&c=3
	 * @param charset 编码
	 * @param resultType 返回结果类型 0-byte[];1-inputStream
	 * @return boolean true－成功，false失败，如果返回成功可以getStreamGetResponseBody()
	 * 获取返回内容字节流，如果失败，则可访问getErrorInfo()获取错误提示。
	 */
	public boolean executeGetFileDownload(String url, String param, String charset, int resultType) {
		if (url == null || url.length() <= 0) {
			errorInfo = "无效url地址";
			return false;
		}
		StringBuffer serverURL = new StringBuffer(url);
		if (param != null && param.length() > 0) {
			if(serverURL.indexOf("?") > -1){
				serverURL.append("&");
			}else{
				serverURL.append("?");
			}
			serverURL.append(param);
		}
		HttpGet httpget = new HttpGet(serverURL.toString());
		try {
			CloseableHttpResponse response = null;
			try{
				response = httpClient.execute(httpget);
				iGetResultCode = response.getStatusLine().getStatusCode();
				HttpEntity httpEntity = response.getEntity();
				responseLength = httpEntity.getContentLength();
				if(resultType == 1){
					inStream = httpEntity.getContent();
				}else{
					content = EntityUtils.toByteArray(httpEntity);
				}
				EntityUtils.consume(httpEntity);
			}finally{
				if(response != null){
					response.close();
				}
			}
			if (iGetResultCode >= 200 && iGetResultCode < 303) {
				return true;
			} else if (iGetResultCode >= 400 && iGetResultCode < 500) {
				httpget.abort();
				errorInfo = "请求的目标地址不存在：" + iGetResultCode;
			} else {
				httpget.abort();
				errorInfo = "请求错误：" + iGetResultCode;
			}
		} catch (Exception ex) {
			errorInfo = ex.getMessage();
		} finally {
			if(httpget != null){
				httpget.releaseConnection();
			}
		}
		return false;
	}
	public boolean executeGetFileDownload(String url, String param, String charset) {
		return executeGetFileDownload(url, param, charset, 0);
	}
	/**
	 * 关闭
	 */
	public void closeHttpClient(){
		try {
			httpClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the content
	 */
	public byte[] getContent() {
		return content;
	}
	/**
	 * @return the inStream
	 */
	public InputStream getInStream() {
		return inStream;
	}
	/**
	 * @return the responseLength
	 */
	public long getResponseLength() {
		return responseLength;
	}
	/**
	 * 文件上传接口
	 * @param strURL
	 * @param params
	 * @param charset
	 * @return
	 */
	public boolean executeFileUploadMethod(String strURL, Map<String,Object> params, String charset) {
		if (strURL == null || strURL.length() <= 0) {
			errorInfo = "无效url地址";
			return false;
		}
		MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create()
				.setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
				.setCharset(Charset.forName(charset));
		if(params != null && !params.isEmpty()){
			for(String key : params.keySet()){
				Object obj = params.get(key);
				AbstractContentBody contentBody = null;
				
				if(obj != null && (obj instanceof File)){
					File tmpFile = (File)obj;
					//文件上传设置为二进制
					ContentType fileContentType = ContentType.APPLICATION_OCTET_STREAM.withCharset(charset);
					contentBody = new FileBody(tmpFile, fileContentType, tmpFile.getName());
				}else{
					String tmpString = "";
					if(obj != null){
						tmpString = obj.toString();
					}
					//字符串设置为文本属性
					contentBody = new StringBody(tmpString, ContentType.TEXT_PLAIN.withCharset(charset));
				}
				multipartEntityBuilder.addPart(key, contentBody);
			}
		}
		HttpEntity httpEntity = multipartEntityBuilder.build();
		HttpPost httpPost = new HttpPost(strURL);
		try {
			httpPost.setEntity(httpEntity);
			
			CloseableHttpResponse response = null;
			try{
				response = httpClient.execute(httpPost);
				iGetResultCode = response.getStatusLine().getStatusCode();
				HttpEntity entity = response.getEntity();
				strGetResponseBody = EntityUtils.toString(entity, charset);
				EntityUtils.consume(entity);
			}finally{
				if(response != null){
					response.close();
				}
			}
			
			if (iGetResultCode >= 200 && iGetResultCode < 303) {
				return true;
			} else if (iGetResultCode >= 400 && iGetResultCode < 500) {
				httpPost.abort();
				errorInfo = "请求的目标地址不存在：" + iGetResultCode;
			} else {
				httpPost.abort();
				errorInfo = "请求错误：" + iGetResultCode;
			}
		} catch (Exception ex) {
			errorInfo = ex.getMessage();
		} finally {
			try {
				EntityUtils.consume(httpEntity);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(httpPost != null){
				httpPost.releaseConnection();
			}
		}
		return false;
	}
	/**
	 * 表单数据提交
	 * @param strURL
	 * @param params
	 * @param charset
	 * @return
	 */
	public boolean executePostFormDataMethod(String strURL, Map<String,String> params, String charset) {
		if (strURL == null || strURL.length() <= 0) {
			errorInfo = "无效url地址";
			return false;
		}
		UrlEncodedFormEntity entity = null;
		if(params != null && !params.isEmpty()){
			List<BasicNameValuePair> formParams = new ArrayList<BasicNameValuePair>();
			for(String key : params.keySet()){
				formParams.add(new BasicNameValuePair(key, params.get(key)));
			}
			entity = new UrlEncodedFormEntity(formParams, Charset.forName(charset));
		}
		HttpPost httpPost = new HttpPost(strURL);
		try {
			if(entity != null){
				httpPost.setEntity(entity);
			}
			CloseableHttpResponse response = null;
			try{
				response = httpClient.execute(httpPost);
				iGetResultCode = response.getStatusLine().getStatusCode();
				HttpEntity httpEntity = response.getEntity();
				strGetResponseBody = EntityUtils.toString(httpEntity, charset);
				EntityUtils.consume(httpEntity);
			}finally{
				if(httpPost != null){
					httpPost.releaseConnection();
				}
			}
			
			if (iGetResultCode >= 200 && iGetResultCode < 303) {
				return true;
			} else if (iGetResultCode >= 400 && iGetResultCode < 500) {
				httpPost.abort();
				errorInfo = "请求的目标地址不存在：" + iGetResultCode;
			} else {
				httpPost.abort();
				errorInfo = "请求错误：" + iGetResultCode;
			}
		} catch (Exception ex) {
			errorInfo = ex.getMessage();
		} finally {
			try {
				EntityUtils.consume(entity);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(httpPost != null){
				httpPost.releaseConnection();
			}
		}
		return false;
	}
	
	/**
	 * 表单数据提交(PUT)
	 * @param strURL
	 * @param params
	 * @param charset
	 * @return
	 */
	public boolean executePutMethod(String strURL, Map<String,String> params, String charset){
		if (strURL == null || strURL.length() <= 0) {
			errorInfo = "无效url地址";
			return false;
		}
		UrlEncodedFormEntity entity = null;
		if(params != null && !params.isEmpty()){
			List<BasicNameValuePair> formParams = new ArrayList<BasicNameValuePair>();
			for(String key : params.keySet()){
				formParams.add(new BasicNameValuePair(key, params.get(key)));
			}
			entity = new UrlEncodedFormEntity(formParams, Charset.forName(charset));
		}
		
		HttpPut httpPut = null;
		CloseableHttpResponse response = null;
		HttpEntity httpEntity = null;
		
		try {
			httpPut = new HttpPut(strURL);
			if(entity != null){
				httpPut.setEntity(entity);
			}
			
			response = httpClient.execute(httpPut);
			iGetResultCode = response.getStatusLine().getStatusCode();
			httpEntity = response.getEntity();
			strGetResponseBody = EntityUtils.toString(httpEntity, charset);
			
			if (iGetResultCode >= 200 && iGetResultCode < 303) {
				return true;
			} else if (iGetResultCode >= 400 && iGetResultCode < 500) {
				httpPut.abort();
				errorInfo = "请求的目标地址不存在：" + iGetResultCode;
			} else {
				httpPut.abort();
				errorInfo = "请求错误：" + iGetResultCode;
			}
		} catch (Exception ex) {
			errorInfo = ex.getMessage();
		} finally {
			try {
				EntityUtils.consume(httpEntity);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				EntityUtils.consume(entity);
			} catch (IOException e) {
				e.printStackTrace();
			}
			HttpClientUtils.closeQuietly(response);
			if(httpPut != null){
				httpPut.releaseConnection();
			}
		}
		return false;
	}
	/**
	 * 表单数据删除(DELETE)
	 * @param strURL
	 * @param charset
	 * @return
	 */
	public boolean executeDeleteMethod(String strURL, String charset){
		if (strURL == null || strURL.length() <= 0) {
			errorInfo = "无效url地址";
			return false;
		}
		
		CloseableHttpResponse response = null;
		HttpDelete httpDelete = null;
		HttpEntity httpEntity = null;
		
		try {
			httpDelete = new HttpDelete(strURL);
			
			response = httpClient.execute(httpDelete);
			iGetResultCode = response.getStatusLine().getStatusCode();
			httpEntity = response.getEntity();
			strGetResponseBody = EntityUtils.toString(httpEntity, charset);
			
			if (iGetResultCode >= 200 && iGetResultCode < 303) {
				return true;
			} else if (iGetResultCode >= 400 && iGetResultCode < 500) {
				httpDelete.abort();
				errorInfo = "请求的目标地址不存在：" + iGetResultCode;
			} else {
				httpDelete.abort();
				errorInfo = "请求错误：" + iGetResultCode;
			}
		} catch (Exception ex) {
			errorInfo = ex.getMessage();
		} finally {
			try {
				EntityUtils.consume(httpEntity);
			} catch (IOException e) {
				e.printStackTrace();
			}
			HttpClientUtils.closeQuietly(response);
			if(httpDelete != null){
				httpDelete.releaseConnection();
			}
		}
		return false;
	}
	
	private static SSLConnectionSocketFactory createSSLConnSocketFactory() {  
		SSLConnectionSocketFactory sslsf = null;  
		try {
			SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {  
					return true;
				}
			}).build();
			sslsf = new SSLConnectionSocketFactory(
					sslContext,
	                new String[] { "TLSv1" },
	                null,
	                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
		} catch (GeneralSecurityException e) {
			e.printStackTrace();  
		}
		return sslsf;  
	}
	
	public int getiGetResultCode() {
		return iGetResultCode;
	}

	public String getStrGetResponseBody() {
		return strGetResponseBody;
	}

	public String getErrorInfo() {
		return errorInfo;
	}
	
	public static void main(String[] args) {
		String httpsUrl = "https://www.amazon.cn/b/ref=s9_acss_bw_fb_78082812_b2?_encoding=UTF8&node=117198071&pf_rd_m=A1AJ19PSB66TGU&pf_rd_s=merchandised-search-3&pf_rd_r=9FF1E15YZRWW3YR67D16&pf_rd_r=9FF1E15YZRWW3YR67D16&pf_rd_t=101&pf_rd_p=855ed8bf-a7e4-46a6-a8e4-e815f9a5f800&pf_rd_p=855ed8bf-a7e4-46a6-a8e4-e815f9a5f800&pf_rd_i=755653051";
		String proxyHost = "10.199.75.12";
		int proxyPort = 8080;
		
		HttpClientTools httpTools = HttpClientTools
				.custom()
				.setHttps(true)
				.setUseProxy(true)
				.setProxyHost(proxyHost)
				.setProxyPort(proxyPort)
				.builder();
		boolean b = httpTools.executeGetMethod(httpsUrl, null, "utf-8");
		if(!b){
			System.out.println(httpTools.getErrorInfo());
		}else{
			String result = httpTools.getStrGetResponseBody();
			System.out.println(result);
		}
	}
}
