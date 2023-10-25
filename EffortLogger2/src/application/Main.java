package application;
	
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

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
import static com.mongodb.client.model.Filters.eq;
import java.security.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;


public class Main extends Application {
	
    public static void main(String[] args) {
        launch(args);
    }
    
    public void start(Stage primaryStage) {
    	//connection string
    	String connectionString = "mongodb+srv://eyenriqu:XmIKNbx9Oyj7YMTp@effortlogger.zfgzhfr.mongodb.net/?retryWrites=true&w=majority";
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            try {
                // Send a ping to confirm a successful connection
                MongoDatabase db = mongoClient.getDatabase("Effortlogs");
                MongoCollection<Document> col = db.getCollection("logs");

            } catch (MongoException e) {
                e.printStackTrace();
            }
        }
    	
    }
    
    //insert into logs collection in database we have to manually pass the db
    public void insertLog(String user, int security, String description, MongoDatabase db) {
    	MongoCollection<Document> col = db.getCollection("logs"); 
        Document test = new Document("user",user)
        		.append("security-level", security)
        		.append("Description", description)
        		.append("TimeStamp", new java.util.Date());
        col.insertOne(test);
        System.out.println("Inserted document");
    }
    
    //helps find every document in the database that matches a filter and data
    //If you are looking for a user who's know is AJ then filter variable would = user 
    //data would = AJ
    //pass the database in the start method
    //returns an iterable that has all the documents
    public FindIterable<Document> findAll(String filter, String data, MongoDatabase db)
    {
    	MongoCollection<Document> col = db.getCollection("logs");
    	FindIterable<Document> iterable;
    	if(filter.equals("security-level")) {
    		iterable = col.find(eq(filter, Integer.parseInt(data)));
    	}
    	else {
    		iterable = col.find(eq(filter, data));
    	}
    	return iterable;
    }
    
    
    //print all logs that where found and store in an iterable
    public void printLogs(FindIterable<Document> iterable) {
    	MongoCursor<Document> results = iterable.iterator();
        while(results.hasNext())
        {
        	System.out.println(results.next().toJson());
       
        }
    }
    
    
}
