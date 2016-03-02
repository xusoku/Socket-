package com.kanke.http.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.kanke.http.net.Header;

public class NetUtil {
	
	public static String get(String link,Header... hds){
		HttpURLConnection huc = null;
		try {
			URL u =new URL(link);
			huc =  (HttpURLConnection) u.openConnection();
			huc.setRequestMethod("GET");
			
			if(hds!=null&&hds.length>0){
				for(Header hd:hds){
					huc.setRequestProperty(hd.getKey(),hd.getValue());
				}
			}
			
			huc.connect();
			InputStream ips = huc.getInputStream();
			InputStreamReader isr = new InputStreamReader(ips,"UTF-8");
			BufferedReader br = new BufferedReader(isr);
			StringBuffer sb = new StringBuffer();
			while(true){
				String str = br.readLine();
				if(str==null){
					break;
				}
				sb.append(str+"\r\n");
			}
			huc.disconnect();
			return sb.toString();
		} catch (Exception e) {
			return null;
		}finally{
			if(huc!=null){
				huc.disconnect();
			}
		}
	}
	

	
	
	
	
	
	
	
	
	public static String get(String link) {
		Header hd []={};
		return get(link,hd);
	}

public static String getCompileValue(String Html,String compile){
		
		Pattern pattern = Pattern.compile(compile);
		Matcher matcher = pattern.matcher(Html);
		
		if(matcher.find()){
			  return (matcher.group(1));
			}
		return null;
	}

}
