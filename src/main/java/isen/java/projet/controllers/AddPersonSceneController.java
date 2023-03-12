package isen.java.projet.controllers;

import java.io.IOException;
import isen.java.projet.App;
import isen.java.projet.daos.PersonDao;
import isen.java.projet.object.Person;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.fxml.FXML;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.scene.control.DatePicker;

public class AddPersonSceneController {
	
	@FXML
    private Button addPerson;

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
    private Button menu;

    @FXML
    private TextField nickname;

    @FXML
    private TextField phonenumber;
    
    @FXML
    private Text personAdded;
    
    @FXML
    private Text errorText;
    
    private FadeTransition fade = new FadeTransition();
	
	/**
	 * When add person is clicked create a new object person, set its properties according to the Textfields, and call add person to the database.
	 * */
	@FXML
    void addData(ActionEvent event) {
		errorText.setVisible(false);
		//create person
		PersonDao personDao = new PersonDao();
		Person person = new Person();
		person.setFirstname(this.firstname.getText());
		person.setLastname(this.lastname.getText());
		person.setAddress(this.address.getText());
		person.setEmailAddress(this.email.getText());
		person.setBirthDate(this.birthdate.getValue());
		person.setPhoneNumber(this.phonenumber.getText());
		person.setNickname(this.nickname.getText());
		
		if (person.getFirstname().equals("") || person.getFirstname() == null) {
			errorText.setText("Please enter a firstname");
			errorText.setVisible(true);
		}
		else if (person.getLastname().equals("") || person.getLastname() == null) {
			errorText.setText("Please enter a lastname");
			errorText.setVisible(true);
		}
		else if (person.getBirthDate() == null) {
			errorText.setText("Please enter a birthdate");
			errorText.setVisible(true);
		}
		else {
			//add person
			personDao.addPerson(person);
			//fade in and out a text to show to user that a person has been added
			playFade();
		}
    }

	/**
	 * Return to the menu when menu button is clicked.
	 * */
    @FXML
    void returnToMenu(ActionEvent event) throws IOException {
    	App.setRoot("menu");
    }
    
    /**
     * Plays a animation for the personAdded text
     * */
    void playFade() {
    	this.personAdded.setVisible(true);
    	//set the node that the fade will be related to
		this.fade.setNode(personAdded);
		
		//fade properties
		fade.setDuration(Duration.millis(2000));
		fade.setFromValue(10);  
        fade.setToValue(0);
		this.fade.play();
		
    }
	
}