package jp.pushmestudio.kcuc.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

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
		Result searchResult = data.searchPages(searchQuery, null, null, offset, limit, "");

		// verify
		int expectedOffset = offset;
		int expectedNext = offset + limit;
		int actualOffset = ((ResultSearchList) searchResult).getOffset();
		int actualNext = ((ResultSearchList) searchResult).getNext();
		int actualTopicSize = ((ResultSearchList) searchResult).getTopics().size();

		// TODO
		// テストを正しく書く場合、下記のように3つまとめて1つのテストケースの中に書かずにグループテストを用いるべき、今後テストケースが増えるようであれば検討する
		assertEquals(expectedOffset, actualOffset); // offset位置を正しく取れているか
		assertEquals(expectedNext, actualNext); // next位置が正しく取れているか
		assertTrue(actualTopicSize > 0); // 1件以上の値が取得できているか
	}

	@Test
	public void 特定ユーザーに同製品の異なる2ページを登録して1件の購読製品が追加されることを確認できる() {
		// setup
		KCData data = new KCData();
		final String userId = "tkhm";
		final String hrefKey1 = "SSTPQH_1.0.0/com.ibm.cloudant.local.install.doc/topics/clinstall_planning_install_location.html";
		final String hrefKey2 = "SSTPQH_1.0.0/com.ibm.cloudant.local.install.doc/topics/clinstall_tuning_automatic_compactor.html";

		// テストデータが事前に登録されていない状態にする
		// TODO 製品指定による購読解除ができるようになったらそちらに切り替える
		data.deleteSubscribedPage(userId, hrefKey1);
		data.deleteSubscribedPage(userId, hrefKey2);

		Result preResult = data.getSubscribedProductList(userId);
		Map<String, String> preProductSet = ((ResultProductList) preResult).getSubscribedProducts();

		data.registerSubscribedPage(userId, hrefKey1);
		data.registerSubscribedPage(userId, hrefKey2);

		// execute
		Result gotResult = data.getSubscribedProductList(userId);
		Map<String, String> productSet = ((ResultProductList) gotResult).getSubscribedProducts();

		// verify
		final int expectedSize = preProductSet.size() + 1;
		final int actualSize = productSet.size();

		// teardown
		/*
		 * テストデータとして登録したページを削除する,
		 * 本来はteardownメソッド内に書くが現時点ではこの削除処理を必要としていないテストケースが多いため割愛
		 * assertTrueよりも後に書くと、assertionErrorになったときに削除されずに終わってしまうので注意
		 */
		data.deleteSubscribedPage(userId, hrefKey1);
		data.deleteSubscribedPage(userId, hrefKey2);

		assertEquals(expectedSize, actualSize);
	}
}
