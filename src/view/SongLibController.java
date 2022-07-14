package view;
//Created by Jaideep Duggempudi and Suneet Dravida
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.stage.Stage;


public class SongLibController {
	
	@FXML
	ListView<String> listview;
	@FXML
	TextField songname;
	@FXML
	TextField artist;
	@FXML
	TextField album;
	@FXML
	TextField year;
	
	@FXML
	TextField songname1;
	@FXML
	TextField artist1;
	@FXML
	TextField album1;
	@FXML
	TextField year1;
	
	private String file = "src/Song Library.txt";
	@FXML
	Label display;
	@FXML
	Label song_name;
	@FXML
	Label artist_name;
	@FXML
	Label album_name;
	@FXML
	Label year_released;
	@FXML 
	Label addsonglabel;
	@FXML
	Label songlabel;
	@FXML
	Label artistlabel;
	@FXML
	Label albumlabel;
	@FXML
	Label yearlabel;
	@FXML
	Button confirm;
	@FXML
	Button deletebutton;
	@FXML
	Button editbutton;
	
	
	public Stage stage;
	public ObservableList<String> obsList;              
	public ObservableMap<String, ArrayList<String>> obsmap;
	public Map<String, ArrayList<String>> map;
	public boolean editingmode = true;
	
	
	public void start(Stage mainstage) throws IOException{                
		// create an ObservableList 
		// from an ArrayList  
		stage = mainstage;
		map = new HashMap<String, ArrayList<String>>();
		readfromFile(map);
		listview.getSelectionModel().select(0);
		displayInformation(listview.getSelectionModel().getSelectedItem());
		listview
		.getSelectionModel()
		.selectedIndexProperty()
		.addListener(
				(obs, oldVal, newVal) -> {
				//showItem(mainStage)
				
				
			
				if(editingmode == false)
				{
					editing(listview.getSelectionModel().getSelectedItem());
						
						
				}
				
				displayInformation(listview.getSelectionModel().getSelectedItem());
				
					
				
			});
		
		stage.setOnCloseRequest(event -> {
			try {
				save();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
			 
	}
	public void save() throws IOException
	{
		FileWriter writer = new FileWriter(file, false);

		
		for(String key: obsmap.keySet())
		{
			ArrayList<String> temp = obsmap.get(key);
			String write = temp.get(0)+"@"+temp.get(1)+"@"+temp.get(2)+"@"+temp.get(3);
			writer.write(write+"\n");
			
		}
		writer.close();
	}
	public void displayInformation(String key)
	{
		
		
		ArrayList<String> temp = obsmap.get(listview.getSelectionModel().getSelectedItem());
		if(temp != null)
		{
			song_name.setText("Song: " + temp.get(0));
			artist_name.setText("Artist: " + temp.get(1));
			album_name.setText("Album: " + temp.get(2));
			year_released.setText("Year: " + temp.get(3));
		}
	}
	public void delete(ActionEvent e)
	{
		if(!listview.getItems().isEmpty())
		{
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.initOwner(stage);
			alert.setTitle("Delete Selected Song from Library");
			alert.setHeaderText("You're about to DELETE this song from your library");
			alert.setContentText("Do you want to continue?");
			
			if (alert.showAndWait().get() == ButtonType.OK){
				
				int selectedIndex = listview.getSelectionModel().getSelectedIndex();
				obsmap.remove(listview.getSelectionModel().getSelectedItem());
				
				if(selectedIndex == 0 && listview.getItems().size() != 1)
				{
					listview.getSelectionModel().select(selectedIndex+1);
					listview.getItems().remove(0);
					
					
					
				}
				else if(selectedIndex == 0 && listview.getItems().size() == 1)
				{
					listview.getItems().clear();
					song_name.setText("\n");
					artist_name.setText("\n");
					album_name.setText("\n");
					year_released.setText("\n");
					display.setText("There are no songs in your library");
			
				}
				else
				{
					if(selectedIndex == listview.getItems().size() - 1)
					{
						listview.getSelectionModel().select(selectedIndex-1);
						listview.getItems().remove(selectedIndex);
						
					}
					else
					{
						listview.getItems().remove(selectedIndex);
						listview.getSelectionModel().select(selectedIndex);
					}
				}
					
			
				
			
			} 
		}
		
	}
	public void addSong(ActionEvent e)
	{
		
		String songs = songname.getText();
		String artists = artist.getText();
		String albums = album.getText();
		String years = year.getText();
		String key = songs + " by " + artists;
		
		
		String exceptions = "Errors: ";
		
		if(songs.equals("") || artists.equals(""))
		{
			exceptions = exceptions + "\n" + "Must include both song and artist, ";
		}
		if(!years.equals(""))
		{
			try {  
			    int i = Integer.parseInt(years);
			    if(i <= 0)
			    {
			    	exceptions = exceptions + "\n" + "Must be a year > 0";
			    }
	
			  } catch(NumberFormatException a){  
			     exceptions = exceptions + "\n" + "Must type number for year";
			  }  
		}
		for(String keys: obsmap.keySet())
		{
			if(key.compareToIgnoreCase(keys) == 0)
			{
				exceptions = exceptions + "\n" + "Song already exist in Library";
				break;
			}
		}
	
			
		
		if(exceptions.equals("Errors: "))
		{
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.initOwner(stage);
			alert.setTitle("Add new song to your library");
			alert.setHeaderText("You're about to ADD this song to your library");
			alert.setContentText("Do you want to continue?");
				
			if (alert.showAndWait().get() == ButtonType.OK) {
				
				listview.getItems().add(key);
				FXCollections.sort(listview.getItems(), String.CASE_INSENSITIVE_ORDER);
				ArrayList<String> newsong = new ArrayList<String>();
				newsong.add(songs);
				newsong.add(artists);
				newsong.add(albums);
				newsong.add(years);
				obsmap.put(key, newsong);
				display.setText("Display");
				listview.getSelectionModel().select(key);
			}
		}
		else
		{
			Alert alerts = new Alert(AlertType.INFORMATION);
			alerts.initOwner(stage);
			alerts.setTitle("Error");
			alerts.setHeaderText("You must fix these errors if you want to add a song");
			alerts.setContentText(exceptions);
			alerts.showAndWait();
			
		}
	
	
	}
	public void edit(ActionEvent e)
	{
		if(listview.getSelectionModel().getSelectedItems().size() != 0)
		{
			editingmode = false;
			editing(listview.getSelectionModel().getSelectedItem());
		}
		else
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.initOwner(stage);
			alert.setTitle("Error");
			alert.setHeaderText("You are trying to edit without a song being in your library");
			alert.setContentText("It is only possible to edit if there is at least one song in your library");
			alert.showAndWait();
		}
		
		
		
		
	}
	public void editing(String key)
	{
		songname.setFocusTraversable(false);
		artist.setFocusTraversable(false);
		album.setFocusTraversable(false);
		year.setFocusTraversable(false);
		songname1.setVisible(true);
		songname1.setFocusTraversable(true);
		artist1.setVisible(true);
		album1.setVisible(true);
		year1.setVisible(true);
		confirm.setVisible(true);
		deletebutton.setVisible(false);
		editbutton.setVisible(false);
		songlabel.setVisible(true);
		artistlabel.setVisible(true);
		albumlabel.setVisible(true);
		yearlabel.setVisible(true);
		
		display.setText("Editing Mode");
		
		ArrayList<String> temp = obsmap.get(key);
		if (temp != null)
		{
			songname1.setText(temp.get(0));
			artist1.setText(temp.get(1));
			album1.setText(temp.get(2));
			year1.setText(temp.get(3));
			
			
		}
		
	}
	public void confirmEdit(ActionEvent e)
	{
		String songs = songname1.getText();
		String artists = artist1.getText();
		String albums = album1.getText();
		String years = year1.getText();
		String key = songs + " by " + artists;
		
		String exceptions = "Errors: ";
		
		if(songs.equals("") || artists.equals(""))
		{
			exceptions = exceptions + "\n" + "Must include both song and artist, ";
		}
		if(!years.equals(""))
		{
			try {  
			    int i = Integer.parseInt(years);
			    if(i <= 0)
			    {
			    	exceptions = exceptions + "\n" + "Must be a year > 0";
			    }
	
			  } catch(NumberFormatException a){  
			     exceptions = exceptions + "\n" + "Must type number for year";
			  }  
		}
		for(String keys: obsmap.keySet())
		{
			if(key.compareToIgnoreCase(keys) == 0)
			{
				if(obsmap.get(keys).get(2).equals(albums) && obsmap.get(keys).get(3).equals(years) && key.equals(listview.getSelectionModel().getSelectedItem()))
				{
					exceptions = exceptions + "\n" + "You have made no edits to this song";
					break;
				}
				if(!keys.equals(listview.getSelectionModel().getSelectedItem()))
				{
					exceptions = exceptions + "\n" + "This song alreadys exists with the same artist";
					break;
				}
			}
		}
		
	
			
		if(exceptions.equals("Errors: "))
		{
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.initOwner(stage);
			alert.setTitle("Edit this song");
			alert.setHeaderText("You're about to EDIT this song in your library");
			alert.setContentText("Do you want to continue?, Cancelling will take you out of Editing mode");
				
			if (alert.showAndWait().get() == ButtonType.OK) {
				
				String original = listview.getSelectionModel().getSelectedItem();
				int selectedIndex = listview.getSelectionModel().getSelectedIndex();
				
				listview.getItems().set(selectedIndex, key);
				FXCollections.sort(listview.getItems(), String.CASE_INSENSITIVE_ORDER);
				ArrayList<String> newsong = new ArrayList<String>();
				newsong.add(songs);
				newsong.add(artists);
				newsong.add(albums);
				newsong.add(years);
				obsmap.remove(original);
				obsmap.put(key, newsong);
				listview.getSelectionModel().select(key);
				
				
				
			}
			editingmode = true;
			displayInformation(key);
			songname1.setVisible(false);
			artist1.setVisible(false);
			album1.setVisible(false);
			year1.setVisible(false);
			confirm.setVisible(false);
			songlabel.setVisible(false);
			artistlabel.setVisible(false);
			albumlabel.setVisible(false);
			yearlabel.setVisible(false);
			deletebutton.setVisible(true);
			editbutton.setVisible(true);
			display.setText("Display");
			
			
			
		
			
			
		}
		else
		{
			Alert exceptionalert = new Alert(AlertType.CONFIRMATION);
			exceptionalert.initOwner(stage);
			exceptionalert.setTitle("Error");
			exceptionalert.setHeaderText("You must fix these errors if you want to EDIT this song, Cancelling will take you out of Editing mode");
			exceptionalert.setContentText(exceptions);
			
			if(exceptionalert.showAndWait().get() == ButtonType.CANCEL)
			{
				songname1.setVisible(false);
				artist1.setVisible(false);
				album1.setVisible(false);
				year1.setVisible(false);
				confirm.setVisible(false);
				songlabel.setVisible(false);
				artistlabel.setVisible(false);
				albumlabel.setVisible(false);
				yearlabel.setVisible(false);
				deletebutton.setVisible(true);
				editbutton.setVisible(true);
				display.setText("Display");
				editingmode = true;
			}
			
		}
		
	}
	
	
	public void readfromFile(Map<String, ArrayList<String>> map) throws IOException
	{
	    BufferedReader reader = new BufferedReader(new FileReader(file));

	    String curLine;
	     
	    while ((curLine = reader.readLine()) != null){
	        curLine = curLine.trim();
			String[] data = curLine.split("@", -2);
			ArrayList<String> datalist = new ArrayList<String>();
			Collections.addAll(datalist, data);
	        String key = data[0] + " by "+ data[1];
	        map.put(key, datalist);
	            
	    }
	    
	    reader.close();
	    displaystartListView();
		
	}
	public void displaystartListView()
	{
		obsmap = FXCollections.observableMap(map);
		obsList = FXCollections.observableArrayList(obsmap.keySet());
		FXCollections.sort(obsList, String.CASE_INSENSITIVE_ORDER);
		if(obsList.size() == 0)
		{
			display.setText("There are no songs in your library");
			song_name.setText("\n");
			artist_name.setText("\n");
			album_name.setText("\n");
			year_released.setText("\n");
			
		}
		listview.setItems(obsList);
		
	    
	}
	
	
	
}
