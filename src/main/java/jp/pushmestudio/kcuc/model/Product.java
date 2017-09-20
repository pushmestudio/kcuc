package jp.pushmestudio.kcuc.model;

import org.json.JSONObject;

/**
 * 元々のProductはhrefだがパラメーターと応答の整合性からprodIdにしている
 * ほぼ同じ内容に <{@link TopicProduct}>がある
 */

public class Product {
	private String prodId;
	private String label;

	public Product() {
		this.prodId = "";
		this.label = "";
	}

	public Product(String prodId, String label) {
		this.prodId = prodId;
		this.label = label;
	}

	public Product(JSONObject jsonProduct) {
		this.prodId = jsonProduct.getString("href"); // KCの応答はhrefとしてprodIdが入ってくる
		this.label = jsonProduct.getString("label");
	}

	public String getProdId() {
		return prodId;
	}

	public void setProdId(String prodId) {
		this.prodId = prodId;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}