package jp.pushmestudio.kcuc.model;

/**
 * 登録ユーザー情報
 */
public class UserInfo {
	private String id;
	private boolean isUpdated;

	/**
	 * @param id
	 *            ユーザーID
	 * @param isUpdated
	 *            更新済みか否かのフラグ
	 */
	public UserInfo(String id, boolean isUpdated) {
		this.id = id;
		this.isUpdated = isUpdated;
	}

	public String getId() {
		return id;
	}

	public boolean getIsUpdated() {
		return isUpdated;
	}
}
