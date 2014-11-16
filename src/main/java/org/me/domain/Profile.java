package org.me.domain;

import java.util.List;

public class Profile {

	private String name;
	private String userId;
	private String networkType;
	private List<Profile> contacts;

	public Profile(String name, String userId, String networkType) {
		this.name = name;
		this.userId = userId;
		this.networkType = networkType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public List<Profile> getContacts() {
		return contacts;
	}

	public void setContacts(List<Profile> contacts) {
		this.contacts = contacts;
	}


	@Override
	public String toString() {
		return "Profile{" +
				"name='" + name + '\'' +
				", userId='" + userId + '\'' +
				", networkType='" + networkType + '\'' +
				", contacts=" + contacts +
				'}';
	}
}
