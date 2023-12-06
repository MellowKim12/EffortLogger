package application;

import static com.mongodb.client.model.Filters.eq;

import java.io.IOException;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class StoryAdditionController {
	private String connectionString;
	private MongoDatabase db;
	private MongoCollection<Document> col;
	private MongoCollection<Document> userCol;
	private MongoClient mongoClient;
	private Login loginSystem;

	private Parent root;
	private Scene scene;
	private Stage stage;

	private int userId;
	private int projectId;

	@FXML
	private TextField description;

	@FXML
	private TextField title;

	public void recieveTransferedItems(String connectionString, MongoDatabase db, MongoCollection<Document> col, MongoCollection<Document> userCol, MongoClient mongoClient, Login loginSystem)
	{
		this.connectionString = connectionString;
		this.db = db;
		this.col = col;
		this.userCol = userCol;
		this.mongoClient = mongoClient;
		this.loginSystem = loginSystem;
		projectId = 1;

		FindIterable<Document> filterUsers = userCol.find(eq("username", loginSystem.getUsername()));
		Document targetObject = filterUsers.first();
		userId = Integer.parseInt(targetObject.get("userID").toString());
	}

	public void storeStory()
	{
		String titleEntry = title.getText();
		String desc = description.getText();
		//check if log description and story are null
		if(desc.length() != 0 && titleEntry.length() != 0) {
			Main.insertStory(userId, projectId, titleEntry, desc, db);
			description.clear();
			title.clear();
			System.out.println("Story successfully added to the database");
		}
		else {
			System.out.println("One or more entries was invalid");
		}
	}

	public void returnHome(ActionEvent event) throws IOException
	{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("StoryMainUI.fxml"));

		System.out.println("Switching to the Story Page");
		root = fxmlLoader.load();
		StoryViewController storyViewController = fxmlLoader.getController();
		storyViewController.recieveTransferedItems(connectionString, db, col, userCol, mongoClient, loginSystem);

		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setTitle("Story View");
		stage.setScene(scene);
		stage.show();
	}
}