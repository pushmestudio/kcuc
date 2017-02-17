package jp.pushmestudio.kcuc.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

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
		// setup
		KCData data = new KCData();
		String userId = "tkhm";
		String hrefKey = "SSYRPW_9.0.1/UsingVerseMobile.html";

		// テストデータが事前に登録されていない状態にする
		data.deleteSubscribedPage(userId, hrefKey);

		Result preResult = data.checkUpdateByUser(userId);
		List<SubscribedPage> prePageList = ((ResultPageList) preResult).getSubscribedPages();

		// execute
		Result checkResult = data.registerSubscribedPage(userId, hrefKey);

		// verify
		List<SubscribedPage> pageList = ((ResultPageList) checkResult).getSubscribedPages();

		boolean actual = pageList.size() > prePageList.size() ? true : false;

		// teardown
		/*
		 * テストデータとして登録したページを削除する,
		 * 本来はteardownメソッド内に書くが現時点ではこの削除処理を必要としていないテストケースが多いため割愛
		 * assertTrueよりも後に書くと、assertionErrorになったときに削除されずに終わってしまうので注意
		 */
		data.deleteSubscribedPage(userId, hrefKey);

		assertTrue(actual);
	}

	@Test
	// 購読解除の処理が実装されるまでPending
	public void 特定ユーザーと購読ページを指定してユーザーの購読リストから解除されることを確認できる() {
		// setup
		KCData data = new KCData();
		String userId = "meltest";
		String hrefKey = "SSYRPW_9.0.1/UsingVerseMobile.html";

		// テストデータが事前に登録されている状態にする
		data.registerSubscribedPage(userId, hrefKey);

		Result preResult = data.checkUpdateByUser(userId);
		List<SubscribedPage> prePageList = ((ResultPageList) preResult).getSubscribedPages();

		// execute
		Result checkResult = data.deleteSubscribedPage(userId, hrefKey);

		// verify
		List<SubscribedPage> pageList = ((ResultPageList) checkResult).getSubscribedPages();

		boolean actual = pageList.size() < prePageList.size() ? true : false;

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
		int actualOffset = ((ResultSearchList) searchResult).getOffset();
		int actualNext = ((ResultSearchList) searchResult).getNext();
		int actualTopicSize = ((ResultSearchList) searchResult).getTopics().size();

		assertEquals(expectedOffset, actualOffset);
		assertEquals(expectedNext, actualNext);
		assertTrue(actualTopicSize > 0);
	}
}
