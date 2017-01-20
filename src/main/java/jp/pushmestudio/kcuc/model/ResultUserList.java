package jp.pushmestudio.kcuc.model;

import java.util.ArrayList;
import java.util.List;

import jp.pushmestudio.kcuc.util.Result;

/**
 * 応答用の特定ページを購読しているユーザーリスト
 */
public class ResultUserList implements Result {
	private String pageHref;
	private List<UserInfo> subscribers;

	public ResultUserList(String pageHref) {
		this.pageHref = pageHref;
		this.subscribers = new ArrayList<>();
	}

	public String getPageHref() {
		return pageHref;
	}

	public List<UserInfo> getSubscribers() {
		return subscribers;
	}

	public void addSubscriber(UserInfo user) {
		subscribers.add(user);
	}

	@Override
	public int getCode() {
		return Result.CODE_NORMAL;
	}
}