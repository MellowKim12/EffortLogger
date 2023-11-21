package application;

import static com.mongodb.client.model.Filters.eq;

import java.io.IOException;
import java.util.ArrayList;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.log;
//
//implements Initializable
public class EffortLoggerMainUI {
	private Scene scene;
	private Stage stage;
	private Parent root;
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

    @FXML
    private Button startPP;

    @FXML
    private Button addButton;

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
				for (log element : list) {
					FXMLLoader fxml = new FXMLLoader();
					fxml.setLocation(getClass().getResource("logThumb.fxml"));
					VBox box = fxml.load();
					logThumbController logthumb = fxml.getController();
					logthumb.setData(element);
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

	// handles front-end UI update of documents stored in server
	public void update(Login loginSystems, MongoDatabase db, MongoCollection<Document> userCol,MongoCollection<Document> col ) {
		this.loginSystem = loginSystems;
		this.userCol = userCol;
		this.col = col;
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
				for (log element : list) {
					FXMLLoader fxml = new FXMLLoader();
					fxml.setLocation(getClass().getResource("logThumb.fxml"));
					VBox box = fxml.load();
					logThumbController logthumb = fxml.getController();
					logthumb.setData(element);
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

	public void startPoker(ActionEvent event) throws IOException{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PlanningPokerMainMenu.fxml"));

		root = fxmlLoader.load();
		PlanningPokerMainMenuController planningPokerMainMenuController = fxmlLoader.getController();
		planningPokerMainMenuController.recieveTransferedItems(connectionString, db, col, userCol, mongoClient, loginSystem);

		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setTitle("Planning Poker Set Up");
		stage.setScene(scene);
		stage.show();

	}

	public void addPage(ActionEvent event) throws IOException
	{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LogAddition.fxml"));

		System.out.println("Switching to the Add Log Page");
		root = fxmlLoader.load();
		LogAdditionController effortLoggerAddLogController = fxmlLoader.getController();
		effortLoggerAddLogController.recieveTransferedItems(connectionString, db, col, userCol, mongoClient, loginSystem);

		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setTitle("Add Log");
		stage.setScene(scene);
		stage.show();
	}

}