package isen.java.projet.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import isen.java.projet.App;
import isen.java.projet.daos.PersonDao;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import isen.java.projet.object.Person;

public class DetailsPersonSceneController {
	
	//PERSON DAO
	
	private PersonDao personDao = new PersonDao();
	
	//TABLE RELATED
	
	private Person selectedPerson;
	private int personID;
	private List<Person> personsList = new ArrayList<>();
	
	@FXML
    private TableColumn<Person, LocalDate> DateofBirthColumn;

    @FXML
    private TableColumn<Person, String> emailColumn;

    @FXML
    private TableColumn<Person, String> firstNameColumn;

    @FXML
    private TableColumn<Person, String> lastNameColumn;

    @FXML
    private TableColumn<Person, String> phoneNumberColumn;
    
    @FXML
    private TableColumn<Person, String> addressColumn;

    @FXML
    private TableView<Person> table;

    
    //BUTTONS 
    @FXML
    private Button updateEntryButton;
    
    @FXML
    private Button menuButton;
    
    @FXML
    private Button deleteEntryButton;
    
    public void listSelection() throws IOException {
    	personsList.add(selectedPerson);
    	ObservableList<Person> list = FXCollections.observableArrayList(personsList);
    	table.setItems(list);
    	firstNameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("firstname"));
    	lastNameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("lastname"));
    	DateofBirthColumn.setCellValueFactory(new PropertyValueFactory<Person, LocalDate>("birthDate"));
    	phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("phoneNumber"));
    	emailColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("emailAddress"));
    	addressColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("address"));
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
