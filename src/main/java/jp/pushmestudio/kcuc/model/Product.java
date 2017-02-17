package jp.pushmestudio.kcuc.model;

import org.json.JSONObject;

public class Product {
	private String href;
	private String label;

	public Product() {
		this.href = "";
		this.label = "";
	}
	
	public Product(String href, String label) {
		this.href = href;
		this.label = label;
	}

	public Product(JSONObject jsonProduct) {
		this.href = jsonProduct.getString("href");
		this.label = jsonProduct.getString("label");
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}