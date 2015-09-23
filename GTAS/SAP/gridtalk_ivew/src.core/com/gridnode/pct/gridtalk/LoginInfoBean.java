package com.gridnode.pct.gridtalk;

import java.io.Serializable;

/**
 * This class is Java Bean containing username and password properties.
 */
public class LoginInfoBean implements Serializable {
	private String username = null;
	private String password = null;

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public void setPassword(String string) {
		password = string;
	}

	public void setUsername(String string) {
		username = string;
	}

}
