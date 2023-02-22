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
	
	public static void showMainMenu() {
		Character choice = null;
		System.out.println("Enter E");
		while (choice == null || choice != 'E') {
			Scanner keyboard = new Scanner(System.in);
			String input = keyboard.next();
			keyboard.nextLine();
			if(input.length() == 1) {
				choice = input.charAt(0);
			}
		}
	}
	
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
	
	public static void main(String[] args) {
		
		//Initializing database
		initDb();
		//Creating a Person Dao
		PersonDao personDao = new PersonDao();
		
		List<Person> contacts = personDao.listPersons();
		
		showMainMenu();
		
	}

}
