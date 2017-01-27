package jp.pushmestudio.kcuc.model;

/**
 * 購読ページ情報
 */
public class SubscribedPage {
	private String pageHref;
	private Boolean isUpdated;
	private String updatedTime;
	private String prodName;

	// コンストラクタ(未使用)
	public SubscribedPage(String pageHref, Boolean isUpdated, String updatedTime, String prodName) {
		this.pageHref = pageHref;
		this.isUpdated = isUpdated;
		this.updatedTime = updatedTime;
		this.prodName = prodName;
	}

	public String getPageHref() {
		return pageHref;
	}

	public Boolean getIsUpdated() {
		return isUpdated;
	}

	public String getUpdatedTime() {
		return updatedTime;
	}

	public String getProdName() {
		return prodName;
	}

	public Boolean setIsUpdated(boolean isUpdated) {
		return this.isUpdated = isUpdated;
	}

	public String toString() {
		return "{pageHref: " + pageHref + ",\nisUpdated: " + isUpdated + ",\nupdatedTime: " + updatedTime
				+ ",\nprodName: " + prodName + "\n}";
	}

}
