package jp.pushmestudio.kcuc.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import jp.pushmestudio.kcuc.model.UserInfo;

public class UserInfoDao {
	/**
	 * 指定したIDのユーザーを返す
	 * 
	 * @param searchId
	 *            探す対象となるユーザーのID
	 * @return DBから取得した、IDに該当するユーザー
	 */
	public List<UserInfo> getUserList(String searchId) {
		List<UserInfo> userList = new ArrayList<>();

		// TODO ここでDBに対してselect処理などを実施する、今はダミーの値を使用する
		DummyStatement stmt = new DummyStatement();
		List<List<Object>> rs = stmt.executeQueryUser(searchId);

		for (int i = 0; i < rs.size(); i++) {
			List<Object> gotUser = rs.get(i);
			String id = (String) gotUser.get(0);
			String password = (String) gotUser.get(1);
			@SuppressWarnings("unchecked")
			Map<String, Long> subscribedPages = (Map<String, Long>) gotUser.get(2);

			UserInfo userInfo = new UserInfo(id, password, subscribedPages);
			userList.add(userInfo);
		}

		return userList;
	}

	/**
	 * 指定したページを購読しているユーザーを返す
	 * 
	 * @param pageHref
	 *            購読ページ
	 * @return 指定した購読ページを購読しているユーザー
	 */
	public List<UserInfo> getSubscribedUserList(String pageHref) {
		List<UserInfo> userList = new ArrayList<>();

		// TODO ここでDBに対してselect処理などを実施する、今はダミーの値を使用する
		DummyStatement stmt = new DummyStatement();
		List<List<Object>> rs = stmt.executeQueryPage(pageHref);

		for (int i = 0; i < rs.size(); i++) {
			List<Object> gotUser = rs.get(i);
			String id = (String) gotUser.get(0);
			String password = (String) gotUser.get(1);
			@SuppressWarnings("unchecked")
			Map<String, Long> subscribedPages = (Map<String, Long>) gotUser.get(2);

			UserInfo userInfo = new UserInfo(id, password, subscribedPages);
			userList.add(userInfo);
		}

		return userList;
	}

	class DummyStatement {
		// DBからデータを取得することを擬制するために、executeQueryの名前で取得
		// 引数の値で、ユーザーIDが合致するもののみ返す
		public List<List<Object>> executeQueryUser(String searchId) {
			Objects.requireNonNull(searchId);
			List<List<Object>> dummyResultSet = new ArrayList<>();

			List<List<Object>> dummyUserTable = new DummyUserTable().getUserTableData();
			for (int i = 0; i < dummyUserTable.size(); i++) {
				List<Object> dummyData = dummyUserTable.get(i);

				// dummyData.get(0) = ユーザーID
				if (searchId.equals((String) dummyData.get(0))) {
					dummyResultSet.add(dummyData);
				}
			}

			return dummyResultSet;
		}

		public List<List<Object>> executeQueryPage(String pageHref) {
			Objects.requireNonNull(pageHref);
			List<List<Object>> dummyResultSet = new ArrayList<>();

			List<List<Object>> dummyUserTable = new DummyUserTable().getUserTableData();
			for (int i = 0; i < dummyUserTable.size(); i++) {
				List<Object> dummyData = dummyUserTable.get(i);

				// dummyData.get(2) = 購読中のページと最後に確認した時間
				@SuppressWarnings("unchecked")
				HashMap<String, Long> subscribedPages = (HashMap<String, Long>) dummyData.get(2);

				// 購読ページ一覧の中にpageHrefがないユーザーはリストに足さない
				if (Objects.isNull(subscribedPages.get(pageHref))) {
					continue;
				} else {
					dummyResultSet.add(dummyData);
				}
			}

			return dummyResultSet;
		}
	}

	// DBからデータを取得したときとできるかぎりイメージを近づけるため、オブジェクト型のリストにしている
	// rs.getString(1)で取得するのと同じような形にできるので状況的には近しくなる
	class DummyUserTable {
		public List<List<Object>> getUserTableData() {
			// ダミーユーザー1
			List<Object> dummyUser1 = new ArrayList<>();
			String dummy1Id = "capsmalt";
			String dummy1Password = "pass";
			Map<String, Long> dummy1SubscribedPages = new HashMap<>();
			dummy1SubscribedPages.put("SSAW57_liberty/com.ibm.websphere.wlp.nd.doc/ae/cwlp_about.html", 1469114812137L);
			dummyUser1.add(dummy1Id);
			dummyUser1.add(dummy1Password);
			dummyUser1.add(dummy1SubscribedPages);

			// ダミーユーザー2
			List<Object> dummyUser2 = new ArrayList<>();
			String dummy2Id = "tkhm";
			String dummy2Password = "word";
			Map<String, Long> dummy2SubscribedPages = new HashMap<>();
			dummyUser2.add(dummy2Id);
			dummyUser2.add(dummy2Password);
			dummyUser2.add(dummy2SubscribedPages);

			List<List<Object>> userTable = new ArrayList<>();
			userTable.add(dummyUser1);
			userTable.add(dummyUser2);
			return userTable;
		}
	}
}
