package jp.pushmestudio.kcuc.util;

/**
 * 非正常系のメッセージを取り扱う
 */
public class ErrorMessage extends Message {

	public ErrorMessage(int messageCode, String messageParameter) {
		super(messageCode, messageParameter);
	}

	/*
	 * (non-Javadoc) 何を渡されてもErrorをセットする
	 * 
	 * @see jp.pushmestudio.kcuc.util.Message#setSubject(java.lang.String)
	 */
	@Override
	protected void setSubject(String parameter) {
		super.setSubject("Error");
	}

	@Override
	protected void setDetail(String parameter) {
		StringBuilder detail = new StringBuilder();
		detail.append("Error is occured. The Reason is the following: ");
		detail.append(parameter);

		super.setDetail(detail.toString());
	}
}
