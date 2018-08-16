package com.kuding.pojos.dingding;

public class DingDingNotice {

	private DingDingText text;
	private DingDingAt at;
	private String msgtype = "text";
	private boolean isAtAll = false;

	public DingDingNotice(DingDingText text, DingDingAt at, String msgtype, boolean isAtAll) {
		super();
		this.text = text;
		this.at = at;
		this.msgtype = msgtype;
		this.isAtAll = isAtAll;
	}

	public DingDingNotice(DingDingText text, DingDingAt at) {
		this.text = text;
		this.at = at;
	}

	public DingDingNotice(String text, String... at) {
		this.text = new DingDingText(text);
		this.at = new DingDingAt(at);
	}

	/**
	 * @return the text
	 */
	public DingDingText getText() {
		return text;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(DingDingText text) {
		this.text = text;
	}

	/**
	 * @return the at
	 */
	public DingDingAt getAt() {
		return at;
	}

	/**
	 * @param at
	 *            the at to set
	 */
	public void setAt(DingDingAt at) {
		this.at = at;
	}

	/**
	 * @return the msgtype
	 */
	public String getMsgtype() {
		return msgtype;
	}

	/**
	 * @param msgtype
	 *            the msgtype to set
	 */
	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	/**
	 * @return the isAtAll
	 */
	public boolean isAtAll() {
		return isAtAll;
	}

	/**
	 * @param isAtAll
	 *            the isAtAll to set
	 */
	public void setAtAll(boolean isAtAll) {
		this.isAtAll = isAtAll;
	}

}
