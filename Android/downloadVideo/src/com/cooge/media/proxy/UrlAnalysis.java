package com.cooge.media.proxy;

import org.apache.http.message.BasicHeader;


public class UrlAnalysis {
	
	 public enum TYPE {
	        FIEL, HTML, M3U8;
	    }
	
	private String enUrl;
	
	private TYPE type =TYPE.FIEL;
	
	
	public UrlAnalysis(String host,String url){
		String encoede = Base164.encodeToString(url);
		String eurl = "http://"+host+"/"+encoede;
		this.enUrl = eurl;
	}
	
	
	
	public UrlAnalysis(String url,BasicHeader... headers){
		String encoede = Base164.encodeToString(url);
		String eurl = "http://127.0.0.1:"+LiveProxy.port+"/"+encoede;
		
		if(headers.length>0){
			eurl = NetUtil.AdditionHeader(eurl, headers);
		}
		this.enUrl = eurl;
	}

	public String getEnUrl() {
		return enUrl;
	}


	public TYPE getType() {
		return type;
	}

	public void setType(TYPE type) {
		this.type = type;
	}
	
	

	public static void main(String[] args) {
		
		//User-Agent: SuperNode Downloader
		
		String url1="http://vodpcjq-cnc.wasu.cn/201603011032/d7db9e79282908a29430ea67900f7262/pcsan09/mams/vod/201509/11/10/201509111026018262bea09bb_98dd4bd5.mp4?vid=6514597&cid=8&version=PadPlayer_V1.4.0";
		
		BasicHeader bh = new BasicHeader("User-Agent", "wasutv_player");
		UrlAnalysis url = new UrlAnalysis(url1,bh);
		System.out.println(url.getEnUrl());
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
