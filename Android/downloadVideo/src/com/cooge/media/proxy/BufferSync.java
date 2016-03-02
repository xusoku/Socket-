package com.cooge.media.proxy;

public abstract class BufferSync {
	
	public void downProcess(int i){};
	public void finish(String path){};
	public void downStatus(String status){};
	
	public void error(){};

}
