package jp.pushmestudio.kcuc.model;

/**
 * 登録ユーザー情報
 */
public class UserInfo {
	private String userName;
	private boolean isUpdated;

	/**
	 * @param userName
	 *            ユーザーID
	 * @param isUpdated
	 *            更新済みか否かのフラグ
	 */
	public UserInfo(String userName, boolean isUpdated) {
		this.userName = userName;
		this.isUpdated = isUpdated;
	}

	public String getUserName() {
		return userName;
	}

	public boolean getIsUpdated() {
		return isUpdated;
	}
}
