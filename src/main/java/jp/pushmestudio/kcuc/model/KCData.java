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
	private String getDummyUserRegisterdProduct() {
		/* 下記のユースケースの結果購読登録が終わっており、その内容をDBから取得している、といった前提を示すスタブ
		 * /products から WebSphere Application Server Network Deployment(SSAW57)を選択
		 * /topic_metadata?href=SSAW57でバージョン違いのドキュメントの存在を確認(SSAW57_liberty)、購読登録 */
		String registerdProduct = "SSAW57_liberty";
		return registerdProduct;
	}
	
	private Date getDummyPreservedDate() {
		/*
		 * 購読している製品のあるページの最終更新日として登録している時間、のダミー
		 */

		// 最後に目視で確認した更新日時をハードコードで指定
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

		/* JSONの中身の特にtoc.topicsは、再帰的な構造になっている
		 * {"productFamily": {},
		 * "toc": { "href":"", "label":"", "topics":[{"href":"", "label":""}, {"href":"", "label":""}]}} */
		JSONObject resJson = new JSONObject(res.readEntity(String.class));

		return resJson.getJSONObject("toc");
	}

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

		System.out.println(dateCreated);
		System.out.println(dateLastModified);

		// Objects.nonNullはJava SE8からなので、Java SE7環境なら書き換え必須
		return Objects.nonNull(dateLastModified) ? dateLastModified : dateCreated;
	}

	/**
	 * 更新を確認するため、キーを取得し、そこからページを取得し、そのページのメタ情報を取得した上で、
	 * 最終更新日時を比較した結果を返す
	 * 現時点では指定したプロダクトキーでとれるTOCのtopics配列1番目に格納されているページを対象に更新確認している
	 * @return 保存されていた時間、最新の時間、更新の有無をJSONオブジェクトにして返す
	 */
	public JSONObject checkUpdate() {
		String productKey = getDummyUserRegisterdProduct();
		String specificHref = ((JSONObject) getTOC(productKey).getJSONArray("topics").get(1)).getString("href");
		String dateLastModified = getSpecificPageMeta(specificHref);
		
		Date preservedDate = getDummyPreservedDate();
		Date lastModifiedDate = new Date(Long.parseLong(dateLastModified));
		
		JSONObject result = new JSONObject();
		
		result.append("preserved", preservedDate)
			.append("current", lastModifiedDate)
			.append("isUpdated", (preservedDate.getTime() < lastModifiedDate.getTime()))
			.append("pageHref", specificHref)
			.append("productKey", productKey);
		return result;
	}

	/**
	 * いずれは引数のキーからページを取得しメタ情報を取得、といった実装にするが、
	 * 現時点では<code>checkUpdate()</code>を呼び出すだけ
	 * @param productKey 更新確認対象のプロダクトのキー
	 * @return 保存されていた時間、最新の時間、更新の有無をJSONオブジェクトにして返す
	 */
	public JSONObject checkUpdate(String productKey) {
		System.out.println("Not implement yet. Call the checkUpdate()");
		return checkUpdate();
	}
}
