package com.ko.na;

import java.util.HashMap;
import java.util.Map.Entry;

public class HomeImage {
	String description;
	String url;
	String imageTag;

	public HomeImage() {
		super();
	}

	public HomeImage(HashMap<String, Object> row) throws Exception {
		super();
		for (Entry<String, Object> entry : row.entrySet()) {
			switch (entry.getKey()) {
			case "description":
				setDescription((String) entry.getValue());
				break;

			case "URL":
				setUrl((String) entry.getValue());
			} // end switch
		} // end for
		setImageTag((new Image(getUrl(), getDescription())).toEmbeddedHTML());
	} // end constructor

	public String getDescription() {
		return description;
	} // end getDescription() method

	public String getImageTag() throws Exception {
		return imageTag;
	}

	public String getUrl() {
		return url;
	} // end getUrl() method

	public void setDescription(String description) {
		this.description = description;
	} // end setDescription() method

	public void setImageTag(String imageTag) {
		this.imageTag = imageTag;
	}

	public void setUrl(String url) {
		this.url = url;
	} // end setUrl() method
} // end class
