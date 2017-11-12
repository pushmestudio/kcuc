package jp.pushmestudio.kcuc.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.api.model.Response;
import com.cloudant.client.org.lightcouch.TooManyRequestsException;

import jp.pushmestudio.kcuc.model.SubscribedPage;
import jp.pushmestudio.kcuc.model.UserDocument;

/**
 * シングルトンパターンを使用(private staticなインスタンス、privateなコンストラクタ、public
 * staticなgetInstance)しているため、インスタンス生成には {@link #getInstance()}を使うこと<br>
 * CRUDの順番に記載しており、Read以外についてはCloudantの応答をそのまま返している(検討の余地あり)<br>
 * TODO ネットワークエラーなどで接続に失敗すると java.net.UnknownHostException,
 * java.net.ConnectException,
 * com.cloudant.client.org.lightcouch.CouchDbExceptionなどが起きうるがどこまで対処するか
 */
public class UserInfoDao {

	private Database kcucDB;

	private static UserInfoDao singleton;

	private UserInfoDao(Database kcucDB) {
		this.kcucDB = kcucDB;
	}

	/**
	 * DB接続回数を抑えるための施策としてシングルトンパターンを使用している
	 * CloudantにおいてはRDBのようなコネクション維持について考慮しなくて良いのでこのような作りにしているが
	 * マルチスレッドに対応できないので拡張が進んだ際には別途対応を検討か
	 * 
	 * @return 生成済インスタンス or インスタンス生成結果
	 */
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
	/**
	 * 指定したIDのユーザーとその購読ページを返す
	 * 
	 * @param userId
	 *            探す対象となるユーザーのID
	 * @return ユーザー作成結果
	 */
	public Response createUser(String userId) {
		UserDocument newUser = new UserDocument(userId);
		Response res;
		try {
			// リトライ向けに2回書いているので修正時はどちらも直すこと
			res = kcucDB.save(newUser);
		} catch (TooManyRequestsException e) {
			// 回数制限に引っかかったら記録を残した上で1秒後に1度だけリトライする
			e.printStackTrace();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ee) {
				ee.printStackTrace();
			}
			// DBへの更新処理
			res = kcucDB.save(newUser);
			// ここをres = this.createUser(userId)にすれば再帰呼び出しで成功するまでリトライし続ける
		}
		return res;
	}

	// READ - CRUD

	/**
	 * 指定したIDのユーザーとその購読ページを返す、IDは一意なはずなのでリストではなくていいはず、変更の余地あり
	 * 
	 * @param userId
	 *            探す対象となるユーザーのID
	 * @return DBから取得した、IDに該当するユーザーの情報
	 */
	public List<UserDocument> getUserList(String userId) {
		// userIdのインデックスを使用して、指定されたユーザ名に一致するユーザのデータを取得
		List<UserDocument> userDocs;
		try {
			// リトライ向けに2回書いているので修正時はどちらも直すこと
			userDocs = kcucDB.findByIndex("{\"selector\":{\"userId\":\"" + userId + "\"}}", UserDocument.class);
		} catch (TooManyRequestsException e) {
			// 回数制限に引っかかったら記録を残した上で1秒後に1度だけリトライする
			e.printStackTrace();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ee) {
				ee.printStackTrace();
			}
			userDocs = kcucDB.findByIndex("{\"selector\":{\"userId\":\"" + userId + "\"}}", UserDocument.class);
			// ここをuserDocs = this.getUserList(userId)にすれば再帰呼び出しで成功するまでリトライし続ける
		}

		return userDocs;
	}

	/**
	 * 指定したIDのユーザーとその購読ページを返す、結果を絞る方法としてはDBへのクエリで絞る方法と、受け取った結果をAPIサーバー上で絞る方法がある<br>
	 * 現在は簡潔・簡便な後者の方法を取っているが、クエリによる絞り込みにすることも検討の余地がある<br>
	 * なお、このメソッドにより取得した結果を使ってupdateすると、update時に使用したデータで上書きするCloudantの仕様上、他の購読情報は消えるので注意
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
		List<UserDocument> userDocs;

		try {
			// リトライ向けに2回書いているので修正時はどちらも直すこと
			userDocs = kcucDB.findByIndex(
					"{\"selector\":{\"subscribedPages\":{\"$elemMatch\":{\"pageHref\":\"" + pageHref + "\"}}}}",
					UserDocument.class);
		} catch (TooManyRequestsException e) {
			// 回数制限に引っかかったら記録を残した上で1秒後に1度だけリトライする
			e.printStackTrace();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ee) {
				ee.printStackTrace();
			}
			userDocs = kcucDB.findByIndex(
					"{\"selector\":{\"subscribedPages\":{\"$elemMatch\":{\"pageHref\":\"" + pageHref + "\"}}}}",
					UserDocument.class);
			// ここをuserDocs =
			// this.getSubscribedUserList(pageHref)にすれば再帰呼び出しで成功するまでリトライし続ける
		}

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
		List<UserDocument> userDocs;
		try {
			// リトライ向けに2回書いているので修正時はどちらも直すこと
			userDocs = kcucDB.findByIndex(
					"{\"selector\":{\"$and\":[{\"userId\":\"" + userId
							+ "\"},{\"subscribedPages\":{\"$elemMatch\":{\"pageHref\":\"" + pageHref + "\"}}}]}}",
					UserDocument.class);
		} catch (TooManyRequestsException e) {
			// 回数制限に引っかかったら記録を残した上で1秒後に1度だけリトライする
			e.printStackTrace();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ee) {
				ee.printStackTrace();
			}
			userDocs = kcucDB.findByIndex(
					"{\"selector\":{\"$and\":[{\"userId\":\"" + userId
							+ "\"},{\"subscribedPages\":{\"$elemMatch\":{\"pageHref\":\"" + pageHref + "\"}}}]}}",
					UserDocument.class);
			// ここをuserDocs =
			// this.isPageExist(userId, pageHref)にすれば再帰呼び出しで成功するまでリトライし続ける
		}
		return userDocs.size() > 0 ? true : false;
	}

	/**
	 * 指定されたユーザの購読製品が既にDBに存在するか確認する
	 * 
	 * @param userId
	 *            確認するユーザ名
	 * @param prodId
	 *            確認する購読製品ID
	 * @return True or False
	 */
	public boolean isProductExist(String userId, String prodId) {
		List<UserDocument> userDocs;
		try {
			// リトライ向けに2回書いているので修正時はどちらも直すこと
			userDocs = kcucDB.findByIndex(
					"{\"selector\":{\"$and\":[{\"userId\":\"" + userId
							+ "\"},{\"subscribedPages\":{\"$elemMatch\":{\"prodId\":\"" + prodId + "\"}}}]}}",
					UserDocument.class);
		} catch (TooManyRequestsException e) {
			// 回数制限に引っかかったら記録を残した上で1秒後に1度だけリトライする
			e.printStackTrace();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ee) {
				ee.printStackTrace();
			}
			userDocs = kcucDB.findByIndex(
					"{\"selector\":{\"$and\":[{\"userId\":\"" + userId
							+ "\"},{\"subscribedPages\":{\"$elemMatch\":{\"prodId\":\"" + prodId + "\"}}}]}}",
					UserDocument.class);
			// ここをuserDocs =
			// this.isProductExist(userId, prodId)にすれば再帰呼び出しで成功するまでリトライし続ける
		}
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
	 * @param pageName
	 *            購読ページ名
	 * @param prodId
	 *            購読ページ製品ID
	 * @param prodName
	 *            購読ページ製品名
	 * @return 登録結果
	 */
	public Response setSubscribedPages(String userId, String pageHref, String pageName, String prodId,
			String prodName) {
		// useNameのインデックスを使用して、指定されたユーザのデータを取得
		List<UserDocument> userDocs = this.getUserList(userId);

		Response res = new Response();

		// 追加するページの情報を作成
		SubscribedPage targetPage = new SubscribedPage(pageHref, pageName, prodId, prodName);

		UserDocument updateTarget;
		Iterator<UserDocument> it = userDocs.iterator();
		// kcucDB.findにてマッチするターゲットを見つけていたものをIteratorに置き換え
		while (it.hasNext()) {
			UserDocument userDoc = it.next();
			if (userDoc.getUserId().equals(userId)) {
				updateTarget = userDoc;

				// 指定されたユーザに該当するレコードを更新
				updateTarget.addSubscribedPages(targetPage);

				try {
					// DBへのアップデート処理
					// リトライ向けに2回書いているので修正時はどちらも直すこと
					res = kcucDB.update(updateTarget);
				} catch (TooManyRequestsException e) {
					// 回数制限に引っかかったら記録を残した上で1秒後に1度だけリトライする
					e.printStackTrace();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException ee) {
						ee.printStackTrace();
					}
					res = kcucDB.update(updateTarget);
					// ここをres = setSubscribedPages(userId, pageHref, pageName,
					// prodId, prodName)にすれば再帰呼び出しで成功するまでリトライし続ける
				}

				break;
			}
		}
		return res;
	}

	/**
	 * ユーザの購読ページを解除する
	 * 
	 * @param userId
	 *            対象ユーザのID
	 * @param hrefs
	 *            購読解除ページ
	 * @return 解除結果
	 */
	public Response cancelSubscribedPages(String userId, List<String> hrefs) {
		List<UserDocument> userDocs = this.getUserList(userId);
		Response res = new Response();
		UserDocument updateTarget;
		Iterator<UserDocument> it = userDocs.iterator();

		// 指定したユーザが購読中のページ内で，解除対象ページの配列番号を調べる
		// kcucDB.findにてマッチするターゲットを見つけていたものをIteratorに置き換え
		while (it.hasNext()) {
			UserDocument userDoc = it.next();
			if (userDoc.getUserId().equals(userId)) {
				updateTarget = userDoc;

				// 解除対象ページの配列番号を，指定したユーザが購読中のページ内から探して，SubscribedPagesからremoveする
				for (String page : hrefs) {
					int target = 0; // getSubscribedPages()した時点での購読中のページ数(解除するための配列番号を特定するために使用)
					for (SubscribedPage targetHref : updateTarget.getSubscribedPages()) {
						if (targetHref.getPageHref().equals(page)) {
							updateTarget.removeSubscribedPage(target);
							break;
						}
						target++;
					}
				}
				// Cloudant上のユーザ購読情報(Document)を更新
				try {
					// DBへのアップデート処理
					// リトライ向けに2回書いているので修正時はどちらも直すこと
					res = kcucDB.update(updateTarget);
				} catch (TooManyRequestsException e) {
					// 回数制限に引っかかったら記録を残した上で1秒後に1度だけリトライする
					e.printStackTrace();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException ee) {
						ee.printStackTrace();
					}
					res = kcucDB.update(updateTarget);
					// ここをres = delSubscribedPages(String userId, List<String> hrefs) にすれば再帰呼び出しで成功するまでリトライし続ける
				}
				break;
			}
		}
		return res;
	}
	
	/**
	 * ユーザーの購読しているページのうち、特定の製品IDを持つものをまとめて購読解除する
	 * 
	 * @param userId
	 *            対象ユーザーのID
	 * @param prodId
	 *            対象製品のID
	 * @return 解除結果
	 */
	public Response cancelSubscribedProduct(String userId, String prodId) throws IndexOutOfBoundsException {
		// userIdで指定されたユーザのデータを取得
		List<UserDocument> userDocs = this.getUserList(userId);

		Response res = new Response();
		UserDocument updateTarget;
		Iterator<UserDocument> it = userDocs.iterator();

		// 指定したユーザが購読中のページ内で，解除対象ページの配列番号を調べる
		// kcucDB.findにてマッチするターゲットを見つけていたものをIteratorに置き換え
		while (it.hasNext()) {
			UserDocument userDoc = it.next();
			if (userDoc.getUserId().equals(userId)) {
				updateTarget = userDoc;

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
					updateTarget.removeSubscribedPage(eachTarget);
				}

				try {
					// DBへのアップデート処理
					// リトライ向けに2回書いているので修正時はどちらも直すこと
					res = kcucDB.update(updateTarget);
				} catch (TooManyRequestsException e) {
					// 回数制限に引っかかったら記録を残した上で1秒後に1度だけリトライする
					e.printStackTrace();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException ee) {
						ee.printStackTrace();
					}
					res = kcucDB.update(updateTarget);
					// ここをres = cancelSubscribedProduct(userId,
					// prodId)にすれば再帰呼び出しで成功するまでリトライし続ける
				}
				break;
			}
		}

		return res;
	}

	// DELETE - CRUD
	/**
	 * 指定したのユーザーを削除する
	 * 
	 * @param userId
	 *            削除対象となるユーザーのID
	 * @return ユーザー削除結果
	 */
	public Response deleteUser(String userId) {
		Response res = new Response();
		UserDocument target;
		// useNameのインデックスを使用して、指定されたユーザのデータを取得
		List<UserDocument> userDocs = this.getUserList(userId);

		for (UserDocument userDoc : userDocs) {
			// ユーザードキュメントから購読製品を取り出してIDが一致するものだけを取り出す
			if (userDoc.getUserId().equals(userId)) {
				target = userDoc;

				try {
					// DBへのアップデート処理
					// リトライ向けに2回書いているので修正時はどちらも直すこと
					res = kcucDB.remove(target);
				} catch (TooManyRequestsException e) {
					// 回数制限に引っかかったら記録を残した上で1秒後に1度だけリトライする
					e.printStackTrace();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException ee) {
						ee.printStackTrace();
					}
					res = kcucDB.remove(target);
					// ここをres = deleteUser(userId)にすれば再帰呼び出しで成功するまでリトライし続ける
				}

				break;
			}
		}
		return res;
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