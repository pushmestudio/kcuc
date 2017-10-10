package jp.pushmestudio.kcuc.model;

import jp.pushmestudio.kcuc.util.Result;

/**
 * 応答用のページ内容 Result系のクラスはSwaggerの応答にも用いられるため、数字・文字列・配列・リスト以外をメンバー変数に使用しない
 */
public class ResultContent implements Result {
	private String pageRawHtml;

	/**
	 * @param pageRawHtml
	 *            ページ内容を示すHTMLドキュメントを文字列として持ったもの
	 */
	public ResultContent(String pageRawHtml) {
		this.pageRawHtml = pageRawHtml;
	}

	@Override
	public int getCode() {
		return Result.CODE_OK;
	}

	public String getPageRawHtml() {
		return pageRawHtml;
	}

	public void setPageRawHtml(String pageRawHtml) {
		this.pageRawHtml = pageRawHtml;
	}

}
