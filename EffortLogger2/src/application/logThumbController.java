package application;

import static com.mongodb.client.model.Filters.eq;

import java.io.IOException;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
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
import model.log;

public class logThumbController {

    @FXML
    private Text LogDesc;

    @FXML
    private Text LogId;

    private Login login;




    public void setData(log logShow) {
    	LogDesc.setText("Description: " + logShow.getDescription());
    	LogId.setText("LogID: " + logShow.getLogID());
    	login = logShow.getLogin();

    }

    public void editLog(ActionEvent event) throws IOException {
    	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("editLog.fxml"));
    	Parent root = fxmlLoader.load();
    	String LogIdtext = LogId.getText();
    	int indexOf =  LogIdtext.indexOf(" ");
    	int editLogID = Integer.parseInt(LogIdtext.substring(indexOf + 1));


    	editLogController editLogData = fxmlLoader.getController();
    	editLogData.setData(editLogID);
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setTitle("Log Editor");
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
    }

    public void deleteLogWarn(ActionEvent event) throws IOException {
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
        secondStage.setResizable(false);
        secondStage.show();

        button.setOnAction(e->{
        		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EffortLoggerMainUI.fxml"));
        		secondStage.close();
				Parent root;
				try {
					root = fxmlLoader.load();
		        	Main object = new Main();
		        	String LogIdtext = LogId.getText();
		        	int indexOf =  LogIdtext.indexOf(" ");
		        	int deleteLog = Integer.parseInt(LogIdtext.substring(indexOf + 1));
		        	String connectionString = "mongodb+srv://<username>:<password>@effortlogger.zfgzhfr.mongodb.net/?retryWrites=true&w=majority";
		        	MongoClient mongoClient = MongoClients.create(connectionString);
		        	MongoDatabase db = mongoClient.getDatabase("Effortlogs");
		        	MongoCollection<Document> userCol = db.getCollection("users");
		        	MongoCollection<Document> col = db.getCollection("logs");
		        	FindIterable<Document> findUserId = col.find(eq("log-id", deleteLog));
		    		Document targetObject = findUserId.first();
		    		String userId = targetObject.get("user-id").toString();
		    		FindIterable<Document> findUser = userCol.find(eq("userID", userId));
		    		Document targetuser = findUser.first();
		    		String userName = targetuser.get("username").toString();
		        	object.deleteEntry(deleteLog, "logs", "log-id", db);
		        	Login logins = new Login();
		        	logins.setUsername(userName);
		        	EffortLoggerMainUI effortLoggerMainUIController = fxmlLoader.getController();
					effortLoggerMainUIController.update(logins, db, userCol, col);
		        	secondStage.close();
		        	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
					Scene scene = new Scene(root);
					stage.setTitle("EffortLogger Main Menu");
					stage.setScene(scene);
					stage.setResizable(false);
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
