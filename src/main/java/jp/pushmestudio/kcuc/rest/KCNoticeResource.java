package jp.pushmestudio.kcuc.rest;

import javax.ws.rs.DefaultValue;
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
	 * すべての更新を確認、意図不明瞭な名称のため使用しないこと、削除予定
	 * 
	 * @return 更新確認結果
	 */
	@Path("/all")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public String getUpdate() {
		@SuppressWarnings("deprecation")
		JSONObject results = data.checkUpdate();
		return results.toString();
	}

	/**
	 * 特定のページを購読しているユーザー一覧を取得・確認 バッチから呼ばれる想定
	 * 
	 * @param href
	 *            更新確認対象のページキー
	 * @return 更新確認結果
	 */
	@Path("/users")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public String getUpdatedUsers(@QueryParam("href") @DefaultValue("") String href) {
		JSONObject results = data.checkUpdateByPage(href);
		return results.toString();
	}

	/**
	 * 特定のユーザーの購読しているページ一覧を取得・確認 クライアントから呼ばれる想定
	 * 
	 * @param user
	 *            更新確認対象のページキー
	 * @return 更新確認結果
	 */
	@Path("/pages")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public String getUpdatedPages(@QueryParam("user") @DefaultValue("") String user) {
		JSONObject results = data.checkUpdateByUser(user);
		return results.toString();
	}
}
