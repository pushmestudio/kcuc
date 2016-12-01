package jp.pushmestudio.kcuc.util;

/**
 * Factoryパターンを適用して、Messageから具体的なメッセージ内容を取得するためのファクトリークラス
 * クラス名をMessageFactoryにしようと検討したが、JavaEEのSOAP関連で同名クラスがあるため名称変更した
 */
public class KCMessageFactory {

	public static final int CODE_NORMAL = 200;
	public static final int CODE_GENERAL_ERROR = 500;
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
		if (messageCode == CODE_GENERAL_ERROR) {
			return new ErrorMessage(messageCode, messageParameter);
		} else {
			return new ErrorMessage(CODE_UNKNOWN, messageParameter);
		}
	}
}
