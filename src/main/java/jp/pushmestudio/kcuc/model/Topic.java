package jp.pushmestudio.kcuc.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 検索結果として得られる各ページの内容
 * 元々のKCの応答はpageHrefではなくhrefだが、全体の整合を取る観点からpageHrefにしている
 */
public class Topic {
	private long date;
	private String pageHref;
	private String label;
	private String summary;
	private List<TopicProduct> products;

	public Topic() {
		this.products = new ArrayList<>();
	}

	/**
	 * JSONObjectから簡単にセットできるようにしたコンストラクタ
	 * 
	 * @param topicJson topicを格納したJSONオブジェクト
	 */
	public Topic(JSONObject topicJson) {
		this.date = topicJson.getLong("date");
		this.pageHref = topicJson.getString("href");
		this.label = topicJson.getString("label");
		this.summary = topicJson.getString("summary");

		JSONArray productsJson = topicJson.getJSONArray("products");
		this.products = new ArrayList<>();

		// JSONの中にあるproductsを読み、1件ずつProductオブジェクトとして初期化し、リストに追加している
		productsJson.forEach(product -> products.add(new TopicProduct((JSONObject) product)));
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public String getPageHref() {
		return pageHref;
	}

	public void setPageHref(String href) {
		this.pageHref = href;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public List<TopicProduct> getProducts() {
		return products;
	}

	public void setProducts(List<TopicProduct> products) {
		this.products = products;
	}
}