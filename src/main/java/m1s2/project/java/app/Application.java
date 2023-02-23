package m1s2.project.java.app;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
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
			System.out.print("-> ");
			Scanner keyboard = new Scanner(System.in);
			String input = keyboard.next();
			if(input.length() == 1) {
				choice = input.charAt(0);
			}
		}
		switch(choice) {
			case 'L': 
				showContactList(new PersonDao(),5);
				break;
			case 'A':
				addContact();
				break;
			case 'E':
				break;
		}
	}
	
	public static void showContactList(PersonDao personDao, Integer limitPerPage)
	{
		List<Person> contacts = personDao.listPersons();
		Integer pageCounter = 0;
		Integer numberOfPages = contacts.size()/limitPerPage + 1;
		Character choice = null;
		String details = "\n//////////////////////\n//     Contacts     //\n//////////////////////\n"
					   + "Page " + (pageCounter+1) + "/" + numberOfPages + "\n";		
		System.out.println(details);
		displayContactListPage(contacts, limitPerPage, pageCounter);		
		while (choice == null || choice != 'B') {
			choice = null;
			String options = generatePageOptions(numberOfPages, pageCounter);
			System.out.println(options);
			while( choice == null || ( choice != 'B' && choice != '>' && choice != '<' ) ) {
				if (choice != null ) {
					System.out.println("Please enter a valid option");
				}
				System.out.print("-> ");
				Scanner keyboard = new Scanner(System.in);
				String input = keyboard.next();
				if(input.length() == 1) {
					choice = input.charAt(0);
				}
			}
			
			switch(choice) {
				case '<':
					if (numberOfPages > 1 && pageCounter == (numberOfPages-1) || numberOfPages > 1 && pageCounter > 0 && pageCounter < (numberOfPages-1)) {
						pageCounter--;
						System.out.println("Page " + (pageCounter+1) + "/" + numberOfPages + "\n");
						displayContactListPage(contacts, limitPerPage, pageCounter);
					}
					break;
				case '>':
					if (numberOfPages > 1 && pageCounter == 0 || numberOfPages > 1 && pageCounter > 0 && pageCounter < (numberOfPages-1) ) {
						pageCounter++;
						System.out.println("Page " + (pageCounter+1) + "/" + numberOfPages + "\n");
						displayContactListPage(contacts, limitPerPage, pageCounter);
					}
					break;
				case 'B':
					break;
			}
		}
		showMainMenu();
	}
	
	public static void displayContactListPage(List<Person> contacts, Integer limitPerPage, Integer pageCounter) {
		if (contacts.size() <= limitPerPage) {
			contacts.forEach((person)->System.out.println(person.getLastname() + " " + person.getFirstname()));;
		}
		else {
			if ( contacts.size() <= (pageCounter * limitPerPage + limitPerPage) ) {
				for (int i = (pageCounter * limitPerPage); i < contacts.size(); i++) {
					System.out.println(contacts.get(i).getLastname() + " " + contacts.get(i).getFirstname());
				}
			}
			else {
				for (int i = (pageCounter * limitPerPage); i < (pageCounter * limitPerPage + limitPerPage); i++) {
					System.out.println(contacts.get(i).getLastname() + " " + contacts.get(i).getFirstname());
				}
			}
		}
	}
	
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
	
	public static void addContact() {
		System.out.println("AddContact");
	}
	
	public static void main(String[] args) {
		
		//Initializing database
		initDb();
		
		showMainMenu();
		
	}

}
