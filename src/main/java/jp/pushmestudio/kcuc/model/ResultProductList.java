package jp.pushmestudio.kcuc.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jp.pushmestudio.kcuc.util.Result;

/**
 * 応答用のユーザーの購読している製品リスト
 * Result系のクラスはSwaggerの応答にも用いられるため、数字・文字列・配列・リスト以外をメンバー変数に使用しないこと
 */
public class ResultProductList extends Result {
	private String userId;
	/** 製品名を重複して持たせないためHashSetにしている */
	private List<Product> subscribedProducts;
	private Set<String> uniqueProductIds;

	/**
	 * コンストラクタ、製品リストなども初期化している
	 * 
	 * @param userId
	 *            製品リストと紐づくユーザーのID
	 */
	public ResultProductList(String userId) {
		this.userId = userId;
		this.subscribedProducts = new ArrayList<>();
		this.uniqueProductIds = new HashSet<>();
	}

	public String getUserId() {
		return userId;
	}

	public List<Product> getSubscribedProducts() {
		return subscribedProducts;
	}

	/**
	 * 重複のあるリストを渡されても重複のない形に補正するように、ユニークチェック後に格納している 速度を求めるなら単に代入の形を取ればいい
	 * 
	 * @param subscribedProducts
	 *            製品リスト、重複ありでもOK
	 */
	public void setSubscribedProducts(List<Product> subscribedProducts) {
		this.subscribedProducts = new ArrayList<>();
		;
		this.uniqueProductIds = new HashSet<>();

		for (Product each : subscribedProducts) {
			if (this.uniqueProductIds.add(each.getProdId())) {
				this.subscribedProducts.add(each);
			}
			;
		}
	}

	/**
	 * ID：名前の形でHashMapに追加する、既にID：名前で登録済みの場合は上書きしない
	 * 
	 * @param prodId
	 *            購読している製品のID
	 * @param prodName
	 *            購読している製品の名前
	 */
	public void addSubscribedProduct(String prodId, String prodName) {
		if (uniqueProductIds.add(prodId)) {
			subscribedProducts.add(new Product(prodId, prodName));
		}
	}
}
