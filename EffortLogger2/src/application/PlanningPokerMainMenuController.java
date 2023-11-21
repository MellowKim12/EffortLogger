package application;
import java.io.IOException;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PlanningPokerMainMenuController {
		private Scene scene;
		private Stage stage;
		private Parent root;

		@FXML
		private TextField sessionField;
		@FXML
		private TextField playerField;
		@FXML
		private Button startPp;
		@FXML
		private Label errorLabel;
		@FXML
		private TextField issues;




		private String connectionString;
		private MongoDatabase db;
		private MongoCollection<Document> col;
		private MongoCollection<Document> userCol;
		private MongoClient mongoClient;
		private Login loginSystem;
		private boolean valid = true;
		private String issuesGotten;
		private String[] issueList;

		public void startPoker(ActionEvent event) throws IOException{
			
			// Below is a list of if statements to check to see if there are any invalid inputs for the three
			// text fields. If in the process of running this code one of the if statements is true, the variable
			// valid is set to false, meaning that one or more of the inputs are false. This will be used later
			// to check if it's ok to continue on to the Planning Poker game.
			
			// This is set up for later in the code when we move to the next JavaFX scene
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PlanningPokerUI.fxml"));
			// Resets the TextFields and labels
			errorLabel.setText("");
			playerField.getText();
			sessionField.getText();
			issues.getText(); 
			valid = true;
			// Checks to see if the name of the Planning Poker session is empty or not
			if (sessionField.getText().equals(""))
			{
				System.out.println("Title of Session cannot be blank");
				errorLabel.setText("Title of Session cannot be blank");
				sessionField.clear();
				valid = false;
			}

			String sessionName = sessionField.getText();
			
			// Checks to see if the text field for the number of players is empty or not
			if (playerField.getText().equals(""))
			{
				System.out.println("Number of players cannot be blank");
				errorLabel.setText("Number of players cannot be blank");
				playerField.clear();
				valid = false;
			}
			
			// Checks to see if possibly dangerous characters are inserted inside of the # of players text field
			if (playerField.getText().contains(":") || playerField.getText().contains("$")) {
				System.out.println("Invalid input character for the playerField");
				errorLabel.setText("Invalid input character");
				playerField.clear();
				valid = false;
			}
			
			// Checks to see if there are some possible dangerous characters inside of the input for the sessionName variable
			if (sessionName.contains(":") || sessionName.contains("$")) {
				errorLabel.setText("Invalid input character for the name of the session");
				sessionField.clear();
				valid = false;
			}
			
			int playerNum = 0;
			boolean numError = false;
			// This try catch statement is used to check if the number of players textfield is an integer
			try {
				playerNum = Integer.parseInt(playerField.getText());
			} catch (NumberFormatException nfe){
				numError = true;
				errorLabel.setText("The input for the number of players is not a number!");
				System.out.println("The input for the number of players is not a number!");
				playerField.clear();
				valid = false;
			}
			// If it is an integer, it checks to see if the number of players is within the allowed for number of players
			if (numError == false) {
				if (playerNum <= 1 || playerNum > 10)
				{
					errorLabel.setText("PlayerNum not valid, please re-enter");
					playerField.clear();
					valid = false;
				}
			}
			
			// This checks to see if the session name is at most 30 characters long
			if (sessionName.length() > 30)
			{
				System.out.println("Session name is too long. Please keep it to underneath 30 characters");
				errorLabel.setText("Session name is too long. Please keep it to underneath 30 characters");
				sessionField.clear();
				valid = false;
			}
			
			// Checks to see if the issues input section is less not empty
			if (issues.getText().equals(""))
			{
				System.out.println("Issues cannot be blank. Please enter at least one issue");
				errorLabel.setText("Issues cannot be blank. Please enter at least one issue");
				issues.clear();
				valid = false;
			}

			issuesGotten = issues.getText();
			issueList = issuesGotten.split(",");

			for (String element : issueList)
				System.out.println(element);

			System.out.println(valid);
			// If none of the above if statements are hit, this if statement will take the user to the actual planning poker game
			if (valid == true)
			{

				root = fxmlLoader.load();

				PlanningPokerController planningPokerController = fxmlLoader.getController();
				planningPokerController.recieveTransferedItems(connectionString, db, col, userCol, mongoClient, loginSystem, sessionName, playerNum, issueList);
				planningPokerController.setScene(sessionName,  issueList, playerNum);


				stage = (Stage)((Node)event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				stage.setTitle(sessionName);
				stage.setScene(scene);
				stage.show();
			}
			
			// If at least one of the above if statements are hit, this if statement will reset this page and allow the user to try again
			if (valid == false) {
				System.out.println("Error Occured!");
				issues.clear();
				sessionField.clear();
				playerField.clear();
			}
		}

		// This method transfers over data from the EffortLoggerMainUIController class and sets up variable for this class
		public void recieveTransferedItems(String connectionString, MongoDatabase db, MongoCollection<Document> col, MongoCollection<Document> userCol, MongoClient mongoClient, Login loginSystem) {
			this.connectionString = connectionString;
			this.db = db;
			this.col = col;
			this.userCol = userCol;
			this.mongoClient = mongoClient;
			this.loginSystem = loginSystem;
		}


}