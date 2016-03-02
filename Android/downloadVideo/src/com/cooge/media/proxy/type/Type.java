package com.cooge.media.proxy.type;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.Header;

import com.cooge.media.proxy.NetUtil;

public abstract class Type {
	
	private Header[] headers;
	
	private OutputStream out;
	
	private InputStream ins;

	public Header[] getHeaders() {
		return headers;
	}

	public InputStream getIns() {
		return ins;
	}

	public void setIns(InputStream ins) {
		this.ins = ins;
	}

	public void setHeaders(Header[] headers) {
		this.headers = headers;
	}
	
	public Type(Header[] headers,OutputStream out,InputStream ins){
		this.setHeaders(headers);
		this.setOut(out);
		this.setIns(ins);
	}
	
	public OutputStream getOut() {
		return out;
	}

	public void setOut(OutputStream out) {
		this.out = out;
	}

	public abstract void Deal(NetUtil  net);

}
