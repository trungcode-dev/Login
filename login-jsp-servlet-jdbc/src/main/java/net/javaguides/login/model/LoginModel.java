package net.javaguides.login.model;

import java.io.Serializable;

public class LoginModel implements Serializable {
	private String username;
	private String email;
	private String password;

	public LoginModel() {
		super();
	}

	public LoginModel(String username, String email, String password) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
