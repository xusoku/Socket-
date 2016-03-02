package com.cooge.media.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ProxyRubble implements Runnable {

	private Socket mSocket;
	
	public ProxyRubble(Socket mSocket){
		this.mSocket = mSocket;
	}
	
	@Override
	public void run() {
		try {
			InputStream inp = this.mSocket.getInputStream();
			Request req = new Request(inp);
			if(req.isLocal){
				new LocationResponse(req, this.mSocket.getOutputStream());
			}else{
				new Response(req, this.mSocket.getOutputStream());
			}
			this.mSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
