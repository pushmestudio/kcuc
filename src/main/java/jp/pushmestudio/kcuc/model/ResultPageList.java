package jp.pushmestudio.kcuc.model;

import java.util.ArrayList;
import java.util.List;

import jp.pushmestudio.kcuc.util.Result;

/**
 * 応答用のユーザーの購読しているページリスト
 */
public class ResultPageList implements Result {
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

	
	@Override
	public int getCode() {
		return Result.CODE_NORMAL;
	}
}
