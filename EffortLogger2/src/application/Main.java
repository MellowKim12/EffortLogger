package application;
	
import java.util.Scanner;
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
import static com.mongodb.client.model.Sorts.descending;


public class Main extends Application {
	
    public static void main(String[] args) {
        launch(args);
    }
    
    public void start(Stage primaryStage) {
    	//connection string replace username with your username and password with your password
    	String connectionString = "mongodb+srv://ndlovelace13:7Cpa4yubfjj7aPql@effortlogger.zfgzhfr.mongodb.net/?retryWrites=true&w=majority";
    	MongoDatabase db;
    	MongoCollection<Document> col;
    	
    	//basic user stuff that James/Cole can link to login credentials later on
    	String currentUser = "Hugh Mungus";
    	int userId = 420;
    	int userSecurity = 1;
    	
    	int project = 1;
    	
    	//stuff needed for the big while loop
    	String continueChoice = "y";
    	int action;
    	Scanner in = new Scanner(System.in);
    	//trying to connect the driver 
    	try (MongoClient mongoClient = MongoClients.create(connectionString)) {
           //this second try is for anything mongo related
    		try {
    			//finding the database and collection
                db = mongoClient.getDatabase("Effortlogs");
                col = db.getCollection("logs");
                //update("log-num", "15", 7, col);
                //printLogs(findAll("user", "eyenriqu", db));
                
                System.out.println("Welcome to the EffortLogger V2 Prototype!\n");
                //Main EffortLogger Loop (FOR NOW)
                while (continueChoice.equals("y"))
                {
                	System.out.println("Please select an action from the following list:\n");
                	System.out.println("1 = Add a Story or Log");
                	System.out.println("2 = Edit an Existing Story or Log");
                	System.out.println("3 = Delete an Existing Story or Log");
                	System.out.println("4 = Search for a Story or Log");
                	System.out.println("5 = Print all Stories or Logs\n");
                	
                	action = in.nextInt();
                	while (action < 1 || action > 5)
                	{
                		System.out.println("Your input is invalid, please choose a number from the above list");
                		action = in.nextInt();
                	}
                	//vars necessary for decision-making
                	String slChoice;
                	String title;
                	String details;
                	
                	switch (action)
                	{
                		case 1:
                			System.out.println("adding a story or log!");
                			
                			System.out.println("Would you like to add a story or log? Enter (S/L)\n");
                			slChoice = in.next().toLowerCase();
                			while (!slChoice.equals("s") && !slChoice.equals("l"))
                        	{
                        		System.out.println("Your input is invalid, please enter S/L");
                        		slChoice = in.next().toLowerCase();
                        	}
                			if (slChoice.equals("s"))
                			{
                				//slChoice = "stories";
                				in.nextLine();
                				System.out.println("What title would you like this user story to have?\n");
                				title = in.nextLine();
                				System.out.println("What description would you like to add to the story?\n");
                				details = in.nextLine();
                				insertStory(userId, project, title, details, db);
                			}
                			else //if (slChoice.equals("l"))
                			{
                				//slChoice = "logs";
                				in.nextLine();
                				System.out.println("What details would you like to add to the log?\n");
                				details = in.nextLine();
                				insertLog(userId, project, 1, details, db);
                			}

                			
                			break;
                		case 2:
                			System.out.println("editing a story or log!");
                			
                			break;
                		case 3:
                			System.out.println("deleting a story or log!");
                			break;
                		case 4:
                			System.out.println("searching for a story or log!");
                			break;
                		case 5:
                			System.out.println("printing all story or log!");
                			break;
                	}
                	
                	System.out.println("Would you like to continue using EffortLogger V2? (Y/N)");
                	continueChoice = in.next().toLowerCase();
                	while (!continueChoice.equals("y") && !continueChoice.equals("n"))
                	{
                		System.out.println("Your input is invalid, please enter Y/N");
                		continueChoice = in.next().toLowerCase();
                	}
                }
                
            } catch (MongoException e) {
                e.printStackTrace();
            }
        }
    	
    }
    
    //insert into logs collection in database we have to manually pass the db feel free to add more tags
    //to get the amounts of logs use col.count() and add 1 
    // example use: insertLog("Cole", 1, "description of user story", col.countDocuments() + 1, db)
    public void insertLog(int userId, int projectId, int storyId, String details, MongoDatabase db) 
    {
    	
    	MongoCollection<Document> col = db.getCollection("logs");
    	//return the most recent item in the collection
    	int logId = newId("logs", "log-id", db);
        Document test = new Document("user-id", userId)
        		.append("project-id", projectId)
        		.append("story-id", storyId)
        		.append("log-id", logId)
        		.append("details", details)
        		.append("TimeStamp", new java.util.Date());
        col.insertOne(test);
        System.out.println("Log successfully added");
    }
    
    public void insertStory(int userId, int projectId, String title, String description, MongoDatabase db) 
    {
    	
    	MongoCollection<Document> col = db.getCollection("stories"); 
    	//return a newId based on the last id in the collection
    	int storyId = newId("stories", "story-id", db);
        Document test = new Document("user",userId)
        		.append("project-id", projectId)
        		.append("story-id", storyId)
        		.append("title", title)
        		.append("description", description)
        		.append("TimeStamp", new java.util.Date());
        col.insertOne(test);
        System.out.println("Story successfully added");
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
    
    //method finding the last of a certain collection
    public FindIterable<Document> findLast(String colName, String sortParam, MongoDatabase db)
    {
    	MongoCollection<Document> col = db.getCollection(colName);
    	FindIterable<Document> iterable;
    	iterable = col.find().limit(1).sort(descending(sortParam));
    	return iterable;
    }
    
    //method that will return a new id number based off the last id of a certain collection
    public int newId(String colName, String sortParam, MongoDatabase db) 
    {
    	FindIterable<Document> bruh = findLast(colName, sortParam, db);
		MongoCursor<Document> results = bruh.iterator();
		int newId = Integer.parseInt(results.next().get(sortParam).toString());
		newId++;
		return newId;
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
