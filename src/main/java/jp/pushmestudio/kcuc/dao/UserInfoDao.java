package jp.pushmestudio.kcuc.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.pushmestudio.kcuc.model.UserInfo;

public class UserInfoDao {
	/**
	 * @return DBから取得したデータ
	 */
	public List<UserInfo> getUserList() {
		List<UserInfo> userList = new ArrayList<>();

		// TODO ここでDBに対してselect処理などを実施する、今はダミーの値を使用する
		DummyStatement stmt = new DummyStatement();
		List<List<Object>> rs = stmt.executeQuery(1);

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
		// 引数の値で、テーブルから取得する件数を指定できるようにしている
		public List<List<Object>> executeQuery(int getRequestCount) {
			List<List<Object>> dummyResultSet = new ArrayList<>();

			List<List<Object>> dummyUserTable = new DummyUserTable().getUserTableData();
			// 取得依頼件数がダミーテーブルの件数よりも多く指定された場合は、ダミーテーブルの件数を取得依頼件数とする
			getRequestCount = dummyUserTable.size() < getRequestCount ? dummyUserTable.size() : getRequestCount;
			for (int i = 0; i < getRequestCount; i++) {
				List<Object> dummyData = dummyUserTable.get(i);
				dummyResultSet.add(dummyData);
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
			dummy1SubscribedPages.put("SSAW57_liberty", 1469114812137L);
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
