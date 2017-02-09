package jp.pushmestudio.kcuc.util;

public interface Result {
	/** 正常系の応答で使う */
	public static final int CODE_NORMAL = 200;
	/** クライアントサイド由来のエラーに使う */
	public static final int CODE_CLIENT_ERROR = 400;
	/** サーバーサイド由来のエラーに使う */
	public static final int CODE_SERVER_ERROR = 500;
	/** 該当コードが存在しない場合などに使う */
	public static final int CODE_UNKNOWN = 999;

	/** 応答時のレスポンスコードの判定に使用する、通常は{@code return Result.CODE_NORMAL;}のように実装すれば良い */
	int getCode();
}
