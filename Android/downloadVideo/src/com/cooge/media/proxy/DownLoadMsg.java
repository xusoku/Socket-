package com.cooge.media.proxy;

public abstract class  DownLoadMsg {
	
	public void fileSize(long size){};
	public void downloadSpeed(String speed){};
	public void downloadProgress(int progress){};
	
}
