package jp.pushmestudio.kcuc.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;

import jp.pushmestudio.kcuc.model.SubscribedPage;
import jp.pushmestudio.kcuc.model.UserDocument;

public class UserInfoDao {

	private Database kcucDB;

	// コンストラクタの生成時にCloudantへ接続
	public UserInfoDao() {
		final String accountProp = "CLOUDANT_ACCOUNT";
		final String userProp = "CLOUDANT_USER";
		final String pwProp = "CLOUDANT_PW";
		final String cloudantPropPath = "/jp/pushmestudio/credentials/cloudant.properties";

		String envAccount = "";
		String envUser = "";
		String envPw = "";
		// 環境変数から値を取得する
		try {
			envAccount = System.getenv(accountProp);
			envUser = System.getenv(userProp);
			envPw = System.getenv(pwProp);
		} catch (NullPointerException | SecurityException e) {
			e.printStackTrace();
		}

		// 環境変数から値を取れていない場合にはローカルのプロパティファイルを読み込む
		if (Objects.isNull(envAccount) || envAccount.length() <= 0 || Objects.isNull(envUser) || envUser.length() <= 0
				|| Objects.isNull(envPw) || envPw.length() <= 0) {
			Properties CLOUDANT_CONFIG = this.loadProperty(cloudantPropPath);
			envAccount = (String) CLOUDANT_CONFIG.get(accountProp);
			envUser = (String) CLOUDANT_CONFIG.get(userProp);
			envPw = (String) CLOUDANT_CONFIG.get(pwProp);
		}

		// Cloudantのインスタンスを作成
		CloudantClient cldClient = ClientBuilder.account(envAccount).username(envUser).password(envPw).build();

		// Databaseのインスタンスを取得
		this.kcucDB = cldClient.database("kcucdb", false);
	}

	/**
	 * 指定したIDのユーザーを返す
	 * 
	 * @param userId
	 *            探す対象となるユーザーのID
	 * @return DBから取得した、IDに該当するユーザーの情報
	 */
	public List<UserDocument> getUserList(String userId) {
		/*
		 * 20161222 接続先をCloudantに移行 public List<UserInfo> getUserList(String
		 * searchId) { List<UserInfo> userList = new ArrayList<>();
		 */

		// userNameのインデックスを使用して、指定されたユーザ名に一致するユーザのデータを取得
		List<UserDocument> userDocs = kcucDB.findByIndex("{\"selector\":{\"userName\":\"" + userId + "\"}}",
				UserDocument.class);

		return userDocs;

		/*
		 * 20161222 接続先をCloudantに移行 // TODO
		 * ここでDBに対してselect処理などを実施する、今はダミーの値を使用する DummyStatement stmt = new
		 * DummyStatement(); List<List<Object>> rs =
		 * stmt.executeQueryUser(userId);
		 * 
		 * for (int i = 0; i < rs.size(); i++) { List<Object> gotUser =
		 * rs.get(i); String id = (String) gotUser.get(0); String password =
		 * (String) gotUser.get(1);
		 * 
		 * @SuppressWarnings("unchecked") Map<String, Long> subscribedPages =
		 * (Map<String, Long>) gotUser.get(2);
		 * 
		 * UserInfo userInfo = new UserInfo(id, password, subscribedPages);
		 * userList.add(userInfo); }
		 * 
		 * return userList;
		 */
	}

	/**
	 * 指定したページを購読しているユーザーを返す
	 * 
	 * @param pageHref
	 *            購読ページ
	 * @return 指定した購読ページを購読しているユーザー
	 */
	public List<UserDocument> getSubscribedUserList(String pageHref) {
		/*
		 * 20161222 接続先をCloudantに移行 public List<UserInfo>
		 * getSubscribedUserList(String pageHref) { List<UserInfo> userList =
		 * new ArrayList<>();
		 */

		// pageHrefのインデックスを使用して、指定されたページキーを購読しているユーザのデータを取得
		List<UserDocument> userDocs = kcucDB.findByIndex(
				"{\"selector\":{\"subscribedPages\":{\"$elemMatch\":{\"pageHref\":\"" + pageHref + "\"}}}}",
				UserDocument.class);

		return userDocs;

		/*
		 * 20161222 接続先をCloudantに移行 // TODO
		 * ここでDBに対してselect処理などを実施する、今はダミーの値を使用する DummyStatement stmt = new
		 * DummyStatement(); List<List<Object>> rs =
		 * stmt.executeQueryPage(pageHref);
		 * 
		 * for (int i = 0; i < rs.size(); i++) { List<Object> gotUser =
		 * rs.get(i); String id = (String) gotUser.get(0); String password =
		 * (String) gotUser.get(1);
		 * 
		 * @SuppressWarnings("unchecked") Map<String, Long> subscribedPages =
		 * (Map<String, Long>) gotUser.get(2);
		 * 
		 * UserInfo userInfo = new UserInfo(id, password, subscribedPages);
		 * userList.add(userInfo); }
		 * 
		 * return userList;
		 */
	}

	/**
	 * ユーザの購読ページを登録する
	 * 
	 * @param userID
	 *            登録するユーザのID
	 * @param pageHref
	 *            購読ページ
	 * @return 指定したユーザの情報一覧（ページ追加後）
	 */
	public List<UserDocument> setSubscribedPages(String userId, String pageHref) {
		/*
		 * public List<UserInfo> setSubscribedPages(String userID, String
		 * pageHref) { // return用 List<UserInfo> userList = new ArrayList<>();
		 */

		// useNameのインデックスを使用して、指定されたユーザのデータを取得
		List<UserDocument> userDocs = kcucDB.findByIndex("{\"selector\":{\"userName\":\"" + userId + "\"}}",
				UserDocument.class);

		// 追加するページの情報を作成
		Date currentTime = new Date();
		String timestamp = Long.toString(currentTime.getTime());
		SubscribedPage targetPage = new SubscribedPage(pageHref, false, timestamp, "unknown");

		// 指定されたユーザに該当するレコードを更新
		UserDocument updateTarget = kcucDB.find(UserDocument.class, userDocs.get(0).getId());
		updateTarget.addSubscribedPages(targetPage);
		kcucDB.update(updateTarget);
		// 1レコードに限定した場合に使用予定
		// Response responseUpdate = kcucDB.update(updateTarget);

		// Todo 本来であればupdateは1つのレコードに対してのみ実施されるため、返り値をListにする必要はない。
		// UserDocument updatedInfo = kcucDB.find(UserDocument.class,
		// responseUpdate.getId());

		List<UserDocument> updatedInfo = kcucDB.findByIndex("{\"selector\":{\"userName\":\"" + userId + "\"}}",
				UserDocument.class);

		return updatedInfo;

		/*
		 * 20161222 接続先をCloudantに移行 // TODO
		 * ここでDBに対してupdate処理などを実施する、今はダミーの値を使用する DummyStatement stmt = new
		 * DummyStatement(); List<List<Object>> rs =
		 * stmt.executeUpdatePage(userID, pageHref);
		 * 
		 * for (int i = 0; i < rs.size(); i++) { List<Object> gotUser =
		 * rs.get(i); String id = (String) gotUser.get(0); String password =
		 * (String) gotUser.get(1);
		 * 
		 * @SuppressWarnings("unchecked") Map<String, Long> subscribedPages =
		 * (Map<String, Long>) gotUser.get(2);
		 * 
		 * UserInfo userInfo = new UserInfo(id, password, subscribedPages);
		 * userList.add(userInfo); }
		 * 
		 * return userList;
		 */
	}

	/**
	 * ユーザがDBに存在するか確認する
	 * 
	 * @param userId
	 *            確認するユーザ名
	 * @return True or False
	 */
	public Boolean isUserExist(String userId) {
		List<UserDocument> userDocs = kcucDB.findByIndex("{\"selector\":{\"userName\":\"" + userId + "\"}}",
				UserDocument.class);
		return userDocs.size() > 0 ? true : false;
	}

	/**
	 * 指定されたユーザの購読ページが既にDBに存在するか確認する
	 * 
	 * @param userId
	 *            確認するユーザ名
	 * @param pageHref
	 *            確認する購読ページキー
	 * @return True or False
	 */
	public Boolean isPageExist(String userId, String pageHref) {
		List<UserDocument> userDocs = kcucDB.findByIndex(
				"{\"selector\":{\"$and\":[{\"userName\":\"" + userId
						+ "\"},{\"subscribedPages\":{\"$elemMatch\":{\"pageHref\":\"" + pageHref + "\"}}}]}}",
				UserDocument.class);
		return userDocs.size() > 0 ? true : false;
	}

	// 20161222 接続先をCloudantに移行
	// class DummyStatement {
	// // DBからデータを取得することを擬制するために、executeQueryの名前で取得
	//
	// /**
	// * 想定している実行SQL(RDBの場合)のイメージは次のとおり。
	// * <code>SELECT * FROM user_table WHERE user_id = '$searchId'</code>
	// *
	// * @param searchId
	// * 情報取得対象となるユーザーのID
	// * @return 指定したIDを持つユーザーを含むResultSet
	// */
	// public List<List<Object>> executeQueryUser(String userId) {
	// Objects.requireNonNull(searchId);
	// List<List<Object>> dummyResultSet = new ArrayList<>();
	//
	// List<List<Object>> dummyUserTable = new
	// DummyUserTable().getUserTableData();
	// for (int i = 0; i < dummyUserTable.size(); i++) {
	// List<Object> dummyData = dummyUserTable.get(i);
	//
	// // dummyData.get(0) = ユーザーID
	// if (searchId.equals((String) dummyData.get(0))) {
	// dummyResultSet.add(dummyData);
	// }
	// }
	//
	// return dummyResultSet;
	// }
	//
	// /**
	// * 想定している実行SQL(RDBの場合)のイメージは次のとおり。<code>SELECT * FROM user_table WHERE
	// * pageHref = '$pageHref'</code>
	// * pageHrefには複数の値を持つことになるので、テーブル構造などを踏まえ、WHEREの仕方は考える必要がある
	// *
	// * @param pageHref
	// * ユーザーを取得するためのキーとなる購読ページ
	// * @return 指定したページを購読しているユーザーを含むResultSet
	// */
	// public List<List<Object>> executeQueryPage(String pageHref) {
	// Objects.requireNonNull(pageHref);
	// List<List<Object>> dummyResultSet = new ArrayList<>();
	//
	// List<List<Object>> dummyUserTable = new
	// DummyUserTable().getUserTableData();
	// for (int i = 0; i < dummyUserTable.size(); i++) {
	// List<Object> dummyData = dummyUserTable.get(i);
	//
	// // dummyData.get(2) = 購読中のページと最後に確認した時間
	// @SuppressWarnings("unchecked")
	// HashMap<String, Long> subscribedPages = (HashMap<String, Long>)
	// dummyData.get(2);
	//
	// // 購読ページ一覧の中にpageHrefがないユーザーはリストに足さない
	// if (Objects.isNull(subscribedPages.get(pageHref))) {
	// continue;
	// } else {
	// dummyResultSet.add(dummyData);
	// }
	// }
	//
	// return dummyResultSet;
	// }
	//
	// /**
	// * 想定している実行SQL(RDBの場合)のイメージは次のとおり。
	// * <code>UPDATE user_table SET pageHref =
	// * '$pageHrefで指定した値' WHERE user_id = '$userId'</code>
	// * pageHrefには複数の値を持つことになるので、テーブル構造などを踏まえ、SETの仕方は考える必要がある
	// *
	// * @param userId
	// * 購読ページを更新する対象のユーザーのID
	// * @param pageHref
	// * 購読ページとして追加する対象のページ
	// * @return 購読ページを追加したユーザーオブジェクトを含むResultSet
	// */
	// public List<List<Object>> executeUpdatePage(String userId, String
	// pageHref) {
	// Objects.requireNonNull(userId);
	// Objects.requireNonNull(pageHref);
	// List<List<Object>> dummyResultSet = new ArrayList<>();
	//
	// List<List<Object>> dummyUserTable = new
	// DummyUserTable().getUserTableData();
	// for (int i = 0; i < dummyUserTable.size(); i++) {
	// List<Object> dummyData = dummyUserTable.get(i);
	//
	// // dummyData.get(0) = ユーザーID
	// if (userId.equals((String) dummyData.get(0))) {
	//
	// // dummyData.get(2) = 購読中のページと最後に確認した時間
	// @SuppressWarnings("unchecked")
	// Map<String, Long> subscribedPages = (Map<String, Long>) dummyData.get(2);
	// // 時間はダミー、Wed Nov 16 14:30:00 2016 JST
	// subscribedPages.put(pageHref, 1479274200137L);
	// dummyResultSet.add(dummyData);
	//
	// break; // ユーザーIDはユニークの想定なので、1件合致したらそれ以上処理は必要ない
	// }
	// }
	//
	// return dummyResultSet;
	// }
	// }
	//
	// // DBからデータを取得したときとできるかぎりイメージを近づけるため、オブジェクト型のリストにしている
	// // rs.getString(1)で取得するのと同じような形にできるので状況的には近しくなる
	// class DummyUserTable {
	// public List<List<Object>> getUserTableData() {
	// // ダミーユーザー1
	// List<Object> dummyUser1 = new ArrayList<>();
	// String dummy1Id = "tkhm";
	// String dummy1Password = "pass";
	// Map<String, Long> dummy1SubscribedPages = new HashMap<>();
	// dummy1SubscribedPages.put("SSMTU9/welcometoibmverse.html",
	// 1469114812137L);
	// dummyUser1.add(dummy1Id);
	// dummyUser1.add(dummy1Password);
	// dummyUser1.add(dummy1SubscribedPages);
	//
	// // ダミーユーザー2
	// List<Object> dummyUser2 = new ArrayList<>();
	// String dummy2Id = "capsmalt";
	// String dummy2Password = "word";
	// Map<String, Long> dummy2SubscribedPages = new HashMap<>();
	// dummyUser2.add(dummy2Id);
	// dummyUser2.add(dummy2Password);
	// dummyUser2.add(dummy2SubscribedPages);
	//
	// List<List<Object>> userTable = new ArrayList<>();
	// userTable.add(dummyUser1);
	// userTable.add(dummyUser2);
	// return userTable;
	// }
	// }

	/**
	 * 指定されたファイル名をクラスパスから探し出し、プロパティとして読み込んで返す
	 * {@code loadProperty("/jp/pushmestudio/credentials/cloudant.properties");}
	 * 
	 * @param fileName
	 *            探索対象のファイル名
	 * @return 探索対象のファイルを読み込んだプロパティ
	 */
	private Properties loadProperty(String fileName) {
		Properties props = new Properties();
		try (InputStream input = getClass().getResourceAsStream(fileName)) {
			props.load(input);
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return props;
	}
}
