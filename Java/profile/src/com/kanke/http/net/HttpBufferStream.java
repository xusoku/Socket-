package com.kanke.http.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class HttpBufferStream extends InputStream{

	private InputStream ips;
	
	private String queryUrl;
	
	private Header[] headers;
	
	

	public InputStream getIps() {
		return ips;
	}

	public Header[] getHeaders() {
		return headers;
	}

	public void setHeaders(Header[] headers) {
		this.headers = headers;
	}

	public String getQueryUrl() {
		return queryUrl;
	}

	public void setQueryUrl(String queryUrl) {
		this.queryUrl = queryUrl;
	}

	public HttpBufferStream(InputStream ips) {
		this.ips = ips;
		try {
			InputStreamReader isr = new InputStreamReader(ips,"UTF-8");
			BufferedReader br = new BufferedReader(isr);
			String qurl = br.readLine();
			if(qurl==null){
				return;
			}
			int start = qurl.indexOf(" ");
			int end = qurl.indexOf(" ", start+1);
			if(end<1){
				end = qurl.length();
			}
			String url = qurl.substring(start+1, end);
			
			this.setQueryUrl(url);
			List<Header> hlist = new ArrayList<Header>();
			while(true){
				String header = br.readLine();
				if(header==null||header.length()<1){
					break;
				}
				int  hs = header.indexOf(":");
				String v = header.substring(hs+2);
				String k = header.substring(0, hs);
				Header hh = new Header(k, v);
				hlist.add(hh);
			}
			Header[] hs = hlist.toArray(new Header[]{});
			this.setHeaders(hs);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int read() throws IOException {
		if(ips!=null){
			return ips.read();
		}
		return -1;
	}

}
