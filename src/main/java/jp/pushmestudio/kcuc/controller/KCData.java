package jp.pushmestudio.kcuc.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
import jp.pushmestudio.kcuc.model.Product;
import jp.pushmestudio.kcuc.model.ResultContent;
import jp.pushmestudio.kcuc.model.ResultPageList;
import jp.pushmestudio.kcuc.model.ResultProductList;
import jp.pushmestudio.kcuc.model.ResultSearchList;
import jp.pushmestudio.kcuc.model.ResultUserList;
import jp.pushmestudio.kcuc.model.SubscribedPage;
import jp.pushmestudio.kcuc.model.Topic;
import jp.pushmestudio.kcuc.model.TopicMeta;
import jp.pushmestudio.kcuc.model.UserDocument;
import jp.pushmestudio.kcuc.model.UserInfo;
import jp.pushmestudio.kcuc.util.KCMessageFactory;
import jp.pushmestudio.kcuc.util.Result;

/**
 * ページ応答と各種処理とをつなぐ コンテキストを共有するわけではないので、シングルトンにした方が良いかもしれない
 * Result型を応答する(=APIインタフェースから呼ばれる)場合は、必ずtry-catch処理を実施すること
 */
public class KCData {
	/**
	 * 購読解除したいページを削除し、購読情報を結果として返す
	 *
	 * @param userId
	 *            対象のユーザーID
	 * @param prodId
	 *            購読解除する製品ID、このIDに紐づくすべてのページを購読解除する
	 * @return 実施結果の成否の入ったオブジェクトをラップしたMessageオブジェクト(何を返すべきか検討の余地あり)
	 */
	public Result cancelSubscribedProduct(String userId, String prodId) {
		try {
			// DBのユーザーからのデータ取得処理
			UserInfoDao userInfoDao = UserInfoDao.getInstance();

			// 指定されたユーザがDBに存在しない場合、エラーメッセージを返す
			if (!userInfoDao.isUserExist(userId)) {
				return KCMessageFactory.createMessage(Result.CODE_SERVER_ERROR, "User Not Found.");
			}

			com.cloudant.client.api.model.Response res = userInfoDao.cancelSubscribedProduct(userId, prodId);
			return KCMessageFactory.createMessage(res.getStatusCode(), res.getReason());
		} catch (JSONException e) {
			e.printStackTrace();
			// エラーメッセージを作成
			return KCMessageFactory.createMessage(Result.CODE_SERVER_ERROR, "Internal Server Error.");
		} catch (IndexOutOfBoundsException ee) {
			// 購読しているページの中に指定製品が含まれるかを確認するメソッドを実装したらこの処理は削除する
			ee.printStackTrace();
			// エラーメッセージを作成
			return KCMessageFactory.createMessage(Result.CODE_SERVER_ERROR, "Not Yet Subscribed This Product.");
		}
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
		String pageHref = this.normalizeHref(pageKey);
		try {
			// KCからのデータ取得処理
			TopicMeta topicMeta = getSpecificPageMeta(pageHref);

			// ページキーが取得できない場合はエラーメッセージを返す
			if (!topicMeta.isExist()) {
				return KCMessageFactory.createMessage(Result.CODE_SERVER_ERROR, "Page Not Found.");
			}

			Date lastModifiedDate = new Date(topicMeta.getDateLastUpdated());

			// DBのユーザーからのデータ取得処理
			UserInfoDao userInfoDao = UserInfoDao.getInstance();
			List<UserDocument> userList = userInfoDao.getSubscribedUserList(pageHref);

			// return用
			Result result = new ResultUserList(pageHref);

			for (UserDocument userDoc : userList) {
				// userDocにはsubscribedPagesがListで複数保持されているため、該当のpageKeyをもつもののみ抽出
				List<Long> targetPageUpdatedTime = userDoc.getSubscribedPages().stream()
						.filter(s -> s.getPageHref().equals(pageHref)).map(s -> s.getUpdatedTime())
						.collect(Collectors.toList());

				// ↑の結果はListで返るが、1ユーザが同じページを購読することは仕様上禁止されるはずであるため最初の値を常に使用できる
				long preservedDate = targetPageUpdatedTime.get(0);
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
		return this.checkUpdateByUser(userId, null);
	}

	/**
	 * 更新確認対象のユーザーIDを元に 最終更新日時を比較した結果を返す 製品ID指定の指定も可能にしている
	 *
	 * @param userId
	 *            更新確認対象のユーザーID
	 * @param prodId
	 *            更新確認対象の製品ID, Optional
	 * @return あるページを購読しているユーザーごとの最終更新日付けとの差異確認結果、以下は例示
	 *         <code>{"pages":[{"isUpdated":true,"pageHref":
	 *         "SSAW57_liberty/com.ibm.websphere.wlp.nd.doc/ae/cwlp_about.html"}],
	 *         "id":"capsmalt"}</code>
	 */
	public Result checkUpdateByUser(String userId, String prodId) {
		try {
			// DBのユーザーからのデータ取得処理
			UserInfoDao userInfoDao = UserInfoDao.getInstance();

			// 指定されたユーザが見つからなかった場合、エラーメッセージを返す
			if (!userInfoDao.isUserExist(userId)) {
				return KCMessageFactory.createMessage(Result.CODE_SERVER_ERROR, "User Not Found.");
			}

			// IDはユニークなはずなので、Listにする必要はない
			List<UserDocument> userList;
			if (Objects.isNull(prodId)) {
				userList = userInfoDao.getUserList(userId);
			} else {
				// 製品ID指定による限定
				userList = userInfoDao.getUserList(userId, prodId);
			}

			// return用
			Result result = new ResultPageList(userId);

			for (UserDocument userDoc : userList) {
				List<SubscribedPage> subscribedPages = userDoc.getSubscribedPages();

				for (SubscribedPage entry : subscribedPages) {
					String pageKey = entry.getPageHref();
					long preservedDate = entry.getUpdatedTime();

					// KCからのデータ取得処理
					TopicMeta topicMeta = getSpecificPageMeta(pageKey);

					// ページの更新情報が取得できないときは0を返す
					if (!topicMeta.isExist()) {
						return KCMessageFactory.createMessage(Result.CODE_SERVER_ERROR, "Page Not Found.");
					}

					Date lastModifiedDate = new Date(topicMeta.getDateLastUpdated());

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
	 * 指定したIDのユーザーを作成する
	 * 
	 * @param userId
	 *            作成対象のユーザーのID
	 * @return 実施結果の成否の入ったオブジェクトをラップしたMessageオブジェクト(何を返すべきか検討の余地あり)
	 */
	public Result createUser(String userId) {
		// DBのユーザーからのデータ取得処理
		UserInfoDao userInfoDao = UserInfoDao.getInstance();

		com.cloudant.client.api.model.Response res = userInfoDao.createUser(userId);

		Result result = KCMessageFactory.createMessage(res.getStatusCode(), res.getReason());
		return result;
	}

	/**
	 * 購読解除したいページを削除し、購読情報を結果として返す
	 *
	 * @param userId
	 *            対象のユーザーID
	 * @param href
	 *            購読解除するページ
	 * @return 実施結果の成否の入ったオブジェクトをラップしたMessageオブジェクト(何を返すべきか検討の余地あり)
	 *
	 */
	public Result deleteSubscribedPage(String userId, String href) {
		try {
			String pageHref = this.normalizeHref(href);

			// DBのユーザーからのデータ取得処理
			UserInfoDao userInfoDao = UserInfoDao.getInstance();

			// 指定されたユーザがDBに存在しない場合、エラーメッセージを返す
			if (!userInfoDao.isUserExist(userId)) {
				return KCMessageFactory.createMessage(Result.CODE_SERVER_ERROR, "User Not Found.");
				// 指定されたページがKnowledgeCenterに存在しない場合もエラーメッセージを返す
			} else if (!isTopicExist(pageHref)) {
				return KCMessageFactory.createMessage(Result.CODE_SERVER_ERROR, "Page Not Found.");
				// 指定されたページを購読していない場合もエラーメッセージを返す
			} else if (!userInfoDao.isPageExist(userId, pageHref)) {
				return KCMessageFactory.createMessage(Result.CODE_SERVER_ERROR, "Not Yet Subscribed This Page.");
			}

			com.cloudant.client.api.model.Response res = userInfoDao.delSubscribedPage(userId, pageHref);
			return KCMessageFactory.createMessage(res.getStatusCode(), res.getReason());
		} catch (JSONException e) {
			e.printStackTrace();
			// エラーメッセージを作成
			return KCMessageFactory.createMessage(Result.CODE_SERVER_ERROR, "Internal Server Error.");
		}
	}

	/**
	 * 指定したIDのユーザーを削除する
	 * 
	 * @param userId
	 *            削除対象のユーザーのID
	 * @return 実施結果の成否の入ったオブジェクトをラップしたMessageオブジェクト(何を返すべきか検討の余地あり)
	 */
	public Result deleteUser(String userId) {
		// DBのユーザーからのデータ取得処理
		UserInfoDao userInfoDao = UserInfoDao.getInstance();

		com.cloudant.client.api.model.Response res = userInfoDao.deleteUser(userId);

		Result result = KCMessageFactory.createMessage(res.getStatusCode(), res.getReason());
		return result;
	}

	/**
	 * 購読しているページ一覧を取得し、その中から製品情報を抽出して一覧にして返す
	 * 同じ製品を重複して登録しないためにHashSetで処理した結果をリストに渡している
	 *
	 * @param userId
	 *            製品一覧
	 * @return 購読している製品一覧とユーザーID
	 * @see {@link ResultProductList}
	 */
	public Result getSubscribedProductList(String userId) {
		// DBのユーザーからのデータ取得処理
		UserInfoDao userInfoDao = UserInfoDao.getInstance();

		// 指定されたユーザが見つからなかった場合、エラーメッセージを返す
		if (!userInfoDao.isUserExist(userId)) {
			return KCMessageFactory.createMessage(Result.CODE_SERVER_ERROR, "User Not Found");
		}

		// IDはユニークなはずなので、Listにする必要はない
		List<UserDocument> userList = userInfoDao.getUserList(userId);

		// return用
		Result result = new ResultProductList(userId);

		/*
		 * TODO 現在は購読しているページ一覧を取得しその中から購読している製品一覧を抽出しているが、DBに投げるクエリを調整して、
		 * 直接購読している製品一覧を取得しても良いかもしれない(特に購読件数が増えたときに通信量の低減と速度向上に繋がる)
		 */
		for (UserDocument userDoc : userList) {
			List<SubscribedPage> subscribedPages = userDoc.getSubscribedPages();

			subscribedPages.forEach(entry -> {
				((ResultProductList) result).addSubscribedProduct(entry.getProdId(), entry.getProdName());
			});
		}

		return result;
	}

	/**
	 * 購読したいページとユーザIDを追加し、追加したページを含めた結果を返す
	 *
	 * @param userId
	 *            登録確認対象のユーザーID
	 * @param href
	 *            購読登録するページ
	 * @return 実施結果の成否の入ったオブジェクトをラップしたMessageオブジェクト(何を返すべきか検討の余地あり)
	 */
	public Result registerSubscribedPage(String userId, String href) {
		try {
			String pageHref = this.normalizeHref(href);

			// DBのユーザーからのデータ取得処理
			UserInfoDao userInfoDao = UserInfoDao.getInstance();

			// KCからのデータ取得処理
			TopicMeta topicMeta = getSpecificPageMeta(pageHref);

			// 指定されたユーザがDBに存在しない場合、エラーメッセージを返す
			if (!userInfoDao.isUserExist(userId)) {
				return KCMessageFactory.createMessage(Result.CODE_SERVER_ERROR, "User Not Found.");
			} else if (!topicMeta.isExist()) {
				// 指定されたページがKnowledgeCenterに存在しない場合もエラーメッセージを返す
				return KCMessageFactory.createMessage(Result.CODE_SERVER_ERROR, "Page Not Found.");
			} else if (userInfoDao.isPageExist(userId, pageHref)) {
				// 指定されたページを既に購読している場合もエラーメッセージを返す
				return KCMessageFactory.createMessage(Result.CODE_SERVER_ERROR, "You Already Subscribe This Page.");
			}

			String prodId = topicMeta.getProduct();
			String prodName = this.searchProduct(prodId).getLabel();
			String pageName = this.getPageName(prodId, pageHref);

			com.cloudant.client.api.model.Response res = userInfoDao.setSubscribedPages(userId, pageHref, pageName,
					prodId, prodName);
			return KCMessageFactory.createMessage(res.getStatusCode(), res.getReason());
		} catch (JSONException e) {
			e.printStackTrace();
			// エラーメッセージを作成
			return KCMessageFactory.createMessage(Result.CODE_SERVER_ERROR, "Communication Error");
		}
	}

	/**
	 * 引数のページキーに対応する内容を返す、誤った言語コードの場合にはKCが自動的に英語で返すようになっているが、
	 * もし誤っている場合は日本語にしたい、などの要件が加わった場合はLocaleクラスを使った判定を加える必要がある
	 *
	 * @param href
	 *            検索対象ページキー
	 * @param lang
	 *            言語コード(ISO 639-1)
	 * @see https://docs.oracle.com/javase/8/docs/api/java/util/Locale.html
	 *
	 * @return ページ内容
	 */
	public Result searchContent(String href, String lang) {
		// @see https://jersey.java.net/documentation/latest/client.html
		Client client = ClientBuilder.newClient();
		final String searchUrl = "https://www.ibm.com/support/knowledgecenter/v1/content";

		String pagehref = this.normalizeHref(href);

		WebTarget target;
		// 引数の言語コードを確認し、nullなら言語コード指定なしのパス指定とする
		if (Objects.isNull(lang)) {
			target = client.target(searchUrl).path(pagehref);
		} else {
			target = client.target(searchUrl).path(lang).path(pagehref);
		}

		Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
		Response res = invocationBuilder.get();
		Result result = new ResultContent(res.readEntity(String.class));

		return result;
	}

	/**
	 * キーワード検索、現時点では特段の独自拡張はしていない 検索されたキーワードをログに残してどういうものが多く探されているか調べてもいいかもしれない
	 * 不要なパラメーターに対してはnullを渡せばOK
	 *
	 * @param query
	 *            スペース区切りで複数ワードが与えられた場合はOR検索
	 * @param products
	 *            取得対象の製品ID、カンマ区切りで複数指定可能
	 * @param inurl
	 *            検索対象とするページのURL、カンマ区切りで複数指定可能
	 * @param offset
	 *            検索結果を何件目から取得するか
	 * @param limit
	 *            取得件数、MAXは20
	 * @param lang
	 *            検索結果がサポートしている言語
	 * @param sort
	 *            並び替え、現時点では日付昇順・降順のみAPIでサポートしている、date:aかdate:d以外が来たら指定がなかったものとみなす
	 * @return 検索結果
	 */
	public Result searchPages(String query, String products, String inurl, Integer offset, Integer limit, String lang,
			String sort) {
		// @see https://jersey.java.net/documentation/latest/client.html
		Client client = ClientBuilder.newClient();
		final String searchUrl = "https://www.ibm.com/support/knowledgecenter/v1/search";

		WebTarget target = client.target(searchUrl).queryParam("query", query);

		/*
		 * パラメーターが存在するなら追加する、という処理、
		 * さらにパラメーターが増えるならわかりにくいので、1.パラメーターがあるならMapに追加、2.Mapを回してパラメーターとして追加、
		 * という処理を実装する, queryParamは新しいWebTargetを返すので、Mapの処理を素直にラムダ式では処理できない
		 */
		Map<String, String> queryMap = new HashMap<>();

		Optional.ofNullable(products).ifPresent(_products -> queryMap.put("products", _products));
		Optional.ofNullable(inurl).ifPresent(_inurl -> queryMap.put("inurl", _inurl));
		Optional.ofNullable(offset).ifPresent(_offset -> queryMap.put("offset", _offset.toString()));
		Optional.ofNullable(limit).ifPresent(_limit -> queryMap.put("limit", _limit.toString()));
		Optional.ofNullable(lang).ifPresent(_lang -> queryMap.put("lang", _lang));

		// 不正な日付並び順を指定された場合はnullとしておくことでパラメーターに使われることを防ぐ
		if (!"date:a".equals(sort) && !"date:d".equals(sort)) {
			sort = null;
		}
		Optional.ofNullable(sort).ifPresent(_sort -> queryMap.put("sort", _sort));

		for (Entry<String, String> each : queryMap.entrySet()) {
			target = target.queryParam(each.getKey(), each.getValue());
		}

		Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
		Response res = invocationBuilder.get();
		JSONObject resJson = new JSONObject(res.readEntity(String.class));

		// ページがtopics情報を持つ場合
		if (resJson.has("topics")) {
			int resOffset = resJson.getInt("offset");
			int resNext = resJson.getInt("next");
			int resPrev = resJson.getInt("prev");
			int resCount = resJson.getInt("count");
			int resTotal = resJson.getInt("total");
			List<Topic> resTopics = new ArrayList<>();

			// JSONの中にあるtopicsを読み、1件ずつTopicオブジェクトとして初期化し、リストに追加している
			resJson.getJSONArray("topics").forEach(topic -> resTopics.add(new Topic((JSONObject) topic)));
			Result result = new ResultSearchList(resOffset, resNext, resPrev, resCount, resTotal, resTopics);
			return ((ResultSearchList) result);
		} else {
			return KCMessageFactory.createMessage(Result.CODE_SERVER_ERROR, "Can't get search result");
		}
	}

	/**
	 * 特定のページのページ名を取得する。公開されているbreadcrumb(パンくずリスト)の応答がhrefとlabelである性質を利用している
	 *
	 * @param prodId
	 *            ページ名を確認する対象製品ID
	 * @param pageHref
	 *            ページ名を確認する対象のpageのHref
	 * @return breadcrumbから抽出したページ名
	 */
	private String getPageName(String prodId, String pageHref) throws JSONException {
		// @see https://jersey.java.net/documentation/latest/client.html
		Client client = ClientBuilder.newClient();
		final String topicMetaUrl = "https://www.ibm.com/support/knowledgecenter/v1/breadcrumb";

		WebTarget target = client.target(topicMetaUrl).path(prodId).queryParam("href", pageHref);

		Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
		Response res = invocationBuilder.get();
		JSONObject resJson = new JSONObject(res.readEntity(String.class));

		String pageName = "";
		// breadcrumbの応答は指定したページまでのパス区切りごとのhrefとlabel、一番最後は必ずこの指定したhrefが入るのでその性質を利用してページ名を得ている
		if (resJson.has("breadcrumb")) {
			JSONArray breadCrumb = resJson.getJSONArray("breadcrumb");
			JSONObject targetPage = breadCrumb.getJSONObject((breadCrumb.length() - 1));
			pageName = targetPage.getString("label");
		}
		return pageName;
	}

	/**
	 * KCに問い合わせた結果を、メタ情報を持ったオブジェクトに詰めて応答する
	 *
	 * @param specificHref
	 *            更新の有無を確認する対象のpageのHref
	 * @return topic_metadataから得られた応答を抽出してプロパティとしてセットした{@link TopicMeta}
	 */
	private TopicMeta getSpecificPageMeta(String specificHref) throws JSONException {
		// @see https://jersey.java.net/documentation/latest/client.html
		Client client = ClientBuilder.newClient();
		final String topicMetaUrl = "https://www.ibm.com/support/knowledgecenter/v1/topic_metadata";
		WebTarget target = client.target(topicMetaUrl).queryParam("href", specificHref);

		Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
		Response res = invocationBuilder.get();

		JSONObject resJson = new JSONObject(res.readEntity(String.class));
		return new TopicMeta(resJson);

	}

	/**
	 * 購読ページがKnowledgeCenter上に存在するか確認する、ページが存在しない場合、最終更新日時の値が初期値の-1となって応答される
	 * 複数回参照する場合やページのメタ情報を利用する場合には{@link TopicMeta#isExist()}を利用する
	 *
	 * @param pageHref
	 *            確認する購読ページキー
	 * @return True or False
	 */
	private boolean isTopicExist(String pageHref) {
		try {
			return getSpecificPageMeta(pageHref).isExist();
		} catch (JSONException | NullPointerException e) {
			return false;
		}
	}

	/**
	 * .htmでの登録は行わせず、全て.htmlで登録を行わせるように拡張子を統一する（不正な拡張子はisTopicExist()で弾かれる)
	 * 検索の結果をそのまま使うとsc=_latestがついてしまい、ページ名取得の際の障害になるので排除するための処理
	 * xxx.htm,xxx.htm?sc=_latest,xxx.html?sc=_latestをxxx.htmlに置き換える
	 * 
	 * @param originalHref
	 *            置き換え前のhref
	 * @return 置き換え後のhref
	 */
	private String normalizeHref(String originalHref) {
		String normalizedHref = originalHref
				.replaceFirst("\\.htm$" + "|\\.htm\\?sc=_latest$" + "|\\.html\\?sc=_latest$", "\\.html");
		return normalizedHref;
	}

	/**
	 * 特定ページが属する製品をKCから取得する
	 *
	 * @param productKey
	 *            特定ページ
	 * @return 得られたJSONを元に生成した{@link Product}オブジェクト | null
	 */
	private Product searchProduct(String productKey) {
		// @see https://jersey.java.net/documentation/latest/client.html
		Client client = ClientBuilder.newClient();
		final String searchUrl = "https://www.ibm.com/support/knowledgecenter/v1/products/";

		WebTarget target = client.target(searchUrl + productKey);

		Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
		Response res = invocationBuilder.get();

		JSONObject resJson = new JSONObject(res.readEntity(String.class));

		// ページがproduct情報を持つ場合
		if (resJson.has("product")) {
			return new Product(resJson.getJSONObject("product"));
		} else {
			return null;
		}
	}

}
