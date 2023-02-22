package m1s2.project.java.daos;

import java.sql.Connection;
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
				String sqlQuery = "SELECT * FROM person";
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
}
