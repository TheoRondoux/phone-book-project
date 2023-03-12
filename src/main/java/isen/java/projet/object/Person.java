package isen.java.projet.object;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Person {
	
	private Integer id;
	private String lastname;
	private String firstname;
	private String nickname;
	private String phoneNumber;
	private String address;
	private String emailAddress;
	private LocalDate birthDate;
	
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
	
	public Person() {
		super();
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
	
	public String generatesVCard() {
		String vCard = "BEGIN:VCARD"
				     + "\nVERSION:4.0"
				     + "\nN:" + this.lastname + ";" + this.firstname + ";"
				     + "\nFN:" + this.firstname + " " + this.lastname
				     + "\nNICKNAME:" + this.nickname
				     + "\nTEL;TYPE=cell:" + this.phoneNumber
				     + "\nADR;TYPE=home:;;" + this.address
				     + "\nEMAIL:" + this.emailAddress
				     + "\nBDAY:" + this.birthDate.format(DateTimeFormatter.BASIC_ISO_DATE)
				     + "\nEND:VCARD";
		return vCard;
	}
	
}
