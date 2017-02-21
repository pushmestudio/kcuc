package jp.pushmestudio.kcuc.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 検索結果として得られる各ページの内容
 */
public class Topic {
	private long date;
	private String href;
	private String label;
	private String summary;
	private List<Product> products;

	public Topic() {
		this.products = new ArrayList<>();
	}

	/**
	 * JSONObjectから簡単にセットできるようにしたコンストラクタ
	 * 
	 * @param topicJson
	 */
	public Topic(JSONObject topicJson) {
		this.date = topicJson.getLong("date");
		this.href = topicJson.getString("href");
		this.label = topicJson.getString("label");
		this.summary = topicJson.getString("summary");

		JSONArray productsJson = topicJson.getJSONArray("products");
		this.products = new ArrayList<>();

		// JSONの中にあるproductsを読み、1件ずつProductオブジェクトとして初期化し、リストに追加している
		productsJson.forEach(product -> products.add(new Product((JSONObject) product)));
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
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

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
}