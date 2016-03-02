package com.kanke.http;

import java.util.ArrayList;
import java.util.List;

import com.kanke.http.net.Header;
import com.kanke.http.util.Base64;

public class DealUrl {
	/**
	 * 链接生成规则 http://127.0.0.1:6363/u/base64(url)/h/base64(header)
	 * @param args
	 */

	private String rootUrl="http://127.0.0.1";
	
	private int port = 6363;
	
	
	private String gurl; 
	
	private String url;
	
	private Header[] headers;
	
	
	public String getRootUrl() {
		return rootUrl;
	}

	public void setRootUrl(String rootUrl) {
		this.rootUrl = rootUrl;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getGurl() {
		return gurl;
	}

	public void setGurl(String gurl) {
		this.gurl = gurl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}



	public Header[] getHeaders() {
		return headers;
	}

	public void setHeaders(Header[] headers) {
		this.headers = headers;
	}
	

	public  void analysisUrl(String gurl,Header... hds) {
		/**
		 * 
		String gurl = "http://127.0.0.1:6363/u/aHR0cDovL3d3dy5iYWlkdS5jb20=/h/YTpi/h/ZDpj";
		DealUrl du = new DealUrl();
		du.analysisUrl(gurl);
		System.out.println(du.getUrl());
		 */
		this.setGurl(gurl);
		int start = gurl.indexOf("u/")+2;
		int end = gurl.indexOf("/",start);
		if(end>0){
			List<Header> hlist = new ArrayList<Header>();
			if(hds!=null&&hds.length>0){
				
				for(Header h:hds){
					hlist.add(h);
				}
				
			}
			String url =Base64.decodeToString(gurl.substring(start, end));
			this.setUrl(url);
			while(true){
				boolean fa = false;
				start = gurl.indexOf("h/", end)+2;
				end = gurl.indexOf("/", start);
				if(end<1){
					end = gurl.length();
					fa = true;
				}
				String header = Base64.decodeToString(gurl.substring(start, end));
				System.out.println(header);
				int  hs = header.indexOf(":");
				if(hs==-1){
					return;
				}
				String v = header.substring(hs+1);
				String k = header.substring(0, hs);
				Header hh = new Header(k, v);
				hlist.add(hh);
				if(fa){
					break;
				}
			}
			Header[] hs = hlist.toArray(new Header[]{});
			this.setHeaders(hs);
		}else{
			String url = Base64.decodeToString(gurl.substring(start));
			this.setUrl(url);
		}
		
		
	}
	
	public  String generateUrl(String url,Header ...h){
		/**
		String url = "http://www.baidu.com";
		Header h1 = new Header("a", "b");
		Header h2 = new Header("d", "c");
		System.out.println(generateUrl(url,h1,h2));
		**/
		String gurl = this.getRootUrl()+":" + this.getPort() + "/";
		gurl = gurl + "u/" + Base64.encodeToString(url);
		if(h==null||h.length==0){
			return gurl; 
		}
		for (int i = 0; i < h.length; i++) {
			gurl = gurl+"/h/"+Base64.encodeToString(h[i].getKey()+":"+h[i].getValue());
		}
		return gurl;
	}
	
	public static void main(String[] args) {
		/**
		 * 地址生成
		 */
		String url = "http://cache.m.iqiyi.com/dc/dt/-0-f45bc84a7ea643209b29a72b0c1e385f/text/c0f2ab25x11111111x777a6171/20151223/a4/2f/afefaa1152288dce8897481f13217284.m3u8";
		System.out.println(new DealUrl().generateUrl(url));
	}
	
	public String getProfile(String url,Header ...headers){
		return new DealUrl().generateUrl(url,headers);
	}
}
