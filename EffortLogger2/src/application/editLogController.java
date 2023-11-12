package application;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class editLogController {

    @FXML
    private TextArea editDetails;

    @FXML
    private TextField editProj;

    @FXML
    private TextField editSID;

    @FXML
    private Text editScreen;

    @FXML
    private DatePicker editTS;

    @FXML
    private Text editWelcome;

    private int logID;
    private Login login;
    
    public void setData(int logID, Login login) {
    	this.logID = logID;
    	this.login = login;
    	editWelcome.setText("Editing Log: " + logID);
    }
    
    

}