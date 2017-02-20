package jp.pushmestudio.kcuc.model;

import java.util.HashMap;
import java.util.Map;

import jp.pushmestudio.kcuc.util.Result;

/**
 * 応答用のユーザーの購読している製品リスト
 * Result系のクラスはSwaggerの応答にも用いられるため、数字・文字列・配列・リスト以外をメンバー変数に使用しないこと
 */
public class ResultProductList implements Result {
	private String userId;
	/** 製品名を重複して持たせないためHashMapにしている */
	private Map<String, String> subscribedProducts;

	public ResultProductList(String userId) {
		this.userId = userId;
		this.subscribedProducts = new HashMap<>();
	}

	public String getUserId() {
		return userId;
	}

	public Map<String, String> getSubscribedProducts() {
		return subscribedProducts;
	}

	public void setSubscribedProducts(Map<String, String> subscribedProducts) {
		this.subscribedProducts = subscribedProducts;
	}

	/**
	 * ID：名前の形でHashMapに追加する、既にID：名前で登録済みの場合は上書きしない
	 * 
	 * @param prodId 購読している製品のID
	 * @param prodName 購読している製品の名前
	 */
	public void addSubscribedProduct(String prodId, String prodName) {
		subscribedProducts.putIfAbsent(prodId, prodName);
	}

	@Override
	public int getCode() {
		return Result.CODE_NORMAL;
	}
}
