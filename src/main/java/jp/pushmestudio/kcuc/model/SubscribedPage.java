package jp.pushmestudio.kcuc.model;

/**
 * 購読ページ情報
 */
public class SubscribedPage {
	private String pageHref;
	private Boolean isUpdated;
	private String updatedTime;
	private String prodId;

	// コンストラクタ(未使用)
	public SubscribedPage() {
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

	public String getProdId() {
		return prodId;
	}
	
	public String toString() {
		return "{pageHref: " + pageHref + ",\nisUpdated: " + isUpdated + ",\nupdatedTime: " 
				+ updatedTime + ",\nprodId: " + prodId + "\n}";
	}

}
