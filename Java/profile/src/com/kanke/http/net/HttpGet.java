package com.kanke.http.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HttpGet {

	private String link;

	private Header[] inheads;
	
	private Header[] outheads;
	
	private String meaasge;
	

	public String getMeaasge() {
		return meaasge;
	}

	public void setMeaasge(String meaasge) {
		this.meaasge = meaasge;
	}

	public Header[] getOutheads() {
		return outheads;
	}

	public void setOutheads(Header[] outheads) {
		this.outheads = outheads;
	}

	private HttpURLConnection httpURLConnection;

	public HttpURLConnection getHttpURLConnection() {
		return httpURLConnection;
	}

	public void setHttpURLConnection(HttpURLConnection httpURLConnection) {
		this.httpURLConnection = httpURLConnection;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}


	public Header[] getInheads() {
		return inheads;
	}

	public void setInheads(Header[] inheads) {
		this.inheads = inheads;
	}


	public HttpGet(String link, Header... hds) {

		this.link = link;
		this.setInheads(hds);
	}

	public InputStream getInputStream() throws IOException {
		URL u = new URL(link);
		System.out.println("wwww=="+link);
		this.httpURLConnection = (HttpURLConnection) u.openConnection();
		this.httpURLConnection.setRequestMethod("GET");

		if (this.inheads != null && this.inheads.length > 0) {
			for (Header hd : this.inheads) {
				this.httpURLConnection.setRequestProperty(hd.getKey(),hd.getValue());
			}
		}
		this.httpURLConnection.connect();
		InputStream ips = this.httpURLConnection.getInputStream();
		Map<String, List<String>> map = this.httpURLConnection.getHeaderFields();
		
		Set<String> kset = map.keySet();
		
		Iterator<String> iterator = kset.iterator();
		List<Header> hlist = new ArrayList<Header>();
		while(iterator.hasNext()){
			String key = iterator.next();
			
			List<String> vlist = map.get(key);
			if(key==null){
				this.setMeaasge(vlist.get(0));
				continue;
			}
			for(String v:vlist){
				Header hd  = new Header(key, v);
				hlist.add(hd);
			}
		}
		Header[] hs = hlist.toArray(new Header[]{});
		this.setOutheads(hs);
		return ips;
	}

	public void close() {
		if(this.httpURLConnection!=null){
			this.httpURLConnection.disconnect();
		}
	}

}
