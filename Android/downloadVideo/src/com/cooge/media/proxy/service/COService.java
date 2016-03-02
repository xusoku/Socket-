package com.cooge.media.proxy.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import com.cooge.media.proxy.DownLoad;
import com.cooge.media.proxy.LiveProxy;
import com.cooge.media.proxy.model.Dmsg;

public class COService extends Service {
	
	@Override
	public IBinder onBind(Intent arg0) {
		return mMessenger.getBinder();
	}
	@Override
	public void onCreate() {
		super.onCreate();
		if(!LiveProxy.isRun){
			System.out.println("服务器已经启动");
			LiveProxy.startService();
		}
	}
	
	public static void	start(Context context){
		Intent intent = new Intent(context,COService.class);
		context.startService(intent);
	}
	public static void	bind(Context context,ServiceConnection mRemoteConnection){
		Intent intent = new Intent(context,COService.class);
		context.bindService(intent, mRemoteConnection, Context.BIND_AUTO_CREATE);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}
	class IncomingHandler extends Handler {
		
		public void handleMessage(Message msg){
			
			switch (msg.what) {
			case DownLoad.STARTDOWN:{
				System.out.println("开始下载------------------------11111111111111111111111");
				System.out.println("key:"+String.valueOf(msg.arg1));
				String msgs = msg.getData().getString(String.valueOf(msg.arg1));
				final Dmsg d = Dmsg.StringToDmsg(msgs);
				System.out.println(d.toString());
				new Thread(){
					@Override
					public void run() {
						DownLoad.DownLoadUrl(d.getUrl(), d.getPath(),d.getFileName());
					}
					
				}.start();
				
			}
				break;
			default:
				break;
			}
			
			
			super.handleMessage(msg);
		}
		
	}
	
	Messenger mMessenger = new Messenger(new IncomingHandler());
}
