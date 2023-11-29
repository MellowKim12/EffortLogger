package model;

import application.Login;

public class story{
	private int storyID;
	private int projectID;
	private String description;
	private String title;
	private Login login;
	public String getStoryID() {
		return "" + storyID;
	}
	public void setStoryID(int newStoryID) {
		storyID = newStoryID;
	}
	public String getProjectID()
	{
		return "" + projectID;
	}
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String newTitle)
	{
		this.title = newTitle;
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