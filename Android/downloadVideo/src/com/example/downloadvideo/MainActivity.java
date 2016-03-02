package com.example.downloadvideo;

import org.apache.http.message.BasicHeader;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.cooge.media.proxy.UrlAnalysis;
import com.cooge.media.proxy.service.COService;

public class MainActivity extends Activity  {
	EditText tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		 tv=(EditText)findViewById(R.id.et);
			findViewById(R.id.btn).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					
					String url1="http://vodpcjq-cnc.wasu.cn/pcsan09/mams/vod/201509/15/10/20150915102808631a466ec6a_1b6c2256.mp4";
					BasicHeader bh = new BasicHeader("User-Agent", "wasutv_player");
					UrlAnalysis url = new UrlAnalysis(url1,bh);
					System.out.println(url.getEnUrl());
					tv.setText(url.getEnUrl());					
					COService.start(MainActivity.this);
					
				}
			});
	}

}
