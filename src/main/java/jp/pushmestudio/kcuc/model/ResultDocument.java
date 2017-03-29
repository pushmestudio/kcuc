package jp.pushmestudio.kcuc.model;

import jp.pushmestudio.kcuc.util.Result;

/**
 * 応答用のページ内容
 * Result系のクラスはSwaggerの応答にも用いられるため、数字・文字列・配列・リスト以外をメンバー変数に使用しない
 */
public class ResultDocument implements Result {
	private String pagestr;

	/**
	 * @param str ページ内容を示す文字列
	 */
	public ResultDocument(String pagestr){
		this.pagestr = pagestr;
	}

	@Override
	public int getCode() {
		return Result.CODE_NORMAL;
	}

	public String getPagestr(){
		return pagestr;
	}

	public void setPagestr(String pagestr){
		this.pagestr = pagestr;
	}

}
