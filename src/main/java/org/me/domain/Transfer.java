package org.me.domain;

public class Transfer {

	private String trnsId;
	private String value;
	private String userId;
	private String networkType;
	private String amount;

	public Transfer(String trnsId, String value, String userId, String networkType, String amount) {
		this.trnsId = trnsId;
		this.value = value;
		this.userId = userId;
		this.networkType = networkType;
		this.amount = amount;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNetworkType() {
		return networkType;
	}

	public void setNetworkType(String networkType) {
		this.networkType = networkType;
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

	@Override
	public String toString() {
		return "Transfer{" +
				"trnsId='" + trnsId + '\'' +
				", value='" + value + '\'' +
				", userId='" + userId + '\'' +
				", networkType='" + networkType + '\'' +
				", amount='" + amount + '\'' +
				'}';
	}
}
