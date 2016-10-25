package classficador;

import java.util.List;

public class Atributos {
	private List<String> genres = null;
	private int ocupation = 0 ;
	private int age = 0;
	private char gender = ' ';
	private int movieID = 0;
	public Atributos(List<String> genres , int ocupation , int age, char gender  ,int movieID ){
		this.genres = genres;
		this.ocupation = ocupation;
		this.age = age;
		this.gender = gender;
		this.movieID = movieID;
	}
	public List<String> getGenres() {
		return genres;
	}
	public void setGenres(List<String> genres) {
		this.genres = genres;
	}
	public int getOcupation() {
		return ocupation;
	}
	public void setOcupation(int ocupation) {
		this.ocupation = ocupation;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public char getGender() {
		return gender;
	}
	public void setGender(char gender) {
		this.gender = gender;
	}
	public int getMovieID() {
		return movieID;
	}
	public void setMovieID(int movieID) {
		this.movieID = movieID;
	}
}
