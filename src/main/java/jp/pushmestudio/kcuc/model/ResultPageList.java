package jp.pushmestudio.kcuc.model;

import java.util.ArrayList;
import java.util.List;

import jp.pushmestudio.kcuc.util.Result;

/**
 * 応答用のユーザーの購読しているページリスト
 */
public class ResultPageList implements Result {
	private String userName;
	private List<SubscribedPage> subscribedPages;

	public ResultPageList(String userName) {
		this.userName = userName;
		this.subscribedPages = new ArrayList<>();
	}

	public String getUserName() {
		return userName;
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