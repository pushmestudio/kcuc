package jp.pushmestudio.kcuc.model;

import static org.junit.Assert.assertTrue;

import org.json.JSONObject;
import org.junit.Test;

public class KCDataTest {

	@Test
	public void 特定製品の最終更新日付を取得して事前に定めた更新日付けよりも新しいことを確認できる() {
		// setup
		KCData data = new KCData();
		String hrefKey = "SSAW57_liberty/com.ibm.websphere.wlp.nd.doc/ae/cwlp_about.html";

		// execute
		JSONObject checkResult = data.checkPageUpdate(hrefKey);

		// verify
		Boolean actual = (Boolean) checkResult.getBoolean("isUpdated");
		assertTrue(actual);
	}

}
