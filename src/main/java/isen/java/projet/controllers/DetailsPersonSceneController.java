package isen.java.projet.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import isen.java.projet.App;
import isen.java.projet.daos.PersonDao;
import java.io.IOException;
import isen.java.projet.object.Person;

public class DetailsPersonSceneController {
	
	//PERSON DAO
	private PersonDao personDao = new PersonDao();
	
	//Information related to the different texts	
	private Person selectedPerson;
	private int personID;
	
	@FXML
	private Text firstnameLastnameText;
	
	@FXML
	private Text nicknameText;
	
	@FXML
	private Text birthdateText;
	
	@FXML
	private Text addressText;
	
	@FXML
	private Text phoneNumberText;
	
	@FXML
	private Text emailText;

    
    //BUTTONS 
    @FXML
    private Button updateEntryButton;
    
    @FXML
    private Button menuButton;
    
    @FXML
    private Button deleteEntryButton;
    
    @FXML
    public void listSelection() throws IOException {
    	this.firstnameLastnameText.setText(String.format("%s %s", selectedPerson.getFirstname(), selectedPerson.getLastname()));
    	this.birthdateText.setText(String.valueOf(selectedPerson.getBirthDate()));
    	this.nicknameText.setText(selectedPerson.getNickname());
    	this.addressText.setText(selectedPerson.getAddress());
    	this.emailText.setText(selectedPerson.getEmailAddress());
    	this.phoneNumberText.setText(selectedPerson.getPhoneNumber());
    }

    //when delete is clicked, create a popup window for user to confirm deletion, delete the current selection
    @FXML
    void deleteEntry(ActionEvent event) throws Exception {
    	//create alert
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Delete entry");
    	alert.setHeaderText("This will permanently remove the user from the database are you sure?");
    	
    	//if alert answer is ok delete selection and reload the table view
    	if(alert.showAndWait().get() == ButtonType.OK) {
    		personDao.deletePerson(selectedPerson.getId());
    		App.setRoot("menu");
    	}
    	
    	//set selection to null, disable delete/update button, and set selection text
    	
    }
    
    //when update entry is clicked, load update data scene and set properties of the udpatedatascene
    @FXML
    void updateEntry(ActionEvent event) throws Exception {
    	//load scene
    	FXMLLoader loader = new FXMLLoader(App.class.getResource("updatedatascene.fxml"));
    	Parent root = loader.load();
    	
    	//create an instance of the update scene controller and use it to call the set id and set TextField of the update data scene
    	UpdatePersonSceneController controller = loader.getController();
    	controller.setId(selectedPerson.getId());
    	controller.setTextField(selectedPerson.getFirstname(), selectedPerson.getLastname(), 
    			selectedPerson.getNickname(), selectedPerson.getPhoneNumber(), selectedPerson.getAddress(), selectedPerson.getEmailAddress(), selectedPerson.getBirthDate());
    	
    	//set selected person to null, and disable the delete and update button
    	//change the scene
    	App.setRoot(root);
    }
    
    @FXML
    void returnToMenu(ActionEvent event) throws Exception {
    	App.setRoot("menu");
    }
    

	public Person getSelectedPerson() {
		return selectedPerson;
	}

	public void setSelectedPerson(Person selectedPerson) {
		this.selectedPerson = selectedPerson;
	}

	public int getPersonID() {
		return personID;
	}

	public void setPersonID(int personID) {
		this.personID = personID;
	}
    
    
	
}
