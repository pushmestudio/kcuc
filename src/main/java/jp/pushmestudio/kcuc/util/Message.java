package jp.pushmestudio.kcuc.util;

import org.json.JSONObject;

/**
 * 生成メッセージの元となる抽象クラス
 */
public abstract class Message {
	private String subject;
	private String detail;
	private int code;
	protected String message;
	protected JSONObject jsonMessage;

	public Message(int messageCode) {
		this(messageCode, null);
	}

	/**
	 * @param messageCode
	 *            各メッセージ種別が持つメッセージコード
	 * @param messageParameter
	 *            メッセージ生成に利用されるパラメーター
	 */
	public Message(int messageCode, String messageParameter) {
		this.setCode(messageCode);
		this.setSubject(messageParameter);
		this.setDetail(messageParameter);
		this.updateMessage();
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

	public int getCode() {
		return code;
	}

	/**
	 * 受け取った値のセットを変更したい場合などには、継承先でoverrideする。
	 * 
	 * @param code
	 *            メッセージのcode number
	 */
	protected void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return this.message;
	}

	/**
	 * subject, detail, code以外のものをメッセージに含みたい場合には 継承先でこのメソッドをoverrideする。 subject,
	 * detail, codeについてのみ変更したい場合には、 継承先で{@link #setSubject}などをoverrideする。
	 */
	protected void setMessage() {
		StringBuilder message = new StringBuilder();
		message.append("subject:").append(this.getSubject());
		message.append("detail:").append(this.getDetail());
		message.append("code:").append(this.getCode());
		this.message = message.toString();
	}

	public JSONObject getJsonMessage() {
		return this.jsonMessage;
	}

	/**
	 * subject, detail, code以外のものをメッセージに含みたい場合には 継承先でこのメソッドをoverrideする。 subject,
	 * detail, codeについてのみ変更したい場合には、 継承先で{@link #setSubject}などをoverrideする。
	 */
	protected void setJsonMessage() {
		JSONObject jsonMessage = new JSONObject();
		jsonMessage.put("subject", this.getSubject());
		jsonMessage.put("detail", this.getDetail());
		jsonMessage.put("code", this.getCode());
		this.jsonMessage = jsonMessage;
	}

	/**
	 * セット済みのフィールドを元に、応答に使うメッセージのオブジェクトを更新する
	 */
	protected void updateMessage() {
		this.setJsonMessage();
		this.setMessage();
	}
}
