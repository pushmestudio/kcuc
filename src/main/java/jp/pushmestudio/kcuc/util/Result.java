package jp.pushmestudio.kcuc.util;

public abstract class Result {
	/** 正常系の応答で使う */
	public static final int CODE_OK = 200;
	/** Putなどの更新系応答時などに使う **/
	public static final int CODE_CREATED = 201;
	/** Deleteなどの削除系応答時などに使う **/
	public static final int CODE_NO_CONTENT = 204;
	/** パラメーターエラーなどに使う */
	public static final int CODE_BAD_REQUEST = 400;
	/** リエクスト先が見つからないもしくは存在を知らせたくないときなどに使う */
	public static final int CODE_NOT_FOUND = 404;
	/** 既に存在する対象を追加しようとする際のエラーなどに使う */
	public static final int CODE_CONFLICT = 409;

	/** サーバーサイド由来のエラーに使う */
	public static final int CODE_INTERNAL_SERVER_ERROR = 500;
	/** メンテ時など応答不能時に使う */
	public static final int CODE_SERVICE_UNAVAILABLE = 503;

	int code;

	/**
	 * 応答時のレスポンスコードの判定に使用する、通常は{@code return Result.CODE_OK;}のように実装すれば良い
	 * 
	 * @return レスポンスコードに使う応答コード
	 */
	public int getCode() {
		return this.code;
	}

	/**
	 * 応答時のレスポンスコードをセットする、正常時以外には様々な応答コードが想定されるためsetCodeでcodeをセットし応答する
	 */
	public void setCode(int newCode) {
		this.code = newCode;
	}
}
