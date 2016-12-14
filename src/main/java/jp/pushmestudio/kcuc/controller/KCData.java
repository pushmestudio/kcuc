package jp.pushmestudio.kcuc.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jp.pushmestudio.kcuc.dao.UserInfoDao;
import jp.pushmestudio.kcuc.model.UserInfo;

public class KCData {
	// TODO メソッドの並びを、コンストラク,、Public, Privateのようにわかりやすい並びにする

	/**
	 * 更新確認対象のページキー(TOCの中のtopics(ページ一覧の)の中の特定のページのhref)を元に 最終更新日時を比較した結果を返す
	 * 取得に失敗した場合、エラーが発生したことを示すJSONを返す
	 * 
	 * @param pageKey
	 *            更新確認対象のページのキー
	 * @return あるページを購読しているユーザーごとの最終更新日付けとの差異確認結果、以下は例示
	 *         <code>{"userList":[{"isUpdated":true,"id":"capsmalt"}],"pageHref":
	 *         "SSAW57_liberty/com.ibm.websphere.wlp.nd.doc/ae/cwlp_about.html"}</code>
	 */
	public JSONObject checkUpdateByPage(String pageKey) {
		try {
			// return用
			JSONObject result = new JSONObject();
			JSONArray resultUserList = new JSONArray();

			// KCからのデータ取得処理
			String dateLastModified = getSpecificPageMeta(pageKey);
			Date lastModifiedDate = new Date(Long.parseLong(dateLastModified));

			// DBのユーザーからのデータ取得処理
			UserInfoDao userInfoDao = new UserInfoDao();
			List<UserInfo> userList = userInfoDao.getSubscribedUserList(pageKey);

			for (UserInfo userInfo : userList) {
				Long preservedDate = userInfo.getSubscribedPages().get(pageKey);
				JSONObject eachUser = new JSONObject();

				eachUser.put("id", userInfo.getId()).put("isUpdated", preservedDate < lastModifiedDate.getTime());
				resultUserList.put(eachUser);
			}

			result.put("pageHref", pageKey);
			result.put("userList", resultUserList);
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
			JSONObject result = new JSONObject();

			// TODO エラーメッセージの充実化
			result.put("message", "No data found.");
			return result;
		}
	}

	/**
	 * 更新確認対象のユーザーIDを元に 最終更新日時を比較した結果を返す
	 * 
	 * @param userId
	 *            更新確認対象のユーザーID
	 * @return あるページを購読しているユーザーごとの最終更新日付けとの差異確認結果、以下は例示
	 *         <code>{"pages":[{"isUpdated":true,"pageHref":
	 *         "SSAW57_liberty/com.ibm.websphere.wlp.nd.doc/ae/cwlp_about.html"}],
	 *         "id":"capsmalt"}</code>
	 */
	public JSONObject checkUpdateByUser(String userId) {
		try {
			// return用
			JSONObject result = new JSONObject();
			JSONArray resultPages = new JSONArray();

			// DBのユーザーからのデータ取得処理
			UserInfoDao userInfoDao = new UserInfoDao();
			// IDはユニークなはずなので、Listにする必要はない
			List<UserInfo> userList = userInfoDao.getUserList(userId);

			for (UserInfo userInfo : userList) {
				Map<String, Long> subscribedPages = userInfo.getSubscribedPages();

				for (Map.Entry<String, Long> entry : subscribedPages.entrySet()) {
					JSONObject eachPage = new JSONObject();
					String pageKey = entry.getKey();
					Long preservedDate = entry.getValue();

					// KCからのデータ取得処理
					String dateLastModified = getSpecificPageMeta(pageKey);
					Date lastModifiedDate = new Date(Long.parseLong(dateLastModified));

					eachPage.put("pageHref", entry.getKey());
					eachPage.put("isUpdated", preservedDate < lastModifiedDate.getTime());
					resultPages.put(eachPage);
				}
			}

			result.put("id", userId);
			result.put("pages", resultPages);
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
			JSONObject result = new JSONObject();

			// TODO エラーメッセージの充実化
			result.put("message", "No data found.");
			return result;
		}
	}

	/**
	 * 購読したいページとユーザIDを追加し、追加したページを含めた結果を返す
	 * 
	 * @param userId
	 *            登録確認対象のユーザーID
	 * @param pageHref
	 *            購読登録するページ
	 * @return 登録の成否と、あるユーザが購読しているリストの一覧。以下は例示
	 *         <code>{"result":"success", "pages":[{"pageHref":"SSAW57_liberty/com.ibm.websphere.wlp.nd.doc/ae/cwlp_about.html"},
	 *         {"pageHref":"SS42VS_7.2.7/com.ibm.qradar.doc/b_qradar_qsg.html"}],
	 *         "id":"capsmalt"}</code>
	 */
	public JSONObject registerSubscribedPages(String userId, String pageHref) {
		try {
			// return用
			JSONObject result = new JSONObject();
			JSONArray subscribedList = new JSONArray();

			// DBのユーザーからのデータ取得処理
			UserInfoDao userInfoDao = new UserInfoDao();

			// DB登録後のユーザ情報を保存するためのリストを作成
			List<UserInfo> userList = userInfoDao.setSubscribedPages(userId, pageHref);

			for (UserInfo userInfo : userList) {
				Map<String, Long> subscribedPages = userInfo.getSubscribedPages();

				for (Map.Entry<String, Long> entry : subscribedPages.entrySet()) {
					JSONObject eachPage = new JSONObject();

					eachPage.put("pageHref", entry.getKey());
					subscribedList.put(eachPage);
				}
			}

			result.put("result", "success");
			result.put("pages", subscribedList);
			result.put("id", userId);
			// JSONArray resultPages = new JSONArray();

			return result;

		} catch (JSONException e) {
			e.printStackTrace();
			JSONObject result = new JSONObject();

			// TODO エラーメッセージの充実化
			result.put("result", "false");
			result.put("message", "error occured");
			return result;
		}
	}

	/**
	 * 現時点では最終更新日付けのみ返しているが、Metadataを返すのであれば、
	 * 戻りの型はJSONObjectにして情報を詰める形にするし、そうではなくて最終更新日付けのみのままにするのであれば
	 * メソッド名を適切なものに修正する
	 * 
	 * @param specificHref
	 *            更新の有無を確認する対象のpageのHref
	 * @return 最終更新日付
	 * @throws JSONException
	 *             ページのメタ情報が見つからない場合はJSONExceptionをスロー
	 */
	private String getSpecificPageMeta(String specificHref) throws JSONException {
		// @see https://jersey.java.net/documentation/latest/client.html
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("https://www.ibm.com/support/knowledgecenter/v1/topic_metadata")
				.queryParam("href", specificHref);

		Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
		Response res = invocationBuilder.get();

		JSONObject resJson = new JSONObject(res.readEntity(String.class));
		// System.out.println(resJson);

		String dateCreated = resJson.getJSONObject("classification").getString("datecreated");
		String dateLastModified = resJson.getJSONObject("classification").getString("datelastmodified");

		// Objects.nonNullはJava SE8からなので、Java SE7環境なら書き換え必須
		return Objects.nonNull(dateLastModified) ? dateLastModified : dateCreated;
	}

	@SuppressWarnings("unused")
	private JSONObject getTOC(String productKey) throws JSONException {
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
}
