package com.cooge.media.proxy.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Dmsg implements Parcelable {

	private String path;

	private String fileName;

	private String url;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeString(fileName);
		arg0.writeString(path);
		arg0.writeString(url);

	}
	public static final Parcelable.Creator<Dmsg> CREATOR = new Creator<Dmsg>() {

		@Override
		public Dmsg createFromParcel(Parcel arg0) {
			Dmsg dmsg = new Dmsg();
			dmsg.fileName = arg0.readString();
			dmsg.path = arg0.readString();
			dmsg.url = arg0.readString();
			return dmsg;
		}

		@Override
		public Dmsg[] newArray(int arg0) {
			return new Dmsg[arg0];
		}
	};

	@Override
	public String toString() {
		return this.getUrl()+"@"+this.getPath()+"@"+this.getFileName();
	}
	public static Dmsg StringToDmsg(String str){
		Dmsg  dmsg = new Dmsg();
		String[] ss = str.split("@");
		dmsg.setUrl(ss[0]);
		dmsg.setPath(ss[1]);
		dmsg.setFileName(ss[2]);
		return dmsg;
	}
	

}
