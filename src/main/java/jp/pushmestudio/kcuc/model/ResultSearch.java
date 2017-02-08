package jp.pushmestudio.kcuc.model;

import jp.pushmestudio.kcuc.util.Result;

/**
 * 応答用の検索結果一覧
 */
public class ResultSearch implements Result {

	private String date;
	private String href;
	private String label;
	private String summary;
	private String products[];
	private String topics[];
	@Override
	public int getCode() {
		return Result.CODE_NORMAL;
	}

}
