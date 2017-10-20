package jp.pushmestudio.kcuc.util;

/**
 * 生成メッセージの元となるクラス
 */
public class Message extends Result {
	private String subject;
	private String detail;
	protected String message;

	public Message(int messageCode) {
		this(messageCode, null, null);
	}

	/**
	 * @param messageCode
	 *            各メッセージ種別が持つメッセージコード
	 * @param messageParameter
	 *            メッセージ生成に利用されるパラメーター
	 */
	public Message(int messageCode, String messageSubject, String messageParameter) {
		this.setCode(messageCode);
		this.setSubject(messageParameter);
		this.setDetail(messageParameter);
	}

	/* setter/getter */
	public String getSubject() {
		return subject;
	}

	/**
	 * 受け取った値のセットを変更したい場合などには、継承先でoverrideする。
	 * 
	 * @param subject
	 *            メッセージの表題
	 */
	protected void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDetail() {
		return detail;
	}

	/**
	 * 受け取った値のセットを変更したい場合などには、継承先でoverrideする。
	 * 
	 * @param detail
	 *            メッセージの詳細
	 */
	protected void setDetail(String detail) {
		this.detail = detail;
	}

	public String getMessage() {
		return this.message;
	}
}
