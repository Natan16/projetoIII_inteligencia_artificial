import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import tabelas.*;



public class Classificador {
	public static List<Movie> movies;
	public static List<Rating> ratings;
	public static List<User> users;
	
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args)  throws IOException {
		
	    DataInputStream inputMovies , inputRatings , inputUsers;
		//leitura e armazenamento de movies.dat
	    inputMovies = new DataInputStream(new FileInputStream("movies.dat"));
		while (inputMovies.available() > 0) {
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
        inputRatings = new DataInputStream(new FileInputStream("ratings.dat"));
		while (inputMovies.available() > 0) {
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
        inputUsers = new DataInputStream(new FileInputStream("users.dat"));
		while (inputMovies.available() > 0) {
			String inputUsersLine = inputUsers.readLine();
			String[] userArguments = inputUsersLine.split("::");
			User mUser = new User();
			mUser.setUserID(Integer.parseInt(userArguments[0]));
			mUser.setGender(userArguments[1].charAt(0));
			mUser.setAge(Integer.parseInt(userArguments[2]));
			mUser.setOccupation(Integer.parseInt(userArguments[3]));
			mUser.setZipcode(Integer.parseInt(userArguments[4]));
		    users.add(mUser);
		}            
        inputMovies.close();
        

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
