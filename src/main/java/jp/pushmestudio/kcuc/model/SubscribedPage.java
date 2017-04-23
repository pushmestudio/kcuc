package jp.pushmestudio.kcuc.model;

/**
 * 購読ページ情報
 */
public class SubscribedPage {
	private String pageHref;
	private String pageName;
	private boolean isUpdated;
	private long updatedTime;
	private String prodId;
	private String prodName;

	/**
	 * @param pageHref
	 *            ページキー
	 * @param pageName
	 *            ページラベル
	 * @param prodId
	 *            製品ID
	 * @param prodName
	 *            製品名
	 */
	public SubscribedPage(String pageHref, String pageName, String prodId, String prodName) {
		this.pageHref = pageHref;
		this.pageName = pageName;
		this.prodId = prodId;
		this.prodName = prodName;
	}

	public String getPageHref() {
		return pageHref;
	}

	public String getPageName() {
		return pageName;
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

	public void setIsUpdated(boolean isUpdated) {
		this.isUpdated = isUpdated;
	}

	public void setUpdatedTime(long updatedTime) {
		this.updatedTime = updatedTime;
	}

	// @see https://github.com/cloudant/java-cloudant
	// toStringをoverrideする実装の際に参考にしたもの
	public String toString() {
		return "{pageHref: " + pageHref + ",\npageName: " + pageName + ",\nisUpdated: " + isUpdated + ",\nupdatedTime: "
				+ updatedTime + ",\nprodId: " + prodId + ",\nprodName: " + prodName + "\n}";
	}
}
