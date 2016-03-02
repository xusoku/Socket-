package com.kanke.http.net;

import java.io.Serializable;

public class Header implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4275785263747364094L;
	private String key;
	private String value;

	
	
	public Header(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
