package jp.pushmestudio.kcuc.util;

/**
 * 非正常系のメッセージを取り扱う
 */
public class ErrorMessage extends Message {

	public ErrorMessage(int messageCode, String messageParameter) {
		super(messageCode, messageParameter);
	}

	@Override
	protected void setSubject(String parameter) {
		super.setSubject("Error");
	}

	@Override
	protected void setDetail(String parameter) {
		StringBuilder detail = new StringBuilder();
		detail.append("ありえない値が渡されました！");
		detail.append("なんと").append(parameter).append("です");

		super.setDetail(detail.toString());
	}
}
