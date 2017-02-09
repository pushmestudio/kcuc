package jp.pushmestudio.kcuc.model;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import jp.pushmestudio.kcuc.controller.KCData;
import jp.pushmestudio.kcuc.util.Result;

public class KCDataTest {

	@Test
	public void 特定ページを取得して1人以上の購読しているユーザーを確認できる() {
		// setup
		KCData data = new KCData();
		String hrefKey = "SSMTU9/welcometoibmverse.html";

		// execute
		Result checkResult = data.checkUpdateByPage(hrefKey);

		// verify
		List<UserInfo> userList = ((ResultUserList) checkResult).getSubscribers();

		boolean actual = userList.size() >= 1;
		assertTrue(actual);
	}

	@Test
	public void 特定ユーザーの購読ページを取得して1件以上ページが存在することを確認できる() {
		// setup
		KCData data = new KCData();
		String userId = "tkhm";

		// execute
		Result checkResult = data.checkUpdateByUser(userId);

		// verify
		List<SubscribedPage> pageList = ((ResultPageList) checkResult).getSubscribedPages();

		boolean actual = pageList.size() >= 1;
		assertTrue(actual);
	}

	@Test
	@Ignore
	// 購読解除の処理が実装されるまでPending
	public void 特定ユーザーと購読ページを指定してユーザーの購読リストに登録されることを確認できる() {
		KCData data = new KCData();
		String userId = "tkhm";
		String hrefKey = "SS42VS_7.2.7/com.ibm.qradar.doc/b_qradar_qsg.html";

		Result preResult = data.checkUpdateByUser(userId);
		List<SubscribedPage> prePageList = ((ResultPageList) preResult).getSubscribedPages();

		// execute
		Result checkResult = data.registerSubscribedPage(userId, hrefKey);

		// verify
		List<SubscribedPage> pageList = ((ResultPageList) checkResult).getSubscribedPages();

		// ダミーデータ（tkhm）には購読ページが1つしかないので、それが2つ以上に増えている場合を正とする。
		// 本当は初期値を取得して1インクリメントされているか見たい
		boolean actual = pageList.size() > prePageList.size() ? true : false;

		/*
		 * TODO ここに今回登録したものの購読解除の処理を書く、なお、本来は@Afterによって実施すべき処理だが、
		 * 全テストに影響が生じることを踏まえこのテスト内に書くようにする
		 * なお、assertTrueの後に書いてしまうと、万が一テストの結果が失敗だった際にAssertionErrorが投げられてしまい、
		 * 購読解除が実現されないので、記載場所には要注意
		 */
		assertTrue(actual);
	}
	
	@Test
	@Ignore
	public void ページを検索できる() {
		// setup
		KCData data = new KCData();
		String searchQuery = "IBM Verse";

		// execute
		data.searchPages(searchQuery, 0, 0, "");

		fail();
		// TODO to be implemented
		// 実装されないままでマージしないこと
	}
}
