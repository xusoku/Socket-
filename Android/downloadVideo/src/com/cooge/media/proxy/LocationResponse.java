package com.cooge.media.proxy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Date;

import com.cooge.media.proxy.NetUtil.TYPE;

public class LocationResponse {
	private static int cache = 1024 * 1024;
	
	public LocationResponse(Request req, OutputStream out){
		
		try {
			String url  = req.getUrl();
			String path = url.substring(url.indexOf(Request.PATH)+Request.PATH.length());
			File fs = new File(Base164.decodeToString(path));
			InputStream myInput=new FileInputStream(fs); 
			byte[] buffer = new byte[cache];  
			int length = myInput.read(buffer);
			int start = 0;
			
			TYPE type = TYPE.FIEL;
			
			out.write("HTTP/1.1 200 OK\r\n".getBytes());
			out.write(("Date: "+new Date()+"\r\n").getBytes());
			
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			while(length > 0)
			{
				if(start==0){
					byte[] yy = new byte[7];
					System.arraycopy(buffer, 0, yy, 0, yy.length);
					String hear = new String(yy);
					if (hear.contains("EXTM3U")) {
						type = TYPE.M3U8;
					}else{
						OutputStreamWriter opw= new OutputStreamWriter(out,"utf-8");
						PrintWriter pw = new PrintWriter(opw, true);
						pw.println("Content-Type: application/octet-stream");
						pw.println("Connection: keep-alive");
						pw.println("Content-Length: "+fs.length());
						pw.println("Content-Disposition: filename="+new Date().getTime());
						pw.println();
					}
				}
				
				if(type == TYPE.M3U8){
					bout.write(buffer, 0, length);
				}else{
					out.write(buffer, 0, length); 
				}
				length = myInput.read(buffer);
				start = 1;
			}
			if (type == TYPE.M3U8) {
				OutputStreamWriter opw= new OutputStreamWriter(out,"utf-8");
				PrintWriter pw = new PrintWriter(opw, true);
				StringBuffer mu_ = new StringBuffer(bout.toString("utf-8"));
				pw.println("Content-Type: application/octet-stream");
				pw.println("Connection: keep-alive");
				pw.println("Content-Length: "+(mu_.toString().getBytes().length));
				pw.println("Content-Disposition: filename="+new Date().getTime()+".m3u8");
				pw.println();
				pw.println(mu_.toString());
				
			}
			out.flush();  
			myInput.close();  
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
