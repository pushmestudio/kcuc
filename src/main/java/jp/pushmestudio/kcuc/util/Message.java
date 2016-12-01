package jp.pushmestudio.kcuc.util;

import org.json.JSONObject;

/**
 * 生成メッセージの元となるインタフェース
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

		this.setMessage();
		this.setJsonMessage();
	}

	/* setter/getter */
	public String getSubject() {
		return subject;
	}

	// 継承先に変更を許可する
	protected void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDetail() {
		return detail;
	}

	// 継承先に変更を許可する
	protected void setDetail(String detail) {
		this.detail = detail;
	}

	public int getCode() {
		return code;
	}

	// 継承先に変更を許可する
	protected void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return this.message;
	}

	// 継承先に変更を許可する
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

	// 継承先に変更を許可する
	protected void setJsonMessage() {
		JSONObject jsonMessage = new JSONObject();
		jsonMessage.put("subject", this.getSubject());
		jsonMessage.put("detail", this.getDetail());
		jsonMessage.put("code", this.getCode());
		this.jsonMessage = jsonMessage;
	}
}
