package classficador;

import java.util.List;

public class Atributos {

	/*int[] ages = {1, 18 , 25, 35, 45, 50 , 56};
	char[]  genders = {'M','F'};
	int[] occupations = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
		*/
	private List<String> atributos;
	
/*	private int ocupation = 0 ;
	private int age = 0;
	private char gender = ' ';
	private int movieID = 0;*/
	public Atributos(List<String> atributos ){
		this.atributos = atributos;
		/*this.genres = genres;
		this.ocupation = ocupation;
		this.age = age;
		this.gender = gender;
		this.movieID = movieID;*/
	}
	public boolean ehVazio(){
		if(this.atributos.size() == 0)	
			return true;
		
		return false;
	}
	public List<String> getAtributos() {
		return atributos;
	}
	public void setAtributos(List<String> atributos) {
		this.atributos = atributos;
	}
	
	/*public List<String> getGenres() {
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
	}*/
}
