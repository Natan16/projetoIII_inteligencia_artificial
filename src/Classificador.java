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
}
