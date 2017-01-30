package jp.pushmestudio.kcuc.model;

/**
 * 登録ユーザー情報
 */
public class UserInfo {
	private String userId;
	private boolean isUpdated;

	/**
	 * @param userId
	 *            ユーザーID
	 * @param isUpdated
	 *            更新済みか否かのフラグ
	 */
	public UserInfo(String userId, boolean isUpdated) {
		this.userId = userId;
		this.isUpdated = isUpdated;
	}

	public String getUserId() {
		return userId;
	}

	public boolean getIsUpdated() {
		return isUpdated;
	}
}
