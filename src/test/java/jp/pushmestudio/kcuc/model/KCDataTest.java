package jp.pushmestudio.kcuc.model;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import jp.pushmestudio.kcuc.controller.KCData;
import jp.pushmestudio.kcuc.util.Result;

public class KCDataTest {

	@Test
	public void 特定ページを取得して1人以上の購読しているユーザーを確認できる() {
		// setup TODO セットアップ実装すること
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
		// setup TODO セットアップ実装すること
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
		int actual = pageList.size();

		// 1件追加されているか
		assertThat(actual, is(prePageList.size() + 1));

		// teardown
		data.deleteSubscribedPage(userId, hrefKey);
	}

	@Test
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
		int actual = pageList.size();

		// 1件削除されているか
		assertThat(actual, is(prePageList.size() - 1));
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

		// TODO
		// テストを正しく書く場合、下記のように3つまとめて1つのテストケースの中に書かずにグループテストを用いるべき、今後テストケースが増えるようであれば検討する
		assertThat(actualOffset, is(expectedOffset)); // offset位置を正しく取れているか
		assertThat(actualNext, is(expectedNext)); // next位置が正しく取れているか
		assertTrue(actualTopicSize > 0); // 1件以上の値が取得できているか
	}

	@Test
	public void 特定ユーザーに同製品の異なる2ページを登録して1件の購読製品が追加されることを確認できる() {
		// setup
		KCData data = new KCData();
		final String userId = "tkhm";
		final String prodId = "SSTPQH_1.0.0";
		final String hrefKey1 = "SSTPQH_1.0.0/com.ibm.cloudant.local.install.doc/topics/clinstall_planning_install_location.html";
		final String hrefKey2 = "SSTPQH_1.0.0/com.ibm.cloudant.local.install.doc/topics/clinstall_tuning_automatic_compactor.html";

		// テストデータが事前に登録されていない状態にする
		data.cancelSubscribedProduct(userId, prodId);

		Result preResult = data.getSubscribedProductList(userId);
		Map<String, String> preProductMap = ((ResultProductList) preResult).getSubscribedProducts();

		data.registerSubscribedPage(userId, hrefKey1);
		data.registerSubscribedPage(userId, hrefKey2);

		// execute
		Result gotResult = data.getSubscribedProductList(userId);
		Map<String, String> productMap = ((ResultProductList) gotResult).getSubscribedProducts();

		// verify
		final int expectedSize = preProductMap.size() + 1;
		final int actualSize = productMap.size();
		assertThat(actualSize, is(expectedSize));

		// teardown
		data.cancelSubscribedProduct(userId, prodId);
	}
}
