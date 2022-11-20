package ada.prac01;

import java.time.LocalDate;

public class Student {

	private String  firstName, lastName, idDoc;
	private LocalDate dob;

	public Student(String firstName, String lastName, String idDoc, LocalDate dob) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.idDoc = idDoc;
		this.dob = dob;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getIdDoc() {
		return idDoc;
	}

	public LocalDate getDob() {
		return dob;
	}
	public String csvFormat(){
		return idDoc + "," + lastName + "," + firstName +"\n";
	}
	@Override
	public String toString() {
		return "Student [firstName=" + firstName + ", lastName=" + lastName + ", dob=" + dob + ", idDoc=" + idDoc + "]";
	}
	
	
}
