package m1s2.project.java.daos;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import m1s2.project.java.entities.Person;

public class PersonDao {
	public List<Person> listPersons(){
		List<Person> personsList = new ArrayList<>();
		try(Connection connection = DataSourceFactory.getDataSource().getConnection()){
			try (Statement statement = connection.createStatement()){
				String sqlQuery = "SELECT * FROM person ORDER BY lastname";
				try (ResultSet results = statement.executeQuery(sqlQuery)){
					while(results.next()) {
						Person person = new Person(results.getInt("idperson"),
												   results.getString("lastname"),
												   results.getString("firstname"),
												   results.getString("nickname"),
												   results.getString("phone_number"),
												   results.getString("address"),
												   results.getString("email_address"),
												   results.getDate("birth_date").toLocalDate());
						personsList.add(person);
					}
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return personsList;
	}
	
	public Person addPerson(Person person) {
		try( Connection connection = DataSourceFactory.getDataSource().getConnection()){
			String sqlQuery = "INSERT INTO person(lastname, firstname, nickname, phone_number, address, email_address, birth_date) VALUES (?,?,?,?,?,?,?)";
			try (PreparedStatement statement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)){
				statement.setString(1, person.getLastname());
				statement.setString(2, person.getFirstname());
				statement.setString(3, person.getNickname());
				statement.setString(4, person.getPhoneNumber());
				statement.setString(5, person.getAddress());
				statement.setString(6, person.getEmailAddress());
				statement.setDate(7, Date.valueOf(person.getBirthDate()));
				statement.executeUpdate();
				ResultSet ids = statement.getGeneratedKeys();
				if (ids.next()) {
					return new Person(ids.getInt(1), 
									  person.getLastname(), 
									  person.getFirstname(), 
									  person.getNickname(), 
									  person.getPhoneNumber(), 
									  person.getAddress(), 
									  person.getEmailAddress(), 
									  person.getBirthDate());
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
