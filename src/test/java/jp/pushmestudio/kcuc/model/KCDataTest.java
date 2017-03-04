package jp.pushmestudio.kcuc.model;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import jp.pushmestudio.kcuc.controller.KCData;
import jp.pushmestudio.kcuc.util.Result;

@RunWith(Enclosed.class)
public class KCDataTest {

	public static class 購読済ページの存在を確認するケース {
		static KCData data = new KCData();
		static String userId = "tkhm";
		static String hrefKey = "SSMTU9/welcometoibmverse.html";

		/** テストデータが事前に登録されている状態にする、グループ内で一度だけ実行 */
		@BeforeClass
		public static void setUp() {
			try {
				// テストデータとして登録する、APIへの負荷を懸念しスリープ処理を入れている
				data.registerSubscribedPage(userId, hrefKey);
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		/** テストデータが事後に登録されていない状態にする、グループ内で一度だけ実行 */
		@AfterClass
		public static void tearDown() {
			// テストデータとして登録したものを削除する、APIへの負荷を懸念しスリープ処理を入れている
			try {
				Thread.sleep(500);
				data.deleteSubscribedPage(userId, hrefKey);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		@Test
		public void 登録済みのページを指定して1人以上の購読ユーザーを確認できる() {
			// execute
			Result checkResult = data.checkUpdateByPage(hrefKey);

			// verify
			List<UserInfo> userList = ((ResultUserList) checkResult).getSubscribers();

			boolean actual = userList.size() >= 1;
			assertTrue(actual);
		}

		@Test
		public void ページを購読しているユーザーを指定して1件以上購読ページを確認できる() {
			// execute
			Result checkResult = data.checkUpdateByUser(userId);

			// verify
			List<SubscribedPage> pageList = ((ResultPageList) checkResult).getSubscribedPages();

			boolean actual = pageList.size() >= 1;
			assertTrue(actual);
		}
	}

	public static class 購読済ページの追加を確認するケース {
		static KCData data = new KCData();
		static String userId = "tkhm";
		static String prodId = "SSTPQH_1.0.0";
		static String hrefKey1 = "SSTPQH_1.0.0/com.ibm.cloudant.local.install.doc/topics/clinstall_planning_install_location.html";
		static String hrefKey2 = "SSTPQH_1.0.0/com.ibm.cloudant.local.install.doc/topics/clinstall_tuning_automatic_compactor.html";
		static Result preResultPages;
		static List<SubscribedPage> prePageList;
		static Result preResultProducts;
		static Map<String, String> preProductMap;
		static Result checkResultPages;

		/** テストデータが事前に登録されている状態にする、グループ内で一度だけ実行 */
		@BeforeClass
		public static void setUp() {
			try {
				// 事前に登録がない状態にする、APIへの負荷を懸念しスリープ処理を入れている
				data.cancelSubscribedProduct(userId, prodId);
				Thread.sleep(1000);

				preResultPages = data.checkUpdateByUser(userId);
				prePageList = ((ResultPageList) preResultPages).getSubscribedPages();

				preResultProducts = data.getSubscribedProductList(userId);
				preProductMap = ((ResultProductList) preResultProducts).getSubscribedProducts();
				Thread.sleep(1000);

				// execute
				checkResultPages = data.registerSubscribedPage(userId, hrefKey1);
				Thread.sleep(1000);
				checkResultPages = data.registerSubscribedPage(userId, hrefKey2);
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		/** テストデータが事後に登録されていない状態にする、グループ内で一度だけ実行 */
		@AfterClass
		public static void tearDown() {
			try {
				// 事後に登録がない状態にする、APIへの負荷を懸念しスリープ処理を入れている
				Thread.sleep(500);
				data.cancelSubscribedProduct(userId, prodId);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		@Test
		public void 追加した2件の購読ページが購読済リストに登録される() {
			// verify
			List<SubscribedPage> pageList = ((ResultPageList) checkResultPages).getSubscribedPages();
			int actual = pageList.size();

			// 2件追加されているか
			assertThat(actual, is(prePageList.size() + 2));
		}

		@Test
		public void 同製品の異なる2ページを登録して購読製品として1件が取得される() {
			// execute
			Result gotResult = data.getSubscribedProductList(userId);
			Map<String, String> productMap = ((ResultProductList) gotResult).getSubscribedProducts();

			// verify
			final int expectedSize = preProductMap.size() + 1;
			final int actualSize = productMap.size();
			assertThat(actualSize, is(expectedSize));
		}
	}

	public static class 購読済ページの削除を確認するケース {
		static KCData data = new KCData();
		static String userId = "meltest";
		static String hrefKey = "SSYRPW_9.0.1/UsingVerseMobile.html";
		static Result preResult;
		static List<SubscribedPage> prePageList;

		/** テストデータが事前に登録されている状態にする、グループ内で一度だけ実行 */
		@BeforeClass
		public static void setUp() {
			try {
				// テストデータとして登録する、APIへの負荷を懸念しスリープ処理を入れている
				data.registerSubscribedPage(userId, hrefKey);

				// テスト前の事前状態を保存しておく
				preResult = data.checkUpdateByUser(userId);
				prePageList = ((ResultPageList) preResult).getSubscribedPages();
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

		/** テストデータが事後に登録されていない状態にする、グループ内で一度だけ実行 */
		@AfterClass
		public static void tearDown() {
			// テストデータとして登録したものを削除する、APIへの負荷を懸念しスリープ処理を入れている
			try {
				Thread.sleep(500);
				data.deleteSubscribedPage(userId, hrefKey);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		@Test
		public void 購読解除した1件のページが購読済リストから取り除かれる() {
			// execute
			Result checkResult = data.deleteSubscribedPage(userId, hrefKey);

			// verify
			List<SubscribedPage> pageList = ((ResultPageList) checkResult).getSubscribedPages();
			int actual = pageList.size();

			// 1件削除されているか
			assertThat(actual, is(prePageList.size() - 1));
		}
	}

	public static class 検索結果を確認するケース {
		static KCData data = new KCData();
		static String searchQuery = "IBM Verse";
		static int offset = 5;
		static int limit = 15;
		static Result searchResult;

		/** テスト前に一度だけ実行 */
		@BeforeClass
		public static void setUp() {
			// execute
			searchResult = data.searchPages(searchQuery, offset, limit, "");
		}

		@Test
		public void 検索結果が1件以上() {
			// verify
			int actualTopicSize = ((ResultSearchList) searchResult).getTopics().size();

			assertTrue(actualTopicSize > 0); // 1件以上の値が取得できているか
		}

		@Test
		public void 検索結果のオフセット位置がパラメーターと一致する() {
			// verify
			int expectedOffset = offset;
			int actualOffset = ((ResultSearchList) searchResult).getOffset();

			assertThat(actualOffset, is(expectedOffset)); // offset位置を正しく取れているか
		}

		@Test
		public void 検索結果の1問い合わせあたりの取得件数がパラメーターと一致する() {
			// verify
			int expectedNext = offset + limit;
			int actualNext = ((ResultSearchList) searchResult).getNext();

			assertThat(actualNext, is(expectedNext)); // next位置が正しく取れているか
		}
	}
}
