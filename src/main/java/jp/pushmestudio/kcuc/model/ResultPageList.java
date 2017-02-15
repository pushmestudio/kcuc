package jp.pushmestudio.kcuc.model;

import java.util.ArrayList;
import java.util.List;

import jp.pushmestudio.kcuc.util.Result;

/**
 * 応答用のユーザーの購読しているページリスト
 * Result系のクラスはSwaggerの応答にも用いられるため、数字・文字列・配列・リスト以外をメンバー変数に使用しないこと
 */
public class ResultPageList implements Result {
	private String userId;
	private List<SubscribedPage> subscribedPages;

	public ResultPageList(String userId) {
		this.userId = userId;
		this.subscribedPages = new ArrayList<>();
	}

	public String getUserId() {
		return userId;
	}

	public List<SubscribedPage> getSubscribedPages() {
		return subscribedPages;
	}

	public void setSubscribedPages(List<SubscribedPage> subscribedPages) {
		this.subscribedPages = subscribedPages;
	}

	public void addSubscribedPage(SubscribedPage targetPage) {
		subscribedPages.add(targetPage);
	}

	@Override
	public int getCode() {
		return Result.CODE_NORMAL;
	}
}
