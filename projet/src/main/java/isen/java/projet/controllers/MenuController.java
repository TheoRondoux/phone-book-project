package isen.java.projet.controllers;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import static org.junit.Assert.*;


import isen.java.projet.App;
import isen.java.projet.daos.PersonDao;
import isen.java.projet.object.Person;

import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;

public class MenuController {
	
	private Person selectedPerson;
	@FXML
	private TableView<Person> table;
	
    @FXML
    private TableColumn<Person, String> firstNameColumn;

    @FXML
    private TableColumn<Person, String> lastNameColumn;
    
    @FXML
    private TableColumn<Person, LocalDate> DateofBirthColumn;
    
    @FXML
    private TableColumn<Person, String> emailColumn;
    
    @FXML
    private TableColumn<Person, String> phoneNumberColumn;
    
    @FXML
    private Button listDatabaseButton;
    
    @FXML
    private Button addPersonButton;
    
    @FXML
    private Button updateEntryButton;
    
    @FXML
    private Button deleteEntryButton;
    
    @FXML
    private Text currentSelection;
    
    private PersonDao personDao = new PersonDao();
    
	//get the database as a list and set it to the tableview
    //set each columns to have value according to a property from the object
    @FXML	
    private void listDatabase() throws IOException {
    	ObservableList<Person> list = this.getPeople();
    	table.setItems(list);
    	firstNameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("firstname"));
    	lastNameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("lastname"));
    	DateofBirthColumn.setCellValueFactory(new PropertyValueFactory<Person, LocalDate>("birthDate"));
    	phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("phoneNumber"));
    	emailColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("emailAddress"));
    }
    
    //When add person is clicked scene is changed to add person scene"
    
    @FXML
    private void addPerson() throws IOException {
    	 App.setRoot("addpersonscene");
    }
    
    //when update entry is clicked, load update data scene and set properties of the udpatedatascene
    @FXML
    private void updateEntry(ActionEvent event) throws IOException {
    	//load scene
    	FXMLLoader loader = new FXMLLoader(App.class.getResource("updatedatascene.fxml"));
    	Parent root = loader.load();
    	
    	//create an instance of the update scene controller and use it to call the set id and set TextField of the update data scene
    	UpdatePersonSceneController controller = loader.getController();
    	controller.setId(selectedPerson.getId());
    	controller.setTextField(selectedPerson.getFirstname(), selectedPerson.getLastname(), 
    			selectedPerson.getNickname(), selectedPerson.getPhoneNumber(), selectedPerson.getAddress(), selectedPerson.getEmailAddress(), selectedPerson.getBirthDate());
    	
    	//set selected person to null, and disable the delete and update button
    	this.selectedPerson = null;
    	deleteEntryButton.setDisable(true);
    	updateEntryButton.setDisable(true);
    	
    	//change the scene
    	App.setRoot(root);
    }
    
    //when delete is clicked, create a popup window for user to confirm deletion, delete the current selection
    @FXML
    private void deleteEntry() throws IOException {
    	//create alert
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Delete entry");
    	alert.setHeaderText("This will permanently remove the user from the database are you sure?");
    	
    	//if alert answer is ok delete selection and reload the table view
    	if(alert.showAndWait().get() == ButtonType.OK) {
    		personDao.deletePerson(selectedPerson.getId());
    		this.listDatabase();
    	}
    	
    	//set selection to null, disable delete/update button, and set selection text
    	this.selectedPerson = null;
    	deleteEntryButton.setDisable(true);
    	updateEntryButton.setDisable(true);
    	this.currentSelection.setText("Current Selection: nothing");
    }
    
    //export a VCF form of each contacts
    @FXML
    private void createBackup() throws IOException {
    	
    	//get all entries as a list
    	List<Person> list = personDao.listPersons();
    	
    	//get cwd and set path to cwd/contact_backup
    	Path currentWorkingDir = Paths.get("").toAbsolutePath();
    	File newDirectory = new File(currentWorkingDir.toString(), "contact_backup");
    	
    	//if the backupfolder exists delete its content
    	if(!newDirectory.exists()) { newDirectory.mkdir(); }
    	else { Files.walk(currentWorkingDir.resolve("contact_backup")).map(Path::toFile).forEach(File::delete); newDirectory.mkdir();}
    	
    	//for each object in the list create a new vcf file (lastname-firstname.vcf) and write the person's information
    	list.forEach((person) -> {
    		try {
				this.writeBackup(newDirectory.getAbsolutePath(), 
						String.format("%s-%s.vcf", person.getLastname(), person.getFirstname()),
						person.generatesVCard());
			} catch (IOException e) {
				e.printStackTrace();
			}
    		
    	});
    	
    }
    
    //when a row in the tableview is clicked return the selected row
    @FXML
    void returnPerson(MouseEvent event) {
    	//set selection mode to only be able to select 1 row at a time
    	TableViewSelectionModel selectionModel = table.getSelectionModel();
    	selectionModel.setSelectionMode(SelectionMode.SINGLE);
    	ObservableList<Person> selectedItems = selectionModel.getSelectedItems();
    	
    	//set selected person as selected row
    	selectedPerson = selectedItems.get(0);
    	
    	//set current selection text and enable update and delete button
    	this.currentSelection.setText(String.format("Current Selection: %s %s", selectedPerson.getFirstname(), selectedPerson.getLastname()));
    	updateEntryButton.setDisable(false);
    	deleteEntryButton.setDisable(false);
    }
    
    //transform a list of people into an observable list for javafx
    private ObservableList<Person> getPeople() throws IOException{
        ObservableList<Person> list = FXCollections.observableArrayList(personDao.listPersons());
    	return list;
    }
    
    //on start list the database
    @FXML
    public void initialize() throws Exception {
    	this.listDatabase();
    }
    
    //function to writeBackup
    public void writeBackup(String backupPath, String filename, String vCard) throws IOException {
    	try (BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(backupPath, filename), StandardCharsets.UTF_8)){
    		bufferedWriter.write(vCard);
        	bufferedWriter.flush();
    	}
    	
    }



}
