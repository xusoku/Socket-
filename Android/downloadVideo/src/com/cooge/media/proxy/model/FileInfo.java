package com.cooge.media.proxy.model;

public class FileInfo {
	
	private double time;
	
	private long size;
	
	private double Totletime;
	
	//private 

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}
	
	public void addTime(double time){
		this.time = this.time+time;
	}
	public void addTotletime(double time){
		this.Totletime = this.Totletime+time;
	}
	public void addSize(long size){
		this.size = this.size+size;
	}
	
	public double getTotletime() {
		return Totletime;
	}

	public void setTotletime(double totletime) {
		Totletime = totletime;
	}

	
	
	public long getFileSize(){
		return	(long) ((this.Totletime/this.time)*this.size);
	}

}
