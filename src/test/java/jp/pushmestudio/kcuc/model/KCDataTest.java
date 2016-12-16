package jp.pushmestudio.kcuc.model;

import static org.junit.Assert.assertTrue;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import jp.pushmestudio.kcuc.controller.KCData;

public class KCDataTest {

	@Test
	public void 特定ページを取得してユーザーの最終閲覧日時よりKCの最終更新日付けの方が新しいことを確認できる() {
		// setup
		KCData data = new KCData();
		String hrefKey = "SSMTU9/welcometoibmverse.html";

		// execute
		JSONObject checkResult = data.checkUpdateByPage(hrefKey);
		// System.out.println(checkResult);

		// verify
		JSONArray userList = checkResult.getJSONArray("userList");
		JSONObject firstUser = userList.getJSONObject(0);
		boolean actual = firstUser.getBoolean("isUpdated");
		assertTrue(actual);
	}

	@Test
	public void 特定ユーザーの購読ページを取得してページのユーザー最終閲覧日付よりKCの最終更新日付けの方が新しいことを確認できる() {
		// setup
		KCData data = new KCData();
		String userId = "capsmalt";

		// execute
		JSONObject checkResult = data.checkUpdateByUser(userId);
		// System.out.println(checkResult);

		// verify
		JSONArray pageList = checkResult.getJSONArray("pages");
		JSONObject firstPage = pageList.getJSONObject(0);
		boolean actual = firstPage.getBoolean("isUpdated");
		assertTrue(actual);
	}

	@Test
	public void 特定ユーザーと購読ページを指定してユーザーの購読リストに登録されることを確認できる() {
		KCData data = new KCData();
		String userId = "capsmalt";
		String hrefKey = "SS42VS_7.2.7/com.ibm.qradar.doc/b_qradar_qsg.html";
		
		// execute
		JSONObject checkResult = data.registerSubscribedPages(userId, hrefKey);
		
		// verify
		JSONArray pageList = checkResult.getJSONArray("pages");
		
		// ダミーデータ（capsmalt）には購読ページが1つしかないので、それが2つ以上に増えている場合を正とする。
		// 本当は初期値を取得して1インクリメントされているか見たい
		boolean actual = pageList.length() >= 2 ? true : false;
		assertTrue(actual);
		
	}
}
