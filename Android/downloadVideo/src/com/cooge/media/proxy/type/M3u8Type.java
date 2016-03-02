package com.cooge.media.proxy.type;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;

import com.cooge.media.proxy.NetUtil;
import com.cooge.media.proxy.Request;
import com.cooge.media.proxy.model.FileInfo;
import com.cooge.media.proxy.model.M3u8;

public class M3u8Type extends Type {

	private ByteArrayOutputStream bout;
	
	private Request req;

	public Request getReq() {
		return req;
	}
	public void setReq(Request req) {
		this.req = req;
	}

	public M3u8Type(Header[] headers, OutputStream out) {
		super(headers, out,null);
	}
	
	private Header[] additionHeader;

	public Header[] getAdditionHeader() {
		return additionHeader;
	}
	public void setAdditionHeader(Header[] additionHeader) {
		this.additionHeader = additionHeader;
	}
	public M3u8Type(Header[] headers, OutputStream out, InputStream ins) {
		super(headers, out, ins);
	}
	
	

	public ByteArrayOutputStream getBout() {
		return bout;
	}



	public void setBout(ByteArrayOutputStream bout) {
		this.bout = bout;
	}



	@Override
	public void Deal(NetUtil  net) {
		
		try {
			OutputStreamWriter opw= new OutputStreamWriter(this.getOut(),"utf-8");
			PrintWriter pw = new PrintWriter(opw, true);
			String mu = this.getBout().toString("utf-8").replaceAll("\r\n", "\n");
			String[] ms = 	mu.split("\n");
			StringBuffer mu_ = new StringBuffer();
			for(String mm:ms){
				if(!mm.startsWith("#")&&mm.trim().length()>4){
					mm= NetUtil.replaceUrl(this.getReq().host,net.getPath(), mm);
					
					if(this.getAdditionHeader()!=null&&this.getAdditionHeader().length>0)
					mm = NetUtil.AdditionHeader(mm,this.getAdditionHeader());
				}
				if(mm.startsWith("#")||mm.startsWith("http")){
					mu_.append(mm+"\r\n");
				}
				if(mm.contains("EXT-X-ENDLIST")){
					break;
				}
			}
			for(Header h_:this.getHeaders()){
				if(h_.getName().toLowerCase().equals("content-length"))
				{
					continue;
				}
				if(h_.getName().toLowerCase().equals("transfer-encoding"))
				{
					continue;
				}
				pw.println(h_.getName()+": "+h_.getValue());
			}
			byte[] bs = mu_.toString().getBytes();
			pw.println("Content-Length: "+(bs.length));
			pw.println("Content-Disposition: filename="+new Date().getTime()+".m3u8");
			pw.println();
			this.getOut().write(bs);
		} catch (Exception e) {
		}
		
		
	}

	public static List<M3u8> getVideoList(String html,NetUtil net){
		html = html.replaceAll("\r\n", "\n");
		List<M3u8> list = new ArrayList<M3u8>();
		String[] ms = 	html.split("\n");
		M3u8 m3u8 =new M3u8();
		for(String mm:ms){
			if(mm.contains("EXTINF")){
				m3u8.setTime(mm);
			}
			if(!mm.startsWith("#")&&mm.trim().length()>4){
				m3u8.setUrl(NetUtil.replaceUrl(net.getPath(), mm));
				list.add(m3u8);
				m3u8 =new M3u8();
			}
		}
		return list;
	}
	public static List<M3u8> getVideoList(String html,String path){
		html = html.replaceAll("\r\n", "\n");
		List<M3u8> list = new ArrayList<M3u8>();
		String[] ms = 	html.split("\n");
		M3u8 m3u8 =new M3u8();
		for(String mm:ms){
			if(mm.contains("EXTINF")){
				m3u8.setTime(mm);
			}
			if(!mm.startsWith("#")&&mm.trim().length()>4){
				m3u8.setUrl(NetUtil.replaceUrl(path, mm));
				list.add(m3u8);
				m3u8 =new M3u8();
			}
		}
		return list;
	}
	
	public static List<M3u8> getVideoList(FileInfo  fileInfo,String html,NetUtil net){
		html = html.replaceAll("\r\n", "\n");
		List<M3u8> list = new ArrayList<M3u8>();
		String[] ms = 	html.split("\n");
		M3u8 m3u8 =new M3u8();
		for(String mm:ms){
			if(mm.contains("EXTINF")){
				m3u8.setTime(mm);
				double t = Double.valueOf(mm.replaceAll("[^0-9|.]", ""));
				fileInfo.addTotletime(t);
			}
			if(!mm.startsWith("#")&&mm.trim().length()>4){
				m3u8.setUrl(NetUtil.replaceUrl(net.getPath(), mm));
				list.add(m3u8);
				m3u8 =new M3u8();
			}
		}
		return list;
	}
	
	public static List<M3u8> getVideoList(String html){
		html = html.replaceAll("\r\n", "\n");
		List<M3u8> list = new ArrayList<M3u8>();
		String[] ms = 	html.split("\n");
		M3u8 m3u8 =new M3u8();
		for(String mm:ms){
			if(mm.contains("EXTINF")){
				m3u8.setTime(mm);
			}
			if(!mm.startsWith("#")&&mm.trim().length()>4){
				list.add(m3u8);
				m3u8 =new M3u8();
			}
		}
		return list;
	}
	
	public static List<M3u8> getVideoList(FileInfo  fileInfo,String html){
		html = html.replaceAll("\r\n", "\n");
		List<M3u8> list = new ArrayList<M3u8>();
		String[] ms = 	html.split("\n");
		M3u8 m3u8 =new M3u8();
		for(String mm:ms){
			if(mm.contains("EXTINF")){
				m3u8.setTime(mm);
				double t = Double.valueOf(mm.replaceAll("[^0-9|.]", ""));
				fileInfo.addTotletime(t);
			}
			if(!mm.startsWith("#")&&mm.trim().length()>4){
				m3u8.setUrl(mm);
				list.add(m3u8);
				m3u8 =new M3u8();
			}
		}
		return list;
	}

}
