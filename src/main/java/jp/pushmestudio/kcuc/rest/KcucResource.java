package jp.pushmestudio.kcuc.rest;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
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
import jp.pushmestudio.kcuc.controller.AppHandler;
import jp.pushmestudio.kcuc.model.ResultPageList;
import jp.pushmestudio.kcuc.model.ResultProductList;
import jp.pushmestudio.kcuc.model.ResultUserList;
import jp.pushmestudio.kcuc.util.Message;
import jp.pushmestudio.kcuc.util.Result;

@Api(value = "User and user content management")
@Path("/users")
public class KcucResource {

	// 実際に取得処理などを行うオブジェクト
	AppHandler appHandler = new AppHandler();

	/**
	 * 特定のページを購読しているユーザー一覧を取得・確認
	 *
	 * @param pageHref
	 *            更新確認対象のページキー
	 * @param time
	 *            更新判断の基準時間
	 * @return 更新確認結果
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "ユーザー一覧取得", notes = "特定のページを購読しているユーザー一覧を取得・確認")
	@ApiResponses(value = { @ApiResponse(code = Result.CODE_OK, response = ResultUserList.class, message = "OK"),
			@ApiResponse(code = Result.CODE_NOT_FOUND, response = Message.class, message = "指定したコンテンツが見つかりません") })
	public Response getUpdatedUsers(
			@ApiParam(value = "更新確認対象のページキー", required = true) @QueryParam("pageHref") String pageHref,
			@ApiParam(value = "更新判断の基準時間, ここで入力されたタイムスタンプよりも後に更新されていれば更新ありとみなす, デフォルトは1週間前時点", required = false) @QueryParam("time") Long time) {

		Result result = appHandler.checkUpdateByPage(pageHref, time);
		return Response.status(result.getCode()).entity(result).build();
	}

	/**
	 * 指定されたIDのユーザーを作成する
	 *
	 * @param userId
	 *            作成ユーザーのID
	 * @return 更新確認結果
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "ユーザー作成", notes = "与えられたIDを元にユーザーを作成する")
	@ApiResponses(value = { @ApiResponse(code = Result.CODE_OK, response = Message.class, message = "OK"),
			@ApiResponse(code = Result.CODE_BAD_REQUEST, response = Message.class, message = "パラメーターが不正です"),
			@ApiResponse(code = Result.CODE_CONFLICT, response = Message.class, message = "既に存在しています") })
	public Response createUser(@ApiParam(value = "ユーザーID", required = true) @FormParam("userId") String userId) {

		Result result = appHandler.createUser(userId);
		return Response.status(result.getCode()).entity(result).build();
	}

	/**
	 * 指定されたIDのユーザーを削除する
	 *
	 * @param userId
	 *            削除ユーザーのID
	 * @return 更新確認結果
	 */
	@Path("{userId}")
	@DELETE
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "ユーザー削除", notes = "与えられたIDを元にユーザーを削除する")
	@ApiResponses(value = { @ApiResponse(code = Result.CODE_OK, response = Message.class, message = "ユーザー削除が完了しました"),
			@ApiResponse(code = Result.CODE_BAD_REQUEST, response = Message.class, message = "パラメーターが不正です"),
			@ApiResponse(code = Result.CODE_NOT_FOUND, response = Message.class, message = "指定したコンテンツが見つかりません") })
	public Response deleteUser(@ApiParam(value = "ユーザーID", required = true) @PathParam("userId") String userId) {

		Result result = appHandler.deleteUser(userId);
		return Response.status(result.getCode()).entity(result).build();
	}

	/**
	 * 指定されたIDのユーザーを削除する、ヘッダーからユーザーIDを読み込む
	 *
	 * @param userId
	 *            削除ユーザーのID
	 * @return 更新確認結果
	 */
	@Path("me")
	@DELETE
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "ユーザー削除", notes = "与えられたIDを元にユーザーを削除する")
	@ApiResponses(value = { @ApiResponse(code = Result.CODE_OK, response = Message.class, message = "ユーザー削除が完了しました"),
			@ApiResponse(code = Result.CODE_BAD_REQUEST, response = Message.class, message = "パラメーターが不正です"),
			@ApiResponse(code = Result.CODE_NOT_FOUND, response = Message.class, message = "指定したコンテンツが見つかりません") })
	public Response deleteUserMe(@ApiParam(value = "ユーザーID", required = true) @HeaderParam("user-id") String userId) {

		Result result = appHandler.deleteUser(userId);
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
	@Path("{userId}/pages")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "ページ一覧取得", notes = "特定のユーザーの購読しているページ一覧を取得・確認")
	@ApiResponses(value = { @ApiResponse(code = Result.CODE_OK, response = ResultPageList.class, message = "OK"),
			@ApiResponse(code = Result.CODE_NOT_FOUND, response = Message.class, message = "指定したコンテンツが見つかりません") })
	public Response getUpdatedPages(
			@ApiParam(value = "更新確認対象のユーザー名", required = true) @PathParam("userId") String userId,
			@ApiParam(value = "更新確認対象の製品ID", required = false) @QueryParam("prodId") String prodId,
			@ApiParam(value = "更新判断の基準時間, ここで入力されたタイムスタンプよりも後に更新されていれば更新ありとみなす, デフォルトは1週間前時点", required = false) @QueryParam("time") Long time) {

		Result result = appHandler.checkUpdateByUser(userId, prodId, time);
		return Response.status(result.getCode()).entity(result).build();
	}

	/**
	 * 特定のユーザーの購読しているページ一覧を取得・確認、ヘッダーからユーザーIDを読み込む
	 *
	 * @param userId
	 *            更新確認対象のユーザーID
	 * @param prodId
	 *            確認対象の製品ID
	 * @param time
	 *            更新判断の基準時間
	 * @return 更新確認結果
	 */
	@Path("me/pages")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "ページ一覧取得", notes = "特定のユーザーの購読しているページ一覧を取得・確認")
	@ApiResponses(value = { @ApiResponse(code = Result.CODE_OK, response = ResultPageList.class, message = "OK"),
			@ApiResponse(code = Result.CODE_NOT_FOUND, response = Message.class, message = "指定したコンテンツが見つかりません") })
	public Response getUpdatedPagesMe(
			@ApiParam(value = "更新確認対象のユーザー名", required = true) @HeaderParam("user-id") String userId,
			@ApiParam(value = "更新確認対象の製品ID", required = false) @QueryParam("prodId") String prodId,
			@ApiParam(value = "更新判断の基準時間, ここで入力されたタイムスタンプよりも後に更新されていれば更新ありとみなす, デフォルトは1週間前時点", required = false) @QueryParam("time") Long time) {

		Result result = appHandler.checkUpdateByUser(userId, prodId, time);
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
	@Path("{userId}/pages")
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "購読ページ追加", notes = "特定のユーザの購読するページを追加・確認")
	@ApiResponses(value = { @ApiResponse(code = Result.CODE_OK, response = Message.class, message = "OK"),
			@ApiResponse(code = Result.CODE_BAD_REQUEST, response = Message.class, message = "パラメーターが不正です"),
			@ApiResponse(code = Result.CODE_NOT_FOUND, response = Message.class, message = "指定したコンテンツが見つかりません"),
			@ApiResponse(code = Result.CODE_CONFLICT, response = Message.class, message = "既に存在しています") })
	public Response setSubscribe(@ApiParam(value = "更新対象のユーザー名", required = true) @PathParam("userId") String userId,
			@ApiParam(value = "購読対象のページキー", required = true) @FormParam("pageHref") String pageHref) {

		Result result = appHandler.registerSubscribedPage(userId, pageHref);
		return Response.status(result.getCode()).entity(result).build();
	}

	/**
	 * 特定のユーザの購読するページを追加・確認、ヘッダーからユーザーIDを読み込む
	 *
	 * @param userId
	 *            購読ページを登録するユーザID（いずれはCookieなど）
	 * @param pageHref
	 *            購読登録対象のページキー
	 * @return 登録確認結果
	 */
	@Path("me/pages")
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "購読ページ追加", notes = "特定のユーザの購読するページを追加・確認")
	@ApiResponses(value = { @ApiResponse(code = Result.CODE_OK, response = Message.class, message = "OK"),
			@ApiResponse(code = Result.CODE_BAD_REQUEST, response = Message.class, message = "パラメーターが不正です"),
			@ApiResponse(code = Result.CODE_NOT_FOUND, response = Message.class, message = "指定したコンテンツが見つかりません"),
			@ApiResponse(code = Result.CODE_CONFLICT, response = Message.class, message = "既に存在しています") })
	public Response setSubscribeMe(
			@ApiParam(value = "更新対象のユーザー名", required = true) @HeaderParam("user-id") String userId,
			@ApiParam(value = "購読対象のページキー", required = true) @FormParam("pageHref") String pageHref) {

		Result result = appHandler.registerSubscribedPage(userId, pageHref);
		return Response.status(result.getCode()).entity(result).build();
	}

	/**
	 * 特定のユーザの購読するページを解除
	 *
	 * @param userId
	 *            購読ページを解除するユーザID（いずれはCookieなど）
	 * @param encodedHref
	 *            エンコード済みの購読解除対象のページキー
	 * @return 購読解除後の購読ページ一覧
	 */
	@Path("{userId}/pages/{encodedHref}")
	@DELETE
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "購読ページ解除", notes = "特定のユーザの購読するページを解除")
	@ApiResponses(value = { @ApiResponse(code = Result.CODE_OK, response = Message.class, message = "購読解除が完了しました"),
			@ApiResponse(code = Result.CODE_NOT_FOUND, response = Message.class, message = "指定したコンテンツが見つかりません") })
	public Response unsetSubscribe(@ApiParam(value = "対象のユーザー名", required = true) @PathParam("userId") String userId,
			@ApiParam(value = "エンコード済みの購読解除対象のページキー", required = true) @PathParam("encodedHref") String encodedHref) {

		Result result = appHandler.deleteSubscribedPage(userId, encodedHref);
		return Response.status(result.getCode()).entity(result).build();
	}

	/**
	 * 特定のユーザの購読するページを解除、ヘッダーからユーザーIDを読み込む
	 *
	 * @param userId
	 *            購読ページを解除するユーザID（いずれはCookieなど）
	 * @param encodedHref
	 *            エンコード済みの購読解除対象のページキー
	 * @return 購読解除後の購読ページ一覧
	 */
	@Path("me/pages/{encodedHref}")
	@DELETE
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "購読ページ解除", notes = "特定のユーザの購読するページを解除")
	@ApiResponses(value = { @ApiResponse(code = Result.CODE_OK, response = Message.class, message = "購読解除が完了しました"),
			@ApiResponse(code = Result.CODE_NOT_FOUND, response = Message.class, message = "指定したコンテンツが見つかりません") })
	public Response unsetSubscribeMe(
			@ApiParam(value = "対象のユーザー名", required = true) @HeaderParam("user-id") String userId,
			@ApiParam(value = "エンコード済みの購読解除対象のページキー", required = true) @PathParam("encodedHref") String encodedHref) {

		Result result = appHandler.deleteSubscribedPage(userId, encodedHref);
		return Response.status(result.getCode()).entity(result).build();
	}

	/**
	 * 特定のユーザーの購読しているページ一覧を取得・確認
	 *
	 * @param userId
	 *            更新確認対象のユーザーID
	 * @return 更新確認結果
	 */
	@Path("{userId}/products")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "購読製品一覧取得", notes = "特定のユーザーの購読しているページが属する製品一覧を取得")
	@ApiResponses(value = { @ApiResponse(code = Result.CODE_OK, response = ResultProductList.class, message = "OK"),
			@ApiResponse(code = Result.CODE_NOT_FOUND, response = Message.class, message = "指定したコンテンツが見つかりません") })
	public Response getSubscribedProducts(
			@ApiParam(value = "確認対象のユーザー名", required = true) @PathParam("userId") String userId) {

		Result result = appHandler.getSubscribedProductList(userId);
		return Response.status(result.getCode()).entity(result).build();
	}

	/**
	 * 特定のユーザーの購読しているページ一覧を取得・確認、ヘッダーからユーザーIDを読み込む
	 *
	 * @param userId
	 *            更新確認対象のユーザーID
	 * @return 更新確認結果
	 */
	@Path("me/products")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "購読製品一覧取得", notes = "特定のユーザーの購読しているページが属する製品一覧を取得")
	@ApiResponses(value = { @ApiResponse(code = Result.CODE_OK, response = ResultProductList.class, message = "OK"),
			@ApiResponse(code = Result.CODE_NOT_FOUND, response = Message.class, message = "指定したコンテンツが見つかりません") })
	public Response getSubscribedProductsMe(
			@ApiParam(value = "確認対象のユーザー名", required = true) @HeaderParam("user-id") String userId) {

		Result result = appHandler.getSubscribedProductList(userId);
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
	@Path("{userId}/products/{prodId}")
	@DELETE
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "購読ページの製品指定解除", notes = "ユーザーが購読するページのうち、特定製品に紐づくページをすべて購読解除する")
	@ApiResponses(value = { @ApiResponse(code = Result.CODE_OK, response = Message.class, message = "購読解除が完了しました"),
			@ApiResponse(code = Result.CODE_NOT_FOUND, response = Message.class, message = "指定したコンテンツが見つかりません") })
	public Response unsetSubscribeProduct(
			@ApiParam(value = "対象のユーザー名", required = true) @PathParam("userId") String userId,
			@ApiParam(value = "購読解除対象の製品ID", required = true) @PathParam("prodId") String prodId) {

		Result result = appHandler.cancelSubscribedProduct(userId, prodId);
		return Response.status(result.getCode()).entity(result).build();
	}

	/**
	 * ユーザーが購読するページのうち、特定製品に紐づくページをすべて購読解除する、ヘッダーからユーザーIDを読み込む
	 *
	 * @param userId
	 *            購読ページを解除するユーザID（いずれはCookieなど）
	 * @param prodId
	 *            購読解除対象の製品のID
	 * @return 購読解除後の購読ページ一覧
	 */
	@Path("me/products/{prodId}")
	@DELETE
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "購読ページの製品指定解除", notes = "ユーザーが購読するページのうち、特定製品に紐づくページをすべて購読解除する")
	@ApiResponses(value = { @ApiResponse(code = Result.CODE_OK, response = Message.class, message = "購読解除が完了しました"),
			@ApiResponse(code = Result.CODE_NOT_FOUND, response = Message.class, message = "指定したコンテンツが見つかりません") })
	public Response unsetSubscribeProductMe(
			@ApiParam(value = "対象のユーザー名", required = true) @HeaderParam("user-id") String userId,
			@ApiParam(value = "購読解除対象の製品ID", required = true) @PathParam("prodId") String prodId) {

		Result result = appHandler.cancelSubscribedProduct(userId, prodId);
		return Response.status(result.getCode()).entity(result).build();
	}
}
