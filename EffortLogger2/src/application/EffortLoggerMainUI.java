package application;

import java.io.IOException;
import java.net.URL;

import com.mongodb.BasicDBObject;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;

import static com.mongodb.client.model.Filters.eq;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.bson.Document;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Updates.*;
import static com.mongodb.client.model.Sorts.descending;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.log;
import javafx.scene.layout.VBox;
//
//implements Initializable
public class EffortLoggerMainUI {
	
	private String connectionString;
	private MongoDatabase db;
	private MongoCollection<Document> col;
	private MongoCollection<Document> userCol;
	private MongoClient mongoClient;
	private Login loginSystem;
	private ArrayList<log> list;
	@FXML
    private GridPane logPane;

    @FXML
    private Text welcome;
	
	//This method is called by the effortLoggerMainUIController in LoginController.java
	//Allows us to send variables we want to send to other controller files
	
	public void recieveTransferedItems(String connectionString, MongoDatabase db, MongoCollection<Document> col, MongoCollection<Document> userCol, MongoClient mongoClient, Login loginSystem) {
		this.connectionString = connectionString;
		this.db = db;
		this.col = col;
		this.userCol = userCol;
		this.mongoClient = mongoClient;
		this.loginSystem = loginSystem;
		welcome.setText("Welcome To Effort Logger: "+ loginSystem.getUsername());
		//This System.out.println prints out a piece of the transfered data to make sure the transfer worked
		list = new ArrayList<>();
		System.out.println(loginSystem.getUsername());
		FindIterable<Document> filterUsers = userCol.find(eq("username", loginSystem.getUsername()));
		Document targetObject = filterUsers.first();
		int id = Integer.parseInt(targetObject.get("userID").toString());
		FindIterable<Document> logs = col.find(eq("user-id", id));
		if(logs.first() != null) {
			MongoCursor<Document> results = logs.iterator();
			while(results.hasNext())
			{
				Document iterLog = results.next();
				log insertlog = new log();
				insertlog.setDescription(iterLog.get("details").toString());
				insertlog.setlogID((int)iterLog.get("log-id"));
				insertlog.setLogin(loginSystem);
				list.add(insertlog);
			}
			int columns = 0;
			int rows = 1;
			try {
				for(int i = 0; i < list.size(); i++) {
					FXMLLoader fxml = new FXMLLoader();
					fxml.setLocation(getClass().getResource("logThumb.fxml"));
					VBox box = fxml.load();
					logThumbController logthumb = fxml.getController();
					logthumb.setData(list.get(i));
					if(columns == 1)
					{
						columns = 0;
						rows++;
					}
//					GridPane.setMargin(box, new Insets(10));
					logPane.add(box, columns++, rows);
					
					
				}
			}
				catch(IOException e) {
					e.printStackTrace();
				}
		}
		
	}
	
	public void update(Login loginSystems, MongoDatabase db, MongoCollection<Document> userCol,MongoCollection<Document> col ) {
		welcome.setText("Welcome To Effort Logger: "+ loginSystems.getUsername());
		//This System.out.println prints out a piece of the transfered data to make sure the transfer worked
		list = new ArrayList<>();
		FindIterable<Document> filterUsers = userCol.find(eq("username", loginSystems.getUsername()));
		Document targetObject = filterUsers.first();
		int id = Integer.parseInt(targetObject.get("userID").toString());
		FindIterable<Document> logs = col.find(eq("user-id", id));
		if(logs.first() != null) {
			MongoCursor<Document> results = logs.iterator();
			while(results.hasNext())
			{
				Document iterLog = results.next();
				log insertlog = new log();
				insertlog.setDescription(iterLog.get("details").toString());
				insertlog.setlogID((int)iterLog.get("log-id"));
				insertlog.setLogin(loginSystem);
				list.add(insertlog);
			}
			int columns = 0;
			int rows = 1;
			try {
				for(int i = 0; i < list.size(); i++) {
					FXMLLoader fxml = new FXMLLoader();
					fxml.setLocation(getClass().getResource("logThumb.fxml"));
					VBox box = fxml.load();
					logThumbController logthumb = fxml.getController();
					logthumb.setData(list.get(i));
					if(columns == 1)
					{
						columns = 0;
						rows++;
					}
//					GridPane.setMargin(box, new Insets(10));
					logPane.add(box, columns++, rows);
					
					
				}
			}
				catch(IOException e) {
					e.printStackTrace();
				}
		}
	}
	
}