package com.cooge.media.proxy;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.cooge.media.proxy.model.DownMsg;
import com.cooge.media.proxy.model.IMsgService;
import com.cooge.media.proxy.service.COService;

public class BindMsg {
	
	
	private ServiceConnection msgConnection = null;
	
	private boolean bindfa = false;
	
	private IMsgService iMsgService = null;
	
	
	public BindMsg(Context context){
		if(!bindfa){
			if(msgConnection==null){
				msgConnection = new ServiceConnection(){
					@Override
					public void onServiceConnected(ComponentName arg0, IBinder arg1) {
						bindfa = true;
						iMsgService = IMsgService.Stub.asInterface(arg1);
					}
					@Override
					public void onServiceDisconnected(ComponentName arg0) {
						bindfa = false;
					}
				};
			}
			COService.bind(context, msgConnection);
		}
	}
	public void downMsg(String key,DownLoadMsg downLoadMsg){
		if(iMsgService!=null&&bindfa){
			try {
				DownMsg dm = iMsgService.getPrice(key);
				downLoadMsg.downloadProgress(dm.getProgress());
				downLoadMsg.downloadSpeed(dm.getSpeed());
				downLoadMsg.fileSize(dm.getSize());
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}else{
			downLoadMsg.downloadProgress(0);
			downLoadMsg.downloadSpeed(null);
			downLoadMsg.fileSize(0);
		}
	}

}
