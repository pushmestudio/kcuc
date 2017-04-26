package jp.pushmestudio.kcuc.rest;

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
import jp.pushmestudio.kcuc.model.ResultContent;
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
	 * @param query 検索キーワード
	 * @param products 取得対象の製品ID
	 * @param inurl 検索対象とするページのURL
	 * @param offset 結果取得のオフセット(表示開始位置)
	 * @param limit 検索結果取得件数
	 * @param lang サポートしている言語による絞り込み
	 * @param sort 日付による並び替え data:a or data:d
	 * @return 更新確認結果
	 */
	@Path("/pages")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "ページ検索", response = ResultSearchList.class, notes = "与えられたキーワードを元にページを購読する")
	@ApiResponses(value = { @ApiResponse(code = Result.CODE_CLIENT_ERROR, message = "Client Error"),
			@ApiResponse(code = Result.CODE_SERVER_ERROR, message = "Internal Server Error") })
	public Response searchPages(
			@ApiParam(value = "検索キーワード, スペース区切りでOR検索", required = true) @QueryParam("query") String query,
			@ApiParam(value = "取得対象の製品ID, カンマ区切りで複数指定可能") @QueryParam("products") String products,
			@ApiParam(value = "検索対象とするページのURL, カンマ区切りで複数指定可能") @QueryParam("inurl") String inurl,
			@ApiParam(value = "結果取得のオフセット(表示開始位置)") @QueryParam("offset") Integer offset,
			@ApiParam(value = "検索結果取得件数, 最大20, デフォルト10") @QueryParam("limit") Integer limit,
			@ApiParam(value = "サポートしている言語による絞り込み (e.g. ja)") @QueryParam("lang") String lang,
			@ApiParam(value = "日付による並び替え, 無指定時は検索キーワードへの関連度順, date:a or date:d にて指定") @QueryParam("sort") String sort) {

		Result result = data.searchPages(query, products, inurl, offset, limit, lang, sort);
		return Response.status(result.getCode()).entity(result).build();
	}

	/**
	 * ページキーに対応するページ内容を返す
	 *
	 * @param href
	 *            検索対象ページキー
	 * @param lang
	 *            言語コード(ISO 639-1)
	 *
	 * @return ページ内容
	 */
	@Path("/content")
	@GET
	@Produces({ MediaType.TEXT_HTML })
	@ApiOperation(value = "ページ内容検索", notes = "与えられたページキーに対応するHTMLを取得、言語指定時に対応した言語が存在しなかった場合は英語にて応答する")
	@ApiResponses(value = { @ApiResponse(code = Result.CODE_CLIENT_ERROR, message = "Client Error"),
			@ApiResponse(code = Result.CODE_SERVER_ERROR, message = "Internal Server Error") })
	public Response searchContent(@ApiParam(value = "検索対象ページキー", required = true) @QueryParam("href") String href,
			@ApiParam(value = "表示言語の指定(e.g. ja)") @QueryParam("lang") String lang) {

		Result result = data.searchContent(href, lang);
		return Response.status(result.getCode()).entity(((ResultContent) result).getPageRawHtml()).build();
	}
}
