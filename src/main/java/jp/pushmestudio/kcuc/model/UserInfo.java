package jp.pushmestudio.kcuc.model;

import java.util.Map;

/**
 * 登録ユーザー情報
 */
public class UserInfo {
	private String id;
	private String password;
	private Map<String, Long> subscribedPages;

	/**
	 * @param id
	 *            ユーザーID
	 * @param password
	 *            ユーザーパスワード
	 * @param subscribedPages
	 *            購読中製品
	 */
	public UserInfo(String id, String password, Map<String, Long> subscribedPages) {
		this.id = id;
		this.password = password;
		this.subscribedPages = subscribedPages;
	}

	public String getId() {
		return id;
	}

	public String getPassword() {
		return password;
	}

	public Map<String, Long> getSubscribedPages() {
		return subscribedPages;
	}
}
