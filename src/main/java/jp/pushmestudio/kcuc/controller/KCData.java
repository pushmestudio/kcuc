package jp.pushmestudio.kcuc.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

import jp.pushmestudio.kcuc.dao.UserInfoDao;
import jp.pushmestudio.kcuc.model.ResultPageList;
import jp.pushmestudio.kcuc.model.ResultSearch;
import jp.pushmestudio.kcuc.model.ResultUserList;
import jp.pushmestudio.kcuc.model.SubscribedPage;
import jp.pushmestudio.kcuc.model.UserDocument;
import jp.pushmestudio.kcuc.model.UserInfo;
import jp.pushmestudio.kcuc.util.KCMessageFactory;
import jp.pushmestudio.kcuc.util.Result;

public class KCData {
	// TODO メソッドの並びを、コンストラクタ, Public, Privateのようにわかりやすい並びにする

	public KCData() {
	}

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
	public Result checkUpdateByPage(String pageKey) {
		try {
			// ページキーが取得できない場合はエラーメッセージを返す
			if (!isTopicExist(pageKey)) {
				return KCMessageFactory.createMessage(Result.CODE_SERVER_ERROR, "Page Not Found.");
			}

			// KCからのデータ取得処理
			String dateLastModified = getSpecificPageMeta(pageKey);
			Date lastModifiedDate = new Date(Long.parseLong(dateLastModified));

			// DBのユーザーからのデータ取得処理
			UserInfoDao userInfoDao = new UserInfoDao();
			List<UserDocument> userList = userInfoDao.getSubscribedUserList(pageKey);

			// return用
			Result result = new ResultUserList(pageKey);

			for (UserDocument userDoc : userList) {
				// userDocにはsubscribedPagesがListで複数保持されているため、該当のpageKeyをもつもののみ抽出
				List<String> targetPageUpdatedTime = userDoc.getSubscribedPages().stream()
						.filter(s -> s.getPageHref().equals(pageKey)).map(s -> s.getUpdatedTime())
						.collect(Collectors.toList());

				// ↑の結果はListで返るが、1ユーザが同じページを購読することは仕様上禁止されるはずであるため最初の値を常に使用できる
				Long preservedDate = Long.parseLong(targetPageUpdatedTime.get(0));
				UserInfo eachUser = new UserInfo(userDoc.getUserId(), preservedDate < lastModifiedDate.getTime());
				((ResultUserList) result).addSubscriber(eachUser);
			}

			return result;
		} catch (JSONException e) {
			e.printStackTrace();

			// エラーメッセージを作成
			return KCMessageFactory.createMessage(Result.CODE_SERVER_ERROR, "Internal Server Error.");
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
	public Result checkUpdateByUser(String userId) {
		try {
			// DBのユーザーからのデータ取得処理
			UserInfoDao userInfoDao = new UserInfoDao();

			// 指定されたユーザが見つからなかった場合、エラーメッセージを返す
			if (!userInfoDao.isUserExist(userId)) {
				return KCMessageFactory.createMessage(Result.CODE_SERVER_ERROR, "User Not Found.");
			}

			// IDはユニークなはずなので、Listにする必要はない
			List<UserDocument> userList = userInfoDao.getUserList(userId);
			// return用
			Result result = new ResultPageList(userId);

			for (UserDocument userDoc : userList) {
				List<SubscribedPage> subscribedPages = userDoc.getSubscribedPages();

				for (SubscribedPage entry : subscribedPages) {
					String pageKey = entry.getPageHref();
					Long preservedDate = Long.parseLong(entry.getUpdatedTime());

					// KCからのデータ取得処理
					String dateLastModified = getSpecificPageMeta(pageKey);

					// ページキーが取得できない場合はエラーメッセージを返す
					if (dateLastModified == "none") {
						return KCMessageFactory.createMessage(Result.CODE_SERVER_ERROR, "Page Not Found.");
					}

					Date lastModifiedDate = new Date(Long.parseLong(dateLastModified));

					entry.setIsUpdated(preservedDate < lastModifiedDate.getTime());
					((ResultPageList) result).addSubscribedPage(entry);
				}
			}

			return result;
		} catch (JSONException e) {
			e.printStackTrace();

			// エラーメッセージを作成
			return KCMessageFactory.createMessage(Result.CODE_SERVER_ERROR, "Internal Server Error.");
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
	public Result registerSubscribedPage(String userId, String href) {
		try {
			// .htmでの登録は行わせず、全て.htmlで登録を行わせるように拡張子を統一（不正な拡張子はisTopicExist()で弾かれる)
			String pageHref = href.replaceFirst("\\.htm$", "\\.html");

			// DBのユーザーからのデータ取得処理
			UserInfoDao userInfoDao = new UserInfoDao();

			// 指定されたユーザがDBに存在しない場合、エラーメッセージを返す
			if (!userInfoDao.isUserExist(userId)) {
				return KCMessageFactory.createMessage(Result.CODE_SERVER_ERROR, "User Not Found.");
			} else if (!isTopicExist(pageHref)) {
				// 指定されたページがKnowledgeCenterに存在しない場合もエラーメッセージを返す
				return KCMessageFactory.createMessage(Result.CODE_SERVER_ERROR, "Page Not Found.");
			} else if (userInfoDao.isPageExist(userId, pageHref)) {
				// 指定されたページを既に購読している場合もエラーメッセージを返す
				return KCMessageFactory.createMessage(Result.CODE_SERVER_ERROR, "You Already Subscribe This Page.");
			}

			List<UserDocument> userList = userInfoDao.setSubscribedPages(userId, pageHref);
			// return用
			Result result = new ResultPageList(userId);
			((ResultPageList) result).setSubscribedPages(userList.get(0).getSubscribedPages());

			return result;
		} catch (JSONException e) {
			e.printStackTrace();
			// エラーメッセージを作成
			return KCMessageFactory.createMessage(Result.CODE_SERVER_ERROR, "Internal Server Error.");
		}
	}

	/**
	 * キーワード検索、現時点では特段の独自拡張はしていない
	 * 検索されたキーワードをログに残してどういうものが多く探されているか調べてもいいかもしれない
	 * @param query スペース区切りで複数ワードが与えられた場合はAND検索
	 * @param offset 検索結果を何件目から取得するか
	 * @param limit 取得件数、MAXは20
	 * @param lang 検索結果がサポートしている言語
	 * @return
	 */
	public Result searchPages(String query, Integer offset, Integer limit, String lang) {
		// @see https://jersey.java.net/documentation/latest/client.html
		Client client = ClientBuilder.newClient();
		final String searchUrl = "https://www.ibm.com/support/knowledgecenter/v1/search";

		WebTarget target = client.target(searchUrl).queryParam("query", query);

		/*
		 * パラメーターが存在するなら追加する、という処理、
		 * さらにパラメーターが増えるならわかりにくいので、1.パラメーターがあるならリスト等に追加、2.リスト等を回してパラメーターとして追加、
		 * という処理を実装する
		 */
		// TODO 値を返さないといけない
		Optional.ofNullable(offset).ifPresent(_offset -> target.queryParam("offset", _offset));
		Optional.ofNullable(limit).ifPresent(_limit -> target.queryParam("limit", _limit));;
		Optional.ofNullable(lang).ifPresent(_lang -> target.queryParam("lang", _lang));;

		Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
		Response res = invocationBuilder.get();

		JSONObject resJson = new JSONObject(res.readEntity(String.class));

		// ページがtopics情報を持つ場合
		if (resJson.has("topics")) {
			Result result = new ResultSearch();
			return ((ResultSearch) result);
		} else {
			return null;
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
		final String topicMetaUrl = "https://www.ibm.com/support/knowledgecenter/v1/topic_metadata";
		WebTarget target = client.target(topicMetaUrl).queryParam("href", specificHref);

		Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
		Response res = invocationBuilder.get();

		JSONObject resJson = new JSONObject(res.readEntity(String.class));

		// ページがclassification情報を持つ場合
		if (resJson.has("classification")) {
			return resJson.getJSONObject("classification").has("datelastmodified")
					? resJson.getJSONObject("classification").getString("datelastmodified")
					: resJson.getJSONObject("classification").getString("datecreated");

			// Objects.nonNullはJava SE8からなので、Java SE7環境なら書き換え必須
			// return Objects.nonNull(dateLastModified) ? dateLastModified :
			// dateCreated;
		} else
			return "none";
	}

	@SuppressWarnings("unused")
	private JSONObject getTOC(String productKey) throws JSONException {
		// @see https://jersey.java.net/documentation/latest/client.html
		Client client = ClientBuilder.newClient();
		final String tocUrl = "https://www.ibm.com/support/knowledgecenter/v1/toc/";
		WebTarget target = client.target(tocUrl + productKey);

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
	 * 購読ページがKnowledgeCenter上に存在するか確認する
	 * 
	 * @param pageHref
	 *            確認する購読ページキー
	 * @return True or False
	 */
	public Boolean isTopicExist(String pageHref) {
		return getSpecificPageMeta(pageHref) != "none" ? true : false;
	}
}
