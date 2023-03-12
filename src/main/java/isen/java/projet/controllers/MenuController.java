package isen.java.projet.controllers;


import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


import isen.java.projet.App;
import isen.java.projet.daos.PersonDao;
import isen.java.projet.object.Person;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;

public class MenuController {
	
	private Person selectedPerson;
	
	//TABLE RELATED
	
	@FXML
	private TableView<Person> table;
	
    @FXML
    private TableColumn<Person, String> firstNameColumn;

    @FXML
    private TableColumn<Person, String> lastNameColumn;
    
    //BUTTONS
    
//    @FXML
//    private Button listDatabaseButton;
    
    @FXML
    private Button addPersonButton;
    
    @FXML
    private Button exitButton;
    
    @FXML
    private Button editButton;

    
    @FXML
    private Text currentSelection;
    
    @FXML
    private Text exportedText;
    
    private FadeTransition fade = new FadeTransition();
    
    private PersonDao personDao = new PersonDao();
    
	//get the database as a list and set it to the tableview
    //set each columns to have value according to a property from the object
    @FXML	
    private void listDatabase() throws IOException {
    	ObservableList<Person> list = this.getPeople();
    	table.setItems(list);
    	firstNameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("firstname"));
    	lastNameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("lastname"));
    }
    
    //When add person is clicked scene is changed to add person scene"
    
    @FXML
    private void addPerson() throws IOException {
    	 App.setRoot("addpersonscene");
    }
  
    //When edit person is clicked scene, load the detailsscene and set data (selectedPerson) of details controller
    
    @FXML
    void editPerson(ActionEvent event) throws Exception {
    	
    	//Load details scene
    	FXMLLoader loader = new FXMLLoader(App.class.getResource("detailspersonscene.fxml"));
    	Parent root = loader.load();
    	
    	//Create DetailsPersonScene controller to manipulate the scene before it is shown
    	DetailsPersonSceneController controller = loader.getController();
    	//set data inside controller
    	controller.setSelectedPerson(selectedPerson);
    	controller.setPersonID(selectedPerson.getId());
    	//show the selection in the table
    	controller.listSelection();
    	//set selected person to null and disable edit button
    	this.selectedPerson = null;
    	editButton.setDisable(true);
    	//change scene to detailspersonscene
    	App.setRoot(root);
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
						String.format("%s-%s.vcf", person.getLastname().toLowerCase(), person.getFirstname().toLowerCase()),
						person.generatesVCard());
				exportedText.setText("Exported in contact_backup folder!");
				exportedText.setStyle("-fx-text-fill: #1ba12f;");
				playFade();
			} catch (IOException e) {
				exportedText.setText("Error during export.");
				exportedText.setStyle("-fx-text-fill: #FF0000;");
				playFade();
				e.printStackTrace();
			}
    		
    	});
    	
    }
    
    //when a row in the tableview is clicked return the selected row
    @FXML
    void returnPerson(MouseEvent event) {
    	//set selection mode to only be able to select 1 row at a time
    	TableViewSelectionModel<Person> selectionModel = table.getSelectionModel();
    	selectionModel.setSelectionMode(SelectionMode.SINGLE);
    	ObservableList<Person> selectedItems = selectionModel.getSelectedItems();
    	
    	//set selected person as selected row
    	if(!selectedItems.isEmpty()) {
    		selectedPerson = selectedItems.get(0);
        	
        	//set current selection text and enable update and delete button
        	this.currentSelection.setText(String.format("Current Selection: %s %s", selectedPerson.getFirstname(), selectedPerson.getLastname()));
        	editButton.setDisable(false);
    	}
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
    
    @FXML
    void exit(ActionEvent event) {
    	Platform.exit();
    }
    
    //animation properties for the fade
    void playFade() {
    	this.exportedText.setVisible(true);
    	//set the node that the fade will be related to
		this.fade.setNode(exportedText);
		
		//fade properties
		fade.setDuration(Duration.millis(2000));
		fade.setFromValue(10);  
        fade.setToValue(0);
		this.fade.play();
		
    }



}
