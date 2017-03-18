package jp.pushmestudio.kcuc.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 検索結果として得られる各ページの内容
 */
public class TopicMeta {
	/** プロダクトID */
	private String product;
	/** 作成日、該当する値がないときは-1だが、ページが存在するなら必ず入る */
	private long dateCreated;
	/** 更新日、該当する値がないときは-1 */
	private long dateLastModified;
	/** 独自拡張、dateLastModifiedがあるときはその値、ないときはdateCreated */
	private long dateLastUpdated;
	private boolean exist;

	/**
	 * topicのメタ情報、いつ更新されたかや製品名など
	 */
	public TopicMeta() {
		this.product = "";
		this.dateCreated = -1;
		this.dateLastModified = -1;
		this.dateLastUpdated = -1;
		this.exist = false;
	}

	/**
	 * JSONObjectから簡単にセットできるようにしたコンストラクタ、現時点では"classification"の中身のみを使用している。製品としては存在しているがページとしては存在しない場合、
	 * ページなら必ず存在する"datecreated"キーがないため、当該キーの存在チェックも実施している
	 * 
	 * @param topicMetaJson
	 *            KCの {@code topic_metadata} から得た応答をそのまま受け取ることとする
	 */
	public TopicMeta(JSONObject topicMetaJson) throws JSONException {
		// 後で取得するキー名が変更になった時に楽にするために変数にして取り回す
		final String key_classification = "classification";
		final String key_datecreated = "datecreated";

		if (topicMetaJson.has(key_classification)
				&& topicMetaJson.getJSONObject(key_classification).has(key_datecreated)) {
			JSONObject extractedMetaJson = topicMetaJson.getJSONObject(key_classification);

			/*
			 * topicとfoundinは大抵同じ値が入っているが、{@code
			 * SS8NLW_11.0.2/com.ibm.swg.im.infosphere.dataexpl.welcome.doc/doc/
			 * watsonexplorer_11.0.2.html}の場合、productが入っていなかった
			 * 現時点ではひとまず、foundinのキーを使用し、様子を見ることとする
			 */
			this.product = extractedMetaJson.getString("foundin");
			this.dateCreated = extractedMetaJson.getLong("datecreated");
			this.dateLastModified = extractedMetaJson.has("datelastmodified")
					? extractedMetaJson.getLong("datelastmodified") : -1;
			this.dateLastUpdated = Math.max(this.dateCreated, this.dateLastModified); // 大きい方を代入
			this.exist = true;
		} else {
			this.product = "";
			this.dateCreated = -1;
			this.dateLastModified = -1;
			this.dateLastUpdated = -1;
			this.exist = false;
		}
	}

	/** 
	 * プロダクトIDを返す、{@link Product}自体ではないので注意
	 **/
	public String getProduct() {
		return product;
	}

	public long getDateCreated() {
		return dateCreated;
	}

	public long getDateLastModified() {
		return dateLastModified;
	}

	public long getDateLastUpdated() {
		return dateLastUpdated;
	}

	public boolean isExist() {
		return exist;
	}
}