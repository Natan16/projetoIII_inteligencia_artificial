package classficador;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tabelas.*;



public class Classificador {
	public static List<Movie> movies;
	public static List<Rating> ratings;
	public static List<User> users;
	public static User me; //o usuário correspondente a mim 
	//minhas avaliações. A chave é a id do filme e o valor a o rating
	public static Map<Integer , Integer> myRatings = new HashMap<Integer , Integer>();
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args)  throws IOException {
		movies = new ArrayList<Movie>();
		ratings = new ArrayList<Rating>();
		users = new ArrayList<User>();
		User me = new User();
		Map<Integer , Integer> aPrioriRatings = new HashMap<Integer , Integer>();
		
		
		//preenchendo as informações sobre mim
		me.setAge(23);
		me.setGender('M');
		me.setOccupation(4);
		me.setZipcode("12228-461");
		//preenchendo as avaliações dos 10 filmes que eu já assiti
		int[] ids = {19,32,47,153,648,2571,2594,2628,2700,3409};
		myRatings.put(19, 4);
		myRatings.put(32, 2);
		myRatings.put(47, 5);
		myRatings.put(153,3);
		myRatings.put(648,3);
		myRatings.put(2571,5);
		myRatings.put(2594,5);
		myRatings.put(2628,2);
		myRatings.put(2700,4);
		myRatings.put(3409,1);
		
		leituraArmazenamento();
	           
        for (int movieID : ids){
			int classAPriori = aPriori(movieID);
			if ( classAPriori != 0){
				aPrioriRatings.put(movieID, classAPriori);
			}
		}
		
		int[][] matrizConfusaoAPriori = Comparador.matrizDeConfusao(aPrioriRatings , myRatings);
		Formatador.formatar(matrizConfusaoAPriori , "a priori");
		Formatador.formatar(matrizConfusaoAPriori, "arvore de decisão");
	 
        
	}
	//no nosso caso, padrão = 3 ( pode ser melhorado tomando como base as avalizações do usuário )
	//código postal pode ser considerado irrelevante pra evitar overfitting 
	public static int arvoreDecisao(List<Rating> exemplos , /*generos possíveis , ids possíveis (será ?) , 
	faixa etária , ocupação, código postal*/   int padrao ){
		
		
		return 0;
	}
	
	public static float entropia(List<Rating> exemplos){
		float[] p = {0,0,0,0,0};
		float sum_p = exemplos.size();
		float entropia = 0;
		for ( Rating rating : exemplos){
			p[rating.getRating() - 1] += 1;
		}
		for ( int i = 0 ; i < 5 ; i++){
			entropia += -(p[i]/sum_p)*Math.log10(p[i]/sum_p);
		}
		
		return entropia;
	}
	
	public static int ganho(){
		return 0;
	}
	
	public static int aPriori(int movieID){
		int soma = 0;
		int quantidade = 0;
		for(Rating rating : ratings ){
			if( rating.getMovieID() == movieID){
				soma += rating.getRating();
				quantidade++;
			}
		}
		if ( quantidade != 0) return soma/quantidade + (2*soma/quantidade)%2;
		return 0;
	}

	public static Movie getMovieById(int movieID){
		for (Movie movie : movies)
			if ( movie.getMovieID() == movieID) return movie;
		return null;
	}
	
	public static User getUserById(int userID){
		for (User user : users)
			if ( user.getUserID() == userID) return user;
		return null;
	}
	
	public static void leituraArmazenamento() throws NumberFormatException, IOException{
		DataInputStream inputMovies , inputRatings , inputUsers;
		//leitura e armazenamento de movies.dat
	    inputMovies = new DataInputStream(new FileInputStream("src/movies.dat"));
		while (inputMovies.available() > 0) {
			@SuppressWarnings("deprecation")
			String inputMoviesLine = inputMovies.readLine();
			String[] movieArguments = inputMoviesLine.split("::");
			Movie mMovie = new Movie();
			mMovie.setMovieID(Integer.parseInt(movieArguments[0]));
		    mMovie.setTitle(movieArguments[1]);
		    mMovie.setGenres(movieArguments[2].split("|"));
		    movies.add(mMovie);
		}            
        inputMovies.close();
        //leitura e armazenamento de ratings.dat
        inputRatings = new DataInputStream(new FileInputStream("src/ratings.dat"));
		while (inputRatings.available() > 0) {
			@SuppressWarnings("deprecation")
			String inputRatingsLine = inputRatings.readLine();
			String[] ratingArguments = inputRatingsLine.split("::");
			Rating mRating = new Rating();
			mRating.setUserID(Integer.parseInt(ratingArguments[0]));
			mRating.setMovieID(Integer.parseInt(ratingArguments[1]));
		    mRating.setRating(Integer.parseInt(ratingArguments[2]));
		    mRating.setTimestamp(Integer.parseInt(ratingArguments[3]));
			ratings.add(mRating);
		}            
        inputRatings.close();
        //leitura e armazenamento de users.dat
        inputUsers = new DataInputStream(new FileInputStream("src/users.dat"));
		while (inputUsers.available() > 0) {
			@SuppressWarnings("deprecation")
			String inputUsersLine = inputUsers.readLine();
			String[] userArguments = inputUsersLine.split("::");
			User mUser = new User();
			mUser.setUserID(Integer.parseInt(userArguments[0]));
			mUser.setGender(userArguments[1].charAt(0));
			mUser.setAge(Integer.parseInt(userArguments[2]));
			mUser.setOccupation(Integer.parseInt(userArguments[3]));
			mUser.setZipcode(userArguments[4]);
		    users.add(mUser);
		    //System.out.println(inputUsersLine);
		}            
        inputUsers.close();
	}
}
