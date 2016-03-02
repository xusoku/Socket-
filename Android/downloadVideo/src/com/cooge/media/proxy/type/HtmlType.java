package com.cooge.media.proxy.type;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.apache.http.Header;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.cooge.media.proxy.NetUtil;

public class HtmlType extends Type {


	public HtmlType(Header[] headers, OutputStream out, InputStream ins) {
		super(headers, out, ins);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void Deal(NetUtil  net) {
		try {
			OutputStreamWriter opw= new OutputStreamWriter(this.getOut(),"utf-8");
			PrintWriter pw = new PrintWriter(opw, true);
			String html = NetUtil.getHtml(this.getIns());
			Document doc = Jsoup.parse(html);
			Elements eles = doc.getElementsByTag("a");
			NetUtil.replaceEle(eles,"href", net.getPath());
			Elements eles_ = doc.getElementsByTag("img");
			NetUtil.replaceEle(eles_,"src", net.getPath());
			for(Header h_:this.getHeaders()){
				pw.println(h_.getName()+": "+h_.getValue());
			}
			pw.println();
			pw.println(doc.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}


}
