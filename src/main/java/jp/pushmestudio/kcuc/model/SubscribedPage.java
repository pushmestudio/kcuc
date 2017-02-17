package jp.pushmestudio.kcuc.model;

/**
 * 購読ページ情報
 */
public class SubscribedPage {
	private String pageHref;
	private boolean isUpdated;
	private long updatedTime;
	private String prodId;
	private String prodName;

	public SubscribedPage(String pageHref, boolean isUpdated, long updatedTime, String prodName) {
		this.pageHref = pageHref;
		this.isUpdated = isUpdated;
		this.updatedTime = updatedTime;
		this.prodName = prodName;
	}

	public String getPageHref() {
		return pageHref;
	}

	public boolean getIsUpdated() {
		return isUpdated;
	}

	public long getUpdatedTime() {
		return updatedTime;
	}

	public String getProdName() {
		return prodName;
	}

	public boolean setIsUpdated(boolean isUpdated) {
		return this.isUpdated = isUpdated;
	}

	public String toString() {
		return "{pageHref: " + pageHref + ",\nisUpdated: " + isUpdated + ",\nupdatedTime: " + updatedTime
				+ ",\nprodName: " + prodName + "\n}";
	}

}
