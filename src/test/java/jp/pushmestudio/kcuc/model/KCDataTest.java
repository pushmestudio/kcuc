package jp.pushmestudio.kcuc.model;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.*;

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
	// 購読解除の処理が実装されるまでPending
	public void 特定ユーザーと購読ページを指定してユーザーの購読リストに登録されることを確認できる() {
		KCData data = new KCData();
		String userId = "tkhm";
		String hrefKey = "SSYRPW_9.0.1/UsingVerseMobile.html";

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
	public void 特定ページをパラメーター付きで検索して指定したOffsetやLimitどおりの検索結果が得られる() {
		// setup
		// IBM Verseで検索して、検索結果の5件目から15件取得
		KCData data = new KCData();
		String searchQuery = "IBM Verse";
		int offset = 5;
		int limit = 15;

		// execute
		Result searchResult = data.searchPages(searchQuery, offset, limit, "");

		// verify
		int expectedOffset = offset;
		int expectedNext = offset + limit;
		int actualOffset = ((ResultSearchList)searchResult).getOffset();
		int actualNext = ((ResultSearchList)searchResult).getNext();
		int actualTopicSize = ((ResultSearchList)searchResult).getTopics().size();

		assertEquals(expectedOffset, actualOffset);
		assertEquals(expectedNext, actualNext);
		assertTrue(actualTopicSize > 0);
	}
}
