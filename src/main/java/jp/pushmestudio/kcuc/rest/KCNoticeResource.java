package jp.pushmestudio.kcuc.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

import jp.pushmestudio.kcuc.model.KCData;

@Path("/check")
public class KCNoticeResource {

	// TODO 公開していないパスへのアクセスが発生した際などのリターンをエラーハンドリングとして指定する必要あり

	// 実際に取得処理などを行うオブジェクト
	KCData data = new KCData();

	/**
	 * すべての更新を確認
	 * @return 更新確認結果
	 */
	@Path("/all")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public String getUpdate() {
		JSONObject results = data.checkUpdate();
		return results.toString();
	}

	/**
	 * 特定のパラメータの更新を確認
	 * @param productKey 更新確認対象のキー
	 * @return 更新確認結果
	 */
	@Path("/{key}")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public String getUpdate(@QueryParam("key") String productKey) {
		JSONObject results = data.checkUpdate(productKey);
		return results.toString();
	}
}
