package application;

import java.io.IOException;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.story;

public class storyThumbController {

    @FXML
    private Text StoryDesc;

    @FXML
    private Text StoryId;

    @FXML
    private Text StoryTitle;

    private Login login;

    private story currentStory;

    private String connectionString;
	private MongoDatabase db;
	private MongoCollection<Document> col;
	private MongoCollection<Document> userCol;
	private MongoClient mongoClient;
	private Login loginSystem;


    public void recieveTransferedItems(String connectionString, MongoDatabase db, MongoCollection<Document> col, MongoCollection<Document> userCol, MongoClient mongoClient, Login loginSystem) {
		this.connectionString = connectionString;
		this.db = db;
		this.col = col;
		this.userCol = userCol;
		this.mongoClient = mongoClient;
		this.loginSystem = loginSystem;
    }

    public void setData(story storyShow) {
    	StoryDesc.setText("Description: " + storyShow.getDescription());
    	StoryTitle.setText(storyShow.getTitle());
    	StoryId.setText("ID: " + storyShow.getStoryID());
    	login = storyShow.getLogin();
    	currentStory = storyShow;
    }

    public void editStory(ActionEvent event) throws IOException {
    	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("editStory.fxml"));
    	Parent root = fxmlLoader.load();
    	int editStoryID = Integer.parseInt(currentStory.getStoryID());


    	editStoryController editStoryData = fxmlLoader.getController();
    	editStoryData.recieveTransferedItems(connectionString, db, col, userCol, mongoClient, loginSystem);
    	editStoryData.setData(editStoryID, currentStory);
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setTitle("Story Editor");
		stage.setScene(scene);
		stage.show();
    }

    public void deleteStoryWarn(ActionEvent event) throws IOException {
    	StackPane root2 = new StackPane();
    	Label label = new Label("Are you sure you want to delete?");
        Button button = new Button("Delete");
        Button button2 = new Button("No");
        root2.getChildren().add(label);
        StackPane.setMargin(label, new Insets(0, 0, 115, 10));
        root2.getChildren().add(button);
        StackPane.setMargin(button, new Insets(0, 50, 50, 0));
        root2.getChildren().add(button2);
        StackPane.setMargin(button2, new Insets(0, 0, 50, 50));
        Scene secondScene = new Scene(root2, 200,150);
        Stage secondStage = new Stage();
        secondStage.setScene(secondScene); // set the scene
        secondStage.setTitle("Delete?");
        secondStage.show();

        button.setOnAction(e->{
        		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("StoryMainUI.fxml"));
        		secondStage.close();
				Parent root;
				try {
					root = fxmlLoader.load();
		        	Main object = new Main();
		        	object.deleteEntry(Integer.parseInt(currentStory.getStoryID()), "stories", "story-id", db);

		        	StoryViewController storyViewController = fxmlLoader.getController();
					storyViewController.recieveTransferedItems(connectionString, db, col, userCol, mongoClient, loginSystem);
		        	secondStage.close();
		        	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
					Scene scene = new Scene(root);
					stage.setTitle("Story View");
					stage.setScene(scene);
					stage.show();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        });
	button2.setOnAction(e->{
	        	secondStage.close();
	        });


        }

}