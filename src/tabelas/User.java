package tabelas;

public class User {
 	private int userID;
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	private char gender ;
	private int age;
	private int occupation;
	private int zipcode ;
	
	public char getGender() {
		return gender;
	}
	public void setGender(char gender) {
		this.gender = gender;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public int getOccupation() {
		return occupation;
	}
	public void setOccupation(int occupation) {
		this.occupation = occupation;
	}
	public int getZipcode() {
		return zipcode;
	}
	public void setZipcode(int zipcode) {
		this.zipcode = zipcode;
	}
}
