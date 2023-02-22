package m1s2.project.java.daos;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import m1s2.project.java.entities.Person;

import org.junit.Before;
import org.junit.Test;

public class PersonDaoTestCase {
	private PersonDao personDao = new PersonDao();
	
	@Before
	public void initDb() throws Exception{
		Connection connection = DataSourceFactory.getDataSource().getConnection();
		Statement stmt = connection.createStatement();
		stmt.execute(				
				"CREATE TABLE IF NOT EXISTS person (\r\n"
				+ "  idperson INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\r\n" + "  lastname VARCHAR(45) NOT NULL,\r\n"
				+ "  firstname VARCHAR(45) NOT NULL,\r\n" + "  nickname VARCHAR(45) NOT NULL,\r\n" + "  phone_number VARCHAR(15) NULL,\r\n"
				+ "  address VARCHAR(200) NULL,\r\n" + "  email_address VARCHAR(150) NULL,\r\n"
				+ "  birth_date DATETIME NULL);");
		stmt.executeUpdate("DELETE FROM person");
		stmt.executeUpdate("INSERT INTO person(idperson, lastname, firstname, nickname, phone_number, address, email_address, birth_date) "
						 + "VALUES (1, 'Poledri', 'Axel', 'LeAsayas', '060102030405','Lille','ax.pol@gmail.com', '1900-01-01 12:00:00.000')");
		stmt.executeUpdate("INSERT INTO person(idperson, lastname, firstname, nickname, phone_number, address, email_address, birth_date) "
				 + "VALUES (2, 'Grincourt', 'Flavien', 'SauX', '060102030405','Lille','fl.gri@gmail.com', '1900-01-01 12:00:00.000')");
		stmt.executeUpdate("INSERT INTO person(idperson, lastname, firstname, nickname, phone_number, address, email_address, birth_date) "
				 + "VALUES (3, 'Vanduynslaeger', 'Victor', 'ReaKy', '060102030405','Lille','vi.van@gmail.com', '1900-01-01 12:00:00.000')");
		stmt.close();
		connection.close();
	}
	
	@Test
	public void shouldListPersons() {
		//WHEN
		List<Person> listOfPerson = personDao.listPersons();
		//THEN
		assertThat(listOfPerson).hasSize(3);
		assertThat(listOfPerson).extracting("id","lastname","firstname","nickname","phone_number","address","email_address","birth_date")
								.containsOnly(
										tuple(1, "Poledri", "Axel", "LeAsayas", "060102030405","Lille","ax.pol@gmail.com", LocalDate.of(1900, 01, 01)),
										tuple(2, "Grincourt", "Flavien", "SauX", "060102030405","Lille","fl.gri@gmail.com", LocalDate.of(1900, 01, 01)),
										tuple(3, "Vanduynslaeger", "Victor", "ReaKy", "060102030405","Lille","vi.van@gmail.com", LocalDate.of(1900, 01, 01))
								);
	}
}
