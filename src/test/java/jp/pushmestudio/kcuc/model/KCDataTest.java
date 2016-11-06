package jp.pushmestudio.kcuc.model;

import static org.junit.Assert.assertTrue;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

public class KCDataTest {

	@Test
	public void 特定ユーザーの最終更新日付よりもKCの最終更新日付けの方が新しいことを確認できる() {
		// setup
		KCData data = new KCData();
		String hrefKey = "SSAW57_liberty/com.ibm.websphere.wlp.nd.doc/ae/cwlp_about.html";

		// execute
		JSONObject checkResult = data.checkPageUpdate(hrefKey);
		// System.out.println(checkResult); 

		// verify
		JSONArray userList = checkResult.getJSONArray("userList");
		JSONObject firstUser = userList.getJSONObject(0);
		boolean actual = firstUser.getBoolean("isUpdated");
		assertTrue(actual);
	}

}
