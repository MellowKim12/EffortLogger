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
import org.bson.Document;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Updates.*;


public class Main extends Application {
	
    public static void main(String[] args) {
        launch(args);
    }
    
    public void start(Stage primaryStage) {
    	//connection string replace username with your username and password with your password
    	String connectionString = "mongodb+srv://<username>:<password>@effortlogger.zfgzhfr.mongodb.net/?retryWrites=true&w=majority";
    	MongoDatabase db;
    	MongoCollection<Document> col;
    	//trying to connect the driver 
    	try (MongoClient mongoClient = MongoClients.create(connectionString)) {
           //this second try is for anything mongo related
    		try {
    			//finding the database and collection
                db = mongoClient.getDatabase("Effortlogs");
                col = db.getCollection("logs");
                update("log-num", "7", 8, col);
            } catch (MongoException e) {
                e.printStackTrace();
            }
        }
    	
    }
    
    //insert into logs collection in database we have to manually pass the db feel free to add more tags
    //to get the amounts of logs use col.count() and add 1 
    // example use: insertLog("Cole", 1, "description of user story", col.countDocuments() + 1, db)
    public void insertLog(String user, int security, String description,long logs, MongoDatabase db) {
    	MongoCollection<Document> col = db.getCollection("logs"); 
        Document test = new Document("user",user)
        		.append("log-num", logs)
        		.append("security-level", security)
        		.append("Description", description)
        		.append("TimeStamp", new java.util.Date());
        col.insertOne(test);
        System.out.println("Inserted document");
    }
    
    //helps find every document in the database that matches a filter and data
    //If you are looking for a user who's name is AJ then filter variable would = user 
    //data would = AJ
    //pass the database from the start method
    //returns an iterable that has all the documents found
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
    
    
    //print all logs that where found and stored in an iterable
    public void printLogs(FindIterable<Document> iterable) {
    	MongoCursor<Document> results = iterable.iterator();
        while(results.hasNext())
        {
        	System.out.println(results.next().toJson());
       
        }
    }
    
    //finds the log we want to delete by log number then deletes it
    public void deleteLog(long logNum, MongoCollection<Document> col){
    	col.deleteOne(eq("log-num", logNum));
    	System.out.println("Deleted Document in DB");
    }
    
    //finds log by log number then sets attribute you decided in update string with data 
    public void update(String update, String data,long logNum,MongoCollection<Document> col) {
    	if(update.equals("log-num") || update.equals("security-level"))
    	{
    		col.findOneAndUpdate(eq("log-num", logNum), set(update, Integer.parseInt(data)));
    	}
    	else {
    		col.findOneAndUpdate(eq("log-num", logNum), set(update, data));
    	}
    	System.out.println("Updated Document in DB");
    }
    
}
