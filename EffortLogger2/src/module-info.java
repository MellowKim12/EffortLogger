module EffortLogger2 {
	requires javafx.controls;
	requires org.mongodb.driver.sync.client;
	requires org.mongodb.bson;
	requires org.mongodb.driver.core;
	
	opens application to javafx.graphics, javafx.fxml;
}
