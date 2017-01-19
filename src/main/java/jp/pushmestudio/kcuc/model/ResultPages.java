package jp.pushmestudio.kcuc.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class ResultPages {
	private String href;
	private List<UserInfo> subscribers;

	public ResultPages(String href) {
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
}
