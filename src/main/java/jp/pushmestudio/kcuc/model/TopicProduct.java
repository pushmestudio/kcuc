package jp.pushmestudio.kcuc.model;

import org.json.JSONObject;

/**
 * Topicで使用するように修正を加えたProduct、基本的な内容はProductと同じ
 * 元々のProductはhrefだがパラメーターと応答の整合性からpageHrefにしている
 * ほぼ同じ内容に <{@link Product}>がある
 */

public class TopicProduct {
	private String pageHref;
	private String label;

	public TopicProduct() {
		this.pageHref = "";
		this.label = "";
	}

	public TopicProduct(String pageHref, String label) {
		this.pageHref = pageHref;
		this.label = label;
	}

	public TopicProduct(JSONObject jsonProduct) {
		this.pageHref = jsonProduct.getString("href"); // KCの応答はhrefとしてprodIdが入ってくる
		this.label = jsonProduct.getString("label");
	}

	public String getPageHref() {
		return pageHref;
	}

	public void setPageHref(String pageHref) {
		this.pageHref = pageHref;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}