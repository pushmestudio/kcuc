package jp.pushmestudio.kcuc.model;

import java.util.List;

import jp.pushmestudio.kcuc.util.Result;

/**
 * 
 */
public class ResultPageList implements Result {
	private String userName;
	private List<SubscribedPage> subscribedPages;

	public ResultPageList(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public List<SubscribedPage> getSubscribedPages() {
		return subscribedPages;
	}

	public void addSubscribedPages(SubscribedPage targetPage) {
		subscribedPages.add(targetPage);
	}

	@Override
	public int getCode() {
		return Result.CODE_NORMAL;
	}
}
