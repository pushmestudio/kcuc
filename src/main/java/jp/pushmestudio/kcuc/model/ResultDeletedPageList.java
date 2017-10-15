package jp.pushmestudio.kcuc.model;

import java.util.ArrayList;
import java.util.List;

import jp.pushmestudio.kcuc.util.Result;

/**
 * 応答用のユーザーが元々購読していたが，本家KCから削除済となったページのリスト
 * Result系のクラスはSwaggerの応答にも用いられるため、数字・文字列・配列・リスト以外をメンバー変数に使用しないこと
 */
public class ResultDeletedPageList implements Result {
	private String userId;
	private List<DeletedPage> deletedPages;

	public ResultDeletedPageList(String userId) {
		this.userId = userId;
		this.deletedPages = new ArrayList<>();
	}

	public String getUserId() {
		return userId;
	}

	public List<DeletedPage> getDeletedPages() {
		return deletedPages;
	}

	public void setDeletedPages(List<DeletedPage> deletedPages) {
		this.deletedPages = deletedPages;
	}

	public void addDeletedPages(DeletedPage targetPage) {
		deletedPages.add(targetPage);
	}

	@Override
	public int getCode() {
		return Result.CODE_NORMAL;
	}
}

