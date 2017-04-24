package jp.pushmestudio.kcuc.rest;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
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

@Api(value = "kcuc")
@Path("/check")
public class KCNoticeResource {

	// TODO 公開していないパスへのアクセスが発生した際などのリターンをエラーハンドリングとして指定する必要あり

	// 実際に取得処理などを行うオブジェクト
	KCData data = new KCData();

	/**
	 * 特定のページを購読しているユーザー一覧を取得・確認
	 * 
	 * @param href
	 *            更新確認対象のページキー
	 * @return 更新確認結果
	 */
	@Path("/users")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "ユーザー一覧取得", response = ResultUserList.class, notes = "特定のページを購読しているユーザー一覧を取得・確認")
	@ApiResponses(value = { @ApiResponse(code = Result.CODE_CLIENT_ERROR, message = "Client Error"),
			@ApiResponse(code = Result.CODE_SERVER_ERROR, message = "Internal Server Error") })
	public Response getUpdatedUsers(@ApiParam(value = "更新確認対象のページキー", required = true) @QueryParam("href") String href,
			@ApiParam(value = "更新判断の基準時間, ここで入力されたタイムスタンプよりも後に更新されていれば更新ありとみなす, デフォルトは1週間前時点", required = false) @QueryParam("time") Long time) {

		Result result = data.checkUpdateByPage(href, time);
		return Response.status(result.getCode()).entity(result).build();
	}

	/**
	 * 特定のユーザーの購読しているページ一覧を取得・確認
	 * 
	 * @param user
	 *            更新確認対象のユーザー名
	 * @param product
	 *            確認対象の製品ID
	 * @return 更新確認結果
	 */
	@Path("/pages")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "ページ一覧取得", response = ResultPageList.class, notes = "特定のユーザーの購読しているページ一覧を取得・確認")
	@ApiResponses(value = { @ApiResponse(code = Result.CODE_CLIENT_ERROR, message = "Client Error"),
			@ApiResponse(code = Result.CODE_SERVER_ERROR, message = "Internal Server Error") })
	public Response getUpdatedPages(@ApiParam(value = "更新確認対象のユーザー名", required = true) @QueryParam("user") String user,
			@ApiParam(value = "更新確認対象の製品ID", required = false) @QueryParam("product") String product,
			@ApiParam(value = "更新判断の基準時間, ここで入力されたタイムスタンプよりも後に更新されていれば更新ありとみなす, デフォルトは1週間前時点", required = false) @QueryParam("time") Long time) {

		Result result = data.checkUpdateByUser(user, product, time);
		return Response.status(result.getCode()).entity(result).build();
	}

	/**
	 * 特定のユーザの購読するページを追加・確認
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
	@ApiOperation(value = "購読ページ追加", response = Message.class, notes = "特定のユーザの購読するページを追加・確認")
	@ApiResponses(value = { @ApiResponse(code = Result.CODE_CLIENT_ERROR, message = "Client Error"),
			@ApiResponse(code = Result.CODE_SERVER_ERROR, message = "Internal Server Error") })
	public Response setSubscribe(@ApiParam(value = "更新対象のユーザー名", required = true) @FormParam("user") String user,
			@ApiParam(value = "購読対象のページキー", required = true) @FormParam("href") String href) {

		Result result = data.registerSubscribedPage(user, href);
		return Response.status(result.getCode()).entity(result).build();
	}

	/**
	 * 特定のユーザの購読するページを解除
	 * 
	 * @param user
	 *            購読ページを解除するユーザ（いずれはCookieなど）
	 * @param href
	 *            購読解除対象のページキー
	 * @return 購読解除後の購読ページ一覧
	 */
	@Path("/pages")
	@PUT
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "購読ページ解除", response = Message.class, notes = "特定のユーザの購読するページを解除")
	@ApiResponses(value = { @ApiResponse(code = Result.CODE_CLIENT_ERROR, message = "Client Error"),
			@ApiResponse(code = Result.CODE_SERVER_ERROR, message = "Internal Server Error") })
	public Response unsetSubscribe(@ApiParam(value = "対象のユーザー名", required = true) @FormParam("user") String user,
			@ApiParam(value = "購読解除対象のページキー", required = true) @FormParam("href") String href) {

		Result result = data.deleteSubscribedPage(user, href);
		return Response.status(result.getCode()).entity(result).build();
	}

	/**
	 * 特定のユーザーの購読しているページ一覧を取得・確認
	 * 
	 * @param user
	 *            更新確認対象のユーザー名
	 * @return 更新確認結果
	 */
	@Path("/products")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "購読製品一覧取得", response = ResultProductList.class, notes = "特定のユーザーの購読しているページが属する製品一覧を取得")
	@ApiResponses(value = { @ApiResponse(code = Result.CODE_CLIENT_ERROR, message = "Client Error"),
			@ApiResponse(code = Result.CODE_SERVER_ERROR, message = "Internal Server Error") })
	public Response getSubscribedProducts(
			@ApiParam(value = "確認対象のユーザー名", required = true) @QueryParam("user") String user) {

		Result result = data.getSubscribedProductList(user);
		return Response.status(result.getCode()).entity(result).build();
	}

	/**
	 * ユーザーが購読するページのうち、特定製品に紐づくページをすべて購読解除する
	 * 
	 * @param user
	 *            購読ページを解除するユーザ（いずれはCookieなど）
	 * @param product
	 *            購読解除対象の製品のID
	 * @return 購読解除後の購読ページ一覧
	 */
	@Path("/products")
	@PUT
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "購読ページの製品指定解除", response = Message.class, notes = "ユーザーが購読するページのうち、特定製品に紐づくページをすべて購読解除する")
	@ApiResponses(value = { @ApiResponse(code = Result.CODE_CLIENT_ERROR, message = "Client Error"),
			@ApiResponse(code = Result.CODE_SERVER_ERROR, message = "Internal Server Error") })
	public Response unsetSubscribeProduct(
			@ApiParam(value = "対象のユーザー名", required = true) @FormParam("user") String userId,
			@ApiParam(value = "購読解除対象の製品ID", required = true) @FormParam("product") String productId) {

		Result result = data.cancelSubscribedProduct(userId, productId);
		return Response.status(result.getCode()).entity(result).build();
	}

}
