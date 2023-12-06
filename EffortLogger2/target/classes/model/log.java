package model;

import application.Login;

public class log{
	private int logID;
	private String Description;
	private Login login;
	public String getLogID() {
		return "" + logID;
	}
	public void setlogID(int newlogID) {
		logID = newlogID;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public void setLogin(Login login)
	{
		this.login = login;
	}

	public Login getLogin()
	{
		return login;
	}
}