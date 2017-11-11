package jp.pushmestudio.kcuc.model;

import java.util.ArrayList;
import java.util.List;

import jp.pushmestudio.kcuc.util.Result;

/**
 * 応答用のユーザーの購読しているページリスト
 * Result系のクラスはSwaggerの応答にも用いられるため、数字・文字列・配列・リスト以外をメンバー変数に使用しないこと
 */
public class ResultPageList extends Result {
	private String userId;
	private List<SubscribedPage> subscribedPages;
	private List<SubscribedPage> unsubscribedPages;

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
	
	public List<SubscribedPage> getUnSubscribedPages() {
		return unsubscribedPages;
	}

	public void setUnSubscribedPages(List<SubscribedPage> unsubscribedPages) {
		this.unsubscribedPages = unsubscribedPages;
	}
}
