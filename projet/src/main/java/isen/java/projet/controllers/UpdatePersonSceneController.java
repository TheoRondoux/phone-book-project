package isen.java.projet.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;

import isen.java.projet.App;
import isen.java.projet.daos.PersonDao;
import isen.java.projet.object.Person;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class UpdatePersonSceneController {
	
		private int id;
	
		@FXML
		private TextField address;

		@FXML
		private DatePicker birthdate;

		@FXML
		private TextField email;

		@FXML
		private TextField firstname;

		@FXML
		private TextField lastname;
		
		@FXML
	    private TextField nickname;

	    @FXML
	    private TextField phonenumber;

	    @FXML
	    private Button menu;

	    @FXML
	    private Button updateData;
	    
	    @FXML
	    private Text personUpdated;
	    
	    private FadeTransition fade = new FadeTransition();
	    
	    //create new person object, set its values to the textfields and call it editperson from personDao
	    @FXML
	    void updateData(ActionEvent event) {
	    	PersonDao personDao = new PersonDao();
	    	Person person = new Person();
	    	person.setId(this.id);
			person.setFirstname(this.firstname.getText());
			person.setLastname(this.lastname.getText());
			person.setAddress(this.address.getText());
			person.setEmailAddress(this.email.getText());
			person.setBirthDate(this.birthdate.getValue());
			person.setPhoneNumber(this.phonenumber.getText());
			person.setNickname(this.nickname.getText());
	    	personDao.editPerson(person);
	    	playFade();
	    	
	    }

	    @FXML
	    void returnToMenu(ActionEvent event) throws Exception {
	    	App.setRoot("menu");
	    }
	    
	    //set the textfields values, used from the MenuController to pass the information of selected person
	    void setTextField(String firstname, String lastname, String nickname, String phoneNumber, String address, String email, LocalDate birthDate) {
	    	this.firstname.setText(firstname);
	    	this.lastname.setText(lastname);
	    	this.nickname.setText(nickname);
	    	this.phonenumber.setText(phoneNumber);
	    	this.address.setText(address);
	    	this.email.setText(email);
	    	this.birthdate.setValue(birthDate);
	    }
	    
	    void setId(int id) {
	    	this.id = id;
	    }
	    
	    void playFade() {
	    	this.personUpdated.setVisible(true);
			this.fade.setNode(personUpdated);
			fade.setDuration(Duration.millis(2000));
			fade.setFromValue(10);  
	        fade.setToValue(0.1);
			this.fade.play();
			
	    }

	
}
