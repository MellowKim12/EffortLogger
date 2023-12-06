module EffortLogger2 {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	requires org.mongodb.driver.sync.client;
	requires org.mongodb.bson;
	requires org.mongodb.driver.core;
	requires com.google.zxing;
	requires org.apache.commons.codec;
	requires totp;
	requires com.google.zxing.javase;
	//requires

	opens application to javafx.graphics, javafx.fxml;
}
