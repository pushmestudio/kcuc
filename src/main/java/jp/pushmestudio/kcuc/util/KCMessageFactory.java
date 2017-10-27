package jp.pushmestudio.kcuc.util;

/**
 * Factoryパターンを適用して、Messageから具体的なメッセージ内容を取得するためのファクトリークラス
 * クラス名をMessageFactoryにしようと検討したが、JavaEEのSOAP関連で同名クラスがあるため名称変更した
 */
public class KCMessageFactory {
	/**
	 * @param messageCode
	 *            作成対象のメッセージコード
	 * @return メッセージコードを元に生成されたメッセージオブジェクト
	 */
	public static Message createMessage(int messageCode) {
		return createMessage(messageCode, null, null);
	}

	/**
	 * @param messageCode
	 *            作成対象のメッセージコード
	 * @param messageParameter
	 *            作成対象のメッセージに付加するパラメーター
	 * @return メッセージコードを元に生成されたメッセージオブジェクト
	 */
	public static Message createMessage(int messageCode, String messageParameter) {
		return new Message(messageCode, messageParameter, messageParameter);
	}

	/**
	 * @param messageCode
	 *            作成対象のメッセージコード
	 * @param messageSubject
	 *            作成対象のメッセージに付加するタイトル
	 * @param messageParameter
	 *            作成対象のメッセージに付加するパラメーター
	 * @return メッセージコードを元に生成されたメッセージオブジェクト
	 */
	public static Message createMessage(int messageCode, String messageSubject, String messageParameter) {
		return new Message(messageCode, messageSubject, messageParameter);
	}
}
