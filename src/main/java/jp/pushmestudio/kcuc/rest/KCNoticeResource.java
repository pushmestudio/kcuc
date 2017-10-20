package jp.pushmestudio.kcuc.rest;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jp.pushmestudio.kcuc.controller.KCData;
import jp.pushmestudio.kcuc.model.ResultPageList;
import jp.pushmestudio.kcuc.model.ResultProductList;
import jp.pushmestudio.kcuc.model.ResultUserList;
import jp.pushmestudio.kcuc.util.Message;
import jp.pushmestudio.kcuc.util.Result;

@Api(value = "User and user content management")
@Path("/users")
public class KCNoticeResource {

	// 実際に取得処理などを行うオブジェクト
	KCData data = new KCData();

	/**
	 * 特定のページを購読しているユーザー一覧を取得・確認
	 *
	 * @param pageHref
	 *            更新確認対象のページキー
	 * @param time
	 *            更新判断の基準時間
	 * @return 更新確認結果
	 */
	@Path("")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "ユーザー一覧取得", notes = "特定のページを購読しているユーザー一覧を取得・確認")
	@ApiResponses(value = { @ApiResponse(code = Result.CODE_OK, response = ResultUserList.class, message = "OK"),
			@ApiResponse(code = Result.CODE_NOT_FOUND, response = Message.class, message = "指定したコンテンツが見つかりません") })
	public Response getUpdatedUsers(
			@ApiParam(value = "更新確認対象のページキー", required = true) @QueryParam("pageHref") String pageHref,
			@ApiParam(value = "更新判断の基準時間, ここで入力されたタイムスタンプよりも後に更新されていれば更新ありとみなす, デフォルトは1週間前時点", required = false) @QueryParam("time") Long time) {

		Result result = data.checkUpdateByPage(pageHref, time);
		return Response.status(result.getCode()).entity(result).build();
	}

	/**
	 * 指定されたIDのユーザーを作成する
	 *
	 * @param userId
	 *            作成ユーザーのID
	 * @return 更新確認結果
	 */
	@Path("")
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "ユーザー作成", notes = "与えられたIDを元にユーザーを作成する")
	@ApiResponses(value = { @ApiResponse(code = Result.CODE_OK, response = Message.class, message = "OK"),
			@ApiResponse(code = Result.CODE_BAD_REQUEST, response = Message.class, message = "パラメーターが不正です"),
			@ApiResponse(code = Result.CODE_CONFLICT, response = Message.class, message = "既に存在しています") })
	public Response createUser(@ApiParam(value = "ユーザーID", required = true) @FormParam("userId") String userId) {

		Result result = data.createUser(userId);
		return Response.status(result.getCode()).entity(result).build();
	}

	/**
	 * 指定されたIDのユーザーを削除する
	 *
	 * @param userId
	 *            削除ユーザーのID
	 * @return 更新確認結果
	 */
	@Path("/{id}")
	@DELETE
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "ユーザー削除", notes = "与えられたIDを元にユーザーを削除する")
	@ApiResponses(value = { @ApiResponse(code = Result.CODE_OK, response = Message.class, message = "購読解除が完了しました"),
			@ApiResponse(code = Result.CODE_NOT_FOUND, response = Message.class, message = "指定したコンテンツが見つかりません") })
	public Response deleteUser(@ApiParam(value = "ユーザーID", required = true) @PathParam("id") String userId) {

		Result result = data.deleteUser(userId);
		return Response.status(result.getCode()).entity(result).build();
	}

	/**
	 * 特定のユーザーの購読しているページ一覧を取得・確認
	 *
	 * @param userId
	 *            更新確認対象のユーザーID
	 * @param prodId
	 *            確認対象の製品ID
	 * @param time
	 *            更新判断の基準時間
	 * @return 更新確認結果
	 */
	@Path("/{id}/pages")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "ページ一覧取得", notes = "特定のユーザーの購読しているページ一覧を取得・確認")
	@ApiResponses(value = { @ApiResponse(code = Result.CODE_OK, response = ResultPageList.class, message = "OK"),
			@ApiResponse(code = Result.CODE_NOT_FOUND, response = Message.class, message = "指定したコンテンツが見つかりません") })
	public Response getUpdatedPages(@ApiParam(value = "更新確認対象のユーザー名", required = true) @PathParam("id") String userId,
			@ApiParam(value = "更新確認対象の製品ID", required = false) @QueryParam("prodId") String prodId,
			@ApiParam(value = "更新判断の基準時間, ここで入力されたタイムスタンプよりも後に更新されていれば更新ありとみなす, デフォルトは1週間前時点", required = false) @QueryParam("time") Long time) {

		Result result = data.checkUpdateByUser(userId, prodId, time);
		return Response.status(result.getCode()).entity(result).build();
	}

	/**
	 * 特定のユーザの購読するページを追加・確認
	 *
	 * @param userId
	 *            購読ページを登録するユーザID（いずれはCookieなど）
	 * @param pageHref
	 *            購読登録対象のページキー
	 * @return 登録確認結果
	 */
	@Path("/{id}/pages")
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "購読ページ追加", notes = "特定のユーザの購読するページを追加・確認")
	@ApiResponses(value = { @ApiResponse(code = Result.CODE_OK, response = Message.class, message = "OK"),
			@ApiResponse(code = Result.CODE_BAD_REQUEST, response = Message.class, message = "パラメーターが不正です"),
			@ApiResponse(code = Result.CODE_NOT_FOUND, response = Message.class, message = "指定したコンテンツが見つかりません"),
			@ApiResponse(code = Result.CODE_CONFLICT, response = Message.class, message = "既に存在しています") })
	public Response setSubscribe(@ApiParam(value = "更新対象のユーザー名", required = true) @PathParam("userId") String userId,
			@ApiParam(value = "購読対象のページキー", required = true) @FormParam("pageHref") String pageHref) {

		Result result = data.registerSubscribedPage(userId, pageHref);
		return Response.status(result.getCode()).entity(result).build();
	}

	/**
	 * 特定のユーザの購読するページを解除
	 *
	 * @param userId
	 *            購読ページを解除するユーザID（いずれはCookieなど）
	 * @param pageHref
	 *            購読解除対象のページキー
	 * @return 購読解除後の購読ページ一覧
	 */
	@Path("/{id}/pages/{pageHref}")
	@DELETE
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "購読ページ解除", notes = "特定のユーザの購読するページを解除")
	@ApiResponses(value = { @ApiResponse(code = Result.CODE_OK, response = Message.class, message = "購読解除が完了しました"),
			@ApiResponse(code = Result.CODE_NOT_FOUND, response = Message.class, message = "指定したコンテンツが見つかりません") })
	public Response unsetSubscribe(@ApiParam(value = "対象のユーザー名", required = true) @PathParam("id") String userId,
			@ApiParam(value = "購読解除対象のページキー", required = true) @PathParam("pageHref") String pageHref) {

		Result result = data.deleteSubscribedPage(userId, pageHref);
		return Response.status(result.getCode()).entity(result).build();
	}

	/**
	 * 特定のユーザーの購読しているページ一覧を取得・確認
	 *
	 * @param userId
	 *            更新確認対象のユーザーID
	 * @return 更新確認結果
	 */
	@Path("/{id}/products")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "購読製品一覧取得", notes = "特定のユーザーの購読しているページが属する製品一覧を取得")
	@ApiResponses(value = { @ApiResponse(code = Result.CODE_OK, response = ResultProductList.class, message = "OK"),
			@ApiResponse(code = Result.CODE_NOT_FOUND, response = Message.class, message = "指定したコンテンツが見つかりません") })
	public Response getSubscribedProducts(
			@ApiParam(value = "確認対象のユーザー名", required = true) @PathParam("id") String userId) {

		Result result = data.getSubscribedProductList(userId);
		return Response.status(result.getCode()).entity(result).build();
	}

	/**
	 * ユーザーが購読するページのうち、特定製品に紐づくページをすべて購読解除する
	 *
	 * @param userId
	 *            購読ページを解除するユーザID（いずれはCookieなど）
	 * @param prodId
	 *            購読解除対象の製品のID
	 * @return 購読解除後の購読ページ一覧
	 */
	@Path("/{id}/products/{prodId}")
	@DELETE
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "購読ページの製品指定解除", notes = "ユーザーが購読するページのうち、特定製品に紐づくページをすべて購読解除する")
	@ApiResponses(value = { @ApiResponse(code = Result.CODE_OK, response = Message.class, message = "購読解除が完了しました"),
			@ApiResponse(code = Result.CODE_NOT_FOUND, response = Message.class, message = "指定したコンテンツが見つかりません") })
	public Response unsetSubscribeProduct(@ApiParam(value = "対象のユーザー名", required = true) @PathParam("id") String userId,
			@ApiParam(value = "購読解除対象の製品ID", required = true) @PathParam("prodId") String prodId) {

		Result result = data.cancelSubscribedProduct(userId, prodId);
		return Response.status(result.getCode()).entity(result).build();
	}
}
