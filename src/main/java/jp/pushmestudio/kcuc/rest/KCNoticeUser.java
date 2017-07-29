package jp.pushmestudio.kcuc.rest;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jp.pushmestudio.kcuc.controller.KCData;
import jp.pushmestudio.kcuc.util.Message;
import jp.pushmestudio.kcuc.util.Result;

/**
 * 検索系
 */
@Api(value = "kcuc")
@Path("/users")
public class KCNoticeUser {
	// 実際に取得処理などを行うオブジェクト
	KCData data = new KCData();

	/**
	 * 指定されたIDのユーザーを作成する
	 *
	 * @param userId
	 *            作成ユーザーのID
	 * @return 更新確認結果
	 */
	@Path("/{id}")
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "ユーザー作成", response = Message.class, code = Result.CODE_CLOUDANT_UPDATE, notes = "与えられたIDを元にユーザーを作成する")
	@ApiResponses(value = { @ApiResponse(code = Result.CODE_SERVER_ERROR, message = "Internal Server Error") })
	public Response createUser(@ApiParam(value = "ユーザーID", required = true) @PathParam("id") String userId) {

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
	@ApiOperation(value = "ユーザー削除", response = Message.class, notes = "与えられたIDを元にユーザーを削除する")
	@ApiResponses(value = { @ApiResponse(code = Result.CODE_SERVER_ERROR, message = "Internal Server Error") })
	public Response deleteUser(@ApiParam(value = "ユーザーID", required = true) @PathParam("id") String userId) {

		Result result = data.deleteUser(userId);
		return Response.status(result.getCode()).entity(result).build();
	}
}
