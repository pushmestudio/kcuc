package jp.pushmestudio.kcuc.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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

	/*
	 * シングルトンパターン使用(private staticなインスタンス、privateなコンストラクタ、public
	 * staticなgetInstance)
	 * DB接続回数を抑えるための施策で、CloudantにおいてはRDBのようなコネクション維持について考慮しなくて良いのでこのような作りにしている
	 * マルチスレッドに対応できないので拡張が進んだ際には別途対応を検討か
	 */
	private static UserInfoDao singleton;

	private UserInfoDao(Database kcucDB) {
		this.kcucDB = kcucDB;
	};

	public static UserInfoDao getInstance() {
		if (Objects.isNull(singleton)) {
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
			if (Objects.isNull(envAccount) || envAccount.length() <= 0 || Objects.isNull(envUser)
					|| envUser.length() <= 0 || Objects.isNull(envPw) || envPw.length() <= 0) {
				Properties cloudantConfig = loadProperty(cloudantPropPath);
				envAccount = (String) cloudantConfig.get(accountProp);
				envUser = (String) cloudantConfig.get(userProp);
				envPw = (String) cloudantConfig.get(pwProp);
			}

			// Cloudantのインスタンスを作成
			CloudantClient cldClient = ClientBuilder.account(envAccount).username(envUser).password(envPw).build();

			// Databaseのインスタンスを取得
			Database dbInstance = cldClient.database("kcucdb", false);
			singleton = new UserInfoDao(dbInstance);
		}

		return singleton;
	}

	// CREATE - CRUD

	// READ - CRUD

	/**
	 * 指定したIDのユーザーとその購読ページを返す
	 * 
	 * @param userId
	 *            探す対象となるユーザーのID
	 * @return DBから取得した、IDに該当するユーザーの情報
	 */
	public List<UserDocument> getUserList(String userId) {
		// userIdのインデックスを使用して、指定されたユーザ名に一致するユーザのデータを取得
		List<UserDocument> userDocs = kcucDB.findByIndex("{\"selector\":{\"userId\":\"" + userId + "\"}}",
				UserDocument.class);

		return userDocs;
	}

	/**
	 * 指定したIDのユーザーとその購読ページを返す、結果を絞る方法としてはDBへのクエリで絞る方法と、受け取った結果をAPIサーバー上で絞る方法がある
	 * 現在は簡潔・簡便な後者の方法を取っているが、クエリによる絞り込みにすることも検討の余地がある
	 * 
	 * @param userId
	 *            探す対象となるユーザーのID
	 * @param prodId
	 *            探す対象となる製品のID
	 * @return DBから取得した、IDに該当するユーザーの、特定製品に限定したページ情報
	 */
	public List<UserDocument> getUserList(String userId, String prodId) {
		// userIdのインデックスを使用して、指定されたユーザ名に一致するユーザのデータを取得
		List<UserDocument> userDocs = this.getUserList(userId);
		List<SubscribedPage> specificProdPages = new ArrayList<>();

		for (UserDocument userDoc : userDocs) {
			// ユーザードキュメントから購読製品を取り出してIDが一致するものだけを取り出す
			List<SubscribedPage> subscribedPages = userDoc.getSubscribedPages();
			for (SubscribedPage page : subscribedPages) {
				if (prodId.equals(page.getProdId())) {
					specificProdPages.add(page);
				}
			}
			// 取り出した製品を絞った購読製品リストを使って既存の購読製品リストを置き換える
			userDoc.replaceSubscribedPages(specificProdPages);
		}

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
	 * ユーザがDBに存在するか確認する
	 * 
	 * @param userId
	 *            確認するユーザ名
	 * @return True or False
	 */
	public boolean isUserExist(String userId) {
		List<UserDocument> userDocs = this.getUserList(userId);
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
	public boolean isPageExist(String userId, String pageHref) {
		List<UserDocument> userDocs = kcucDB.findByIndex(
				"{\"selector\":{\"$and\":[{\"userId\":\"" + userId
						+ "\"},{\"subscribedPages\":{\"$elemMatch\":{\"pageHref\":\"" + pageHref + "\"}}}]}}",
				UserDocument.class);
		return userDocs.size() > 0 ? true : false;
	}

	// UPDATE - CRUD

	/**
	 * ユーザの購読ページを登録する
	 * 
	 * @param userId
	 *            登録するユーザのID
	 * @param pageHref
	 *            購読ページ
	 * @return 指定したユーザの情報一覧（ページ追加後）
	 */
	public List<UserDocument> setSubscribedPages(String userId, String pageHref, String pageName, String prodId,
			String prodName) {
		// useNameのインデックスを使用して、指定されたユーザのデータを取得
		List<UserDocument> userDocs = this.getUserList(userId);

		// 追加するページの情報を作成
		Date currentTime = new Date();
		long timestamp = currentTime.getTime();
		SubscribedPage targetPage = new SubscribedPage(pageHref, pageName, false, timestamp, prodId, prodName);

		// 指定されたユーザに該当するレコードを更新
		UserDocument updateTarget = kcucDB.find(UserDocument.class, userDocs.get(0).getId());
		updateTarget.addSubscribedPages(targetPage);
		kcucDB.update(updateTarget);
		// 1レコードに限定した場合に使用予定
		// Response responseUpdate = kcucDB.update(updateTarget);

		// TODO 本来であればupdateは1つのレコードに対してのみ実施されるため、返り値をListにする必要はない。
		// UserDocument updatedInfo = kcucDB.find(UserDocument.class,
		// responseUpdate.getId());

		List<UserDocument> updatedInfo = this.getUserList(userId);

		return updatedInfo;
	}

	/**
	 * ユーザの購読ページを解除する
	 * 
	 * @param userId
	 *            対象ユーザのID
	 * @param pageHref
	 *            購読解除ページ
	 * @return 指定したユーザの情報一覧（ページ追加後）// 購読解除したページ情報のみをレスポンスとしても良い気もする
	 */
	public List<UserDocument> delSubscribedPage(String userId, String pageHref) {
		// userIdとpageHrefで指定されたユーザのデータを取得
		List<UserDocument> userDocs = kcucDB.findByIndex(
				"{\"selector\":{\"$and\":[{\"userId\":\"" + userId
						+ "\"},{\"subscribedPages\":{\"$elemMatch\":{\"pageHref\":\"" + pageHref + "\"}}}]}}",
				UserDocument.class);
		
		UserDocument updateTarget;
		Iterator<UserDocument> i = userDocs.iterator();
		while (i.hasNext()) {
			UserDocument userDoc = i.next();
			if (userDoc.getUserId().equals(userId)) {
				updateTarget = userDoc;
				int target = 0;
				for (SubscribedPage targetHref : updateTarget.getSubscribedPages()) {
					if (targetHref.getPageHref().equals(pageHref)) {
						break;
					}
					target++;
				}
				// 対象ページの購読解除
				updateTarget.delSubscribedPage(target);
				kcucDB.update(updateTarget);
				// 購読解除前のUserDocumentを削除
				i.remove();
				// 購読解除を反映したUserDocumentをuserDocs(Return用)に追加
				userDocs.add(updateTarget);
				break;
			}
		}
		return userDocs;
	}

	/**
	 * ユーザーの購読しているページのうち、特定の製品IDを持つものをまとめて購読解除する
	 * 
	 * @param userId
	 *            対象ユーザーのID
	 * @param prodId
	 *            対象製品のID
	 * @return 購読解除後の購読済みページ一覧
	 */
	public List<UserDocument> cancelSubscribedProduct(String userId, String prodId) throws IndexOutOfBoundsException {
		// userIdとpageHrefで指定されたユーザのデータを取得
		List<UserDocument> userDocs = this.getUserList(userId, prodId);

		// 指定したユーザが購読中のページ内で，解除対象ページの配列番号を調べる
		UserDocument updateTarget = kcucDB.find(UserDocument.class, userDocs.get(0).getId());
		int targetIndex = 0; // 購読ページ数(配列数)を超えるとjava.lang.ArrayIndexOutOfBoundsExceptionになるが，そもそもdelSubscribedPage()が呼ばれないので例外処理はしていない
		List<Integer> targetIndexList = new ArrayList<>();
		for (SubscribedPage targetPage : updateTarget.getSubscribedPages()) {
			if (targetPage.getProdId().equals(prodId)) {
				targetIndexList.add(targetIndex);
				continue;
			}
			/*
			 * 該当しない時だけ数字をプラスする。実装の意図としては、配列の[0,1,2,3,4]の
			 * 0,3,4だけを消したい場合、順に消していくと [1,2,3,4] -> [1,2,4] ->
			 * [1,2]}のように消えていくこととなる。そのため、例えばすべて消すときはindexは常に0になる。
			 * そのため、該当しない時だけindexの値を増やすことで効率的に該当する対象のみを消す処理を書いている。
			 */
			targetIndex++;
		}

		// 対象ページの購読解除
		for (int eachTarget : targetIndexList) {
			updateTarget.delSubscribedPage(eachTarget);
		}

		// Cloudant上のユーザ購読情報(Document)を更新
		kcucDB.update(updateTarget);

		// 更新後の購読ページ情報をCloudantから取得
		List<UserDocument> updatedInfo = this.getUserList(userId);
		return updatedInfo;
	}

	/**
	 * 指定されたファイル名をクラスパスから探し出し、プロパティとして読み込んで返す
	 * {@code loadProperty("/jp/pushmestudio/credentials/cloudant.properties");}
	 * 
	 * @param fileName
	 *            探索対象のファイル名
	 * @return 探索対象のファイルを読み込んだプロパティ
	 */
	private static Properties loadProperty(String fileName) {
		Properties props = new Properties();
		try (InputStream input = UserInfoDao.class.getResourceAsStream(fileName)) {
			props.load(input);
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return props;
	}
}
