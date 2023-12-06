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

public class editLogController {

    @FXML
    private TextArea editDetails;

    @FXML
    private TextField editProj;

    @FXML
    private TextField editSID;

    @FXML
    private DatePicker editStartTime;

    @FXML
    private DatePicker editEndTime;

    @FXML
    private Text editWelcome;

    @FXML
    private Text finishUpdating;

    private int logID;

    private Date editTime;


    // updates UI appropriately with the logID of current log being updated
    public void setData(int logID) {
    	this.logID = logID;
    	editWelcome.setText("Editing Log: " + logID);
    }

    // handles the user input back-end with the server and updates the log
    public void editLogData(ActionEvent event) {
    	// establish connection with mongo server
    	Main updateLogs = new Main();
    	String connectionString = "mongodb+srv://ndlovelace13:7Cpa4yubfjj7aPql@effortlogger.zfgzhfr.mongodb.net/?retryWrites=true&w=majority";
    	MongoClient mongoClient = MongoClients.create(connectionString);
    	MongoDatabase db = mongoClient.getDatabase("Effortlogs");

    	// note time of submit
    	editTime = new Date();

    	// edit log checks. each case represents different case of input validation
    	if(!editDetails.getText().equals(""))
    	{
    		String newDets = editDetails.getText();
    		updateLogs.update("logs", "details", "log-id", logID,  newDets, 0, editTime, db);
    	}
    	if(!editProj.getText().equals(""))
    	{
    		int projectID = Integer.parseInt(editProj.getText());
    		updateLogs.update("logs", "project-id", "log-id", logID, null, projectID, editTime, db);
    	}
    	if(!editSID.getText().equals(""))
    	{
    		int storyID = Integer.parseInt(editSID.getText());
    		updateLogs.update("logs", "story-id", "log-id", logID, null, storyID, editTime, db);
    	}

    	// checks whether or not start-time and end-time fields are empty or not
    	if(editStartTime.getValue() != null)
    	{
    		MongoCollection<Document> col = db.getCollection("logs");
    		col.findOneAndUpdate(eq("log-id", logID), set("start-time", editStartTime.getValue()));
    		col.findOneAndUpdate(eq("log-id", logID), set("end-time", editEndTime.getValue()));
    	}

    	finishUpdating.setText("Done updating");

    }

    // returns back to the home screen and updates it accordingly
    public void returnHome(ActionEvent event) throws IOException{
    	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EffortLoggerMainUI.fxml"));
    	Parent root;
    	root = fxmlLoader.load();
    	String connectionString = "mongodb+srv://ndlovelace13:7Cpa4yubfjj7aPql@effortlogger.zfgzhfr.mongodb.net/?retryWrites=true&w=majority";
    	MongoClient mongoClient = MongoClients.create(connectionString);
    	MongoDatabase db = mongoClient.getDatabase("Effortlogs");
    	MongoCollection<Document> userCol = db.getCollection("users");
    	MongoCollection<Document> col = db.getCollection("logs");
    	FindIterable<Document> findUserId = col.find(eq("log-id", logID));
		Document targetObject = findUserId.first();
		String userId = targetObject.get("user-id").toString();
		FindIterable<Document> findUser = userCol.find(eq("userID", userId));
		Document targetuser = findUser.first();
		String userName = targetuser.get("username").toString();
    	Login logins = new Login();
    	logins.setUsername(userName);
    	EffortLoggerMainUI effortLoggerMainUIController = fxmlLoader.getController();
		effortLoggerMainUIController.update(logins, db, userCol, col);
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setTitle("EffortLogger Main Menu");
		stage.setScene(scene);
		stage.show();
    }


}