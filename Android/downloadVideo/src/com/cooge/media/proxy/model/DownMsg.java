package com.cooge.media.proxy.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DownMsg implements Parcelable{
	
	private long size;
	
	private String speed;
	
	private int progress;

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeInt(progress);
		arg0.writeLong(size);
		arg0.writeString(speed);
	}
	
	public static final Parcelable.Creator<DownMsg> CREATOR = new Creator<DownMsg>() {
		@Override
		public DownMsg createFromParcel(Parcel arg0) {
			DownMsg dm = new DownMsg();
			dm.progress = arg0.readInt();
			dm.size = arg0.readLong();
			dm.speed = arg0.readString();
			return dm;
		}
		@Override
		public DownMsg[] newArray(int arg0) {
			return new DownMsg[arg0];
		}

	};

}
