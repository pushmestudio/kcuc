package jp.pushmestudio.kcuc.model;

import java.util.List;

/**
 * Cloudantに登録されているユーザーごとの購読情報
 */
public class UserDocument {
	private String _id;
	private String _rev;
	private String userName;
	private List<SubscribedPage> subscribedPages;

	// コンストラクタ(未使用)
	public UserDocument(String userName) {
		this.userName = userName;
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

	public void addSubscribedPages(SubscribedPage targetPage) {
		subscribedPages.add(targetPage);
	}

	public String toString() {
		return "{id: " + _id + ",\nrev: " + _rev + ",\nuserName: " + userName + ",\nsubscribedPages: " + subscribedPages
				+ "\n}";
	}
}
