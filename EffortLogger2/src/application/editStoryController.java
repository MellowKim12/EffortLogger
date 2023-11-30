package application;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

import java.io.IOException;
import java.util.Date;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.story;

public class editStoryController {

    @FXML
    private TextArea editDetails;

    @FXML
    private TextField editProj;

    @FXML
    private TextField editTitle;

    @FXML
    private Text editWelcome;

    @FXML
    private Text finishUpdating;

    private int storyID;

    private Date editTime;
    
    private Parent root;
	private Scene scene;
	private Stage stage;
    
    private String connectionString;
	private MongoDatabase db;
	private MongoCollection<Document> col;
	private MongoCollection<Document> userCol;
	private MongoClient mongoClient;
	private Login loginSystem;
	
	private story currentStory;
	
	public void recieveTransferedItems(String connectionString, MongoDatabase db, MongoCollection<Document> col, MongoCollection<Document> userCol, MongoClient mongoClient, Login loginSystem) {
		this.connectionString = connectionString;
		this.db = db;
		this.col = col;
		this.userCol = userCol;
		this.mongoClient = mongoClient;
		this.loginSystem = loginSystem;
    }


    // updates UI appropriately with the logID of current log being updated
    public void setData(int storyID, story currentStory) {
    	this.storyID = storyID;
    	this.currentStory = currentStory;
    	editWelcome.setText("Editing Story: " + storyID);
    	editDetails.setPromptText(currentStory.getDescription());
    	editProj.setPromptText(currentStory.getProjectID());
    	editTitle.setPromptText(currentStory.getTitle());
    }

    // handles the user input back-end with the server and updates the log
    public void editLogData(ActionEvent event) {
    	// establish connection with mongo server
    	Main updateLogs = new Main();

    	// note time of submit
    	editTime = new Date();

    	// edit log checks. each case represents different case of input validation
    	if(!editDetails.getText().equals(""))
    	{
    		String newDets = editDetails.getText();
    		updateLogs.update("stories", "details", "story-id", storyID,  newDets, 0, editTime, db);
    	}
    	if(!editProj.getText().equals(""))
    	{
    		int projectID = Integer.parseInt(editProj.getText());
    		updateLogs.update("stories", "project-id", "story-id", storyID, null, projectID, editTime, db);
    	}
    	if(!editTitle.getText().equals(""))
    	{
    		String newTitle = editTitle.getText();
    		updateLogs.update("stories", "title", "story-id", storyID, newTitle, 0, editTime, db);
    	}

    	// checks whether or not start-time and end-time fields are empty or not

    	finishUpdating.setText("Done updating");

    }

    // returns back to the home screen and updates it accordingly
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
		stage.setResizable(false);
		stage.show();
	}


}