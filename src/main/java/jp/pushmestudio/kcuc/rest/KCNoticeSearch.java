package jp.pushmestudio.kcuc.rest;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
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
import jp.pushmestudio.kcuc.model.ResultSearchList;
import jp.pushmestudio.kcuc.util.Result;

/**
 * 検索系
 */
@Api(value = "kcuc")
@Path("/search")
public class KCNoticeSearch {
	// 実際に取得処理などを行うオブジェクト
	KCData data = new KCData();

	/**
	 * 検索キーワードにマッチするページを検索して返す
	 * 
	 * @param query
	 *            検索キーワード
	 * @return 更新確認結果
	 */
	@Path("/pages")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "ページ検索", response = ResultSearchList.class, notes = "与えられたキーワードを元にページを購読する")
	@ApiResponses(value = { @ApiResponse(code = Result.CODE_CLIENT_ERROR, message = "Client Error"),
			@ApiResponse(code = Result.CODE_SERVER_ERROR, message = "Internal Server Error") })
	public Response searchPages(
			@ApiParam(value = "検索キーワード、スペース区切りでOR検索", required = true) @QueryParam("query") @DefaultValue("") String query,
			@ApiParam(value = "取得対象の製品ID、カンマ区切りで複数指定可能") @QueryParam("products") String products,
			@ApiParam(value = "検索対象とするページのURL、カンマ区切りで複数指定可能") @QueryParam("inurl") String inurl,
			@ApiParam(value = "結果取得のオフセット(表示開始位置)") @QueryParam("offset") Integer offset,
			@ApiParam(value = "検索結果取得件数, 最大20, デフォルト10") @QueryParam("limit") Integer limit,
			@ApiParam(value = "サポートしている言語による絞り込み (e.g. ja)") @QueryParam("lang") String lang) {

		Result result = data.searchPages(query, products, inurl, offset, limit, lang);
		return Response.status(result.getCode()).entity(result).build();
	}
}