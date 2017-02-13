package jp.pushmestudio.kcuc.model;

import java.util.List;

/**
 * Cloudantに登録されているユーザーごとの購読情報
 */
public class UserDocument {
	private String _id;
	private String _rev;
	private String userId;
	private List<SubscribedPage> subscribedPages;

	// コンストラクタ(未使用)
	public UserDocument(String userId) {
		this.userId = userId;
	}

	public String getId() {
		return _id;
	}

	public String getRev() {
		return _rev;
	}

	public String getUserId() {
		return userId;
	}

	public List<SubscribedPage> getSubscribedPages() {
		return subscribedPages;
	}

	public void addSubscribedPages(SubscribedPage targetPage) {
		subscribedPages.add(targetPage);
	}
	
	public SubscribedPage delSubscribedPage(int target) {
		return subscribedPages.remove(target);
	}

	public String toString() {
		return "{id: " + _id + ",\nrev: " + _rev + ",\nuserId: " + userId + ",\nsubscribedPages: " + subscribedPages
				+ "\n}";
	}
}
