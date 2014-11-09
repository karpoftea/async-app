package org.me.domain;

public class Transfer {

	private String trnsId;
	private String value;

	public Transfer(String trnsId, String value) {
		this.trnsId = trnsId;
		this.value = value;
	}

	public String getTrnsId() {
		return trnsId;
	}

	public void setTrnsId(String trnsId) {
		this.trnsId = trnsId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
