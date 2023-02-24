package m1s2.project.java.entities;

import java.time.LocalDate;

public class Person {
	
	private Integer id;
	private String lastname;
	private String firstname;
	private String nickname;
	private String phoneNumber;
	private String address;
	private String emailAddress;
	private LocalDate birthDate;
	
	public Person() {
		
	}
	
	public Person(Integer id, String lastname, String firstname, String nickname, String phoneNumber, String address,
			String emailAddress, LocalDate birthDate) {
		super();
		this.id = id;
		this.lastname = lastname;
		this.firstname = firstname;
		this.nickname = nickname;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.emailAddress = emailAddress;
		this.birthDate = birthDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}
	
	public void displayInfos() {
		String infos = this.firstname + " " + this.lastname + "\n"
					 + this.nickname + "\n"
					 + this.birthDate + "\n"
					 + "------------------------\n"
					 + "Phone number: " + this.phoneNumber + "\n"
					 + "Address: " + this.address + "\n"
					 + "Email: "+ this.emailAddress + "\n";
		System.out.println(infos);
	}
	

}
