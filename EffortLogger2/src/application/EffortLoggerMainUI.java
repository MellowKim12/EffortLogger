package application;

import java.io.IOException;

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
import org.bson.Document;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Updates.*;
import static com.mongodb.client.model.Sorts.descending;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;


public class EffortLoggerMainUI{
	
	private String connectionString;
	private MongoDatabase db;
	private MongoCollection<Document> col;
	private MongoCollection<Document> userCol;
	private MongoClient mongoClient;
	private Login loginSystem;
	
	//This method is called by the effortLoggerMainUIController in LoginController.java
	//Allows us to send variables we want to send to other controller files
	public void recieveTransferedItems(String connectionString, MongoDatabase db, MongoCollection<Document> col, MongoCollection<Document> userCol, MongoClient mongoClient, Login loginSystem) {
		this.connectionString = connectionString;
		this.db = db;
		this.col = col;
		this.userCol = userCol;
		this.mongoClient = mongoClient;
		this.loginSystem = loginSystem;
		//This System.out.println prints out a piece of the transfered data to make sure the transfer worked
		System.out.println(connectionString);
		System.out.println("We transfered over this login information: " + loginSystem.getUsername());
	}
	
	
}