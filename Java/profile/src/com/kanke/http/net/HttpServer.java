package com.kanke.http.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;

import com.kanke.http.DealUrl;

public class HttpServer {

	private static int port = 6363;

	public static void start() {

		try {
			@SuppressWarnings("resource")
			ServerSocket ss = new ServerSocket(port);
			while (true) {
				new Thread(new ServerRunnable(ss.accept())).start();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static class ServerRunnable implements Runnable {

		private Socket socket;
		
		private InputStream ips;
		
		private OutputStream ops;

		public ServerRunnable(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			
			try {
				ips = socket.getInputStream();
				ops = socket.getOutputStream();
				HttpBufferStream hbs = new HttpBufferStream(ips);
				String queryurl = hbs.getQueryUrl();
				System.out.println("我错了222    "+queryurl  );
				if(queryurl==null){
					ops.write("error".getBytes());
				}else{
					DealUrl du = new DealUrl();
					du.analysisUrl(queryurl,hbs.getHeaders());
					
					String url = du.getUrl();
					System.out.println("我错了     "+url  );
					if(!url.startsWith("http")){
						System.out.println("我错了");
						ops.write("errorlink".getBytes());
					}else{
						
						HttpGet get = new HttpGet(url, du.getHeaders());
						try {
							
							InputStream input = get.getInputStream();
							
							ByteArrayOutputStream  baos = new ByteArrayOutputStream();
//							
							byte[] cache = new byte[512];
//						Transfer-Encoding
							ops.write(get.getMeaasge().getBytes());
							ops.write("\r\n".getBytes());
							for(Header h:get.getOutheads()){
								ops.write((h.getKey()+": "+h.getValue()).getBytes());
								ops.write("\r\n".getBytes());
							}
							HttpURLConnection huc = get.getHttpURLConnection();
							String te = huc.getHeaderField("Transfer-Encoding");
							boolean ch = false;
							if(te!=null&&te.equals("chunked")){
								ch = true;
							}else{
								ops.write("\r\n".getBytes());
								ops.flush();	
							}
							int i = 0;
							while(true){
								int len = input.read(cache);
								if(len>0){
									baos.write(cache, 0, len);
									if(baos.size()>=512){
										if(ch){
											if(i!=0){
												ops.write("\r\n".getBytes());
											}
											ops.write(Integer.toHexString(baos.size()).getBytes());
											ops.write("\r\n".getBytes());
										}
										ops.write(baos.toByteArray());
										ops.flush();
										baos.reset();
									}
								}else{
									if(baos.size()>0){
										if(ch){
											if(i!=0){
												ops.write("\r\n".getBytes());
											}
											ops.write(Integer.toHexString(baos.size()).getBytes());
											ops.write("\r\n".getBytes());
										}
										ops.write(baos.toByteArray());
										ops.flush();
										baos.reset();
									}
									break;
								}
								i = 1;
							}
							System.out.println("end");
						} catch (IOException e) {
							System.out.println("我错了1");
							e.printStackTrace();
							
						}finally{
							System.out.println("close");
							get.close();
						}
						
					}
					System.out.println(du.getUrl());
				}
				ops.flush();
				ops.close();
				hbs.close();
			} catch (IOException e) {
				close();
			}
		}
		
		public void close(){
			
			if(ips!=null){
				try {
					ips.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(ops!=null){
				try {
					
					ops.flush();
					ops.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(socket!=null){
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		}

	}

}
