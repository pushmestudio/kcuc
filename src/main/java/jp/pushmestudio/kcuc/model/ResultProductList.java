package jp.pushmestudio.kcuc.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jp.pushmestudio.kcuc.util.Result;

/**
 * 応答用のユーザーの購読している製品リスト
 * Result系のクラスはSwaggerの応答にも用いられるため、数字・文字列・配列・リスト以外をメンバー変数に使用しないこと
 */
public class ResultProductList implements Result {
	private String userId;
	/** 製品名を重複して持たせないためHashSetにしている、これにより重複リストを渡されてもセット時にユニークになる */
	private Set<Product> subscribedProducts;

	public ResultProductList(String userId) {
		this.userId = userId;
		this.subscribedProducts = new HashSet<>();
	}

	public String getUserId() {
		return userId;
	}

	public Set<Product> getSubscribedProducts() {
		return subscribedProducts;
	}

	public void setSubscribedProducts(Set<Product> subscribedProducts) {
		this.subscribedProducts = subscribedProducts;
	}

	/**
	 * 重複ありのリストをHashSetによってユニーク化して登録する
	 * 
	 * @param products
	 *            購読製品一覧(重複OK)
	 */
	public void addSubscribedProduct(List<Product> products) {
		subscribedProducts.addAll(products);
	}

	@Override
	public int getCode() {
		return Result.CODE_NORMAL;
	}
}
