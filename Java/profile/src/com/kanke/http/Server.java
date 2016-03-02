package com.kanke.http;

import com.kanke.http.net.Header;
import com.kanke.http.net.HttpServer;

public class Server {
	/**
	 * Æô¶¯µã
	 * @param args
	 */
	public static void main(String[] args) {
		
		Header header = new Header("User-Agent", "wasutv_player");
		String url="http://vodpcjq-cnc.wasu.cn/pcsan09/mams/vod/201509/15/10/20150915102808631a466ec6a_1b6c2256.mp4";
		String url1=new DealUrl().generateUrl(url,header );
		System.out.println(url1);
		 HttpServer.start();
		 
	}

}
