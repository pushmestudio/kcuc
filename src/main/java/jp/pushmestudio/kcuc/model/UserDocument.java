package jp.pushmestudio.kcuc.model;

import java.util.List;

/**
 * 登録ユーザー情報
 */
public class UserDocument {
	private String _id;
	private String _rev;
	private String userName;
	private List<SubscribedPage> subscribedPages;

	public UserDocument(String userName) {
		this.userName = userName;
		// this.subscribedPages = null;
	}
	
	public String getId() {
		return _id;
	}

	public String getRev() {
		return _rev;
	}
	
	public String getUserName() {
		return userName;
	}

	public List<SubscribedPage> getSubscribedPages() {
		return subscribedPages;
	}

	public String toString() {
		return "{id: " + _id + ",\nrev: " + _rev + ",\nuserName: " + userName + ",\nsubscribedPages: " + subscribedPages + "\n}";
	}
}
