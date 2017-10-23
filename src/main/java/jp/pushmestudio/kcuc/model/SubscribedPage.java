package jp.pushmestudio.kcuc.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
	private String encodedHref;

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
		this.encodedHref = this.encodeString(pageHref);
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

	public String getEncodedHref() {
		return encodedHref;
	}

	public void setIsUpdated(boolean isUpdated) {
		this.isUpdated = isUpdated;
	}

	public void setUpdatedTime(long updatedTime) {
		this.updatedTime = updatedTime;
	}

	private String encodeString(String decoded) {
		String encoded = "";
		try {
			encoded = URLEncoder.encode(decoded, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encoded;
	}

	// @see https://github.com/cloudant/java-cloudant
	// toStringをoverrideする実装の際に参考にしたもの
	public String toString() {
		return "{pageHref: " + pageHref + ",\npageName: " + pageName + ",\nisUpdated: " + isUpdated + ",\nupdatedTime: "
				+ updatedTime + ",\nprodId: " + prodId + ",\nprodName: " + prodName + ",\nencodedHref: " + encodedHref
				+ "\n}";
	}
}
