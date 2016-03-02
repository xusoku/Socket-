package com.cooge.media.proxy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.apache.http.Header;

import com.cooge.media.proxy.NetUtil.TYPE;
import com.cooge.media.proxy.type.HtmlType;
import com.cooge.media.proxy.type.M3u8Type;

public class Response {

	private static int cache = 1024 * 128;

	public Response(Request req, OutputStream out) {

		System.out.println("当前访问的URL为:"+req.getUrl());
		
		try {
			String url  = req.getUrl();
			OutputStreamWriter opw = new OutputStreamWriter(out, "utf-8");
			NetUtil net = new NetUtil();
			InputStream ins = net.getInputStream(url, req.getHeaders());
			PrintWriter pw = new PrintWriter(opw, true);
			/////////////////////////////////
			pw.println(net.getResultHear());
			////////////////////////////////
			Header[] hs_ = net.getHeaders();
			if (net.getType() == TYPE.HTML) {
				HtmlType html = new HtmlType(net.getHeaders(), out, ins);
				html.Deal(net);
			} else {
				byte[] buffer = new byte[cache];
				int length = ins.read(buffer);
				int start = 0;
				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				boolean chunked = false;
				while (length > 0) {
					if (start == 0) {
						byte[] yy = new byte[7];
						System.arraycopy(buffer, 0, yy, 0, yy.length);
						String hear = new String(yy);
						if (hear.contains("EXTM3U")) {
							net.setType(TYPE.M3U8);
						} else {
							String ss = "";
							for (Header h_ : hs_) {
								if(h_.getName().toLowerCase().equals("transfer-encoding"))
								{
									if(h_.getValue().equals("chunked")){
										chunked = true;
									}
								}
								pw.println(h_.getName() + ": " + h_.getValue());
							}
							pw.println("Content-Disposition: filename="+NetUtil.md5(url)+ss);
							if(!chunked){
								pw.println("");
							}
						}
					}
					try {
						bout.write(buffer, 0, length);
						if(bout.size()>=cache){
							if(chunked){
								pw.println("");
								pw.println(NumberUtil.LongTOString(bout.size()));
								
							}
							out.write(bout.toByteArray());
							bout.reset();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					length = ins.read(buffer);
					start = 1;
				}
				if (net.getType() == TYPE.M3U8) {
					M3u8Type mt = new M3u8Type(net.getHeaders(), out);
					mt.setAdditionHeader(req.getAdditionHeader());
					mt.setReq(req);
					mt.setBout(bout);
					mt.Deal(net);
				}else{
					if(chunked){
						pw.println("");
						pw.println(NumberUtil.LongTOString(bout.size()));
					}
					out.write(bout.toByteArray());
					if(chunked){
						pw.println("");
						pw.println(0);
						pw.println("");
					}
				}
			}
			out.flush();
			ins.close();
			out.close();
		} catch (Exception e) {
			try {
				if(out!=null){
					out.flush();
					out.close();
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

}
