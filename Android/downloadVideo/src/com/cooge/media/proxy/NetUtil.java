package com.cooge.media.proxy;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NetUtil {
	public static String getHtml(String url) throws Exception {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		HttpResponse response = httpclient.execute(httpget);
		HttpEntity entity = response.getEntity();
		InputStream in = entity.getContent();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in,
				"UTF-8"));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\r\n");
		}
		return sb.toString();
	}

	public TYPE getType() {
		return type;
	}

	public void setType(TYPE type) {
		this.type = type;
	}

	public enum TYPE {
		FIEL, HTML, M3U8;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	private TYPE type = TYPE.FIEL;

	private String path;

	private Header[] headers;

	public Header[] getHeaders() {
		return headers;
	}

	public void setHeaders(Header[] headers) {
		this.headers = headers;
	}

	private String resultHear;

	public String getResultHear() {
		return resultHear;
	}

	public void setResultHear(String resultHear) {
		this.resultHear = resultHear;
	}

	public InputStream getInputStream(String url, Header... header)
			throws Exception {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		httpget.setHeaders(header);
		HttpResponse response = httpclient.execute(httpget);
		response.getFirstHeader("");
		Header[] hs = response.getHeaders("Content-Type");
		this.setResultHear("HTTP/1.1" + " "
				+ response.getStatusLine().getStatusCode() + " "
				+ response.getStatusLine().getReasonPhrase());
		this.setHeaders(response.getAllHeaders());
		if (hs.length > 0) {
			String Content_Type = hs[0].getValue();
			if (Content_Type.contains("text") || Content_Type.contains("html")) {
				this.setType(TYPE.HTML);
			}
		}
		this.setPath(NetUtil.getUrlPath(url));
		HttpEntity entity = response.getEntity();
		InputStream in = entity.getContent();
		return in;
	}

	public static String getHtml(InputStream in) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in,
				"UTF-8"));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\r\n");
		}
		return sb.toString();
	}

	public static String getHtml(String url, Header... header) throws Exception {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		httpget.setHeaders(header);
		HttpResponse response = httpclient.execute(httpget);
		HttpEntity entity = response.getEntity();
		InputStream in = entity.getContent();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in,
				"UTF-8"));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\r\n");
		}
		return sb.toString();
	}

	public static String postHtml(String url, Map<String, String> m)
			throws Exception {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		Iterator<Entry<String, String>> iterator = m.entrySet().iterator();
		while (iterator.hasNext()) {

			Entry<String, String> me = iterator.next();
			NameValuePair nameValuePair = new BasicNameValuePair(me.getKey(),
					me.getValue());
			list.add(nameValuePair);
		}
		HttpEntity requestenEntity = new UrlEncodedFormEntity(list);
		httppost.setEntity(requestenEntity);
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();
		InputStream in = entity.getContent();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in,
				"UTF-8"));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\r\n");
		}
		// System.out.println(sb.toString());
		System.out.println(url);
		return sb.toString();
	}

	public static String format(String... values) {
		String str = values[0];
		for (int i = 1; i < values.length; i++) {
			str = str.replace("%s", values[i]);
		}
		return str;
	}

	public static String md5(String s) {

		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		char str[];
		byte strTemp[] = s.getBytes();

		MessageDigest mdTemp;
		try {
			mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte md[] = mdTemp.digest();
			int j = md.length;
			str = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String getUrlPath(String url) {
		int last = url.lastIndexOf("/") + 1;
		int last_ = url.indexOf("//") + 2;
		if (last > last_) {
			url = url.substring(0, url.lastIndexOf("/"));
		}
		return url;
	}

	public static String GetDownloadFile(String url, String savePath,BufferSync bufferSync) {
		String filename = md5(url);
		File file = new File(savePath, filename);
		// ///////////////已经存在则不下载/////////////////////////
		if (file.exists() && file.length() > 0) {
			String path = null;
			try {
				path = file.getCanonicalPath();
			} catch (IOException e) {
				e.printStackTrace();
			}
			bufferSync.finish(path);
			return path;
		}
		//////////////////////////////////
		HttpClient httpclient = new DefaultHttpClient();
		InputStream in = null;
		OutputStream os = null;
		try {
			url = url.replaceAll(" ", "%");

			HttpGet httpget = new HttpGet(url);
			HttpResponse response = httpclient.execute(httpget);
			bufferSync.downStatus(String.valueOf(response.getStatusLine().getStatusCode()));

			HttpEntity entity = response.getEntity();
			in = entity.getContent();
			os = new FileOutputStream(file);
			String path = file.getCanonicalPath();
			// //////////////////////////
			long tsize = in.available();
			if (tsize == 0) {
				tsize = entity.getContentLength();
			}
			int p = 0;
			int i = 0;

			File f = new File(savePath);
			if (!f.isDirectory()) {
				f.mkdirs();
			}
			byte b[] = new byte[1024];
			int len = 0;
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			while ((len = in.read(b, 0, b.length)) != -1) {
				if (tsize > 0) {
					p = p + b.length;
					i = (int) (p * 100 / tsize);
					bufferSync.downProcess(i);
				}
				bout.write(b, 0, len);
				if (bout.size() >= 1024) {
					os.write(bout.toByteArray());
					bout.reset();
				}
			}
			bufferSync.finish(path);
			os.flush();
			in.close();
			os.close();
			return path;
		} catch (Exception e) {
			e.printStackTrace();
			if (in != null)
				try {
					in.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			if (os != null)
				try {
					os.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			bufferSync.error();
		}
		return null;
	}

	public static void replaceEle(Elements eles, String attr, String Path) {
		java.util.Iterator<Element> iterator = eles.iterator();
		while (iterator.hasNext()) {
			Element ele = iterator.next();
			String href = ele.attr(attr);
			if (href.startsWith("#")) {
				continue;
			}
			if (href.contains("http")) {
				UrlAnalysis url_ = new UrlAnalysis(href);
				ele.attr(attr, url_.getEnUrl());
			} else {
				if (href.startsWith("/")) {
					UrlAnalysis url_ = new UrlAnalysis(Path + href);
					ele.attr(attr, url_.getEnUrl());
				} else {

					UrlAnalysis url_ = new UrlAnalysis(Path + "/" + href);
					ele.attr(attr, url_.getEnUrl());
				}
			}
		}
	}

	public static String replaceUrl(String path, String url) {
		if (url.contains("http")) {
		} else {
			if (url.startsWith("/")) {
				url = (path + url);
			} else {

				url = (path + "/" + url);
			}
		}

		UrlAnalysis url_ = new UrlAnalysis(url);
		return url_.getEnUrl();
	}

	public static String AdditionHeader(String url, Header... headers) {
		if (headers.length > 0) {
			StringBuffer ps = new StringBuffer();
			for (Header h : headers) {
				ps.append("&");
				ps.append(h.getName() + "=" + h.getValue());
			}
			String head = Base164.encodeToString(ps.substring(1).toString());
			url = url + Request.HEADER + head;
		}
		return url;
	}

	public static String replaceUrl(String host, String path, String url) {
		if (url.contains("http")) {
		} else {
			if (url.startsWith("/")) {
				url = (path + url);
			} else {

				url = (path + "/" + url);
			}
		}

		UrlAnalysis url_ = new UrlAnalysis(host, url);
		return url_.getEnUrl();
	}


}
