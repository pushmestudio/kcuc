package jp.pushmestudio.kcuc.rest;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

import jp.pushmestudio.kcuc.controller.KCData;

import io.swagger.annotations.*;

@Api(value = "kcuc")
@Path("/check")
public class KCNoticeResource {

	// TODO 公開していないパスへのアクセスが発生した際などのリターンをエラーハンドリングとして指定する必要あり

	// 実際に取得処理などを行うオブジェクト
	KCData data = new KCData();

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
	@ApiOperation(value = "ユーザー一覧取得", notes = "特定のページを購読しているユーザー一覧を取得・確認 バッチから呼ばれる想定")
	public String getUpdatedUsers(@ApiParam(value = "更新確認対象のページキー", required = true) @QueryParam("href") @DefaultValue("") String href) {
		JSONObject results = data.checkUpdateByPage(href);
		return results.toString();
	}

	/**
	 * 特定のユーザーの購読しているページ一覧を取得・確認 クライアントから呼ばれる想定
	 * 
	 * @param user
	 *            更新確認対象のユーザー名
	 * @return 更新確認結果
	 */
	@Path("/pages")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "ページ一覧取得", notes = "特定のユーザーの購読しているページ一覧を取得・確認 クライアントから呼ばれる想定")
	public String getUpdatedPages(@ApiParam(value = "更新確認対象のユーザー名", required = true) @QueryParam("user") @DefaultValue("") String user) {
		JSONObject results = data.checkUpdateByUser(user);
		return results.toString();
	}

	/**
	 * 特定のユーザの購読するページを追加・確認 クライアントから呼ばれる想定
	 * 
	 * @param user
	 *            購読ページを登録するユーザ（いずれはCookieなど）
	 * @param href
	 *            購読登録対象のページキー
	 * @return 登録確認結果
	 */
	@Path("/pages")
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "購読ページ追加", notes = "特定のユーザの購読するページを追加・確認 クライアントから呼ばれる想定")
	public String setSubscribe(@ApiParam(value = "更新対象のユーザー名", required = true) @FormParam("user") @DefaultValue("") String user, 
									@ApiParam(value = "購読対象のページキー", required = true) @FormParam("href") String href) {
		JSONObject results = data.registerSubscribedPage(user, href);
		return results.toString();
	}
}
