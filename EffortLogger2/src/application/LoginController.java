package application;

//import java.io.FileInputStream;
//import org.jcp.xml.dsig.internal.dom.Utils;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
//import java.io.FileInputStream;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import org.bson.Document;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
//import com.google.zxing.common.detector.MathUtils;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import de.taimos.totp.TOTP;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
public class LoginController{
	private Scene scene;
	private Stage stage;
	private Parent root;

	@FXML
	private TextField username; //these variables have to have the same name as the ID in fxml file
	@FXML
	private PasswordField password;
	@FXML
	private Button submit;
	@FXML
	private Label failLabel;
	@FXML
	private FlowPane imagePane;
	@FXML
	private ImageView imageViewer;
	@FXML
	private Text authText;
	@FXML
	private TextField authEnter;
	@FXML
	private Button authSubmit;

	private boolean Monkey = false;
	String loginUsername;
	String loginPassword;
	
	
	String connectionString = "mongodb+srv://ndlovelace13:7Cpa4yubfjj7aPql@effortlogger.zfgzhfr.mongodb.net/?retryWrites=true&w=majority";
	MongoClient mongoClient = MongoClients.create(connectionString);
	MongoDatabase db = 	mongoClient.getDatabase("Effortlogs");
	MongoCollection<Document> col = db.getCollection("logs");
	MongoCollection<Document> userCol = db.getCollection("users");
	Login loginSystem = new Login();

	private static String authKey = "F4HYRBEPAROMJTZVMJBRQVCNWMJJVTCH";

	public void submit2FA(ActionEvent event) throws IOException
	{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EffortLoggerMainUI.fxml"));

		String code = authEnter.getText();
		if (code.equals(getTOTPCode(authKey)))
		{
			
			root = fxmlLoader.load();
			EffortLoggerMainUI effortLoggerMainUIController = fxmlLoader.getController();
			effortLoggerMainUIController.recieveTransferedItems(connectionString, db, col, userCol, mongoClient, loginSystem);
			
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setTitle("EffortLogger Main Menu");
			stage.setScene(scene);
			stage.show();
		}
		else
		{
			authEnter.clear();
			authText.setFill(Color.RED);
			authText.setText("Code does not match. Please Try Again or resubmit login credentials for a new QR code");
		}
	}

	//built this method assuming we have getters and setters in login java
	public void submitLogin(ActionEvent event) throws IOException, WriterException{
		//if you want to load another page do it here ex: FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(file name))
		
		if(Monkey)
		{

		}
		
		/*
		Login loginSystem = new Login();
		//feel free to change how I call this login you can probably make a new login object
		String connectionString = "mongodb+srv://ndlovelace13:7Cpa4yubfjj7aPql@effortlogger.zfgzhfr.mongodb.net/?retryWrites=true&w=majority";
		MongoClient mongoClient = MongoClients.create(connectionString);
		MongoDatabase db = 	mongoClient.getDatabase("Effortlogs");
		MongoCollection<Document> col = db.getCollection("logs");
		MongoCollection<Document> userCol = db.getCollection("users");
        //System.out.println("Code: " + loginSystem.generateAuthKey());*/
		loginUsername = username.getText();
		loginPassword = password.getText();
		// do security check
		System.out.println("This is the username: " + loginUsername);
		System.out.println("This is the password: " + loginPassword);
		loginSystem.setPassword(loginPassword);
		loginSystem.setUsername(loginUsername);

		// This if statement checks to see if the login information is correct or not
		// A username and password in the database currently you can test is
		// Username: jlkim1  Password: cats12
		if (loginSystem.findUser(loginUsername, loginPassword, db)) {
				// Jump over to the next scene, likely main effort logger UI
			System.out.println("Account found, moving to main menu.");
			//root = fxmlLoader.load();
			failLabel.setText("");
			// before we load next scene, perform 2FA check

			String email = "test@gmail.com";
			String companyName = "Carter Company";
			String barCodeUrl = getGoogleAuthBarCode(authKey, email, companyName);
			System.out.println(barCodeUrl);
			String filepath = "src/application/code.png";
			createQRCode(barCodeUrl, filepath, 200,200);
			//InputStream stream = new FileInputStream("C:/Users/James Kim/Documents/Eclipse_Workspaces/EffortLogger/EffortLogger2/src/application/code.png");
			Image image = new Image(getClass().getResourceAsStream("code.png"));
			//System.out.println(image.toString());
			imageViewer.setViewport(new Rectangle2D(0,0,200,200));
			imageViewer.setVisible(true);
			imageViewer.setImage(image);
			System.out.println("Something should have showed up");

			// opening 2FA gui to user
			authText.setText("Please Enter 2 Factor Authentication Code");
			authEnter.setEditable(true);
			authEnter.setOpacity(1f);
			authSubmit.setOpacity(1f);
			authSubmit.setDisable(false);

			// testing synch of google auth code and local app code
			/*
			while (true)
			{
				String code = loginSystem.getTOTPCode(authKey);
				if (!code.equals(lastCode))
				{
					System.out.println(code);
				}
				lastCode = code;
				try
				{
					Thread.sleep(1000);
				} catch (InterruptedException e) {};
			}*/

		}
		else {
			System.out.println("Account not found");
			// .clear() removes all of the text that's currently present in the text field
			username.clear();
			password.clear();
			failLabel.setText("Password not found, please try again!");
			authText.setText("");
			authEnter.setEditable(false);
			authEnter.setOpacity(0f);
			authSubmit.setOpacity(0f);
			authSubmit.setDisable(true);
			imageViewer.setVisible(false);;
			//mongoClient.close();
		}
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

	// generate string to send to server
	public String getGoogleAuthBarCode(String authKey, String account, String issuer)
	{
		try {
	        return "otpauth://totp/"
	                + URLEncoder.encode(issuer + ":" + account, "UTF-8").replace("+", "%20")
	                + "?secret=" + URLEncoder.encode(authKey, "UTF-8").replace("+", "%20")
	                + "&issuer=" + URLEncoder.encode(issuer, "UTF-8").replace("+", "%20");
	    } catch (UnsupportedEncodingException e) {
	        throw new IllegalStateException(e);
	    }
	}

	// generate QR Code
	public void createQRCode (String barCodeData, String filePath, int height, int width)

		throws WriterException, IOException {
			BitMatrix matrix = new MultiFormatWriter().encode(barCodeData,
					BarcodeFormat.QR_CODE, width, height);

			try (FileOutputStream out = new FileOutputStream(filePath))
			{
				MatrixToImageWriter.writeToStream(matrix, "png", out);
			}
		}

	// checks code against server
	public String getTOTPCode(String authKey)
	{
		Base32 base32 = new Base32();
		byte[] bytes = base32.decode(authKey);
		String hexKey = Hex.encodeHexString(bytes);
		return TOTP.getOTP(hexKey);
	}

}