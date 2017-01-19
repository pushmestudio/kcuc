package jp.pushmestudio.kcuc.model;

import java.util.ArrayList;
import java.util.List;

import jp.pushmestudio.kcuc.util.Result;

/**
 * 応答用のユーザーの購読しているページリスト
 */
public class ResultPageList implements Result {
	private String id;
	private List<SubscribedPage> subscribedPages;

	public ResultPageList(String id) {
		this.id = id;
		this.subscribedPages = new ArrayList<>();
	}

	public String getUserName() {
		return id;
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
