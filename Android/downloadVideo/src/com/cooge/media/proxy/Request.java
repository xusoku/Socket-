package com.cooge.media.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.message.BasicHeader;

public class Request {
	
	public static String PATH = "path=";
	
	public static String  HEADER = "&httpheader=";
	
	public String host;

	private String url;
	
	private BasicHeader[] headers;
	
	public boolean isLocal = true;

	public BasicHeader[] getHeaders() {
		return headers;
	}
	public void setHeaders(BasicHeader[] headers) {
		this.headers = headers;
	}
	
	private BasicHeader[] AdditionHeader;

	public BasicHeader[] getAdditionHeader() {
		return AdditionHeader;
	}
	public void setAdditionHeader(BasicHeader[] additionHeader) {
		AdditionHeader = additionHeader;
	}
	Request(InputStream input) {
		InputStreamReader ins = new InputStreamReader(input);
		BufferedReader br = new BufferedReader(ins);
		String line = null;
		
		List<BasicHeader> hlist = new ArrayList<BasicHeader>();
		HashMap<String,String> dd = new HashMap<String,String>(); 
		
		try {
			while ((line = br.readLine()) != null) {
				if (line.indexOf("GET") != -1 || line.indexOf("get") != -1) {
					String[] firstheader = line.split(" ");
					String u = firstheader[1];
					this.url = u.substring(u.indexOf("/") + 1);
					if(!url.contains(Request.PATH)){
						isLocal = false;
						int index = url.indexOf(HEADER);
						if(index>0){
							String furl = url;
							url = url.substring(0, index);
							String header = furl.substring(furl.indexOf(HEADER)+HEADER.length());
							String[] ss = Base164.decodeToString(header).split("&");
							List<BasicHeader> addlist = new ArrayList<BasicHeader>();
							for(String s:ss){
								String[] ms = s.split("=");
								String name = ms[0].toLowerCase();
								String value = ms[1];
								dd.put(name, value);
								BasicHeader bh = new BasicHeader(ms[0],ms[1]);
								hlist.add(bh);
								addlist.add(bh);
							}
							this.setAdditionHeader(addlist.toArray(new BasicHeader[]{}));
						}
						
						this.url = Base164.decodeToString(url);
					}
					continue;
				}
				int md = line.indexOf(":");
				if(md<1){
					this.setHeaders(hlist.toArray(new BasicHeader[]{}));
					break;
				}
				String name = line.substring(0, md);
				String value = line.substring(md+2);
				String v1 = dd.get(name.toLowerCase());
				if(v1!=null&&v1.length()>0){
					continue;
				}
				if(name.toLowerCase().equals("host")){
					host = line.substring(md+2);
					continue;
				}
				BasicHeader bh = new BasicHeader(name,value);
				hlist.add(bh);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public static void main(String[] args) {
		System.out.println(Base164.decodeToString(Base164.encodeToString("123")));
	}
}
