package com.cooge.media.proxy.type;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.Header;

import com.cooge.media.proxy.NetUtil;

public class FileType extends Type {


	public FileType(Header[] headers, OutputStream out, InputStream ins) {
		super(headers, out, ins);
	}

	@Override
	public void Deal(NetUtil  net) {
		
	}


}
