package jp.pushmestudio.kcuc.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Cloudantに登録されているユーザーごとの購読情報
 */
public class UserDocument {
	private String _id;
	private String _rev;
	private String userId;
	private List<SubscribedPage> subscribedPages;
	private List<DeletedPage> deletedPages;

	public UserDocument(String userId) {
		this.userId = userId;
		this.subscribedPages = new ArrayList<>();
		this.deletedPages = new ArrayList<>();
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

	public void delSubscribedPage(int target) {
		subscribedPages.remove(target);
	}

	public void replaceSubscribedPages(List<SubscribedPage> pages) {
		subscribedPages = new ArrayList<>(pages);
	}

	public List<DeletedPage> getDeletedPages() {
		return deletedPages;
	}

	public void addDeletedPages(DeletedPage targetPage) {
		deletedPages.add(targetPage);
	}
	
	
	// @see https://github.com/cloudant/java-cloudant
	// toStringをoverrideする実装の際に参考にしたもの
	public String toString() {
		return "{id: " + _id + ",\nrev: " + _rev + ",\nuserId: " + userId + ",\nsubscribedPages: " + subscribedPages
				+ ",\ndeletedPages: " + deletedPages
				+"\n}";
	}
}
