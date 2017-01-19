package jp.pushmestudio.kcuc.model;

import java.util.ArrayList;
import java.util.List;

import jp.pushmestudio.kcuc.util.Result;

/**
 * 
 */
public class ResultUserList implements Result {
	private String href;
	private List<UserInfo> subscribers;

	public ResultUserList(String href) {
		this.href = href;
		this.subscribers = new ArrayList<>();
	}

	public String getHref() {
		return href;
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
