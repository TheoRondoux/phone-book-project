package m1s2.project.java.app;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import m1s2.project.java.daos.DataSourceFactory;
import m1s2.project.java.daos.PersonDao;
import m1s2.project.java.entities.Person;

public class Application {
	
	public static void initDb() {
		try(Connection connection = DataSourceFactory.getDataSource().getConnection()){
			try(Statement statement = connection.createStatement()){
				String sqlQuery = "CREATE TABLE IF NOT EXISTS person (\r\n"
								+ "  idperson INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\r\n" + "  lastname VARCHAR(45) NOT NULL,\r\n"
								+ "  firstname VARCHAR(45) NOT NULL,\r\n" + "  nickname VARCHAR(45) NOT NULL,\r\n" + "  phone_number VARCHAR(15) NULL,\r\n"
								+ "  address VARCHAR(200) NULL,\r\n" + "  email_address VARCHAR(150) NULL,\r\n"
								+ "  birth_date DATETIME NULL);";
				statement.execute(sqlQuery);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Waits for the user to enter a character.
	 * 
	 * @return the character if the input length is greater than 1, null in any other case
	 * */
	public static Character getOption() {
		System.out.print("-> ");
		Scanner keyboard = new Scanner(System.in);
		String input = keyboard.next();
		if (input.length() == 1) {
			return input.charAt(0);
		}
		else {
			return null;
		}	
	}
	
	/**
	 * Generates and displays the main menu for the phone book.
	 * */
	public static void showMainMenu() {
		Character choice = null;
		String options = "//////////////////////\n//    Phonebook     //\n//////////////////////\n"
					   + "\n[L]ist all contacts"
					   + "\n[A]dd a new person"
					   + "\n[E]xit";
		System.out.println(options);
		while (choice == null || ( !choice.equals('E') && !choice.equals('L') && !choice.equals('A') ) ) {
			if (choice != null) {
				System.out.println("Please enter a valid option");
			}
			choice = getOption();
		}
		switch(choice) {
			case 'L': 
				showContactList(new PersonDao(),5);
				break;
			case 'A':
				addContact(new PersonDao());
				break;
			case 'E':
				break;
		}
	}
	/**
	 * Generates the "List of contacts" tab for the phone book.
	 * 
	 * @param personDao is a DAO to retrieve the information of the person database table
	 * @param limitPerPage is the number of contacts to display per page
	 * */
	public static void showContactList(PersonDao personDao, Integer limitPerPage)
	{
		List<Person> contacts = personDao.listPersons();
		Integer pageCounter = 0;
		Integer numberOfPages = (int) Math.ceil(contacts.size()/(double)limitPerPage);
		Character choice = null;
		String details = "\n//////////////////////\n//     Contacts     //\n//////////////////////\n"
					   + "Page " + (pageCounter+1) + "/" + numberOfPages;		
		System.out.println(details);
		List<Person> contactsOnPage = displayContactListPage(contacts, limitPerPage, pageCounter);;
		String options = generatePageOptions(numberOfPages, pageCounter);
		while (choice == null || choice != 'B') {
			while( choice == null || ( choice != 'B' && choice != '>' && choice != '<'  && choice < '1' && choice > (char)contactsOnPage.size()) ) {
				if (choice != null ) {
					System.out.println("\nPlease enter a valid option or choose a contact");
					contactsOnPage = displayContactListPage(contacts, limitPerPage, pageCounter);
				}
				System.out.println(options);
				choice = getOption();
			}
			
			switch(choice) {
				case '<':
					if (numberOfPages > 1 && pageCounter == (numberOfPages-1) || numberOfPages > 1 && pageCounter > 0 && pageCounter < (numberOfPages-1)) {
						pageCounter--;
						System.out.println("\nPage " + (pageCounter+1) + "/" + numberOfPages);
						contactsOnPage = displayContactListPage(contacts, limitPerPage, pageCounter);
						options = generatePageOptions(numberOfPages, pageCounter);
					}
					break;
				case '>':
					if (numberOfPages > 1 && pageCounter == 0 || numberOfPages > 1 && pageCounter > 0 && pageCounter < (numberOfPages-1) ) {
						pageCounter++;
						System.out.println("\nPage " + (pageCounter+1) + "/" + numberOfPages);
						contactsOnPage = displayContactListPage(contacts, limitPerPage, pageCounter);
						options = generatePageOptions(numberOfPages, pageCounter);
					}
					break;
				case 'B':
					break;
				default:
					try{
						showInfoContactMenu(contactsOnPage.get((int)choice-49), personDao);
						contacts = personDao.listPersons();
						System.out.println("\n//////////////////////\n//     Contacts     //\n//////////////////////\n" + "Page " + (pageCounter+1) + "/" + numberOfPages);
						contactsOnPage = displayContactListPage(contacts, limitPerPage, pageCounter);
					}
					catch (IndexOutOfBoundsException e) {
						choice = '0';
					}
					break;
			}
			if (choice != 'B' && choice != '0') {
				choice = null;
			}
		}
		showMainMenu();
	}
	/**
	 * Displays a certain amount of persons
	 * 
	 * @param contacts is a list of Person
	 * @param limitPerPage is the number of contacts to display on each page
	 * @param pageCounter is the number of the current page
	 * */
	public static List<Person> displayContactListPage(List<Person> contacts, Integer limitPerPage, Integer pageCounter) {
		List<Person> contactsOnPage = new ArrayList<>();
		int counter = 0;
		System.out.println("----------------------");
		if (contacts.size() <= limitPerPage) {
			for (int i=0; i < contacts.size(); i++) {
				counter++;
				System.out.println(counter + " - " + contacts.get(i).getLastname() + " " + contacts.get(i).getFirstname());
				contactsOnPage.add(contacts.get(i));	
			}
		}
		else {
			if ( contacts.size() <= (pageCounter * limitPerPage + limitPerPage) ) {
				for (int i = (pageCounter * limitPerPage); i < contacts.size(); i++) {
					counter++;
					System.out.println(counter + " - " + contacts.get(i).getLastname() + " " + contacts.get(i).getFirstname());
					contactsOnPage.add(contacts.get(i));
				}
			}
			else {
				for (int i = (pageCounter * limitPerPage); i < (pageCounter * limitPerPage + limitPerPage); i++) {
					counter++;
					System.out.println(counter + " - " + contacts.get(i).getLastname() + " " + contacts.get(i).getFirstname());
					contactsOnPage.add(contacts.get(i));
				}
			}
		}
		System.out.println("----------------------");
		return contactsOnPage;
	}
	
	/**
	 * Generates the right options for the List contacts tab.
	 * 
	 * @param numberOfPages is the number of the pages needed for all the contacts
	 * @param pageCounter is the number of the actual page
	 * 
	 * @return a string with the options that the user can choose
	 * */
	public static String generatePageOptions(Integer numberOfPages, Integer pageCounter) {
		String options = "\n";
		if (numberOfPages > 1 && pageCounter == 0) {
			options += "Next page [>]\n";
		}
		else if (numberOfPages > 1 && pageCounter == (numberOfPages-1) ) {
			options += "[<] Previous page\n";
		}
		else if (numberOfPages > 1 && pageCounter > 0 && pageCounter < (numberOfPages-1) ) {
			options += "[<] Previous page | Next page [>]\n";
		}
		options += "[B]ack to main menu";
		
		return options;
	}
	
	/**
	 * Checks if a string contains the '@' char
	 * 
	 * @param input is the string to check
	 * @return true if the char was found, false in the other case.
	 * */
	public static boolean checkEmailInput(String input) {
		boolean foundChar = false;
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) == '@') {
				foundChar = true;
			}
		}
		
		return foundChar;
	}
	
	/**
	 * Asks the user to enter something with his keyboard
	 * 
	 * @param parameter is a hint for the input
	 * @return user's input
	 * */
	public static String askForStringInput(String parameter) {
		System.out.print(parameter+": ");
		Scanner keyboard = new Scanner(System.in);
		String input = keyboard.nextLine();
		
		return input;
	}
	
	/**
	 * Asks for the day, the month or the year, depending on the parameter.
	 * 
	 * @param message is the message displayed to help the user to enter something
	 * @param parameter is used to define if the input is the day, the month or the year
	 * @return an integer corresponding to the wanted value.
	 * */
	public static Integer askForDateInt(String message, String parameter) {
		Scanner keyboard = new Scanner(System.in);
		Integer input = null;
		switch(parameter) {
			case "month":
				while (input == null || (input < 1 || input > 12)) {
					if (input != null) {
						System.out.print("Please enter a number between 1 and 12: ");
					} else {
						System.out.print(message+": ");
					}
					try {
						input = keyboard.nextInt();
						keyboard.nextLine();
					}
					catch (InputMismatchException e) {
						keyboard.nextLine();
						input = 0;
					}
				}
				break;
			
			case "day":
				while (input == null || (input < 1 || input > 31)) {
					if (input != null) {
						System.out.print("Please enter a number between 1 and 31: ");
					} else {
						System.out.print(message+": ");
					}
					try {
						input = keyboard.nextInt();
						keyboard.nextLine();
					}
					catch (InputMismatchException e) {
						keyboard.nextLine();
						input = 0;
					}
				}
				break;
				
			case "year":
				while (input == null || (input < 1900 || input > Year.now().getValue())) {
					if (input != null) {
						System.out.print("Please enter a number between 1900 and "+Year.now().getValue()+": ");
					} else {
						System.out.print(message+": ");
					}
					try {
						input = keyboard.nextInt();
						keyboard.nextLine();
					}
					catch (InputMismatchException e) {
						keyboard.nextLine();
						input = 0;
					}
				}
				
				break;
				
			default: 
				while (input == null) {
					System.out.print(message+": ");
					try {
						input = keyboard.nextInt();
						keyboard.nextLine();
					}
					catch (InputMismatchException e) {
						input = null;
						keyboard.nextLine();
					}
				}
				break;
		}
		//keyboard.close();
		return input;
	}
	
	/**
	 * Generates the Add contact tab.
	 * 
	 * @param personDao is a DAO used to add a person to the database
	 * */
	public static void addContact(PersonDao personDao) {
		System.out.println("\n//////////////////////\n//    Add Contact   //\n//////////////////////\n");
		Character choice = null;
		Person person = new Person();
		person.setLastname(askForStringInput("Lastname"));
		person.setFirstname(askForStringInput("Firstname"));
		person.setNickname(askForStringInput("Nickname"));
		person.setPhoneNumber(askForStringInput("Phone number"));
		person.setAddress(askForStringInput("Address"));
		person.setEmailAddress(askForStringInput("Email"));
		Integer birthDay = askForDateInt("Day of birth (between 1 and 31)", "day");
		Integer birthMonth = askForDateInt("Month of birth (between 1 and 12)", "month");
		Integer birthYear = askForDateInt("Year of birth (between 1900 and "+Year.now().getValue()+")", "year");
		person.setBirthDate(LocalDate.of(birthYear, birthMonth, birthDay));
		
		Person result = personDao.addPerson(person);
		if (result == null) {
			System.out.println("Something went wrong while adding the new contact, please try again.");
		}
		else {
			System.out.println("Successfully added "+person.getFirstname()+" "+person.getLastname()+"!");
		}
		System.out.println("[B]ack to main menu");
		while (choice == null || choice != 'B') {
			Scanner keyboard = new Scanner(System.in);
			if (choice != null) {
				System.out.println("Please enter a valid option");
			}
			choice = getOption();
		}
		showMainMenu();
		
	}
	/**
	 * Displays all the information about a contact
	 * 
	 * @param person is the contact that we want to know more about
	 * @param personDao is a DAO
	 * */
	public static void showInfoContactMenu(Person person, PersonDao personDao) {
		System.out.println("\n//////////////////////\n//     Details      //\n//////////////////////");
		person.displayInfos();
		System.out.println("[E]dit\n[B]ack to contacts list");
		Character choice = null;
		while ( choice == null || (choice != 'B' && choice != 'E') ) {
			if (choice != null) {
				System.out.println("Please select a valid option");
			}
			choice = getOption();
		}
		
		switch(choice) {
			case 'E':
				showEditContactMenu(person, personDao);
				break;
			case 'B':
				break;
		}
	}
	
	/**
	 * Shows the edition options for a contact and allows the user to modify the elements
	 * 
	 * @param person is the contact that we want to modify his information
	 * @param personDao is a DAO
	 * */
	public static void showEditContactMenu(Person person, PersonDao personDao) {
		Character choice = null;
		System.out.println("\n//////////////////////\n//     Edition      //\n//////////////////////\n" + person.getFirstname() + " " + person.getLastname() + "\n");
		while (choice == null || ( choice != 'B' && choice != 'R' )) {
			System.out.println("Choose an element to modify:\n[F]irstname\n[L]astname\n[N]ickname\nBirth [D]ate\n[P]hone number\n[A]ddress\n[E]mail address\n[R]emove person\n\nOr go [B]ack to the list");
			while (choice == null || (choice != 'F' && choice != 'L' && choice != 'N' && choice != 'D' && choice != 'P' && choice != 'A' && choice != 'E' && choice != 'B' && choice != 'R') ) {
				if (choice != null) {
					System.out.println("Please enter a valid option.");
				}
				choice = getOption();
			}
			
			switch(choice){
				case 'F':
					person.setFirstname(askForStringInput("Firstname"));
					break;
				case 'L': 
					person.setLastname(askForStringInput("Lastname"));
					break;
				case 'N': 
					person.setNickname(askForStringInput("Nickname"));
					break;
				case 'D': 
					person.setBirthDate(LocalDate.of(askForDateInt("Year of birth between (1900 and " + Year.now().getValue() + ")", "year"), askForDateInt("Month of birth (between 1 and 12)", "month"), askForDateInt("Day of birth (between 1 and 31)", "day")));
					break;
				case 'P': 
					person.setPhoneNumber(askForStringInput("Phone number"));
					break;
				case 'A': 
					person.setAddress(askForStringInput("Address"));
					break;
				case 'E': 
					person.setEmailAddress(askForStringInput("Email"));
					break;
				case 'R':
					confirmPersonDeletion(person, personDao);
					break;
				case 'B':
					break;
			}
			if (choice != 'B' && choice != 'R') {
				personDao.editPerson(person);
				checkContactEdition(person, personDao);
				person.displayInfos();
				choice = null;
			}
		}
	}
	
	/**
	 * Checks if the modification of the contact has been made on the database
	 * 
	 * @param personInfo is the contact edited locally
	 * @param personDao is a DAO
	 * */
	public static void checkContactEdition(Person personInfo, PersonDao personDao) {
		List<Person> tempContacts = personDao.listPersons();
		for (Person contact : tempContacts) {
			if (contact.getId().equals(personInfo.getId()) && contact.getAddress().equals(personInfo.getAddress()) && contact.getBirthDate().equals(personInfo.getBirthDate()) 
				&& contact.getEmailAddress().equals(personInfo.getEmailAddress()) && contact.getFirstname().equals(personInfo.getFirstname()) && contact.getLastname().equals(personInfo.getLastname()) 
				&& contact.getNickname().equals(personInfo.getNickname()) && contact.getPhoneNumber().equals(personInfo.getPhoneNumber()) ){
				System.out.println("\nContact successfully edited!\n");
				return;
			}
		}
		System.out.println("\nSomething went wrong, please try again.\n");
	}
	
	/**
	 * Asks the user to confirm the deletion of a contact. If yes, deletes the contact and checks if the contact has been deleted in the database.
	 * 
	 * @param person is the contact to delete
	 * @param personDao is a DAO
	 * @return true if the person has been deleted, false in any other case.
	 * */
	public static boolean confirmPersonDeletion(Person person, PersonDao personDao) {
		Character choice = null;
		System.out.println("Are you sure you want to delete "+person.getFirstname()+" "+person.getLastname()+"? This operation is irreversible.\n[N]o\n[Y]es");
		while (choice == null || (choice != 'Y' && choice != 'N')) {
			if (choice != null) {
				System.out.println("Please select a valid option.");
			}
			choice = getOption();
		}
		
		switch(choice) {
			case 'N':
				break;
			case 'Y':
				personDao.deletePerson(person.getId());
				List<Person> tempContacts = personDao.listPersons();
				for (Person tempContact : tempContacts) {
					if (tempContact.getId().equals(person.getId())) {
						System.out.println("Something went wrong while deleting the contact, please try again.");
						return false;
					}
				}
				System.out.println("Successfuly deleted " + person.getFirstname() + " " + person.getLastname()+".");
				return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		
		//Initializing database
		initDb();
		
		//Launching the main menu
		showMainMenu();
		
	}

}
