package jp.pushmestudio.kcuc.util;

/**
 * 非正常系のメッセージを取り扱う
 */
public class NormalMessage extends Message {

	public NormalMessage(int messageCode, String messageParameter) {
		super(messageCode, messageParameter);
	}

	/*
	 * (non-Javadoc) 何を渡されてもOKをセットする
	 * 
	 * @see jp.pushmestudio.kcuc.util.Message#setSubject(java.lang.String)
	 */
	@Override
	protected void setSubject(String parameter) {
		super.setSubject("OK");
	}

	@Override
	protected void setDetail(String parameter) {
		StringBuilder detail = new StringBuilder();
		detail.append("Response message is the following: ");
		detail.append(parameter);

		super.setDetail(detail.toString());
	}
}
