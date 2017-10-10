package jp.pushmestudio.kcuc.model;

import java.util.List;

import jp.pushmestudio.kcuc.util.Result;

/**
 * 応答用の検索結果一覧、検索結果をそのまま使用しているので、このクラスによってラップする意義があるかは検討の余地がある
 * Result系のクラスはSwaggerの応答にも用いられるため、数字・文字列・配列・リスト以外をメンバー変数に使用しない
 */
public class ResultSearchList implements Result {
	private int offset;
	private int next;
	private int prev;
	private int count;
	private int total;
	private List<Topic> topics;

	/**
	 * @param offset 検索結果取得開始位置
	 * @param next 次に取得すべき検索結果取得開始位置
	 * @param prev 一つ前に戻る場合に取得すべき検索結果の取得開始位置
	 * @param count 取得件数
	 * @param total 合計ヒット数
	 * @param topics 取得件数分の実際の検索結果
	 */
	public ResultSearchList(int offset, int next, int prev, int count, int total, List<Topic> topics) {
		this.offset = offset;
		this.next = next;
		this.prev = prev;
		this.count = count;
		this.total = total;
		this.topics = topics;
	}

	@Override
	public int getCode() {
		return Result.CODE_OK;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getNext() {
		return next;
	}

	public void setNext(int next) {
		this.next = next;
	}

	public int getPrev() {
		return prev;
	}

	public void setPrev(int prev) {
		this.prev = prev;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<Topic> getTopics() {
		return topics;
	}

	public void setTopics(List<Topic> topics) {
		this.topics = topics;
	}

}
