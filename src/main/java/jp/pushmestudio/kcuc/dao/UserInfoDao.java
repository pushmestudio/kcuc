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
	}

	/**
	 * 指定したページを購読しているユーザーを返す
	 * 
	 * @param pageHref
	 *            購読ページ
	 * @return 指定した購読ページを購読しているユーザー
	 */
	public List<UserDocument> getSubscribedUserList(String pageHref) {
		// pageHrefのインデックスを使用して、指定されたページキーを購読しているユーザのデータを取得
		List<UserDocument> userDocs = kcucDB.findByIndex(
				"{\"selector\":{\"subscribedPages\":{\"$elemMatch\":{\"pageHref\":\"" + pageHref + "\"}}}}",
				UserDocument.class);

		return userDocs;
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

		// TODO 本来であればupdateは1つのレコードに対してのみ実施されるため、返り値をListにする必要はない。
		// UserDocument updatedInfo = kcucDB.find(UserDocument.class,
		// responseUpdate.getId());

		List<UserDocument> updatedInfo = kcucDB.findByIndex("{\"selector\":{\"userName\":\"" + userId + "\"}}",
				UserDocument.class);

		return updatedInfo;
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
