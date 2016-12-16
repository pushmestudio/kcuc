package jp.pushmestudio.kcuc.util;

/**
 * Factoryパターンを適用して、Messageから具体的なメッセージ内容を取得するためのファクトリークラス
 * クラス名をMessageFactoryにしようと検討したが、JavaEEのSOAP関連で同名クラスがあるため名称変更した
 */
public class KCMessageFactory {

	/** 正常系の応答で使う */
	public static final int CODE_NORMAL = 200;
	/** クライアントサイド由来のエラーに使う */
	public static final int CODE_CLIENT_ERROR = 400;
	/** サーバーサイド由来のエラーに使う */
	public static final int CODE_SERVER_ERROR = 500;
	/** 該当コードが存在しない場合などに使う */
	public static final int CODE_UNKNOWN = 999;

	/**
	 * @param messageCode
	 *            作成対象のメッセージコード
	 * @return メッセージコードを元に生成されたメッセージオブジェクト
	 */
	public static Message createMessage(int messageCode) {
		return createMessage(messageCode, null);
	}

	/**
	 * @param messageCode
	 *            作成対象のメッセージコード
	 * @param messageParameter
	 *            作成対象のメッセージに付加するパラメーター
	 * @return メッセージコードを元に生成されたメッセージオブジェクト
	 */
	public static Message createMessage(int messageCode, String messageParameter) {
		if (messageCode == CODE_SERVER_ERROR) {
			return new ErrorMessage(messageCode, messageParameter);
		} else {
			return new ErrorMessage(CODE_UNKNOWN, messageParameter);
		}
	}
}
