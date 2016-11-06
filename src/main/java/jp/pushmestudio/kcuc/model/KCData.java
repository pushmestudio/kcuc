package jp.pushmestudio.kcuc.model;

import java.util.Date;
import java.util.Objects;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

public class KCData {
	// TODO メソッドの並びを、コンストラク,、Public, Privateのようにわかりやすい並びにする

	/**
	 * 別クラスに用意しているでスタブ用ユーザーのクラスを利用すること
	 * @return 購読しているページ名
	 */
	@Deprecated
	private String getDummyUserRegisterdProduct() {
		/*
		 * 下記のユースケースの結果購読登録が終わっており、その内容をDBから取得している、といった前提を示すスタブ /products から
		 * WebSphere Application Server Network Deployment(SSAW57)を選択
		 * /topic_metadata?href=SSAW57でバージョン違いのドキュメントの存在を確認(SSAW57_liberty)、購読登録
		 */
		String registerdProduct = "SSAW57_liberty";
		return registerdProduct;
	}

	/**
	 * 別クラスに用意しているでスタブ用ユーザーのクラスを利用すること
	 * @return 最後にページを確認した時間のダミーデータ
	 */
	@Deprecated
	private Date getDummyPreservedDate() {
		/*
		 * 購読している製品のあるページの最終更新日として登録している時間、のダミー
		 */

		// 最後に目視で確認した更新日時をハードコードで指定
		// Fri 22 Jul 2016 12:26:52.137 AM JST
		Long preservedTime = 1469114812137L;
		Date date = new Date(preservedTime);

		return date;
	}

	private JSONObject getTOC(String productKey) {
		// @see https://jersey.java.net/documentation/latest/client.html
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("https://www.ibm.com/support/knowledgecenter/v1/toc/" + productKey);

		Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
		Response res = invocationBuilder.get();

		/*
		 * JSONの中身の特にtoc.topicsは、再帰的な構造になっている {"productFamily": {}, "toc": {
		 * "href":"", "label":"", "topics":[{"href":"", "label":""}, {"href":"",
		 * "label":""}]}}
		 */
		JSONObject resJson = new JSONObject(res.readEntity(String.class));

		return resJson.getJSONObject("toc");
	}

	/**
	 * 現時点では最終更新日付けのみ返しているが、Metadataを返すのであれば、
	 * 戻りの型はJSONObjectにして情報を詰める形にするし、そうではなくて最終更新日付けのみのままにするのであれば
	 * メソッド名を適切なものに修正する
	 * 
	 * @param specificHref
	 *            更新の有無を確認する対象のpageのHref
	 * @return 最終更新日付
	 */
	private String getSpecificPageMeta(String specificHref) {
		// @see https://jersey.java.net/documentation/latest/client.html
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("https://www.ibm.com/support/knowledgecenter/v1/topic_metadata")
				.queryParam("href", specificHref);

		Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
		Response res = invocationBuilder.get();

		JSONObject resJson = new JSONObject(res.readEntity(String.class));

		String dateCreated = resJson.getJSONObject("classification").getString("datecreated");
		String dateLastModified = resJson.getJSONObject("classification").getString("datelastmodified");

		// TODO loggerなどで適切にロギングすること
		System.out.println(dateCreated);
		System.out.println(dateLastModified);

		// Objects.nonNullはJava SE8からなので、Java SE7環境なら書き換え必須
		return Objects.nonNull(dateLastModified) ? dateLastModified : dateCreated;
	}

	/**
	 * 更新を確認するため、キーを取得し、そこからページを取得し、そのページのメタ情報を取得した上で、 最終更新日時を比較した結果を返す
	 * 現時点では指定したプロダクトキーでとれるTOCのtopics配列1番目に格納されているページを対象に更新確認している
	 * 
	 * @return 保存されていた時間、最新の時間、更新の有無をJSONオブジェクトにして返す
	 */
	public JSONObject checkUpdate() {
		String productKey = getDummyUserRegisterdProduct();
		String specificHref = ((JSONObject) getTOC(productKey).getJSONArray("topics").get(1)).getString("href");
		String dateLastModified = getSpecificPageMeta(specificHref);

		Date preservedDate = getDummyPreservedDate();
		Date lastModifiedDate = new Date(Long.parseLong(dateLastModified));

		JSONObject result = new JSONObject();

		result.put("preserved", preservedDate).put("current", lastModifiedDate)
				.put("isUpdated", (preservedDate.getTime() < lastModifiedDate.getTime())).put("pageHref", specificHref)
				.put("productKey", productKey);
		return result;
	}

	/**
	 * 更新確認対象のページキー(TOCの中のtopics(ページ一覧の)の中の特定のページのhref)を元に 最終更新日時を比較した結果を返す
	 * 
	 * @param pageKey
	 *            更新確認対象のページのキー
	 * @return 保存されていた時間、最新の時間、更新の有無をJSONオブジェクトにして返す
	 */
	public JSONObject checkPageUpdate(String pageKey) {
		String dateLastModified = getSpecificPageMeta(pageKey);

		Date preservedDate = getDummyPreservedDate();
		Date lastModifiedDate = new Date(Long.parseLong(dateLastModified));

		JSONObject result = new JSONObject();

		result.put("preserved", preservedDate).put("current", lastModifiedDate)
				.put("isUpdated", (preservedDate.getTime() < lastModifiedDate.getTime())).put("pageHref", pageKey);
		/*
		 * resultには次のような値が入る { "current": "Wed Sep 14 03:44:12 JST 2016",
		 * "preserved": "Fri Jul 22 00:26:52 JST 2016", "isUpdated": true,
		 * "pageHref":
		 * "SSAW57_liberty/com.ibm.websphere.wlp.nd.doc/ae/cwlp_about.html" }
		 */
		return result;
	}
}
