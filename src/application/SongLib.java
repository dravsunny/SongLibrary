package application;
//Created by Jaideep Duggempudi and Suneet Dravida
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import view.SongLibController;

public class SongLib extends Application{
	
	public void start(Stage primaryStage) throws Exception {
		
		
		// set up FXML loader
		FXMLLoader loader = new FXMLLoader();   
		loader.setLocation(
				getClass().getResource("/view/SongLib.fxml"));
		
		// load the fxml
		GridPane root = (GridPane)loader.load();

		// get the controller (Do NOT create a new Controller()!!)
		// instead, get it through the loader
		SongLibController listController = loader.getController();
		listController.start(primaryStage);

		Scene scene = new Scene(root, 800, 500);
		//scene.getStylesheets().add(getClass().getResource("css.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setTitle("Song Library");
		primaryStage.show(); 

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);

	}
}
