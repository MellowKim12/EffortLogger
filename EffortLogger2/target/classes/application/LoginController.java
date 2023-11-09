package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
public class LoginController{
	private Scene scene;
	private Stage stage;
	
	@FXML
	private TextField username; //these variables have to have the same name as the ID in fxml file
	@FXML
	private PasswordField password;
	
	//built this method assuming we have getters and setters in login java
	public void submitLogin(ActionEvent event) throws IOException{
		//if you want to load another page do it here ex: FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(file name))
		//feel free to change how I call this login you can probably make a new login object
		Login.setUsername(username.getText()); 
		Login.setPassword(password.getText());
		// do security check 
		System.out.println("This is the username: " + Login.getUsername());
		System.out.println("This is the password: " + Login.getPassword());
		//if we can go to new page use this code
		/* stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		 * scene = new Scene(fxmlLoader.load(),int, int) these ints are the x and y axis.
		 * stage.setTitle("") choose a title name
		 * <name of new controller class> control = fxmlLoader.getController
		 * can call controller functions from here  
		 * ex:
		 * control.displayLogin() if there was a displayLogin() function in the other controller class
		 * stage.setScene(scene)
		 * stage.show()
		 */
	}
	
}