package application;

import static com.mongodb.client.model.Filters.eq;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import model.log;

public class logThumbController {

    @FXML
    private Text LogDesc;

    @FXML
    private Text LogId;
    
    
	
    public void setData(log logShow) {
    	LogDesc.setText(logShow.getDescription());
    	LogId.setText(logShow.getLogID());    			
    }
}