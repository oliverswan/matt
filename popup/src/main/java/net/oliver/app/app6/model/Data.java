package net.oliver.app.app6.model;

import java.util.HashMap;
import java.util.Map;

public class Data {

	private String id;
	private Map<String, String> info;

	public Data(String id) {
		this.id = id;
	}

	public void setInfo(String key, String content) {
		if (info == null)
			info = new HashMap<String, String>();

		info.put(key, content);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String, String> getInfo() {
		return info;
	}

	public void setInfo(Map<String, String> info) {
		this.info = info;
	}

}
