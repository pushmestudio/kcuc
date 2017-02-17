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

	public SubscribedPage(String pageHref, boolean isUpdated, long updatedTime, String prodId, String prodName) {
		this.pageHref = pageHref;
		this.isUpdated = isUpdated;
		this.updatedTime = updatedTime;
		this.prodId = prodId;
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

	public String getProdId() {
		return prodId;
	}

	public String getProdName() {
		return prodName;
	}

	public boolean setIsUpdated(boolean isUpdated) {
		return this.isUpdated = isUpdated;
	}

	// TODO toStringをoverrideしてる目的を明らかにする, cloudantで使用するため, か？
	public String toString() {
		return "{pageHref: " + pageHref + ",\nisUpdated: " + isUpdated + ",\nupdatedTime: " + updatedTime
				+ ",\nprodId: " + prodId + ",\nprodName: " + prodName + "\n}";
	}
}
